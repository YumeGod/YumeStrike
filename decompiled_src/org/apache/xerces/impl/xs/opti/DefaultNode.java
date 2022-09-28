package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class DefaultNode implements Node {
   public String getNodeName() {
      return null;
   }

   public String getNodeValue() throws DOMException {
      return null;
   }

   public short getNodeType() {
      return -1;
   }

   public Node getParentNode() {
      return null;
   }

   public NodeList getChildNodes() {
      return null;
   }

   public Node getFirstChild() {
      return null;
   }

   public Node getLastChild() {
      return null;
   }

   public Node getPreviousSibling() {
      return null;
   }

   public Node getNextSibling() {
      return null;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public Document getOwnerDocument() {
      return null;
   }

   public boolean hasChildNodes() {
      return false;
   }

   public Node cloneNode(boolean var1) {
      return null;
   }

   public void normalize() {
   }

   public boolean isSupported(String var1, String var2) {
      return false;
   }

   public String getNamespaceURI() {
      return null;
   }

   public String getPrefix() {
      return null;
   }

   public String getLocalName() {
      return null;
   }

   public String getBaseURI() {
      return null;
   }

   public boolean hasAttributes() {
      return false;
   }

   public void setNodeValue(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node removeChild(Node var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node appendChild(Node var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setPrefix(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public short compareDocumentPosition(Node var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public String getTextContent() throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public void setTextContent(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public boolean isSameNode(Node var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public String lookupPrefix(String var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public boolean isDefaultNamespace(String var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public String lookupNamespaceURI(String var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public boolean isEqualNode(Node var1) {
      throw new DOMException((short)9, "Method not supported");
   }

   public Object getFeature(String var1, String var2) {
      return null;
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) {
      throw new DOMException((short)9, "Method not supported");
   }

   public Object getUserData(String var1) {
      return null;
   }
}
