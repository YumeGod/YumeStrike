package org.apache.fop.render.ps;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.apache.fop.render.RenderingContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.ImageRawJPEG;
import org.apache.xmlgraphics.ps.FormGenerator;
import org.apache.xmlgraphics.ps.ImageEncoder;
import org.apache.xmlgraphics.ps.ImageFormGenerator;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSImageHandlerRawJPEG implements PSImageHandler {
   private static final ImageFlavor[] FLAVORS;

   public void handleImage(RenderingContext context, Image image, Rectangle pos) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageRawJPEG jpeg = (ImageRawJPEG)image;
      float x = (float)pos.getX() / 1000.0F;
      float y = (float)pos.getY() / 1000.0F;
      float w = (float)pos.getWidth() / 1000.0F;
      float h = (float)pos.getHeight() / 1000.0F;
      Rectangle2D targetRect = new Rectangle2D.Float(x, y, w, h);
      ImageInfo info = image.getInfo();
      ImageEncoder encoder = new ImageEncoderJPEG(jpeg);
      org.apache.xmlgraphics.ps.PSImageUtils.writeImage(encoder, info.getSize().getDimensionPx(), info.getOriginalURI(), targetRect, jpeg.getColorSpace(), 8, jpeg.isInverted(), gen);
   }

   public void generateForm(RenderingContext context, Image image, PSImageFormResource form) throws IOException {
      PSRenderingContext psContext = (PSRenderingContext)context;
      PSGenerator gen = psContext.getGenerator();
      ImageRawJPEG jpeg = (ImageRawJPEG)image;
      ImageInfo info = image.getInfo();
      String imageDescription = info.getMimeType() + " " + info.getOriginalURI();
      ImageEncoder encoder = new ImageEncoderJPEG(jpeg);
      FormGenerator formGen = new ImageFormGenerator(form.getName(), imageDescription, info.getSize().getDimensionPt(), info.getSize().getDimensionPx(), encoder, jpeg.getColorSpace(), jpeg.isInverted());
      formGen.generate(gen);
   }

   public int getPriority() {
      return 200;
   }

   public Class getSupportedImageClass() {
      return ImageRawJPEG.class;
   }

   public ImageFlavor[] getSupportedImageFlavors() {
      return FLAVORS;
   }

   public boolean isCompatible(RenderingContext targetContext, Image image) {
      if (targetContext instanceof PSRenderingContext) {
         PSRenderingContext psContext = (PSRenderingContext)targetContext;
         if (psContext.getGenerator().getPSLevel() >= 2) {
            return image == null || image instanceof ImageRawJPEG;
         }
      }

      return false;
   }

   static {
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_JPEG};
   }
}
