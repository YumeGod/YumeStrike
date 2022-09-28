package org.apache.xmlgraphics.image.loader.impl;

import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;

public class ImageConverterBuffered2Rendered extends AbstractImageConverter {
   public Image convert(Image src, Map hints) {
      this.checkSourceFlavor(src);
      ImageBuffered buffered = (ImageBuffered)src;
      return new ImageRendered(buffered.getInfo(), buffered.getRenderedImage(), buffered.getTransparentColor());
   }

   public ImageFlavor getSourceFlavor() {
      return ImageFlavor.BUFFERED_IMAGE;
   }

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RENDERED_IMAGE;
   }

   public int getConversionPenalty() {
      return 0;
   }
}
