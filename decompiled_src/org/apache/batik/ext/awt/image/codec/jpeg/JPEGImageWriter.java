package org.apache.batik.ext.awt.image.codec.jpeg;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

public class JPEGImageWriter implements ImageWriter {
   public void writeImage(RenderedImage var1, OutputStream var2) throws IOException {
      this.writeImage(var1, var2, (ImageWriterParams)null);
   }

   public void writeImage(RenderedImage var1, OutputStream var2, ImageWriterParams var3) throws IOException {
      BufferedImage var4;
      if (var1 instanceof BufferedImage) {
         var4 = (BufferedImage)var1;
      } else {
         var4 = GraphicsUtil.makeLinearBufferedImage(var1.getWidth(), var1.getHeight(), false);
      }

      JPEGImageEncoder var5 = JPEGCodec.createJPEGEncoder(var2);
      if (var3 != null) {
         JPEGEncodeParam var6 = var5.getDefaultJPEGEncodeParam(var4);
         if (var3.getJPEGQuality() != null) {
            var6.setQuality(var3.getJPEGQuality(), var3.getJPEGForceBaseline());
         }

         var5.encode(var4, var6);
      } else {
         var5.encode(var4);
      }

   }

   public String getMIMEType() {
      return "image/jpeg";
   }
}
