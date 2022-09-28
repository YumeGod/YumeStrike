package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;

public class UTF16Decoder extends AbstractCharDecoder {
   protected boolean bigEndian;

   public UTF16Decoder(InputStream var1) throws IOException {
      super(var1);
      int var2 = var1.read();
      if (var2 == -1) {
         this.endOfStreamError("UTF-16");
      }

      int var3 = var1.read();
      if (var3 == -1) {
         this.endOfStreamError("UTF-16");
      }

      int var4 = (var2 & 255) << 8 | var3 & 255;
      switch (var4) {
         case 65279:
            this.bigEndian = true;
         case 65534:
            break;
         default:
            this.charError("UTF-16");
      }

   }

   public UTF16Decoder(InputStream var1, boolean var2) {
      super(var1);
      this.bigEndian = var2;
   }

   public int readChar() throws IOException {
      if (this.position == this.count) {
         this.fillBuffer();
      }

      if (this.count == -1) {
         return -1;
      } else {
         byte var1 = this.buffer[this.position++];
         if (this.position == this.count) {
            this.fillBuffer();
         }

         if (this.count == -1) {
            this.endOfStreamError("UTF-16");
         }

         byte var2 = this.buffer[this.position++];
         int var3 = this.bigEndian ? (var1 & 255) << 8 | var2 & 255 : (var2 & 255) << 8 | var1 & 255;
         if (var3 == 65534) {
            this.charError("UTF-16");
         }

         return var3;
      }
   }
}
