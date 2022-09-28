package org.apache.fop.render.ps;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageRawEPS;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSImageHandlerEPS implements ImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageRawEPS eps = (ImageRawEPS)image;
      float x = (float)pos.getX() / 1000.0F;
      float y = (float)pos.getY() / 1000.0F;
      float w = (float)pos.getWidth() / 1000.0F;
      float h = (float)pos.getHeight() / 1000.0F;
      ImageInfo info = image.getInfo();
      Rectangle2D bbox = eps.getBoundingBox();
      if (bbox == null) {
         bbox = new Rectangle2D.Double();
         ((Rectangle2D)bbox).setFrame(new Point2D.Double(), info.getSize().getDimensionPt());
      }

      InputStream in = eps.createInputStream();

      try {
         String resourceName = info.getOriginalURI();
         if (resourceName == null) {
            resourceName = "inline image";
         }

         org.apache.xmlgraphics.ps.PSImageUtils.renderEPS(in, resourceName, new Rectangle2D.Float(x, y, w, h), (Rectangle2D)bbox, gen);
      } finally {
         IOUtils.closeQuietly(in);
      }

   }

   public int getPriority() {
      return 200;
   }

   public Class getSupportedImageClass() {
      return ImageRawEPS.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      if (!(targetContext instanceof PSRenderingContext)) {
         return false;
      } else {
         PSRenderingContext psContext = (PSRenderingContext)targetContext;
         return !psContext.isCreateForms() && (image == null || image instanceof ImageRawEPS);
      }
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_EPS};
   }
}
