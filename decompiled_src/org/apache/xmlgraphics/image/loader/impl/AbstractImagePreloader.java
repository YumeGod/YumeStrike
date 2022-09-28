package org.apache.xmlgraphics.image.loader.impl;

import java.io.IOException;
import java.util.Arrays;
import javax.imageio.stream.ImageInputStream;
import org.apache.xmlgraphics.image.loader.spi.ImagePreloader;

public abstract class AbstractImagePreloader implements ImagePreloader {
   protected byte[] getHeader(ImageInputStream in, int size) throws IOException {
      byte[] header = new byte[size];
      long startPos = in.getStreamPosition();
      int read = in.read(header);
      if (read < size) {
         Arrays.fill(header, (byte)0);
      }

      in.seek(startPos);
      return header;
   }

   public int getPriority() {
      return 1000;
   }
}
