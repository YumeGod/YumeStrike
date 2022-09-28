package org.apache.batik.ext.awt;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

public abstract class MultipleGradientPaint implements Paint {
   protected int transparency;
   protected float[] fractions;
   protected Color[] colors;
   protected AffineTransform gradientTransform;
   protected CycleMethodEnum cycleMethod;
   protected ColorSpaceEnum colorSpace;
   public static final CycleMethodEnum NO_CYCLE = new CycleMethodEnum();
   public static final CycleMethodEnum REFLECT = new CycleMethodEnum();
   public static final CycleMethodEnum REPEAT = new CycleMethodEnum();
   public static final ColorSpaceEnum SRGB = new ColorSpaceEnum();
   public static final ColorSpaceEnum LINEAR_RGB = new ColorSpaceEnum();

   public MultipleGradientPaint(float[] var1, Color[] var2, CycleMethodEnum var3, ColorSpaceEnum var4, AffineTransform var5) {
      if (var1 == null) {
         throw new IllegalArgumentException("Fractions array cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Colors array cannot be null");
      } else if (var1.length != var2.length) {
         throw new IllegalArgumentException("Colors and fractions must have equal size");
      } else if (var2.length < 2) {
         throw new IllegalArgumentException("User must specify at least 2 colors");
      } else if (var4 != LINEAR_RGB && var4 != SRGB) {
         throw new IllegalArgumentException("Invalid colorspace for interpolation.");
      } else if (var3 != NO_CYCLE && var3 != REFLECT && var3 != REPEAT) {
         throw new IllegalArgumentException("Invalid cycle method.");
      } else if (var5 == null) {
         throw new IllegalArgumentException("Gradient transform cannot be null.");
      } else {
         this.fractions = new float[var1.length];
         System.arraycopy(var1, 0, this.fractions, 0, var1.length);
         this.colors = new Color[var2.length];
         System.arraycopy(var2, 0, this.colors, 0, var2.length);
         this.colorSpace = var4;
         this.cycleMethod = var3;
         this.gradientTransform = (AffineTransform)var5.clone();
         boolean var6 = true;

         for(int var7 = 0; var7 < var2.length; ++var7) {
            var6 = var6 && var2[var7].getAlpha() == 255;
         }

         if (var6) {
            this.transparency = 1;
         } else {
            this.transparency = 3;
         }

      }
   }

   public Color[] getColors() {
      Color[] var1 = new Color[this.colors.length];
      System.arraycopy(this.colors, 0, var1, 0, this.colors.length);
      return var1;
   }

   public float[] getFractions() {
      float[] var1 = new float[this.fractions.length];
      System.arraycopy(this.fractions, 0, var1, 0, this.fractions.length);
      return var1;
   }

   public int getTransparency() {
      return this.transparency;
   }

   public CycleMethodEnum getCycleMethod() {
      return this.cycleMethod;
   }

   public ColorSpaceEnum getColorSpace() {
      return this.colorSpace;
   }

   public AffineTransform getTransform() {
      return (AffineTransform)this.gradientTransform.clone();
   }

   public static class CycleMethodEnum {
   }

   public static class ColorSpaceEnum {
   }
}
