package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class ImageGraphics2D extends AbstractImage {
   private Graphics2DImagePainter painter;

   public ImageGraphics2D(ImageInfo info, Graphics2DImagePainter painter) {
      super(info);
      this.setGraphics2DImagePainter(painter);
   }

   public ImageFlavor getFlavor() {
      return ImageFlavor.GRAPHICS2D;
   }

   public boolean isCacheable() {
      return true;
   }

   public Graphics2DImagePainter getGraphics2DImagePainter() {
      return this.painter;
   }

   public void setGraphics2DImagePainter(Graphics2DImagePainter painter) {
      this.painter = painter;
   }
}
