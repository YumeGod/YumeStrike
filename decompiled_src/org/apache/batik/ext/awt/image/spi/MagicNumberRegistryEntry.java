package org.apache.batik.ext.awt.image.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;

public abstract class MagicNumberRegistryEntry extends AbstractRegistryEntry implements StreamRegistryEntry {
   public static final float PRIORITY = 1000.0F;
   MagicNumber[] magicNumbers;

   public MagicNumberRegistryEntry(String var1, float var2, String var3, String var4, int var5, byte[] var6) {
      super(var1, var2, var3, var4);
      this.magicNumbers = new MagicNumber[1];
      this.magicNumbers[0] = new MagicNumber(var5, var6);
   }

   public MagicNumberRegistryEntry(String var1, String var2, String var3, int var4, byte[] var5) {
      this(var1, 1000.0F, var2, var3, var4, var5);
   }

   public MagicNumberRegistryEntry(String var1, float var2, String var3, String var4, MagicNumber[] var5) {
      super(var1, var2, var3, var4);
      this.magicNumbers = var5;
   }

   public MagicNumberRegistryEntry(String var1, String var2, String var3, MagicNumber[] var4) {
      this(var1, 1000.0F, var2, var3, var4);
   }

   public MagicNumberRegistryEntry(String var1, float var2, String[] var3, String[] var4, int var5, byte[] var6) {
      super(var1, var2, var3, var4);
      this.magicNumbers = new MagicNumber[1];
      this.magicNumbers[0] = new MagicNumber(var5, var6);
   }

   public MagicNumberRegistryEntry(String var1, String[] var2, String[] var3, int var4, byte[] var5) {
      this(var1, 1000.0F, var2, var3, var4, var5);
   }

   public MagicNumberRegistryEntry(String var1, float var2, String[] var3, String[] var4, MagicNumber[] var5) {
      super(var1, var2, var3, var4);
      this.magicNumbers = var5;
   }

   public MagicNumberRegistryEntry(String var1, String[] var2, String[] var3, MagicNumber[] var4) {
      this(var1, 1000.0F, var2, var3, var4);
   }

   public MagicNumberRegistryEntry(String var1, String[] var2, String[] var3, MagicNumber[] var4, float var5) {
      super(var1, var5, var2, var3);
      this.magicNumbers = var4;
   }

   public int getReadlimit() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.magicNumbers.length; ++var2) {
         int var3 = this.magicNumbers[var2].getReadlimit();
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   public boolean isCompatibleStream(InputStream var1) throws StreamCorruptedException {
      for(int var2 = 0; var2 < this.magicNumbers.length; ++var2) {
         if (this.magicNumbers[var2].isMatch(var1)) {
            return true;
         }
      }

      return false;
   }

   public static class MagicNumber {
      int offset;
      byte[] magicNumber;
      byte[] buffer;

      public MagicNumber(int var1, byte[] var2) {
         this.offset = var1;
         this.magicNumber = (byte[])var2.clone();
         this.buffer = new byte[var2.length];
      }

      int getReadlimit() {
         return this.offset + this.magicNumber.length;
      }

      boolean isMatch(InputStream var1) throws StreamCorruptedException {
         int var2 = 0;
         var1.mark(this.getReadlimit());

         try {
            boolean var4;
            try {
               int var3;
               while(var2 < this.offset) {
                  var3 = (int)var1.skip((long)(this.offset - var2));
                  if (var3 == -1) {
                     var4 = false;
                     return var4;
                  }

                  var2 += var3;
               }

               for(var2 = 0; var2 < this.buffer.length; var2 += var3) {
                  var3 = var1.read(this.buffer, var2, this.buffer.length - var2);
                  if (var3 == -1) {
                     var4 = false;
                     return var4;
                  }
               }

               for(var3 = 0; var3 < this.magicNumber.length; ++var3) {
                  if (this.magicNumber[var3] != this.buffer[var3]) {
                     var4 = false;
                     return var4;
                  }
               }

               return true;
            } catch (IOException var17) {
               var4 = false;
               return var4;
            }
         } finally {
            try {
               var1.reset();
            } catch (IOException var16) {
               throw new StreamCorruptedException(var16.getMessage());
            }
         }
      }
   }
}
