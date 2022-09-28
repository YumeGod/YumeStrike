package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAnimatedNumber {
   float getBaseVal();

   void setBaseVal(float var1) throws DOMException;

   float getAnimVal();
}
