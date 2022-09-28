package org.apache.xmlgraphics.image.loader.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.writer.ImageWriter;
import org.apache.xmlgraphics.image.writer.ImageWriterParams;
import org.apache.xmlgraphics.image.writer.ImageWriterRegistry;

public class ImageConverterRendered2PNG extends AbstractImageConverter {
   public Image convert(Image src, Map hints) throws ImageException, IOException {
      this.checkSourceFlavor(src);
      ImageRendered rendered = (ImageRendered)src;
      ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor("image/png");
      if (writer == null) {
         throw new ImageException("Cannot convert image to PNG. No suitable ImageWriter found.");
      } else {
         ByteArrayOutputStream baout = new ByteArrayOutputStream();
         ImageWriterParams params = new ImageWriterParams();
         params.setResolution((int)Math.round(src.getSize().getDpiHorizontal()));
         writer.writeImage(rendered.getRenderedImage(), baout, params);
         return new ImageRawStream(src.getInfo(), this.getTargetFlavor(), new ByteArrayInputStream(baout.toByteArray()));
      }
   }

   public ImageFlavor getSourceFlavor() {
      return ImageFlavor.RENDERED_IMAGE;
   }

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RAW_PNG;
   }
}
