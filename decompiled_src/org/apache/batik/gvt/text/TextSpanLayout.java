package org.apache.batik.gvt.text;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.apache.batik.gvt.font.GVTGlyphMetrics;
import org.apache.batik.gvt.font.GVTGlyphVector;
import org.apache.batik.gvt.font.GVTLineMetrics;

public interface TextSpanLayout {
   int DECORATION_UNDERLINE = 1;
   int DECORATION_STRIKETHROUGH = 2;
   int DECORATION_OVERLINE = 4;
   int DECORATION_ALL = 7;

   void draw(Graphics2D var1);

   Shape getDecorationOutline(int var1);

   Rectangle2D getBounds2D();

   Rectangle2D getGeometricBounds();

   Shape getOutline();

   Point2D getAdvance2D();

   float[] getGlyphAdvances();

   GVTGlyphMetrics getGlyphMetrics(int var1);

   GVTLineMetrics getLineMetrics();

   Point2D getTextPathAdvance();

   Point2D getOffset();

   void setScale(float var1, float var2, boolean var3);

   void setOffset(Point2D var1);

   Shape getHighlightShape(int var1, int var2);

   TextHit hitTestChar(float var1, float var2);

   boolean isVertical();

   boolean isOnATextPath();

   int getGlyphCount();

   int getCharacterCount(int var1, int var2);

   int getGlyphIndex(int var1);

   boolean isLeftToRight();

   boolean hasCharacterIndex(int var1);

   GVTGlyphVector getGlyphVector();

   double getComputedOrientationAngle(int var1);

   boolean isAltGlyph();
}
