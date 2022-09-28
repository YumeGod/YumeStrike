package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGGlyphRefElement extends SVGElement, SVGURIReference, SVGStylable {
   String getGlyphRef();

   void setGlyphRef(String var1) throws DOMException;

   String getFormat();

   void setFormat(String var1) throws DOMException;

   float getX();

   void setX(float var1) throws DOMException;

   float getY();

   void setY(float var1) throws DOMException;

   float getDx();

   void setDx(float var1) throws DOMException;

   float getDy();

   void setDy(float var1) throws DOMException;
}
