package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

public class ISO_8859_1Decoder extends AbstractCharDecoder {
   public ISO_8859_1Decoder(InputStream var1) {
      super(var1);
   }

   public int readChar() throws IOException {
      if (this.position == this.count) {
         this.fillBuffer();
      }

      return this.count == -1 ? -1 : this.buffer[this.position++] & 255;
   }
}
