package org.apache.xalan.lib.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class DTMDocument extends DTMDefaultBaseIterators {
   private boolean DEBUG = false;
   protected static final String S_NAMESPACE = "http://xml.apache.org/xalan/SQLExtension";
   protected static final String S_ATTRIB_NOT_SUPPORTED = "Not Supported";
   protected static final String S_ISTRUE = "true";
   protected static final String S_ISFALSE = "false";
   protected static final String S_DOCUMENT = "#root";
   protected static final String S_TEXT_NODE = "#text";
   protected static final String S_ELEMENT_NODE = "#element";
   protected int m_Document_TypeID = 0;
   protected int m_TextNode_TypeID = 0;
   protected ObjectArray m_ObjectArray = new ObjectArray();
   protected SuballocatedIntVector m_attribute = new SuballocatedIntVector(512);
   protected int m_DocumentIdx;

   public DTMDocument(DTMManager mgr, int ident) {
      super(mgr, (Source)null, ident, (DTMWSFilter)null, mgr.getXMLStringFactory(), true);
   }

   private int allocateNodeObject(Object o) {
      ++super.m_size;
      return this.m_ObjectArray.append(o);
   }

   protected int addElementWithData(Object o, int level, int extendedType, int parent, int prevsib) {
      int elementIdx = this.addElement(level, extendedType, parent, prevsib);
      int data = this.allocateNodeObject(o);
      super.m_firstch.setElementAt(data, elementIdx);
      super.m_exptype.setElementAt(this.m_TextNode_TypeID, data);
      super.m_parent.setElementAt(elementIdx, data);
      super.m_prevsib.setElementAt(-1, data);
      super.m_nextsib.setElementAt(-1, data);
      this.m_attribute.setElementAt(-1, data);
      super.m_firstch.setElementAt(-1, data);
      return elementIdx;
   }

   protected int addElement(int level, int extendedType, int parent, int prevsib) {
      int node = -1;

      try {
         node = this.allocateNodeObject("#element");
         super.m_exptype.setElementAt(extendedType, node);
         super.m_nextsib.setElementAt(-1, node);
         super.m_prevsib.setElementAt(prevsib, node);
         super.m_parent.setElementAt(parent, node);
         super.m_firstch.setElementAt(-1, node);
         this.m_attribute.setElementAt(-1, node);
         if (prevsib != -1) {
            if (super.m_nextsib.elementAt(prevsib) != -1) {
               super.m_nextsib.setElementAt(super.m_nextsib.elementAt(prevsib), node);
            }

            super.m_nextsib.setElementAt(node, prevsib);
         }

         if (parent != -1 && super.m_prevsib.elementAt(node) == -1) {
            super.m_firstch.setElementAt(node, parent);
         }
      } catch (Exception var7) {
         this.error("Error in addElement: " + var7.getMessage());
      }

      return node;
   }

   protected int addAttributeToNode(Object o, int extendedType, int pnode) {
      int attrib = -1;
      int prevsib = true;
      int lastattrib = true;
      int value = true;

      try {
         attrib = this.allocateNodeObject(o);
         this.m_attribute.setElementAt(-1, attrib);
         super.m_exptype.setElementAt(extendedType, attrib);
         super.m_nextsib.setElementAt(-1, attrib);
         super.m_prevsib.setElementAt(-1, attrib);
         super.m_parent.setElementAt(pnode, attrib);
         super.m_firstch.setElementAt(-1, attrib);
         if (this.m_attribute.elementAt(pnode) != -1) {
            int lastattrib = this.m_attribute.elementAt(pnode);
            super.m_nextsib.setElementAt(lastattrib, attrib);
            super.m_prevsib.setElementAt(attrib, lastattrib);
         }

         this.m_attribute.setElementAt(attrib, pnode);
      } catch (Exception var9) {
         this.error("Error in addAttributeToNode: " + var9.getMessage());
      }

      return attrib;
   }

   protected void cloneAttributeFromNode(int toNode, int fromNode) {
      try {
         if (this.m_attribute.elementAt(toNode) != -1) {
            this.error("Cloneing Attributes, where from Node already had addtibures assigned");
         }

         this.m_attribute.setElementAt(this.m_attribute.elementAt(fromNode), toNode);
      } catch (Exception var4) {
         this.error("Cloning attributes");
      }

   }

   public int getFirstAttribute(int parm1) {
      if (this.DEBUG) {
         System.out.println("getFirstAttribute(" + parm1 + ")");
      }

      int nodeIdx = this.makeNodeIdentity(parm1);
      if (nodeIdx != -1) {
         int attribIdx = this.m_attribute.elementAt(nodeIdx);
         return this.makeNodeHandle(attribIdx);
      } else {
         return -1;
      }
   }

   public String getNodeValue(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNodeValue(" + parm1 + ")");
      }

      try {
         Object o = this.m_ObjectArray.getAt(this.makeNodeIdentity(parm1));
         return o != null && o != "#element" ? o.toString() : "";
      } catch (Exception var3) {
         this.error("Getting String Value");
         return null;
      }
   }

   public XMLString getStringValue(int nodeHandle) {
      int nodeIdx = this.makeNodeIdentity(nodeHandle);
      if (this.DEBUG) {
         System.out.println("getStringValue(" + nodeIdx + ")");
      }

      Object o = this.m_ObjectArray.getAt(nodeIdx);
      if (o == "#element") {
         FastStringBuffer buf = StringBufferPool.get();

         String s;
         try {
            this.getNodeData(nodeIdx, buf);
            s = buf.length() > 0 ? buf.toString() : "";
         } finally {
            StringBufferPool.free(buf);
         }

         return super.m_xstrf.newstr(s);
      } else {
         return o != null ? super.m_xstrf.newstr(o.toString()) : super.m_xstrf.emptystr();
      }
   }

   protected void getNodeData(int nodeIdx, FastStringBuffer buf) {
      for(int child = this._firstch(nodeIdx); child != -1; child = this._nextsib(child)) {
         Object o = this.m_ObjectArray.getAt(child);
         if (o == "#element") {
            this.getNodeData(child, buf);
         } else if (o != null) {
            buf.append(o.toString());
         }
      }

   }

   public int getNextAttribute(int parm1) {
      int nodeIdx = this.makeNodeIdentity(parm1);
      if (this.DEBUG) {
         System.out.println("getNextAttribute(" + nodeIdx + ")");
      }

      return nodeIdx != -1 ? this.makeNodeHandle(super.m_nextsib.elementAt(nodeIdx)) : -1;
   }

   protected int getNumberOfNodes() {
      if (this.DEBUG) {
         System.out.println("getNumberOfNodes()");
      }

      return super.m_size;
   }

   protected boolean nextNode() {
      if (this.DEBUG) {
         System.out.println("nextNode()");
      }

      return false;
   }

   protected void createExpandedNameTable() {
      this.m_Document_TypeID = super.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "#root", 9);
      this.m_TextNode_TypeID = super.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "#text", 3);
   }

   public void dumpDTM() {
      try {
         File f = new File("DTMDump.txt");
         System.err.println("Dumping... " + f.getAbsolutePath());
         PrintStream ps = new PrintStream(new FileOutputStream(f));

         while(this.nextNode()) {
         }

         int nRecords = super.m_size;
         ps.println("Total nodes: " + nRecords);

         for(int i = 0; i < nRecords; ++i) {
            ps.println("=========== " + i + " ===========");
            ps.println("NodeName: " + this.getNodeName(this.makeNodeHandle(i)));
            ps.println("NodeNameX: " + this.getNodeNameX(this.makeNodeHandle(i)));
            ps.println("LocalName: " + this.getLocalName(this.makeNodeHandle(i)));
            ps.println("NamespaceURI: " + this.getNamespaceURI(this.makeNodeHandle(i)));
            ps.println("Prefix: " + this.getPrefix(this.makeNodeHandle(i)));
            int exTypeID = this.getExpandedTypeID(this.makeNodeHandle(i));
            ps.println("Expanded Type ID: " + Integer.toHexString(exTypeID));
            int type = this.getNodeType(this.makeNodeHandle(i));
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
            int firstChild = this._firstch(i);
            if (-1 == firstChild) {
               ps.println("First child: DTM.NULL");
            } else if (-2 == firstChild) {
               ps.println("First child: NOTPROCESSED");
            } else {
               ps.println("First child: " + firstChild);
            }

            int prevSibling = this._prevsib(i);
            if (-1 == prevSibling) {
               ps.println("Prev sibling: DTM.NULL");
            } else if (-2 == prevSibling) {
               ps.println("Prev sibling: NOTPROCESSED");
            } else {
               ps.println("Prev sibling: " + prevSibling);
            }

            int nextSibling = this._nextsib(i);
            if (-1 == nextSibling) {
               ps.println("Next sibling: DTM.NULL");
            } else if (-2 == nextSibling) {
               ps.println("Next sibling: NOTPROCESSED");
            } else {
               ps.println("Next sibling: " + nextSibling);
            }

            int parent = this._parent(i);
            if (-1 == parent) {
               ps.println("Parent: DTM.NULL");
            } else if (-2 == parent) {
               ps.println("Parent: NOTPROCESSED");
            } else {
               ps.println("Parent: " + parent);
            }

            int level = this._level(i);
            ps.println("Level: " + level);
            ps.println("Node Value: " + this.getNodeValue(i));
            ps.println("String Value: " + this.getStringValue(i));
            ps.println("First Attribute Node: " + this.m_attribute.elementAt(i));
         }

      } catch (IOException var13) {
         var13.printStackTrace(System.err);
         throw new RuntimeException(var13.getMessage());
      }
   }

   protected static void dispatchNodeData(Node node, ContentHandler ch, int depth) throws SAXException {
      switch (node.getNodeType()) {
         case 1:
         case 9:
         case 11:
            for(Node child = node.getFirstChild(); null != child; child = child.getNextSibling()) {
               dispatchNodeData(child, ch, depth + 1);
            }
         case 5:
         case 6:
         case 10:
         default:
            break;
         case 7:
         case 8:
            if (0 != depth) {
               break;
            }
         case 2:
         case 3:
         case 4:
            String str = node.getNodeValue();
            if (ch instanceof CharacterNodeHandler) {
               ((CharacterNodeHandler)ch).characters(node);
            } else {
               ch.characters(str.toCharArray(), 0, str.length());
            }
      }

   }

   public void setProperty(String property, Object value) {
   }

   public SourceLocator getSourceLocatorFor(int node) {
      return null;
   }

   protected int getNextNodeIdentity(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNextNodeIdenty(" + parm1 + ")");
      }

      return -1;
   }

   public int getAttributeNode(int parm1, String parm2, String parm3) {
      if (this.DEBUG) {
         System.out.println("getAttributeNode(" + parm1 + "," + parm2 + "," + parm3 + ")");
      }

      return -1;
   }

   public String getLocalName(int parm1) {
      int exID = this.getExpandedTypeID(parm1);
      if (this.DEBUG) {
         this.DEBUG = false;
         System.out.print("getLocalName(" + parm1 + ") -> ");
         System.out.println("..." + this.getLocalNameFromExpandedNameID(exID));
         this.DEBUG = true;
      }

      return this.getLocalNameFromExpandedNameID(exID);
   }

   public String getNodeName(int parm1) {
      int exID = this.getExpandedTypeID(parm1);
      if (this.DEBUG) {
         this.DEBUG = false;
         System.out.print("getLocalName(" + parm1 + ") -> ");
         System.out.println("..." + this.getLocalNameFromExpandedNameID(exID));
         this.DEBUG = true;
      }

      return this.getLocalNameFromExpandedNameID(exID);
   }

   public boolean isAttributeSpecified(int parm1) {
      if (this.DEBUG) {
         System.out.println("isAttributeSpecified(" + parm1 + ")");
      }

      return false;
   }

   public String getUnparsedEntityURI(String parm1) {
      if (this.DEBUG) {
         System.out.println("getUnparsedEntityURI(" + parm1 + ")");
      }

      return "";
   }

   public DTDHandler getDTDHandler() {
      if (this.DEBUG) {
         System.out.println("getDTDHandler()");
      }

      return null;
   }

   public String getPrefix(int parm1) {
      if (this.DEBUG) {
         System.out.println("getPrefix(" + parm1 + ")");
      }

      return "";
   }

   public EntityResolver getEntityResolver() {
      if (this.DEBUG) {
         System.out.println("getEntityResolver()");
      }

      return null;
   }

   public String getDocumentTypeDeclarationPublicIdentifier() {
      if (this.DEBUG) {
         System.out.println("get_DTD_PubId()");
      }

      return "";
   }

   public LexicalHandler getLexicalHandler() {
      if (this.DEBUG) {
         System.out.println("getLexicalHandler()");
      }

      return null;
   }

   public boolean needsTwoThreads() {
      if (this.DEBUG) {
         System.out.println("needsTwoThreads()");
      }

      return false;
   }

   public ContentHandler getContentHandler() {
      if (this.DEBUG) {
         System.out.println("getContentHandler()");
      }

      return null;
   }

   public void dispatchToEvents(int parm1, ContentHandler parm2) throws SAXException {
      if (this.DEBUG) {
         System.out.println("dispathcToEvents(" + parm1 + "," + parm2 + ")");
      }

   }

   public String getNamespaceURI(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNamespaceURI(" + parm1 + ")");
      }

      return "";
   }

   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
      if (this.DEBUG) {
         System.out.println("dispatchCharacterEvents(" + nodeHandle + "," + ch + "," + normalize + ")");
      }

      if (normalize) {
         XMLString str = this.getStringValue(nodeHandle);
         str = str.fixWhiteSpace(true, true, false);
         str.dispatchCharactersEvents(ch);
      } else {
         this.getNodeType(nodeHandle);
         Node node = this.getNode(nodeHandle);
         dispatchNodeData(node, ch, 0);
      }

   }

   public boolean supportsPreStripping() {
      if (this.DEBUG) {
         System.out.println("supportsPreStripping()");
      }

      return super.supportsPreStripping();
   }

   protected int _exptype(int parm1) {
      if (this.DEBUG) {
         System.out.println("_exptype(" + parm1 + ")");
      }

      return super._exptype(parm1);
   }

   protected SuballocatedIntVector findNamespaceContext(int parm1) {
      if (this.DEBUG) {
         System.out.println("SuballocatedIntVector(" + parm1 + ")");
      }

      return super.findNamespaceContext(parm1);
   }

   protected int _prevsib(int parm1) {
      if (this.DEBUG) {
         System.out.println("_prevsib(" + parm1 + ")");
      }

      return super._prevsib(parm1);
   }

   protected short _type(int parm1) {
      if (this.DEBUG) {
         System.out.println("_type(" + parm1 + ")");
      }

      return super._type(parm1);
   }

   public Node getNode(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNode(" + parm1 + ")");
      }

      return super.getNode(parm1);
   }

   public int getPreviousSibling(int parm1) {
      if (this.DEBUG) {
         System.out.println("getPrevSib(" + parm1 + ")");
      }

      return super.getPreviousSibling(parm1);
   }

   public String getDocumentStandalone(int parm1) {
      if (this.DEBUG) {
         System.out.println("getDOcStandAlone(" + parm1 + ")");
      }

      return super.getDocumentStandalone(parm1);
   }

   public String getNodeNameX(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNodeNameX(" + parm1 + ")");
      }

      return this.getNodeName(parm1);
   }

   public void setFeature(String parm1, boolean parm2) {
      if (this.DEBUG) {
         System.out.println("setFeature(" + parm1 + "," + parm2 + ")");
      }

      super.setFeature(parm1, parm2);
   }

   protected int _parent(int parm1) {
      if (this.DEBUG) {
         System.out.println("_parent(" + parm1 + ")");
      }

      return super._parent(parm1);
   }

   protected void indexNode(int parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("indexNode(" + parm1 + "," + parm2 + ")");
      }

      super.indexNode(parm1, parm2);
   }

   protected boolean getShouldStripWhitespace() {
      if (this.DEBUG) {
         System.out.println("getShouldStripWS()");
      }

      return super.getShouldStripWhitespace();
   }

   protected void popShouldStripWhitespace() {
      if (this.DEBUG) {
         System.out.println("popShouldStripWS()");
      }

      super.popShouldStripWhitespace();
   }

   public boolean isNodeAfter(int parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("isNodeAfter(" + parm1 + "," + parm2 + ")");
      }

      return super.isNodeAfter(parm1, parm2);
   }

   public int getNamespaceType(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNamespaceType(" + parm1 + ")");
      }

      return super.getNamespaceType(parm1);
   }

   protected int _level(int parm1) {
      if (this.DEBUG) {
         System.out.println("_level(" + parm1 + ")");
      }

      return super._level(parm1);
   }

   protected void pushShouldStripWhitespace(boolean parm1) {
      if (this.DEBUG) {
         System.out.println("push_ShouldStripWS(" + parm1 + ")");
      }

      super.pushShouldStripWhitespace(parm1);
   }

   public String getDocumentVersion(int parm1) {
      if (this.DEBUG) {
         System.out.println("getDocVer(" + parm1 + ")");
      }

      return super.getDocumentVersion(parm1);
   }

   public boolean isSupported(String parm1, String parm2) {
      if (this.DEBUG) {
         System.out.println("isSupported(" + parm1 + "," + parm2 + ")");
      }

      return super.isSupported(parm1, parm2);
   }

   protected void setShouldStripWhitespace(boolean parm1) {
      if (this.DEBUG) {
         System.out.println("set_ShouldStripWS(" + parm1 + ")");
      }

      super.setShouldStripWhitespace(parm1);
   }

   protected void ensureSizeOfIndex(int parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("ensureSizeOfIndex(" + parm1 + "," + parm2 + ")");
      }

      super.ensureSizeOfIndex(parm1, parm2);
   }

   protected void ensureSize(int parm1) {
      if (this.DEBUG) {
         System.out.println("ensureSize(" + parm1 + ")");
      }

   }

   public String getDocumentEncoding(int parm1) {
      if (this.DEBUG) {
         System.out.println("getDocumentEncoding(" + parm1 + ")");
      }

      return super.getDocumentEncoding(parm1);
   }

   public void appendChild(int parm1, boolean parm2, boolean parm3) {
      if (this.DEBUG) {
         System.out.println("appendChild(" + parm1 + "," + parm2 + "," + parm3 + ")");
      }

      super.appendChild(parm1, parm2, parm3);
   }

   public short getLevel(int parm1) {
      if (this.DEBUG) {
         System.out.println("getLevel(" + parm1 + ")");
      }

      return super.getLevel(parm1);
   }

   public String getDocumentBaseURI() {
      if (this.DEBUG) {
         System.out.println("getDocBaseURI()");
      }

      return super.getDocumentBaseURI();
   }

   public int getNextNamespaceNode(int parm1, int parm2, boolean parm3) {
      if (this.DEBUG) {
         System.out.println("getNextNamesapceNode(" + parm1 + "," + parm2 + "," + parm3 + ")");
      }

      return super.getNextNamespaceNode(parm1, parm2, parm3);
   }

   public void appendTextChild(String parm1) {
      if (this.DEBUG) {
         System.out.println("appendTextChild(" + parm1 + ")");
      }

      super.appendTextChild(parm1);
   }

   protected int findGTE(int[] parm1, int parm2, int parm3, int parm4) {
      if (this.DEBUG) {
         System.out.println("findGTE(" + parm1 + "," + parm2 + "," + parm3 + ")");
      }

      return super.findGTE(parm1, parm2, parm3, parm4);
   }

   public int getFirstNamespaceNode(int parm1, boolean parm2) {
      if (this.DEBUG) {
         System.out.println("getFirstNamespaceNode()");
      }

      return super.getFirstNamespaceNode(parm1, parm2);
   }

   public int getStringValueChunkCount(int parm1) {
      if (this.DEBUG) {
         System.out.println("getStringChunkCount(" + parm1 + ")");
      }

      return super.getStringValueChunkCount(parm1);
   }

   public int getLastChild(int parm1) {
      if (this.DEBUG) {
         System.out.println("getLastChild(" + parm1 + ")");
      }

      return super.getLastChild(parm1);
   }

   public boolean hasChildNodes(int parm1) {
      if (this.DEBUG) {
         System.out.println("hasChildNodes(" + parm1 + ")");
      }

      return super.hasChildNodes(parm1);
   }

   public short getNodeType(int parm1) {
      if (this.DEBUG) {
         this.DEBUG = false;
         System.out.print("getNodeType(" + parm1 + ") ");
         int exID = this.getExpandedTypeID(parm1);
         String name = this.getLocalNameFromExpandedNameID(exID);
         System.out.println(".. Node name [" + name + "]" + "[" + this.getNodeType(parm1) + "]");
         this.DEBUG = true;
      }

      return super.getNodeType(parm1);
   }

   public boolean isCharacterElementContentWhitespace(int parm1) {
      if (this.DEBUG) {
         System.out.println("isCharacterElementContentWhitespace(" + parm1 + ")");
      }

      return super.isCharacterElementContentWhitespace(parm1);
   }

   public int getFirstChild(int parm1) {
      if (this.DEBUG) {
         System.out.println("getFirstChild(" + parm1 + ")");
      }

      return super.getFirstChild(parm1);
   }

   public String getDocumentSystemIdentifier(int parm1) {
      if (this.DEBUG) {
         System.out.println("getDocSysID(" + parm1 + ")");
      }

      return super.getDocumentSystemIdentifier(parm1);
   }

   protected void declareNamespaceInContext(int parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("declareNamespaceContext(" + parm1 + "," + parm2 + ")");
      }

      super.declareNamespaceInContext(parm1, parm2);
   }

   public String getNamespaceFromExpandedNameID(int parm1) {
      if (this.DEBUG) {
         this.DEBUG = false;
         System.out.print("getNamespaceFromExpandedNameID(" + parm1 + ")");
         System.out.println("..." + super.getNamespaceFromExpandedNameID(parm1));
         this.DEBUG = true;
      }

      return super.getNamespaceFromExpandedNameID(parm1);
   }

   public String getLocalNameFromExpandedNameID(int parm1) {
      if (this.DEBUG) {
         this.DEBUG = false;
         System.out.print("getLocalNameFromExpandedNameID(" + parm1 + ")");
         System.out.println("..." + super.getLocalNameFromExpandedNameID(parm1));
         this.DEBUG = true;
      }

      return super.getLocalNameFromExpandedNameID(parm1);
   }

   public int getExpandedTypeID(int parm1) {
      if (this.DEBUG) {
         System.out.println("getExpandedTypeID(" + parm1 + ")");
      }

      return super.getExpandedTypeID(parm1);
   }

   public int getDocument() {
      if (this.DEBUG) {
         System.out.println("getDocument()");
      }

      return super.getDocument();
   }

   protected int findInSortedSuballocatedIntVector(SuballocatedIntVector parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("findInSortedSubAlloctedVector(" + parm1 + "," + parm2 + ")");
      }

      return super.findInSortedSuballocatedIntVector(parm1, parm2);
   }

   public boolean isDocumentAllDeclarationsProcessed(int parm1) {
      if (this.DEBUG) {
         System.out.println("isDocumentAllDeclProc(" + parm1 + ")");
      }

      return super.isDocumentAllDeclarationsProcessed(parm1);
   }

   protected void error(String parm1) {
      if (this.DEBUG) {
         System.out.println("error(" + parm1 + ")");
      }

      super.error(parm1);
   }

   protected int _firstch(int parm1) {
      if (this.DEBUG) {
         System.out.println("_firstch(" + parm1 + ")");
      }

      return super._firstch(parm1);
   }

   public int getOwnerDocument(int parm1) {
      if (this.DEBUG) {
         System.out.println("getOwnerDoc(" + parm1 + ")");
      }

      return super.getOwnerDocument(parm1);
   }

   protected int _nextsib(int parm1) {
      if (this.DEBUG) {
         System.out.println("_nextSib(" + parm1 + ")");
      }

      return super._nextsib(parm1);
   }

   public int getNextSibling(int parm1) {
      if (this.DEBUG) {
         System.out.println("getNextSibling(" + parm1 + ")");
      }

      return super.getNextSibling(parm1);
   }

   public boolean getDocumentAllDeclarationsProcessed() {
      if (this.DEBUG) {
         System.out.println("getDocAllDeclProc()");
      }

      return super.getDocumentAllDeclarationsProcessed();
   }

   public int getParent(int parm1) {
      if (this.DEBUG) {
         System.out.println("getParent(" + parm1 + ")");
      }

      return super.getParent(parm1);
   }

   public int getExpandedTypeID(String parm1, String parm2, int parm3) {
      if (this.DEBUG) {
         System.out.println("getExpandedTypeID()");
      }

      return super.getExpandedTypeID(parm1, parm2, parm3);
   }

   public void setDocumentBaseURI(String parm1) {
      if (this.DEBUG) {
         System.out.println("setDocBaseURI()");
      }

      super.setDocumentBaseURI(parm1);
   }

   public char[] getStringValueChunk(int parm1, int parm2, int[] parm3) {
      if (this.DEBUG) {
         System.out.println("getStringChunkValue(" + parm1 + "," + parm2 + ")");
      }

      return super.getStringValueChunk(parm1, parm2, parm3);
   }

   public DTMAxisTraverser getAxisTraverser(int parm1) {
      if (this.DEBUG) {
         System.out.println("getAxixTraverser(" + parm1 + ")");
      }

      return super.getAxisTraverser(parm1);
   }

   public DTMAxisIterator getTypedAxisIterator(int parm1, int parm2) {
      if (this.DEBUG) {
         System.out.println("getTypedAxisIterator(" + parm1 + "," + parm2 + ")");
      }

      return super.getTypedAxisIterator(parm1, parm2);
   }

   public DTMAxisIterator getAxisIterator(int parm1) {
      if (this.DEBUG) {
         System.out.println("getAxisIterator(" + parm1 + ")");
      }

      return super.getAxisIterator(parm1);
   }

   public int getElementById(String parm1) {
      if (this.DEBUG) {
         System.out.println("getElementByID(" + parm1 + ")");
      }

      return -1;
   }

   public DeclHandler getDeclHandler() {
      if (this.DEBUG) {
         System.out.println("getDeclHandler()");
      }

      return null;
   }

   public ErrorHandler getErrorHandler() {
      if (this.DEBUG) {
         System.out.println("getErrorHandler()");
      }

      return null;
   }

   public String getDocumentTypeDeclarationSystemIdentifier() {
      if (this.DEBUG) {
         System.out.println("get_DTD-SID()");
      }

      return null;
   }

   public interface CharacterNodeHandler {
      void characters(Node var1) throws SAXException;
   }
}
