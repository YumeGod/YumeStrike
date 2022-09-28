package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAltGlyphElement extends SVGTextPositioningElement, SVGURIReference {
   String getGlyphRef();

   void setGlyphRef(String var1) throws DOMException;

   String getFormat();

   void setFormat(String var1) throws DOMException;
}
