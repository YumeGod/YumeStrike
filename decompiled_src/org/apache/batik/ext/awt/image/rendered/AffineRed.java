package org.apache.batik.ext.awt.image.rendered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class AffineRed extends AbstractRed {
   RenderingHints hints;
   AffineTransform src2me;
   AffineTransform me2src;

   public AffineTransform getTransform() {
      return (AffineTransform)this.src2me.clone();
   }

   public CachableRed getSource() {
      return (CachableRed)this.getSources().get(0);
   }

   public AffineRed(CachableRed var1, AffineTransform var2, RenderingHints var3) {
      this.src2me = var2;
      this.hints = var3;

      try {
         this.me2src = var2.createInverse();
      } catch (NoninvertibleTransformException var9) {
         this.me2src = null;
      }

      Rectangle var4 = var1.getBounds();
      Rectangle var5 = var2.createTransformedShape(var4).getBounds();
      ColorModel var6 = fixColorModel(var1);
      SampleModel var7 = this.fixSampleModel(var1, var6, var5);
      Point2D.Float var8 = new Point2D.Float((float)var1.getTileGridXOffset(), (float)var1.getTileGridYOffset());
      Point2D var10 = var2.transform(var8, (Point2D)null);
      this.init(var1, var5, var6, var7, (int)var10.getX(), (int)var10.getY(), (Map)null);
   }

   public WritableRaster copyData(WritableRaster var1) {
      PadRed.ZeroRecter var2 = PadRed.ZeroRecter.getZeroRecter(var1);
      var2.zeroRect(new Rectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight()));
      this.genRect(var1);
      return var1;
   }

   public Raster getTile(int var1, int var2) {
      if (this.me2src == null) {
         return null;
      } else {
         int var3 = this.tileGridXOff + var1 * this.tileWidth;
         int var4 = this.tileGridYOff + var2 * this.tileHeight;
         Point var5 = new Point(var3, var4);
         WritableRaster var6 = Raster.createWritableRaster(this.sm, var5);
         this.genRect(var6);
         return var6;
      }
   }

   public void genRect(WritableRaster var1) {
      if (this.me2src != null) {
         Rectangle var2 = this.me2src.createTransformedShape(var1.getBounds()).getBounds();
         var2.setBounds(var2.x - 1, var2.y - 1, var2.width + 2, var2.height + 2);
         CachableRed var3 = (CachableRed)this.getSources().get(0);
         if (var2.intersects(var3.getBounds())) {
            Raster var4 = var3.getData(var2.intersection(var3.getBounds()));
            if (var4 != null) {
               AffineTransform var5 = (AffineTransform)this.src2me.clone();
               var5.concatenate(AffineTransform.getTranslateInstance((double)var4.getMinX(), (double)var4.getMinY()));
               Point2D.Float var6 = new Point2D.Float((float)var1.getMinX(), (float)var1.getMinY());
               Point2D var14 = this.me2src.transform(var6, (Point2D)null);
               Point2D.Double var7 = new Point2D.Double(var14.getX() - (double)var4.getMinX(), var14.getY() - (double)var4.getMinY());
               Point2D var15 = var5.transform(var7, (Point2D)null);
               var5.preConcatenate(AffineTransform.getTranslateInstance(-var15.getX(), -var15.getY()));
               AffineTransformOp var8 = new AffineTransformOp(var5, this.hints);
               ColorModel var11 = var3.getColorModel();
               ColorModel var12 = this.getColorModel();
               WritableRaster var13 = (WritableRaster)var4;
               var11 = GraphicsUtil.coerceData(var13, var11, true);
               BufferedImage var9 = new BufferedImage(var11, var13.createWritableTranslatedChild(0, 0), var11.isAlphaPremultiplied(), (Hashtable)null);
               BufferedImage var10 = new BufferedImage(var12, var1.createWritableTranslatedChild(0, 0), var12.isAlphaPremultiplied(), (Hashtable)null);
               var8.filter(var9, var10);
            }
         }
      }
   }

   protected static ColorModel fixColorModel(CachableRed var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1.hasAlpha()) {
         if (!var1.isAlphaPremultiplied()) {
            var1 = GraphicsUtil.coerceColorModel(var1, true);
         }

         return var1;
      } else {
         ColorSpace var2 = var1.getColorSpace();
         int var3 = var0.getSampleModel().getNumBands() + 1;
         int[] var4;
         int var5;
         if (var3 == 4) {
            var4 = new int[4];

            for(var5 = 0; var5 < var3 - 1; ++var5) {
               var4[var5] = 16711680 >> 8 * var5;
            }

            var4[3] = 255 << 8 * (var3 - 1);
            return new DirectColorModel(var2, 8 * var3, var4[0], var4[1], var4[2], var4[3], true, 3);
         } else {
            var4 = new int[var3];

            for(var5 = 0; var5 < var3; ++var5) {
               var4[var5] = 8;
            }

            return new ComponentColorModel(var2, var4, true, true, 3, 3);
         }
      }
   }

   protected SampleModel fixSampleModel(CachableRed var1, ColorModel var2, Rectangle var3) {
      SampleModel var4 = var1.getSampleModel();
      int var5 = AbstractTiledRed.getDefaultTileSize();
      int var6 = var4.getWidth();
      if (var6 < var5) {
         var6 = var5;
      }

      if (var6 > var3.width) {
         var6 = var3.width;
      }

      int var7 = var4.getHeight();
      if (var7 < var5) {
         var7 = var5;
      }

      if (var7 > var3.height) {
         var7 = var3.height;
      }

      if (var6 <= 0 || var7 <= 0) {
         var6 = 1;
         var7 = 1;
      }

      return var2.createCompatibleSampleModel(var6, var7);
   }
}
