package org.apache.batik.svggen;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;

public class ImageHandlerJPEGEncoder extends AbstractImageHandlerEncoder {
   public ImageHandlerJPEGEncoder(String var1, String var2) throws SVGGraphics2DIOException {
      super(var1, var2);
   }

   public final String getSuffix() {
      return ".jpg";
   }

   public final String getPrefix() {
      return "jpegImage";
   }

   public void encodeImage(BufferedImage var1, File var2) throws SVGGraphics2DIOException {
      try {
         FileOutputStream var3 = new FileOutputStream(var2);

         try {
            ImageWriter var4 = ImageWriterRegistry.getInstance().getWriterFor("image/jpeg");
            ImageWriterParams var5 = new ImageWriterParams();
            var5.setJPEGQuality(1.0F, false);
            var4.writeImage(var1, var3, var5);
         } finally {
            var3.close();
         }

      } catch (IOException var10) {
         throw new SVGGraphics2DIOException("could not write image File " + var2.getName());
      }
   }

   public BufferedImage buildBufferedImage(Dimension var1) {
      return new BufferedImage(var1.width, var1.height, 1);
   }
}
