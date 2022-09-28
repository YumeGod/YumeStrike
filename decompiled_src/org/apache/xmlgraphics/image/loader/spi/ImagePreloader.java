package org.apache.xmlgraphics.image.loader.spi;

import java.io.IOException;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public interface ImagePreloader {
   int DEFAULT_PRIORITY = 1000;

   ImageInfo preloadImage(String var1, Source var2, ImageContext var3) throws ImageException, IOException;

   int getPriority();
}
