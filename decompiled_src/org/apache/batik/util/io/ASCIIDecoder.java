package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

public class ASCIIDecoder extends AbstractCharDecoder {
   public ASCIIDecoder(InputStream var1) {
      super(var1);
   }

   public int readChar() throws IOException {
      if (this.position == this.count) {
         this.fillBuffer();
      }

      if (this.count == -1) {
         return -1;
      } else {
         byte var1 = this.buffer[this.position++];
         if (var1 < 0) {
            this.charError("ASCII");
         }

         return var1;
      }
   }
}
