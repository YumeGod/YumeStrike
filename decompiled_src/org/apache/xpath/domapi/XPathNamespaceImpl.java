package org.apache.xpath.domapi;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.xpath.XPathNamespace;

class XPathNamespaceImpl implements XPathNamespace {
   private final Node m_attributeNode;
   private String textContent;

   XPathNamespaceImpl(Node node) {
      this.m_attributeNode = node;
   }

   public Element getOwnerElement() {
      return ((Attr)this.m_attributeNode).getOwnerElement();
   }

   public String getNodeName() {
      return "#namespace";
   }

   public String getNodeValue() throws DOMException {
      return this.m_attributeNode.getNodeValue();
   }

   public void setNodeValue(String arg0) throws DOMException {
   }

   public short getNodeType() {
      return 13;
   }

   public Node getParentNode() {
      return this.m_attributeNode.getParentNode();
   }

   public NodeList getChildNodes() {
      return this.m_attributeNode.getChildNodes();
   }

   public Node getFirstChild() {
      return this.m_attributeNode.getFirstChild();
   }

   public Node getLastChild() {
      return this.m_attributeNode.getLastChild();
   }

   public Node getPreviousSibling() {
      return this.m_attributeNode.getPreviousSibling();
   }

   public Node getNextSibling() {
      return this.m_attributeNode.getNextSibling();
   }

   public NamedNodeMap getAttributes() {
      return this.m_attributeNode.getAttributes();
   }

   public Document getOwnerDocument() {
      return this.m_attributeNode.getOwnerDocument();
   }

   public Node insertBefore(Node arg0, Node arg1) throws DOMException {
      return null;
   }

   public Node replaceChild(Node arg0, Node arg1) throws DOMException {
      return null;
   }

   public Node removeChild(Node arg0) throws DOMException {
      return null;
   }

   public Node appendChild(Node arg0) throws DOMException {
      return null;
   }

   public boolean hasChildNodes() {
      return false;
   }

   public Node cloneNode(boolean arg0) {
      throw new DOMException((short)9, (String)null);
   }

   public void normalize() {
      this.m_attributeNode.normalize();
   }

   public boolean isSupported(String arg0, String arg1) {
      return this.m_attributeNode.isSupported(arg0, arg1);
   }

   public String getNamespaceURI() {
      return this.m_attributeNode.getNodeValue();
   }

   public String getPrefix() {
      return this.m_attributeNode.getPrefix();
   }

   public void setPrefix(String arg0) throws DOMException {
   }

   public String getLocalName() {
      return this.m_attributeNode.getPrefix();
   }

   public boolean hasAttributes() {
      return this.m_attributeNode.hasAttributes();
   }

   public String getBaseURI() {
      return null;
   }

   public short compareDocumentPosition(Node other) throws DOMException {
      return 0;
   }

   public String getTextContent() throws DOMException {
      return this.textContent;
   }

   public void setTextContent(String textContent) throws DOMException {
      this.textContent = textContent;
   }

   public boolean isSameNode(Node other) {
      return false;
   }

   public String lookupPrefix(String namespaceURI) {
      return "";
   }

   public boolean isDefaultNamespace(String namespaceURI) {
      return false;
   }

   public String lookupNamespaceURI(String prefix) {
      return null;
   }

   public boolean isEqualNode(Node arg) {
      return false;
   }

   public Object getFeature(String feature, String version) {
      return null;
   }

   public Object setUserData(String key, Object data, UserDataHandler handler) {
      return null;
   }

   public Object getUserData(String key) {
      return null;
   }
}
