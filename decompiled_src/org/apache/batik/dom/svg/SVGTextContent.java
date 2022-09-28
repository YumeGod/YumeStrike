package org.apache.batik.dom.svg;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface SVGTextContent {
   int getNumberOfChars();

   Rectangle2D getExtentOfChar(int var1);

   Point2D getStartPositionOfChar(int var1);

   Point2D getEndPositionOfChar(int var1);

   float getRotationOfChar(int var1);

   void selectSubString(int var1, int var2);

   float getComputedTextLength();

   float getSubStringLength(int var1, int var2);

   int getCharNumAtPosition(float var1, float var2);
}
