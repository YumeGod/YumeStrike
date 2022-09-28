package org.apache.xml.dtm.ref;

import java.util.Vector;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
import org.apache.xpath.NodeSet;
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

public class DTMNodeProxy implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment {
   public DTM dtm;
   int node;
   private static final String EMPTYSTRING = "";
   static final DOMImplementation implementation = new DTMNodeProxyImplementation();
   protected String fDocumentURI;
   protected String actualEncoding;
   private String xmlEncoding;
   private boolean xmlStandalone;
   private String xmlVersion;

   public DTMNodeProxy(DTM dtm, int node) {
      this.dtm = dtm;
      this.node = node;
   }

   public final DTM getDTM() {
      return this.dtm;
   }

   public final int getDTMNodeNumber() {
      return this.node;
   }

   public final boolean equals(Node node) {
      try {
         DTMNodeProxy dtmp = (DTMNodeProxy)node;
         return dtmp.node == this.node && dtmp.dtm == this.dtm;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public final boolean equals(Object node) {
      try {
         return this.equals((Node)node);
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public final boolean sameNodeAs(Node other) {
      if (!(other instanceof DTMNodeProxy)) {
         return false;
      } else {
         DTMNodeProxy that = (DTMNodeProxy)other;
         return this.dtm == that.dtm && this.node == that.node;
      }
   }

   public final String getNodeName() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getTarget() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getLocalName() {
      return this.dtm.getLocalName(this.node);
   }

   public final String getPrefix() {
      return this.dtm.getPrefix(this.node);
   }

   public final void setPrefix(String prefix) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final String getNamespaceURI() {
      return this.dtm.getNamespaceURI(this.node);
   }

   public final boolean supports(String feature, String version) {
      return implementation.hasFeature(feature, version);
   }

   public final boolean isSupported(String feature, String version) {
      return implementation.hasFeature(feature, version);
   }

   public final String getNodeValue() throws DOMException {
      return this.dtm.getNodeValue(this.node);
   }

   public final String getStringValue() throws DOMException {
      return this.dtm.getStringValue(this.node).toString();
   }

   public final void setNodeValue(String nodeValue) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final short getNodeType() {
      return this.dtm.getNodeType(this.node);
   }

   public final Node getParentNode() {
      if (this.getNodeType() == 2) {
         return null;
      } else {
         int newnode = this.dtm.getParent(this.node);
         return newnode == -1 ? null : this.dtm.getNode(newnode);
      }
   }

   public final Node getOwnerNode() {
      int newnode = this.dtm.getParent(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final NodeList getChildNodes() {
      return new DTMChildIterNodeList(this.dtm, this.node);
   }

   public final Node getFirstChild() {
      int newnode = this.dtm.getFirstChild(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getLastChild() {
      int newnode = this.dtm.getLastChild(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getPreviousSibling() {
      int newnode = this.dtm.getPreviousSibling(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getNextSibling() {
      if (this.dtm.getNodeType(this.node) == 2) {
         return null;
      } else {
         int newnode = this.dtm.getNextSibling(this.node);
         return newnode == -1 ? null : this.dtm.getNode(newnode);
      }
   }

   public final NamedNodeMap getAttributes() {
      return new DTMNamedNodeMap(this.dtm, this.node);
   }

   public boolean hasAttribute(String name) {
      return -1 != this.dtm.getAttributeNode(this.node, (String)null, name);
   }

   public boolean hasAttributeNS(String namespaceURI, String localName) {
      return -1 != this.dtm.getAttributeNode(this.node, namespaceURI, localName);
   }

   public final Document getOwnerDocument() {
      return (Document)this.dtm.getNode(this.dtm.getOwnerDocument(this.node));
   }

   public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node removeChild(Node oldChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node appendChild(Node newChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final boolean hasChildNodes() {
      return -1 != this.dtm.getFirstChild(this.node);
   }

   public final Node cloneNode(boolean deep) {
      throw new DTMDOMException((short)9);
   }

   public final DocumentType getDoctype() {
      return null;
   }

   public final DOMImplementation getImplementation() {
      return implementation;
   }

   public final Element getDocumentElement() {
      int dochandle = this.dtm.getDocument();
      int elementhandle = -1;

      for(int kidhandle = this.dtm.getFirstChild(dochandle); kidhandle != -1; kidhandle = this.dtm.getNextSibling(kidhandle)) {
         switch (this.dtm.getNodeType(kidhandle)) {
            case 1:
               if (elementhandle != -1) {
                  elementhandle = -1;
                  kidhandle = this.dtm.getLastChild(dochandle);
               } else {
                  elementhandle = kidhandle;
               }
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            default:
               elementhandle = -1;
               kidhandle = this.dtm.getLastChild(dochandle);
            case 7:
            case 8:
            case 10:
         }
      }

      if (elementhandle == -1) {
         throw new DTMDOMException((short)9);
      } else {
         return (Element)this.dtm.getNode(elementhandle);
      }
   }

   public final Element createElement(String tagName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final DocumentFragment createDocumentFragment() {
      throw new DTMDOMException((short)9);
   }

   public final Text createTextNode(String data) {
      throw new DTMDOMException((short)9);
   }

   public final Comment createComment(String data) {
      throw new DTMDOMException((short)9);
   }

   public final CDATASection createCDATASection(String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr createAttribute(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final EntityReference createEntityReference(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final NodeList getElementsByTagName(String tagname) {
      Vector listVector = new Vector();
      Node retNode = this.dtm.getNode(this.node);
      int i;
      if (retNode != null) {
         boolean isTagNameWildCard = "*".equals(tagname);
         if (1 == retNode.getNodeType()) {
            NodeList nodeList = retNode.getChildNodes();

            for(i = 0; i < nodeList.getLength(); ++i) {
               this.traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
            }
         } else if (9 == retNode.getNodeType()) {
            this.traverseChildren(listVector, this.dtm.getNode(this.node), tagname, isTagNameWildCard);
         }
      }

      int size = listVector.size();
      NodeSet nodeSet = new NodeSet(size);

      for(i = 0; i < size; ++i) {
         nodeSet.addNode((Node)listVector.elementAt(i));
      }

      return nodeSet;
   }

   private final void traverseChildren(Vector listVector, Node tempNode, String tagname, boolean isTagNameWildCard) {
      if (tempNode != null) {
         if (tempNode.getNodeType() == 1 && (isTagNameWildCard || tempNode.getNodeName().equals(tagname))) {
            listVector.add(tempNode);
         }

         if (tempNode.hasChildNodes()) {
            NodeList nodeList = tempNode.getChildNodes();

            for(int i = 0; i < nodeList.getLength(); ++i) {
               this.traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
            }
         }

      }
   }

   public final Node importNode(Node importedNode, boolean deep) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
      Vector listVector = new Vector();
      Node retNode = this.dtm.getNode(this.node);
      if (retNode != null) {
         boolean isNamespaceURIWildCard = "*".equals(namespaceURI);
         boolean isLocalNameWildCard = "*".equals(localName);
         if (1 == retNode.getNodeType()) {
            NodeList nodeList = retNode.getChildNodes();

            for(int i = 0; i < nodeList.getLength(); ++i) {
               this.traverseChildren(listVector, nodeList.item(i), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
            }
         } else if (9 == retNode.getNodeType()) {
            this.traverseChildren(listVector, this.dtm.getNode(this.node), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
         }
      }

      int size = listVector.size();
      NodeSet nodeSet = new NodeSet(size);

      for(int i = 0; i < size; ++i) {
         nodeSet.addNode((Node)listVector.elementAt(i));
      }

      return nodeSet;
   }

   private final void traverseChildren(Vector listVector, Node tempNode, String namespaceURI, String localname, boolean isNamespaceURIWildCard, boolean isLocalNameWildCard) {
      if (tempNode != null) {
         if (tempNode.getNodeType() == 1 && (isLocalNameWildCard || tempNode.getLocalName().equals(localname))) {
            String nsURI = tempNode.getNamespaceURI();
            if (namespaceURI == null && nsURI == null || isNamespaceURIWildCard || namespaceURI != null && namespaceURI.equals(nsURI)) {
               listVector.add(tempNode);
            }
         }

         if (tempNode.hasChildNodes()) {
            NodeList nl = tempNode.getChildNodes();

            for(int i = 0; i < nl.getLength(); ++i) {
               this.traverseChildren(listVector, nl.item(i), namespaceURI, localname, isNamespaceURIWildCard, isLocalNameWildCard);
            }
         }

      }
   }

   public final Element getElementById(String elementId) {
      return (Element)this.dtm.getNode(this.dtm.getElementById(elementId));
   }

   public final Text splitText(int offset) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getData() throws DOMException {
      return this.dtm.getNodeValue(this.node);
   }

   public final void setData(String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final int getLength() {
      return this.dtm.getNodeValue(this.node).length();
   }

   public final String substringData(int offset, int count) throws DOMException {
      return this.getData().substring(offset, offset + count);
   }

   public final void appendData(String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void insertData(int offset, String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void deleteData(int offset, int count) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void replaceData(int offset, int count, String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getTagName() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getAttribute(String name) {
      DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
      Node node = map.getNamedItem(name);
      return null == node ? "" : node.getNodeValue();
   }

   public final void setAttribute(String name, String value) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void removeAttribute(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr getAttributeNode(String name) {
      DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
      return (Attr)map.getNamedItem(name);
   }

   public final Attr setAttributeNode(Attr newAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr removeAttributeNode(Attr oldAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public boolean hasAttributes() {
      return -1 != this.dtm.getFirstAttribute(this.node);
   }

   public final void normalize() {
      throw new DTMDOMException((short)9);
   }

   public final String getAttributeNS(String namespaceURI, String localName) {
      Node retNode = null;
      int n = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
      if (n != -1) {
         retNode = this.dtm.getNode(n);
      }

      return null == retNode ? "" : retNode.getNodeValue();
   }

   public final void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr getAttributeNodeNS(String namespaceURI, String localName) {
      Attr retAttr = null;
      int n = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
      if (n != -1) {
         retAttr = (Attr)this.dtm.getNode(n);
      }

      return retAttr;
   }

   public final Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getName() {
      return this.dtm.getNodeName(this.node);
   }

   public final boolean getSpecified() {
      return true;
   }

   public final String getValue() {
      return this.dtm.getNodeValue(this.node);
   }

   public final void setValue(String value) {
      throw new DTMDOMException((short)9);
   }

   public final Element getOwnerElement() {
      if (this.getNodeType() != 2) {
         return null;
      } else {
         int newnode = this.dtm.getParent(this.node);
         return newnode == -1 ? null : (Element)this.dtm.getNode(newnode);
      }
   }

   public Node adoptNode(Node source) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public String getInputEncoding() {
      throw new DTMDOMException((short)9);
   }

   public boolean getStrictErrorChecking() {
      throw new DTMDOMException((short)9);
   }

   public void setStrictErrorChecking(boolean strictErrorChecking) {
      throw new DTMDOMException((short)9);
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

   public boolean isElementContentWhitespace() {
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

   static class DTMNodeProxyImplementation implements DOMImplementation {
      public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) {
         throw new DTMDOMException((short)9);
      }

      public Document createDocument(String namespaceURI, String qualfiedName, DocumentType doctype) {
         throw new DTMDOMException((short)9);
      }

      public boolean hasFeature(String feature, String version) {
         return ("CORE".equals(feature.toUpperCase()) || "XML".equals(feature.toUpperCase())) && ("1.0".equals(version) || "2.0".equals(version));
      }

      public Object getFeature(String feature, String version) {
         return null;
      }
   }
}
