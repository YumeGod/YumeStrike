package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAnimatedBoolean {
   boolean getBaseVal();

   void setBaseVal(boolean var1) throws DOMException;

   boolean getAnimVal();
}
