package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGICCColor {
   String getColorProfile();

   void setColorProfile(String var1) throws DOMException;

   SVGNumberList getColors();
}
