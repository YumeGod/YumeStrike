package org.apache.xmlgraphics.image.loader.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageProcessingHints;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.util.UnitConv;

public class ImageConverterG2D2Bitmap extends AbstractImageConverter {
   public Image convert(Image src, Map hints) {
      this.checkSourceFlavor(src);
      ImageGraphics2D g2dImage = (ImageGraphics2D)src;
      Object formatIntent = hints.get(ImageProcessingHints.BITMAP_TYPE_INTENT);
      int bitsPerPixel = 24;
      if ("gray".equals(formatIntent)) {
         bitsPerPixel = 8;
      } else if ("mono".equals(formatIntent)) {
         bitsPerPixel = 1;
      }

      Object transparencyIntent = hints.get(ImageProcessingHints.TRANSPARENCY_INTENT);
      boolean withAlpha = true;
      if ("ignore".equals(transparencyIntent)) {
         withAlpha = false;
      }

      int resolution = 300;
      Number res = (Number)hints.get(ImageProcessingHints.TARGET_RESOLUTION);
      if (res != null) {
         resolution = res.intValue();
      }

      BufferedImage bi = this.paintToBufferedImage(g2dImage, bitsPerPixel, withAlpha, resolution);
      ImageBuffered bufImage = new ImageBuffered(src.getInfo(), bi, (Color)null);
      return bufImage;
   }

   protected BufferedImage paintToBufferedImage(ImageGraphics2D g2dImage, int bitsPerPixel, boolean withAlpha, int resolution) {
      ImageSize size = g2dImage.getSize();
      RenderingHints additionalHints = null;
      int bmw = (int)Math.ceil(UnitConv.mpt2px((double)size.getWidthMpt(), resolution));
      int bmh = (int)Math.ceil(UnitConv.mpt2px((double)size.getHeightMpt(), resolution));
      BufferedImage bi;
      switch (bitsPerPixel) {
         case 1:
            bi = new BufferedImage(bmw, bmh, 12);
            withAlpha = false;
            additionalHints = new RenderingHints((Map)null);
            additionalHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            break;
         case 8:
            if (withAlpha) {
               bi = createGrayBufferedImageWithAlpha(bmw, bmh);
            } else {
               bi = new BufferedImage(bmw, bmh, 10);
            }
            break;
         default:
            if (withAlpha) {
               bi = new BufferedImage(bmw, bmh, 2);
            } else {
               bi = new BufferedImage(bmw, bmh, 1);
            }
      }

      Graphics2D g2d = bi.createGraphics();

      try {
         g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
         this.setRenderingHintsForBufferedImage(g2d);
         if (additionalHints != null) {
            g2d.addRenderingHints(additionalHints);
         }

         g2d.setBackground(Color.white);
         g2d.setColor(Color.black);
         if (!withAlpha) {
            g2d.clearRect(0, 0, bmw, bmh);
         }

         double sx = (double)bmw / (double)size.getWidthMpt();
         double sy = (double)bmh / (double)size.getHeightMpt();
         g2d.scale(sx, sy);
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, (double)size.getWidthMpt(), (double)size.getHeightMpt());
         g2dImage.getGraphics2DImagePainter().paint(g2d, area);
      } finally {
         g2d.dispose();
      }

      return bi;
   }

   private static BufferedImage createGrayBufferedImageWithAlpha(int width, int height) {
      boolean alphaPremultiplied = true;
      int bands = 2;
      int[] bits = new int[bands];

      for(int i = 0; i < bands; ++i) {
         bits[i] = 8;
      }

      ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(1003), bits, true, alphaPremultiplied, 3, 0);
      WritableRaster wr = Raster.createInterleavedRaster(0, width, height, bands, new Point(0, 0));
      BufferedImage bi = new BufferedImage(cm, wr, alphaPremultiplied, (Hashtable)null);
      return bi;
   }

   protected void setRenderingHintsForBufferedImage(Graphics2D g2d) {
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
   }

   public ImageFlavor getSourceFlavor() {
      return ImageFlavor.GRAPHICS2D;
   }

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.BUFFERED_IMAGE;
   }
}
