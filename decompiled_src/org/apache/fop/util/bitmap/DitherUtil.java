package org.apache.fop.util.bitmap;

import java.awt.Color;

public class DitherUtil {
   public static final int DITHER_MATRIX_2X2 = 2;
   public static final int DITHER_MATRIX_4X4 = 4;
   public static final int DITHER_MATRIX_8X8 = 8;
   private static final int[] BAYER_D2 = new int[]{0, 2, 3, 1};
   private static final int[] BAYER_D4;
   private static final int[] BAYER_D8;

   private static int[] deriveBayerMatrix(int[] d) {
      int[] dn = new int[d.length * 4];
      int half = (int)Math.sqrt((double)d.length);

      for(int part = 0; part < 4; ++part) {
         int i = 0;

         for(int c = d.length; i < c; ++i) {
            setValueInMatrix(dn, half, part, i, d[i] * 4 + BAYER_D2[part]);
         }
      }

      return dn;
   }

   private static void setValueInMatrix(int[] dn, int half, int part, int idx, int value) {
      int xoff = (part & 1) * half;
      int yoff = (part & 2) * half * half;
      int matrixIndex = yoff + idx / half * half * 2 + idx % half + xoff;
      dn[matrixIndex] = value;
   }

   public static int[] getBayerBasePattern(int matrix) {
      int[] result = new int[matrix * matrix];
      switch (matrix) {
         case 2:
            System.arraycopy(BAYER_D2, 0, result, 0, BAYER_D2.length);
            break;
         case 4:
            System.arraycopy(BAYER_D4, 0, result, 0, BAYER_D4.length);
            break;
         case 8:
            System.arraycopy(BAYER_D8, 0, result, 0, BAYER_D8.length);
            break;
         default:
            throw new IllegalArgumentException("Unsupported dither matrix: " + matrix);
      }

      return result;
   }

   public static byte[] getBayerDither(int matrix, int gray255, boolean doubleMatrix) {
      int ditherIndex;
      int[] var5;
      switch (matrix) {
         case 4:
            ditherIndex = gray255 * 17 / 255;
            var5 = BAYER_D4;
            break;
         case 8:
            ditherIndex = gray255 * 65 / 255;
            var5 = BAYER_D8;
            break;
         default:
            throw new IllegalArgumentException("Unsupported dither matrix: " + matrix);
      }

      byte[] dither;
      int i;
      int c;
      boolean dot;
      int byteIdx;
      if (doubleMatrix) {
         if (doubleMatrix && matrix != 4) {
            throw new IllegalArgumentException("doubleMatrix=true is only allowed for 4x4");
         }

         dither = new byte[var5.length / 8 * 4];
         i = 0;

         for(c = var5.length; i < c; ++i) {
            dot = var5[i] >= ditherIndex - 1;
            if (dot) {
               byteIdx = i / 4;
               dither[byteIdx] = (byte)(dither[byteIdx] | 1 << i % 4);
               dither[byteIdx] = (byte)(dither[byteIdx] | 1 << i % 4 + 4);
               dither[byteIdx + 4] = (byte)(dither[byteIdx + 4] | 1 << i % 4);
               dither[byteIdx + 4] = (byte)(dither[byteIdx + 4] | 1 << i % 4 + 4);
            }
         }
      } else {
         dither = new byte[var5.length / 8];
         i = 0;

         for(c = var5.length; i < c; ++i) {
            dot = var5[i] >= ditherIndex - 1;
            if (dot) {
               byteIdx = i / 8;
               dither[byteIdx] = (byte)(dither[byteIdx] | 1 << i % 8);
            }
         }
      }

      return dither;
   }

   public static byte[] getBayerDither(int matrix, Color col, boolean doubleMatrix) {
      float black = (float)BitmapImageUtil.convertToGray(col.getRGB()) / 256.0F;
      return getBayerDither(matrix, Math.round(black * 256.0F), doubleMatrix);
   }

   static {
      BAYER_D4 = deriveBayerMatrix(BAYER_D2);
      BAYER_D8 = deriveBayerMatrix(BAYER_D4);
   }
}
