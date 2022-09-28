package org.apache.fop.render.java2d;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;

public class Java2DImageHandlerGraphics2D implements ImageHandler {
   public int getPriority() {
      return 200;
   }

   public Class getSupportedImageClass() {
      return ImageGraphics2D.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return new ImageFlavor[]{ImageFlavor.GRAPHICS2D};
   }

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      Java2DRenderingContext java2dContext = (Java2DRenderingContext)context;
      ImageInfo info = image.getInfo();
      ImageGraphics2D imageG2D = (ImageGraphics2D)image;
      Dimension dim = info.getSize().getDimensionMpt();
      Graphics2D g2d = (Graphics2D)java2dContext.getGraphics2D().create();
      g2d.translate(pos.x, pos.y);
      double sx = (double)pos.width / dim.getWidth();
      double sy = (double)pos.height / dim.getHeight();
      g2d.scale(sx, sy);
      Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, dim.getWidth(), dim.getHeight());
      imageG2D.getGraphics2DImagePainter().paint(g2d, area);
      g2d.dispose();
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageGraphics2D) && targetContext instanceof Java2DRenderingContext;
   }
}
