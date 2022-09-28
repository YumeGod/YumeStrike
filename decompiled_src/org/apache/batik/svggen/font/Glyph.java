package org.apache.batik.svggen.font;

import org.apache.batik.svggen.font.table.GlyphDescription;

public class Glyph {
   protected short leftSideBearing;
   protected int advanceWidth;
   private Point[] points;

   public Glyph(GlyphDescription var1, short var2, int var3) {
      this.leftSideBearing = var2;
      this.advanceWidth = var3;
      this.describe(var1);
   }

   public int getAdvanceWidth() {
      return this.advanceWidth;
   }

   public short getLeftSideBearing() {
      return this.leftSideBearing;
   }

   public Point getPoint(int var1) {
      return this.points[var1];
   }

   public int getPointCount() {
      return this.points.length;
   }

   public void reset() {
   }

   public void scale(int var1) {
      for(int var2 = 0; var2 < this.points.length; ++var2) {
         this.points[var2].x = (this.points[var2].x << 10) * var1 >> 26;
         this.points[var2].y = (this.points[var2].y << 10) * var1 >> 26;
      }

      this.leftSideBearing = (short)(this.leftSideBearing * var1 >> 6);
      this.advanceWidth = this.advanceWidth * var1 >> 6;
   }

   private void describe(GlyphDescription var1) {
      int var2 = 0;
      this.points = new Point[var1.getPointCount() + 2];

      for(int var3 = 0; var3 < var1.getPointCount(); ++var3) {
         boolean var4 = var1.getEndPtOfContours(var2) == var3;
         if (var4) {
            ++var2;
         }

         this.points[var3] = new Point(var1.getXCoordinate(var3), var1.getYCoordinate(var3), (var1.getFlags(var3) & 1) != 0, var4);
      }

      this.points[var1.getPointCount()] = new Point(0, 0, true, true);
      this.points[var1.getPointCount() + 1] = new Point(this.advanceWidth, 0, true, true);
   }
}
