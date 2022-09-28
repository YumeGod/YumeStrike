package org.apache.fop.image.loader.batik;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.transform.Source;
import org.apache.batik.transcoder.wmf.tosvg.WMFRecordStore;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.UnclosableInputStream;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;

public class PreloaderWMF extends AbstractImagePreloader {
   private static Log log;
   private boolean batikAvailable = true;

   public ImageInfo preloadImage(String uri, Source src, ImageContext context) throws IOException {
      if (!ImageUtil.hasInputStream(src)) {
         return null;
      } else {
         ImageInfo info = null;
         if (this.batikAvailable) {
            try {
               Loader loader = new Loader();
               info = loader.getImage(uri, src, context);
            } catch (NoClassDefFoundError var6) {
               this.batikAvailable = false;
               log.warn("Batik not in class path", var6);
               return null;
            }
         }

         if (info != null) {
            ImageUtil.closeQuietly(src);
         }

         return info;
      }
   }

   static {
      log = LogFactory.getLog(PreloaderWMF.class);
   }

   class Loader {
      private ImageInfo getImage(String uri, Source src, ImageContext context) {
         InputStream in = new UnclosableInputStream(ImageUtil.needInputStream(src));

         try {
            in.mark(5);
            DataInputStream din = new DataInputStream(in);
            int magic = EndianUtils.swapInteger(din.readInt());
            din.reset();
            if (magic != -1698247209) {
               return null;
            } else {
               WMFRecordStore wmfStore = new WMFRecordStore();
               wmfStore.read(din);
               IOUtils.closeQuietly((InputStream)din);
               int width = wmfStore.getWidthUnits();
               int height = wmfStore.getHeightUnits();
               int dpi = wmfStore.getMetaFileUnitsPerInch();
               ImageInfo info = new ImageInfo(uri, "image/x-wmf");
               ImageSize size = new ImageSize();
               size.setSizeInPixels(width, height);
               size.setResolution((double)dpi);
               size.calcSizeFromPixels();
               info.setSize(size);
               ImageWMF img = new ImageWMF(info, wmfStore);
               info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, img);
               return info;
            }
         } catch (NoClassDefFoundError var16) {
            try {
               in.reset();
            } catch (IOException var14) {
            }

            PreloaderWMF.this.batikAvailable = false;
            PreloaderWMF.log.warn("Batik not in class path", var16);
            return null;
         } catch (IOException var17) {
            PreloaderWMF.log.debug("Error while trying to load stream as an WMF file: " + var17.getMessage());

            try {
               in.reset();
            } catch (IOException var15) {
            }

            return null;
         }
      }
   }
}
