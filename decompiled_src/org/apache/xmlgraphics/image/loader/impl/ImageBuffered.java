package org.apache.xmlgraphics.image.loader.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class ImageBuffered extends ImageRendered {
   public ImageBuffered(ImageInfo info, BufferedImage buffered, Color transparentColor) {
      super(info, buffered, transparentColor);
   }

   public ImageFlavor getFlavor() {
      return ImageFlavor.BUFFERED_IMAGE;
   }

   public BufferedImage getBufferedImage() {
      return (BufferedImage)this.getRenderedImage();
   }
}
