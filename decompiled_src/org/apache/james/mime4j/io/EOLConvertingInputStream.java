package org.apache.james.mime4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class EOLConvertingInputStream extends InputStream {
   public static final int CONVERT_CR = 1;
   public static final int CONVERT_LF = 2;
   public static final int CONVERT_BOTH = 3;
   private PushbackInputStream in;
   private int previous;
   private int flags;

   public EOLConvertingInputStream(InputStream in) {
      this(in, 3);
   }

   public EOLConvertingInputStream(InputStream in, int flags) {
      this.in = null;
      this.previous = 0;
      this.flags = 3;
      this.in = new PushbackInputStream(in, 2);
      this.flags = flags;
   }

   public void close() throws IOException {
      this.in.close();
   }

   public int read() throws IOException {
      int b = this.in.read();
      if (b == -1) {
         return -1;
      } else {
         if ((this.flags & 1) != 0 && b == 13) {
            int c = this.in.read();
            if (c != -1) {
               this.in.unread(c);
            }

            if (c != 10) {
               this.in.unread(10);
            }
         } else if ((this.flags & 2) != 0 && b == 10 && this.previous != 13) {
            b = 13;
            this.in.unread(10);
         }

         this.previous = b;
         return b;
      }
   }
}
