package org.w3c.dom;

public interface NamedNodeMap {
   Node getNamedItem(String var1);

   Node setNamedItem(Node var1) throws DOMException;

   Node removeNamedItem(String var1) throws DOMException;

   Node item(int var1);

   int getLength();

   Node getNamedItemNS(String var1, String var2) throws DOMException;

   Node setNamedItemNS(Node var1) throws DOMException;

   Node removeNamedItemNS(String var1, String var2) throws DOMException;
}
