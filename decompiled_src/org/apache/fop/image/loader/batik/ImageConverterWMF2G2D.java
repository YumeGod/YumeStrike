package org.apache.fop.image.loader.batik;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.batik.transcoder.wmf.tosvg.WMFPainter;
import org.apache.batik.transcoder.wmf.tosvg.WMFRecordStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

public class ImageConverterWMF2G2D extends AbstractImageConverter {
   private static Log log;

   public Image convert(Image src, Map hints) {
      this.checkSourceFlavor(src);
      ImageWMF wmf = (ImageWMF)src;
      Graphics2DImagePainter painter = new Graphics2DImagePainterWMF(wmf);
      ImageGraphics2D g2dImage = new ImageGraphics2D(src.getInfo(), painter);
      return g2dImage;
   }

   public ImageFlavor getSourceFlavor() {
      return ImageWMF.WMF_IMAGE;
   }

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.GRAPHICS2D;
   }

   static {
      log = LogFactory.getLog(ImageConverterWMF2G2D.class);
   }

   private static class Graphics2DImagePainterWMF implements Graphics2DImagePainter {
      private ImageWMF wmf;

      public Graphics2DImagePainterWMF(ImageWMF wmf) {
         this.wmf = wmf;
      }

      public Dimension getImageSize() {
         return this.wmf.getSize().getDimensionMpt();
      }

      public void paint(Graphics2D g2d, Rectangle2D area) {
         WMFRecordStore wmfStore = this.wmf.getRecordStore();
         double w = area.getWidth();
         double h = area.getHeight();
         g2d.translate(area.getX(), area.getY());
         double sx = w / (double)wmfStore.getWidthPixels();
         double sy = h / (double)wmfStore.getHeightPixels();
         if (sx != 1.0 || sy != 1.0) {
            g2d.scale(sx, sy);
         }

         WMFPainter painter = new WMFPainter(wmfStore, 1.0F);
         long start = System.currentTimeMillis();
         painter.paint(g2d);
         if (ImageConverterWMF2G2D.log.isDebugEnabled()) {
            long duration = System.currentTimeMillis() - start;
            ImageConverterWMF2G2D.log.debug("Painting WMF took " + duration + " ms.");
         }

      }
   }
}
