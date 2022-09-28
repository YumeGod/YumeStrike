package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class MorphologyOp implements BufferedImageOp, RasterOp {
   private int radiusX;
   private int radiusY;
   private boolean doDilation;
   private final int rangeX;
   private final int rangeY;
   private final ColorSpace sRGB = ColorSpace.getInstance(1000);
   private final ColorSpace lRGB = ColorSpace.getInstance(1004);

   public MorphologyOp(int var1, int var2, boolean var3) {
      if (var1 > 0 && var2 > 0) {
         this.radiusX = var1;
         this.radiusY = var2;
         this.doDilation = var3;
         this.rangeX = 2 * var1 + 1;
         this.rangeY = 2 * var2 + 1;
      } else {
         throw new IllegalArgumentException("The radius of X-axis or Y-axis should not be Zero or Negatives.");
      }
   }

   public Rectangle2D getBounds2D(Raster var1) {
      this.checkCompatible(var1.getSampleModel());
      return new Rectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight());
   }

   public Rectangle2D getBounds2D(BufferedImage var1) {
      return new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
   }

   public Point2D getPoint2D(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Point2D.Float();
      }

      ((Point2D)var2).setLocation(var1.getX(), var1.getY());
      return (Point2D)var2;
   }

   private void checkCompatible(ColorModel var1, SampleModel var2) {
      ColorSpace var3 = var1.getColorSpace();
      if (!var3.equals(this.sRGB) && !var3.equals(this.lRGB)) {
         throw new IllegalArgumentException("Expected CS_sRGB or CS_LINEAR_RGB color model");
      } else if (!(var1 instanceof DirectColorModel)) {
         throw new IllegalArgumentException("colorModel should be an instance of DirectColorModel");
      } else if (var2.getDataType() != 3) {
         throw new IllegalArgumentException("colorModel's transferType should be DataBuffer.TYPE_INT");
      } else {
         DirectColorModel var4 = (DirectColorModel)var1;
         if (var4.getRedMask() != 16711680) {
            throw new IllegalArgumentException("red mask in source should be 0x00ff0000");
         } else if (var4.getGreenMask() != 65280) {
            throw new IllegalArgumentException("green mask in source should be 0x0000ff00");
         } else if (var4.getBlueMask() != 255) {
            throw new IllegalArgumentException("blue mask in source should be 0x000000ff");
         } else if (var4.getAlphaMask() != -16777216) {
            throw new IllegalArgumentException("alpha mask in source should be 0xff000000");
         }
      }
   }

   private boolean isCompatible(ColorModel var1, SampleModel var2) {
      ColorSpace var3 = var1.getColorSpace();
      if (var3 != ColorSpace.getInstance(1000) && var3 != ColorSpace.getInstance(1004)) {
         return false;
      } else if (!(var1 instanceof DirectColorModel)) {
         return false;
      } else if (var2.getDataType() != 3) {
         return false;
      } else {
         DirectColorModel var4 = (DirectColorModel)var1;
         if (var4.getRedMask() != 16711680) {
            return false;
         } else if (var4.getGreenMask() != 65280) {
            return false;
         } else if (var4.getBlueMask() != 255) {
            return false;
         } else {
            return var4.getAlphaMask() == -16777216;
         }
      }
   }

   private void checkCompatible(SampleModel var1) {
      if (!(var1 instanceof SinglePixelPackedSampleModel)) {
         throw new IllegalArgumentException("MorphologyOp only works with Rasters using SinglePixelPackedSampleModels");
      } else {
         int var2 = var1.getNumBands();
         if (var2 != 4) {
            throw new IllegalArgumentException("MorphologyOp only words with Rasters having 4 bands");
         } else if (var1.getDataType() != 3) {
            throw new IllegalArgumentException("MorphologyOp only works with Rasters using DataBufferInt");
         } else {
            int[] var3 = ((SinglePixelPackedSampleModel)var1).getBitOffsets();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4] % 8 != 0) {
                  throw new IllegalArgumentException("MorphologyOp only works with Rasters using 8 bits per band : " + var4 + " : " + var3[var4]);
               }
            }

         }
      }
   }

   public RenderingHints getRenderingHints() {
      return null;
   }

   public WritableRaster createCompatibleDestRaster(Raster var1) {
      this.checkCompatible(var1.getSampleModel());
      return var1.createCompatibleWritableRaster();
   }

   public BufferedImage createCompatibleDestImage(BufferedImage var1, ColorModel var2) {
      BufferedImage var3 = null;
      if (var2 == null) {
         var2 = var1.getColorModel();
      }

      WritableRaster var4 = var2.createCompatibleWritableRaster(var1.getWidth(), var1.getHeight());
      this.checkCompatible(var2, var4.getSampleModel());
      var3 = new BufferedImage(var2, var4, var2.isAlphaPremultiplied(), (Hashtable)null);
      return var3;
   }

   static final boolean isBetter(int var0, int var1, boolean var2) {
      if (var0 > var1) {
         return var2;
      } else if (var0 < var1) {
         return !var2;
      } else {
         return true;
      }
   }

   private void specialProcessRow(Raster var1, WritableRaster var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      DataBufferInt var5 = (DataBufferInt)var1.getDataBuffer();
      DataBufferInt var6 = (DataBufferInt)var2.getDataBuffer();
      SinglePixelPackedSampleModel var7 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var8 = var5.getOffset() + var7.getOffset(var1.getMinX() - var1.getSampleModelTranslateX(), var1.getMinY() - var1.getSampleModelTranslateY());
      var7 = (SinglePixelPackedSampleModel)var2.getSampleModel();
      int var9 = var6.getOffset() + var7.getOffset(var2.getMinX() - var2.getSampleModelTranslateX(), var2.getMinY() - var2.getSampleModelTranslateY());
      int var10 = ((SinglePixelPackedSampleModel)var1.getSampleModel()).getScanlineStride();
      int var11 = ((SinglePixelPackedSampleModel)var2.getSampleModel()).getScanlineStride();
      int[] var12 = var5.getBankData()[0];
      int[] var13 = var6.getBankData()[0];
      int var14;
      int var15;
      int var21;
      int var22;
      int var24;
      int var25;
      int var26;
      int var27;
      int var28;
      int var29;
      int var30;
      int var31;
      if (var3 <= this.radiusX) {
         for(int var32 = 0; var32 < var4; ++var32) {
            var14 = var8 + var32 * var10;
            var15 = var9 + var32 * var11;
            var21 = var12[var14++];
            var24 = var21 >>> 24;
            var25 = var21 & 16711680;
            var26 = var21 & '\uff00';
            var27 = var21 & 255;

            int var33;
            for(var33 = 1; var33 < var3; ++var33) {
               var22 = var12[var14++];
               var28 = var22 >>> 24;
               var29 = var22 & 16711680;
               var30 = var22 & '\uff00';
               var31 = var22 & 255;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
               }

               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
               }

               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
               }

               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
               }
            }

            for(var33 = 0; var33 < var3; ++var33) {
               var13[var15++] = var24 << 24 | var25 | var26 | var27;
            }
         }
      } else {
         int[] var39 = new int[var3];
         int[] var40 = new int[var3];
         int[] var34 = new int[var3];
         int[] var35 = new int[var3];

         for(int var36 = 0; var36 < var4; ++var36) {
            var14 = var8 + var36 * var10;
            var15 = var9 + var36 * var11;
            int var16 = 0;
            int var17 = 0;
            int var18 = 0;
            int var19 = 0;
            int var20 = 0;
            var21 = var12[var14++];
            var24 = var21 >>> 24;
            var25 = var21 & 16711680;
            var26 = var21 & '\uff00';
            var27 = var21 & 255;
            var39[0] = var24;
            var40[0] = var25;
            var34[0] = var26;
            var35[0] = var27;

            int var37;
            for(var37 = 1; var37 <= this.radiusX; ++var37) {
               var22 = var12[var14++];
               var28 = var22 >>> 24;
               var29 = var22 & 16711680;
               var30 = var22 & '\uff00';
               var31 = var22 & 255;
               var39[var37] = var28;
               var40[var37] = var29;
               var34[var37] = var30;
               var35[var37] = var31;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var37;
               }

               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var37;
               }

               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var37;
               }

               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var37;
               }
            }

            var13[var15++] = var24 << 24 | var25 | var26 | var27;

            for(var37 = 1; var37 <= var3 - this.radiusX - 1; ++var37) {
               int var23 = var12[var14++];
               var24 = var39[var17];
               var28 = var23 >>> 24;
               var39[var37 + this.radiusX] = var28;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var37 + this.radiusX;
               }

               var25 = var40[var18];
               var29 = var23 & 16711680;
               var40[var37 + this.radiusX] = var29;
               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var37 + this.radiusX;
               }

               var26 = var34[var19];
               var30 = var23 & '\uff00';
               var34[var37 + this.radiusX] = var30;
               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var37 + this.radiusX;
               }

               var27 = var35[var20];
               var31 = var23 & 255;
               var35[var37 + this.radiusX] = var31;
               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var37 + this.radiusX;
               }

               var13[var15++] = var24 << 24 | var25 | var26 | var27;
            }

            for(var37 = var3 - this.radiusX; var37 <= this.radiusX; ++var37) {
               var13[var15] = var13[var15 - 1];
               ++var15;
            }

            for(var37 = this.radiusX + 1; var37 < var3; ++var37) {
               int var38;
               if (var17 == var16) {
                  var24 = var39[var16 + 1];
                  var17 = var16 + 1;

                  for(var38 = var16 + 2; var38 < var3; ++var38) {
                     var28 = var39[var38];
                     if (isBetter(var28, var24, this.doDilation)) {
                        var24 = var28;
                        var17 = var38;
                     }
                  }
               } else {
                  var24 = var39[var17];
               }

               if (var18 == var16) {
                  var25 = var40[var16 + 1];
                  var18 = var16 + 1;

                  for(var38 = var16 + 2; var38 < var3; ++var38) {
                     var29 = var40[var38];
                     if (isBetter(var29, var25, this.doDilation)) {
                        var25 = var29;
                        var18 = var38;
                     }
                  }
               } else {
                  var25 = var40[var18];
               }

               if (var19 == var16) {
                  var26 = var34[var16 + 1];
                  var19 = var16 + 1;

                  for(var38 = var16 + 2; var38 < var3; ++var38) {
                     var30 = var34[var38];
                     if (isBetter(var30, var26, this.doDilation)) {
                        var26 = var30;
                        var19 = var38;
                     }
                  }
               } else {
                  var26 = var34[var19];
               }

               if (var20 == var16) {
                  var27 = var35[var16 + 1];
                  var20 = var16 + 1;

                  for(var38 = var16 + 2; var38 < var3; ++var38) {
                     var31 = var35[var38];
                     if (isBetter(var31, var27, this.doDilation)) {
                        var27 = var31;
                        var20 = var38;
                     }
                  }
               } else {
                  var27 = var35[var20];
               }

               ++var16;
               var13[var15++] = var24 << 24 | var25 | var26 | var27;
            }
         }
      }

   }

   private void specialProcessColumn(Raster var1, WritableRaster var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      DataBufferInt var5 = (DataBufferInt)var2.getDataBuffer();
      int var6 = var5.getOffset();
      int var7 = ((SinglePixelPackedSampleModel)var2.getSampleModel()).getScanlineStride();
      int[] var8 = var5.getBankData()[0];
      int var9;
      int var10;
      int var16;
      int var17;
      int var19;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      if (var4 <= this.radiusY) {
         for(int var27 = 0; var27 < var3; ++var27) {
            var9 = var6 + var27;
            var10 = var6 + var27;
            var16 = var8[var10];
            var10 += var7;
            var19 = var16 >>> 24;
            var20 = var16 & 16711680;
            var21 = var16 & '\uff00';
            var22 = var16 & 255;

            int var28;
            for(var28 = 1; var28 < var4; ++var28) {
               var17 = var8[var10];
               var10 += var7;
               var23 = var17 >>> 24;
               var24 = var17 & 16711680;
               var25 = var17 & '\uff00';
               var26 = var17 & 255;
               if (isBetter(var23, var19, this.doDilation)) {
                  var19 = var23;
               }

               if (isBetter(var24, var20, this.doDilation)) {
                  var20 = var24;
               }

               if (isBetter(var25, var21, this.doDilation)) {
                  var21 = var25;
               }

               if (isBetter(var26, var22, this.doDilation)) {
                  var22 = var26;
               }
            }

            for(var28 = 0; var28 < var4; ++var28) {
               var8[var9] = var19 << 24 | var20 | var21 | var22;
               var9 += var7;
            }
         }
      } else {
         int[] var34 = new int[var4];
         int[] var35 = new int[var4];
         int[] var29 = new int[var4];
         int[] var30 = new int[var4];

         for(int var31 = 0; var31 < var3; ++var31) {
            var9 = var6 + var31;
            var10 = var6 + var31;
            int var11 = 0;
            int var12 = 0;
            int var13 = 0;
            int var14 = 0;
            int var15 = 0;
            var16 = var8[var10];
            var10 += var7;
            var19 = var16 >>> 24;
            var20 = var16 & 16711680;
            var21 = var16 & '\uff00';
            var22 = var16 & 255;
            var34[0] = var19;
            var35[0] = var20;
            var29[0] = var21;
            var30[0] = var22;

            int var32;
            for(var32 = 1; var32 <= this.radiusY; ++var32) {
               var17 = var8[var10];
               var10 += var7;
               var23 = var17 >>> 24;
               var24 = var17 & 16711680;
               var25 = var17 & '\uff00';
               var26 = var17 & 255;
               var34[var32] = var23;
               var35[var32] = var24;
               var29[var32] = var25;
               var30[var32] = var26;
               if (isBetter(var23, var19, this.doDilation)) {
                  var19 = var23;
                  var12 = var32;
               }

               if (isBetter(var24, var20, this.doDilation)) {
                  var20 = var24;
                  var13 = var32;
               }

               if (isBetter(var25, var21, this.doDilation)) {
                  var21 = var25;
                  var14 = var32;
               }

               if (isBetter(var26, var22, this.doDilation)) {
                  var22 = var26;
                  var15 = var32;
               }
            }

            var8[var9] = var19 << 24 | var20 | var21 | var22;
            var9 += var7;

            for(var32 = 1; var32 <= var4 - this.radiusY - 1; ++var32) {
               int var18 = var8[var10];
               var10 += var7;
               var19 = var34[var12];
               var23 = var18 >>> 24;
               var34[var32 + this.radiusY] = var23;
               if (isBetter(var23, var19, this.doDilation)) {
                  var19 = var23;
                  var12 = var32 + this.radiusY;
               }

               var20 = var35[var13];
               var24 = var18 & 16711680;
               var35[var32 + this.radiusY] = var24;
               if (isBetter(var24, var20, this.doDilation)) {
                  var20 = var24;
                  var13 = var32 + this.radiusY;
               }

               var21 = var29[var14];
               var25 = var18 & '\uff00';
               var29[var32 + this.radiusY] = var25;
               if (isBetter(var25, var21, this.doDilation)) {
                  var21 = var25;
                  var14 = var32 + this.radiusY;
               }

               var22 = var30[var15];
               var26 = var18 & 255;
               var30[var32 + this.radiusY] = var26;
               if (isBetter(var26, var22, this.doDilation)) {
                  var22 = var26;
                  var15 = var32 + this.radiusY;
               }

               var8[var9] = var19 << 24 | var20 | var21 | var22;
               var9 += var7;
            }

            for(var32 = var4 - this.radiusY; var32 <= this.radiusY; ++var32) {
               var8[var9] = var8[var9 - var7];
               var9 += var7;
            }

            for(var32 = this.radiusY + 1; var32 < var4; ++var32) {
               int var33;
               if (var12 == var11) {
                  var19 = var34[var11 + 1];
                  var12 = var11 + 1;

                  for(var33 = var11 + 2; var33 < var4; ++var33) {
                     var23 = var34[var33];
                     if (isBetter(var23, var19, this.doDilation)) {
                        var19 = var23;
                        var12 = var33;
                     }
                  }
               } else {
                  var19 = var34[var12];
               }

               if (var13 == var11) {
                  var20 = var35[var11 + 1];
                  var13 = var11 + 1;

                  for(var33 = var11 + 2; var33 < var4; ++var33) {
                     var24 = var35[var33];
                     if (isBetter(var24, var20, this.doDilation)) {
                        var20 = var24;
                        var13 = var33;
                     }
                  }
               } else {
                  var20 = var35[var13];
               }

               if (var14 == var11) {
                  var21 = var29[var11 + 1];
                  var14 = var11 + 1;

                  for(var33 = var11 + 2; var33 < var4; ++var33) {
                     var25 = var29[var33];
                     if (isBetter(var25, var21, this.doDilation)) {
                        var21 = var25;
                        var14 = var33;
                     }
                  }
               } else {
                  var21 = var29[var14];
               }

               if (var15 == var11) {
                  var22 = var30[var11 + 1];
                  var15 = var11 + 1;

                  for(var33 = var11 + 2; var33 < var4; ++var33) {
                     var26 = var30[var33];
                     if (isBetter(var26, var22, this.doDilation)) {
                        var22 = var26;
                        var15 = var33;
                     }
                  }
               } else {
                  var22 = var30[var15];
               }

               ++var11;
               var8[var9] = var19 << 24 | var20 | var21 | var22;
               var9 += var7;
            }
         }
      }

   }

   public WritableRaster filter(Raster var1, WritableRaster var2) {
      if (var2 != null) {
         this.checkCompatible(var2.getSampleModel());
      } else {
         if (var1 == null) {
            throw new IllegalArgumentException("src should not be null when dest is null");
         }

         var2 = this.createCompatibleDestRaster(var1);
      }

      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      DataBufferInt var5 = (DataBufferInt)var1.getDataBuffer();
      DataBufferInt var6 = (DataBufferInt)var2.getDataBuffer();
      int var7 = var5.getOffset();
      int var8 = var6.getOffset();
      int var9 = ((SinglePixelPackedSampleModel)var1.getSampleModel()).getScanlineStride();
      int var10 = ((SinglePixelPackedSampleModel)var2.getSampleModel()).getScanlineStride();
      int[] var11 = var5.getBankData()[0];
      int[] var12 = var6.getBankData()[0];
      int var14;
      int var16;
      int var17;
      int var18;
      int var19;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      int var28;
      int var29;
      int var30;
      int var31;
      int[] var32;
      int[] var33;
      int[] var34;
      int[] var35;
      int var36;
      int var37;
      int var38;
      int var39;
      int var40;
      int var41;
      int var42;
      if (var3 <= 2 * this.radiusX) {
         this.specialProcessRow(var1, var2);
      } else {
         var32 = new int[this.rangeX];
         var33 = new int[this.rangeX];
         var34 = new int[this.rangeX];
         var35 = new int[this.rangeX];

         for(var36 = 0; var36 < var4; ++var36) {
            int var13 = var7 + var36 * var9;
            var14 = var8 + var36 * var10;
            var16 = 0;
            var17 = 0;
            var18 = 0;
            var19 = 0;
            var20 = 0;
            var21 = var11[var13++];
            var24 = var21 >>> 24;
            var25 = var21 & 16711680;
            var26 = var21 & '\uff00';
            var27 = var21 & 255;
            var32[0] = var24;
            var33[0] = var25;
            var34[0] = var26;
            var35[0] = var27;

            for(var37 = 1; var37 <= this.radiusX; ++var37) {
               var22 = var11[var13++];
               var28 = var22 >>> 24;
               var29 = var22 & 16711680;
               var30 = var22 & '\uff00';
               var31 = var22 & 255;
               var32[var37] = var28;
               var33[var37] = var29;
               var34[var37] = var30;
               var35[var37] = var31;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var37;
               }

               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var37;
               }

               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var37;
               }

               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var37;
               }
            }

            var12[var14++] = var24 << 24 | var25 | var26 | var27;

            for(var37 = 1; var37 <= this.radiusX; ++var37) {
               var23 = var11[var13++];
               var24 = var32[var17];
               var28 = var23 >>> 24;
               var32[var37 + this.radiusX] = var28;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var37 + this.radiusX;
               }

               var25 = var33[var18];
               var29 = var23 & 16711680;
               var33[var37 + this.radiusX] = var29;
               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var37 + this.radiusX;
               }

               var26 = var34[var19];
               var30 = var23 & '\uff00';
               var34[var37 + this.radiusX] = var30;
               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var37 + this.radiusX;
               }

               var27 = var35[var20];
               var31 = var23 & 255;
               var35[var37 + this.radiusX] = var31;
               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var37 + this.radiusX;
               }

               var12[var14++] = var24 << 24 | var25 | var26 | var27;
            }

            for(var37 = this.radiusX + 1; var37 <= var3 - 1 - this.radiusX; ++var37) {
               var23 = var11[var13++];
               var28 = var23 >>> 24;
               var29 = var23 & 16711680;
               var30 = var23 & '\uff00';
               var31 = var23 & 255;
               var32[var16] = var28;
               var33[var16] = var29;
               var34[var16] = var30;
               var35[var16] = var31;
               if (var17 == var16) {
                  var24 = var32[0];
                  var17 = 0;

                  for(var38 = 1; var38 < this.rangeX; ++var38) {
                     var28 = var32[var38];
                     if (isBetter(var28, var24, this.doDilation)) {
                        var24 = var28;
                        var17 = var38;
                     }
                  }
               } else {
                  var24 = var32[var17];
                  if (isBetter(var28, var24, this.doDilation)) {
                     var24 = var28;
                     var17 = var16;
                  }
               }

               if (var18 == var16) {
                  var25 = var33[0];
                  var18 = 0;

                  for(var38 = 1; var38 < this.rangeX; ++var38) {
                     var29 = var33[var38];
                     if (isBetter(var29, var25, this.doDilation)) {
                        var25 = var29;
                        var18 = var38;
                     }
                  }
               } else {
                  var25 = var33[var18];
                  if (isBetter(var29, var25, this.doDilation)) {
                     var25 = var29;
                     var18 = var16;
                  }
               }

               if (var19 == var16) {
                  var26 = var34[0];
                  var19 = 0;

                  for(var38 = 1; var38 < this.rangeX; ++var38) {
                     var30 = var34[var38];
                     if (isBetter(var30, var26, this.doDilation)) {
                        var26 = var30;
                        var19 = var38;
                     }
                  }
               } else {
                  var26 = var34[var19];
                  if (isBetter(var30, var26, this.doDilation)) {
                     var26 = var30;
                     var19 = var16;
                  }
               }

               if (var20 == var16) {
                  var27 = var35[0];
                  var20 = 0;

                  for(var38 = 1; var38 < this.rangeX; ++var38) {
                     var31 = var35[var38];
                     if (isBetter(var31, var27, this.doDilation)) {
                        var27 = var31;
                        var20 = var38;
                     }
                  }
               } else {
                  var27 = var35[var20];
                  if (isBetter(var31, var27, this.doDilation)) {
                     var27 = var31;
                     var20 = var16;
                  }
               }

               var12[var14++] = var24 << 24 | var25 | var26 | var27;
               var16 = (var16 + 1) % this.rangeX;
            }

            var38 = var16 == 0 ? this.rangeX - 1 : var16 - 1;
            var39 = this.rangeX - 1;

            for(var40 = var3 - this.radiusX; var40 < var3; ++var40) {
               var37 = (var16 + 1) % this.rangeX;
               if (var17 == var16) {
                  var24 = var32[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var28 = var32[var41];
                     if (isBetter(var28, var24, this.doDilation)) {
                        var24 = var28;
                        var17 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeX;
                  }
               }

               if (var18 == var16) {
                  var25 = var33[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var29 = var33[var41];
                     if (isBetter(var29, var25, this.doDilation)) {
                        var25 = var29;
                        var18 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeX;
                  }
               }

               if (var19 == var16) {
                  var26 = var34[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var30 = var34[var41];
                     if (isBetter(var30, var26, this.doDilation)) {
                        var26 = var30;
                        var19 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeX;
                  }
               }

               if (var20 == var16) {
                  var27 = var35[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var31 = var35[var41];
                     if (isBetter(var31, var27, this.doDilation)) {
                        var27 = var31;
                        var20 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeX;
                  }
               }

               var12[var14++] = var24 << 24 | var25 | var26 | var27;
               var16 = (var16 + 1) % this.rangeX;
               --var39;
            }
         }
      }

      if (var4 <= 2 * this.radiusY) {
         this.specialProcessColumn(var1, var2);
      } else {
         var32 = new int[this.rangeY];
         var33 = new int[this.rangeY];
         var34 = new int[this.rangeY];
         var35 = new int[this.rangeY];

         for(var36 = 0; var36 < var3; ++var36) {
            var14 = var8 + var36;
            int var15 = var8 + var36;
            var16 = 0;
            var17 = 0;
            var18 = 0;
            var19 = 0;
            var20 = 0;
            var21 = var12[var15];
            var15 += var10;
            var24 = var21 >>> 24;
            var25 = var21 & 16711680;
            var26 = var21 & '\uff00';
            var27 = var21 & 255;
            var32[0] = var24;
            var33[0] = var25;
            var34[0] = var26;
            var35[0] = var27;

            for(var37 = 1; var37 <= this.radiusY; ++var37) {
               var22 = var12[var15];
               var15 += var10;
               var28 = var22 >>> 24;
               var29 = var22 & 16711680;
               var30 = var22 & '\uff00';
               var31 = var22 & 255;
               var32[var37] = var28;
               var33[var37] = var29;
               var34[var37] = var30;
               var35[var37] = var31;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var37;
               }

               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var37;
               }

               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var37;
               }

               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var37;
               }
            }

            var12[var14] = var24 << 24 | var25 | var26 | var27;
            var14 += var10;

            for(var37 = 1; var37 <= this.radiusY; ++var37) {
               var38 = var37 + this.radiusY;
               var23 = var12[var15];
               var15 += var10;
               var24 = var32[var17];
               var28 = var23 >>> 24;
               var32[var38] = var28;
               if (isBetter(var28, var24, this.doDilation)) {
                  var24 = var28;
                  var17 = var38;
               }

               var25 = var33[var18];
               var29 = var23 & 16711680;
               var33[var38] = var29;
               if (isBetter(var29, var25, this.doDilation)) {
                  var25 = var29;
                  var18 = var38;
               }

               var26 = var34[var19];
               var30 = var23 & '\uff00';
               var34[var38] = var30;
               if (isBetter(var30, var26, this.doDilation)) {
                  var26 = var30;
                  var19 = var38;
               }

               var27 = var35[var20];
               var31 = var23 & 255;
               var35[var38] = var31;
               if (isBetter(var31, var27, this.doDilation)) {
                  var27 = var31;
                  var20 = var38;
               }

               var12[var14] = var24 << 24 | var25 | var26 | var27;
               var14 += var10;
            }

            for(var37 = this.radiusY + 1; var37 <= var4 - 1 - this.radiusY; ++var37) {
               var23 = var12[var15];
               var15 += var10;
               var28 = var23 >>> 24;
               var29 = var23 & 16711680;
               var30 = var23 & '\uff00';
               var31 = var23 & 255;
               var32[var16] = var28;
               var33[var16] = var29;
               var34[var16] = var30;
               var35[var16] = var31;
               if (var17 == var16) {
                  var24 = var32[0];
                  var17 = 0;

                  for(var38 = 1; var38 <= 2 * this.radiusY; ++var38) {
                     var28 = var32[var38];
                     if (isBetter(var28, var24, this.doDilation)) {
                        var24 = var28;
                        var17 = var38;
                     }
                  }
               } else {
                  var24 = var32[var17];
                  if (isBetter(var28, var24, this.doDilation)) {
                     var24 = var28;
                     var17 = var16;
                  }
               }

               if (var18 == var16) {
                  var25 = var33[0];
                  var18 = 0;

                  for(var38 = 1; var38 <= 2 * this.radiusY; ++var38) {
                     var29 = var33[var38];
                     if (isBetter(var29, var25, this.doDilation)) {
                        var25 = var29;
                        var18 = var38;
                     }
                  }
               } else {
                  var25 = var33[var18];
                  if (isBetter(var29, var25, this.doDilation)) {
                     var25 = var29;
                     var18 = var16;
                  }
               }

               if (var19 == var16) {
                  var26 = var34[0];
                  var19 = 0;

                  for(var38 = 1; var38 <= 2 * this.radiusY; ++var38) {
                     var30 = var34[var38];
                     if (isBetter(var30, var26, this.doDilation)) {
                        var26 = var30;
                        var19 = var38;
                     }
                  }
               } else {
                  var26 = var34[var19];
                  if (isBetter(var30, var26, this.doDilation)) {
                     var26 = var30;
                     var19 = var16;
                  }
               }

               if (var20 == var16) {
                  var27 = var35[0];
                  var20 = 0;

                  for(var38 = 1; var38 <= 2 * this.radiusY; ++var38) {
                     var31 = var35[var38];
                     if (isBetter(var31, var27, this.doDilation)) {
                        var27 = var31;
                        var20 = var38;
                     }
                  }
               } else {
                  var27 = var35[var20];
                  if (isBetter(var31, var27, this.doDilation)) {
                     var27 = var31;
                     var20 = var16;
                  }
               }

               var12[var14] = var24 << 24 | var25 | var26 | var27;
               var14 += var10;
               var16 = (var16 + 1) % this.rangeY;
            }

            var38 = var16 == 0 ? 2 * this.radiusY : var16 - 1;
            var39 = this.rangeY - 1;

            for(var40 = var4 - this.radiusY; var40 < var4 - 1; ++var40) {
               var37 = (var16 + 1) % this.rangeY;
               if (var17 == var16) {
                  var24 = var32[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var28 = var32[var41];
                     if (isBetter(var28, var24, this.doDilation)) {
                        var24 = var28;
                        var17 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeY;
                  }
               }

               if (var18 == var16) {
                  var25 = var33[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var29 = var33[var41];
                     if (isBetter(var29, var25, this.doDilation)) {
                        var25 = var29;
                        var18 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeY;
                  }
               }

               if (var19 == var16) {
                  var26 = var34[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var30 = var34[var41];
                     if (isBetter(var30, var26, this.doDilation)) {
                        var26 = var30;
                        var19 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeY;
                  }
               }

               if (var20 == var16) {
                  var27 = var35[var38];
                  var41 = var37;

                  for(var42 = 1; var42 < var39; ++var42) {
                     var31 = var35[var41];
                     if (isBetter(var31, var27, this.doDilation)) {
                        var27 = var31;
                        var20 = var41;
                     }

                     var41 = (var41 + 1) % this.rangeY;
                  }
               }

               var12[var14] = var24 << 24 | var25 | var26 | var27;
               var14 += var10;
               var16 = (var16 + 1) % this.rangeY;
               --var39;
            }
         }
      }

      return var2;
   }

   public BufferedImage filter(BufferedImage var1, BufferedImage var2) {
      if (var1 == null) {
         throw new NullPointerException("Source image should not be null");
      } else {
         BufferedImage var3 = var1;
         BufferedImage var4 = var2;
         ColorModel var5;
         ColorModel var6;
         if (!this.isCompatible(var1.getColorModel(), var1.getSampleModel())) {
            var1 = new BufferedImage(var1.getWidth(), var1.getHeight(), 3);
            GraphicsUtil.copyData(var3, var1);
         } else if (!var1.isAlphaPremultiplied()) {
            var5 = var1.getColorModel();
            var6 = GraphicsUtil.coerceColorModel(var5, true);
            var1 = new BufferedImage(var6, var1.getRaster(), true, (Hashtable)null);
            GraphicsUtil.copyData(var3, var1);
         }

         if (var2 == null) {
            var2 = this.createCompatibleDestImage(var1, (ColorModel)null);
            var4 = var2;
         } else if (!this.isCompatible(var2.getColorModel(), var2.getSampleModel())) {
            var2 = this.createCompatibleDestImage(var1, (ColorModel)null);
         } else if (!var2.isAlphaPremultiplied()) {
            var5 = var2.getColorModel();
            var6 = GraphicsUtil.coerceColorModel(var5, true);
            var2 = new BufferedImage(var6, var2.getRaster(), true, (Hashtable)null);
         }

         this.filter((Raster)var1.getRaster(), (WritableRaster)var2.getRaster());
         if (var1.getRaster() == var3.getRaster() && var1.isAlphaPremultiplied() != var3.isAlphaPremultiplied()) {
            GraphicsUtil.copyData(var1, var3);
         }

         if (var2.getRaster() != var4.getRaster() || var2.isAlphaPremultiplied() != var4.isAlphaPremultiplied()) {
            GraphicsUtil.copyData(var2, var4);
         }

         return var4;
      }
   }
}
