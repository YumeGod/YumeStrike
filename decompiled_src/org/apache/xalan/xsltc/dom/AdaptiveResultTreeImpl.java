package org.apache.xalan.xsltc.dom;

import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.XMLString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class AdaptiveResultTreeImpl extends SimpleResultTreeImpl {
   private static int _documentURIIndex = 0;
   private SAXImpl _dom;
   private DTMWSFilter _wsfilter;
   private int _initSize;
   private boolean _buildIdIndex;
   private final AttributeList _attributes = new AttributeList();
   private String _openElementName;

   public AdaptiveResultTreeImpl(XSLTCDTMManager dtmManager, int documentID, DTMWSFilter wsfilter, int initSize, boolean buildIdIndex) {
      super(dtmManager, documentID);
      this._wsfilter = wsfilter;
      this._initSize = initSize;
      this._buildIdIndex = buildIdIndex;
   }

   public DOM getNestedDOM() {
      return this._dom;
   }

   public int getDocument() {
      return this._dom != null ? this._dom.getDocument() : super.getDocument();
   }

   public String getStringValue() {
      return this._dom != null ? this._dom.getStringValue() : super.getStringValue();
   }

   public DTMAxisIterator getIterator() {
      return this._dom != null ? this._dom.getIterator() : super.getIterator();
   }

   public DTMAxisIterator getChildren(int node) {
      return this._dom != null ? this._dom.getChildren(node) : super.getChildren(node);
   }

   public DTMAxisIterator getTypedChildren(int type) {
      return this._dom != null ? this._dom.getTypedChildren(type) : super.getTypedChildren(type);
   }

   public DTMAxisIterator getAxisIterator(int axis) {
      return this._dom != null ? this._dom.getAxisIterator(axis) : super.getAxisIterator(axis);
   }

   public DTMAxisIterator getTypedAxisIterator(int axis, int type) {
      return this._dom != null ? this._dom.getTypedAxisIterator(axis, type) : super.getTypedAxisIterator(axis, type);
   }

   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself) {
      return this._dom != null ? this._dom.getNthDescendant(node, n, includeself) : super.getNthDescendant(node, n, includeself);
   }

   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns) {
      return this._dom != null ? this._dom.getNamespaceAxisIterator(axis, ns) : super.getNamespaceAxisIterator(axis, ns);
   }

   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op) {
      return this._dom != null ? this._dom.getNodeValueIterator(iter, returnType, value, op) : super.getNodeValueIterator(iter, returnType, value, op);
   }

   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
      return this._dom != null ? this._dom.orderNodes(source, node) : super.orderNodes(source, node);
   }

   public String getNodeName(int node) {
      return this._dom != null ? this._dom.getNodeName(node) : super.getNodeName(node);
   }

   public String getNodeNameX(int node) {
      return this._dom != null ? this._dom.getNodeNameX(node) : super.getNodeNameX(node);
   }

   public String getNamespaceName(int node) {
      return this._dom != null ? this._dom.getNamespaceName(node) : super.getNamespaceName(node);
   }

   public int getExpandedTypeID(int nodeHandle) {
      return this._dom != null ? this._dom.getExpandedTypeID(nodeHandle) : super.getExpandedTypeID(nodeHandle);
   }

   public int getNamespaceType(int node) {
      return this._dom != null ? this._dom.getNamespaceType(node) : super.getNamespaceType(node);
   }

   public int getParent(int nodeHandle) {
      return this._dom != null ? this._dom.getParent(nodeHandle) : super.getParent(nodeHandle);
   }

   public int getAttributeNode(int gType, int element) {
      return this._dom != null ? this._dom.getAttributeNode(gType, element) : super.getAttributeNode(gType, element);
   }

   public String getStringValueX(int nodeHandle) {
      return this._dom != null ? this._dom.getStringValueX(nodeHandle) : super.getStringValueX(nodeHandle);
   }

   public void copy(int node, SerializationHandler handler) throws TransletException {
      if (this._dom != null) {
         this._dom.copy(node, handler);
      } else {
         super.copy(node, handler);
      }

   }

   public void copy(DTMAxisIterator nodes, SerializationHandler handler) throws TransletException {
      if (this._dom != null) {
         this._dom.copy(nodes, handler);
      } else {
         super.copy(nodes, handler);
      }

   }

   public String shallowCopy(int node, SerializationHandler handler) throws TransletException {
      return this._dom != null ? this._dom.shallowCopy(node, handler) : super.shallowCopy(node, handler);
   }

   public boolean lessThan(int node1, int node2) {
      return this._dom != null ? this._dom.lessThan(node1, node2) : super.lessThan(node1, node2);
   }

   public void characters(int node, SerializationHandler handler) throws TransletException {
      if (this._dom != null) {
         this._dom.characters(node, handler);
      } else {
         super.characters(node, handler);
      }

   }

   public Node makeNode(int index) {
      return this._dom != null ? this._dom.makeNode(index) : super.makeNode(index);
   }

   public Node makeNode(DTMAxisIterator iter) {
      return this._dom != null ? this._dom.makeNode(iter) : super.makeNode(iter);
   }

   public NodeList makeNodeList(int index) {
      return this._dom != null ? this._dom.makeNodeList(index) : super.makeNodeList(index);
   }

   public NodeList makeNodeList(DTMAxisIterator iter) {
      return this._dom != null ? this._dom.makeNodeList(iter) : super.makeNodeList(iter);
   }

   public String getLanguage(int node) {
      return this._dom != null ? this._dom.getLanguage(node) : super.getLanguage(node);
   }

   public int getSize() {
      return this._dom != null ? this._dom.getSize() : super.getSize();
   }

   public String getDocumentURI(int node) {
      return this._dom != null ? this._dom.getDocumentURI(node) : "adaptive_rtf" + _documentURIIndex++;
   }

   public void setFilter(StripFilter filter) {
      if (this._dom != null) {
         this._dom.setFilter(filter);
      } else {
         super.setFilter(filter);
      }

   }

   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces) {
      if (this._dom != null) {
         this._dom.setupMapping(names, uris, types, namespaces);
      } else {
         super.setupMapping(names, uris, types, namespaces);
      }

   }

   public boolean isElement(int node) {
      return this._dom != null ? this._dom.isElement(node) : super.isElement(node);
   }

   public boolean isAttribute(int node) {
      return this._dom != null ? this._dom.isAttribute(node) : super.isAttribute(node);
   }

   public String lookupNamespace(int node, String prefix) throws TransletException {
      return this._dom != null ? this._dom.lookupNamespace(node, prefix) : super.lookupNamespace(node, prefix);
   }

   public final int getNodeIdent(int nodehandle) {
      return this._dom != null ? this._dom.getNodeIdent(nodehandle) : super.getNodeIdent(nodehandle);
   }

   public final int getNodeHandle(int nodeId) {
      return this._dom != null ? this._dom.getNodeHandle(nodeId) : super.getNodeHandle(nodeId);
   }

   public DOM getResultTreeFrag(int initialSize, int rtfType) {
      return this._dom != null ? this._dom.getResultTreeFrag(initialSize, rtfType) : super.getResultTreeFrag(initialSize, rtfType);
   }

   public SerializationHandler getOutputDomBuilder() {
      return this;
   }

   public int getNSType(int node) {
      return this._dom != null ? this._dom.getNSType(node) : super.getNSType(node);
   }

   public String getUnparsedEntityURI(String name) {
      return this._dom != null ? this._dom.getUnparsedEntityURI(name) : super.getUnparsedEntityURI(name);
   }

   public Hashtable getElementsWithIDs() {
      return this._dom != null ? this._dom.getElementsWithIDs() : super.getElementsWithIDs();
   }

   private void maybeEmitStartElement() throws SAXException {
      if (this._openElementName != null) {
         int index;
         if ((index = this._openElementName.indexOf(":")) < 0) {
            this._dom.startElement((String)null, this._openElementName, this._openElementName, this._attributes);
         } else {
            this._dom.startElement((String)null, this._openElementName.substring(index + 1), this._openElementName, this._attributes);
         }

         this._openElementName = null;
      }

   }

   private void prepareNewDOM() throws SAXException {
      this._dom = (SAXImpl)super._dtmManager.getDTM((Source)null, true, this._wsfilter, true, false, false, this._initSize, this._buildIdIndex);
      this._dom.startDocument();

      for(int i = 0; i < super._size; ++i) {
         String str = super._textArray[i];
         this._dom.characters(str.toCharArray(), 0, str.length());
      }

      super._size = 0;
   }

   public void startDocument() throws SAXException {
   }

   public void endDocument() throws SAXException {
      if (this._dom != null) {
         this._dom.endDocument();
      } else {
         super.endDocument();
      }

   }

   public void characters(String str) throws SAXException {
      if (this._dom != null) {
         this.characters(str.toCharArray(), 0, str.length());
      } else {
         super.characters(str);
      }

   }

   public void characters(char[] ch, int offset, int length) throws SAXException {
      if (this._dom != null) {
         this.maybeEmitStartElement();
         this._dom.characters(ch, offset, length);
      } else {
         super.characters(ch, offset, length);
      }

   }

   public boolean setEscaping(boolean escape) throws SAXException {
      return this._dom != null ? this._dom.setEscaping(escape) : super.setEscaping(escape);
   }

   public void startElement(String elementName) throws SAXException {
      if (this._dom == null) {
         this.prepareNewDOM();
      }

      this.maybeEmitStartElement();
      this._openElementName = elementName;
      this._attributes.clear();
   }

   public void startElement(String uri, String localName, String qName) throws SAXException {
      this.startElement(qName);
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      this.startElement(qName);
   }

   public void endElement(String elementName) throws SAXException {
      this.maybeEmitStartElement();
      this._dom.endElement((String)null, (String)null, elementName);
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      this.endElement(qName);
   }

   public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
      this.addAttribute(qName, value);
   }

   public void addAttribute(String name, String value) {
      if (this._openElementName != null) {
         this._attributes.add(name, value);
      } else {
         BasisLibrary.runTimeError("STRAY_ATTRIBUTE_ERR", (Object)name);
      }

   }

   public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
      if (this._dom == null) {
         this.prepareNewDOM();
      }

      this._dom.startPrefixMapping(prefix, uri);
   }

   public void comment(String comment) throws SAXException {
      if (this._dom == null) {
         this.prepareNewDOM();
      }

      this.maybeEmitStartElement();
      char[] chars = comment.toCharArray();
      this._dom.comment(chars, 0, chars.length);
   }

   public void comment(char[] chars, int offset, int length) throws SAXException {
      if (this._dom == null) {
         this.prepareNewDOM();
      }

      this.maybeEmitStartElement();
      this._dom.comment(chars, offset, length);
   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (this._dom == null) {
         this.prepareNewDOM();
      }

      this.maybeEmitStartElement();
      this._dom.processingInstruction(target, data);
   }

   public void setFeature(String featureId, boolean state) {
      if (this._dom != null) {
         this._dom.setFeature(featureId, state);
      }

   }

   public void setProperty(String property, Object value) {
      if (this._dom != null) {
         this._dom.setProperty(property, value);
      }

   }

   public DTMAxisTraverser getAxisTraverser(int axis) {
      return this._dom != null ? this._dom.getAxisTraverser(axis) : super.getAxisTraverser(axis);
   }

   public boolean hasChildNodes(int nodeHandle) {
      return this._dom != null ? this._dom.hasChildNodes(nodeHandle) : super.hasChildNodes(nodeHandle);
   }

   public int getFirstChild(int nodeHandle) {
      return this._dom != null ? this._dom.getFirstChild(nodeHandle) : super.getFirstChild(nodeHandle);
   }

   public int getLastChild(int nodeHandle) {
      return this._dom != null ? this._dom.getLastChild(nodeHandle) : super.getLastChild(nodeHandle);
   }

   public int getAttributeNode(int elementHandle, String namespaceURI, String name) {
      return this._dom != null ? this._dom.getAttributeNode(elementHandle, namespaceURI, name) : super.getAttributeNode(elementHandle, namespaceURI, name);
   }

   public int getFirstAttribute(int nodeHandle) {
      return this._dom != null ? this._dom.getFirstAttribute(nodeHandle) : super.getFirstAttribute(nodeHandle);
   }

   public int getFirstNamespaceNode(int nodeHandle, boolean inScope) {
      return this._dom != null ? this._dom.getFirstNamespaceNode(nodeHandle, inScope) : super.getFirstNamespaceNode(nodeHandle, inScope);
   }

   public int getNextSibling(int nodeHandle) {
      return this._dom != null ? this._dom.getNextSibling(nodeHandle) : super.getNextSibling(nodeHandle);
   }

   public int getPreviousSibling(int nodeHandle) {
      return this._dom != null ? this._dom.getPreviousSibling(nodeHandle) : super.getPreviousSibling(nodeHandle);
   }

   public int getNextAttribute(int nodeHandle) {
      return this._dom != null ? this._dom.getNextAttribute(nodeHandle) : super.getNextAttribute(nodeHandle);
   }

   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope) {
      return this._dom != null ? this._dom.getNextNamespaceNode(baseHandle, namespaceHandle, inScope) : super.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
   }

   public int getOwnerDocument(int nodeHandle) {
      return this._dom != null ? this._dom.getOwnerDocument(nodeHandle) : super.getOwnerDocument(nodeHandle);
   }

   public int getDocumentRoot(int nodeHandle) {
      return this._dom != null ? this._dom.getDocumentRoot(nodeHandle) : super.getDocumentRoot(nodeHandle);
   }

   public XMLString getStringValue(int nodeHandle) {
      return this._dom != null ? this._dom.getStringValue(nodeHandle) : super.getStringValue(nodeHandle);
   }

   public int getStringValueChunkCount(int nodeHandle) {
      return this._dom != null ? this._dom.getStringValueChunkCount(nodeHandle) : super.getStringValueChunkCount(nodeHandle);
   }

   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen) {
      return this._dom != null ? this._dom.getStringValueChunk(nodeHandle, chunkIndex, startAndLen) : super.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
   }

   public int getExpandedTypeID(String namespace, String localName, int type) {
      return this._dom != null ? this._dom.getExpandedTypeID(namespace, localName, type) : super.getExpandedTypeID(namespace, localName, type);
   }

   public String getLocalNameFromExpandedNameID(int ExpandedNameID) {
      return this._dom != null ? this._dom.getLocalNameFromExpandedNameID(ExpandedNameID) : super.getLocalNameFromExpandedNameID(ExpandedNameID);
   }

   public String getNamespaceFromExpandedNameID(int ExpandedNameID) {
      return this._dom != null ? this._dom.getNamespaceFromExpandedNameID(ExpandedNameID) : super.getNamespaceFromExpandedNameID(ExpandedNameID);
   }

   public String getLocalName(int nodeHandle) {
      return this._dom != null ? this._dom.getLocalName(nodeHandle) : super.getLocalName(nodeHandle);
   }

   public String getPrefix(int nodeHandle) {
      return this._dom != null ? this._dom.getPrefix(nodeHandle) : super.getPrefix(nodeHandle);
   }

   public String getNamespaceURI(int nodeHandle) {
      return this._dom != null ? this._dom.getNamespaceURI(nodeHandle) : super.getNamespaceURI(nodeHandle);
   }

   public String getNodeValue(int nodeHandle) {
      return this._dom != null ? this._dom.getNodeValue(nodeHandle) : super.getNodeValue(nodeHandle);
   }

   public short getNodeType(int nodeHandle) {
      return this._dom != null ? this._dom.getNodeType(nodeHandle) : super.getNodeType(nodeHandle);
   }

   public short getLevel(int nodeHandle) {
      return this._dom != null ? this._dom.getLevel(nodeHandle) : super.getLevel(nodeHandle);
   }

   public boolean isSupported(String feature, String version) {
      return this._dom != null ? this._dom.isSupported(feature, version) : super.isSupported(feature, version);
   }

   public String getDocumentBaseURI() {
      return this._dom != null ? this._dom.getDocumentBaseURI() : super.getDocumentBaseURI();
   }

   public void setDocumentBaseURI(String baseURI) {
      if (this._dom != null) {
         this._dom.setDocumentBaseURI(baseURI);
      } else {
         super.setDocumentBaseURI(baseURI);
      }

   }

   public String getDocumentSystemIdentifier(int nodeHandle) {
      return this._dom != null ? this._dom.getDocumentSystemIdentifier(nodeHandle) : super.getDocumentSystemIdentifier(nodeHandle);
   }

   public String getDocumentEncoding(int nodeHandle) {
      return this._dom != null ? this._dom.getDocumentEncoding(nodeHandle) : super.getDocumentEncoding(nodeHandle);
   }

   public String getDocumentStandalone(int nodeHandle) {
      return this._dom != null ? this._dom.getDocumentStandalone(nodeHandle) : super.getDocumentStandalone(nodeHandle);
   }

   public String getDocumentVersion(int documentHandle) {
      return this._dom != null ? this._dom.getDocumentVersion(documentHandle) : super.getDocumentVersion(documentHandle);
   }

   public boolean getDocumentAllDeclarationsProcessed() {
      return this._dom != null ? this._dom.getDocumentAllDeclarationsProcessed() : super.getDocumentAllDeclarationsProcessed();
   }

   public String getDocumentTypeDeclarationSystemIdentifier() {
      return this._dom != null ? this._dom.getDocumentTypeDeclarationSystemIdentifier() : super.getDocumentTypeDeclarationSystemIdentifier();
   }

   public String getDocumentTypeDeclarationPublicIdentifier() {
      return this._dom != null ? this._dom.getDocumentTypeDeclarationPublicIdentifier() : super.getDocumentTypeDeclarationPublicIdentifier();
   }

   public int getElementById(String elementId) {
      return this._dom != null ? this._dom.getElementById(elementId) : super.getElementById(elementId);
   }

   public boolean supportsPreStripping() {
      return this._dom != null ? this._dom.supportsPreStripping() : super.supportsPreStripping();
   }

   public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle) {
      return this._dom != null ? this._dom.isNodeAfter(firstNodeHandle, secondNodeHandle) : super.isNodeAfter(firstNodeHandle, secondNodeHandle);
   }

   public boolean isCharacterElementContentWhitespace(int nodeHandle) {
      return this._dom != null ? this._dom.isCharacterElementContentWhitespace(nodeHandle) : super.isCharacterElementContentWhitespace(nodeHandle);
   }

   public boolean isDocumentAllDeclarationsProcessed(int documentHandle) {
      return this._dom != null ? this._dom.isDocumentAllDeclarationsProcessed(documentHandle) : super.isDocumentAllDeclarationsProcessed(documentHandle);
   }

   public boolean isAttributeSpecified(int attributeHandle) {
      return this._dom != null ? this._dom.isAttributeSpecified(attributeHandle) : super.isAttributeSpecified(attributeHandle);
   }

   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize) throws SAXException {
      if (this._dom != null) {
         this._dom.dispatchCharactersEvents(nodeHandle, ch, normalize);
      } else {
         super.dispatchCharactersEvents(nodeHandle, ch, normalize);
      }

   }

   public void dispatchToEvents(int nodeHandle, ContentHandler ch) throws SAXException {
      if (this._dom != null) {
         this._dom.dispatchToEvents(nodeHandle, ch);
      } else {
         super.dispatchToEvents(nodeHandle, ch);
      }

   }

   public Node getNode(int nodeHandle) {
      return this._dom != null ? this._dom.getNode(nodeHandle) : super.getNode(nodeHandle);
   }

   public boolean needsTwoThreads() {
      return this._dom != null ? this._dom.needsTwoThreads() : super.needsTwoThreads();
   }

   public ContentHandler getContentHandler() {
      return this._dom != null ? this._dom.getContentHandler() : super.getContentHandler();
   }

   public LexicalHandler getLexicalHandler() {
      return this._dom != null ? this._dom.getLexicalHandler() : super.getLexicalHandler();
   }

   public EntityResolver getEntityResolver() {
      return this._dom != null ? this._dom.getEntityResolver() : super.getEntityResolver();
   }

   public DTDHandler getDTDHandler() {
      return this._dom != null ? this._dom.getDTDHandler() : super.getDTDHandler();
   }

   public ErrorHandler getErrorHandler() {
      return this._dom != null ? this._dom.getErrorHandler() : super.getErrorHandler();
   }

   public DeclHandler getDeclHandler() {
      return this._dom != null ? this._dom.getDeclHandler() : super.getDeclHandler();
   }

   public void appendChild(int newChild, boolean clone, boolean cloneDepth) {
      if (this._dom != null) {
         this._dom.appendChild(newChild, clone, cloneDepth);
      } else {
         super.appendChild(newChild, clone, cloneDepth);
      }

   }

   public void appendTextChild(String str) {
      if (this._dom != null) {
         this._dom.appendTextChild(str);
      } else {
         super.appendTextChild(str);
      }

   }

   public SourceLocator getSourceLocatorFor(int node) {
      return this._dom != null ? this._dom.getSourceLocatorFor(node) : super.getSourceLocatorFor(node);
   }

   public void documentRegistration() {
      if (this._dom != null) {
         this._dom.documentRegistration();
      } else {
         super.documentRegistration();
      }

   }

   public void documentRelease() {
      if (this._dom != null) {
         this._dom.documentRelease();
      } else {
         super.documentRelease();
      }

   }
}
