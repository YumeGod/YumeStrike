package org.apache.batik.gvt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.gvt.text.AttributedCharacterSpanIterator;

public class MultiGlyphVector implements GVTGlyphVector {
   GVTGlyphVector[] gvs;
   int[] nGlyphs;
   int[] off;
   int nGlyph;

   public MultiGlyphVector(List var1) {
      int var2 = var1.size();
      this.gvs = new GVTGlyphVector[var2];
      this.nGlyphs = new int[var2];
      this.off = new int[var2];
      Iterator var3 = var1.iterator();

      int var4;
      for(var4 = 0; var3.hasNext(); ++var4) {
         this.off[var4] = this.nGlyph;
         GVTGlyphVector var5 = (GVTGlyphVector)var3.next();
         this.gvs[var4] = var5;
         this.nGlyphs[var4] = var5.getNumGlyphs();
         this.nGlyph += this.nGlyphs[var4];
      }

      int var10002 = this.nGlyphs[var4 - 1]++;
   }

   public int getNumGlyphs() {
      return this.nGlyph;
   }

   int getGVIdx(int var1) {
      if (var1 > this.nGlyph) {
         return -1;
      } else if (var1 == this.nGlyph) {
         return this.gvs.length - 1;
      } else {
         for(int var2 = 0; var2 < this.nGlyphs.length; ++var2) {
            if (var1 - this.off[var2] < this.nGlyphs[var2]) {
               return var2;
            }
         }

         return -1;
      }
   }

   public GVTFont getFont() {
      throw new IllegalArgumentException("Can't be correctly Implemented");
   }

   public FontRenderContext getFontRenderContext() {
      return this.gvs[0].getFontRenderContext();
   }

   public int getGlyphCode(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphCode(var1 - this.off[var2]);
   }

   public GlyphJustificationInfo getGlyphJustificationInfo(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphJustificationInfo(var1 - this.off[var2]);
   }

   public Shape getGlyphLogicalBounds(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphLogicalBounds(var1 - this.off[var2]);
   }

   public GVTGlyphMetrics getGlyphMetrics(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphMetrics(var1 - this.off[var2]);
   }

   public Shape getGlyphOutline(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphOutline(var1 - this.off[var2]);
   }

   public Rectangle2D getGlyphCellBounds(int var1) {
      return this.getGlyphLogicalBounds(var1).getBounds2D();
   }

   public Point2D getGlyphPosition(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphPosition(var1 - this.off[var2]);
   }

   public AffineTransform getGlyphTransform(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphTransform(var1 - this.off[var2]);
   }

   public Shape getGlyphVisualBounds(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].getGlyphVisualBounds(var1 - this.off[var2]);
   }

   public void setGlyphPosition(int var1, Point2D var2) {
      int var3 = this.getGVIdx(var1);
      this.gvs[var3].setGlyphPosition(var1 - this.off[var3], var2);
   }

   public void setGlyphTransform(int var1, AffineTransform var2) {
      int var3 = this.getGVIdx(var1);
      this.gvs[var3].setGlyphTransform(var1 - this.off[var3], var2);
   }

   public void setGlyphVisible(int var1, boolean var2) {
      int var3 = this.getGVIdx(var1);
      this.gvs[var3].setGlyphVisible(var1 - this.off[var3], var2);
   }

   public boolean isGlyphVisible(int var1) {
      int var2 = this.getGVIdx(var1);
      return this.gvs[var2].isGlyphVisible(var1 - this.off[var2]);
   }

   public int[] getGlyphCodes(int var1, int var2, int[] var3) {
      int[] var4 = var3;
      if (var3 == null) {
         var4 = new int[var2];
      }

      int[] var5 = null;
      int var6 = this.getGVIdx(var1);
      int var7 = var1 - this.off[var6];

      int var10;
      for(int var8 = 0; var2 != 0; var8 += var10) {
         var10 = var2;
         if (var7 + var2 > this.nGlyphs[var6]) {
            var10 = this.nGlyphs[var6] - var7;
         }

         GVTGlyphVector var9 = this.gvs[var6];
         if (var8 == 0) {
            var9.getGlyphCodes(var7, var10, var4);
         } else {
            if (var5 == null || var5.length < var10) {
               var5 = new int[var10];
            }

            var9.getGlyphCodes(var7, var10, var5);
            System.arraycopy(var5, 0, var4, var8, var10);
         }

         var7 = 0;
         ++var6;
         var2 -= var10;
      }

      return var4;
   }

   public float[] getGlyphPositions(int var1, int var2, float[] var3) {
      float[] var4 = var3;
      if (var3 == null) {
         var4 = new float[var2 * 2];
      }

      float[] var5 = null;
      int var6 = this.getGVIdx(var1);
      int var7 = var1 - this.off[var6];

      int var10;
      for(int var8 = 0; var2 != 0; var8 += var10 * 2) {
         var10 = var2;
         if (var7 + var2 > this.nGlyphs[var6]) {
            var10 = this.nGlyphs[var6] - var7;
         }

         GVTGlyphVector var9 = this.gvs[var6];
         if (var8 == 0) {
            var9.getGlyphPositions(var7, var10, var4);
         } else {
            if (var5 == null || var5.length < var10 * 2) {
               var5 = new float[var10 * 2];
            }

            var9.getGlyphPositions(var7, var10, var5);
            System.arraycopy(var5, 0, var4, var8, var10 * 2);
         }

         var7 = 0;
         ++var6;
         var2 -= var10;
      }

      return var4;
   }

   public Rectangle2D getLogicalBounds() {
      Rectangle2D var1 = null;

      for(int var2 = 0; var2 < this.gvs.length; ++var2) {
         Rectangle2D var3 = this.gvs[var2].getLogicalBounds();
         if (var1 == null) {
            var1 = var3;
         } else {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Shape getOutline() {
      GeneralPath var1 = null;

      for(int var2 = 0; var2 < this.gvs.length; ++var2) {
         Shape var3 = this.gvs[var2].getOutline();
         if (var1 == null) {
            var1 = new GeneralPath(var3);
         } else {
            var1.append(var3, false);
         }
      }

      return var1;
   }

   public Shape getOutline(float var1, float var2) {
      Shape var3 = this.getOutline();
      AffineTransform var4 = AffineTransform.getTranslateInstance((double)var1, (double)var2);
      var3 = var4.createTransformedShape(var3);
      return var3;
   }

   public Rectangle2D getBounds2D(AttributedCharacterIterator var1) {
      Rectangle2D var2 = null;
      int var3 = var1.getBeginIndex();

      for(int var4 = 0; var4 < this.gvs.length; ++var4) {
         GVTGlyphVector var5 = this.gvs[var4];
         int var6 = var5.getCharacterCount(0, var5.getNumGlyphs()) + 1;
         Rectangle2D var7 = this.gvs[var4].getBounds2D(new AttributedCharacterSpanIterator(var1, var3, var6));
         if (var2 == null) {
            var2 = var7;
         } else {
            var2.add(var7);
         }

         var3 = var6;
      }

      return var2;
   }

   public Rectangle2D getGeometricBounds() {
      Rectangle2D var1 = null;

      for(int var2 = 0; var2 < this.gvs.length; ++var2) {
         Rectangle2D var3 = this.gvs[var2].getGeometricBounds();
         if (var1 == null) {
            var1 = var3;
         } else {
            var1.add(var3);
         }
      }

      return var1;
   }

   public void performDefaultLayout() {
      for(int var1 = 0; var1 < this.gvs.length; ++var1) {
         this.gvs[var1].performDefaultLayout();
      }

   }

   public int getCharacterCount(int var1, int var2) {
      int var3 = this.getGVIdx(var1);
      int var4 = this.getGVIdx(var2);
      int var5 = 0;

      for(int var6 = var3; var6 <= var4; ++var6) {
         int var7 = var1 - this.off[var6];
         int var8 = var2 - this.off[var6];
         if (var8 >= this.nGlyphs[var6]) {
            var8 = this.nGlyphs[var6] - 1;
         }

         var5 += this.gvs[var6].getCharacterCount(var7, var8);
         var1 += var8 - var7 + 1;
      }

      return var5;
   }

   public void draw(Graphics2D var1, AttributedCharacterIterator var2) {
      int var3 = var2.getBeginIndex();

      for(int var4 = 0; var4 < this.gvs.length; ++var4) {
         GVTGlyphVector var5 = this.gvs[var4];
         int var6 = var5.getCharacterCount(0, var5.getNumGlyphs()) + 1;
         var5.draw(var1, new AttributedCharacterSpanIterator(var2, var3, var6));
         var3 = var6;
      }

   }
}
