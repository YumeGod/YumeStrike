package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import java.util.Map;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class ImageLoaderRaw extends AbstractImageLoader {
   private String mime;
   private ImageFlavor targetFlavor;

   public ImageLoaderRaw(ImageFlavor targetFlavor) {
      this.targetFlavor = targetFlavor;
      this.mime = ImageLoaderFactoryRaw.getMimeForRawFlavor(targetFlavor);
   }

   public ImageFlavor getTargetFlavor() {
      return this.targetFlavor;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!this.mime.equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from a image with MIME type: " + this.mime);
      } else {
         Source src = session.needSource(info.getOriginalURI());
         ImageRawStream rawImage = new ImageRawStream(info, this.getTargetFlavor(), ImageUtil.needInputStream(src));
         return rawImage;
      }
   }
}
