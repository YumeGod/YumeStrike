package org.apache.batik.svggen;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;

public class CachedImageHandlerPNGEncoder extends DefaultCachedImageHandler {
   public static final String CACHED_PNG_PREFIX = "pngImage";
   public static final String CACHED_PNG_SUFFIX = ".png";
   protected String refPrefix = "";

   public CachedImageHandlerPNGEncoder(String var1, String var2) throws SVGGraphics2DIOException {
      this.refPrefix = var2 + "/";
      this.setImageCacher(new ImageCacher.External(var1, "pngImage", ".png"));
   }

   public void encodeImage(BufferedImage var1, OutputStream var2) throws IOException {
      ImageWriter var3 = ImageWriterRegistry.getInstance().getWriterFor("image/png");
      var3.writeImage(var1, var2);
   }

   public int getBufferedImageType() {
      return 2;
   }

   public String getRefPrefix() {
      return this.refPrefix;
   }
}
