package org.apache.fop.render.java2d;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;

public class Java2DImageHandlerRenderedImage implements ImageHandler {
   public int getPriority() {
      return 300;
   }

   public Class getSupportedImageClass() {
      return ImageRendered.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return new ImageFlavor[]{ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      Java2DRenderingContext java2dContext = (Java2DRenderingContext)context;
      ImageInfo info = image.getInfo();
      ImageRendered imageRend = (ImageRendered)image;
      Graphics2D g2d = java2dContext.getGraphics2D();
      AffineTransform at = new AffineTransform();
      at.translate((double)pos.x, (double)pos.y);
      double sx = pos.getWidth() / (double)info.getSize().getWidthMpt();
      double sy = pos.getHeight() / (double)info.getSize().getHeightMpt();
      float sourceResolution = 72.0F;
      sourceResolution *= 1000.0F;
      sx *= (double)sourceResolution / info.getSize().getDpiHorizontal();
      sy *= (double)sourceResolution / info.getSize().getDpiVertical();
      at.scale(sx, sy);
      RenderedImage rend = imageRend.getRenderedImage();
      if (imageRend.getTransparentColor() != null && !rend.getColorModel().hasAlpha()) {
         int transCol = imageRend.getTransparentColor().getRGB();
         BufferedImage bufImage = this.makeTransparentImage(rend);
         WritableRaster alphaRaster = bufImage.getAlphaRaster();
         int[] transparent = new int[]{0};
         int y = 0;

         for(int maxy = bufImage.getHeight(); y < maxy; ++y) {
            int x = 0;

            for(int maxx = bufImage.getWidth(); x < maxx; ++x) {
               int col = bufImage.getRGB(x, y);
               if (col == transCol) {
                  alphaRaster.setPixel(x, y, transparent);
               }
            }
         }

         g2d.drawRenderedImage(bufImage, at);
      } else {
         g2d.drawRenderedImage(rend, at);
      }

   }

   private BufferedImage makeTransparentImage(RenderedImage src) {
      BufferedImage bufImage = new BufferedImage(src.getWidth(), src.getHeight(), 2);
      Graphics2D g2d = bufImage.createGraphics();
      g2d.drawRenderedImage(src, new AffineTransform());
      g2d.dispose();
      return bufImage;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageRendered) && targetContext instanceof Java2DRenderingContext;
   }
}
