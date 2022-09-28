package org.apache.fop.afp.svg;

import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.fop.svg.AbstractFOPImageElementBridge;
import org.apache.xmlgraphics.image.loader.ImageFlavor;

public class AFPImageElementBridge extends AbstractFOPImageElementBridge {
   private final ImageFlavor[] supportedFlavors;

   public AFPImageElementBridge() {
      this.supportedFlavors = new ImageFlavor[]{ImageFlavor.GRAPHICS2D, BatikImageFlavors.SVG_DOM};
   }

   protected ImageFlavor[] getSupportedFlavours() {
      return this.supportedFlavors;
   }
}
