package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.util.io.SubInputStream;

public class ImageLoaderEPS extends AbstractImageLoader {
   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RAW_EPS;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!"application/postscript".equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from a image with MIME type: application/postscript");
      } else {
         Source src = session.needSource(info.getOriginalURI());
         InputStream in = ImageUtil.needInputStream(src);
         ImageUtil.removeStreams(src);
         PreloaderEPS.EPSBinaryFileHeader binaryHeader = (PreloaderEPS.EPSBinaryFileHeader)info.getCustomObjects().get(PreloaderEPS.EPS_BINARY_HEADER);
         if (binaryHeader != null) {
            ((InputStream)in).skip(binaryHeader.getPSStart());
            in = new SubInputStream((InputStream)in, binaryHeader.getPSLength(), true);
         }

         ImageRawEPS epsImage = new ImageRawEPS(info, (InputStream)in);
         return epsImage;
      }
   }
}
