package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAnimatedEnumeration {
   short getBaseVal();

   void setBaseVal(short var1) throws DOMException;

   short getAnimVal();
}
