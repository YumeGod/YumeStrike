package org.apache.batik.ext.awt.image.codec.png;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

public class PNGImageWriter implements ImageWriter {
   public void writeImage(RenderedImage var1, OutputStream var2) throws IOException {
      this.writeImage(var1, var2, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage var1, OutputStream var2, ImageWriterParams var3) throws IOException {
      PNGImageEncoder var4 = new PNGImageEncoder(var2, (PNGEncodeParam)null);
      var4.encode(var1);
   }

   public String getMIMEType() {
      return "image/png";
   }
}
