package org.apache.xmlgraphics.image.loader.impl.imageio;

import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.Source;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class PreloaderImageIO extends AbstractImagePreloader {
   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException, ImageException {
      if (!ImageUtil.hasImageInputStream(src)) {
         return null;
      } else {
         ImageInputStream in = ImageUtil.needImageInputStream(src);
         Iterator iter = ImageIO.getImageReaders(in);
         if (!iter.hasNext()) {
            return null;
         } else {
            IOException firstIOException = null;
            IIOMetadata iiometa = null;
            ImageSize size = null;
            String mime = null;

            while(iter.hasNext()) {
               in.mark();
               ImageReader reader = (ImageReader)iter.next();

               try {
                  reader.setInput(ImageUtil.ignoreFlushing(in), true, false);
                  int imageIndex = false;
                  iiometa = reader.getImageMetadata(0);
                  size = new ImageSize();
                  size.setSizeInPixels(reader.getWidth(0), reader.getHeight(0));
                  mime = reader.getOriginatingProvider().getMIMETypes()[0];
                  break;
               } catch (IOException var15) {
                  if (firstIOException == null) {
                     firstIOException = var15;
                  }
               } finally {
                  reader.dispose();
                  in.reset();
               }
            }

            if (iiometa == null) {
               if (firstIOException == null) {
                  throw new ImageException("Could not extract image metadata");
               } else {
                  throw new ImageException("I/O error while extracting image metadata" + (firstIOException.getMessage() != null ? ": " + firstIOException.getMessage() : ""), firstIOException);
               }
            } else {
               size.setResolution((double)context.getSourceResolution());
               ImageIOUtil.extractResolution(iiometa, size);
               if (size.getWidthPx() > 0 && size.getHeightPx() > 0) {
                  if (size.getWidthMpt() == 0) {
                     size.calcSizeFromPixels();
                  }

                  ImageInfo info = new ImageInfo(uri, mime);
                  info.getCustomObjects().put(ImageIOUtil.IMAGEIO_METADATA, iiometa);
                  info.setSize(size);
                  return info;
               } else {
                  return null;
               }
            }
         }
      }
   }

   public int getPriority() {
      return 2000;
   }
}
