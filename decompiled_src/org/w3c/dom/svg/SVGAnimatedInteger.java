package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAnimatedInteger {
   int getBaseVal();

   void setBaseVal(int var1) throws DOMException;

   int getAnimVal();
}
