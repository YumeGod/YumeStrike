package org.apache.batik.ext.awt.image.spi;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public interface ImageWriter {
   void writeImage(RenderedImage var1, OutputStream var2) throws IOException;

   void writeImage(RenderedImage var1, OutputStream var2, ImageWriterParams var3) throws IOException;

   String getMIMEType();
}
