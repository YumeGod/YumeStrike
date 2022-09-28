package org.apache.batik.extension.svg;

import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.font.GVTGlyphVector;

public class LineInfo {
   Point2D.Float loc;
   AttributedCharacterIterator aci;
   GVTGlyphVector gv;
   int startIdx;
   int endIdx;
   float advance;
   float visualAdvance;
   float lastCharWidth;
   float lineWidth;
   boolean partial;
   Point2D.Float verticalAlignOffset;

   public LineInfo(Point2D.Float var1, AttributedCharacterIterator var2, GVTGlyphVector var3, int var4, int var5, float var6, float var7, float var8, float var9, boolean var10, Point2D.Float var11) {
      this.loc = var1;
      this.aci = var2;
      this.gv = var3;
      this.startIdx = var4;
      this.endIdx = var5;
      this.advance = var6;
      this.visualAdvance = var7;
      this.lastCharWidth = var8;
      this.lineWidth = var9;
      this.partial = var10;
      this.verticalAlignOffset = var11;
   }

   public Point2D.Float getLocation() {
      return this.loc;
   }

   public AttributedCharacterIterator getACI() {
      return this.aci;
   }

   public GVTGlyphVector getGlyphVector() {
      return this.gv;
   }

   public int getStartIdx() {
      return this.startIdx;
   }

   public int getEndIdx() {
      return this.endIdx;
   }

   public float getAdvance() {
      return this.advance;
   }

   public float getVisualAdvance() {
      return this.visualAdvance;
   }

   public float getLastCharWidth() {
      return this.lastCharWidth;
   }

   public float getLineWidth() {
      return this.lineWidth;
   }

   public boolean isPartialLine() {
      return this.partial;
   }

   public Point2D.Float getVerticalAlignOffset() {
      return this.verticalAlignOffset;
   }

   public String toString() {
      return "[LineInfo loc: " + this.loc + " [" + this.startIdx + ',' + this.endIdx + "] " + " LWidth: " + this.lineWidth + " Adv: " + this.advance + " VAdv: " + this.visualAdvance + " LCW: " + this.lastCharWidth + " Partial: " + this.partial + " verticalAlignOffset: " + this.verticalAlignOffset;
   }
}
