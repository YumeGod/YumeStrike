package org.apache.xmlgraphics.image.writer;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractImageWriter implements ImageWriter {
   public MultiImageWriter createMultiImageWriter(OutputStream out) throws IOException {
      throw new UnsupportedOperationException("This ImageWriter does not support writing multiple images to a single image file.");
   }

   public boolean isFunctional() {
      return true;
   }

   public boolean supportsMultiImageWriter() {
      return false;
   }
}
