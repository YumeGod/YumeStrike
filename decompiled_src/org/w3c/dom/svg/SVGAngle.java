package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGAngle {
   short SVG_ANGLETYPE_UNKNOWN = 0;
   short SVG_ANGLETYPE_UNSPECIFIED = 1;
   short SVG_ANGLETYPE_DEG = 2;
   short SVG_ANGLETYPE_RAD = 3;
   short SVG_ANGLETYPE_GRAD = 4;

   short getUnitType();

   float getValue();

   void setValue(float var1) throws DOMException;

   float getValueInSpecifiedUnits();

   void setValueInSpecifiedUnits(float var1) throws DOMException;

   String getValueAsString();

   void setValueAsString(String var1) throws DOMException;

   void newValueSpecifiedUnits(short var1, float var2);

   void convertToSpecifiedUnits(short var1);
}
