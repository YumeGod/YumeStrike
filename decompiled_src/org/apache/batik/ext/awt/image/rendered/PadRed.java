package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;

public class PadRed extends AbstractRed {
   static final boolean DEBUG = false;
   PadMode padMode;
   RenderingHints hints;

   public PadRed(CachableRed var1, Rectangle var2, PadMode var3, RenderingHints var4) {
      super((CachableRed)var1, var2, var1.getColorModel(), fixSampleModel(var1, var2), var2.x, var2.y, (Map)null);
      this.padMode = var3;
      this.hints = var4;
   }

   public WritableRaster copyData(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      Rectangle var3 = var2.getBounds();
      Rectangle var4 = var1.getBounds();
      if (var4.intersects(var3)) {
         Rectangle var5 = var4.intersection(var3);
         WritableRaster var6 = var1.createWritableChild(var5.x, var5.y, var5.width, var5.height, var5.x, var5.y, (int[])null);
         var2.copyData(var6);
      }

      if (this.padMode == PadMode.ZERO_PAD) {
         this.handleZero(var1);
      } else if (this.padMode == PadMode.REPLICATE) {
         this.handleReplicate(var1);
      } else if (this.padMode == PadMode.WRAP) {
         this.handleWrap(var1);
      }

      return var1;
   }

   protected void handleZero(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      Rectangle var3 = var2.getBounds();
      Rectangle var4 = var1.getBounds();
      ZeroRecter var5 = PadRed.ZeroRecter.getZeroRecter(var1);
      Rectangle var6 = new Rectangle(var4.x, var4.y, var4.width, var4.height);
      Rectangle var7 = new Rectangle(var4.x, var4.y, var4.width, var4.height);
      int var8;
      if (var6.x < var3.x) {
         var8 = var3.x - var6.x;
         if (var8 > var6.width) {
            var8 = var6.width;
         }

         var7.width = var8;
         var5.zeroRect(var7);
         var6.x += var8;
         var6.width -= var8;
      }

      if (var6.y < var3.y) {
         var8 = var3.y - var6.y;
         if (var8 > var6.height) {
            var8 = var6.height;
         }

         var7.x = var6.x;
         var7.y = var6.y;
         var7.width = var6.width;
         var7.height = var8;
         var5.zeroRect(var7);
         var6.y += var8;
         var6.height -= var8;
      }

      int var9;
      if (var6.y + var6.height > var3.y + var3.height) {
         var8 = var6.y + var6.height - (var3.y + var3.height);
         if (var8 > var6.height) {
            var8 = var6.height;
         }

         var9 = var6.y + var6.height - var8;
         var7.x = var6.x;
         var7.y = var9;
         var7.width = var6.width;
         var7.height = var8;
         var5.zeroRect(var7);
         var6.height -= var8;
      }

      if (var6.x + var6.width > var3.x + var3.width) {
         var8 = var6.x + var6.width - (var3.x + var3.width);
         if (var8 > var6.width) {
            var8 = var6.width;
         }

         var9 = var6.x + var6.width - var8;
         var7.x = var9;
         var7.y = var6.y;
         var7.width = var8;
         var7.height = var6.height;
         var5.zeroRect(var7);
         var6.width -= var8;
      }

   }

   protected void handleReplicate(WritableRaster var1) {
      CachableRed var2 = (CachableRed)this.getSources().get(0);
      Rectangle var3 = var2.getBounds();
      Rectangle var4 = var1.getBounds();
      int var5 = var4.x;
      int var6 = var4.y;
      int var7 = var4.width;
      int var8 = var4.height;
      int var10 = var3.x > var5 ? var3.x : var5;
      int var11 = var3.x + var3.width - 1 < var5 + var7 - 1 ? var3.x + var3.width - 1 : var5 + var7 - 1;
      int var12 = var3.y > var6 ? var3.y : var6;
      int var13 = var3.y + var3.height - 1 < var6 + var8 - 1 ? var3.y + var3.height - 1 : var6 + var8 - 1;
      int var14 = var10;
      int var15 = var11 - var10 + 1;
      int var16 = var12;
      int var17 = var13 - var12 + 1;
      if (var15 < 0) {
         var14 = 0;
         var15 = 0;
      }

      if (var17 < 0) {
         var16 = 0;
         var17 = 0;
      }

      Rectangle var9 = new Rectangle(var14, var16, var15, var17);
      if (var6 < var3.y) {
         var10 = var9.width;
         var11 = var9.x;
         var12 = var9.x;
         if (var5 + var7 - 1 <= var3.x) {
            var10 = 1;
            var11 = var3.x;
            var12 = var5 + var7 - 1;
         } else if (var5 >= var3.x + var3.width) {
            var10 = 1;
            var11 = var3.x + var3.width - 1;
            var12 = var5;
         }

         WritableRaster var19 = var1.createWritableChild(var12, var6, var10, 1, var11, var3.y, (int[])null);
         var2.copyData(var19);
         var13 = var6 + 1;
         var15 = var3.y;
         if (var6 + var8 < var15) {
            var15 = var6 + var8;
         }

         if (var13 < var15) {
            for(int[] var21 = var1.getPixels(var12, var13 - 1, var10, 1, (int[])null); var13 < var3.y; ++var13) {
               var1.setPixels(var12, var13, var10, 1, var21);
            }
         }
      }

      if (var6 + var8 > var3.y + var3.height) {
         var10 = var9.width;
         var11 = var9.x;
         var12 = var3.y + var3.height - 1;
         var13 = var9.x;
         var14 = var3.y + var3.height;
         if (var14 < var6) {
            var14 = var6;
         }

         if (var5 + var7 <= var3.x) {
            var10 = 1;
            var11 = var3.x;
            var13 = var5 + var7 - 1;
         } else if (var5 >= var3.x + var3.width) {
            var10 = 1;
            var11 = var3.x + var3.width - 1;
            var13 = var5;
         }

         WritableRaster var22 = var1.createWritableChild(var13, var14, var10, 1, var11, var12, (int[])null);
         var2.copyData(var22);
         ++var14;
         var16 = var6 + var8;
         if (var14 < var16) {
            for(int[] var23 = var1.getPixels(var13, var14 - 1, var10, 1, (int[])null); var14 < var16; ++var14) {
               var1.setPixels(var13, var14, var10, 1, var23);
            }
         }
      }

      if (var5 < var3.x) {
         var10 = var3.x;
         if (var5 + var7 <= var3.x) {
            var10 = var5 + var7 - 1;
         }

         var11 = var5;

         for(int[] var18 = var1.getPixels(var10, var6, 1, var8, (int[])null); var11 < var10; ++var11) {
            var1.setPixels(var11, var6, 1, var8, var18);
         }
      }

      if (var5 + var7 > var3.x + var3.width) {
         var10 = var3.x + var3.width - 1;
         if (var5 >= var3.x + var3.width) {
            var10 = var5;
         }

         var11 = var10 + 1;
         var12 = var5 + var7 - 1;

         for(int[] var20 = var1.getPixels(var10, var6, 1, var8, (int[])null); var11 < var12; ++var11) {
            var1.setPixels(var11, var6, 1, var8, var20);
         }
      }

   }

   protected void handleWrap(WritableRaster var1) {
      this.handleZero(var1);
   }

   protected static SampleModel fixSampleModel(CachableRed var0, Rectangle var1) {
      int var2 = AbstractTiledRed.getDefaultTileSize();
      SampleModel var3 = var0.getSampleModel();
      int var4 = var3.getWidth();
      if (var4 < var2) {
         var4 = var2;
      }

      if (var4 > var1.width) {
         var4 = var1.width;
      }

      int var5 = var3.getHeight();
      if (var5 < var2) {
         var5 = var2;
      }

      if (var5 > var1.height) {
         var5 = var1.height;
      }

      return var3.createCompatibleSampleModel(var4, var5);
   }

   protected static class ZeroRecter_INT_PACK extends ZeroRecter {
      final int base;
      final int scanStride;
      final int[] pixels;
      final int[] zeros;
      final int x0;
      final int y0;

      public ZeroRecter_INT_PACK(WritableRaster var1) {
         super(var1);
         SinglePixelPackedSampleModel var2 = (SinglePixelPackedSampleModel)var1.getSampleModel();
         this.scanStride = var2.getScanlineStride();
         DataBufferInt var3 = (DataBufferInt)var1.getDataBuffer();
         this.x0 = var1.getMinY();
         this.y0 = var1.getMinX();
         this.base = var3.getOffset() + var2.getOffset(this.x0 - var1.getSampleModelTranslateX(), this.y0 - var1.getSampleModelTranslateY());
         this.pixels = var3.getBankData()[0];
         if (var1.getWidth() > 10) {
            this.zeros = new int[var1.getWidth()];
         } else {
            this.zeros = null;
         }

      }

      public void zeroRect(Rectangle var1) {
         int var2 = this.base + (var1.x - this.x0) + (var1.y - this.y0) * this.scanStride;
         int var3;
         int var4;
         if (var1.width > 10) {
            for(var3 = 0; var3 < var1.height; ++var3) {
               var4 = var2 + var3 * this.scanStride;
               System.arraycopy(this.zeros, 0, this.pixels, var4, var1.width);
            }
         } else {
            var3 = var2;
            var4 = var2 + var1.width;
            int var5 = this.scanStride - var1.width;

            for(int var6 = 0; var6 < var1.height; ++var6) {
               while(var3 < var4) {
                  this.pixels[var3++] = 0;
               }

               var3 += var5;
               var4 += this.scanStride;
            }
         }

      }
   }

   protected static class ZeroRecter {
      WritableRaster wr;
      int bands;
      static int[] zeros = null;

      public ZeroRecter(WritableRaster var1) {
         this.wr = var1;
         this.bands = var1.getSampleModel().getNumBands();
      }

      public void zeroRect(Rectangle var1) {
         synchronized(this) {
            if (zeros == null || zeros.length < var1.width * this.bands) {
               zeros = new int[var1.width * this.bands];
            }
         }

         for(int var2 = 0; var2 < var1.height; ++var2) {
            this.wr.setPixels(var1.x, var1.y + var2, var1.width, 1, zeros);
         }

      }

      public static ZeroRecter getZeroRecter(WritableRaster var0) {
         return (ZeroRecter)(GraphicsUtil.is_INT_PACK_Data(var0.getSampleModel(), false) ? new ZeroRecter_INT_PACK(var0) : new ZeroRecter(var0));
      }

      public static void zeroRect(WritableRaster var0) {
         ZeroRecter var1 = getZeroRecter(var0);
         var1.zeroRect(var0.getBounds());
      }
   }
}
