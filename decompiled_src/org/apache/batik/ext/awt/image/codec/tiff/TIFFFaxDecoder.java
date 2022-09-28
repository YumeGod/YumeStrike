package org.apache.batik.ext.awt.image.codec.tiff;

class TIFFFaxDecoder {
   private int bitPointer;
   private int bytePointer;
   private byte[] data;
   private int w;
   private int h;
   private int fillOrder;
   private int changingElemSize = 0;
   private int[] prevChangingElems;
   private int[] currChangingElems;
   private int lastChangingElement = 0;
   private int compression = 2;
   private int uncompressedMode = 0;
   private int fillBits = 0;
   private int oneD;
   static int[] table1 = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255};
   static int[] table2 = new int[]{0, 128, 192, 224, 240, 248, 252, 254, 255};
   static byte[] flipTable = new byte[]{0, -128, 64, -64, 32, -96, 96, -32, 16, -112, 80, -48, 48, -80, 112, -16, 8, -120, 72, -56, 40, -88, 104, -24, 24, -104, 88, -40, 56, -72, 120, -8, 4, -124, 68, -60, 36, -92, 100, -28, 20, -108, 84, -44, 52, -76, 116, -12, 12, -116, 76, -52, 44, -84, 108, -20, 28, -100, 92, -36, 60, -68, 124, -4, 2, -126, 66, -62, 34, -94, 98, -30, 18, -110, 82, -46, 50, -78, 114, -14, 10, -118, 74, -54, 42, -86, 106, -22, 26, -102, 90, -38, 58, -70, 122, -6, 6, -122, 70, -58, 38, -90, 102, -26, 22, -106, 86, -42, 54, -74, 118, -10, 14, -114, 78, -50, 46, -82, 110, -18, 30, -98, 94, -34, 62, -66, 126, -2, 1, -127, 65, -63, 33, -95, 97, -31, 17, -111, 81, -47, 49, -79, 113, -15, 9, -119, 73, -55, 41, -87, 105, -23, 25, -103, 89, -39, 57, -71, 121, -7, 5, -123, 69, -59, 37, -91, 101, -27, 21, -107, 85, -43, 53, -75, 117, -11, 13, -115, 77, -51, 45, -83, 109, -19, 29, -99, 93, -35, 61, -67, 125, -3, 3, -125, 67, -61, 35, -93, 99, -29, 19, -109, 83, -45, 51, -77, 115, -13, 11, -117, 75, -53, 43, -85, 107, -21, 27, -101, 91, -37, 59, -69, 123, -5, 7, -121, 71, -57, 39, -89, 103, -25, 23, -105, 87, -41, 55, -73, 119, -9, 15, -113, 79, -49, 47, -81, 111, -17, 31, -97, 95, -33, 63, -65, 127, -1};
   static short[] white = new short[]{6430, 6400, 6400, 6400, 3225, 3225, 3225, 3225, 944, 944, 944, 944, 976, 976, 976, 976, 1456, 1456, 1456, 1456, 1488, 1488, 1488, 1488, 718, 718, 718, 718, 718, 718, 718, 718, 750, 750, 750, 750, 750, 750, 750, 750, 1520, 1520, 1520, 1520, 1552, 1552, 1552, 1552, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 654, 654, 654, 654, 654, 654, 654, 654, 1072, 1072, 1072, 1072, 1104, 1104, 1104, 1104, 1136, 1136, 1136, 1136, 1168, 1168, 1168, 1168, 1200, 1200, 1200, 1200, 1232, 1232, 1232, 1232, 622, 622, 622, 622, 622, 622, 622, 622, 1008, 1008, 1008, 1008, 1040, 1040, 1040, 1040, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 1712, 1712, 1712, 1712, 1744, 1744, 1744, 1744, 846, 846, 846, 846, 846, 846, 846, 846, 1264, 1264, 1264, 1264, 1296, 1296, 1296, 1296, 1328, 1328, 1328, 1328, 1360, 1360, 1360, 1360, 1392, 1392, 1392, 1392, 1424, 1424, 1424, 1424, 686, 686, 686, 686, 686, 686, 686, 686, 910, 910, 910, 910, 910, 910, 910, 910, 1968, 1968, 1968, 1968, 2000, 2000, 2000, 2000, 2032, 2032, 2032, 2032, 16, 16, 16, 16, 10257, 10257, 10257, 10257, 12305, 12305, 12305, 12305, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 878, 878, 878, 878, 878, 878, 878, 878, 1904, 1904, 1904, 1904, 1936, 1936, 1936, 1936, -18413, -18413, -16365, -16365, -14317, -14317, -10221, -10221, 590, 590, 590, 590, 590, 590, 590, 590, 782, 782, 782, 782, 782, 782, 782, 782, 1584, 1584, 1584, 1584, 1616, 1616, 1616, 1616, 1648, 1648, 1648, 1648, 1680, 1680, 1680, 1680, 814, 814, 814, 814, 814, 814, 814, 814, 1776, 1776, 1776, 1776, 1808, 1808, 1808, 1808, 1840, 1840, 1840, 1840, 1872, 1872, 1872, 1872, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, 14353, 14353, 14353, 14353, 16401, 16401, 16401, 16401, 22547, 22547, 24595, 24595, 20497, 20497, 20497, 20497, 18449, 18449, 18449, 18449, 26643, 26643, 28691, 28691, 30739, 30739, -32749, -32749, -30701, -30701, -28653, -28653, -26605, -26605, -24557, -24557, -22509, -22509, -20461, -20461, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232};
   static short[] additionalMakeup = new short[]{28679, 28679, 31752, -32759, -31735, -30711, -29687, -28663, 29703, 29703, 30727, 30727, -27639, -26615, -25591, -24567};
   static short[] initBlack = new short[]{3226, 6412, 200, 168, 38, 38, 134, 134, 100, 100, 100, 100, 68, 68, 68, 68};
   static short[] twoBitBlack = new short[]{292, 260, 226, 226};
   static short[] black = new short[]{62, 62, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 588, 588, 588, 588, 588, 588, 588, 588, 1680, 1680, 20499, 22547, 24595, 26643, 1776, 1776, 1808, 1808, -24557, -22509, -20461, -18413, 1904, 1904, 1936, 1936, -16365, -14317, 782, 782, 782, 782, 814, 814, 814, 814, -12269, -10221, 10257, 10257, 12305, 12305, 14353, 14353, 16403, 18451, 1712, 1712, 1744, 1744, 28691, 30739, -32749, -30701, -28653, -26605, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 750, 750, 750, 750, 1616, 1616, 1648, 1648, 1424, 1424, 1456, 1456, 1488, 1488, 1520, 1520, 1840, 1840, 1872, 1872, 1968, 1968, 8209, 8209, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 1552, 1552, 1584, 1584, 2000, 2000, 2032, 2032, 976, 976, 1008, 1008, 1040, 1040, 1072, 1072, 1296, 1296, 1328, 1328, 718, 718, 718, 718, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 4113, 4113, 6161, 6161, 848, 848, 880, 880, 912, 912, 944, 944, 622, 622, 622, 622, 654, 654, 654, 654, 1104, 1104, 1136, 1136, 1168, 1168, 1200, 1200, 1232, 1232, 1264, 1264, 686, 686, 686, 686, 1360, 1360, 1392, 1392, 12, 12, 12, 12, 12, 12, 12, 12, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390};
   static byte[] twoDCodes = new byte[]{80, 88, 23, 71, 30, 30, 62, 62, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41};

   public TIFFFaxDecoder(int var1, int var2, int var3) {
      this.fillOrder = var1;
      this.w = var2;
      this.h = var3;
      this.bitPointer = 0;
      this.bytePointer = 0;
      this.prevChangingElems = new int[var2];
      this.currChangingElems = new int[var2];
   }

   public void decode1D(byte[] var1, byte[] var2, int var3, int var4) {
      this.data = var2;
      int var5 = 0;
      int var6 = (this.w + 7) / 8;
      this.bitPointer = 0;
      this.bytePointer = 0;

      for(int var7 = 0; var7 < var4; ++var7) {
         this.decodeNextScanline(var1, var5, var3);
         var5 += var6;
      }

   }

   public void decodeNextScanline(byte[] var1, int var2, int var3) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var10 = true;
      this.changingElemSize = 0;

      while(var3 < this.w) {
         int var7;
         short var8;
         int var11;
         int var12;
         int var13;
         while(var10) {
            var7 = this.nextNBits(10);
            var8 = white[var7];
            var13 = var8 & 1;
            var11 = var8 >>> 1 & 15;
            if (var11 == 12) {
               int var9 = this.nextLesserThan8Bits(2);
               var7 = var7 << 2 & 12 | var9;
               var8 = additionalMakeup[var7];
               var11 = var8 >>> 1 & 7;
               var12 = var8 >>> 4 & 4095;
               var3 += var12;
               this.updatePointer(4 - var11);
            } else {
               if (var11 == 0) {
                  throw new Error("TIFFFaxDecoder0");
               }

               if (var11 == 15) {
                  throw new Error("TIFFFaxDecoder1");
               }

               var12 = var8 >>> 5 & 2047;
               var3 += var12;
               this.updatePointer(10 - var11);
               if (var13 == 0) {
                  var10 = false;
                  this.currChangingElems[this.changingElemSize++] = var3;
               }
            }
         }

         if (var3 == this.w) {
            if (this.compression == 2) {
               this.advancePointer();
            }
            break;
         }

         while(!var10) {
            var7 = this.nextLesserThan8Bits(4);
            var8 = initBlack[var7];
            var13 = var8 & 1;
            var11 = var8 >>> 1 & 15;
            var12 = var8 >>> 5 & 2047;
            if (var12 == 100) {
               var7 = this.nextNBits(9);
               var8 = black[var7];
               var13 = var8 & 1;
               var11 = var8 >>> 1 & 15;
               var12 = var8 >>> 5 & 2047;
               if (var11 == 12) {
                  this.updatePointer(5);
                  var7 = this.nextLesserThan8Bits(4);
                  var8 = additionalMakeup[var7];
                  var11 = var8 >>> 1 & 7;
                  var12 = var8 >>> 4 & 4095;
                  this.setToBlack(var1, var2, var3, var12);
                  var3 += var12;
                  this.updatePointer(4 - var11);
               } else {
                  if (var11 == 15) {
                     throw new Error("TIFFFaxDecoder2");
                  }

                  this.setToBlack(var1, var2, var3, var12);
                  var3 += var12;
                  this.updatePointer(9 - var11);
                  if (var13 == 0) {
                     var10 = true;
                     this.currChangingElems[this.changingElemSize++] = var3;
                  }
               }
            } else if (var12 == 200) {
               var7 = this.nextLesserThan8Bits(2);
               var8 = twoBitBlack[var7];
               var12 = var8 >>> 5 & 2047;
               var11 = var8 >>> 1 & 15;
               this.setToBlack(var1, var2, var3, var12);
               var3 += var12;
               this.updatePointer(2 - var11);
               var10 = true;
               this.currChangingElems[this.changingElemSize++] = var3;
            } else {
               this.setToBlack(var1, var2, var3, var12);
               var3 += var12;
               this.updatePointer(4 - var11);
               var10 = true;
               this.currChangingElems[this.changingElemSize++] = var3;
            }
         }

         if (var3 == this.w) {
            if (this.compression == 2) {
               this.advancePointer();
            }
            break;
         }
      }

      this.currChangingElems[this.changingElemSize++] = var3;
   }

   public void decode2D(byte[] var1, byte[] var2, int var3, int var4, long var5) {
      this.data = var2;
      this.compression = 3;
      this.bitPointer = 0;
      this.bytePointer = 0;
      int var7 = (this.w + 7) / 8;
      int[] var12 = new int[2];
      boolean var17 = false;
      this.oneD = (int)(var5 & 1L);
      this.uncompressedMode = (int)((var5 & 2L) >> 1);
      this.fillBits = (int)((var5 & 4L) >> 2);
      if (this.readEOL() != 1) {
         throw new Error("TIFFFaxDecoder3");
      } else {
         int var19 = 0;
         this.decodeNextScanline(var1, var19, var3);
         var19 += var7;

         for(int var21 = 1; var21 < var4; ++var21) {
            if (this.readEOL() != 0) {
               this.decodeNextScanline(var1, var19, var3);
            } else {
               int[] var18 = this.prevChangingElems;
               this.prevChangingElems = this.currChangingElems;
               this.currChangingElems = var18;
               int var23 = 0;
               int var8 = -1;
               boolean var16 = true;
               int var20 = var3;
               this.lastChangingElement = 0;

               while(var20 < this.w) {
                  this.getNextChangingElement(var8, var16, var12);
                  int var10 = var12[0];
                  int var11 = var12[1];
                  int var13 = this.nextLesserThan8Bits(7);
                  var13 = twoDCodes[var13] & 255;
                  int var14 = (var13 & 120) >>> 3;
                  int var15 = var13 & 7;
                  if (var14 == 0) {
                     if (!var16) {
                        this.setToBlack(var1, var19, var20, var11 - var20);
                     }

                     var8 = var11;
                     var20 = var11;
                     this.updatePointer(7 - var15);
                  } else if (var14 == 1) {
                     this.updatePointer(7 - var15);
                     int var22;
                     if (var16) {
                        var22 = this.decodeWhiteCodeWord();
                        var20 += var22;
                        this.currChangingElems[var23++] = var20;
                        var22 = this.decodeBlackCodeWord();
                        this.setToBlack(var1, var19, var20, var22);
                        var20 += var22;
                        this.currChangingElems[var23++] = var20;
                     } else {
                        var22 = this.decodeBlackCodeWord();
                        this.setToBlack(var1, var19, var20, var22);
                        var20 += var22;
                        this.currChangingElems[var23++] = var20;
                        var22 = this.decodeWhiteCodeWord();
                        var20 += var22;
                        this.currChangingElems[var23++] = var20;
                     }

                     var8 = var20;
                  } else {
                     if (var14 > 8) {
                        throw new Error("TIFFFaxDecoder4");
                     }

                     int var9 = var10 + (var14 - 5);
                     this.currChangingElems[var23++] = var9;
                     if (!var16) {
                        this.setToBlack(var1, var19, var20, var9 - var20);
                     }

                     var8 = var9;
                     var20 = var9;
                     var16 = !var16;
                     this.updatePointer(7 - var15);
                  }
               }

               this.currChangingElems[var23++] = var20;
               this.changingElemSize = var23;
            }

            var19 += var7;
         }

      }
   }

   public synchronized void decodeT6(byte[] var1, byte[] var2, int var3, int var4, long var5) {
      this.data = var2;
      this.compression = 4;
      this.bitPointer = 0;
      this.bytePointer = 0;
      int var7 = (this.w + 7) / 8;
      int[] var18 = new int[2];
      this.uncompressedMode = (int)((var5 & 2L) >> 1);
      int[] var19 = this.currChangingElems;
      this.changingElemSize = 0;
      var19[this.changingElemSize++] = this.w;
      var19[this.changingElemSize++] = this.w;
      int var20 = 0;

      for(int var22 = 0; var22 < var4; ++var22) {
         int var8 = -1;
         boolean var15 = true;
         int[] var17 = this.prevChangingElems;
         this.prevChangingElems = this.currChangingElems;
         var19 = this.currChangingElems = var17;
         int var16 = 0;
         int var21 = var3;
         this.lastChangingElement = 0;

         while(true) {
            while(var21 < this.w) {
               this.getNextChangingElement(var8, var15, var18);
               int var10 = var18[0];
               int var11 = var18[1];
               int var12 = this.nextLesserThan8Bits(7);
               var12 = twoDCodes[var12] & 255;
               int var13 = (var12 & 120) >>> 3;
               int var14 = var12 & 7;
               if (var13 == 0) {
                  if (!var15) {
                     this.setToBlack(var1, var20, var21, var11 - var21);
                  }

                  var8 = var11;
                  var21 = var11;
                  this.updatePointer(7 - var14);
               } else {
                  int var23;
                  if (var13 == 1) {
                     this.updatePointer(7 - var14);
                     if (var15) {
                        var23 = this.decodeWhiteCodeWord();
                        var21 += var23;
                        var19[var16++] = var21;
                        var23 = this.decodeBlackCodeWord();
                        this.setToBlack(var1, var20, var21, var23);
                        var21 += var23;
                        var19[var16++] = var21;
                     } else {
                        var23 = this.decodeBlackCodeWord();
                        this.setToBlack(var1, var20, var21, var23);
                        var21 += var23;
                        var19[var16++] = var21;
                        var23 = this.decodeWhiteCodeWord();
                        var21 += var23;
                        var19[var16++] = var21;
                     }

                     var8 = var21;
                  } else if (var13 <= 8) {
                     int var9 = var10 + (var13 - 5);
                     var19[var16++] = var9;
                     if (!var15) {
                        this.setToBlack(var1, var20, var21, var9 - var21);
                     }

                     var8 = var9;
                     var21 = var9;
                     var15 = !var15;
                     this.updatePointer(7 - var14);
                  } else {
                     if (var13 != 11) {
                        throw new Error("TIFFFaxDecoder5");
                     }

                     if (this.nextLesserThan8Bits(3) != 7) {
                        throw new Error("TIFFFaxDecoder5");
                     }

                     var23 = 0;
                     boolean var24 = false;

                     while(!var24) {
                        while(this.nextLesserThan8Bits(1) != 1) {
                           ++var23;
                        }

                        if (var23 > 5) {
                           var23 -= 6;
                           if (!var15 && var23 > 0) {
                              var19[var16++] = var21;
                           }

                           var21 += var23;
                           if (var23 > 0) {
                              var15 = true;
                           }

                           if (this.nextLesserThan8Bits(1) == 0) {
                              if (!var15) {
                                 var19[var16++] = var21;
                              }

                              var15 = true;
                           } else {
                              if (var15) {
                                 var19[var16++] = var21;
                              }

                              var15 = false;
                           }

                           var24 = true;
                        }

                        if (var23 == 5) {
                           if (!var15) {
                              var19[var16++] = var21;
                           }

                           var21 += var23;
                           var15 = true;
                        } else {
                           var21 += var23;
                           var19[var16++] = var21;
                           this.setToBlack(var1, var20, var21, 1);
                           ++var21;
                           var15 = false;
                        }
                     }
                  }
               }
            }

            var19[var16++] = var21;
            this.changingElemSize = var16;
            var20 += var7;
            break;
         }
      }

   }

   private void setToBlack(byte[] var1, int var2, int var3, int var4) {
      int var5 = 8 * var2 + var3;
      int var6 = var5 + var4;
      int var7 = var5 >> 3;
      int var8 = var5 & 7;
      if (var8 > 0) {
         int var9 = 1 << 7 - var8;

         byte var10;
         for(var10 = var1[var7]; var9 > 0 && var5 < var6; ++var5) {
            var10 = (byte)(var10 | var9);
            var9 >>= 1;
         }

         var1[var7] = var10;
      }

      for(var7 = var5 >> 3; var5 < var6 - 7; var5 += 8) {
         var1[var7++] = -1;
      }

      while(var5 < var6) {
         var7 = var5 >> 3;
         var1[var7] = (byte)(var1[var7] | 1 << 7 - (var5 & 7));
         ++var5;
      }

   }

   private int decodeWhiteCodeWord() {
      boolean var6 = true;
      int var7 = 0;
      boolean var8 = true;

      while(var8) {
         int var1 = this.nextNBits(10);
         short var2 = white[var1];
         int var4 = var2 & 1;
         int var3 = var2 >>> 1 & 15;
         int var9;
         if (var3 == 12) {
            int var5 = this.nextLesserThan8Bits(2);
            var1 = var1 << 2 & 12 | var5;
            var2 = additionalMakeup[var1];
            var3 = var2 >>> 1 & 7;
            var9 = var2 >>> 4 & 4095;
            var7 += var9;
            this.updatePointer(4 - var3);
         } else {
            if (var3 == 0) {
               throw new Error("TIFFFaxDecoder0");
            }

            if (var3 == 15) {
               throw new Error("TIFFFaxDecoder1");
            }

            var9 = var2 >>> 5 & 2047;
            var7 += var9;
            this.updatePointer(10 - var3);
            if (var4 == 0) {
               var8 = false;
            }
         }
      }

      return var7;
   }

   private int decodeBlackCodeWord() {
      boolean var5 = true;
      int var6 = 0;
      boolean var7 = false;

      while(!var7) {
         int var1 = this.nextLesserThan8Bits(4);
         short var2 = initBlack[var1];
         int var4 = var2 & 1;
         int var3 = var2 >>> 1 & 15;
         int var8 = var2 >>> 5 & 2047;
         if (var8 == 100) {
            var1 = this.nextNBits(9);
            var2 = black[var1];
            var4 = var2 & 1;
            var3 = var2 >>> 1 & 15;
            var8 = var2 >>> 5 & 2047;
            if (var3 == 12) {
               this.updatePointer(5);
               var1 = this.nextLesserThan8Bits(4);
               var2 = additionalMakeup[var1];
               var3 = var2 >>> 1 & 7;
               var8 = var2 >>> 4 & 4095;
               var6 += var8;
               this.updatePointer(4 - var3);
            } else {
               if (var3 == 15) {
                  throw new Error("TIFFFaxDecoder2");
               }

               var6 += var8;
               this.updatePointer(9 - var3);
               if (var4 == 0) {
                  var7 = true;
               }
            }
         } else if (var8 == 200) {
            var1 = this.nextLesserThan8Bits(2);
            var2 = twoBitBlack[var1];
            var8 = var2 >>> 5 & 2047;
            var6 += var8;
            var3 = var2 >>> 1 & 15;
            this.updatePointer(2 - var3);
            var7 = true;
         } else {
            var6 += var8;
            this.updatePointer(4 - var3);
            var7 = true;
         }
      }

      return var6;
   }

   private int readEOL() {
      if (this.fillBits == 0) {
         if (this.nextNBits(12) != 1) {
            throw new Error("TIFFFaxDecoder6");
         }
      } else if (this.fillBits == 1) {
         int var1 = 8 - this.bitPointer;
         if (this.nextNBits(var1) != 0) {
            throw new Error("TIFFFaxDecoder8");
         }

         if (var1 < 4 && this.nextNBits(8) != 0) {
            throw new Error("TIFFFaxDecoder8");
         }

         int var2;
         while((var2 = this.nextNBits(8)) != 1) {
            if (var2 != 0) {
               throw new Error("TIFFFaxDecoder8");
            }
         }
      }

      return this.oneD == 0 ? 1 : this.nextLesserThan8Bits(1);
   }

   private void getNextChangingElement(int var1, boolean var2, int[] var3) {
      int[] var4 = this.prevChangingElems;
      int var5 = this.changingElemSize;
      int var6 = this.lastChangingElement > 0 ? this.lastChangingElement - 1 : 0;
      if (var2) {
         var6 &= -2;
      } else {
         var6 |= 1;
      }

      int var7;
      for(var7 = var6; var7 < var5; var7 += 2) {
         int var8 = var4[var7];
         if (var8 > var1) {
            this.lastChangingElement = var7;
            var3[0] = var8;
            break;
         }
      }

      if (var7 + 1 < var5) {
         var3[1] = var4[var7 + 1];
      }

   }

   private int nextNBits(int var1) {
      int var5 = this.data.length - 1;
      int var6 = this.bytePointer;
      byte var2;
      byte var3;
      byte var4;
      if (this.fillOrder == 1) {
         var2 = this.data[var6];
         if (var6 == var5) {
            var3 = 0;
            var4 = 0;
         } else if (var6 + 1 == var5) {
            var3 = this.data[var6 + 1];
            var4 = 0;
         } else {
            var3 = this.data[var6 + 1];
            var4 = this.data[var6 + 2];
         }
      } else {
         if (this.fillOrder != 2) {
            throw new Error("TIFFFaxDecoder7");
         }

         var2 = flipTable[this.data[var6] & 255];
         if (var6 == var5) {
            var3 = 0;
            var4 = 0;
         } else if (var6 + 1 == var5) {
            var3 = flipTable[this.data[var6 + 1] & 255];
            var4 = 0;
         } else {
            var3 = flipTable[this.data[var6 + 1] & 255];
            var4 = flipTable[this.data[var6 + 2] & 255];
         }
      }

      int var7 = 8 - this.bitPointer;
      int var8 = var1 - var7;
      int var9 = 0;
      if (var8 > 8) {
         var9 = var8 - 8;
         var8 = 8;
      }

      ++this.bytePointer;
      int var10 = (var2 & table1[var7]) << var1 - var7;
      int var11 = (var3 & table2[var8]) >>> 8 - var8;
      boolean var12 = false;
      if (var9 != 0) {
         var11 <<= var9;
         int var14 = (var4 & table2[var9]) >>> 8 - var9;
         var11 |= var14;
         ++this.bytePointer;
         this.bitPointer = var9;
      } else if (var8 == 8) {
         this.bitPointer = 0;
         ++this.bytePointer;
      } else {
         this.bitPointer = var8;
      }

      int var13 = var10 | var11;
      return var13;
   }

   private int nextLesserThan8Bits(int var1) {
      int var4 = this.data.length - 1;
      int var5 = this.bytePointer;
      byte var2;
      byte var3;
      if (this.fillOrder == 1) {
         var2 = this.data[var5];
         if (var5 == var4) {
            var3 = 0;
         } else {
            var3 = this.data[var5 + 1];
         }
      } else {
         if (this.fillOrder != 2) {
            throw new Error("TIFFFaxDecoder7");
         }

         var2 = flipTable[this.data[var5] & 255];
         if (var5 == var4) {
            var3 = 0;
         } else {
            var3 = flipTable[this.data[var5 + 1] & 255];
         }
      }

      int var6 = 8 - this.bitPointer;
      int var7 = var1 - var6;
      int var8 = var6 - var1;
      int var9;
      if (var8 >= 0) {
         var9 = (var2 & table1[var6]) >>> var8;
         this.bitPointer += var1;
         if (this.bitPointer == 8) {
            this.bitPointer = 0;
            ++this.bytePointer;
         }
      } else {
         var9 = (var2 & table1[var6]) << -var8;
         int var10 = (var3 & table2[var7]) >>> 8 - var7;
         var9 |= var10;
         ++this.bytePointer;
         this.bitPointer = var7;
      }

      return var9;
   }

   private void updatePointer(int var1) {
      int var2 = this.bitPointer - var1;
      if (var2 < 0) {
         --this.bytePointer;
         this.bitPointer = 8 + var2;
      } else {
         this.bitPointer = var2;
      }

   }

   private boolean advancePointer() {
      if (this.bitPointer != 0) {
         ++this.bytePointer;
         this.bitPointer = 0;
      }

      return true;
   }
}
