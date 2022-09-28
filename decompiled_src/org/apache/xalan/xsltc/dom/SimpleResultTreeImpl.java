package org.apache.xalan.xsltc.dom;

import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.serializer.EmptySerializer;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringDefault;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class SimpleResultTreeImpl extends EmptySerializer implements DOM, DTM {
   private static final DTMAxisIterator EMPTY_ITERATOR = new DTMAxisIteratorBase() {
      public DTMAxisIterator reset() {
         return this;
      }

      public DTMAxisIterator setStartNode(int node) {
         return this;
      }

      public int next() {
         return -1;
      }

      public void setMark() {
      }

      public void gotoMark() {
      }

      public int getLast() {
         return 0;
      }

      public int getPosition() {
         return 0;
      }

      public DTMAxisIterator cloneIterator() {
         return this;
      }

      public void setRestartable(boolean isRestartable) {
      }
   };
   public static final int RTF_ROOT = 0;
   public static final int RTF_TEXT = 1;
   public static final int NUMBER_OF_NODES = 2;
   private static int _documentURIIndex = 0;
   private static final String EMPTY_STR = "";
   private String _text;
   protected String[] _textArray;
   protected XSLTCDTMManager _dtmManager;
   protected int _size = 0;
   private int _documentID;
   private BitArray _dontEscape = null;
   private boolean _escaping = true;

   public SimpleResultTreeImpl(XSLTCDTMManager dtmManager, int documentID) {
      this._dtmManager = dtmManager;
      this._documentID = documentID;
      this._textArray = new String[4];
   }

   public DTMManagerDefault getDTMManager() {
      return this._dtmManager;
   }

   public int getDocument() {
      return this._documentID;
   }

   public String getStringValue() {
      return this._text;
   }

   public DTMAxisIterator getIterator() {
      return new SingletonIterator(this.getDocument());
   }

   public DTMAxisIterator getChildren(int node) {
      return (new SimpleIterator()).setStartNode(node);
   }

   public DTMAxisIterator getTypedChildren(int type) {
      return new SimpleIterator(1, type);
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      switch (axis) {
         case 0:
         case 10:
            return new SimpleIterator(0);
         case 1:
            return (new SimpleIterator(0)).includeSelf();
         case 2:
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         default:
            return EMPTY_ITERATOR;
         case 3:
         case 4:
            return new SimpleIterator(1);
         case 5:
            return (new SimpleIterator(1)).includeSelf();
         case 13:
            return new SingletonIterator();
      }
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      switch (axis) {
         case 0:
         case 10:
            return new SimpleIterator(0, type);
         case 1:
            return (new SimpleIterator(0, type)).includeSelf();
         case 2:
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         default:
            return EMPTY_ITERATOR;
         case 3:
         case 4:
            return new SimpleIterator(1, type);
         case 5:
            return (new SimpleIterator(1, type)).includeSelf();
         case 13:
            return new SingletonIterator(type);
      }
   }

   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself) {
      return null;
   }

   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
      return null;
   }

   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op) {
      return null;
   }

   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
      return source;
   }

   public String getNodeName(int node) {
      return this.getNodeIdent(node) == 1 ? "#text" : "";
   }

   public String getNodeNameX(int node) {
      return "";
   }

   public String getNamespaceName(int node) {
      return "";
   }

   public int getExpandedTypeID(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      if (nodeID == 1) {
         return 3;
      } else {
         return nodeID == 0 ? 0 : -1;
      }
   }

   public int getNamespaceType(int node) {
      return 0;
   }

   public int getParent(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      return nodeID == 1 ? this.getNodeHandle(0) : -1;
   }

   public int getAttributeNode(int gType, int element) {
      return -1;
   }

   public String getStringValueX(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      return nodeID != 0 && nodeID != 1 ? "" : this._text;
   }

   public void copy(int node, SerializationHandler handler) throws TransletException {
      this.characters(node, handler);
   }

   public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
      int node;
      while((node = nodes.next()) != -1) {
         this.copy(node, handler);
      }

   }

   public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
      this.characters(node, handler);
      return null;
   }

   public boolean lessThan(int node1, int node2) {
      if (node1 == -1) {
         return false;
      } else if (node2 == -1) {
         return true;
      } else {
         return node1 < node2;
      }
   }

   public void characters(int node, SerializationHandler handler) throws TransletException {
      int nodeID = this.getNodeIdent(node);
      if (nodeID == 0 || nodeID == 1) {
         boolean escapeBit = false;
         boolean oldEscapeSetting = false;

         try {
            for(int i = 0; i < this._size; ++i) {
               if (this._dontEscape != null) {
                  escapeBit = this._dontEscape.getBit(i);
                  if (escapeBit) {
                     oldEscapeSetting = handler.setEscaping(false);
                  }
               }

               handler.characters(this._textArray[i]);
               if (escapeBit) {
                  handler.setEscaping(oldEscapeSetting);
               }
            }
         } catch (SAXException var7) {
            throw new TransletException(var7);
         }
      }

   }

   public Node makeNode(int index) {
      return null;
   }

   public Node makeNode(DTMAxisIterator iter) {
      return null;
   }

   public NodeList makeNodeList(int index) {
      return null;
   }

   public NodeList makeNodeList(DTMAxisIterator iter) {
      return null;
   }

   public String getLanguage(int node) {
      return null;
   }

   public int getSize() {
      return 2;
   }

   public String getDocumentURI(int node) {
      return "simple_rtf" + _documentURIIndex++;
   }

   public void setFilter(StripFilter filter) {
   }

   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
   }

   public boolean isElement(int node) {
      return false;
   }

   public boolean isAttribute(int node) {
      return false;
   }

   public String lookupNamespace(int node, String prefix) throws TransletException {
      return null;
   }

   public int getNodeIdent(int nodehandle) {
      return nodehandle != -1 ? nodehandle - this._documentID : -1;
   }

   public int getNodeHandle(int nodeId) {
      return nodeId != -1 ? nodeId + this._documentID : -1;
   }

   public DOM getResultTreeFrag(int initialSize, int rtfType) {
      return null;
   }

   public DOM getResultTreeFrag(int initialSize, int rtfType, boolean addToManager) {
      return null;
   }

   public SerializationHandler getOutputDomBuilder() {
      return this;
   }

   public int getNSType(int node) {
      return 0;
   }

   public String getUnparsedEntityURI(String name) {
      return null;
   }

   public Hashtable getElementsWithIDs() {
      return null;
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
      if (this._size == 1) {
         this._text = this._textArray[0];
      } else {
         StringBuffer buffer = new StringBuffer();

         for(int i = 0; i < this._size; ++i) {
            buffer.append(this._textArray[i]);
         }

         this._text = buffer.toString();
      }

   }

   public void characters(String str) throws SAXException {
      if (this._size >= this._textArray.length) {
         String[] newTextArray = new String[this._textArray.length * 2];
         System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
         this._textArray = newTextArray;
      }

      if (!this._escaping) {
         if (this._dontEscape == null) {
            this._dontEscape = new BitArray(8);
         }

         if (this._size >= this._dontEscape.size()) {
            this._dontEscape.resize(this._dontEscape.size() * 2);
         }

         this._dontEscape.setBit(this._size);
      }

      this._textArray[this._size++] = str;
   }

   public void characters(char[] ch, int offset, int length) throws SAXException {
      if (this._size >= this._textArray.length) {
         String[] newTextArray = new String[this._textArray.length * 2];
         System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
         this._textArray = newTextArray;
      }

      if (!this._escaping) {
         if (this._dontEscape == null) {
            this._dontEscape = new BitArray(8);
         }

         if (this._size >= this._dontEscape.size()) {
            this._dontEscape.resize(this._dontEscape.size() * 2);
         }

         this._dontEscape.setBit(this._size);
      }

      this._textArray[this._size++] = new String(ch, offset, length);
   }

   public boolean setEscaping(boolean escape) throws SAXException {
      boolean temp = this._escaping;
      this._escaping = escape;
      return temp;
   }

   public void setFeature(String featureId, boolean state) {
   }

   public void setProperty(String property, Object value) {
   }

   public DTMAxisTraverser getAxisTraverser(int axis) {
      return null;
   }

   public boolean hasChildNodes(int nodeHandle) {
      return this.getNodeIdent(nodeHandle) == 0;
   }

   public int getFirstChild(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      return nodeID == 0 ? this.getNodeHandle(1) : -1;
   }

   public int getLastChild(int nodeHandle) {
      return this.getFirstChild(nodeHandle);
   }

   public int getAttributeNode(int elementHandle, String namespaceURI, String name) {
      return -1;
   }

   public int getFirstAttribute(int nodeHandle) {
      return -1;
   }

   public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
      return -1;
   }

   public int getNextSibling(int nodeHandle) {
      return -1;
   }

   public int getPreviousSibling(int nodeHandle) {
      return -1;
   }

   public int getNextAttribute(int nodeHandle) {
      return -1;
   }

   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
      return -1;
   }

   public int getOwnerDocument(int nodeHandle) {
      return this.getDocument();
   }

   public int getDocumentRoot(int nodeHandle) {
      return this.getDocument();
   }

   public XMLString getStringValue(int nodeHandle) {
      return new XMLStringDefault(this.getStringValueX(nodeHandle));
   }

   public int getStringValueChunkCount(int nodeHandle) {
      return 0;
   }

   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
      return null;
   }

   public int getExpandedTypeID(String namespace, String localName, int type) {
      return -1;
   }

   public String getLocalNameFromExpandedNameID(int ExpandedNameID) {
      return "";
   }

   public String getNamespaceFromExpandedNameID(int ExpandedNameID) {
      return "";
   }

   public String getLocalName(int nodeHandle) {
      return "";
   }

   public String getPrefix(int nodeHandle) {
      return null;
   }

   public String getNamespaceURI(int nodeHandle) {
      return "";
   }

   public String getNodeValue(int nodeHandle) {
      return this.getNodeIdent(nodeHandle) == 1 ? this._text : null;
   }

   public short getNodeType(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      if (nodeID == 1) {
         return 3;
      } else {
         return (short)(nodeID == 0 ? 0 : -1);
      }
   }

   public short getLevel(int nodeHandle) {
      int nodeID = this.getNodeIdent(nodeHandle);
      if (nodeID == 1) {
         return 2;
      } else {
         return (short)(nodeID == 0 ? 1 : -1);
      }
   }

   public boolean isSupported(String feature, String version) {
      return false;
   }

   public String getDocumentBaseURI() {
      return "";
   }

   public void setDocumentBaseURI(String baseURI) {
   }

   public String getDocumentSystemIdentifier(int nodeHandle) {
      return null;
   }

   public String getDocumentEncoding(int nodeHandle) {
      return null;
   }

   public String getDocumentStandalone(int nodeHandle) {
      return null;
   }

   public String getDocumentVersion(int documentHandle) {
      return null;
   }

   public boolean getDocumentAllDeclarationsProcessed() {
      return false;
   }

   public String getDocumentTypeDeclarationSystemIdentifier() {
      return null;
   }

   public String getDocumentTypeDeclarationPublicIdentifier() {
      return null;
   }

   public int getElementById(String elementId) {
      return -1;
   }

   public boolean supportsPreStripping() {
      return false;
   }

   public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle) {
      return this.lessThan(firstNodeHandle, secondNodeHandle);
   }

   public boolean isCharacterElementContentWhitespace(int nodeHandle) {
      return false;
   }

   public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
      return false;
   }

   public boolean isAttributeSpecified(int attributeHandle) {
      return false;
   }

   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
   }

   public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
   }

   public Node getNode(int nodeHandle) {
      return this.makeNode(nodeHandle);
   }

   public boolean needsTwoThreads() {
      return false;
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

   public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
   }

   public void appendTextChild(String str) {
   }

   public SourceLocator getSourceLocatorFor(int node) {
      return null;
   }

   public void documentRegistration() {
   }

   public void documentRelease() {
   }

   public void migrateTo(DTMManager manager) {
   }

   public final class SingletonIterator extends DTMAxisIteratorBase {
      static final int NO_TYPE = -1;
      int _type = -1;
      int _currentNode;

      public SingletonIterator() {
      }

      public SingletonIterator(int type) {
         this._type = type;
      }

      public void setMark() {
         super._markedNode = this._currentNode;
      }

      public void gotoMark() {
         this._currentNode = super._markedNode;
      }

      public DTMAxisIterator setStartNode(int nodeHandle) {
         this._currentNode = super._startNode = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle);
         return this;
      }

      public int next() {
         if (this._currentNode == -1) {
            return -1;
         } else {
            this._currentNode = -1;
            if (this._type == -1) {
               return SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
            } else {
               return (this._currentNode != 0 || this._type != 0) && (this._currentNode != 1 || this._type != 3) ? -1 : SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
            }
         }
      }
   }

   public final class SimpleIterator extends DTMAxisIteratorBase {
      static final int DIRECTION_UP = 0;
      static final int DIRECTION_DOWN = 1;
      static final int NO_TYPE = -1;
      int _direction = 1;
      int _type = -1;
      int _currentNode;

      public SimpleIterator() {
      }

      public SimpleIterator(int direction) {
         this._direction = direction;
      }

      public SimpleIterator(int direction, int type) {
         this._direction = direction;
         this._type = type;
      }

      public int next() {
         if (this._direction == 1) {
            while(this._currentNode < 2) {
               if (this._type == -1) {
                  return this.returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode++));
               }

               if (this._currentNode == 0 && this._type == 0 || this._currentNode == 1 && this._type == 3) {
                  return this.returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode++));
               }

               ++this._currentNode;
            }

            return -1;
         } else {
            while(this._currentNode >= 0) {
               if (this._type == -1) {
                  return this.returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode--));
               }

               if (this._currentNode == 0 && this._type == 0 || this._currentNode == 1 && this._type == 3) {
                  return this.returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode--));
               }

               --this._currentNode;
            }

            return -1;
         }
      }

      public DTMAxisIterator setStartNode(int nodeHandle) {
         int nodeID = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle);
         super._startNode = nodeID;
         if (!super._includeSelf && nodeID != -1) {
            if (this._direction == 1) {
               ++nodeID;
            } else if (this._direction == 0) {
               --nodeID;
            }
         }

         this._currentNode = nodeID;
         return this;
      }

      public void setMark() {
         super._markedNode = this._currentNode;
      }

      public void gotoMark() {
         this._currentNode = super._markedNode;
      }
   }
}
