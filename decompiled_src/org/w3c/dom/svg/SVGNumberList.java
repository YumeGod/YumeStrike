package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGNumberList {
   int getNumberOfItems();

   void clear() throws DOMException;

   SVGNumber initialize(SVGNumber var1) throws DOMException, SVGException;

   SVGNumber getItem(int var1) throws DOMException;

   SVGNumber insertItemBefore(SVGNumber var1, int var2) throws DOMException, SVGException;

   SVGNumber replaceItem(SVGNumber var1, int var2) throws DOMException, SVGException;

   SVGNumber removeItem(int var1) throws DOMException;

   SVGNumber appendItem(SVGNumber var1) throws DOMException, SVGException;
}
