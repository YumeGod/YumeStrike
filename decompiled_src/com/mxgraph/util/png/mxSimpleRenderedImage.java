package com.mxgraph.util.png;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class mxSimpleRenderedImage implements RenderedImage {
   protected int minX;
   protected int minY;
   protected int width;
   protected int height;
   protected int tileWidth;
   protected int tileHeight;
   protected int tileGridXOffset = 0;
   protected int tileGridYOffset = 0;
   protected SampleModel sampleModel = null;
   protected ColorModel colorModel = null;
   protected List sources = new ArrayList();
   protected Map properties = new HashMap();

   public int getMinX() {
      return this.minX;
   }

   public final int getMaxX() {
      return this.getMinX() + this.getWidth();
   }

   public int getMinY() {
      return this.minY;
   }

   public final int getMaxY() {
      return this.getMinY() + this.getHeight();
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
   }

   public int getTileWidth() {
      return this.tileWidth;
   }

   public int getTileHeight() {
      return this.tileHeight;
   }

   public int getTileGridXOffset() {
      return this.tileGridXOffset;
   }

   public int getTileGridYOffset() {
      return this.tileGridYOffset;
   }

   public int getMinTileX() {
      return this.XToTileX(this.getMinX());
   }

   public int getMaxTileX() {
      return this.XToTileX(this.getMaxX() - 1);
   }

   public int getNumXTiles() {
      return this.getMaxTileX() - this.getMinTileX() + 1;
   }

   public int getMinTileY() {
      return this.YToTileY(this.getMinY());
   }

   public int getMaxTileY() {
      return this.YToTileY(this.getMaxY() - 1);
   }

   public int getNumYTiles() {
      return this.getMaxTileY() - this.getMinTileY() + 1;
   }

   public SampleModel getSampleModel() {
      return this.sampleModel;
   }

   public ColorModel getColorModel() {
      return this.colorModel;
   }

   public Object getProperty(String var1) {
      var1 = var1.toLowerCase();
      return this.properties.get(var1);
   }

   public String[] getPropertyNames() {
      String[] var1 = new String[this.properties.size()];
      this.properties.keySet().toArray(var1);
      return var1;
   }

   public String[] getPropertyNames(String var1) {
      String[] var2 = this.getPropertyNames();
      if (var2 == null) {
         return null;
      } else {
         var1 = var1.toLowerCase();
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].startsWith(var1)) {
               var3.add(var2[var4]);
            }
         }

         if (var3.size() == 0) {
            return null;
         } else {
            String[] var5 = new String[var3.size()];
            var3.toArray(var5);
            return var5;
         }
      }
   }

   public static int XToTileX(int var0, int var1, int var2) {
      var0 -= var1;
      if (var0 < 0) {
         var0 += 1 - var2;
      }

      return var0 / var2;
   }

   public static int YToTileY(int var0, int var1, int var2) {
      var0 -= var1;
      if (var0 < 0) {
         var0 += 1 - var2;
      }

      return var0 / var2;
   }

   public int XToTileX(int var1) {
      return XToTileX(var1, this.getTileGridXOffset(), this.getTileWidth());
   }

   public int YToTileY(int var1) {
      return YToTileY(var1, this.getTileGridYOffset(), this.getTileHeight());
   }

   public static int tileXToX(int var0, int var1, int var2) {
      return var0 * var2 + var1;
   }

   public static int tileYToY(int var0, int var1, int var2) {
      return var0 * var2 + var1;
   }

   public int tileXToX(int var1) {
      return var1 * this.tileWidth + this.tileGridXOffset;
   }

   public int tileYToY(int var1) {
      return var1 * this.tileHeight + this.tileGridYOffset;
   }

   public Vector getSources() {
      return null;
   }

   public Raster getData() {
      Rectangle var1 = new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
      return this.getData(var1);
   }

   public Raster getData(Rectangle var1) {
      int var2 = this.XToTileX(var1.x);
      int var3 = this.YToTileY(var1.y);
      int var4 = this.XToTileX(var1.x + var1.width - 1);
      int var5 = this.YToTileY(var1.y + var1.height - 1);
      Raster var6;
      if (var2 == var4 && var3 == var5) {
         var6 = this.getTile(var2, var3);
         return var6.createChild(var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, (int[])null);
      } else {
         SampleModel var7 = this.sampleModel.createCompatibleSampleModel(var1.width, var1.height);
         WritableRaster var8 = Raster.createWritableRaster(var7, var1.getLocation());

         for(int var9 = var3; var9 <= var5; ++var9) {
            for(int var10 = var2; var10 <= var4; ++var10) {
               var6 = this.getTile(var10, var9);
               Rectangle var11 = var1.intersection(var6.getBounds());
               Raster var12 = var6.createChild(var11.x, var11.y, var11.width, var11.height, var11.x, var11.y, (int[])null);
               var8.setDataElements(0, 0, var12);
            }
         }

         return var8;
      }
   }

   public WritableRaster copyData(WritableRaster var1) {
      Rectangle var2;
      if (var1 == null) {
         var2 = this.getBounds();
         Point var4 = new Point(this.minX, this.minY);
         SampleModel var5 = this.sampleModel.createCompatibleSampleModel(this.width, this.height);
         var1 = Raster.createWritableRaster(var5, var4);
      } else {
         var2 = var1.getBounds();
      }

      int var12 = this.XToTileX(var2.x);
      int var13 = this.YToTileY(var2.y);
      int var6 = this.XToTileX(var2.x + var2.width - 1);
      int var7 = this.YToTileY(var2.y + var2.height - 1);

      for(int var8 = var13; var8 <= var7; ++var8) {
         for(int var9 = var12; var9 <= var6; ++var9) {
            Raster var3 = this.getTile(var9, var8);
            Rectangle var10 = var2.intersection(var3.getBounds());
            Raster var11 = var3.createChild(var10.x, var10.y, var10.width, var10.height, var10.x, var10.y, (int[])null);
            var1.setDataElements(0, 0, var11);
         }
      }

      return var1;
   }
}
