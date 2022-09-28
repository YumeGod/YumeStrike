package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

public class UTF8Decoder extends AbstractCharDecoder {
   protected static final byte[] UTF8_BYTES = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0};
   protected int nextChar = -1;

   public UTF8Decoder(InputStream var1) {
      super(var1);
   }

   public int readChar() throws IOException {
      int var1;
      if (this.nextChar != -1) {
         var1 = this.nextChar;
         this.nextChar = -1;
         return var1;
      } else {
         if (this.position == this.count) {
            this.fillBuffer();
         }

         if (this.count == -1) {
            return -1;
         } else {
            var1 = this.buffer[this.position++] & 255;
            byte var2;
            byte var3;
            switch (UTF8_BYTES[var1]) {
               case 2:
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  return (var1 & 31) << 6 | this.buffer[this.position++] & 63;
               case 3:
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  var2 = this.buffer[this.position++];
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  var3 = this.buffer[this.position++];
                  if ((var2 & 192) != 128 || (var3 & 192) != 128) {
                     this.charError("UTF-8");
                  }

                  return (var1 & 31) << 12 | (var2 & 63) << 6 | var3 & 31;
               case 4:
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  var2 = this.buffer[this.position++];
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  var3 = this.buffer[this.position++];
                  if (this.position == this.count) {
                     this.fillBuffer();
                  }

                  if (this.count == -1) {
                     this.endOfStreamError("UTF-8");
                  }

                  byte var4 = this.buffer[this.position++];
                  if ((var2 & 192) != 128 || (var3 & 192) != 128 || (var4 & 192) != 128) {
                     this.charError("UTF-8");
                  }

                  int var5 = (var1 & 31) << 18 | (var2 & 63) << 12 | (var3 & 31) << 6 | var4 & 31;
                  this.nextChar = (var5 - 65536) % 1024 + '\udc00';
                  return (var5 - 65536) / 1024 + '\ud800';
               default:
                  this.charError("UTF-8");
               case 1:
                  return var1;
            }
         }
      }
   }
}
