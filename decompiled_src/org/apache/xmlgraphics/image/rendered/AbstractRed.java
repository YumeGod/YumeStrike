package org.apache.xmlgraphics.image.rendered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.xmlgraphics.image.GraphicsUtil;

public abstract class AbstractRed implements CachableRed {
   protected Rectangle bounds;
   protected Vector srcs;
   protected Map props;
   protected SampleModel sm;
   protected ColorModel cm;
   protected int tileGridXOff;
   protected int tileGridYOff;
   protected int tileWidth;
   protected int tileHeight;
   protected int minTileX;
   protected int minTileY;
   protected int numXTiles;
   protected int numYTiles;

   protected AbstractRed() {
   }

   protected AbstractRed(Rectangle bounds, Map props) {
      this.init((CachableRed)((CachableRed)null), bounds, (ColorModel)null, (SampleModel)null, bounds.x, bounds.y, props);
   }

   protected AbstractRed(CachableRed src, Map props) {
      this.init(src, src.getBounds(), src.getColorModel(), src.getSampleModel(), src.getTileGridXOffset(), src.getTileGridYOffset(), props);
   }

   protected AbstractRed(CachableRed src, Rectangle bounds, Map props) {
      this.init(src, bounds, src.getColorModel(), src.getSampleModel(), src.getTileGridXOffset(), src.getTileGridYOffset(), props);
   }

   protected AbstractRed(CachableRed src, Rectangle bounds, ColorModel cm, SampleModel sm, Map props) {
      this.init(src, bounds, cm, sm, src == null ? 0 : src.getTileGridXOffset(), src == null ? 0 : src.getTileGridYOffset(), props);
   }

   protected AbstractRed(CachableRed src, Rectangle bounds, ColorModel cm, SampleModel sm, int tileGridXOff, int tileGridYOff, Map props) {
      this.init(src, bounds, cm, sm, tileGridXOff, tileGridYOff, props);
   }

   protected void init(CachableRed src, Rectangle bounds, ColorModel cm, SampleModel sm, int tileGridXOff, int tileGridYOff, Map props) {
      this.srcs = new Vector(1);
      if (src != null) {
         this.srcs.add(src);
         if (bounds == null) {
            bounds = src.getBounds();
         }

         if (cm == null) {
            cm = src.getColorModel();
         }

         if (sm == null) {
            sm = src.getSampleModel();
         }
      }

      this.bounds = bounds;
      this.tileGridXOff = tileGridXOff;
      this.tileGridYOff = tileGridYOff;
      this.props = new HashMap();
      if (props != null) {
         this.props.putAll(props);
      }

      if (cm == null) {
         cm = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      }

      this.cm = (ColorModel)cm;
      if (sm == null) {
         sm = ((ColorModel)cm).createCompatibleSampleModel(bounds.width, bounds.height);
      }

      this.sm = sm;
      this.updateTileGridInfo();
   }

   protected AbstractRed(List srcs, Rectangle bounds, Map props) {
      this.init((List)srcs, bounds, (ColorModel)null, (SampleModel)null, bounds.x, bounds.y, props);
   }

   protected AbstractRed(List srcs, Rectangle bounds, ColorModel cm, SampleModel sm, Map props) {
      this.init(srcs, bounds, cm, sm, bounds.x, bounds.y, props);
   }

   protected AbstractRed(List srcs, Rectangle bounds, ColorModel cm, SampleModel sm, int tileGridXOff, int tileGridYOff, Map props) {
      this.init(srcs, bounds, cm, sm, tileGridXOff, tileGridYOff, props);
   }

   protected void init(List srcs, Rectangle bounds, ColorModel cm, SampleModel sm, int tileGridXOff, int tileGridYOff, Map props) {
      this.srcs = new Vector();
      if (srcs != null) {
         this.srcs.addAll(srcs);
      }

      if (srcs.size() != 0) {
         CachableRed src = (CachableRed)srcs.get(0);
         if (bounds == null) {
            bounds = src.getBounds();
         }

         if (cm == null) {
            cm = src.getColorModel();
         }

         if (sm == null) {
            sm = src.getSampleModel();
         }
      }

      this.bounds = bounds;
      this.tileGridXOff = tileGridXOff;
      this.tileGridYOff = tileGridYOff;
      this.props = new HashMap();
      if (props != null) {
         this.props.putAll(props);
      }

      if (cm == null) {
         cm = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      }

      this.cm = (ColorModel)cm;
      if (sm == null) {
         sm = ((ColorModel)cm).createCompatibleSampleModel(bounds.width, bounds.height);
      }

      this.sm = sm;
      this.updateTileGridInfo();
   }

   protected void updateTileGridInfo() {
      this.tileWidth = this.sm.getWidth();
      this.tileHeight = this.sm.getHeight();
      this.minTileX = this.getXTile(this.bounds.x);
      this.minTileY = this.getYTile(this.bounds.y);
      int x1 = this.bounds.x + this.bounds.width - 1;
      int maxTileX = this.getXTile(x1);
      this.numXTiles = maxTileX - this.minTileX + 1;
      int y1 = this.bounds.y + this.bounds.height - 1;
      int maxTileY = this.getYTile(y1);
      this.numYTiles = maxTileY - this.minTileY + 1;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
   }

   public Vector getSources() {
      return this.srcs;
   }

   public ColorModel getColorModel() {
      return this.cm;
   }

   public SampleModel getSampleModel() {
      return this.sm;
   }

   public int getMinX() {
      return this.bounds.x;
   }

   public int getMinY() {
      return this.bounds.y;
   }

   public int getWidth() {
      return this.bounds.width;
   }

   public int getHeight() {
      return this.bounds.height;
   }

   public int getTileWidth() {
      return this.tileWidth;
   }

   public int getTileHeight() {
      return this.tileHeight;
   }

   public int getTileGridXOffset() {
      return this.tileGridXOff;
   }

   public int getTileGridYOffset() {
      return this.tileGridYOff;
   }

   public int getMinTileX() {
      return this.minTileX;
   }

   public int getMinTileY() {
      return this.minTileY;
   }

   public int getNumXTiles() {
      return this.numXTiles;
   }

   public int getNumYTiles() {
      return this.numYTiles;
   }

   public Object getProperty(String name) {
      Object ret = this.props.get(name);
      if (ret != null) {
         return ret;
      } else {
         Iterator i = this.srcs.iterator();

         do {
            if (!i.hasNext()) {
               return null;
            }

            RenderedImage ri = (RenderedImage)i.next();
            ret = ri.getProperty(name);
         } while(ret == null);

         return ret;
      }
   }

   public String[] getPropertyNames() {
      Set keys = this.props.keySet();
      String[] ret = new String[keys.size()];
      keys.toArray(ret);
      Iterator iter = this.srcs.iterator();

      while(iter.hasNext()) {
         RenderedImage ri = (RenderedImage)iter.next();
         String[] srcProps = ri.getPropertyNames();
         if (srcProps.length != 0) {
            String[] tmp = new String[ret.length + srcProps.length];
            System.arraycopy(ret, 0, tmp, 0, ret.length);
            System.arraycopy(srcProps, 0, tmp, ret.length, srcProps.length);
            ret = tmp;
         }
      }

      return ret;
   }

   public Shape getDependencyRegion(int srcIndex, Rectangle outputRgn) {
      if (srcIndex >= 0 && srcIndex <= this.srcs.size()) {
         return !outputRgn.intersects(this.bounds) ? new Rectangle() : outputRgn.intersection(this.bounds);
      } else {
         throw new IndexOutOfBoundsException("Nonexistent source requested.");
      }
   }

   public Shape getDirtyRegion(int srcIndex, Rectangle inputRgn) {
      if (srcIndex != 0) {
         throw new IndexOutOfBoundsException("Nonexistent source requested.");
      } else {
         return !inputRgn.intersects(this.bounds) ? new Rectangle() : inputRgn.intersection(this.bounds);
      }
   }

   public Raster getTile(int tileX, int tileY) {
      WritableRaster wr = this.makeTile(tileX, tileY);
      return this.copyData(wr);
   }

   public Raster getData() {
      return this.getData(this.bounds);
   }

   public Raster getData(Rectangle rect) {
      SampleModel smRet = this.sm.createCompatibleSampleModel(rect.width, rect.height);
      Point pt = new Point(rect.x, rect.y);
      WritableRaster wr = Raster.createWritableRaster(smRet, pt);
      return this.copyData(wr);
   }

   public final int getXTile(int xloc) {
      int tgx = xloc - this.tileGridXOff;
      return tgx >= 0 ? tgx / this.tileWidth : (tgx - this.tileWidth + 1) / this.tileWidth;
   }

   public final int getYTile(int yloc) {
      int tgy = yloc - this.tileGridYOff;
      return tgy >= 0 ? tgy / this.tileHeight : (tgy - this.tileHeight + 1) / this.tileHeight;
   }

   public void copyToRaster(WritableRaster wr) {
      int tx0 = this.getXTile(wr.getMinX());
      int ty0 = this.getYTile(wr.getMinY());
      int tx1 = this.getXTile(wr.getMinX() + wr.getWidth() - 1);
      int ty1 = this.getYTile(wr.getMinY() + wr.getHeight() - 1);
      if (tx0 < this.minTileX) {
         tx0 = this.minTileX;
      }

      if (ty0 < this.minTileY) {
         ty0 = this.minTileY;
      }

      if (tx1 >= this.minTileX + this.numXTiles) {
         tx1 = this.minTileX + this.numXTiles - 1;
      }

      if (ty1 >= this.minTileY + this.numYTiles) {
         ty1 = this.minTileY + this.numYTiles - 1;
      }

      boolean is_INT_PACK = GraphicsUtil.is_INT_PACK_Data(this.getSampleModel(), false);

      for(int y = ty0; y <= ty1; ++y) {
         for(int x = tx0; x <= tx1; ++x) {
            Raster r = this.getTile(x, y);
            if (is_INT_PACK) {
               GraphicsUtil.copyData_INT_PACK(r, wr);
            } else {
               GraphicsUtil.copyData_FALLBACK(r, wr);
            }
         }
      }

   }

   public WritableRaster makeTile(int tileX, int tileY) {
      if (tileX >= this.minTileX && tileX < this.minTileX + this.numXTiles && tileY >= this.minTileY && tileY < this.minTileY + this.numYTiles) {
         Point pt = new Point(this.tileGridXOff + tileX * this.tileWidth, this.tileGridYOff + tileY * this.tileHeight);
         WritableRaster wr = Raster.createWritableRaster(this.sm, pt);
         int x0 = wr.getMinX();
         int y0 = wr.getMinY();
         int x1 = x0 + wr.getWidth() - 1;
         int y1 = y0 + wr.getHeight() - 1;
         if (x0 < this.bounds.x || x1 >= this.bounds.x + this.bounds.width || y0 < this.bounds.y || y1 >= this.bounds.y + this.bounds.height) {
            if (x0 < this.bounds.x) {
               x0 = this.bounds.x;
            }

            if (y0 < this.bounds.y) {
               y0 = this.bounds.y;
            }

            if (x1 >= this.bounds.x + this.bounds.width) {
               x1 = this.bounds.x + this.bounds.width - 1;
            }

            if (y1 >= this.bounds.y + this.bounds.height) {
               y1 = this.bounds.y + this.bounds.height - 1;
            }

            wr = wr.createWritableChild(x0, y0, x1 - x0 + 1, y1 - y0 + 1, x0, y0, (int[])null);
         }

         return wr;
      } else {
         throw new IndexOutOfBoundsException("Requested Tile (" + tileX + ',' + tileY + ") lies outside the bounds of image");
      }
   }

   public static void copyBand(Raster src, int srcBand, WritableRaster dst, int dstBand) {
      Rectangle srcR = new Rectangle(src.getMinX(), src.getMinY(), src.getWidth(), src.getHeight());
      Rectangle dstR = new Rectangle(dst.getMinX(), dst.getMinY(), dst.getWidth(), dst.getHeight());
      Rectangle cpR = srcR.intersection(dstR);
      int[] samples = null;

      for(int y = cpR.y; y < cpR.y + cpR.height; ++y) {
         samples = src.getSamples(cpR.x, y, cpR.width, 1, srcBand, samples);
         dst.setSamples(cpR.x, y, cpR.width, 1, dstBand, samples);
      }

   }
}
