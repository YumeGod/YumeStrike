package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/** @deprecated */
public interface ASNamedObjectMap {
   int getLength();

   ASObject getNamedItem(String var1);

   ASObject getNamedItemNS(String var1, String var2);

   ASObject item(int var1);

   ASObject removeNamedItem(String var1) throws DOMException;

   ASObject removeNamedItemNS(String var1, String var2) throws DOMException;

   ASObject setNamedItem(ASObject var1) throws DOMException;

   ASObject setNamedItemNS(ASObject var1) throws DOMException;
}
