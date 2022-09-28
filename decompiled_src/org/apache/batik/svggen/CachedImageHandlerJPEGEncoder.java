package org.apache.batik.svggen;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;

public class CachedImageHandlerJPEGEncoder extends DefaultCachedImageHandler {
   public static final String CACHED_JPEG_PREFIX = "jpegImage";
   public static final String CACHED_JPEG_SUFFIX = ".jpg";
   protected String refPrefix = "";

   public CachedImageHandlerJPEGEncoder(String var1, String var2) throws SVGGraphics2DIOException {
      this.refPrefix = var2 + "/";
      this.setImageCacher(new ImageCacher.External(var1, "jpegImage", ".jpg"));
   }

   public void encodeImage(BufferedImage var1, OutputStream var2) throws IOException {
      ImageWriter var3 = ImageWriterRegistry.getInstance().getWriterFor("image/jpeg");
      ImageWriterParams var4 = new ImageWriterParams();
      var4.setJPEGQuality(1.0F, false);
      var3.writeImage(var1, var2, var4);
   }

   public int getBufferedImageType() {
      return 1;
   }

   public String getRefPrefix() {
      return this.refPrefix;
   }
}
