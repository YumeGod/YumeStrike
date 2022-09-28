package org.apache.xmlgraphics.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ASCII85InputStream extends InputStream implements ASCII85Constants {
   private InputStream in;
   private boolean eodReached = false;
   private int[] b = new int[4];
   private int bSize = 0;
   private int bIndex = 0;

   public ASCII85InputStream(InputStream in) {
      this.in = in;
   }

   public int read() throws IOException {
      if (this.bIndex >= this.bSize) {
         if (this.eodReached) {
            return -1;
         }

         this.readNextTuple();
         if (this.bSize == 0) {
            if (!this.eodReached) {
               throw new IllegalStateException("Internal error");
            }

            return -1;
         }
      }

      int result = this.b[this.bIndex];
      result = result < 0 ? 256 + result : result;
      ++this.bIndex;
      return result;
   }

   private int filteredRead() throws IOException {
      while(true) {
         int buf = this.in.read();
         switch (buf) {
            case 0:
            case 9:
            case 10:
            case 12:
            case 13:
            case 32:
               break;
            case 122:
            case 126:
               return buf;
            default:
               if (buf >= 33 && buf <= 117) {
                  return buf;
               }

               throw new IOException("Illegal character detected: " + buf);
         }
      }
   }

   private void handleEOD() throws IOException {
      int buf = this.in.read();
      if (buf != EOD[1]) {
         throw new IOException("'>' expected after '~' (EOD)");
      } else {
         this.eodReached = true;
         this.bSize = 0;
         this.bIndex = 0;
      }
   }

   private void readNextTuple() throws IOException {
      long tuple = 0L;
      int buf = this.filteredRead();
      if (buf == 122) {
         Arrays.fill(this.b, 0);
         this.bSize = 4;
         this.bIndex = 0;
      } else if (buf == EOD[0]) {
         this.handleEOD();
      } else {
         int cIndex = 0;
         tuple = (long)(buf - 33) * POW85[cIndex];
         ++cIndex;

         while(cIndex < 5) {
            buf = this.filteredRead();
            if (buf == EOD[0]) {
               this.handleEOD();
               break;
            }

            if (buf == 122) {
               throw new IOException("Illegal 'z' within tuple");
            }

            tuple += (long)(buf - 33) * POW85[cIndex];
            ++cIndex;
         }

         int cSize = cIndex;
         if (cIndex == 1) {
            throw new IOException("Only one character in tuple");
         }

         while(cIndex < 5) {
            tuple += POW85[cIndex - 1];
            ++cIndex;
         }

         if (tuple > 4294967295L) {
            throw new IOException("Illegal tuple (> 2^32 - 1)");
         }

         this.b[0] = (byte)((int)(tuple >> 24 & 255L));
         this.b[1] = (byte)((int)(tuple >> 16 & 255L));
         this.b[2] = (byte)((int)(tuple >> 8 & 255L));
         this.b[3] = (byte)((int)(tuple & 255L));
         this.bSize = cSize - 1;
         this.bIndex = 0;
      }

   }
}
