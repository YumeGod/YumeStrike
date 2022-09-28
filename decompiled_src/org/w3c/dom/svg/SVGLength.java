package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGLength {
   short SVG_LENGTHTYPE_UNKNOWN = 0;
   short SVG_LENGTHTYPE_NUMBER = 1;
   short SVG_LENGTHTYPE_PERCENTAGE = 2;
   short SVG_LENGTHTYPE_EMS = 3;
   short SVG_LENGTHTYPE_EXS = 4;
   short SVG_LENGTHTYPE_PX = 5;
   short SVG_LENGTHTYPE_CM = 6;
   short SVG_LENGTHTYPE_MM = 7;
   short SVG_LENGTHTYPE_IN = 8;
   short SVG_LENGTHTYPE_PT = 9;
   short SVG_LENGTHTYPE_PC = 10;

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
