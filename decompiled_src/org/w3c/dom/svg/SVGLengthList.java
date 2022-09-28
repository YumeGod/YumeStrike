package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGLengthList {
   int getNumberOfItems();

   void clear() throws DOMException;

   SVGLength initialize(SVGLength var1) throws DOMException, SVGException;

   SVGLength getItem(int var1) throws DOMException;

   SVGLength insertItemBefore(SVGLength var1, int var2) throws DOMException, SVGException;

   SVGLength replaceItem(SVGLength var1, int var2) throws DOMException, SVGException;

   SVGLength removeItem(int var1) throws DOMException;

   SVGLength appendItem(SVGLength var1) throws DOMException, SVGException;
}
