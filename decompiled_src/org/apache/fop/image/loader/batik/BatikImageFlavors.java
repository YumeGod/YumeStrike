package org.apache.fop.image.loader.batik;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;

public interface BatikImageFlavors {
   XMLNamespaceEnabledImageFlavor SVG_DOM = new XMLNamespaceEnabledImageFlavor(ImageFlavor.XML_DOM, "http://www.w3.org/2000/svg");
}
