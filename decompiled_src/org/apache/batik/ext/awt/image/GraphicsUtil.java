package org.apache.batik.ext.awt.image;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.ext.awt.image.renderable.PaintRable;
import org.apache.batik.ext.awt.image.rendered.AffineRed;
import org.apache.batik.ext.awt.image.rendered.Any2LsRGBRed;
import org.apache.batik.ext.awt.image.rendered.Any2sRGBRed;
import org.apache.batik.ext.awt.image.rendered.BufferedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.rendered.RenderedImageCachableRed;
import org.apache.batik.ext.awt.image.rendered.TranslateRed;

public class GraphicsUtil {
   public static AffineTransform IDENTITY = new AffineTransform();
   public static final boolean WARN_DESTINATION;
   public static final ColorModel Linear_sRGB;
   public static final ColorModel Linear_sRGB_Pre;
   public static final ColorModel Linear_sRGB_Unpre;
   public static final ColorModel sRGB;
   public static final ColorModel sRGB_Pre;
   public static final ColorModel sRGB_Unpre;

   public static void drawImage(Graphics2D var0, RenderedImage var1) {
      drawImage(var0, wrap(var1));
   }

   public static void drawImage(Graphics2D var0, CachableRed var1) {
      AffineTransform var2 = null;

      while(true) {
         TranslateRed var3;
         for(; !(var1 instanceof AffineRed); var1 = var3.getSource()) {
            if (!(var1 instanceof TranslateRed)) {
               AffineTransform var42 = var0.getTransform();
               if (var2 != null && !var2.isIdentity()) {
                  var2.preConcatenate(var42);
               } else {
                  var2 = var42;
               }

               ColorModel var44 = ((CachableRed)var1).getColorModel();
               ColorModel var45 = getDestinationColorModel(var0);
               ColorSpace var6 = null;
               if (var45 != null) {
                  var6 = var45.getColorSpace();
               }

               if (var6 == null) {
                  var6 = ColorSpace.getInstance(1000);
               }

               ColorModel var7 = var45;
               if (var45 == null || !var45.hasAlpha()) {
                  var7 = sRGB_Unpre;
               }

               if (var1 instanceof BufferedImageCachableRed && var6.equals(var44.getColorSpace()) && var7.equals(var44)) {
                  var0.setTransform(var2);
                  BufferedImageCachableRed var46 = (BufferedImageCachableRed)var1;
                  var0.drawImage(var46.getBufferedImage(), var46.getMinX(), var46.getMinY(), (ImageObserver)null);
                  var0.setTransform(var42);
                  return;
               }

               double var8 = var2.getDeterminant();
               if (!var2.isIdentity() && var8 <= 1.0) {
                  if (var2.getType() != 1) {
                     var1 = new AffineRed((CachableRed)var1, var2, var0.getRenderingHints());
                  } else {
                     int var10 = ((CachableRed)var1).getMinX() + (int)var2.getTranslateX();
                     int var11 = ((CachableRed)var1).getMinY() + (int)var2.getTranslateY();
                     var1 = new TranslateRed((CachableRed)var1, var10, var11);
                  }
               }

               if (var6 != var44.getColorSpace()) {
                  if (var6 == ColorSpace.getInstance(1000)) {
                     var1 = convertTosRGB((CachableRed)var1);
                  } else if (var6 == ColorSpace.getInstance(1004)) {
                     var1 = convertToLsRGB((CachableRed)var1);
                  }
               }

               var44 = ((CachableRed)var1).getColorModel();
               if (!var7.equals(var44)) {
                  var1 = FormatRed.construct((CachableRed)var1, var7);
               }

               if (!var2.isIdentity() && var8 > 1.0) {
                  var1 = new AffineRed((CachableRed)var1, var2, var0.getRenderingHints());
               }

               var0.setTransform(IDENTITY);
               Composite var47 = var0.getComposite();
               if (var0.getRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING) == "Printing" && SVGComposite.OVER.equals(var47)) {
                  var0.setComposite(SVGComposite.OVER);
               }

               Rectangle var48 = ((CachableRed)var1).getBounds();
               Shape var12 = var0.getClip();

               try {
                  Rectangle var13;
                  if (var12 == null) {
                     var13 = var48;
                  } else {
                     var13 = var12.getBounds();
                     if (!var13.intersects(var48)) {
                        return;
                     }

                     var13 = var13.intersection(var48);
                  }

                  Rectangle var14 = getDestinationBounds(var0);
                  if (var14 != null) {
                     if (!var13.intersects(var14)) {
                        return;
                     }

                     var13 = var13.intersection(var14);
                  }

                  boolean var15 = false;
                  var44 = ((CachableRed)var1).getColorModel();
                  SampleModel var16 = ((CachableRed)var1).getSampleModel();
                  if (var16.getWidth() * var16.getHeight() >= var13.width * var13.height) {
                     var15 = true;
                  }

                  Object var17 = var0.getRenderingHint(RenderingHintsKeyExt.KEY_AVOID_TILE_PAINTING);
                  if (var17 == RenderingHintsKeyExt.VALUE_AVOID_TILE_PAINTING_ON) {
                     var15 = true;
                  }

                  if (var17 == RenderingHintsKeyExt.VALUE_AVOID_TILE_PAINTING_OFF) {
                     var15 = false;
                  }

                  WritableRaster var18;
                  if (var15) {
                     Raster var19 = ((CachableRed)var1).getData(var13);
                     var18 = ((WritableRaster)var19).createWritableChild(var13.x, var13.y, var13.width, var13.height, 0, 0, (int[])null);
                     BufferedImage var20 = new BufferedImage(var44, var18, var44.isAlphaPremultiplied(), (Hashtable)null);
                     var0.drawImage(var20, var13.x, var13.y, (ImageObserver)null);
                  } else {
                     var18 = Raster.createWritableRaster(var16, new Point(0, 0));
                     BufferedImage var49 = new BufferedImage(var44, var18, var44.isAlphaPremultiplied(), (Hashtable)null);
                     int var50 = ((CachableRed)var1).getMinTileX();
                     int var21 = var50 + ((CachableRed)var1).getNumXTiles();
                     int var22 = ((CachableRed)var1).getMinTileY();
                     int var23 = var22 + ((CachableRed)var1).getNumYTiles();
                     int var24 = var16.getWidth();
                     int var25 = var16.getHeight();
                     Rectangle var26 = new Rectangle(0, 0, var24, var25);
                     Rectangle var27 = new Rectangle(0, 0, 0, 0);
                     int var28 = var22 * var25 + ((CachableRed)var1).getTileGridYOffset();
                     int var29 = (var13.y - var28) / var25;
                     if (var29 < 0) {
                        var29 = 0;
                     }

                     var22 += var29;
                     int var30 = var50 * var24 + ((CachableRed)var1).getTileGridXOffset();
                     var29 = (var13.x - var30) / var24;
                     if (var29 < 0) {
                        var29 = 0;
                     }

                     var50 += var29;
                     int var31 = var13.x + var13.width - 1;
                     int var32 = var13.y + var13.height - 1;
                     var28 = var22 * var25 + ((CachableRed)var1).getTileGridYOffset();
                     int var33 = var50 * var24 + ((CachableRed)var1).getTileGridXOffset();
                     int var34 = var24;
                     var30 = var33;

                     for(int var35 = var22; var35 < var23 && var28 <= var32; var28 += var25) {
                        for(int var36 = var50; var36 < var21 && var30 >= var33 && var30 <= var31; var30 += var34) {
                           var26.x = var30;
                           var26.y = var28;
                           Rectangle2D.intersect(var48, var26, var27);
                           WritableRaster var37 = var18.createWritableChild(0, 0, var27.width, var27.height, var27.x, var27.y, (int[])null);
                           ((CachableRed)var1).copyData(var37);
                           BufferedImage var38 = var49.getSubimage(0, 0, var27.width, var27.height);
                           var0.drawImage(var38, var27.x, var27.y, (ImageObserver)null);
                           ++var36;
                        }

                        var34 = -var34;
                        var30 += var34;
                        ++var35;
                     }
                  }
               } finally {
                  var0.setTransform(var42);
                  var0.setComposite(var47);
               }

               return;
            }

            var3 = (TranslateRed)var1;
            int var4 = var3.getDeltaX();
            int var5 = var3.getDeltaY();
            if (var2 == null) {
               var2 = AffineTransform.getTranslateInstance((double)var4, (double)var5);
            } else {
               var2.translate((double)var4, (double)var5);
            }
         }

         AffineRed var43 = (AffineRed)var1;
         if (var2 == null) {
            var2 = var43.getTransform();
         } else {
            var2.concatenate(var43.getTransform());
         }

         var1 = var43.getSource();
      }
   }

   public static void drawImage(Graphics2D var0, RenderableImage var1, RenderContext var2) {
      AffineTransform var3 = var0.getTransform();
      Shape var4 = var0.getClip();
      RenderingHints var5 = var0.getRenderingHints();
      Shape var6 = var2.getAreaOfInterest();
      if (var6 != null) {
         var0.clip(var6);
      }

      var0.transform(var2.getTransform());
      var0.setRenderingHints(var2.getRenderingHints());
      drawImage(var0, var1);
      var0.setTransform(var3);
      var0.setClip(var4);
      var0.setRenderingHints(var5);
   }

   public static void drawImage(Graphics2D var0, RenderableImage var1) {
      if (var1 instanceof PaintRable) {
         PaintRable var2 = (PaintRable)var1;
         if (var2.paintRable(var0)) {
            return;
         }
      }

      AffineTransform var4 = var0.getTransform();
      RenderedImage var3 = var1.createRendering(new RenderContext(var4, var0.getClip(), var0.getRenderingHints()));
      if (var3 != null) {
         var0.setTransform(IDENTITY);
         drawImage(var0, wrap(var3));
         var0.setTransform(var4);
      }
   }

   public static Graphics2D createGraphics(BufferedImage var0, RenderingHints var1) {
      Graphics2D var2 = var0.createGraphics();
      if (var1 != null) {
         var2.addRenderingHints(var1);
      }

      var2.setRenderingHint(RenderingHintsKeyExt.KEY_BUFFERED_IMAGE, new WeakReference(var0));
      var2.clip(new Rectangle(0, 0, var0.getWidth(), var0.getHeight()));
      return var2;
   }

   public static Graphics2D createGraphics(BufferedImage var0) {
      Graphics2D var1 = var0.createGraphics();
      var1.setRenderingHint(RenderingHintsKeyExt.KEY_BUFFERED_IMAGE, new WeakReference(var0));
      var1.clip(new Rectangle(0, 0, var0.getWidth(), var0.getHeight()));
      return var1;
   }

   public static BufferedImage getDestination(Graphics2D var0) {
      Object var1 = var0.getRenderingHint(RenderingHintsKeyExt.KEY_BUFFERED_IMAGE);
      if (var1 != null) {
         return (BufferedImage)((Reference)var1).get();
      } else {
         GraphicsConfiguration var2 = var0.getDeviceConfiguration();
         if (var2 == null) {
            return null;
         } else {
            GraphicsDevice var3 = var2.getDevice();
            if (WARN_DESTINATION && var3.getType() == 2 && var0.getRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING) != "Printing") {
               System.err.println("Graphics2D from BufferedImage lacks BUFFERED_IMAGE hint");
            }

            return null;
         }
      }
   }

   public static ColorModel getDestinationColorModel(Graphics2D var0) {
      BufferedImage var1 = getDestination(var0);
      if (var1 != null) {
         return var1.getColorModel();
      } else {
         GraphicsConfiguration var2 = var0.getDeviceConfiguration();
         if (var2 == null) {
            return null;
         } else if (var2.getDevice().getType() == 2) {
            return var0.getRenderingHint(RenderingHintsKeyExt.KEY_TRANSCODING) == "Printing" ? sRGB_Unpre : null;
         } else {
            return var2.getColorModel();
         }
      }
   }

   public static ColorSpace getDestinationColorSpace(Graphics2D var0) {
      ColorModel var1 = getDestinationColorModel(var0);
      return var1 != null ? var1.getColorSpace() : null;
   }

   public static Rectangle getDestinationBounds(Graphics2D var0) {
      BufferedImage var1 = getDestination(var0);
      if (var1 != null) {
         return new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
      } else {
         GraphicsConfiguration var2 = var0.getDeviceConfiguration();
         if (var2 == null) {
            return null;
         } else {
            return var2.getDevice().getType() == 2 ? null : null;
         }
      }
   }

   public static ColorModel makeLinear_sRGBCM(boolean var0) {
      return var0 ? Linear_sRGB_Pre : Linear_sRGB_Unpre;
   }

   public static BufferedImage makeLinearBufferedImage(int var0, int var1, boolean var2) {
      ColorModel var3 = makeLinear_sRGBCM(var2);
      WritableRaster var4 = var3.createCompatibleWritableRaster(var0, var1);
      return new BufferedImage(var3, var4, var2, (Hashtable)null);
   }

   public static CachableRed convertToLsRGB(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      ColorSpace var2 = var1.getColorSpace();
      return (CachableRed)(var2 == ColorSpace.getInstance(1004) ? var0 : new Any2LsRGBRed(var0));
   }

   public static CachableRed convertTosRGB(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      ColorSpace var2 = var1.getColorSpace();
      return (CachableRed)(var2 == ColorSpace.getInstance(1000) ? var0 : new Any2sRGBRed(var0));
   }

   public static CachableRed wrap(RenderedImage var0) {
      if (var0 instanceof CachableRed) {
         return (CachableRed)var0;
      } else {
         return (CachableRed)(var0 instanceof BufferedImage ? new BufferedImageCachableRed((BufferedImage)var0) : new RenderedImageCachableRed(var0));
      }
   }

   public static void copyData_INT_PACK(Raster var0, WritableRaster var1) {
      int var2 = var1.getMinX();
      if (var2 < var0.getMinX()) {
         var2 = var0.getMinX();
      }

      int var3 = var1.getMinY();
      if (var3 < var0.getMinY()) {
         var3 = var0.getMinY();
      }

      int var4 = var1.getMinX() + var1.getWidth() - 1;
      if (var4 > var0.getMinX() + var0.getWidth() - 1) {
         var4 = var0.getMinX() + var0.getWidth() - 1;
      }

      int var5 = var1.getMinY() + var1.getHeight() - 1;
      if (var5 > var0.getMinY() + var0.getHeight() - 1) {
         var5 = var0.getMinY() + var0.getHeight() - 1;
      }

      int var6 = var4 - var2 + 1;
      int var7 = var5 - var3 + 1;
      SinglePixelPackedSampleModel var8 = (SinglePixelPackedSampleModel)var0.getSampleModel();
      int var9 = var8.getScanlineStride();
      DataBufferInt var10 = (DataBufferInt)var0.getDataBuffer();
      int[] var11 = var10.getBankData()[0];
      int var12 = var10.getOffset() + var8.getOffset(var2 - var0.getSampleModelTranslateX(), var3 - var0.getSampleModelTranslateY());
      SinglePixelPackedSampleModel var13 = (SinglePixelPackedSampleModel)var1.getSampleModel();
      int var14 = var13.getScanlineStride();
      DataBufferInt var15 = (DataBufferInt)var1.getDataBuffer();
      int[] var16 = var15.getBankData()[0];
      int var17 = var15.getOffset() + var13.getOffset(var2 - var1.getSampleModelTranslateX(), var3 - var1.getSampleModelTranslateY());
      if (var9 == var14 && var9 == var6) {
         System.arraycopy(var11, var12, var16, var17, var6 * var7);
      } else {
         int var18;
         int var19;
         int var20;
         if (var6 > 128) {
            var18 = var12;
            var19 = var17;

            for(var20 = 0; var20 < var7; ++var20) {
               System.arraycopy(var11, var18, var16, var19, var6);
               var18 += var9;
               var19 += var14;
            }
         } else {
            for(var18 = 0; var18 < var7; ++var18) {
               var19 = var12 + var18 * var9;
               var20 = var17 + var18 * var14;

               for(int var21 = 0; var21 < var6; ++var21) {
                  var16[var20++] = var11[var19++];
               }
            }
         }
      }

   }

   public static void copyData_FALLBACK(Raster var0, WritableRaster var1) {
      int var2 = var1.getMinX();
      if (var2 < var0.getMinX()) {
         var2 = var0.getMinX();
      }

      int var3 = var1.getMinY();
      if (var3 < var0.getMinY()) {
         var3 = var0.getMinY();
      }

      int var4 = var1.getMinX() + var1.getWidth() - 1;
      if (var4 > var0.getMinX() + var0.getWidth() - 1) {
         var4 = var0.getMinX() + var0.getWidth() - 1;
      }

      int var5 = var1.getMinY() + var1.getHeight() - 1;
      if (var5 > var0.getMinY() + var0.getHeight() - 1) {
         var5 = var0.getMinY() + var0.getHeight() - 1;
      }

      int var6 = var4 - var2 + 1;
      int[] var7 = null;

      for(int var8 = var3; var8 <= var5; ++var8) {
         var7 = var0.getPixels(var2, var8, var6, 1, var7);
         var1.setPixels(var2, var8, var6, 1, var7);
      }

   }

   public static void copyData(Raster var0, WritableRaster var1) {
      if (is_INT_PACK_Data(var0.getSampleModel(), false) && is_INT_PACK_Data(var1.getSampleModel(), false)) {
         copyData_INT_PACK(var0, var1);
      } else {
         copyData_FALLBACK(var0, var1);
      }
   }

   public static WritableRaster copyRaster(Raster var0) {
      return copyRaster(var0, var0.getMinX(), var0.getMinY());
   }

   public static WritableRaster copyRaster(Raster var0, int var1, int var2) {
      WritableRaster var3 = Raster.createWritableRaster(var0.getSampleModel(), new Point(0, 0));
      var3 = var3.createWritableChild(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY(), var0.getWidth(), var0.getHeight(), var1, var2, (int[])null);
      DataBuffer var4 = var0.getDataBuffer();
      DataBuffer var5 = var3.getDataBuffer();
      if (var4.getDataType() != var5.getDataType()) {
         throw new IllegalArgumentException("New DataBuffer doesn't match original");
      } else {
         int var6 = var4.getSize();
         int var7 = var4.getNumBanks();
         int[] var8 = var4.getOffsets();

         for(int var9 = 0; var9 < var7; ++var9) {
            switch (var4.getDataType()) {
               case 0:
                  DataBufferByte var14 = (DataBufferByte)var4;
                  DataBufferByte var17 = (DataBufferByte)var5;
                  System.arraycopy(var14.getData(var9), var8[var9], var17.getData(var9), var8[var9], var6);
                  break;
               case 1:
                  DataBufferUShort var13 = (DataBufferUShort)var4;
                  DataBufferUShort var16 = (DataBufferUShort)var5;
                  System.arraycopy(var13.getData(var9), var8[var9], var16.getData(var9), var8[var9], var6);
                  break;
               case 2:
                  DataBufferShort var12 = (DataBufferShort)var4;
                  DataBufferShort var15 = (DataBufferShort)var5;
                  System.arraycopy(var12.getData(var9), var8[var9], var15.getData(var9), var8[var9], var6);
                  break;
               case 3:
                  DataBufferInt var10 = (DataBufferInt)var4;
                  DataBufferInt var11 = (DataBufferInt)var5;
                  System.arraycopy(var10.getData(var9), var8[var9], var11.getData(var9), var8[var9], var6);
            }
         }

         return var3;
      }
   }

   public static WritableRaster makeRasterWritable(Raster var0) {
      return makeRasterWritable(var0, var0.getMinX(), var0.getMinY());
   }

   public static WritableRaster makeRasterWritable(Raster var0, int var1, int var2) {
      WritableRaster var3 = Raster.createWritableRaster(var0.getSampleModel(), var0.getDataBuffer(), new Point(0, 0));
      var3 = var3.createWritableChild(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY(), var0.getWidth(), var0.getHeight(), var1, var2, (int[])null);
      return var3;
   }

   public static ColorModel coerceColorModel(ColorModel var0, boolean var1) {
      if (var0.isAlphaPremultiplied() == var1) {
         return var0;
      } else {
         WritableRaster var2 = var0.createCompatibleWritableRaster(1, 1);
         return var0.coerceData(var2, var1);
      }
   }

   public static ColorModel coerceData(WritableRaster var0, ColorModel var1, boolean var2) {
      if (!var1.hasAlpha()) {
         return var1;
      } else if (var1.isAlphaPremultiplied() == var2) {
         return var1;
      } else {
         if (var2) {
            multiplyAlpha(var0);
         } else {
            divideAlpha(var0);
         }

         return coerceColorModel(var1, var2);
      }
   }

   public static void multiplyAlpha(WritableRaster var0) {
      if (is_BYTE_COMP_Data(var0.getSampleModel())) {
         mult_BYTE_COMP_Data(var0);
      } else if (is_INT_PACK_Data(var0.getSampleModel(), true)) {
         mult_INT_PACK_Data(var0);
      } else {
         int[] var1 = null;
         int var2 = var0.getNumBands();
         float var3 = 0.003921569F;
         int var4 = var0.getMinX();
         int var5 = var4 + var0.getWidth();
         int var6 = var0.getMinY();
         int var7 = var6 + var0.getHeight();

         for(int var11 = var6; var11 < var7; ++var11) {
            for(int var12 = var4; var12 < var5; ++var12) {
               var1 = var0.getPixel(var12, var11, var1);
               int var8 = var1[var2 - 1];
               if (var8 >= 0 && var8 < 255) {
                  float var10 = (float)var8 * var3;

                  for(int var9 = 0; var9 < var2 - 1; ++var9) {
                     var1[var9] = (int)((float)var1[var9] * var10 + 0.5F);
                  }

                  var0.setPixel(var12, var11, var1);
               }
            }
         }
      }

   }

   public static void divideAlpha(WritableRaster var0) {
      if (is_BYTE_COMP_Data(var0.getSampleModel())) {
         divide_BYTE_COMP_Data(var0);
      } else if (is_INT_PACK_Data(var0.getSampleModel(), true)) {
         divide_INT_PACK_Data(var0);
      } else {
         int var8 = var0.getNumBands();
         int[] var9 = null;
         int var1 = var0.getMinX();
         int var2 = var1 + var0.getWidth();
         int var3 = var0.getMinY();
         int var4 = var3 + var0.getHeight();

         for(int var10 = var3; var10 < var4; ++var10) {
            for(int var11 = var1; var11 < var2; ++var11) {
               var9 = var0.getPixel(var11, var10, var9);
               int var5 = var9[var8 - 1];
               if (var5 > 0 && var5 < 255) {
                  float var7 = 255.0F / (float)var5;

                  for(int var6 = 0; var6 < var8 - 1; ++var6) {
                     var9[var6] = (int)((float)var9[var6] * var7 + 0.5F);
                  }

                  var0.setPixel(var11, var10, var9);
               }
            }
         }
      }

   }

   public static void copyData(BufferedImage var0, BufferedImage var1) {
      Rectangle var2 = new Rectangle(0, 0, var0.getWidth(), var0.getHeight());
      copyData(var0, var2, var1, new Point(0, 0));
   }

   public static void copyData(BufferedImage var0, Rectangle var1, BufferedImage var2, Point var3) {
      boolean var4 = var0.getColorModel().hasAlpha();
      boolean var5 = var2.getColorModel().hasAlpha();
      if (var4 != var5 || var4 && var0.isAlphaPremultiplied() != var2.isAlphaPremultiplied()) {
         int[] var6 = null;
         WritableRaster var7 = var0.getRaster();
         WritableRaster var8 = var2.getRaster();
         int var9 = var8.getNumBands();
         int var10 = var3.x - var1.x;
         int var11 = var3.y - var1.y;
         int var12 = var1.width;
         int var13 = var1.x;
         int var14 = var1.y;
         int var15 = var14 + var1.height - 1;
         int[] var16;
         int var17;
         int var18;
         int var19;
         int var20;
         if (!var4) {
            var16 = new int[var9 * var12];

            for(var17 = var12 * var9 - 1; var17 >= 0; var17 -= var9) {
               var16[var17] = 255;
            }

            for(var20 = var14; var20 <= var15; ++var20) {
               var6 = var7.getPixels(var13, var20, var12, 1, var6);
               var19 = var12 * (var9 - 1) - 1;
               var17 = var12 * var9 - 2;
               label195:
               switch (var9) {
                  case 4:
                     while(true) {
                        if (var19 < 0) {
                           break label195;
                        }

                        var16[var17--] = var6[var19--];
                        var16[var17--] = var6[var19--];
                        var16[var17--] = var6[var19--];
                        --var17;
                     }
                  default:
                     while(var19 >= 0) {
                        for(var18 = 0; var18 < var9 - 1; ++var18) {
                           var16[var17--] = var6[var19--];
                        }

                        --var17;
                     }
               }

               var8.setPixels(var13 + var10, var20 + var11, var12, 1, var16);
            }
         } else {
            int var21;
            int var22;
            int var26;
            if (var5 && var2.isAlphaPremultiplied()) {
               var20 = 65793;
               var21 = 8388608;

               for(var22 = var14; var22 <= var15; ++var22) {
                  var6 = var7.getPixels(var13, var22, var12, 1, var6);
                  var19 = var9 * var12 - 1;
                  label164:
                  switch (var9) {
                     case 4:
                        while(true) {
                           if (var19 < 0) {
                              break label164;
                           }

                           var26 = var6[var19];
                           if (var26 == 255) {
                              var19 -= 4;
                           } else {
                              --var19;
                              var18 = var20 * var26;
                              var6[var19] = var6[var19] * var18 + var21 >>> 24;
                              --var19;
                              var6[var19] = var6[var19] * var18 + var21 >>> 24;
                              --var19;
                              var6[var19] = var6[var19] * var18 + var21 >>> 24;
                              --var19;
                           }
                        }
                     default:
                        label178:
                        while(true) {
                           while(true) {
                              if (var19 < 0) {
                                 break label178;
                              }

                              var26 = var6[var19];
                              if (var26 == 255) {
                                 var19 -= var9;
                              } else {
                                 --var19;
                                 var18 = var20 * var26;

                                 for(var17 = 0; var17 < var9 - 1; ++var17) {
                                    var6[var19] = var6[var19] * var18 + var21 >>> 24;
                                    --var19;
                                 }
                              }
                           }
                        }
                  }

                  var8.setPixels(var13 + var10, var22 + var11, var12, 1, var6);
               }
            } else if (var5 && !var2.isAlphaPremultiplied()) {
               var20 = 16711680;
               char var27 = '耀';

               for(var22 = var14; var22 <= var15; ++var22) {
                  var6 = var7.getPixels(var13, var22, var12, 1, var6);
                  var19 = var9 * var12 - 1;
                  label132:
                  switch (var9) {
                     case 4:
                        while(true) {
                           while(true) {
                              if (var19 < 0) {
                                 break label132;
                              }

                              var26 = var6[var19];
                              if (var26 > 0 && var26 < 255) {
                                 --var19;
                                 var18 = var20 / var26;
                                 var6[var19] = var6[var19] * var18 + var27 >>> 16;
                                 --var19;
                                 var6[var19] = var6[var19] * var18 + var27 >>> 16;
                                 --var19;
                                 var6[var19] = var6[var19] * var18 + var27 >>> 16;
                                 --var19;
                              } else {
                                 var19 -= 4;
                              }
                           }
                        }
                     default:
                        label148:
                        while(true) {
                           while(true) {
                              if (var19 < 0) {
                                 break label148;
                              }

                              var26 = var6[var19];
                              if (var26 > 0 && var26 < 255) {
                                 --var19;
                                 var18 = var20 / var26;

                                 for(var17 = 0; var17 < var9 - 1; ++var17) {
                                    var6[var19] = var6[var19] * var18 + var27 >>> 16;
                                    --var19;
                                 }
                              } else {
                                 var19 -= var9;
                              }
                           }
                        }
                  }

                  var8.setPixels(var13 + var10, var22 + var11, var12, 1, var6);
               }
            } else if (var0.isAlphaPremultiplied()) {
               var16 = new int[var9 * var12];
               var22 = 16711680;
               char var23 = '耀';

               label116:
               for(int var24 = var14; var24 <= var15; ++var24) {
                  var6 = var7.getPixels(var13, var24, var12, 1, var6);
                  var20 = (var9 + 1) * var12 - 1;
                  var21 = var9 * var12 - 1;

                  while(true) {
                     while(true) {
                        while(var20 >= 0) {
                           var17 = var6[var20];
                           --var20;
                           if (var17 > 0) {
                              if (var17 < 255) {
                                 var19 = var22 / var17;

                                 for(var18 = 0; var18 < var9; ++var18) {
                                    var16[var21--] = var6[var20--] * var19 + var23 >>> 16;
                                 }
                              } else {
                                 for(var18 = 0; var18 < var9; ++var18) {
                                    var16[var21--] = var6[var20--];
                                 }
                              }
                           } else {
                              var20 -= var9;

                              for(var18 = 0; var18 < var9; ++var18) {
                                 var16[var21--] = 255;
                              }
                           }
                        }

                        var8.setPixels(var13 + var10, var24 + var11, var12, 1, var16);
                        continue label116;
                     }
                  }
               }
            } else {
               Rectangle var25 = new Rectangle(var3.x, var3.y, var1.width, var1.height);

               for(var17 = 0; var17 < var9; ++var17) {
                  copyBand(var7, var1, var17, var8, var25, var17);
               }
            }
         }

      } else {
         copyData((Raster)var0.getRaster(), (WritableRaster)var2.getRaster());
      }
   }

   public static void copyBand(Raster var0, int var1, WritableRaster var2, int var3) {
      Rectangle var4 = var0.getBounds();
      Rectangle var5 = var2.getBounds();
      Rectangle var6 = var4.intersection(var5);
      copyBand(var0, var6, var1, var2, var6, var3);
   }

   public static void copyBand(Raster var0, Rectangle var1, int var2, WritableRaster var3, Rectangle var4, int var5) {
      int var6 = var4.y - var1.y;
      int var7 = var4.x - var1.x;
      var1 = var1.intersection(var0.getBounds());
      var4 = var4.intersection(var3.getBounds());
      int var8;
      if (var4.width < var1.width) {
         var8 = var4.width;
      } else {
         var8 = var1.width;
      }

      int var9;
      if (var4.height < var1.height) {
         var9 = var4.height;
      } else {
         var9 = var1.height;
      }

      int var10 = var1.x + var7;
      int[] var11 = null;

      for(int var12 = var1.y; var12 < var1.y + var9; ++var12) {
         var11 = var0.getSamples(var1.x, var12, var8, 1, var2, var11);
         var3.setSamples(var10, var12 + var6, var8, 1, var5, var11);
      }

   }

   public static boolean is_INT_PACK_Data(SampleModel var0, boolean var1) {
      if (!(var0 instanceof SinglePixelPackedSampleModel)) {
         return false;
      } else if (var0.getDataType() != 3) {
         return false;
      } else {
         SinglePixelPackedSampleModel var2 = (SinglePixelPackedSampleModel)var0;
         int[] var3 = var2.getBitMasks();
         if (var3.length == 3) {
            if (var1) {
               return false;
            }
         } else if (var3.length != 4) {
            return false;
         }

         if (var3[0] != 16711680) {
            return false;
         } else if (var3[1] != 65280) {
            return false;
         } else if (var3[2] != 255) {
            return false;
         } else {
            return var3.length != 4 || var3[3] == -16777216;
         }
      }
   }

   public static boolean is_BYTE_COMP_Data(SampleModel var0) {
      if (!(var0 instanceof ComponentSampleModel)) {
         return false;
      } else {
         return var0.getDataType() == 0;
      }
   }

   protected static void divide_INT_PACK_Data(WritableRaster var0) {
      SinglePixelPackedSampleModel var1 = (SinglePixelPackedSampleModel)var0.getSampleModel();
      int var2 = var0.getWidth();
      int var3 = var1.getScanlineStride();
      DataBufferInt var4 = (DataBufferInt)var0.getDataBuffer();
      int var5 = var4.getOffset() + var1.getOffset(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY());
      int[] var6 = var4.getBankData()[0];

      for(int var7 = 0; var7 < var0.getHeight(); ++var7) {
         int var8 = var5 + var7 * var3;

         for(int var9 = var8 + var2; var8 < var9; ++var8) {
            int var10 = var6[var8];
            int var11 = var10 >>> 24;
            if (var11 <= 0) {
               var6[var8] = 16777215;
            } else if (var11 < 255) {
               int var12 = 16711680 / var11;
               var6[var8] = var11 << 24 | ((var10 & 16711680) >> 16) * var12 & 16711680 | (((var10 & '\uff00') >> 8) * var12 & 16711680) >> 8 | ((var10 & 255) * var12 & 16711680) >> 16;
            }
         }
      }

   }

   protected static void mult_INT_PACK_Data(WritableRaster var0) {
      SinglePixelPackedSampleModel var1 = (SinglePixelPackedSampleModel)var0.getSampleModel();
      int var2 = var0.getWidth();
      int var3 = var1.getScanlineStride();
      DataBufferInt var4 = (DataBufferInt)var0.getDataBuffer();
      int var5 = var4.getOffset() + var1.getOffset(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY());
      int[] var6 = var4.getBankData()[0];

      for(int var7 = 0; var7 < var0.getHeight(); ++var7) {
         int var8 = var5 + var7 * var3;

         for(int var9 = var8 + var2; var8 < var9; ++var8) {
            int var10 = var6[var8];
            int var11 = var10 >>> 24;
            if (var11 >= 0 && var11 < 255) {
               var6[var8] = var11 << 24 | (var10 & 16711680) * var11 >> 8 & 16711680 | (var10 & '\uff00') * var11 >> 8 & '\uff00' | (var10 & 255) * var11 >> 8 & 255;
            }
         }
      }

   }

   protected static void divide_BYTE_COMP_Data(WritableRaster var0) {
      ComponentSampleModel var1 = (ComponentSampleModel)var0.getSampleModel();
      int var2 = var0.getWidth();
      int var3 = var1.getScanlineStride();
      int var4 = var1.getPixelStride();
      int[] var5 = var1.getBandOffsets();
      DataBufferByte var6 = (DataBufferByte)var0.getDataBuffer();
      int var7 = var6.getOffset() + var1.getOffset(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY());
      int var8 = var5[var5.length - 1];
      int var9 = var5.length - 1;
      byte[] var10 = var6.getBankData()[0];

      for(int var11 = 0; var11 < var0.getHeight(); ++var11) {
         int var12 = var7 + var11 * var3;

         for(int var13 = var12 + var2 * var4; var12 < var13; var12 += var4) {
            int var14 = var10[var12 + var8] & 255;
            int var15;
            if (var14 == 0) {
               for(var15 = 0; var15 < var9; ++var15) {
                  var10[var12 + var5[var15]] = -1;
               }
            } else if (var14 < 255) {
               var15 = 16711680 / var14;

               for(int var16 = 0; var16 < var9; ++var16) {
                  int var17 = var12 + var5[var16];
                  var10[var17] = (byte)((var10[var17] & 255) * var15 >>> 16);
               }
            }
         }
      }

   }

   protected static void mult_BYTE_COMP_Data(WritableRaster var0) {
      ComponentSampleModel var1 = (ComponentSampleModel)var0.getSampleModel();
      int var2 = var0.getWidth();
      int var3 = var1.getScanlineStride();
      int var4 = var1.getPixelStride();
      int[] var5 = var1.getBandOffsets();
      DataBufferByte var6 = (DataBufferByte)var0.getDataBuffer();
      int var7 = var6.getOffset() + var1.getOffset(var0.getMinX() - var0.getSampleModelTranslateX(), var0.getMinY() - var0.getSampleModelTranslateY());
      int var8 = var5[var5.length - 1];
      int var9 = var5.length - 1;
      byte[] var10 = var6.getBankData()[0];

      for(int var11 = 0; var11 < var0.getHeight(); ++var11) {
         int var12 = var7 + var11 * var3;

         for(int var13 = var12 + var2 * var4; var12 < var13; var12 += var4) {
            int var14 = var10[var12 + var8] & 255;
            if (var14 != 255) {
               for(int var15 = 0; var15 < var9; ++var15) {
                  int var16 = var12 + var5[var15];
                  var10[var16] = (byte)((var10[var16] & 255) * var14 >> 8);
               }
            }
         }
      }

   }

   static {
      boolean var0 = true;

      try {
         String var1 = System.getProperty("org.apache.batik.warn_destination", "true");
         var0 = Boolean.valueOf(var1);
      } catch (SecurityException var6) {
      } catch (NumberFormatException var7) {
      } finally {
         WARN_DESTINATION = var0;
      }

      Linear_sRGB = new DirectColorModel(ColorSpace.getInstance(1004), 24, 16711680, 65280, 255, 0, false, 3);
      Linear_sRGB_Pre = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, 65280, 255, -16777216, true, 3);
      Linear_sRGB_Unpre = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, 65280, 255, -16777216, false, 3);
      sRGB = new DirectColorModel(ColorSpace.getInstance(1000), 24, 16711680, 65280, 255, 0, false, 3);
      sRGB_Pre = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, true, 3);
      sRGB_Unpre = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, false, 3);
   }
}
