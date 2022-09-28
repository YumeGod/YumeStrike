package org.apache.xmlgraphics.image.writer;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class ImageWriterUtil {
   public static void saveAsPNG(RenderedImage bitmap, File outputFile) throws IOException {
      saveAsPNG(bitmap, 96, outputFile);
   }

   public static void saveAsPNG(RenderedImage bitmap, int resolution, File outputFile) throws IOException {
      saveAsFile(bitmap, resolution, outputFile, "image/png");
   }

   public static void saveAsFile(RenderedImage bitmap, int resolution, File outputFile, String mime) throws IOException {
      OutputStream out = new FileOutputStream(outputFile);

      try {
         ImageWriter writer = ImageWriterRegistry.getInstance().getWriterFor(mime);
         ImageWriterParams params = new ImageWriterParams();
         params.setResolution(resolution);
         writer.writeImage(bitmap, out, params);
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }
}
