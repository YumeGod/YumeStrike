package org.apache.xmlgraphics.image.loader.impl;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.codec.tiff.TIFFDirectory;
import org.apache.xmlgraphics.image.codec.tiff.TIFFField;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.image.loader.util.SeekableStreamAdapter;
import org.apache.xmlgraphics.util.io.SubInputStream;

public class ImageLoaderRawCCITTFax extends AbstractImageLoader implements JPEGConstants {
   protected static Log log;

   public ImageFlavor getTargetFlavor() {
      return ImageFlavor.RAW_CCITTFAX;
   }

   public Image loadImage(ImageInfo info, Map hints, ImageSessionContext session) throws ImageException, IOException {
      if (!"image/tiff".equals(info.getMimeType())) {
         throw new IllegalArgumentException("ImageInfo must be from a image with MIME type: image/tiff");
      } else {
         int fillOrder = 1;
         int compression = 1;
         Source src = session.needSource(info.getOriginalURI());
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         in.mark();

         long stripOffset;
         long stripLength;
         try {
            SeekableStream seekable = new SeekableStreamAdapter(in);
            TIFFDirectory dir = new TIFFDirectory(seekable, 0);
            TIFFField fld = dir.getField(259);
            if (fld != null) {
               compression = fld.getAsInt(0);
               switch (compression) {
                  case 2:
                  case 3:
                  case 4:
                     break;
                  default:
                     log.debug("Unsupported compression " + compression);
                     throw new ImageException("ImageLoader doesn't support TIFF compression: " + compression);
               }
            }

            fld = dir.getField(266);
            if (fld != null) {
               fillOrder = fld.getAsInt(0);
            }

            fld = dir.getField(278);
            int stripCount;
            if (fld == null) {
               stripCount = 1;
            } else {
               stripCount = (int)((long)info.getSize().getHeightPx() / fld.getAsLong(0));
            }

            if (stripCount > 1) {
               log.debug("More than one strip found in TIFF image.");
               throw new ImageException("ImageLoader doesn't support multiple strips");
            }

            stripOffset = dir.getField(273).getAsLong(0);
            stripLength = dir.getField(279).getAsLong(0);
         } finally {
            in.reset();
         }

         in.seek(stripOffset);
         Object subin = new SubInputStream(ImageUtil.needInputStream(src), stripLength, true);
         if (fillOrder == 2) {
            subin = new FillOrderChangeInputStream((InputStream)subin);
         }

         ImageRawCCITTFax rawImage = new ImageRawCCITTFax(info, (InputStream)subin, compression);
         ImageUtil.removeStreams(src);
         return rawImage;
      }
   }

   static {
      log = LogFactory.getLog(ImageLoaderRawCCITTFax.class);
   }

   private static class FillOrderChangeInputStream extends FilterInputStream {
      private static final byte[] FLIP_TABLE = new byte[]{0, -128, 64, -64, 32, -96, 96, -32, 16, -112, 80, -48, 48, -80, 112, -16, 8, -120, 72, -56, 40, -88, 104, -24, 24, -104, 88, -40, 56, -72, 120, -8, 4, -124, 68, -60, 36, -92, 100, -28, 20, -108, 84, -44, 52, -76, 116, -12, 12, -116, 76, -52, 44, -84, 108, -20, 28, -100, 92, -36, 60, -68, 124, -4, 2, -126, 66, -62, 34, -94, 98, -30, 18, -110, 82, -46, 50, -78, 114, -14, 10, -118, 74, -54, 42, -86, 106, -22, 26, -102, 90, -38, 58, -70, 122, -6, 6, -122, 70, -58, 38, -90, 102, -26, 22, -106, 86, -42, 54, -74, 118, -10, 14, -114, 78, -50, 46, -82, 110, -18, 30, -98, 94, -34, 62, -66, 126, -2, 1, -127, 65, -63, 33, -95, 97, -31, 17, -111, 81, -47, 49, -79, 113, -15, 9, -119, 73, -55, 41, -87, 105, -23, 25, -103, 89, -39, 57, -71, 121, -7, 5, -123, 69, -59, 37, -91, 101, -27, 21, -107, 85, -43, 53, -75, 117, -11, 13, -115, 77, -51, 45, -83, 109, -19, 29, -99, 93, -35, 61, -67, 125, -3, 3, -125, 67, -61, 35, -93, 99, -29, 19, -109, 83, -45, 51, -77, 115, -13, 11, -117, 75, -53, 43, -85, 107, -21, 27, -101, 91, -37, 59, -69, 123, -5, 7, -121, 71, -57, 39, -89, 103, -25, 23, -105, 87, -41, 55, -73, 119, -9, 15, -113, 79, -49, 47, -81, 111, -17, 31, -97, 95, -33, 63, -65, 127, -1};

      protected FillOrderChangeInputStream(InputStream in) {
         super(in);
      }

      public int read(byte[] b, int off, int len) throws IOException {
         int result = super.read(b, off, len);
         if (result > 0) {
            int endpos = off + result;

            for(int i = off; i < endpos; ++i) {
               b[i] = FLIP_TABLE[b[i] & 255];
            }
         }

         return result;
      }

      public int read() throws IOException {
         int b = super.read();
         return b < 0 ? b : FLIP_TABLE[b] & 255;
      }
   }
}
