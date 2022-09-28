package org.apache.batik.util.io;

import java.io.IOException;
import java.io.Reader;

public abstract class NormalizingReader extends Reader {
   public int read(char[] var1, int var2, int var3) throws IOException {
      if (var3 == 0) {
         return 0;
      } else {
         int var4 = this.read();
         if (var4 == -1) {
            return -1;
         } else {
            int var5 = 0;

            do {
               var1[var5 + var2] = (char)var4;
               ++var5;
               var4 = this.read();
            } while(var4 != -1 && var5 < var3);

            return var5;
         }
      }
   }

   public abstract int getLine();

   public abstract int getColumn();
}
