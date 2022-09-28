package org.apache.fop.svg;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

class PDFGraphicsConfiguration extends GraphicsConfiguration {
   private static final BufferedImage BI_WITH_ALPHA = new BufferedImage(1, 1, 2);
   private static final BufferedImage BI_WITHOUT_ALPHA = new BufferedImage(1, 1, 1);

   public BufferedImage createCompatibleImage(int width, int height, int transparency) {
      return transparency == 1 ? new BufferedImage(width, height, 1) : new BufferedImage(width, height, 2);
   }

   public BufferedImage createCompatibleImage(int width, int height) {
      return new BufferedImage(width, height, 2);
   }

   public Rectangle getBounds() {
      return null;
   }

   public ColorModel getColorModel() {
      return BI_WITH_ALPHA.getColorModel();
   }

   public ColorModel getColorModel(int transparency) {
      return transparency == 1 ? BI_WITHOUT_ALPHA.getColorModel() : BI_WITH_ALPHA.getColorModel();
   }

   public AffineTransform getDefaultTransform() {
      return new AffineTransform();
   }

   public AffineTransform getNormalizingTransform() {
      return new AffineTransform(2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F);
   }

   public GraphicsDevice getDevice() {
      return new PDFGraphicsDevice(this);
   }
}
