package org.w3c.dom.svg;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.RGBColor;

public interface SVGColor extends CSSValue {
   short SVG_COLORTYPE_UNKNOWN = 0;
   short SVG_COLORTYPE_RGBCOLOR = 1;
   short SVG_COLORTYPE_RGBCOLOR_ICCCOLOR = 2;
   short SVG_COLORTYPE_CURRENTCOLOR = 3;

   short getColorType();

   RGBColor getRGBColor();

   SVGICCColor getICCColor();

   void setRGBColor(String var1) throws SVGException;

   void setRGBColorICCColor(String var1, String var2) throws SVGException;

   void setColor(short var1, String var2, String var3) throws SVGException;
}
