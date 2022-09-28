package org.apache.xmlgraphics.image.loader.impl;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.spi.ImageConverter;

public abstract class AbstractImageConverter implements ImageConverter {
   protected void checkSourceFlavor(Image img) {
      if (!this.getSourceFlavor().equals(img.getFlavor())) {
         throw new IllegalArgumentException("Incompatible image: " + img);
      }
   }

   public int getConversionPenalty() {
      return 10;
   }
}
