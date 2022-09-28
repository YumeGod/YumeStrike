package org.apache.batik.ext.awt.image.rendered;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Vector;

public class RenderedImageCachableRed implements CachableRed {
   private RenderedImage src;
   private Vector srcs = new Vector(0);

   public static CachableRed wrap(RenderedImage var0) {
      if (var0 instanceof CachableRed) {
         return (CachableRed)var0;
      } else {
         return (CachableRed)(var0 instanceof BufferedImage ? new BufferedImageCachableRed((BufferedImage)var0) : new RenderedImageCachableRed(var0));
      }
   }

   public RenderedImageCachableRed(RenderedImage var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.src = var1;
      }
   }

   public Vector getSources() {
      return this.srcs;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
   }

   public int getMinX() {
      return this.src.getMinX();
   }

   public int getMinY() {
      return this.src.getMinY();
   }

   public int getWidth() {
      return this.src.getWidth();
   }

   public int getHeight() {
      return this.src.getHeight();
   }

   public ColorModel getColorModel() {
      return this.src.getColorModel();
   }

   public SampleModel getSampleModel() {
      return this.src.getSampleModel();
   }

   public int getMinTileX() {
      return this.src.getMinTileX();
   }

   public int getMinTileY() {
      return this.src.getMinTileY();
   }

   public int getNumXTiles() {
      return this.src.getNumXTiles();
   }

   public int getNumYTiles() {
      return this.src.getNumYTiles();
   }

   public int getTileGridXOffset() {
      return this.src.getTileGridXOffset();
   }

   public int getTileGridYOffset() {
      return this.src.getTileGridYOffset();
   }

   public int getTileWidth() {
      return this.src.getTileWidth();
   }

   public int getTileHeight() {
      return this.src.getTileHeight();
   }

   public Object getProperty(String var1) {
      return this.src.getProperty(var1);
   }

   public String[] getPropertyNames() {
      return this.src.getPropertyNames();
   }

   public Raster getTile(int var1, int var2) {
      return this.src.getTile(var1, var2);
   }

   public WritableRaster copyData(WritableRaster var1) {
      return this.src.copyData(var1);
   }

   public Raster getData() {
      return this.src.getData();
   }

   public Raster getData(Rectangle var1) {
      return this.src.getData(var1);
   }

   public Shape getDependencyRegion(int var1, Rectangle var2) {
      throw new IndexOutOfBoundsException("Nonexistant source requested.");
   }

   public Shape getDirtyRegion(int var1, Rectangle var2) {
      throw new IndexOutOfBoundsException("Nonexistant source requested.");
   }
}
