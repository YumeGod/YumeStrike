package org.apache.batik.ext.awt.image.rendered;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.color.ICCColorSpaceExt;

public class ProfileRed extends AbstractRed {
   private static final ColorSpace sRGBCS = ColorSpace.getInstance(1000);
   private static final ColorModel sRGBCM;
   private ICCColorSpaceExt colorSpace;

   public ProfileRed(CachableRed var1, ICCColorSpaceExt var2) {
      this.colorSpace = var2;
      this.init(var1, var1.getBounds(), sRGBCM, sRGBCM.createCompatibleSampleModel(var1.getWidth(), var1.getHeight()), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public CachableRed getSource() {
      return (CachableRed)this.getSources().get(0);
   }

   public WritableRaster copyData(WritableRaster var1) {
      try {
         CachableRed var2 = this.getSource();
         Object var3 = var2.getColorModel();
         ColorSpace var4 = ((ColorModel)var3).getColorSpace();
         int var5 = var4.getNumComponents();
         int var6 = this.colorSpace.getNumComponents();
         if (var5 != var6) {
            System.err.println("Input image and associated color profile have mismatching number of color components: conversion is not possible");
            return var1;
         } else {
            int var7 = var1.getWidth();
            int var8 = var1.getHeight();
            int var9 = var1.getMinX();
            int var10 = var1.getMinY();
            WritableRaster var11 = ((ColorModel)var3).createCompatibleWritableRaster(var7, var8);
            var11 = var11.createWritableTranslatedChild(var9, var10);
            var2.copyData(var11);
            ComponentColorModel var12;
            BufferedImage var14;
            if (!(var3 instanceof ComponentColorModel) || !(var2.getSampleModel() instanceof BandedSampleModel) || ((ColorModel)var3).hasAlpha() && ((ColorModel)var3).isAlphaPremultiplied()) {
               var12 = new ComponentColorModel(var4, ((ColorModel)var3).getComponentSize(), ((ColorModel)var3).hasAlpha(), false, ((ColorModel)var3).getTransparency(), 0);
               WritableRaster var13 = Raster.createBandedRaster(0, var1.getWidth(), var1.getHeight(), var12.getNumComponents(), new Point(0, 0));
               var14 = new BufferedImage(var12, var13, var12.isAlphaPremultiplied(), (Hashtable)null);
               BufferedImage var15 = new BufferedImage((ColorModel)var3, var11.createWritableTranslatedChild(0, 0), ((ColorModel)var3).isAlphaPremultiplied(), (Hashtable)null);
               Graphics2D var16 = var14.createGraphics();
               var16.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
               var16.drawImage(var15, 0, 0, (ImageObserver)null);
               var3 = var12;
               var11 = var13.createWritableTranslatedChild(var9, var10);
            }

            var12 = new ComponentColorModel(this.colorSpace, ((ColorModel)var3).getComponentSize(), false, false, 1, 0);
            DataBufferByte var25 = (DataBufferByte)var11.getDataBuffer();
            var11 = Raster.createBandedRaster(var25, var1.getWidth(), var1.getHeight(), var1.getWidth(), new int[]{0, 1, 2}, new int[]{0, 0, 0}, new Point(0, 0));
            var14 = new BufferedImage(var12, var11, var12.isAlphaPremultiplied(), (Hashtable)null);
            ComponentColorModel var26 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8}, false, false, 1, 0);
            WritableRaster var27 = Raster.createBandedRaster(0, var1.getWidth(), var1.getHeight(), var26.getNumComponents(), new Point(0, 0));
            BufferedImage var17 = new BufferedImage(var26, var27, false, (Hashtable)null);
            ColorConvertOp var18 = new ColorConvertOp((RenderingHints)null);
            var18.filter(var14, var17);
            if (((ColorModel)var3).hasAlpha()) {
               DataBufferByte var19 = (DataBufferByte)var27.getDataBuffer();
               byte[][] var20 = var25.getBankData();
               byte[][] var21 = var19.getBankData();
               byte[][] var22 = new byte[][]{var21[0], var21[1], var21[2], var20[3]};
               DataBufferByte var23 = new DataBufferByte(var22, var20[0].length);
               var11 = Raster.createBandedRaster(var23, var1.getWidth(), var1.getHeight(), var1.getWidth(), new int[]{0, 1, 2, 3}, new int[]{0, 0, 0, 0}, new Point(0, 0));
               var26 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
               var17 = new BufferedImage(var26, var11, false, (Hashtable)null);
            }

            BufferedImage var28 = new BufferedImage(sRGBCM, var1.createWritableTranslatedChild(0, 0), false, (Hashtable)null);
            Graphics2D var29 = var28.createGraphics();
            var29.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            var29.drawImage(var17, 0, 0, (ImageObserver)null);
            var29.dispose();
            return var1;
         }
      } catch (Exception var24) {
         var24.printStackTrace();
         throw new Error(var24.getMessage());
      }
   }

   static {
      sRGBCM = new DirectColorModel(sRGBCS, 32, 16711680, 65280, 255, -16777216, false, 3);
   }
}
