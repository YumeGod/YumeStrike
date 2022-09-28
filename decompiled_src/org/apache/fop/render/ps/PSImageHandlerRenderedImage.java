package org.apache.fop.render.ps;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.ps.FormGenerator;
import org.apache.xmlgraphics.ps.ImageFormGenerator;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSImageHandlerRenderedImage implements PSImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageRendered imageRend = (ImageRendered)image;
      float x = (float)pos.getX() / 1000.0F;
      float y = (float)pos.getY() / 1000.0F;
      float w = (float)pos.getWidth() / 1000.0F;
      float h = (float)pos.getHeight() / 1000.0F;
      RenderedImage ri = imageRend.getRenderedImage();
      org.apache.xmlgraphics.ps.PSImageUtils.renderBitmapImage(ri, x, y, w, h, gen);
   }

   public void generateForm(RenderingContext context, Image image, PSImageFormResource form) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageRendered imageRend = (ImageRendered)image;
      ImageInfo info = image.getInfo();
      String imageDescription = info.getMimeType() + " " + info.getOriginalURI();
      RenderedImage ri = imageRend.getRenderedImage();
      FormGenerator formGen = new ImageFormGenerator(form.getName(), imageDescription, info.getSize().getDimensionPt(), ri, false);
      formGen.generate(gen);
   }

   public int getPriority() {
      return 300;
   }

   public Class getSupportedImageClass() {
      return ImageRendered.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      return (image == null || image instanceof ImageRendered) && targetContext instanceof PSRenderingContext;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
   }
}
