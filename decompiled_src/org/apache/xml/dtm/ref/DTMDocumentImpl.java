package org.apache.xml.dtm.ref;

import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class DTMDocumentImpl implements DTM, ContentHandler, LexicalHandler {
   protected static final byte DOCHANDLE_SHIFT = 22;
   protected static final int NODEHANDLE_MASK = 8388607;
   protected static final int DOCHANDLE_MASK = -8388608;
   int m_docHandle = -1;
   int m_docElement = -1;
   int currentParent = 0;
   int previousSibling = 0;
   protected int m_currentNode = -1;
   private boolean previousSiblingWasParent = false;
   int[] gotslot = new int[4];
   private boolean done = false;
   boolean m_isError = false;
   private final boolean DEBUG = false;
   protected String m_documentBaseURI;
   private IncrementalSAXSource m_incrSAXSource = null;
   ChunkedIntArray nodes = new ChunkedIntArray(4);
   private FastStringBuffer m_char = new FastStringBuffer();
   private int m_char_current_start = 0;
   private DTMStringPool m_localNames = new DTMStringPool();
   private DTMStringPool m_nsNames = new DTMStringPool();
   private DTMStringPool m_prefixNames = new DTMStringPool();
   private ExpandedNameTable m_expandedNames = new ExpandedNameTable();
   private XMLStringFactory m_xsf;
   private static final String[] fixednames = new String[]{null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null};

   public DTMDocumentImpl(DTMManager mgr, int documentNumber, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory) {
      this.initDocument(documentNumber);
      this.m_xsf = xstringfactory;
   }

   public void setIncrementalSAXSource(IncrementalSAXSource source) {
      this.m_incrSAXSource = source;
      source.setContentHandler(this);
      source.setLexicalHandler(this);
   }

   private final int appendNode(int w0, int w1, int w2, int w3) {
      int slotnumber = this.nodes.appendSlot(w0, w1, w2, w3);
      if (this.previousSiblingWasParent) {
         this.nodes.writeEntry(this.previousSibling, 2, slotnumber);
      }

      this.previousSiblingWasParent = false;
      return slotnumber;
   }

   public void setFeature(String featureId, boolean state) {
   }

   public void setLocalNameTable(DTMStringPool poolRef) {
      this.m_localNames = poolRef;
   }

   public DTMStringPool getLocalNameTable() {
      return this.m_localNames;
   }

   public void setNsNameTable(DTMStringPool poolRef) {
      this.m_nsNames = poolRef;
   }

   public DTMStringPool getNsNameTable() {
      return this.m_nsNames;
   }

   public void setPrefixNameTable(DTMStringPool poolRef) {
      this.m_prefixNames = poolRef;
   }

   public DTMStringPool getPrefixNameTable() {
      return this.m_prefixNames;
   }

   void setContentBuffer(FastStringBuffer buffer) {
      this.m_char = buffer;
   }

   FastStringBuffer getContentBuffer() {
      return this.m_char;
   }

   public ContentHandler getContentHandler() {
      return (ContentHandler)(this.m_incrSAXSource instanceof IncrementalSAXSource_Filter ? (ContentHandler)this.m_incrSAXSource : this);
   }

   public LexicalHandler getLexicalHandler() {
      return (LexicalHandler)(this.m_incrSAXSource instanceof IncrementalSAXSource_Filter ? (LexicalHandler)this.m_incrSAXSource : this);
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
      return null != this.m_incrSAXSource;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.m_char.append(ch, start, length);
   }

   private void processAccumulatedText() {
      int len = this.m_char.length();
      if (len != this.m_char_current_start) {
         this.appendTextChild(this.m_char_current_start, len - this.m_char_current_start);
         this.m_char_current_start = len;
      }

   }

   public void endDocument() throws SAXException {
      this.appendEndDocument();
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      this.processAccumulatedText();
      this.appendEndElement();
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.processAccumulatedText();
   }

   public void setDocumentLocator(Locator locator) {
   }

   public void skippedEntity(String name) throws SAXException {
      this.processAccumulatedText();
   }

   public void startDocument() throws SAXException {
      this.appendStartDocument();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
      this.processAccumulatedText();
      String prefix = null;
      int colon = qName.indexOf(58);
      if (colon > 0) {
         prefix = qName.substring(0, colon);
      }

      System.out.println("Prefix=" + prefix + " index=" + this.m_prefixNames.stringToIndex(prefix));
      this.appendStartElement(this.m_nsNames.stringToIndex(namespaceURI), this.m_localNames.stringToIndex(localName), this.m_prefixNames.stringToIndex(prefix));
      int nAtts = atts == null ? 0 : atts.getLength();

      for(int i = nAtts - 1; i >= 0; --i) {
         qName = atts.getQName(i);
         if (qName.startsWith("xmlns:") || "xmlns".equals(qName)) {
            prefix = null;
            colon = qName.indexOf(58);
            if (colon > 0) {
               prefix = qName.substring(0, colon);
            } else {
               prefix = null;
            }

            this.appendNSDeclaration(this.m_prefixNames.stringToIndex(prefix), this.m_nsNames.stringToIndex(atts.getValue(i)), atts.getType(i).equalsIgnoreCase("ID"));
         }
      }

      for(int i = nAtts - 1; i >= 0; --i) {
         qName = atts.getQName(i);
         if (!qName.startsWith("xmlns:") && !"xmlns".equals(qName)) {
            prefix = null;
            colon = qName.indexOf(58);
            if (colon > 0) {
               prefix = qName.substring(0, colon);
               localName = qName.substring(colon + 1);
            } else {
               prefix = "";
               localName = qName;
            }

            this.m_char.append(atts.getValue(i));
            int contentEnd = this.m_char.length();
            if (!"xmlns".equals(prefix) && !"xmlns".equals(qName)) {
               this.appendAttribute(this.m_nsNames.stringToIndex(atts.getURI(i)), this.m_localNames.stringToIndex(localName), this.m_prefixNames.stringToIndex(prefix), atts.getType(i).equalsIgnoreCase("ID"), this.m_char_current_start, contentEnd - this.m_char_current_start);
            }

            this.m_char_current_start = contentEnd;
         }
      }

   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      this.processAccumulatedText();
      this.m_char.append(ch, start, length);
      this.appendComment(this.m_char_current_start, length);
      this.m_char_current_start += length;
   }

   public void endCDATA() throws SAXException {
   }

   public void endDTD() throws SAXException {
   }

   public void endEntity(String name) throws SAXException {
   }

   public void startCDATA() throws SAXException {
   }

   public void startDTD(String name, String publicId, String systemId) throws SAXException {
   }

   public void startEntity(String name) throws SAXException {
   }

   final void initDocument(int documentNumber) {
      this.m_docHandle = documentNumber << 22;
      this.nodes.writeSlot(0, 9, -1, -1, 0);
      this.done = false;
   }

   public boolean hasChildNodes(int nodeHandle) {
      return this.getFirstChild(nodeHandle) != -1;
   }

   public int getFirstChild(int nodeHandle) {
      nodeHandle &= 8388607;
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      if (type == 1 || type == 9 || type == 5) {
         int kid = nodeHandle + 1;
         this.nodes.readSlot(kid, this.gotslot);

         while(2 == (this.gotslot[0] & '\uffff')) {
            kid = this.gotslot[2];
            if (kid == -1) {
               return -1;
            }

            this.nodes.readSlot(kid, this.gotslot);
         }

         if (this.gotslot[1] == nodeHandle) {
            int firstChild = kid | this.m_docHandle;
            return firstChild;
         }
      }

      return -1;
   }

   public int getLastChild(int nodeHandle) {
      nodeHandle &= 8388607;
      int lastChild = -1;

      for(int nextkid = this.getFirstChild(nodeHandle); nextkid != -1; nextkid = this.getNextSibling(nextkid)) {
         lastChild = nextkid;
      }

      return lastChild | this.m_docHandle;
   }

   public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
      int nsIndex = this.m_nsNames.stringToIndex(namespaceURI);
      int nameIndex = this.m_localNames.stringToIndex(name);
      nodeHandle &= 8388607;
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      if (type == 1) {
         ++nodeHandle;
      }

      while(type == 2) {
         if (nsIndex == this.gotslot[0] << 16 && this.gotslot[3] == nameIndex) {
            return nodeHandle | this.m_docHandle;
         }

         nodeHandle = this.gotslot[2];
         this.nodes.readSlot(nodeHandle, this.gotslot);
      }

      return -1;
   }

   public int getFirstAttribute(int nodeHandle) {
      nodeHandle &= 8388607;
      if (1 != (this.nodes.readEntry(nodeHandle, 0) & '\uffff')) {
         return -1;
      } else {
         ++nodeHandle;
         return 2 == (this.nodes.readEntry(nodeHandle, 0) & '\uffff') ? nodeHandle | this.m_docHandle : -1;
      }
   }

   public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
      return -1;
   }

   public int getNextSibling(int nodeHandle) {
      nodeHandle &= 8388607;
      if (nodeHandle == 0) {
         return -1;
      } else {
         short type = (short)(this.nodes.readEntry(nodeHandle, 0) & '\uffff');
         int nextSib;
         if (type == 1 || type == 2 || type == 5) {
            nextSib = this.nodes.readEntry(nodeHandle, 2);
            if (nextSib == -1) {
               return -1;
            }

            if (nextSib != 0) {
               return this.m_docHandle | nextSib;
            }
         }

         nextSib = this.nodes.readEntry(nodeHandle, 1);
         ++nodeHandle;
         return this.nodes.readEntry(nodeHandle, 1) == nextSib ? this.m_docHandle | nodeHandle : -1;
      }
   }

   public int getPreviousSibling(int nodeHandle) {
      nodeHandle &= 8388607;
      if (nodeHandle == 0) {
         return -1;
      } else {
         int parent = this.nodes.readEntry(nodeHandle, 1);
         int kid = -1;

         for(int nextkid = this.getFirstChild(parent); nextkid != nodeHandle; nextkid = this.getNextSibling(nextkid)) {
            kid = nextkid;
         }

         return kid | this.m_docHandle;
      }
   }

   public int getNextAttribute(int nodeHandle) {
      nodeHandle &= 8388607;
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      if (type == 1) {
         return this.getFirstAttribute(nodeHandle);
      } else {
         return type == 2 && this.gotslot[2] != -1 ? this.m_docHandle | this.gotslot[2] : -1;
      }
   }

   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
      return -1;
   }

   public int getNextDescendant(int subtreeRootHandle, int nodeHandle) {
      subtreeRootHandle &= 8388607;
      nodeHandle &= 8388607;
      if (nodeHandle == 0) {
         return -1;
      } else {
         while(!this.m_isError && (!this.done || nodeHandle <= this.nodes.slotsUsed())) {
            if (nodeHandle > subtreeRootHandle) {
               this.nodes.readSlot(nodeHandle + 1, this.gotslot);
               if (this.gotslot[2] != 0) {
                  short type = (short)(this.gotslot[0] & '\uffff');
                  if (type != 2) {
                     int nextParentPos = this.gotslot[1];
                     if (nextParentPos >= subtreeRootHandle) {
                        return this.m_docHandle | nodeHandle + 1;
                     }
                     break;
                  }

                  nodeHandle += 2;
               } else {
                  if (!this.done) {
                     continue;
                  }
                  break;
               }
            } else {
               ++nodeHandle;
            }
         }

         return -1;
      }
   }

   public int getNextFollowing(int axisContextHandle, int nodeHandle) {
      return -1;
   }

   public int getNextPreceding(int axisContextHandle, int nodeHandle) {
      nodeHandle &= 8388607;

      while(nodeHandle > 1) {
         --nodeHandle;
         if (2 != (this.nodes.readEntry(nodeHandle, 0) & '\uffff')) {
            return this.m_docHandle | this.nodes.specialFind(axisContextHandle, nodeHandle);
         }
      }

      return -1;
   }

   public int getParent(int nodeHandle) {
      return this.m_docHandle | this.nodes.readEntry(nodeHandle, 1);
   }

   public int getDocumentRoot() {
      return this.m_docHandle | this.m_docElement;
   }

   public int getDocument() {
      return this.m_docHandle;
   }

   public int getOwnerDocument(int nodeHandle) {
      return (nodeHandle & 8388607) == 0 ? -1 : nodeHandle & -8388608;
   }

   public int getDocumentRoot(int nodeHandle) {
      return (nodeHandle & 8388607) == 0 ? -1 : nodeHandle & -8388608;
   }

   public XMLString getStringValue(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      int nodetype = this.gotslot[0] & 255;
      String value = null;
      switch (nodetype) {
         case 3:
         case 4:
         case 8:
            value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
         case 1:
         case 2:
         case 5:
         case 6:
         case 7:
         default:
            return this.m_xsf.newstr(value);
      }
   }

   public int getStringValueChunkCount(int nodeHandle) {
      return 0;
   }

   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
      return new char[0];
   }

   public int getExpandedTypeID(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      String qName = this.m_localNames.indexToString(this.gotslot[3]);
      int colonpos = qName.indexOf(":");
      String localName = qName.substring(colonpos + 1);
      String namespace = this.m_nsNames.indexToString(this.gotslot[0] << 16);
      String expandedName = namespace + ":" + localName;
      int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
      return expandedNameID;
   }

   public int getExpandedTypeID(String namespace, String localName, int type) {
      String expandedName = namespace + ":" + localName;
      int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
      return expandedNameID;
   }

   public String getLocalNameFromExpandedNameID(int ExpandedNameID) {
      String expandedName = this.m_localNames.indexToString(ExpandedNameID);
      int colonpos = expandedName.indexOf(":");
      String localName = expandedName.substring(colonpos + 1);
      return localName;
   }

   public String getNamespaceFromExpandedNameID(int ExpandedNameID) {
      String expandedName = this.m_localNames.indexToString(ExpandedNameID);
      int colonpos = expandedName.indexOf(":");
      String nsName = expandedName.substring(0, colonpos);
      return nsName;
   }

   public String getNodeName(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      String name = fixednames[type];
      if (null == name) {
         int i = this.gotslot[3];
         System.out.println("got i=" + i + " " + (i >> 16) + "/" + (i & '\uffff'));
         name = this.m_localNames.indexToString(i & '\uffff');
         String prefix = this.m_prefixNames.indexToString(i >> 16);
         if (prefix != null && prefix.length() > 0) {
            name = prefix + ":" + name;
         }
      }

      return name;
   }

   public String getNodeNameX(int nodeHandle) {
      return null;
   }

   public String getLocalName(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      String name = "";
      if (type == 1 || type == 2) {
         int i = this.gotslot[3];
         name = this.m_localNames.indexToString(i & '\uffff');
         if (name == null) {
            name = "";
         }
      }

      return name;
   }

   public String getPrefix(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      short type = (short)(this.gotslot[0] & '\uffff');
      String name = "";
      if (type == 1 || type == 2) {
         int i = this.gotslot[3];
         name = this.m_prefixNames.indexToString(i >> 16);
         if (name == null) {
            name = "";
         }
      }

      return name;
   }

   public String getNamespaceURI(int nodeHandle) {
      return null;
   }

   public String getNodeValue(int nodeHandle) {
      this.nodes.readSlot(nodeHandle, this.gotslot);
      int nodetype = this.gotslot[0] & 255;
      String value = null;
      switch (nodetype) {
         case 2:
            this.nodes.readSlot(nodeHandle + 1, this.gotslot);
         case 3:
         case 4:
         case 8:
            value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
         case 1:
         case 5:
         case 6:
         case 7:
         default:
            return value;
      }
   }

   public short getNodeType(int nodeHandle) {
      return (short)(this.nodes.readEntry(nodeHandle, 0) & '\uffff');
   }

   public short getLevel(int nodeHandle) {
      short count;
      for(count = 0; nodeHandle != 0; nodeHandle = this.nodes.readEntry(nodeHandle, 1)) {
         ++count;
      }

      return count;
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
      return 0;
   }

   public String getUnparsedEntityURI(String name) {
      return null;
   }

   public boolean supportsPreStripping() {
      return false;
   }

   public boolean isNodeAfter(int nodeHandle1, int nodeHandle2) {
      return false;
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
      return null;
   }

   public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
      boolean sameDoc = (newChild & -8388608) == this.m_docHandle;
      if (!clone && !sameDoc) {
      }

   }

   public void appendTextChild(String str) {
   }

   void appendTextChild(int m_char_current_start, int contentLength) {
      int w0 = 3;
      int w1 = this.currentParent;
      int ourslot = this.appendNode(w0, w1, m_char_current_start, contentLength);
      this.previousSibling = ourslot;
   }

   void appendComment(int m_char_current_start, int contentLength) {
      int w0 = 8;
      int w1 = this.currentParent;
      int ourslot = this.appendNode(w0, w1, m_char_current_start, contentLength);
      this.previousSibling = ourslot;
   }

   void appendStartElement(int namespaceIndex, int localNameIndex, int prefixIndex) {
      int w0 = namespaceIndex << 16 | 1;
      int w1 = this.currentParent;
      int w2 = 0;
      int w3 = localNameIndex | prefixIndex << 16;
      System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & '\uffff'));
      int ourslot = this.appendNode(w0, w1, w2, w3);
      this.currentParent = ourslot;
      this.previousSibling = 0;
      if (this.m_docElement == -1) {
         this.m_docElement = ourslot;
      }

   }

   void appendNSDeclaration(int prefixIndex, int namespaceIndex, boolean isID) {
      int namespaceForNamespaces = this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/");
      int w0 = 13 | this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/") << 16;
      int w1 = this.currentParent;
      int w2 = 0;
      int ourslot = this.appendNode(w0, w1, w2, namespaceIndex);
      this.previousSibling = ourslot;
      this.previousSiblingWasParent = false;
   }

   void appendAttribute(int namespaceIndex, int localNameIndex, int prefixIndex, boolean isID, int m_char_current_start, int contentLength) {
      int w0 = 2 | namespaceIndex << 16;
      int w1 = this.currentParent;
      int w2 = 0;
      int w3 = localNameIndex | prefixIndex << 16;
      System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & '\uffff'));
      int ourslot = this.appendNode(w0, w1, w2, w3);
      this.previousSibling = ourslot;
      int w0 = 3;
      this.appendNode(w0, ourslot, m_char_current_start, contentLength);
      this.previousSiblingWasParent = true;
   }

   public DTMAxisTraverser getAxisTraverser(int axis) {
      return null;
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      return null;
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      return null;
   }

   void appendEndElement() {
      if (this.previousSiblingWasParent) {
         this.nodes.writeEntry(this.previousSibling, 2, -1);
      }

      this.previousSibling = this.currentParent;
      this.nodes.readSlot(this.currentParent, this.gotslot);
      this.currentParent = this.gotslot[1] & '\uffff';
      this.previousSiblingWasParent = true;
   }

   void appendStartDocument() {
      this.m_docElement = -1;
      this.initDocument(0);
   }

   void appendEndDocument() {
      this.done = true;
   }

   public void setProperty(String property, Object value) {
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
}
