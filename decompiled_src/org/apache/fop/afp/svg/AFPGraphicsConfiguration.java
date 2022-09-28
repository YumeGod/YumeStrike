package org.apache.fop.afp.svg;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.svg.GraphicsConfiguration;

public class AFPGraphicsConfiguration extends GraphicsConfiguration {
   private static final BufferedImage BI_WITH_ALPHA = new BufferedImage(1, 1, 2);
   private static final BufferedImage BI_WITHOUT_ALPHA = new BufferedImage(1, 1, 1);
   private static final Log log;
   private AffineTransform defaultTransform = null;
   private AffineTransform normalizingTransform = null;
   private final GraphicsDevice graphicsDevice = new AFPGraphicsDevice(this);

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
      log.debug("getDefaultTransform()");
      if (this.defaultTransform == null) {
         this.defaultTransform = new AffineTransform();
      }

      return this.defaultTransform;
   }

   public AffineTransform getNormalizingTransform() {
      log.debug("getNormalizingTransform()");
      if (this.normalizingTransform == null) {
         this.normalizingTransform = new AffineTransform(2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F);
      }

      return this.normalizingTransform;
   }

   public GraphicsDevice getDevice() {
      log.debug("getDevice()");
      return this.graphicsDevice;
   }

   static {
      log = LogFactory.getLog(AFPGraphicsConfiguration.class);
   }
}
