package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGTransformList {
   int getNumberOfItems();

   void clear() throws DOMException;

   SVGTransform initialize(SVGTransform var1) throws DOMException, SVGException;

   SVGTransform getItem(int var1) throws DOMException;

   SVGTransform insertItemBefore(SVGTransform var1, int var2) throws DOMException, SVGException;

   SVGTransform replaceItem(SVGTransform var1, int var2) throws DOMException, SVGException;

   SVGTransform removeItem(int var1) throws DOMException;

   SVGTransform appendItem(SVGTransform var1) throws DOMException, SVGException;

   SVGTransform createSVGTransformFromMatrix(SVGMatrix var1);

   SVGTransform consolidate();
}
