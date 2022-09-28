package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.batik.ext.awt.image.GraphicsUtil;

public class BufferedImageCachableRed extends AbstractRed {
   BufferedImage bi;

   public BufferedImageCachableRed(BufferedImage var1) {
      super((CachableRed)((CachableRed)null), new Rectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight()), var1.getColorModel(), var1.getSampleModel(), var1.getMinX(), var1.getMinY(), (Map)null);
      this.bi = var1;
   }

   public BufferedImageCachableRed(BufferedImage var1, int var2, int var3) {
      super((CachableRed)((CachableRed)null), new Rectangle(var2, var3, var1.getWidth(), var1.getHeight()), var1.getColorModel(), var1.getSampleModel(), var2, var3, (Map)null);
      this.bi = var1;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
   }

   public BufferedImage getBufferedImage() {
      return this.bi;
   }

   public Object getProperty(String var1) {
      return this.bi.getProperty(var1);
   }

   public String[] getPropertyNames() {
      return this.bi.getPropertyNames();
   }

   public Raster getTile(int var1, int var2) {
      return this.bi.getTile(var1, var2);
   }

   public Raster getData() {
      Raster var1 = this.bi.getData();
      return var1.createTranslatedChild(this.getMinX(), this.getMinY());
   }

   public Raster getData(Rectangle var1) {
      Rectangle var2 = (Rectangle)var1.clone();
      if (!var2.intersects(this.getBounds())) {
         return null;
      } else {
         var2 = var2.intersection(this.getBounds());
         var2.translate(-this.getMinX(), -this.getMinY());
         Raster var3 = this.bi.getData(var2);
         return var3.createTranslatedChild(var3.getMinX() + this.getMinX(), var3.getMinY() + this.getMinY());
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      WritableRaster var2 = var1.createWritableTranslatedChild(var1.getMinX() - this.getMinX(), var1.getMinY() - this.getMinY());
      GraphicsUtil.copyData((Raster)this.bi.getRaster(), (WritableRaster)var2);
      return var1;
   }
}
