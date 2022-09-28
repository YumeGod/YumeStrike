package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class FormatRed extends AbstractRed {
   public static CachableRed construct(CachableRed var0, ColorModel var1) {
      ColorModel var2 = var0.getColorModel();
      if (var1.hasAlpha() == var2.hasAlpha() && var1.isAlphaPremultiplied() == var2.isAlphaPremultiplied()) {
         if (var1.getNumComponents() != var2.getNumComponents()) {
            throw new IllegalArgumentException("Incompatible ColorModel given");
         } else if (var2 instanceof ComponentColorModel && var1 instanceof ComponentColorModel) {
            return var0;
         } else {
            return (CachableRed)(var2 instanceof DirectColorModel && var1 instanceof DirectColorModel ? var0 : new FormatRed(var0, var1));
         }
      } else {
         return new FormatRed(var0, var1);
      }
   }

   public FormatRed(CachableRed var1, SampleModel var2) {
      super((CachableRed)var1, var1.getBounds(), makeColorModel(var1, var2), var2, var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public FormatRed(CachableRed var1, ColorModel var2) {
      super((CachableRed)var1, var1.getBounds(), var2, makeSampleModel(var1, var2), var1.getTileGridXOffset(), var1.getTileGridYOffset(), (Map)null);
   }

   public CachableRed getSource() {
      return (CachableRed)this.getSources().get(0);
   }

   public Object getProperty(String var1) {
      return this.getSource().getProperty(var1);
   }

   public String[] getPropertyNames() {
      return this.getSource().getPropertyNames();
   }

   public WritableRaster copyData(WritableRaster var1) {
      ColorModel var2 = this.getColorModel();
      CachableRed var3 = this.getSource();
      ColorModel var4 = var3.getColorModel();
      SampleModel var5 = var3.getSampleModel();
      var5 = var5.createCompatibleSampleModel(var1.getWidth(), var1.getHeight());
      WritableRaster var6 = Raster.createWritableRaster(var5, new Point(var1.getMinX(), var1.getMinY()));
      this.getSource().copyData(var6);
      BufferedImage var7 = new BufferedImage(var4, var6.createWritableTranslatedChild(0, 0), var4.isAlphaPremultiplied(), (Hashtable)null);
      BufferedImage var8 = new BufferedImage(var2, var1.createWritableTranslatedChild(0, 0), var2.isAlphaPremultiplied(), (Hashtable)null);
      GraphicsUtil.copyData(var7, var8);
      return var1;
   }

   public static SampleModel makeSampleModel(CachableRed var0, ColorModel var1) {
      SampleModel var2 = var0.getSampleModel();
      return var1.createCompatibleSampleModel(var2.getWidth(), var2.getHeight());
   }

   public static ColorModel makeColorModel(CachableRed var0, SampleModel var1) {
      ColorModel var2 = var0.getColorModel();
      ColorSpace var3 = var2.getColorSpace();
      int var4 = var1.getNumBands();
      int var6 = var1.getDataType();
      byte var5;
      switch (var6) {
         case 0:
            var5 = 8;
            break;
         case 1:
            var5 = 16;
            break;
         case 2:
            var5 = 16;
            break;
         case 3:
            var5 = 32;
            break;
         default:
            throw new IllegalArgumentException("Unsupported DataBuffer type: " + var6);
      }

      boolean var7 = var2.hasAlpha();
      if (var7) {
         if (var4 == var2.getNumComponents() - 1) {
            var7 = false;
         } else if (var4 != var2.getNumComponents()) {
            throw new IllegalArgumentException("Incompatible number of bands in and out");
         }
      } else if (var4 == var2.getNumComponents() + 1) {
         var7 = true;
      } else if (var4 != var2.getNumComponents()) {
         throw new IllegalArgumentException("Incompatible number of bands in and out");
      }

      boolean var8 = var2.isAlphaPremultiplied();
      if (!var7) {
         var8 = false;
      }

      if (!(var1 instanceof ComponentSampleModel)) {
         if (var1 instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel var11 = (SinglePixelPackedSampleModel)var1;
            int[] var12 = var11.getBitMasks();
            if (var4 == 4) {
               return new DirectColorModel(var3, var5, var12[0], var12[1], var12[2], var12[3], var8, var6);
            } else if (var4 == 3) {
               return new DirectColorModel(var3, var5, var12[0], var12[1], var12[2], 0, var8, var6);
            } else {
               throw new IllegalArgumentException("Incompatible number of bands out for ColorModel");
            }
         } else {
            throw new IllegalArgumentException("Unsupported SampleModel Type");
         }
      } else {
         int[] var9 = new int[var4];

         for(int var10 = 0; var10 < var4; ++var10) {
            var9[var10] = var5;
         }

         return new ComponentColorModel(var3, var9, var7, var8, var7 ? 3 : 1, var6);
      }
   }
}
