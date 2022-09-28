package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAnimatedString {
   String getBaseVal();

   void setBaseVal(String var1) throws DOMException;

   String getAnimVal();
}
