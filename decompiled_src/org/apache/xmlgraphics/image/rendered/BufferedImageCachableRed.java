package org.apache.xmlgraphics.image.rendered;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;
import org.apache.xmlgraphics.image.GraphicsUtil;

public class BufferedImageCachableRed extends AbstractRed {
   BufferedImage bi;

   public BufferedImageCachableRed(BufferedImage bi) {
      super((CachableRed)((CachableRed)null), new Rectangle(bi.getMinX(), bi.getMinY(), bi.getWidth(), bi.getHeight()), bi.getColorModel(), bi.getSampleModel(), bi.getMinX(), bi.getMinY(), (Map)null);
      this.bi = bi;
   }

   public BufferedImageCachableRed(BufferedImage bi, int xloc, int yloc) {
      super((CachableRed)((CachableRed)null), new Rectangle(xloc, yloc, bi.getWidth(), bi.getHeight()), bi.getColorModel(), bi.getSampleModel(), xloc, yloc, (Map)null);
      this.bi = bi;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
   }

   public BufferedImage getBufferedImage() {
      return this.bi;
   }

   public Object getProperty(String name) {
      return this.bi.getProperty(name);
   }

   public String[] getPropertyNames() {
      return this.bi.getPropertyNames();
   }

   public Raster getTile(int tileX, int tileY) {
      return this.bi.getTile(tileX, tileY);
   }

   public Raster getData() {
      Raster r = this.bi.getData();
      return r.createTranslatedChild(this.getMinX(), this.getMinY());
   }

   public Raster getData(Rectangle rect) {
      Rectangle r = (Rectangle)rect.clone();
      if (!r.intersects(this.getBounds())) {
         return null;
      } else {
         r = r.intersection(this.getBounds());
         r.translate(-this.getMinX(), -this.getMinY());
         Raster ret = this.bi.getData(r);
         return ret.createTranslatedChild(ret.getMinX() + this.getMinX(), ret.getMinY() + this.getMinY());
      }
   }

   public WritableRaster copyData(WritableRaster wr) {
      WritableRaster wr2 = wr.createWritableTranslatedChild(wr.getMinX() - this.getMinX(), wr.getMinY() - this.getMinY());
      GraphicsUtil.copyData((Raster)this.bi.getRaster(), (WritableRaster)wr2);
      return wr;
   }
}
