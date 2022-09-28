package org.apache.fop.image.loader.batik;

import java.io.IOException;
import java.util.Map;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;

public class ImageLoaderWMF extends AbstractImageLoader {
   private ImageFlavor targetFlavor;

   public ImageLoaderWMF(ImageFlavor targetFlavor) {
      if (!ImageWMF.WMF_IMAGE.equals(targetFlavor)) {
         throw new IllegalArgumentException("Unsupported target ImageFlavor: " + targetFlavor);
      } else {
         this.targetFlavor = targetFlavor;
      }
   }

   public ImageFlavor getTargetFlavor() {
      return this.targetFlavor;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!"image/x-wmf".equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from a WMF image");
      } else {
         Image img = info.getOriginalImage();
         if (!(img instanceof ImageWMF)) {
            throw new IllegalArgumentException("ImageInfo was expected to contain the Windows Metafile (WMF)");
         } else {
            ImageWMF wmfImage = (ImageWMF)img;
            return wmfImage;
         }
      }
   }
}
