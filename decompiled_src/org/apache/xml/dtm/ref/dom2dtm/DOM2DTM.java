package org.apache.xml.dtm.ref.dom2dtm;

import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.dom.DOMSource;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xml.utils.TreeWalker;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class DOM2DTM extends DTMDefaultBaseIterators {
   static final boolean JJK_DEBUG = false;
   static final boolean JJK_NEWCODE = true;
   static final String NAMESPACE_DECL_NS = "http://www.w3.org/XML/1998/namespace";
   private transient Node m_pos;
   private int m_last_parent = 0;
   private int m_last_kid = -1;
   private transient Node m_root;
   boolean m_processedFirstElement = false;
   private transient boolean m_nodesAreProcessed;
   protected Vector m_nodes = new Vector();
   TreeWalker m_walker = new TreeWalker((ContentHandler)null);

   public DOM2DTM(DTMManager mgr, DOMSource domSource, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing) {
      super(mgr, domSource, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
      this.m_pos = this.m_root = domSource.getNode();
      this.m_last_parent = this.m_last_kid = -1;
      this.m_last_kid = this.addNode(this.m_root, this.m_last_parent, this.m_last_kid, -1);
      if (1 == this.m_root.getNodeType()) {
         NamedNodeMap attrs = this.m_root.getAttributes();
         int attrsize = attrs == null ? 0 : attrs.getLength();
         if (attrsize > 0) {
            int attrIndex = -1;

            for(int i = 0; i < attrsize; ++i) {
               attrIndex = this.addNode(attrs.item(i), 0, attrIndex, -1);
               super.m_firstch.setElementAt(-1, attrIndex);
            }

            super.m_nextsib.setElementAt(-1, attrIndex);
         }
      }

      this.m_nodesAreProcessed = false;
   }

   protected int addNode(Node node, int parentIndex, int previousSibling, int forceNodeType) {
      int nodeIndex = this.m_nodes.size();
      if (super.m_dtmIdent.size() == nodeIndex >>> 16) {
         try {
            if (super.m_mgr == null) {
               throw new ClassCastException();
            }

            DTMManagerDefault mgrD = (DTMManagerDefault)super.m_mgr;
            int id = mgrD.getFirstFreeDTMID();
            mgrD.addDTM(this, id, nodeIndex);
            super.m_dtmIdent.addElement(id << 16);
         } catch (ClassCastException var11) {
            this.error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", (Object[])null));
         }
      }

      ++super.m_size;
      int type;
      if (-1 == forceNodeType) {
         type = node.getNodeType();
      } else {
         type = forceNodeType;
      }

      String nsURI;
      if (2 == type) {
         nsURI = node.getNodeName();
         if (nsURI.startsWith("xmlns:") || nsURI.equals("xmlns")) {
            type = 13;
         }
      }

      this.m_nodes.addElement(node);
      super.m_firstch.setElementAt(-2, nodeIndex);
      super.m_nextsib.setElementAt(-2, nodeIndex);
      super.m_prevsib.setElementAt(previousSibling, nodeIndex);
      super.m_parent.setElementAt(parentIndex, nodeIndex);
      if (-1 != parentIndex && type != 2 && type != 13 && -2 == super.m_firstch.elementAt(parentIndex)) {
         super.m_firstch.setElementAt(nodeIndex, parentIndex);
      }

      nsURI = node.getNamespaceURI();
      String localName = type == 7 ? node.getNodeName() : node.getLocalName();
      if ((type == 1 || type == 2) && null == localName) {
         localName = node.getNodeName();
      }

      ExpandedNameTable exnt = super.m_expandedNameTable;
      if (node.getLocalName() == null && type != 1 && type == 2) {
      }

      int expandedNameID = null != localName ? exnt.getExpandedTypeID(nsURI, localName, type) : exnt.getExpandedTypeID(type);
      super.m_exptype.setElementAt(expandedNameID, nodeIndex);
      this.indexNode(expandedNameID, nodeIndex);
      if (-1 != previousSibling) {
         super.m_nextsib.setElementAt(nodeIndex, previousSibling);
      }

      if (type == 13) {
         this.declareNamespaceInContext(parentIndex, nodeIndex);
      }

      return nodeIndex;
   }

   public int getNumberOfNodes() {
      return this.m_nodes.size();
   }

   protected boolean nextNode() {
      if (this.m_nodesAreProcessed) {
         return false;
      } else {
         Node pos = this.m_pos;
         Node next = null;
         int nexttype = -1;

         do {
            if (pos.hasChildNodes()) {
               next = pos.getFirstChild();
               if (next != null && 10 == next.getNodeType()) {
                  next = next.getNextSibling();
               }

               if (5 != pos.getNodeType()) {
                  this.m_last_parent = this.m_last_kid;
                  this.m_last_kid = -1;
                  if (null != super.m_wsfilter) {
                     short wsv = super.m_wsfilter.getShouldStripSpace(this.makeNodeHandle(this.m_last_parent), this);
                     boolean shouldStrip = 3 == wsv ? this.getShouldStripWhitespace() : 2 == wsv;
                     this.pushShouldStripWhitespace(shouldStrip);
                  }
               }
            } else {
               if (this.m_last_kid != -1 && super.m_firstch.elementAt(this.m_last_kid) == -2) {
                  super.m_firstch.setElementAt(-1, this.m_last_kid);
               }

               while(this.m_last_parent != -1) {
                  next = pos.getNextSibling();
                  if (next != null && 10 == next.getNodeType()) {
                     next = next.getNextSibling();
                  }

                  if (next != null) {
                     break;
                  }

                  pos = pos.getParentNode();
                  if (pos == null) {
                  }

                  if (pos == null || 5 != pos.getNodeType()) {
                     this.popShouldStripWhitespace();
                     if (this.m_last_kid == -1) {
                        super.m_firstch.setElementAt(-1, this.m_last_parent);
                     } else {
                        super.m_nextsib.setElementAt(-1, this.m_last_kid);
                     }

                     this.m_last_parent = super.m_parent.elementAt(this.m_last_kid = this.m_last_parent);
                  }
               }

               if (this.m_last_parent == -1) {
                  next = null;
               }
            }

            if (next != null) {
               nexttype = next.getNodeType();
            }

            if (5 == nexttype) {
               pos = next;
            }
         } while(5 == nexttype);

         if (next == null) {
            super.m_nextsib.setElementAt(-1, 0);
            this.m_nodesAreProcessed = true;
            this.m_pos = null;
            return false;
         } else {
            boolean suppressNode = false;
            Node lastTextNode = null;
            nexttype = next.getNodeType();
            if (3 != nexttype && 4 != nexttype) {
               if (7 == nexttype) {
                  suppressNode = pos.getNodeName().toLowerCase().equals("xml");
               }
            } else {
               suppressNode = null != super.m_wsfilter && this.getShouldStripWhitespace();

               for(Node n = next; n != null; n = this.logicalNextDOMTextNode(n)) {
                  lastTextNode = n;
                  if (3 == n.getNodeType()) {
                     nexttype = 3;
                  }

                  suppressNode &= XMLCharacterRecognizer.isWhiteSpace(n.getNodeValue());
               }
            }

            if (!suppressNode) {
               int nextindex = this.addNode(next, this.m_last_parent, this.m_last_kid, nexttype);
               this.m_last_kid = nextindex;
               if (1 == nexttype) {
                  int attrIndex = -1;
                  NamedNodeMap attrs = next.getAttributes();
                  int attrsize = attrs == null ? 0 : attrs.getLength();
                  if (attrsize > 0) {
                     for(int i = 0; i < attrsize; ++i) {
                        attrIndex = this.addNode(attrs.item(i), nextindex, attrIndex, -1);
                        super.m_firstch.setElementAt(-1, attrIndex);
                        if (!this.m_processedFirstElement && "xmlns:xml".equals(attrs.item(i).getNodeName())) {
                           this.m_processedFirstElement = true;
                        }
                     }
                  }

                  if (!this.m_processedFirstElement) {
                     attrIndex = this.addNode(new DOM2DTMdefaultNamespaceDeclarationNode((Element)next, "xml", "http://www.w3.org/XML/1998/namespace", this.makeNodeHandle((attrIndex == -1 ? nextindex : attrIndex) + 1)), nextindex, attrIndex, -1);
                     super.m_firstch.setElementAt(-1, attrIndex);
                     this.m_processedFirstElement = true;
                  }

                  if (attrIndex != -1) {
                     super.m_nextsib.setElementAt(-1, attrIndex);
                  }
               }
            }

            if (3 == nexttype || 4 == nexttype) {
               next = lastTextNode;
            }

            this.m_pos = next;
            return true;
         }
      }
   }

   public Node getNode(int nodeHandle) {
      int identity = this.makeNodeIdentity(nodeHandle);
      return (Node)this.m_nodes.elementAt(identity);
   }

   protected Node lookupNode(int nodeIdentity) {
      return (Node)this.m_nodes.elementAt(nodeIdentity);
   }

   protected int getNextNodeIdentity(int identity) {
      ++identity;
      if (identity >= this.m_nodes.size() && !this.nextNode()) {
         identity = -1;
      }

      return identity;
   }

   private int getHandleFromNode(Node node) {
      if (null != node) {
         int len = this.m_nodes.size();
         int i = 0;

         boolean isMore;
         do {
            while(i < len) {
               if (this.m_nodes.elementAt(i) == node) {
                  return this.makeNodeHandle(i);
               }

               ++i;
            }

            isMore = this.nextNode();
            len = this.m_nodes.size();
         } while(isMore || i < len);
      }

      return -1;
   }

   public int getHandleOfNode(Node node) {
      if (null != node && (this.m_root == node || this.m_root.getNodeType() == 9 && this.m_root == node.getOwnerDocument() || this.m_root.getNodeType() != 9 && this.m_root.getOwnerDocument() == node.getOwnerDocument())) {
         for(Node cursor = node; cursor != null; cursor = ((Node)cursor).getNodeType() != 2 ? ((Node)cursor).getParentNode() : ((Attr)cursor).getOwnerElement()) {
            if (cursor == this.m_root) {
               return this.getHandleFromNode(node);
            }
         }
      }

      return -1;
   }

   public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
      if (null == namespaceURI) {
         namespaceURI = "";
      }

      int type = this.getNodeType(nodeHandle);
      if (1 == type) {
         int identity = this.makeNodeIdentity(nodeHandle);

         while(-1 != (identity = this.getNextNodeIdentity(identity))) {
            type = this._type(identity);
            if (type != 2 && type != 13) {
               break;
            }

            Node node = this.lookupNode(identity);
            String nodeuri = node.getNamespaceURI();
            if (null == nodeuri) {
               nodeuri = "";
            }

            String nodelocalname = node.getLocalName();
            if (nodeuri.equals(namespaceURI) && name.equals(nodelocalname)) {
               return this.makeNodeHandle(identity);
            }
         }
      }

      return -1;
   }

   public XMLString getStringValue(int nodeHandle) {
      int type = this.getNodeType(nodeHandle);
      Node node = this.getNode(nodeHandle);
      FastStringBuffer buf;
      String s;
      if (1 != type && 9 != type && 11 != type) {
         if (3 != type && 4 != type) {
            return super.m_xstrf.newstr(node.getNodeValue());
         } else {
            for(buf = StringBufferPool.get(); node != null; node = this.logicalNextDOMTextNode(node)) {
               buf.append(node.getNodeValue());
            }

            s = buf.length() > 0 ? buf.toString() : "";
            StringBufferPool.free(buf);
            return super.m_xstrf.newstr(s);
         }
      } else {
         buf = StringBufferPool.get();

         try {
            getNodeData(node, buf);
            s = buf.length() > 0 ? buf.toString() : "";
         } finally {
            StringBufferPool.free(buf);
         }

         return super.m_xstrf.newstr(s);
      }
   }

   public boolean isWhitespace(int nodeHandle) {
      int type = this.getNodeType(nodeHandle);
      Node node = this.getNode(nodeHandle);
      if (3 != type && 4 != type) {
         return false;
      } else {
         FastStringBuffer buf;
         for(buf = StringBufferPool.get(); node != null; node = this.logicalNextDOMTextNode(node)) {
            buf.append(node.getNodeValue());
         }

         boolean b = buf.isWhitespace(0, buf.length());
         StringBufferPool.free(buf);
         return b;
      }
   }

   protected static void getNodeData(Node node, FastStringBuffer buf) {
      switch (node.getNodeType()) {
         case 1:
         case 9:
         case 11:
            for(Node child = node.getFirstChild(); null != child; child = child.getNextSibling()) {
               getNodeData(child, buf);
            }

            return;
         case 2:
         case 3:
         case 4:
            buf.append(node.getNodeValue());
         case 5:
         case 6:
         case 7:
         case 8:
         case 10:
      }

   }

   public String getNodeName(int nodeHandle) {
      Node node = this.getNode(nodeHandle);
      return node.getNodeName();
   }

   public String getNodeNameX(int nodeHandle) {
      short type = this.getNodeType(nodeHandle);
      String name;
      Node node;
      switch (type) {
         case 1:
         case 2:
         case 5:
         case 7:
            node = this.getNode(nodeHandle);
            name = node.getNodeName();
            break;
         case 3:
         case 4:
         case 6:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         default:
            name = "";
            break;
         case 13:
            node = this.getNode(nodeHandle);
            name = node.getNodeName();
            if (name.startsWith("xmlns:")) {
               name = QName.getLocalPart(name);
            } else if (name.equals("xmlns")) {
               name = "";
            }
      }

      return name;
   }

   public String getLocalName(int nodeHandle) {
      int id = this.makeNodeIdentity(nodeHandle);
      if (-1 == id) {
         return null;
      } else {
         Node newnode = (Node)this.m_nodes.elementAt(id);
         String newname = newnode.getLocalName();
         if (null == newname) {
            String qname = newnode.getNodeName();
            if ('#' == qname.charAt(0)) {
               newname = "";
            } else {
               int index = qname.indexOf(58);
               newname = index < 0 ? qname : qname.substring(index + 1);
            }
         }

         return newname;
      }
   }

   public String getPrefix(int nodeHandle) {
      short type = this.getNodeType(nodeHandle);
      String prefix;
      Node node;
      String qname;
      int index;
      switch (type) {
         case 1:
         case 2:
            node = this.getNode(nodeHandle);
            qname = node.getNodeName();
            index = qname.indexOf(58);
            prefix = index < 0 ? "" : qname.substring(0, index);
            break;
         case 13:
            node = this.getNode(nodeHandle);
            qname = node.getNodeName();
            index = qname.indexOf(58);
            prefix = index < 0 ? "" : qname.substring(index + 1);
            break;
         default:
            prefix = "";
      }

      return prefix;
   }

   public String getNamespaceURI(int nodeHandle) {
      int id = this.makeNodeIdentity(nodeHandle);
      if (id == -1) {
         return null;
      } else {
         Node node = (Node)this.m_nodes.elementAt(id);
         return node.getNamespaceURI();
      }
   }

   private Node logicalNextDOMTextNode(Node n) {
      Node p = n.getNextSibling();
      if (p == null) {
         for(n = n.getParentNode(); n != null && 5 == n.getNodeType(); n = n.getParentNode()) {
            p = n.getNextSibling();
            if (p != null) {
               break;
            }
         }
      }

      n = p;

      while(n != null && 5 == n.getNodeType()) {
         if (n.hasChildNodes()) {
            n = n.getFirstChild();
         } else {
            n = n.getNextSibling();
         }
      }

      if (n != null) {
         int ntype = n.getNodeType();
         if (3 != ntype && 4 != ntype) {
            n = null;
         }
      }

      return n;
   }

   public String getNodeValue(int nodeHandle) {
      int type = this._exptype(this.makeNodeIdentity(nodeHandle));
      int type = -1 != type ? this.getNodeType(nodeHandle) : -1;
      if (3 != type && 4 != type) {
         return this.getNode(nodeHandle).getNodeValue();
      } else {
         Node node = this.getNode(nodeHandle);
         Node n = this.logicalNextDOMTextNode(node);
         if (n == null) {
            return node.getNodeValue();
         } else {
            FastStringBuffer buf = StringBufferPool.get();
            buf.append(node.getNodeValue());

            while(n != null) {
               buf.append(n.getNodeValue());
               n = this.logicalNextDOMTextNode(n);
            }

            String s = buf.length() > 0 ? buf.toString() : "";
            StringBufferPool.free(buf);
            return s;
         }
      }
   }

   public String getDocumentTypeDeclarationSystemIdentifier() {
      Document doc;
      if (this.m_root.getNodeType() == 9) {
         doc = (Document)this.m_root;
      } else {
         doc = this.m_root.getOwnerDocument();
      }

      if (null != doc) {
         DocumentType dtd = doc.getDoctype();
         if (null != dtd) {
            return dtd.getSystemId();
         }
      }

      return null;
   }

   public String getDocumentTypeDeclarationPublicIdentifier() {
      Document doc;
      if (this.m_root.getNodeType() == 9) {
         doc = (Document)this.m_root;
      } else {
         doc = this.m_root.getOwnerDocument();
      }

      if (null != doc) {
         DocumentType dtd = doc.getDoctype();
         if (null != dtd) {
            return dtd.getPublicId();
         }
      }

      return null;
   }

   public int getElementById(String elementId) {
      Document doc = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
      if (null != doc) {
         Node elem = doc.getElementById(elementId);
         if (null != elem) {
            int elemHandle = this.getHandleFromNode(elem);
            if (-1 == elemHandle) {
               int identity = this.m_nodes.size() - 1;

               while(-1 != (identity = this.getNextNodeIdentity(identity))) {
                  Node node = this.getNode(identity);
                  if (node == elem) {
                     elemHandle = this.getHandleFromNode(elem);
                     break;
                  }
               }
            }

            return elemHandle;
         }
      }

      return -1;
   }

   public String getUnparsedEntityURI(String name) {
      String url = "";
      Document doc = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
      if (null != doc) {
         DocumentType doctype = doc.getDoctype();
         if (null != doctype) {
            NamedNodeMap entities = doctype.getEntities();
            if (null == entities) {
               return url;
            }

            Entity entity = (Entity)entities.getNamedItem(name);
            if (null == entity) {
               return url;
            }

            String notationName = entity.getNotationName();
            if (null != notationName) {
               url = entity.getSystemId();
               if (null == url) {
                  url = entity.getPublicId();
               }
            }
         }
      }

      return url;
   }

   public boolean isAttributeSpecified(int attributeHandle) {
      int type = this.getNodeType(attributeHandle);
      if (2 == type) {
         Attr attr = (Attr)this.getNode(attributeHandle);
         return attr.getSpecified();
      } else {
         return false;
      }
   }

   public void setIncrementalSAXSource(IncrementalSAXSource source) {
   }

   public ContentHandler getContentHandler() {
      return null;
   }

   public LexicalHandler getLexicalHandler() {
      return null;
   }

   public EntityResolver getEntityResolver() {
      return null;
   }

   public DTDHandler getDTDHandler() {
      return null;
   }

   public ErrorHandler getErrorHandler() {
      return null;
   }

   public DeclHandler getDeclHandler() {
      return null;
   }

   public boolean needsTwoThreads() {
      return false;
   }

   private static boolean isSpace(char ch) {
      return XMLCharacterRecognizer.isWhiteSpace(ch);
   }

   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
      if (normalize) {
         XMLString str = this.getStringValue(nodeHandle);
         str = str.fixWhiteSpace(true, true, false);
         str.dispatchCharactersEvents(ch);
      } else {
         int type = this.getNodeType(nodeHandle);
         Node node = this.getNode(nodeHandle);
         dispatchNodeData(node, ch, 0);
         if (3 == type || 4 == type) {
            while(null != (node = this.logicalNextDOMTextNode(node))) {
               dispatchNodeData(node, ch, 0);
            }
         }
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

   public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
      TreeWalker treeWalker = this.m_walker;
      ContentHandler prevCH = treeWalker.getContentHandler();
      if (null != prevCH) {
         treeWalker = new TreeWalker((ContentHandler)null);
      }

      treeWalker.setContentHandler(ch);

      try {
         Node node = this.getNode(nodeHandle);
         treeWalker.traverse(node);
      } finally {
         treeWalker.setContentHandler((ContentHandler)null);
      }

   }

   public void setProperty(String property, Object value) {
   }

   public SourceLocator getSourceLocatorFor(int node) {
      return null;
   }

   public interface CharacterNodeHandler {
      void characters(Node var1) throws SAXException;
   }
}
