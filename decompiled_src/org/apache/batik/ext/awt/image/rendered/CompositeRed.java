package org.apache.batik.ext.awt.image.rendered;

import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.batik.ext.awt.image.CompositeRule;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.SVGComposite;

public class CompositeRed extends AbstractRed {
   CompositeRule rule;
   CompositeContext[] contexts;

   public CompositeRed(List var1, CompositeRule var2) {
      CachableRed var3 = (CachableRed)((List)var1).get(0);
      ColorModel var4 = fixColorModel(var3);
      this.rule = var2;
      SVGComposite var5 = new SVGComposite(var2);
      this.contexts = new CompositeContext[((List)var1).size()];
      int var6 = 0;
      Iterator var7 = ((List)var1).iterator();
      Rectangle var8 = null;

      while(var7.hasNext()) {
         CachableRed var9 = (CachableRed)var7.next();
         this.contexts[var6++] = var5.createContext(var9.getColorModel(), var4, (RenderingHints)null);
         Rectangle var10 = var9.getBounds();
         if (var8 == null) {
            var8 = var10;
         } else {
            switch (var2.getRule()) {
               case 2:
                  if (var8.intersects(var10)) {
                     var8 = var8.intersection(var10);
                  } else {
                     var8.width = 0;
                     var8.height = 0;
                  }
                  break;
               case 3:
                  var8 = var10;
                  break;
               default:
                  var8.add(var10);
            }
         }
      }

      if (var8 == null) {
         throw new IllegalArgumentException("Composite Operation Must have some source!");
      } else {
         if (var2.getRule() == 6) {
            ArrayList var13 = new ArrayList(((List)var1).size());

            Object var15;
            for(var7 = ((List)var1).iterator(); var7.hasNext(); var13.add(var15)) {
               var15 = (CachableRed)var7.next();
               Rectangle var11 = ((CachableRed)var15).getBounds();
               if (var11.x != var8.x || var11.y != var8.y || var11.width != var8.width || var11.height != var8.height) {
                  var15 = new PadRed((CachableRed)var15, var8, PadMode.ZERO_PAD, (RenderingHints)null);
               }
            }

            var1 = var13;
         }

         SampleModel var14 = fixSampleModel(var3, var4, var8);
         int var16 = AbstractTiledRed.getDefaultTileSize();
         int var17 = var16 * (int)Math.floor((double)(var8.x / var16));
         int var12 = var16 * (int)Math.floor((double)(var8.y / var16));
         this.init((List)var1, var8, var4, var14, var17, var12, (Map)null);
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.genRect(var1);
      return var1;
   }

   public Raster getTile(int var1, int var2) {
      int var3 = this.tileGridXOff + var1 * this.tileWidth;
      int var4 = this.tileGridYOff + var2 * this.tileHeight;
      Point var5 = new Point(var3, var4);
      WritableRaster var6 = Raster.createWritableRaster(this.sm, var5);
      this.genRect(var6);
      return var6;
   }

   public void emptyRect(WritableRaster var1) {
      PadRed.ZeroRecter var2 = PadRed.ZeroRecter.getZeroRecter(var1);
      var2.zeroRect(new Rectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight()));
   }

   public void genRect(WritableRaster var1) {
      Rectangle var2 = var1.getBounds();
      int var3 = 0;
      Iterator var4 = this.srcs.iterator();

      for(boolean var5 = true; var4.hasNext(); ++var3) {
         CachableRed var6 = (CachableRed)var4.next();
         Rectangle var7;
         if (!var5) {
            var7 = var6.getBounds();
            if (var7.intersects(var2)) {
               Rectangle var8 = var7.intersection(var2);
               Raster var9 = var6.getData(var8);
               WritableRaster var10 = var1.createWritableChild(var8.x, var8.y, var8.width, var8.height, var8.x, var8.y, (int[])null);
               this.contexts[var3].compose(var9, var10, var10);
            }
         } else {
            var7 = var6.getBounds();
            if (var2.x < var7.x || var2.y < var7.y || var2.x + var2.width > var7.x + var7.width || var2.y + var2.height > var7.y + var7.height) {
               this.emptyRect(var1);
            }

            var6.copyData(var1);
            if (!var6.getColorModel().isAlphaPremultiplied()) {
               GraphicsUtil.coerceData(var1, var6.getColorModel(), true);
            }

            var5 = false;
         }
      }

   }

   public void genRect_OVER(WritableRaster var1) {
      Rectangle var2 = var1.getBounds();
      ColorModel var3 = this.getColorModel();
      BufferedImage var4 = new BufferedImage(var3, var1.createWritableTranslatedChild(0, 0), var3.isAlphaPremultiplied(), (Hashtable)null);
      Graphics2D var5 = GraphicsUtil.createGraphics(var4);
      var5.translate(-var2.x, -var2.y);
      Iterator var6 = this.srcs.iterator();
      boolean var7 = true;

      while(true) {
         while(var6.hasNext()) {
            CachableRed var8 = (CachableRed)var6.next();
            if (var7) {
               Rectangle var9 = var8.getBounds();
               if (var2.x < var9.x || var2.y < var9.y || var2.x + var2.width > var9.x + var9.width || var2.y + var2.height > var9.y + var9.height) {
                  this.emptyRect(var1);
               }

               var8.copyData(var1);
               GraphicsUtil.coerceData(var1, var8.getColorModel(), var3.isAlphaPremultiplied());
               var7 = false;
            } else {
               GraphicsUtil.drawImage(var5, var8);
            }
         }

         return;
      }
   }

   protected static SampleModel fixSampleModel(CachableRed var0, ColorModel var1, Rectangle var2) {
      int var3 = AbstractTiledRed.getDefaultTileSize();
      int var4 = var3 * (int)Math.floor((double)(var2.x / var3));
      int var5 = var3 * (int)Math.floor((double)(var2.y / var3));
      int var6 = var2.x + var2.width - var4;
      int var7 = var2.y + var2.height - var5;
      SampleModel var8 = var0.getSampleModel();
      int var9 = var8.getWidth();
      if (var9 < var3) {
         var9 = var3;
      }

      if (var9 > var6) {
         var9 = var6;
      }

      int var10 = var8.getHeight();
      if (var10 < var3) {
         var10 = var3;
      }

      if (var10 > var7) {
         var10 = var7;
      }

      if (var9 <= 0 || var10 <= 0) {
         var9 = 1;
         var10 = 1;
      }

      return var1.createCompatibleSampleModel(var9, var10);
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1.hasAlpha()) {
         if (!var1.isAlphaPremultiplied()) {
            var1 = GraphicsUtil.coerceColorModel(var1, true);
         }

         return var1;
      } else {
         int var2 = var0.getSampleModel().getNumBands() + 1;
         if (var2 > 4) {
            throw new IllegalArgumentException("CompositeRed can only handle up to three band images");
         } else {
            int[] var3 = new int[4];

            for(int var4 = 0; var4 < var2 - 1; ++var4) {
               var3[var4] = 16711680 >> 8 * var4;
            }

            var3[3] = 255 << 8 * (var2 - 1);
            ColorSpace var5 = var1.getColorSpace();
            return new DirectColorModel(var5, 8 * var2, var3[0], var3[1], var3[2], var3[3], true, 3);
         }
      }
   }
}
