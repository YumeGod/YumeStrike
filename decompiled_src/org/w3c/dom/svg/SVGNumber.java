package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGNumber {
   float getValue();

   void setValue(float var1) throws DOMException;
}
