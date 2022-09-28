package org.apache.batik.ext.awt.image.rendered;

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
import org.apache.batik.ext.awt.image.GraphicsUtil;

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

   protected AbstractRed(Rectangle var1, Map var2) {
      this.init((CachableRed)((CachableRed)null), var1, (ColorModel)null, (SampleModel)null, var1.x, var1.y, var2);
   }

   protected AbstractRed(CachableRed var1, Map var2) {
      this.init(var1, var1.getBounds(), var1.getColorModel(), var1.getSampleModel(), var1.getTileGridXOffset(), var1.getTileGridYOffset(), var2);
   }

   protected AbstractRed(CachableRed var1, Rectangle var2, Map var3) {
      this.init(var1, var2, var1.getColorModel(), var1.getSampleModel(), var1.getTileGridXOffset(), var1.getTileGridYOffset(), var3);
   }

   protected AbstractRed(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, Map var5) {
      this.init(var1, var2, var3, var4, var1 == null ? 0 : var1.getTileGridXOffset(), var1 == null ? 0 : var1.getTileGridYOffset(), var5);
   }

   protected AbstractRed(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      this.init(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void init(CachableRed var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      this.srcs = new Vector(1);
      if (var1 != null) {
         this.srcs.add(var1);
         if (var2 == null) {
            var2 = var1.getBounds();
         }

         if (var3 == null) {
            var3 = var1.getColorModel();
         }

         if (var4 == null) {
            var4 = var1.getSampleModel();
         }
      }

      this.bounds = var2;
      this.tileGridXOff = var5;
      this.tileGridYOff = var6;
      this.props = new HashMap();
      if (var7 != null) {
         this.props.putAll(var7);
      }

      if (var3 == null) {
         var3 = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      }

      this.cm = (ColorModel)var3;
      if (var4 == null) {
         var4 = ((ColorModel)var3).createCompatibleSampleModel(var2.width, var2.height);
      }

      this.sm = var4;
      this.updateTileGridInfo();
   }

   protected AbstractRed(List var1, Rectangle var2, Map var3) {
      this.init((List)var1, var2, (ColorModel)null, (SampleModel)null, var2.x, var2.y, var3);
   }

   protected AbstractRed(List var1, Rectangle var2, ColorModel var3, SampleModel var4, Map var5) {
      this.init(var1, var2, var3, var4, var2.x, var2.y, var5);
   }

   protected AbstractRed(List var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      this.init(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void init(List var1, Rectangle var2, ColorModel var3, SampleModel var4, int var5, int var6, Map var7) {
      this.srcs = new Vector();
      if (var1 != null) {
         this.srcs.addAll(var1);
      }

      if (var1.size() != 0) {
         CachableRed var8 = (CachableRed)var1.get(0);
         if (var2 == null) {
            var2 = var8.getBounds();
         }

         if (var3 == null) {
            var3 = var8.getColorModel();
         }

         if (var4 == null) {
            var4 = var8.getSampleModel();
         }
      }

      this.bounds = var2;
      this.tileGridXOff = var5;
      this.tileGridYOff = var6;
      this.props = new HashMap();
      if (var7 != null) {
         this.props.putAll(var7);
      }

      if (var3 == null) {
         var3 = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{8}, false, false, 1, 0);
      }

      this.cm = (ColorModel)var3;
      if (var4 == null) {
         var4 = ((ColorModel)var3).createCompatibleSampleModel(var2.width, var2.height);
      }

      this.sm = var4;
      this.updateTileGridInfo();
   }

   protected void updateTileGridInfo() {
      this.tileWidth = this.sm.getWidth();
      this.tileHeight = this.sm.getHeight();
      this.minTileX = this.getXTile(this.bounds.x);
      this.minTileY = this.getYTile(this.bounds.y);
      int var1 = this.bounds.x + this.bounds.width - 1;
      int var3 = this.getXTile(var1);
      this.numXTiles = var3 - this.minTileX + 1;
      int var2 = this.bounds.y + this.bounds.height - 1;
      int var4 = this.getYTile(var2);
      this.numYTiles = var4 - this.minTileY + 1;
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

   public Object getProperty(String var1) {
      Object var2 = this.props.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Iterator var3 = this.srcs.iterator();

         do {
            if (!var3.hasNext()) {
               return null;
            }

            RenderedImage var4 = (RenderedImage)var3.next();
            var2 = var4.getProperty(var1);
         } while(var2 == null);

         return var2;
      }
   }

   public String[] getPropertyNames() {
      Set var1 = this.props.keySet();
      String[] var2 = new String[var1.size()];
      var1.toArray(var2);
      Iterator var3 = this.srcs.iterator();

      while(var3.hasNext()) {
         RenderedImage var4 = (RenderedImage)var3.next();
         String[] var5 = var4.getPropertyNames();
         if (var5.length != 0) {
            String[] var6 = new String[var2.length + var5.length];
            System.arraycopy(var2, 0, var6, 0, var2.length);
            System.arraycopy(var5, 0, var6, var2.length, var5.length);
            var2 = var6;
         }
      }

      return var2;
   }

   public Shape getDependencyRegion(int var1, Rectangle var2) {
      if (var1 >= 0 && var1 <= this.srcs.size()) {
         return !var2.intersects(this.bounds) ? new Rectangle() : var2.intersection(this.bounds);
      } else {
         throw new IndexOutOfBoundsException("Nonexistant source requested.");
      }
   }

   public Shape getDirtyRegion(int var1, Rectangle var2) {
      if (var1 != 0) {
         throw new IndexOutOfBoundsException("Nonexistant source requested.");
      } else {
         return !var2.intersects(this.bounds) ? new Rectangle() : var2.intersection(this.bounds);
      }
   }

   public Raster getTile(int var1, int var2) {
      WritableRaster var3 = this.makeTile(var1, var2);
      return this.copyData(var3);
   }

   public Raster getData() {
      return this.getData(this.bounds);
   }

   public Raster getData(Rectangle var1) {
      SampleModel var2 = this.sm.createCompatibleSampleModel(var1.width, var1.height);
      Point var3 = new Point(var1.x, var1.y);
      WritableRaster var4 = Raster.createWritableRaster(var2, var3);
      return this.copyData(var4);
   }

   public final int getXTile(int var1) {
      int var2 = var1 - this.tileGridXOff;
      return var2 >= 0 ? var2 / this.tileWidth : (var2 - this.tileWidth + 1) / this.tileWidth;
   }

   public final int getYTile(int var1) {
      int var2 = var1 - this.tileGridYOff;
      return var2 >= 0 ? var2 / this.tileHeight : (var2 - this.tileHeight + 1) / this.tileHeight;
   }

   public void copyToRaster(WritableRaster var1) {
      int var2 = this.getXTile(var1.getMinX());
      int var3 = this.getYTile(var1.getMinY());
      int var4 = this.getXTile(var1.getMinX() + var1.getWidth() - 1);
      int var5 = this.getYTile(var1.getMinY() + var1.getHeight() - 1);
      if (var2 < this.minTileX) {
         var2 = this.minTileX;
      }

      if (var3 < this.minTileY) {
         var3 = this.minTileY;
      }

      if (var4 >= this.minTileX + this.numXTiles) {
         var4 = this.minTileX + this.numXTiles - 1;
      }

      if (var5 >= this.minTileY + this.numYTiles) {
         var5 = this.minTileY + this.numYTiles - 1;
      }

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

   }

   public WritableRaster makeTile(int var1, int var2) {
      if (var1 >= this.minTileX && var1 < this.minTileX + this.numXTiles && var2 >= this.minTileY && var2 < this.minTileY + this.numYTiles) {
         Point var3 = new Point(this.tileGridXOff + var1 * this.tileWidth, this.tileGridYOff + var2 * this.tileHeight);
         WritableRaster var4 = Raster.createWritableRaster(this.sm, var3);
         int var5 = var4.getMinX();
         int var6 = var4.getMinY();
         int var7 = var5 + var4.getWidth() - 1;
         int var8 = var6 + var4.getHeight() - 1;
         if (var5 < this.bounds.x || var7 >= this.bounds.x + this.bounds.width || var6 < this.bounds.y || var8 >= this.bounds.y + this.bounds.height) {
            if (var5 < this.bounds.x) {
               var5 = this.bounds.x;
            }

            if (var6 < this.bounds.y) {
               var6 = this.bounds.y;
            }

            if (var7 >= this.bounds.x + this.bounds.width) {
               var7 = this.bounds.x + this.bounds.width - 1;
            }

            if (var8 >= this.bounds.y + this.bounds.height) {
               var8 = this.bounds.y + this.bounds.height - 1;
            }

            var4 = var4.createWritableChild(var5, var6, var7 - var5 + 1, var8 - var6 + 1, var5, var6, (int[])null);
         }

         return var4;
      } else {
         throw new IndexOutOfBoundsException("Requested Tile (" + var1 + ',' + var2 + ") lies outside the bounds of image");
      }
   }

   public static void copyBand(Raster var0, int var1, WritableRaster var2, int var3) {
      Rectangle var4 = new Rectangle(var0.getMinX(), var0.getMinY(), var0.getWidth(), var0.getHeight());
      Rectangle var5 = new Rectangle(var2.getMinX(), var2.getMinY(), var2.getWidth(), var2.getHeight());
      Rectangle var6 = var4.intersection(var5);
      int[] var7 = null;

      for(int var8 = var6.y; var8 < var6.y + var6.height; ++var8) {
         var7 = var0.getSamples(var6.x, var8, var6.width, 1, var1, var7);
         var2.setSamples(var6.x, var8, var6.width, 1, var3, var7);
      }

   }
}
