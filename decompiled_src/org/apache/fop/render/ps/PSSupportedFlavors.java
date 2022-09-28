package org.apache.fop.render.ps;

import org.apache.xmlgraphics.image.loader.ImageFlavor;

public interface PSSupportedFlavors {
   ImageFlavor[] LEVEL_2_FLAVORS_INLINE = new ImageFlavor[]{ImageFlavor.RAW_EPS, ImageFlavor.RAW_CCITTFAX, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE, ImageFlavor.XML_DOM};
   ImageFlavor[] LEVEL_3_FLAVORS_INLINE = new ImageFlavor[]{ImageFlavor.RAW_EPS, ImageFlavor.RAW_JPEG, ImageFlavor.RAW_CCITTFAX, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE, ImageFlavor.XML_DOM};
   ImageFlavor[] LEVEL_2_FLAVORS_FORM = new ImageFlavor[]{ImageFlavor.RAW_CCITTFAX, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
   ImageFlavor[] LEVEL_3_FLAVORS_FORM = new ImageFlavor[]{ImageFlavor.RAW_JPEG, ImageFlavor.RAW_CCITTFAX, ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE};
}
