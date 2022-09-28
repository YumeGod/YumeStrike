package org.apache.batik.ext.awt.image.rendered;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class FloodRed extends AbstractRed {
   private WritableRaster raster;

   public FloodRed(Rectangle var1) {
      this(var1, new Color(0, 0, 0, 0));
   }

   public FloodRed(Rectangle var1, Paint var2) {
      ColorModel var3 = GraphicsUtil.sRGB_Unpre;
      int var4 = AbstractTiledRed.getDefaultTileSize();
      int var5 = var1.width;
      if (var5 > var4) {
         var5 = var4;
      }

      int var6 = var1.height;
      if (var6 > var4) {
         var6 = var4;
      }

      SampleModel var7 = var3.createCompatibleSampleModel(var5, var6);
      this.init((CachableRed)null, var1, var3, var7, 0, 0, (Map)null);
      this.raster = Raster.createWritableRaster(var7, new Point(0, 0));
      BufferedImage var8 = new BufferedImage(var3, this.raster, var3.isAlphaPremultiplied(), (Hashtable)null);
      Graphics2D var9 = GraphicsUtil.createGraphics(var8);
      var9.setPaint(var2);
      var9.fillRect(0, 0, var1.width, var1.height);
      var9.dispose();
   }

   public Raster getTile(int var1, int var2) {
      int var3 = this.tileGridXOff + var1 * this.tileWidth;
      int var4 = this.tileGridYOff + var2 * this.tileHeight;
      return this.raster.createTranslatedChild(var3, var4);
   }

   public WritableRaster copyData(WritableRaster var1) {
      int var2 = this.getXTile(var1.getMinX());
      int var3 = this.getYTile(var1.getMinY());
      int var4 = this.getXTile(var1.getMinX() + var1.getWidth() - 1);
      int var5 = this.getYTile(var1.getMinY() + var1.getHeight() - 1);
      boolean var6 = GraphicsUtil.is_INT_PACK_Data(this.getSampleModel(), false);

      for(int var7 = var3; var7 <= var5; ++var7) {
         for(int var8 = var2; var8 <= var4; ++var8) {
            Raster var9 = this.getTile(var8, var7);
            if (var6) {
               GraphicsUtil.copyData_INT_PACK(var9, var1);
            } else {
               GraphicsUtil.copyData_FALLBACK(var9, var1);
            }
         }
      }

      return var1;
   }
}
