package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public interface CSSPrimitiveValue extends CSSValue {
   short CSS_UNKNOWN = 0;
   short CSS_NUMBER = 1;
   short CSS_PERCENTAGE = 2;
   short CSS_EMS = 3;
   short CSS_EXS = 4;
   short CSS_PX = 5;
   short CSS_CM = 6;
   short CSS_MM = 7;
   short CSS_IN = 8;
   short CSS_PT = 9;
   short CSS_PC = 10;
   short CSS_DEG = 11;
   short CSS_RAD = 12;
   short CSS_GRAD = 13;
   short CSS_MS = 14;
   short CSS_S = 15;
   short CSS_HZ = 16;
   short CSS_KHZ = 17;
   short CSS_DIMENSION = 18;
   short CSS_STRING = 19;
   short CSS_URI = 20;
   short CSS_IDENT = 21;
   short CSS_ATTR = 22;
   short CSS_COUNTER = 23;
   short CSS_RECT = 24;
   short CSS_RGBCOLOR = 25;

   short getPrimitiveType();

   void setFloatValue(short var1, float var2) throws DOMException;

   float getFloatValue(short var1) throws DOMException;

   void setStringValue(short var1, String var2) throws DOMException;

   String getStringValue() throws DOMException;

   Counter getCounterValue() throws DOMException;

   Rect getRectValue() throws DOMException;

   RGBColor getRGBColorValue() throws DOMException;
}
