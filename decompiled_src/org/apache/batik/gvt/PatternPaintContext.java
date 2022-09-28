package org.apache.batik.gvt;

import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.TileRable8Bit;
import org.apache.batik.ext.awt.image.rendered.TileCacheRed;

public class PatternPaintContext implements PaintContext {
   private ColorModel rasterCM;
   private WritableRaster raster;
   private RenderedImage tiled;
   protected AffineTransform usr2dev;
   private static Rectangle EVERYTHING = new Rectangle(-536870912, -536870912, 1073741823, 1073741823);

   public AffineTransform getUsr2Dev() {
      return this.usr2dev;
   }

   public PatternPaintContext(ColorModel var1, AffineTransform var2, RenderingHints var3, Filter var4, Rectangle2D var5, boolean var6) {
      if (var2 == null) {
         throw new IllegalArgumentException();
      } else {
         if (var3 == null) {
            var3 = new RenderingHints((Map)null);
         }

         if (var4 == null) {
            throw new IllegalArgumentException();
         } else {
            this.usr2dev = var2;
            TileRable8Bit var7 = new TileRable8Bit(var4, EVERYTHING, var5, var6);
            ColorSpace var8 = var1.getColorSpace();
            if (var8 == ColorSpace.getInstance(1000)) {
               var7.setColorSpaceLinear(false);
            } else if (var8 == ColorSpace.getInstance(1004)) {
               var7.setColorSpaceLinear(true);
            }

            RenderContext var9 = new RenderContext(var2, EVERYTHING, var3);
            this.tiled = var7.createRendering(var9);
            if (this.tiled == null) {
               this.rasterCM = ColorModel.getRGBdefault();
               WritableRaster var11 = this.rasterCM.createCompatibleWritableRaster(32, 32);
               this.tiled = GraphicsUtil.wrap(new BufferedImage(this.rasterCM, var11, false, (Hashtable)null));
            } else {
               Rectangle var10 = var2.createTransformedShape(var5).getBounds();
               if (var10.getWidth() > 128.0 || var10.getHeight() > 128.0) {
                  this.tiled = new TileCacheRed(GraphicsUtil.wrap(this.tiled), 256, 64);
               }

               this.rasterCM = this.tiled.getColorModel();
               if (this.rasterCM.hasAlpha()) {
                  if (var1.hasAlpha()) {
                     this.rasterCM = GraphicsUtil.coerceColorModel(this.rasterCM, var1.isAlphaPremultiplied());
                  } else {
                     this.rasterCM = GraphicsUtil.coerceColorModel(this.rasterCM, false);
                  }
               }

            }
         }
      }
   }

   public void dispose() {
      this.raster = null;
   }

   public ColorModel getColorModel() {
      return this.rasterCM;
   }

   public Raster getRaster(int var1, int var2, int var3, int var4) {
      if (this.raster == null || this.raster.getWidth() < var3 || this.raster.getHeight() < var4) {
         this.raster = this.rasterCM.createCompatibleWritableRaster(var3, var4);
      }

      WritableRaster var5 = this.raster.createWritableChild(0, 0, var3, var4, var1, var2, (int[])null);
      this.tiled.copyData(var5);
      GraphicsUtil.coerceData(var5, this.tiled.getColorModel(), this.rasterCM.isAlphaPremultiplied());
      return (Raster)(this.raster.getWidth() == var3 && this.raster.getHeight() == var4 ? this.raster : var5.createTranslatedChild(0, 0));
   }
}
