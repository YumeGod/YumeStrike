package org.apache.regexp;

import java.io.IOException;
import java.io.InputStream;

public final class StreamCharacterIterator implements CharacterIterator {
   private final InputStream is;
   private final StringBuffer buff;
   private boolean closed;

   public StreamCharacterIterator(InputStream var1) {
      this.is = var1;
      this.buff = new StringBuffer(512);
      this.closed = false;
   }

   public char charAt(int var1) {
      try {
         this.ensure(var1);
         return this.buff.charAt(var1);
      } catch (IOException var3) {
         throw new StringIndexOutOfBoundsException(var3.getMessage());
      }
   }

   private void ensure(int var1) throws IOException {
      if (!this.closed) {
         if (var1 >= this.buff.length()) {
            this.read(var1 + 1 - this.buff.length());
         }
      }
   }

   public boolean isEnd(int var1) {
      if (this.buff.length() > var1) {
         return false;
      } else {
         try {
            this.ensure(var1);
            return this.buff.length() <= var1;
         } catch (IOException var3) {
            throw new StringIndexOutOfBoundsException(var3.getMessage());
         }
      }
   }

   private int read(int var1) throws IOException {
      if (this.closed) {
         return 0;
      } else {
         int var3 = var1;

         while(true) {
            --var3;
            if (var3 < 0) {
               break;
            }

            int var2 = this.is.read();
            if (var2 < 0) {
               this.closed = true;
               break;
            }

            this.buff.append((char)var2);
         }

         return var1 - var3;
      }
   }

   private void readAll() throws IOException {
      while(!this.closed) {
         this.read(1000);
      }

   }

   public String substring(int var1) {
      try {
         this.readAll();
         return this.buff.toString().substring(var1);
      } catch (IOException var3) {
         throw new StringIndexOutOfBoundsException(var3.getMessage());
      }
   }

   public String substring(int var1, int var2) {
      try {
         this.ensure(var1 + var2);
         return this.buff.toString().substring(var1, var2);
      } catch (IOException var4) {
         throw new StringIndexOutOfBoundsException(var4.getMessage());
      }
   }
}
