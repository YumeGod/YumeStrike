package org.apache.xmlgraphics.image.loader.impl;

import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class ImageRawEPS extends ImageRawStream {
   public ImageRawEPS(ImageInfo info, ImageRawStream.InputStreamFactory streamFactory) {
      super(info, ImageFlavor.RAW_EPS, streamFactory);
   }

   public ImageRawEPS(ImageInfo info, InputStream in) {
      super(info, ImageFlavor.RAW_EPS, in);
   }

   public Rectangle2D getBoundingBox() {
      Rectangle2D bbox = (Rectangle2D)this.getInfo().getCustomObjects().get(PreloaderEPS.EPS_BOUNDING_BOX);
      return bbox;
   }
}
