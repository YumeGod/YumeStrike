package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class PreloaderJPEG extends AbstractImagePreloader implements JPEGConstants {
   private static final int JPG_SIG_LENGTH = 3;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException, ImageException {
      if (!ImageUtil.hasImageInputStream(src)) {
         return null;
      } else {
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         byte[] header = this.getHeader(in, 3);
         boolean supported = header[0] == -1 && header[1] == -40 && header[2] == -1;
         if (supported) {
            ImageInfo info = new ImageInfo(uri, "image/jpeg");
            info.setSize(this.determineSize(in, context));
            return info;
         } else {
            return null;
         }
      }
   }

   private ImageSize determineSize(ImageInputStream in, ImageContext context) throws IOException, ImageException {
      in.mark();

      try {
         ImageSize size = new ImageSize();
         JPEGFile jpeg = new JPEGFile(in);

         while(true) {
            int segID = jpeg.readMarkerSegment();
            int reclen;
            ImageSize var12;
            switch (segID) {
               case 0:
               case 216:
                  break;
               case 192:
               case 193:
               case 194:
               case 202:
                  reclen = jpeg.readSegmentLength();
                  in.skipBytes(1);
                  int height = in.readUnsignedShort();
                  int width = in.readUnsignedShort();
                  size.setSizeInPixels(width, height);
                  if (size.getDpiHorizontal() != 0.0) {
                     size.calcSizeFromPixels();
                     var12 = size;
                     return var12;
                  }

                  in.skipBytes(reclen - 7);
                  break;
               case 217:
               case 218:
                  if (size.getDpiHorizontal() == 0.0) {
                     size.setResolution((double)context.getSourceResolution());
                     size.calcSizeFromPixels();
                  }

                  var12 = size;
                  return var12;
               case 224:
                  reclen = jpeg.readSegmentLength();
                  in.skipBytes(7);
                  int densityUnits = in.read();
                  int xdensity = in.readUnsignedShort();
                  int ydensity = in.readUnsignedShort();
                  if (densityUnits == 2) {
                     size.setResolution((double)((float)xdensity * 2.54F), (double)((float)ydensity * 2.54F));
                  } else if (densityUnits == 1) {
                     size.setResolution((double)xdensity, (double)ydensity);
                  } else {
                     size.setResolution((double)context.getSourceResolution());
                  }

                  if (size.getWidthPx() != 0) {
                     size.calcSizeFromPixels();
                     ImageSize var10 = size;
                     return var10;
                  }

                  in.skipBytes(reclen - 14);
                  break;
               default:
                  jpeg.skipCurrentMarkerSegment();
            }
         }
      } finally {
         in.reset();
      }
   }
}
