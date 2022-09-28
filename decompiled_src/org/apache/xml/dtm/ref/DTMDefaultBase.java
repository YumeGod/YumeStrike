package org.apache.xml.dtm.ref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public abstract class DTMDefaultBase implements DTM {
   static final boolean JJK_DEBUG = false;
   public static final int ROOTNODE = 0;
   protected int m_size;
   protected SuballocatedIntVector m_exptype;
   protected SuballocatedIntVector m_firstch;
   protected SuballocatedIntVector m_nextsib;
   protected SuballocatedIntVector m_prevsib;
   protected SuballocatedIntVector m_parent;
   protected Vector m_namespaceDeclSets;
   protected SuballocatedIntVector m_namespaceDeclSetElements;
   protected int[][][] m_elemIndexes;
   public static final int DEFAULT_BLOCKSIZE = 512;
   public static final int DEFAULT_NUMBLOCKS = 32;
   public static final int DEFAULT_NUMBLOCKS_SMALL = 4;
   protected static final int NOTPROCESSED = -2;
   public DTMManager m_mgr;
   protected DTMManagerDefault m_mgrDefault;
   protected SuballocatedIntVector m_dtmIdent;
   protected String m_documentBaseURI;
   protected DTMWSFilter m_wsfilter;
   protected boolean m_shouldStripWS;
   protected BoolStack m_shouldStripWhitespaceStack;
   protected XMLStringFactory m_xstrf;
   protected ExpandedNameTable m_expandedNameTable;
   protected boolean m_indexing;
   protected DTMAxisTraverser[] m_traversers;
   private Vector m_namespaceLists;

   public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
   }

   public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable) {
      this.m_size = 0;
      this.m_namespaceDeclSets = null;
      this.m_namespaceDeclSetElements = null;
      this.m_mgrDefault = null;
      this.m_shouldStripWS = false;
      this.m_namespaceLists = null;
      byte numblocks;
      if (blocksize <= 64) {
         numblocks = 4;
         this.m_dtmIdent = new SuballocatedIntVector(4, 1);
      } else {
         numblocks = 32;
         this.m_dtmIdent = new SuballocatedIntVector(32);
      }

      this.m_exptype = new SuballocatedIntVector(blocksize, numblocks);
      this.m_firstch = new SuballocatedIntVector(blocksize, numblocks);
      this.m_nextsib = new SuballocatedIntVector(blocksize, numblocks);
      this.m_parent = new SuballocatedIntVector(blocksize, numblocks);
      if (usePrevsib) {
         this.m_prevsib = new SuballocatedIntVector(blocksize, numblocks);
      }

      this.m_mgr = mgr;
      if (mgr instanceof DTMManagerDefault) {
         this.m_mgrDefault = (DTMManagerDefault)mgr;
      }

      this.m_documentBaseURI = null != source ? source.getSystemId() : null;
      this.m_dtmIdent.setElementAt(dtmIdentity, 0);
      this.m_wsfilter = whiteSpaceFilter;
      this.m_xstrf = xstringfactory;
      this.m_indexing = doIndexing;
      if (doIndexing) {
         this.m_expandedNameTable = new ExpandedNameTable();
      } else {
         this.m_expandedNameTable = this.m_mgrDefault.getExpandedNameTable(this);
      }

      if (null != whiteSpaceFilter) {
         this.m_shouldStripWhitespaceStack = new BoolStack();
         this.pushShouldStripWhitespace(false);
      }

   }

   protected void ensureSizeOfIndex(int namespaceID, int LocalNameID) {
      if (null == this.m_elemIndexes) {
         this.m_elemIndexes = new int[namespaceID + 20][][];
      } else if (this.m_elemIndexes.length <= namespaceID) {
         int[][][] indexes = this.m_elemIndexes;
         this.m_elemIndexes = new int[namespaceID + 20][][];
         System.arraycopy(indexes, 0, this.m_elemIndexes, 0, indexes.length);
      }

      int[][] localNameIndex = this.m_elemIndexes[namespaceID];
      if (null == localNameIndex) {
         localNameIndex = new int[LocalNameID + 100][];
         this.m_elemIndexes[namespaceID] = localNameIndex;
      } else if (localNameIndex.length <= LocalNameID) {
         int[][] indexes = localNameIndex;
         localNameIndex = new int[LocalNameID + 100][];
         System.arraycopy(indexes, 0, localNameIndex, 0, indexes.length);
         this.m_elemIndexes[namespaceID] = localNameIndex;
      }

      int[] elemHandles = localNameIndex[LocalNameID];
      if (null == elemHandles) {
         elemHandles = new int[128];
         localNameIndex[LocalNameID] = elemHandles;
         elemHandles[0] = 1;
      } else if (elemHandles.length <= elemHandles[0] + 1) {
         int[] indexes = elemHandles;
         elemHandles = new int[elemHandles[0] + 1024];
         System.arraycopy(indexes, 0, elemHandles, 0, indexes.length);
         localNameIndex[LocalNameID] = elemHandles;
      }

   }

   protected void indexNode(int expandedTypeID, int identity) {
      ExpandedNameTable ent = this.m_expandedNameTable;
      short type = ent.getType(expandedTypeID);
      if (1 == type) {
         int namespaceID = ent.getNamespaceID(expandedTypeID);
         int localNameID = ent.getLocalNameID(expandedTypeID);
         this.ensureSizeOfIndex(namespaceID, localNameID);
         int[] index = this.m_elemIndexes[namespaceID][localNameID];
         index[index[0]] = identity;
         int var10002 = index[0]++;
      }

   }

   protected int findGTE(int[] list, int start, int len, int value) {
      int low = start;
      int high = start + (len - 1);
      int end = high;

      while(low <= high) {
         int mid = (low + high) / 2;
         int c = list[mid];
         if (c > value) {
            high = mid - 1;
         } else {
            if (c >= value) {
               return mid;
            }

            low = mid + 1;
         }
      }

      return low <= end && list[low] > value ? low : -1;
   }

   int findElementFromIndex(int nsIndex, int lnIndex, int firstPotential) {
      int[][][] indexes = this.m_elemIndexes;
      if (null != indexes && nsIndex < indexes.length) {
         int[][] lnIndexs = indexes[nsIndex];
         if (null != lnIndexs && lnIndex < lnIndexs.length) {
            int[] elems = lnIndexs[lnIndex];
            if (null != elems) {
               int pos = this.findGTE(elems, 1, elems[0], firstPotential);
               if (pos > -1) {
                  return elems[pos];
               }
            }
         }
      }

      return -2;
   }

   protected abstract int getNextNodeIdentity(int var1);

   protected abstract boolean nextNode();

   protected abstract int getNumberOfNodes();

   protected short _type(int identity) {
      int info = this._exptype(identity);
      return -1 != info ? this.m_expandedNameTable.getType(info) : -1;
   }

   protected int _exptype(int identity) {
      if (identity == -1) {
         return -1;
      } else {
         do {
            if (identity < this.m_size) {
               return this.m_exptype.elementAt(identity);
            }
         } while(this.nextNode() || identity < this.m_size);

         return -1;
      }
   }

   protected int _level(int identity) {
      while(true) {
         if (identity >= this.m_size) {
            boolean isMore = this.nextNode();
            if (isMore || identity < this.m_size) {
               continue;
            }

            return -1;
         }

         int i;
         for(i = 0; -1 != (identity = this._parent(identity)); ++i) {
         }

         return i;
      }
   }

   protected int _firstch(int identity) {
      int info = identity >= this.m_size ? -2 : this.m_firstch.elementAt(identity);

      boolean isMore;
      do {
         if (info != -2) {
            return info;
         }

         isMore = this.nextNode();
         if (identity >= this.m_size && !isMore) {
            return -1;
         }

         info = this.m_firstch.elementAt(identity);
      } while(info != -2 || isMore);

      return -1;
   }

   protected int _nextsib(int identity) {
      int info = identity >= this.m_size ? -2 : this.m_nextsib.elementAt(identity);

      boolean isMore;
      do {
         if (info != -2) {
            return info;
         }

         isMore = this.nextNode();
         if (identity >= this.m_size && !isMore) {
            return -1;
         }

         info = this.m_nextsib.elementAt(identity);
      } while(info != -2 || isMore);

      return -1;
   }

   protected int _prevsib(int identity) {
      if (identity < this.m_size) {
         return this.m_prevsib.elementAt(identity);
      } else {
         do {
            boolean isMore = this.nextNode();
            if (identity >= this.m_size && !isMore) {
               return -1;
            }
         } while(identity >= this.m_size);

         return this.m_prevsib.elementAt(identity);
      }
   }

   protected int _parent(int identity) {
      if (identity < this.m_size) {
         return this.m_parent.elementAt(identity);
      } else {
         do {
            boolean isMore = this.nextNode();
            if (identity >= this.m_size && !isMore) {
               return -1;
            }
         } while(identity >= this.m_size);

         return this.m_parent.elementAt(identity);
      }
   }

   public void dumpDTM(OutputStream os) {
      try {
         if (os == null) {
            File f = new File("DTMDump" + this.hashCode() + ".txt");
            System.err.println("Dumping... " + f.getAbsolutePath());
            os = new FileOutputStream(f);
         }

         PrintStream ps = new PrintStream((OutputStream)os);

         while(this.nextNode()) {
         }

         int nRecords = this.m_size;
         ps.println("Total nodes: " + nRecords);

         for(int index = 0; index < nRecords; ++index) {
            int i = this.makeNodeHandle(index);
            ps.println("=========== index=" + index + " handle=" + i + " ===========");
            ps.println("NodeName: " + this.getNodeName(i));
            ps.println("NodeNameX: " + this.getNodeNameX(i));
            ps.println("LocalName: " + this.getLocalName(i));
            ps.println("NamespaceURI: " + this.getNamespaceURI(i));
            ps.println("Prefix: " + this.getPrefix(i));
            int exTypeID = this._exptype(index);
            ps.println("Expanded Type ID: " + Integer.toHexString(exTypeID));
            int type = this._type(index);
            String typestring;
            switch (type) {
               case -1:
                  typestring = "NULL";
                  break;
               case 0:
               default:
                  typestring = "Unknown!";
                  break;
               case 1:
                  typestring = "ELEMENT_NODE";
                  break;
               case 2:
                  typestring = "ATTRIBUTE_NODE";
                  break;
               case 3:
                  typestring = "TEXT_NODE";
                  break;
               case 4:
                  typestring = "CDATA_SECTION_NODE";
                  break;
               case 5:
                  typestring = "ENTITY_REFERENCE_NODE";
                  break;
               case 6:
                  typestring = "ENTITY_NODE";
                  break;
               case 7:
                  typestring = "PROCESSING_INSTRUCTION_NODE";
                  break;
               case 8:
                  typestring = "COMMENT_NODE";
                  break;
               case 9:
                  typestring = "DOCUMENT_NODE";
                  break;
               case 10:
                  typestring = "DOCUMENT_NODE";
                  break;
               case 11:
                  typestring = "DOCUMENT_FRAGMENT_NODE";
                  break;
               case 12:
                  typestring = "NOTATION_NODE";
                  break;
               case 13:
                  typestring = "NAMESPACE_NODE";
            }

            ps.println("Type: " + typestring);
            int firstChild = this._firstch(index);
            if (-1 == firstChild) {
               ps.println("First child: DTM.NULL");
            } else if (-2 == firstChild) {
               ps.println("First child: NOTPROCESSED");
            } else {
               ps.println("First child: " + firstChild);
            }

            int nextSibling;
            if (this.m_prevsib != null) {
               nextSibling = this._prevsib(index);
               if (-1 == nextSibling) {
                  ps.println("Prev sibling: DTM.NULL");
               } else if (-2 == nextSibling) {
                  ps.println("Prev sibling: NOTPROCESSED");
               } else {
                  ps.println("Prev sibling: " + nextSibling);
               }
            }

            nextSibling = this._nextsib(index);
            if (-1 == nextSibling) {
               ps.println("Next sibling: DTM.NULL");
            } else if (-2 == nextSibling) {
               ps.println("Next sibling: NOTPROCESSED");
            } else {
               ps.println("Next sibling: " + nextSibling);
            }

            int parent = this._parent(index);
            if (-1 == parent) {
               ps.println("Parent: DTM.NULL");
            } else if (-2 == parent) {
               ps.println("Parent: NOTPROCESSED");
            } else {
               ps.println("Parent: " + parent);
            }

            int level = this._level(index);
            ps.println("Level: " + level);
            ps.println("Node Value: " + this.getNodeValue(i));
            ps.println("String Value: " + this.getStringValue(i));
         }

      } catch (IOException var13) {
         var13.printStackTrace(System.err);
         throw new RuntimeException(var13.getMessage());
      }
   }

   public String dumpNode(int nodeHandle) {
      if (nodeHandle == -1) {
         return "[null]";
      } else {
         String typestring;
         switch (this.getNodeType(nodeHandle)) {
            case -1:
               typestring = "null";
               break;
            case 0:
            default:
               typestring = "Unknown!";
               break;
            case 1:
               typestring = "ELEMENT";
               break;
            case 2:
               typestring = "ATTR";
               break;
            case 3:
               typestring = "TEXT";
               break;
            case 4:
               typestring = "CDATA";
               break;
            case 5:
               typestring = "ENT_REF";
               break;
            case 6:
               typestring = "ENTITY";
               break;
            case 7:
               typestring = "PI";
               break;
            case 8:
               typestring = "COMMENT";
               break;
            case 9:
               typestring = "DOC";
               break;
            case 10:
               typestring = "DOC_TYPE";
               break;
            case 11:
               typestring = "DOC_FRAG";
               break;
            case 12:
               typestring = "NOTATION";
               break;
            case 13:
               typestring = "NAMESPACE";
         }

         StringBuffer sb = new StringBuffer();
         sb.append("[" + nodeHandle + ": " + typestring + "(0x" + Integer.toHexString(this.getExpandedTypeID(nodeHandle)) + ") " + this.getNodeNameX(nodeHandle) + " {" + this.getNamespaceURI(nodeHandle) + "}" + "=\"" + this.getNodeValue(nodeHandle) + "\"]");
         return sb.toString();
      }
   }

   public void setFeature(String featureId, boolean state) {
   }

   public boolean hasChildNodes(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int firstChild = this._firstch(identity);
      return firstChild != -1;
   }

   public final int makeNodeHandle(int nodeIdentity) {
      return -1 == nodeIdentity ? -1 : this.m_dtmIdent.elementAt(nodeIdentity >>> 16) + (nodeIdentity & '\uffff');
   }

   public final int makeNodeIdentity(int nodeHandle) {
      if (-1 == nodeHandle) {
         return -1;
      } else {
         int whichDTMid;
         if (this.m_mgrDefault != null) {
            whichDTMid = nodeHandle >>> 16;
            return this.m_mgrDefault.m_dtms[whichDTMid] != this ? -1 : this.m_mgrDefault.m_dtm_offsets[whichDTMid] | nodeHandle & '\uffff';
         } else {
            whichDTMid = this.m_dtmIdent.indexOf(nodeHandle & -65536);
            return whichDTMid == -1 ? -1 : (whichDTMid << 16) + (nodeHandle & '\uffff');
         }
      }
   }

   public int getFirstChild(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int firstChild = this._firstch(identity);
      return this.makeNodeHandle(firstChild);
   }

   public int getTypedFirstChild(int nodeHandle, int nodeType) {
      int firstChild;
      if (nodeType < 14) {
         for(firstChild = this._firstch(this.makeNodeIdentity(nodeHandle)); firstChild != -1; firstChild = this._nextsib(firstChild)) {
            int eType = this._exptype(firstChild);
            if (eType == nodeType || eType >= 14 && this.m_expandedNameTable.getType(eType) == nodeType) {
               return this.makeNodeHandle(firstChild);
            }
         }
      } else {
         for(firstChild = this._firstch(this.makeNodeIdentity(nodeHandle)); firstChild != -1; firstChild = this._nextsib(firstChild)) {
            if (this._exptype(firstChild) == nodeType) {
               return this.makeNodeHandle(firstChild);
            }
         }
      }

      return -1;
   }

   public int getLastChild(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int child = this._firstch(identity);

      int lastChild;
      for(lastChild = -1; child != -1; child = this._nextsib(child)) {
         lastChild = child;
      }

      return this.makeNodeHandle(lastChild);
   }

   public abstract int getAttributeNode(int var1, String var2, String var3);

   public int getFirstAttribute(int nodeHandle) {
      int nodeID = this.makeNodeIdentity(nodeHandle);
      return this.makeNodeHandle(this.getFirstAttributeIdentity(nodeID));
   }

   protected int getFirstAttributeIdentity(int identity) {
      int type = this._type(identity);
      if (1 == type) {
         while(-1 != (identity = this.getNextNodeIdentity(identity))) {
            type = this._type(identity);
            if (type == 2) {
               return identity;
            }

            if (13 != type) {
               break;
            }
         }
      }

      return -1;
   }

   protected int getTypedAttribute(int nodeHandle, int attType) {
      int type = this.getNodeType(nodeHandle);
      if (1 == type) {
         int identity = this.makeNodeIdentity(nodeHandle);

         while(-1 != (identity = this.getNextNodeIdentity(identity))) {
            type = this._type(identity);
            if (type == 2) {
               if (this._exptype(identity) == attType) {
                  return this.makeNodeHandle(identity);
               }
            } else if (13 != type) {
               break;
            }
         }
      }

      return -1;
   }

   public int getNextSibling(int nodeHandle) {
      return nodeHandle == -1 ? -1 : this.makeNodeHandle(this._nextsib(this.makeNodeIdentity(nodeHandle)));
   }

   public int getTypedNextSibling(int nodeHandle, int nodeType) {
      if (nodeHandle == -1) {
         return -1;
      } else {
         int node = this.makeNodeIdentity(nodeHandle);

         int eType;
         while((node = this._nextsib(node)) != -1 && (eType = this._exptype(node)) != nodeType && this.m_expandedNameTable.getType(eType) != nodeType) {
         }

         return node == -1 ? -1 : this.makeNodeHandle(node);
      }
   }

   public int getPreviousSibling(int nodeHandle) {
      if (nodeHandle == -1) {
         return -1;
      } else if (this.m_prevsib != null) {
         return this.makeNodeHandle(this._prevsib(this.makeNodeIdentity(nodeHandle)));
      } else {
         int nodeID = this.makeNodeIdentity(nodeHandle);
         int parent = this._parent(nodeID);
         int node = this._firstch(parent);

         int result;
         for(result = -1; node != nodeID; node = this._nextsib(node)) {
            result = node;
         }

         return this.makeNodeHandle(result);
      }
   }

   public int getNextAttribute(int nodeHandle) {
      int nodeID = this.makeNodeIdentity(nodeHandle);
      return this._type(nodeID) == 2 ? this.makeNodeHandle(this.getNextAttributeIdentity(nodeID)) : -1;
   }

   protected int getNextAttributeIdentity(int identity) {
      while(true) {
         if (-1 != (identity = this.getNextNodeIdentity(identity))) {
            int type = this._type(identity);
            if (type == 2) {
               return identity;
            }

            if (type == 13) {
               continue;
            }
         }

         return -1;
      }
   }

   protected void declareNamespaceInContext(int elementNodeIndex, int namespaceNodeIndex) {
      SuballocatedIntVector nsList = null;
      int newEType;
      if (this.m_namespaceDeclSets == null) {
         this.m_namespaceDeclSetElements = new SuballocatedIntVector(32);
         this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
         this.m_namespaceDeclSets = new Vector();
         nsList = new SuballocatedIntVector(32);
         this.m_namespaceDeclSets.addElement(nsList);
      } else {
         newEType = this.m_namespaceDeclSetElements.size() - 1;
         if (newEType >= 0 && elementNodeIndex == this.m_namespaceDeclSetElements.elementAt(newEType)) {
            nsList = (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(newEType);
         }
      }

      int i;
      if (nsList == null) {
         this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
         SuballocatedIntVector inherited = this.findNamespaceContext(this._parent(elementNodeIndex));
         if (inherited != null) {
            i = inherited.size();
            nsList = new SuballocatedIntVector(Math.max(Math.min(i + 16, 2048), 32));

            for(int i = 0; i < i; ++i) {
               nsList.addElement(inherited.elementAt(i));
            }
         } else {
            nsList = new SuballocatedIntVector(32);
         }

         this.m_namespaceDeclSets.addElement(nsList);
      }

      newEType = this._exptype(namespaceNodeIndex);

      for(i = nsList.size() - 1; i >= 0; --i) {
         if (newEType == this.getExpandedTypeID(nsList.elementAt(i))) {
            nsList.setElementAt(this.makeNodeHandle(namespaceNodeIndex), i);
            return;
         }
      }

      nsList.addElement(this.makeNodeHandle(namespaceNodeIndex));
   }

   protected SuballocatedIntVector findNamespaceContext(int elementNodeIndex) {
      if (null != this.m_namespaceDeclSetElements) {
         int wouldBeAt = this.findInSortedSuballocatedIntVector(this.m_namespaceDeclSetElements, elementNodeIndex);
         if (wouldBeAt >= 0) {
            return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
         } else if (wouldBeAt == -1) {
            return null;
         } else {
            wouldBeAt = -1 - wouldBeAt;
            --wouldBeAt;
            int candidate = this.m_namespaceDeclSetElements.elementAt(wouldBeAt);
            int ancestor = this._parent(elementNodeIndex);
            if (wouldBeAt == 0 && candidate < ancestor) {
               int rootHandle = this.getDocumentRoot(this.makeNodeHandle(elementNodeIndex));
               int rootID = this.makeNodeIdentity(rootHandle);
               int uppermostNSCandidateID;
               if (this.getNodeType(rootHandle) == 9) {
                  int ch = this._firstch(rootID);
                  uppermostNSCandidateID = ch != -1 ? ch : rootID;
               } else {
                  uppermostNSCandidateID = rootID;
               }

               if (candidate == uppermostNSCandidateID) {
                  return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
               }
            }

            while(true) {
               while(wouldBeAt >= 0 && ancestor > 0) {
                  if (candidate == ancestor) {
                     return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
                  }

                  if (candidate < ancestor) {
                     while(true) {
                        ancestor = this._parent(ancestor);
                        if (candidate >= ancestor) {
                           break;
                        }
                     }
                  } else {
                     if (wouldBeAt <= 0) {
                        return null;
                     }

                     --wouldBeAt;
                     candidate = this.m_namespaceDeclSetElements.elementAt(wouldBeAt);
                  }
               }

               return null;
            }
         }
      } else {
         return null;
      }
   }

   protected int findInSortedSuballocatedIntVector(SuballocatedIntVector vector, int lookfor) {
      int i = 0;
      if (vector != null) {
         int first = 0;
         int last = vector.size() - 1;

         while(first <= last) {
            i = (first + last) / 2;
            int test = lookfor - vector.elementAt(i);
            if (test == 0) {
               return i;
            }

            if (test < 0) {
               last = i - 1;
            } else {
               first = i + 1;
            }
         }

         if (first > i) {
            i = first;
         }
      }

      return -1 - i;
   }

   public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
      int identity;
      if (inScope) {
         identity = this.makeNodeIdentity(nodeHandle);
         if (this._type(identity) == 1) {
            SuballocatedIntVector nsContext = this.findNamespaceContext(identity);
            return nsContext != null && nsContext.size() >= 1 ? nsContext.elementAt(0) : -1;
         } else {
            return -1;
         }
      } else {
         identity = this.makeNodeIdentity(nodeHandle);
         if (this._type(identity) != 1) {
            return -1;
         } else {
            while(-1 != (identity = this.getNextNodeIdentity(identity))) {
               int type = this._type(identity);
               if (type == 13) {
                  return this.makeNodeHandle(identity);
               }

               if (2 != type) {
                  break;
               }
            }

            return -1;
         }
      }
   }

   public int getNextNamespaceNode(int baseHandle, int nodeHandle, boolean inScope) {
      int type;
      if (inScope) {
         SuballocatedIntVector nsContext = this.findNamespaceContext(this.makeNodeIdentity(baseHandle));
         if (nsContext == null) {
            return -1;
         } else {
            type = 1 + nsContext.indexOf(nodeHandle);
            return type > 0 && type != nsContext.size() ? nsContext.elementAt(type) : -1;
         }
      } else {
         int identity = this.makeNodeIdentity(nodeHandle);

         while(-1 != (identity = this.getNextNodeIdentity(identity))) {
            type = this._type(identity);
            if (type == 13) {
               return this.makeNodeHandle(identity);
            }

            if (type != 2) {
               break;
            }
         }

         return -1;
      }
   }

   public int getParent(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      return identity > 0 ? this.makeNodeHandle(this._parent(identity)) : -1;
   }

   public int getDocument() {
      return this.m_dtmIdent.elementAt(0);
   }

   public int getOwnerDocument(int nodeHandle) {
      return 9 == this.getNodeType(nodeHandle) ? -1 : this.getDocumentRoot(nodeHandle);
   }

   public int getDocumentRoot(int nodeHandle) {
      return this.getDocument();
   }

   public abstract XMLString getStringValue(int var1);

   public int getStringValueChunkCount(int nodeHandle) {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
      return 0;
   }

   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
      return null;
   }

   public int getExpandedTypeID(int nodeHandle) {
      int id = this.makeNodeIdentity(nodeHandle);
      return id == -1 ? -1 : this._exptype(id);
   }

   public int getExpandedTypeID(String namespace, String localName, int type) {
      ExpandedNameTable ent = this.m_expandedNameTable;
      return ent.getExpandedTypeID(namespace, localName, type);
   }

   public String getLocalNameFromExpandedNameID(int expandedNameID) {
      return this.m_expandedNameTable.getLocalName(expandedNameID);
   }

   public String getNamespaceFromExpandedNameID(int expandedNameID) {
      return this.m_expandedNameTable.getNamespace(expandedNameID);
   }

   public int getNamespaceType(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      int expandedNameID = this._exptype(identity);
      return this.m_expandedNameTable.getNamespaceID(expandedNameID);
   }

   public abstract String getNodeName(int var1);

   public String getNodeNameX(int nodeHandle) {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
      return null;
   }

   public abstract String getLocalName(int var1);

   public abstract String getPrefix(int var1);

   public abstract String getNamespaceURI(int var1);

   public abstract String getNodeValue(int var1);

   public short getNodeType(int nodeHandle) {
      return nodeHandle == -1 ? -1 : this.m_expandedNameTable.getType(this._exptype(this.makeNodeIdentity(nodeHandle)));
   }

   public short getLevel(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      return (short)(this._level(identity) + 1);
   }

   public int getNodeIdent(int nodeHandle) {
      return this.makeNodeIdentity(nodeHandle);
   }

   public int getNodeHandle(int nodeId) {
      return this.makeNodeHandle(nodeId);
   }

   public boolean isSupported(String feature, String version) {
      return false;
   }

   public String getDocumentBaseURI() {
      return this.m_documentBaseURI;
   }

   public void setDocumentBaseURI(String baseURI) {
      this.m_documentBaseURI = baseURI;
   }

   public String getDocumentSystemIdentifier(int nodeHandle) {
      return this.m_documentBaseURI;
   }

   public String getDocumentEncoding(int nodeHandle) {
      return "UTF-8";
   }

   public String getDocumentStandalone(int nodeHandle) {
      return null;
   }

   public String getDocumentVersion(int documentHandle) {
      return null;
   }

   public boolean getDocumentAllDeclarationsProcessed() {
      return true;
   }

   public abstract String getDocumentTypeDeclarationSystemIdentifier();

   public abstract String getDocumentTypeDeclarationPublicIdentifier();

   public abstract int getElementById(String var1);

   public abstract String getUnparsedEntityURI(String var1);

   public boolean supportsPreStripping() {
      return true;
   }

   public boolean isNodeAfter(int nodeHandle1, int nodeHandle2) {
      int index1 = this.makeNodeIdentity(nodeHandle1);
      int index2 = this.makeNodeIdentity(nodeHandle2);
      return index1 != -1 && index2 != -1 && index1 <= index2;
   }

   public boolean isCharacterElementContentWhitespace(int nodeHandle) {
      return false;
   }

   public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
      return true;
   }

   public abstract boolean isAttributeSpecified(int var1);

   public abstract void dispatchCharactersEvents(int var1, ContentHandler var2, boolean var3) throws SAXException;

   public abstract void dispatchToEvents(int var1, ContentHandler var2) throws SAXException;

   public Node getNode(int nodeHandle) {
      return new DTMNodeProxy(this, nodeHandle);
   }

   public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
   }

   public void appendTextChild(String str) {
      this.error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", (Object[])null));
   }

   protected void error(String msg) {
      throw new DTMException(msg);
   }

   protected boolean getShouldStripWhitespace() {
      return this.m_shouldStripWS;
   }

   protected void pushShouldStripWhitespace(boolean shouldStrip) {
      this.m_shouldStripWS = shouldStrip;
      if (null != this.m_shouldStripWhitespaceStack) {
         this.m_shouldStripWhitespaceStack.push(shouldStrip);
      }

   }

   protected void popShouldStripWhitespace() {
      if (null != this.m_shouldStripWhitespaceStack) {
         this.m_shouldStripWS = this.m_shouldStripWhitespaceStack.popAndTop();
      }

   }

   protected void setShouldStripWhitespace(boolean shouldStrip) {
      this.m_shouldStripWS = shouldStrip;
      if (null != this.m_shouldStripWhitespaceStack) {
         this.m_shouldStripWhitespaceStack.setTop(shouldStrip);
      }

   }

   public void documentRegistration() {
   }

   public void documentRelease() {
   }

   public void migrateTo(DTMManager mgr) {
      this.m_mgr = mgr;
      if (mgr instanceof DTMManagerDefault) {
         this.m_mgrDefault = (DTMManagerDefault)mgr;
      }

   }

   public DTMManager getManager() {
      return this.m_mgr;
   }

   public SuballocatedIntVector getDTMIDs() {
      return this.m_mgr == null ? null : this.m_dtmIdent;
   }

   public abstract SourceLocator getSourceLocatorFor(int var1);

   public abstract DeclHandler getDeclHandler();

   public abstract ErrorHandler getErrorHandler();

   public abstract DTDHandler getDTDHandler();

   public abstract EntityResolver getEntityResolver();

   public abstract LexicalHandler getLexicalHandler();

   public abstract ContentHandler getContentHandler();

   public abstract boolean needsTwoThreads();

   public abstract DTMAxisIterator getTypedAxisIterator(int var1, int var2);

   public abstract DTMAxisIterator getAxisIterator(int var1);

   public abstract DTMAxisTraverser getAxisTraverser(int var1);

   public abstract void setProperty(String var1, Object var2);
}
