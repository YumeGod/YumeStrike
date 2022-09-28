package org.w3c.dom;

public interface Element extends Node {
   String getTagName();

   String getAttribute(String var1);

   void setAttribute(String var1, String var2) throws DOMException;

   void removeAttribute(String var1) throws DOMException;

   Attr getAttributeNode(String var1);

   Attr setAttributeNode(Attr var1) throws DOMException;

   Attr removeAttributeNode(Attr var1) throws DOMException;

   NodeList getElementsByTagName(String var1);

   String getAttributeNS(String var1, String var2) throws DOMException;

   void setAttributeNS(String var1, String var2, String var3) throws DOMException;

   void removeAttributeNS(String var1, String var2) throws DOMException;

   Attr getAttributeNodeNS(String var1, String var2) throws DOMException;

   Attr setAttributeNodeNS(Attr var1) throws DOMException;

   NodeList getElementsByTagNameNS(String var1, String var2) throws DOMException;

   boolean hasAttribute(String var1);

   boolean hasAttributeNS(String var1, String var2) throws DOMException;

   TypeInfo getSchemaTypeInfo();

   void setIdAttribute(String var1, boolean var2) throws DOMException;

   void setIdAttributeNS(String var1, String var2, boolean var3) throws DOMException;

   void setIdAttributeNode(Attr var1, boolean var2) throws DOMException;
}
