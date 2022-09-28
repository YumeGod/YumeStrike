package org.apache.xmlgraphics.image.loader.spi;

import java.io.IOException;
import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;

public interface ImageConverter {
   int NO_CONVERSION_PENALTY = 0;
   int MINIMAL_CONVERSION_PENALTY = 1;
   int MEDIUM_CONVERSION_PENALTY = 10;

   Image convert(Image var1, Map var2) throws ImageException, IOException;

   ImageFlavor getTargetFlavor();

   ImageFlavor getSourceFlavor();

   int getConversionPenalty();
}
