package org.apache.fop.svg;

import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.xmlgraphics.image.loader.ImageFlavor;

public class PDFImageElementBridge extends AbstractFOPImageElementBridge {
   private final ImageFlavor[] supportedFlavors;

   public PDFImageElementBridge() {
      this.supportedFlavors = new ImageFlavor[]{ImageFlavor.RAW_JPEG, ImageFlavor.RAW_CCITTFAX, ImageFlavor.GRAPHICS2D, BatikImageFlavors.SVG_DOM};
   }

   protected ImageFlavor[] getSupportedFlavours() {
      return this.supportedFlavors;
   }
}
