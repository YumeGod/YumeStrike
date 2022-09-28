package org.apache.fop.afp.util;

import java.io.IOException;
import java.io.InputStream;

public class StructuredFieldReader {
   private InputStream inputStream = null;

   public StructuredFieldReader(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public byte[] getNext(byte[] identifier) throws IOException {
      int bufferPointer = 0;
      byte[] bufferData = new byte[identifier.length + 2];

      int c;
      for(c = 0; c < identifier.length; ++c) {
         bufferData[c] = 0;
      }

      while((c = this.inputStream.read()) > -1) {
         bufferData[bufferPointer] = (byte)c;
         int index = 0;
         boolean found = true;

         int a;
         for(int i = identifier.length - 1; i > -1; --i) {
            a = bufferPointer - index;
            if (a < 0) {
               a += bufferData.length;
            }

            ++index;
            if (identifier[i] != bufferData[a]) {
               found = false;
               break;
            }
         }

         if (found) {
            byte[] length = new byte[2];
            a = bufferPointer - identifier.length;
            if (a < 0) {
               a += bufferData.length;
            }

            int b = bufferPointer - identifier.length - 1;
            if (b < 0) {
               b += bufferData.length;
            }

            length[0] = bufferData[b];
            length[1] = bufferData[a];
            int reclength = ((length[0] & 255) << 8) + (length[1] & 255) - identifier.length - 2;
            byte[] retval = new byte[reclength];
            this.inputStream.read(retval, 0, reclength);
            return retval;
         }

         ++bufferPointer;
         if (bufferPointer >= bufferData.length) {
            bufferPointer = 0;
         }
      }

      return null;
   }
}
