package org.apache.fop.render;

import java.awt.Color;
import java.awt.Dimension;
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
import org.apache.fop.util.UnitConv;

public abstract class AbstractImageHandlerGraphics2D implements ImageHandler {
   protected BufferedImage paintToBufferedImage(org.apache.xmlgraphics.java2d.Graphics2DImagePainter painter, Dimension targetDimension, int resolution, boolean gray, boolean withAlpha) {
      int bmw = (int)Math.ceil(UnitConv.mpt2px(targetDimension.getWidth(), resolution));
      int bmh = (int)Math.ceil(UnitConv.mpt2px(targetDimension.getHeight(), resolution));
      BufferedImage bi;
      if (gray) {
         if (withAlpha) {
            bi = createGrayBufferedImageWithAlpha(bmw, bmh);
         } else {
            bi = new BufferedImage(bmw, bmh, 10);
         }
      } else if (withAlpha) {
         bi = new BufferedImage(bmw, bmh, 2);
      } else {
         bi = new BufferedImage(bmw, bmh, 1);
      }

      Graphics2D g2d = bi.createGraphics();

      try {
         g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
         this.setRenderingHintsForBufferedImage(g2d);
         g2d.setBackground(Color.white);
         g2d.setColor(Color.black);
         if (!withAlpha) {
            g2d.clearRect(0, 0, bmw, bmh);
         }

         double sx = (double)bmw / targetDimension.getWidth();
         double sy = (double)bmh / targetDimension.getHeight();
         g2d.scale(sx, sy);
         Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, targetDimension.getWidth(), targetDimension.getHeight());
         painter.paint(g2d, area);
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
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
   }
}
