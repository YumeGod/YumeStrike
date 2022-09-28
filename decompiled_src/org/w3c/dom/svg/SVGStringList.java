package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGStringList {
   int getNumberOfItems();

   void clear() throws DOMException;

   String initialize(String var1) throws DOMException, SVGException;

   String getItem(int var1) throws DOMException;

   String insertItemBefore(String var1, int var2) throws DOMException, SVGException;

   String replaceItem(String var1, int var2) throws DOMException, SVGException;

   String removeItem(int var1) throws DOMException;

   String appendItem(String var1) throws DOMException, SVGException;
}
