package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class PreloaderGIF extends AbstractImagePreloader {
   private static final int GIF_SIG_LENGTH = 10;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException {
      if (!ImageUtil.hasImageInputStream(src)) {
         return null;
      } else {
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         byte[] header = this.getHeader(in, 10);
         boolean supported = header[0] == 71 && header[1] == 73 && header[2] == 70 && header[3] == 56 && (header[4] == 55 || header[4] == 57) && header[5] == 97;
         if (supported) {
            ImageInfo info = new ImageInfo(uri, "image/gif");
            info.setSize(this.determineSize(header, context));
            return info;
         } else {
            return null;
         }
      }
   }

   private ImageSize determineSize(byte[] header, ImageContext context) {
      int byte1 = header[6] & 255;
      int byte2 = header[7] & 255;
      int width = (byte2 << 8 | byte1) & '\uffff';
      byte1 = header[8] & 255;
      byte2 = header[9] & 255;
      int height = (byte2 << 8 | byte1) & '\uffff';
      ImageSize size = new ImageSize(width, height, (double)context.getSourceResolution());
      size.calcSizeFromPixels();
      return size;
   }
}
