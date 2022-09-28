package org.apache.xmlgraphics.image.rendered;

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

   public static CachableRed wrap(RenderedImage ri) {
      if (ri instanceof CachableRed) {
         return (CachableRed)ri;
      } else {
         return (CachableRed)(ri instanceof BufferedImage ? new BufferedImageCachableRed((BufferedImage)ri) : new RenderedImageCachableRed(ri));
      }
   }

   public RenderedImageCachableRed(RenderedImage src) {
      if (src == null) {
         throw new IllegalArgumentException();
      } else {
         this.src = src;
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

   public Object getProperty(String name) {
      return this.src.getProperty(name);
   }

   public String[] getPropertyNames() {
      return this.src.getPropertyNames();
   }

   public Raster getTile(int tileX, int tileY) {
      return this.src.getTile(tileX, tileY);
   }

   public WritableRaster copyData(WritableRaster raster) {
      return this.src.copyData(raster);
   }

   public Raster getData() {
      return this.src.getData();
   }

   public Raster getData(Rectangle rect) {
      return this.src.getData(rect);
   }

   public Shape getDependencyRegion(int srcIndex, Rectangle outputRgn) {
      throw new IndexOutOfBoundsException("Nonexistant source requested.");
   }

   public Shape getDirtyRegion(int srcIndex, Rectangle inputRgn) {
      throw new IndexOutOfBoundsException("Nonexistant source requested.");
   }
}
