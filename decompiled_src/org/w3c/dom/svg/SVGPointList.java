package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGPointList {
   int getNumberOfItems();

   void clear() throws DOMException;

   SVGPoint initialize(SVGPoint var1) throws DOMException, SVGException;

   SVGPoint getItem(int var1) throws DOMException;

   SVGPoint insertItemBefore(SVGPoint var1, int var2) throws DOMException, SVGException;

   SVGPoint replaceItem(SVGPoint var1, int var2) throws DOMException, SVGException;

   SVGPoint removeItem(int var1) throws DOMException;

   SVGPoint appendItem(SVGPoint var1) throws DOMException, SVGException;
}
