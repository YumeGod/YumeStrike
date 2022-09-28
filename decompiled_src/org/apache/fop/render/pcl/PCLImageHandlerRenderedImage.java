package org.apache.fop.render.pcl;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;

public class PCLImageHandlerRenderedImage implements ImageHandler {
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
      PCLRenderingContext pclContext = (PCLRenderingContext)context;
      ImageRendered imageRend = (ImageRendered)image;
      PCLGenerator gen = pclContext.getPCLGenerator();
      RenderedImage ri = imageRend.getRenderedImage();
      Point2D transPoint = pclContext.transformedPoint(pos.x, pos.y);
      gen.setCursorPos(transPoint.getX(), transPoint.getY());
      gen.paintBitmap(ri, new Dimension(pos.width, pos.height), pclContext.isSourceTransparencyEnabled());
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageRendered) && targetContext instanceof PCLRenderingContext;
   }
}
