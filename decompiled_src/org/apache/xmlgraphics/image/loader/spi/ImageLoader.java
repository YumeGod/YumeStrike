package org.apache.xmlgraphics.image.loader.spi;

import java.io.IOException;
import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;

public interface ImageLoader {
   int NO_LOADING_PENALTY = 0;
   int MEDIUM_LOADING_PENALTY = 10;

   Image loadImage(ImageInfo var1, Map var2, ImageSessionContext var3) throws ImageException, IOException;

   Image loadImage(ImageInfo var1, ImageSessionContext var2) throws ImageException, IOException;

   ImageFlavor getTargetFlavor();

   int getUsagePenalty();
}
