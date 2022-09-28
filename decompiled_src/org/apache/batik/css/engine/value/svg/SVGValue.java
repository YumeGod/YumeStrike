package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.value.Value;
import org.w3c.dom.DOMException;

public interface SVGValue extends Value {
   short getPaintType() throws DOMException;

   String getUri() throws DOMException;

   short getColorType() throws DOMException;

   String getColorProfile() throws DOMException;

   int getNumberOfColors() throws DOMException;

   float getColor(int var1) throws DOMException;
}
