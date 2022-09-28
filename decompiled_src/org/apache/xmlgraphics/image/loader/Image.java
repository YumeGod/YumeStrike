package org.apache.xmlgraphics.image.loader;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;

public interface Image {
   ImageInfo getInfo();

   ImageSize getSize();

   ImageFlavor getFlavor();

   boolean isCacheable();

   ICC_Profile getICCProfile();

   ColorSpace getColorSpace();
}
