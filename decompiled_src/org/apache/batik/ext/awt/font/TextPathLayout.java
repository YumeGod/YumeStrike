package org.apache.batik.ext.awt.font;

import java.awt.Shape;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import org.apache.batik.ext.awt.geom.PathLength;

public class TextPathLayout {
   public static final int ALIGN_START = 0;
   public static final int ALIGN_MIDDLE = 1;
   public static final int ALIGN_END = 2;
   public static final int ADJUST_SPACING = 0;
   public static final int ADJUST_GLYPHS = 1;

   public static Shape layoutGlyphVector(GlyphVector var0, Shape var1, int var2, float var3, float var4, int var5) {
      GeneralPath var6 = new GeneralPath();
      PathLength var7 = new PathLength(var1);
      float var8 = var7.lengthOfPath();
      if (var0 == null) {
         return var6;
      } else {
         float var9 = (float)var0.getVisualBounds().getWidth();
         if (var1 != null && var0.getNumGlyphs() != 0 && var7.lengthOfPath() != 0.0F && var9 != 0.0F) {
            float var10 = var4 / var9;
            float var11 = var3;
            if (var2 == 2) {
               var11 = var3 + (var8 - var4);
            } else if (var2 == 1) {
               var11 = var3 + (var8 - var4) / 2.0F;
            }

            for(int var12 = 0; var12 < var0.getNumGlyphs(); ++var12) {
               GlyphMetrics var13 = var0.getGlyphMetrics(var12);
               float var14 = var13.getAdvance();
               Shape var15 = var0.getGlyphOutline(var12);
               if (var5 == 1) {
                  AffineTransform var16 = AffineTransform.getScaleInstance((double)var10, 1.0);
                  var15 = var16.createTransformedShape(var15);
                  var14 *= var10;
               }

               float var21 = (float)var15.getBounds2D().getWidth();
               float var17 = var11 + var21 / 2.0F;
               Point2D var18 = var7.pointAtLength(var17);
               if (var18 != null) {
                  float var19 = var7.angleAtLength(var17);
                  AffineTransform var20 = new AffineTransform();
                  var20.translate(var18.getX(), var18.getY());
                  var20.rotate((double)var19);
                  var20.translate((double)(var14 / -2.0F), 0.0);
                  var15 = var20.createTransformedShape(var15);
                  var6.append(var15, false);
               }

               if (var5 == 0) {
                  var11 += var14 * var10;
               } else {
                  var11 += var14;
               }
            }

            return var6;
         } else {
            return var6;
         }
      }
   }

   public static Shape layoutGlyphVector(GlyphVector var0, Shape var1, int var2) {
      return layoutGlyphVector(var0, var1, var2, 0.0F, (float)var0.getVisualBounds().getWidth(), 0);
   }

   public static Shape layoutGlyphVector(GlyphVector var0, Shape var1) {
      return layoutGlyphVector(var0, var1, 0);
   }
}
