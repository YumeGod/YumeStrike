package org.apache.batik.svggen;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;

public class ImageHandlerPNGEncoder extends AbstractImageHandlerEncoder {
   public ImageHandlerPNGEncoder(String var1, String var2) throws SVGGraphics2DIOException {
      super(var1, var2);
   }

   public final String getSuffix() {
      return ".png";
   }

   public final String getPrefix() {
      return "pngImage";
   }

   public void encodeImage(BufferedImage var1, File var2) throws SVGGraphics2DIOException {
      try {
         FileOutputStream var3 = new FileOutputStream(var2);

         try {
            ImageWriter var4 = ImageWriterRegistry.getInstance().getWriterFor("image/png");
            var4.writeImage(var1, var3);
         } finally {
            var3.close();
         }

      } catch (IOException var9) {
         throw new SVGGraphics2DIOException("could not write image File " + var2.getName());
      }
   }

   public BufferedImage buildBufferedImage(Dimension var1) {
      return new BufferedImage(var1.width, var1.height, 2);
   }
}
