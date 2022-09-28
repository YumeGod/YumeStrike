package org.apache.xmlgraphics.image.writer;

import java.awt.image.RenderedImage;
import java.io.IOException;

public interface MultiImageWriter {
   void writeImage(RenderedImage var1, ImageWriterParams var2) throws IOException;

   void close() throws IOException;
}
