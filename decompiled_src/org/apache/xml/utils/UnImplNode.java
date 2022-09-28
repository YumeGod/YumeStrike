package org.apache.xml.utils;

import org.apache.xml.res.XMLMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class UnImplNode implements Node, Element, NodeList, Document {
   protected String fDocumentURI;
   protected String actualEncoding;
   private String xmlEncoding;
   private boolean xmlStandalone;
   private String xmlVersion;

   public void error(String msg) {
      System.out.println("DOM ERROR! class: " + this.getClass().getName());
      throw new RuntimeException(XMLMessages.createXMLMessage(msg, (Object[])null));
   }

   public void error(String msg, Object[] args) {
      System.out.println("DOM ERROR! class: " + this.getClass().getName());
      throw new RuntimeException(XMLMessages.createXMLMessage(msg, args));
   }

   public Node appendChild(Node newChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasChildNodes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public short getNodeType() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return 0;
   }

   public Node getParentNode() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public NodeList getChildNodes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getFirstChild() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getLastChild() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getNextSibling() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public int getLength() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return 0;
   }

   public Node item(int index) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Document getOwnerDocument() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getTagName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getNodeName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void normalize() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public NodeList getElementsByTagName(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr setAttributeNode(Attr newAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasAttribute(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public boolean hasAttributeNS(String name, String x) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public Attr getAttributeNode(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void removeAttribute(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setAttribute(String name, String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getAttribute(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasAttributes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr getAttributeNodeNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getAttributeNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getPreviousSibling() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node cloneNode(boolean deep) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getNodeValue() throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setNodeValue(String nodeValue) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setValue(String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public Element getOwnerElement() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean getSpecified() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public NamedNodeMap getAttributes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node insertBefore(Node newChild, Node refChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node removeChild(Node oldChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean isSupported(String feature, String version) {
      return false;
   }

   public String getNamespaceURI() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getPrefix() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setPrefix(String prefix) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getLocalName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DocumentType getDoctype() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DOMImplementation getImplementation() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element getDocumentElement() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element createElement(String tagName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DocumentFragment createDocumentFragment() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Text createTextNode(String data) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Comment createComment(String data) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public CDATASection createCDATASection(String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr createAttribute(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public EntityReference createEntityReference(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node importNode(Node importedNode, boolean deep) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element getElementById(String elementId) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setData(String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String substringData(int offset, int count) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void appendData(String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void insertData(int offset, String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void deleteData(int offset, int count) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void replaceData(int offset, int count, String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public Text splitText(int offset) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node adoptNode(Node source) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getInputEncoding() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setInputEncoding(String encoding) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public boolean getStrictErrorChecking() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public void setStrictErrorChecking(boolean strictErrorChecking) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public Object setUserData(String key, Object data, UserDataHandler handler) {
      return this.getOwnerDocument().setUserData(key, data, handler);
   }

   public Object getUserData(String key) {
      return this.getOwnerDocument().getUserData(key);
   }

   public Object getFeature(String feature, String version) {
      return this.isSupported(feature, version) ? this : null;
   }

   public boolean isEqualNode(Node arg) {
      if (arg == this) {
         return true;
      } else if (arg.getNodeType() != this.getNodeType()) {
         return false;
      } else {
         if (this.getNodeName() == null) {
            if (arg.getNodeName() != null) {
               return false;
            }
         } else if (!this.getNodeName().equals(arg.getNodeName())) {
            return false;
         }

         if (this.getLocalName() == null) {
            if (arg.getLocalName() != null) {
               return false;
            }
         } else if (!this.getLocalName().equals(arg.getLocalName())) {
            return false;
         }

         if (this.getNamespaceURI() == null) {
            if (arg.getNamespaceURI() != null) {
               return false;
            }
         } else if (!this.getNamespaceURI().equals(arg.getNamespaceURI())) {
            return false;
         }

         if (this.getPrefix() == null) {
            if (arg.getPrefix() != null) {
               return false;
            }
         } else if (!this.getPrefix().equals(arg.getPrefix())) {
            return false;
         }

         if (this.getNodeValue() == null) {
            if (arg.getNodeValue() != null) {
               return false;
            }
         } else if (!this.getNodeValue().equals(arg.getNodeValue())) {
            return false;
         }

         return true;
      }
   }

   public String lookupNamespaceURI(String specifiedPrefix) {
      short type = this.getNodeType();
      switch (type) {
         case 1:
            String namespace = this.getNamespaceURI();
            String prefix = this.getPrefix();
            if (namespace != null) {
               if (specifiedPrefix == null && prefix == specifiedPrefix) {
                  return namespace;
               }

               if (prefix != null && prefix.equals(specifiedPrefix)) {
                  return namespace;
               }
            }

            if (this.hasAttributes()) {
               NamedNodeMap map = this.getAttributes();
               int length = map.getLength();

               for(int i = 0; i < length; ++i) {
                  Node attr = map.item(i);
                  String attrPrefix = attr.getPrefix();
                  String value = attr.getNodeValue();
                  namespace = attr.getNamespaceURI();
                  if (namespace != null && namespace.equals("http://www.w3.org/2000/xmlns/")) {
                     if (specifiedPrefix == null && attr.getNodeName().equals("xmlns")) {
                        return value;
                     }

                     if (attrPrefix != null && attrPrefix.equals("xmlns") && attr.getLocalName().equals(specifiedPrefix)) {
                        return value;
                     }
                  }
               }
            }

            return null;
         case 2:
            if (this.getOwnerElement().getNodeType() == 1) {
               return this.getOwnerElement().lookupNamespaceURI(specifiedPrefix);
            }

            return null;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 9:
         default:
            return null;
         case 6:
         case 10:
         case 11:
         case 12:
            return null;
      }
   }

   public boolean isDefaultNamespace(String namespaceURI) {
      return false;
   }

   public String lookupPrefix(String namespaceURI) {
      if (namespaceURI == null) {
         return null;
      } else {
         short type = this.getNodeType();
         switch (type) {
            case 2:
               if (this.getOwnerElement().getNodeType() == 1) {
                  return this.getOwnerElement().lookupPrefix(namespaceURI);
               }

               return null;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            default:
               return null;
            case 6:
            case 10:
            case 11:
            case 12:
               return null;
         }
      }
   }

   public boolean isSameNode(Node other) {
      return this == other;
   }

   public void setTextContent(String textContent) throws DOMException {
      this.setNodeValue(textContent);
   }

   public String getTextContent() throws DOMException {
      return this.getNodeValue();
   }

   public short compareDocumentPosition(Node other) throws DOMException {
      return 0;
   }

   public String getBaseURI() {
      return null;
   }

   public Node renameNode(Node n, String namespaceURI, String name) throws DOMException {
      return n;
   }

   public void normalizeDocument() {
   }

   public DOMConfiguration getDomConfig() {
      return null;
   }

   public void setDocumentURI(String documentURI) {
      this.fDocumentURI = documentURI;
   }

   public String getDocumentURI() {
      return this.fDocumentURI;
   }

   public String getActualEncoding() {
      return this.actualEncoding;
   }

   public void setActualEncoding(String value) {
      this.actualEncoding = value;
   }

   public Text replaceWholeText(String content) throws DOMException {
      return null;
   }

   public String getWholeText() {
      return null;
   }

   public boolean isWhitespaceInElementContent() {
      return false;
   }

   public void setIdAttribute(boolean id) {
   }

   public void setIdAttribute(String name, boolean makeId) {
   }

   public void setIdAttributeNode(Attr at, boolean makeId) {
   }

   public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId) {
   }

   public TypeInfo getSchemaTypeInfo() {
      return null;
   }

   public boolean isId() {
      return false;
   }

   public String getXmlEncoding() {
      return this.xmlEncoding;
   }

   public void setXmlEncoding(String xmlEncoding) {
      this.xmlEncoding = xmlEncoding;
   }

   public boolean getXmlStandalone() {
      return this.xmlStandalone;
   }

   public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
      this.xmlStandalone = xmlStandalone;
   }

   public String getXmlVersion() {
      return this.xmlVersion;
   }

   public void setXmlVersion(String xmlVersion) throws DOMException {
      this.xmlVersion = xmlVersion;
   }
}
