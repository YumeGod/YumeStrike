package org.apache.xmlgraphics.image.writer;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public interface ImageWriter {
   void writeImage(RenderedImage var1, OutputStream var2) throws IOException;

   void writeImage(RenderedImage var1, OutputStream var2, ImageWriterParams var3) throws IOException;

   String getMIMEType();

   boolean isFunctional();

   boolean supportsMultiImageWriter();

   MultiImageWriter createMultiImageWriter(OutputStream var1) throws IOException;
}
