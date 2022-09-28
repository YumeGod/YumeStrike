package org.apache.batik.ext.awt.image.renderable;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.util.Map;
import org.apache.batik.ext.awt.image.rendered.ColorMatrixRed;

public class ColorMatrixRable8Bit extends AbstractColorInterpolationRable implements ColorMatrixRable {
   private static float[][] MATRIX_LUMINANCE_TO_ALPHA = new float[][]{{0.0F, 0.0F, 0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.0F}, {0.2125F, 0.7154F, 0.0721F, 0.0F, 0.0F}};
   private int type;
   private float[][] matrix;

   public void setSource(Filter var1) {
      this.init(var1, (Map)null);
   }

   public Filter getSource() {
      return (Filter)this.getSources().get(0);
   }

   public int getType() {
      return this.type;
   }

   public float[][] getMatrix() {
      return this.matrix;
   }

   private ColorMatrixRable8Bit() {
   }

   public static ColorMatrixRable buildMatrix(float[][] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else if (var0.length != 4) {
         throw new IllegalArgumentException();
      } else {
         float[][] var1 = new float[4][];

         for(int var2 = 0; var2 < 4; ++var2) {
            float[] var3 = var0[var2];
            if (var3 == null) {
               throw new IllegalArgumentException();
            }

            if (var3.length != 5) {
               throw new IllegalArgumentException();
            }

            var1[var2] = new float[5];

            for(int var4 = 0; var4 < 5; ++var4) {
               var1[var2][var4] = var3[var4];
            }
         }

         ColorMatrixRable8Bit var5 = new ColorMatrixRable8Bit();
         var5.type = 0;
         var5.matrix = var1;
         return var5;
      }
   }

   public static ColorMatrixRable buildSaturate(float var0) {
      ColorMatrixRable8Bit var1 = new ColorMatrixRable8Bit();
      var1.type = 1;
      var1.matrix = new float[][]{{0.213F + 0.787F * var0, 0.715F - 0.715F * var0, 0.072F - 0.072F * var0, 0.0F, 0.0F}, {0.213F - 0.213F * var0, 0.715F + 0.285F * var0, 0.072F - 0.072F * var0, 0.0F, 0.0F}, {0.213F - 0.213F * var0, 0.715F - 0.715F * var0, 0.072F + 0.928F * var0, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F, 1.0F, 0.0F}};
      return var1;
   }

   public static ColorMatrixRable buildHueRotate(float var0) {
      ColorMatrixRable8Bit var1 = new ColorMatrixRable8Bit();
      var1.type = 2;
      float var2 = (float)Math.cos((double)var0);
      float var3 = (float)Math.sin((double)var0);
      float var4 = 0.213F + var2 * 0.787F - var3 * 0.213F;
      float var5 = 0.213F - var2 * 0.212F + var3 * 0.143F;
      float var6 = 0.213F - var2 * 0.213F - var3 * 0.787F;
      float var7 = 0.715F - var2 * 0.715F - var3 * 0.715F;
      float var8 = 0.715F + var2 * 0.285F + var3 * 0.14F;
      float var9 = 0.715F - var2 * 0.715F + var3 * 0.715F;
      float var10 = 0.072F - var2 * 0.072F + var3 * 0.928F;
      float var11 = 0.072F - var2 * 0.072F - var3 * 0.283F;
      float var12 = 0.072F + var2 * 0.928F + var3 * 0.072F;
      var1.matrix = new float[][]{{var4, var7, var10, 0.0F, 0.0F}, {var5, var8, var11, 0.0F, 0.0F}, {var6, var9, var12, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F, 1.0F, 0.0F}};
      return var1;
   }

   public static ColorMatrixRable buildLuminanceToAlpha() {
      ColorMatrixRable8Bit var0 = new ColorMatrixRable8Bit();
      var0.type = 3;
      var0.matrix = MATRIX_LUMINANCE_TO_ALPHA;
      return var0;
   }

   public RenderedImage createRendering(RenderContext var1) {
      RenderedImage var2 = this.getSource().createRendering(var1);
      return var2 == null ? null : new ColorMatrixRed(this.convertSourceCS(var2), this.matrix);
   }
}
