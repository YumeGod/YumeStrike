package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import java.text.MessageFormat;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.image.codec.tiff.TIFFDirectory;
import org.apache.xmlgraphics.image.codec.tiff.TIFFField;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.SubImageNotFoundException;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.apache.xmlgraphics.image.loader.util.SeekableStreamAdapter;
import org.apache.xmlgraphics.util.UnitConv;

public class PreloaderTIFF extends AbstractImagePreloader {
   private static Log log;
   private static final int TIFF_SIG_LENGTH = 8;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException, ImageException {
      if (!ImageUtil.hasImageInputStream(src)) {
         return null;
      } else {
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         byte[] header = this.getHeader(in, 8);
         boolean supported = false;
         if (header[0] == 73 && header[1] == 73 && header[2] == 42 && header[3] == 0) {
            supported = true;
         }

         if (header[0] == 77 && header[1] == 77 && header[2] == 0 && header[3] == 42) {
            supported = true;
         }

         if (supported) {
            ImageInfo info = this.createImageInfo(uri, in, context);
            return info;
         } else {
            return null;
         }
      }
   }

   private ImageInfo createImageInfo(String uri, ImageInputStream in, ImageContext context) throws IOException, ImageException {
      ImageInfo info = null;
      in.mark();

      try {
         int pageIndex = ImageUtil.needPageIndexFromURI(uri);
         SeekableStream seekable = new SeekableStreamAdapter(in);

         TIFFDirectory dir;
         try {
            dir = new TIFFDirectory(seekable, pageIndex);
         } catch (IllegalArgumentException var21) {
            String errorMessage = MessageFormat.format("Subimage {0} does not exist.", new Integer(pageIndex));
            throw new SubImageNotFoundException(errorMessage);
         }

         int width = (int)dir.getFieldAsLong(256);
         int height = (int)dir.getFieldAsLong(257);
         ImageSize size = new ImageSize();
         size.setSizeInPixels(width, height);
         int unit = 2;
         if (dir.isTagPresent(296)) {
            unit = (int)dir.getFieldAsLong(296);
         }

         if (unit != 2 && unit != 3) {
            size.setResolution((double)context.getSourceResolution());
         } else {
            TIFFField fldx = dir.getField(282);
            TIFFField fldy = dir.getField(283);
            float xRes;
            float yRes;
            if (fldx != null && fldy != null) {
               xRes = fldx.getAsFloat(0);
               yRes = fldy.getAsFloat(0);
            } else {
               unit = 2;
               xRes = context.getSourceResolution();
               yRes = xRes;
            }

            if (unit == 2) {
               size.setResolution((double)xRes, (double)yRes);
            } else {
               size.setResolution(UnitConv.in2mm((double)xRes) / 10.0, UnitConv.in2mm((double)yRes) / 10.0);
            }
         }

         size.calcSizeFromPixels();
         if (log.isTraceEnabled()) {
            log.trace("TIFF image detected: " + size);
         }

         info = new ImageInfo(uri, "image/tiff");
         info.setSize(size);
         TIFFField fld = dir.getField(259);
         int stripCount;
         if (fld != null) {
            stripCount = fld.getAsInt(0);
            if (log.isTraceEnabled()) {
               log.trace("TIFF compression: " + stripCount);
            }

            info.getCustomObjects().put("TIFF_COMPRESSION", new Integer(stripCount));
         }

         fld = dir.getField(322);
         if (fld != null) {
            if (log.isTraceEnabled()) {
               log.trace("TIFF is tiled");
            }

            info.getCustomObjects().put("TIFF_TILED", Boolean.TRUE);
         }

         fld = dir.getField(278);
         if (fld == null) {
            stripCount = 1;
         } else {
            stripCount = (int)Math.ceil((double)size.getHeightPx() / (double)fld.getAsLong(0));
         }

         if (log.isTraceEnabled()) {
            log.trace("TIFF has " + stripCount + " strips.");
         }

         info.getCustomObjects().put("TIFF_STRIP_COUNT", new Integer(stripCount));

         try {
            new TIFFDirectory(seekable, pageIndex + 1);
            info.getCustomObjects().put(ImageInfo.HAS_MORE_IMAGES, Boolean.TRUE);
            if (log.isTraceEnabled()) {
               log.trace("TIFF is multi-page.");
            }
         } catch (IllegalArgumentException var20) {
            info.getCustomObjects().put(ImageInfo.HAS_MORE_IMAGES, Boolean.FALSE);
         }
      } finally {
         in.reset();
      }

      return info;
   }

   static {
      log = LogFactory.getLog(PreloaderTIFF.class);
   }
}
