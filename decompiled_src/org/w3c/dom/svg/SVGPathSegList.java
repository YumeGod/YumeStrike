package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGPathSegList {
   int getNumberOfItems();

   void clear() throws DOMException;

   SVGPathSeg initialize(SVGPathSeg var1) throws DOMException, SVGException;

   SVGPathSeg getItem(int var1) throws DOMException;

   SVGPathSeg insertItemBefore(SVGPathSeg var1, int var2) throws DOMException, SVGException;

   SVGPathSeg replaceItem(SVGPathSeg var1, int var2) throws DOMException, SVGException;

   SVGPathSeg removeItem(int var1) throws DOMException;

   SVGPathSeg appendItem(SVGPathSeg var1) throws DOMException, SVGException;
}
