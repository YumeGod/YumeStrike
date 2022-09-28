package org.w3c.dom;

public interface Node {
   short ELEMENT_NODE = 1;
   short ATTRIBUTE_NODE = 2;
   short TEXT_NODE = 3;
   short CDATA_SECTION_NODE = 4;
   short ENTITY_REFERENCE_NODE = 5;
   short ENTITY_NODE = 6;
   short PROCESSING_INSTRUCTION_NODE = 7;
   short COMMENT_NODE = 8;
   short DOCUMENT_NODE = 9;
   short DOCUMENT_TYPE_NODE = 10;
   short DOCUMENT_FRAGMENT_NODE = 11;
   short NOTATION_NODE = 12;
   short DOCUMENT_POSITION_DISCONNECTED = 1;
   short DOCUMENT_POSITION_PRECEDING = 2;
   short DOCUMENT_POSITION_FOLLOWING = 4;
   short DOCUMENT_POSITION_CONTAINS = 8;
   short DOCUMENT_POSITION_CONTAINED_BY = 16;
   short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

   String getNodeName();

   String getNodeValue() throws DOMException;

   void setNodeValue(String var1) throws DOMException;

   short getNodeType();

   Node getParentNode();

   NodeList getChildNodes();

   Node getFirstChild();

   Node getLastChild();

   Node getPreviousSibling();

   Node getNextSibling();

   NamedNodeMap getAttributes();

   Document getOwnerDocument();

   Node insertBefore(Node var1, Node var2) throws DOMException;

   Node replaceChild(Node var1, Node var2) throws DOMException;

   Node removeChild(Node var1) throws DOMException;

   Node appendChild(Node var1) throws DOMException;

   boolean hasChildNodes();

   Node cloneNode(boolean var1);

   void normalize();

   boolean isSupported(String var1, String var2);

   String getNamespaceURI();

   String getPrefix();

   void setPrefix(String var1) throws DOMException;

   String getLocalName();

   boolean hasAttributes();

   String getBaseURI();

   short compareDocumentPosition(Node var1) throws DOMException;

   String getTextContent() throws DOMException;

   void setTextContent(String var1) throws DOMException;

   boolean isSameNode(Node var1);

   String lookupPrefix(String var1);

   boolean isDefaultNamespace(String var1);

   String lookupNamespaceURI(String var1);

   boolean isEqualNode(Node var1);

   Object getFeature(String var1, String var2);

   Object setUserData(String var1, Object var2, UserDataHandler var3);

   Object getUserData(String var1);
}
