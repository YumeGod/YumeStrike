package org.apache.batik.gvt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;

public interface GVTGlyphVector {
   GVTFont getFont();

   FontRenderContext getFontRenderContext();

   int getGlyphCode(int var1);

   int[] getGlyphCodes(int var1, int var2, int[] var3);

   GlyphJustificationInfo getGlyphJustificationInfo(int var1);

   Shape getGlyphLogicalBounds(int var1);

   GVTGlyphMetrics getGlyphMetrics(int var1);

   Shape getGlyphOutline(int var1);

   Rectangle2D getGlyphCellBounds(int var1);

   Point2D getGlyphPosition(int var1);

   float[] getGlyphPositions(int var1, int var2, float[] var3);

   AffineTransform getGlyphTransform(int var1);

   Shape getGlyphVisualBounds(int var1);

   Rectangle2D getLogicalBounds();

   int getNumGlyphs();

   Shape getOutline();

   Shape getOutline(float var1, float var2);

   Rectangle2D getGeometricBounds();

   Rectangle2D getBounds2D(AttributedCharacterIterator var1);

   void performDefaultLayout();

   void setGlyphPosition(int var1, Point2D var2);

   void setGlyphTransform(int var1, AffineTransform var2);

   void setGlyphVisible(int var1, boolean var2);

   boolean isGlyphVisible(int var1);

   int getCharacterCount(int var1, int var2);

   void draw(Graphics2D var1, AttributedCharacterIterator var2);
}
