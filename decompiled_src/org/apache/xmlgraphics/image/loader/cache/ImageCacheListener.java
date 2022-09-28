package org.apache.xmlgraphics.image.loader.cache;

import java.util.EventListener;

public interface ImageCacheListener extends EventListener {
   void invalidHit(String var1);

   void cacheHitImageInfo(String var1);

   void cacheMissImageInfo(String var1);

   void cacheHitImage(ImageKey var1);

   void cacheMissImage(ImageKey var1);
}
