package org.apache.batik.ext.awt.image.codec.tiff;

public class TIFFLZWDecoder {
   byte[][] stringTable;
   byte[] data = null;
   byte[] uncompData;
   int tableIndex;
   int bitsToGet = 9;
   int bytePointer;
   int bitPointer;
   int dstIndex;
   int w;
   int h;
   int predictor;
   int samplesPerPixel;
   int nextData = 0;
   int nextBits = 0;
   int[] andTable = new int[]{511, 1023, 2047, 4095};

   public TIFFLZWDecoder(int var1, int var2, int var3) {
      this.w = var1;
      this.predictor = var2;
      this.samplesPerPixel = var3;
   }

   public byte[] decode(byte[] var1, byte[] var2, int var3) {
      if (var1[0] == 0 && var1[1] == 1) {
         throw new UnsupportedOperationException("TIFFLZWDecoder0");
      } else {
         this.initializeStringTable();
         this.data = var1;
         this.h = var3;
         this.uncompData = var2;
         this.bytePointer = 0;
         this.bitPointer = 0;
         this.dstIndex = 0;
         this.nextData = 0;
         this.nextBits = 0;
         int var5 = 0;

         int var4;
         while((var4 = this.getNextCode()) != 257 && this.dstIndex != var2.length) {
            if (var4 == 256) {
               this.initializeStringTable();
               var4 = this.getNextCode();
               if (var4 == 257) {
                  break;
               }

               this.writeString(this.stringTable[var4]);
               var5 = var4;
            } else {
               byte[] var6;
               if (var4 < this.tableIndex) {
                  var6 = this.stringTable[var4];
                  this.writeString(var6);
                  this.addStringToTable(this.stringTable[var5], var6[0]);
                  var5 = var4;
               } else {
                  var6 = this.stringTable[var5];
                  var6 = this.composeString(var6, var6[0]);
                  this.writeString(var6);
                  this.addStringToTable(var6);
                  var5 = var4;
               }
            }
         }

         if (this.predictor == 2) {
            for(int var8 = 0; var8 < var3; ++var8) {
               int var7 = this.samplesPerPixel * (var8 * this.w + 1);

               for(int var9 = this.samplesPerPixel; var9 < this.w * this.samplesPerPixel; ++var9) {
                  var2[var7] += var2[var7 - this.samplesPerPixel];
                  ++var7;
               }
            }
         }

         return var2;
      }
   }

   public void initializeStringTable() {
      this.stringTable = new byte[4096][];

      for(int var1 = 0; var1 < 256; ++var1) {
         this.stringTable[var1] = new byte[1];
         this.stringTable[var1][0] = (byte)var1;
      }

      this.tableIndex = 258;
      this.bitsToGet = 9;
   }

   public void writeString(byte[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.uncompData[this.dstIndex++] = var1[var2];
      }

   }

   public void addStringToTable(byte[] var1, byte var2) {
      int var3 = var1.length;
      byte[] var4 = new byte[var3 + 1];
      System.arraycopy(var1, 0, var4, 0, var3);
      var4[var3] = var2;
      this.stringTable[this.tableIndex++] = var4;
      if (this.tableIndex == 511) {
         this.bitsToGet = 10;
      } else if (this.tableIndex == 1023) {
         this.bitsToGet = 11;
      } else if (this.tableIndex == 2047) {
         this.bitsToGet = 12;
      }

   }

   public void addStringToTable(byte[] var1) {
      this.stringTable[this.tableIndex++] = var1;
      if (this.tableIndex == 511) {
         this.bitsToGet = 10;
      } else if (this.tableIndex == 1023) {
         this.bitsToGet = 11;
      } else if (this.tableIndex == 2047) {
         this.bitsToGet = 12;
      }

   }

   public byte[] composeString(byte[] var1, byte var2) {
      int var3 = var1.length;
      byte[] var4 = new byte[var3 + 1];
      System.arraycopy(var1, 0, var4, 0, var3);
      var4[var3] = var2;
      return var4;
   }

   public int getNextCode() {
      try {
         this.nextData = this.nextData << 8 | this.data[this.bytePointer++] & 255;
         this.nextBits += 8;
         if (this.nextBits < this.bitsToGet) {
            this.nextData = this.nextData << 8 | this.data[this.bytePointer++] & 255;
            this.nextBits += 8;
         }

         int var1 = this.nextData >> this.nextBits - this.bitsToGet & this.andTable[this.bitsToGet - 9];
         this.nextBits -= this.bitsToGet;
         return var1;
      } catch (ArrayIndexOutOfBoundsException var2) {
         return 257;
      }
   }
}
