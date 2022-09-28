package org.apache.batik.ext.awt.color;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

public class ICCColorSpaceExt extends ICC_ColorSpace {
   public static final int PERCEPTUAL = 0;
   public static final int RELATIVE_COLORIMETRIC = 1;
   public static final int ABSOLUTE_COLORIMETRIC = 2;
   public static final int SATURATION = 3;
   public static final int AUTO = 4;
   static final ColorSpace sRGB = ColorSpace.getInstance(1000);
   int intent;

   public ICCColorSpaceExt(ICC_Profile var1, int var2) {
      super(var1);
      this.intent = var2;
      switch (var2) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
            if (var2 != 4) {
               byte[] var3 = var1.getData(1751474532);
               var3[64] = (byte)var2;
            }

            return;
         default:
            throw new IllegalArgumentException();
      }
   }

   public float[] intendedToRGB(float[] var1) {
      switch (this.intent) {
         case 0:
         case 4:
            return this.perceptualToRGB(var1);
         case 1:
            return this.relativeColorimetricToRGB(var1);
         case 2:
            return this.absoluteColorimetricToRGB(var1);
         case 3:
            return this.saturationToRGB(var1);
         default:
            throw new Error("invalid intent:" + this.intent);
      }
   }

   public float[] perceptualToRGB(float[] var1) {
      return this.toRGB(var1);
   }

   public float[] relativeColorimetricToRGB(float[] var1) {
      float[] var2 = this.toCIEXYZ(var1);
      return sRGB.fromCIEXYZ(var2);
   }

   public float[] absoluteColorimetricToRGB(float[] var1) {
      return this.perceptualToRGB(var1);
   }

   public float[] saturationToRGB(float[] var1) {
      return this.perceptualToRGB(var1);
   }
}
