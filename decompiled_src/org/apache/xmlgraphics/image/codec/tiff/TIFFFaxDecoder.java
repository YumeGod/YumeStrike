package org.apache.xmlgraphics.image.codec.tiff;

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

   public TIFFFaxDecoder(int fillOrder, int w, int h) {
      this.fillOrder = fillOrder;
      this.w = w;
      this.h = h;
      this.bitPointer = 0;
      this.bytePointer = 0;
      this.prevChangingElems = new int[w];
      this.currChangingElems = new int[w];
   }

   public void decode1D(byte[] buffer, byte[] compData, int startX, int height) {
      this.data = compData;
      int lineOffset = 0;
      int scanlineStride = (this.w + 7) / 8;
      this.bitPointer = 0;
      this.bytePointer = 0;

      for(int i = 0; i < height; ++i) {
         this.decodeNextScanline(buffer, lineOffset, startX);
         lineOffset += scanlineStride;
      }

   }

   public void decodeNextScanline(byte[] buffer, int lineOffset, int bitOffset) {
      int bits = false;
      int code = false;
      int isT = false;
      boolean isWhite = true;
      this.changingElemSize = 0;

      while(bitOffset < this.w) {
         int current;
         short entry;
         int bits;
         int code;
         int isT;
         while(isWhite) {
            current = this.nextNBits(10);
            entry = white[current];
            isT = entry & 1;
            bits = entry >>> 1 & 15;
            if (bits == 12) {
               int twoBits = this.nextLesserThan8Bits(2);
               current = current << 2 & 12 | twoBits;
               entry = additionalMakeup[current];
               bits = entry >>> 1 & 7;
               code = entry >>> 4 & 4095;
               bitOffset += code;
               this.updatePointer(4 - bits);
            } else {
               if (bits == 0) {
                  throw new Error("TIFFFaxDecoder0");
               }

               if (bits == 15) {
                  throw new Error("TIFFFaxDecoder1");
               }

               code = entry >>> 5 & 2047;
               bitOffset += code;
               this.updatePointer(10 - bits);
               if (isT == 0) {
                  isWhite = false;
                  this.currChangingElems[this.changingElemSize++] = bitOffset;
               }
            }
         }

         if (bitOffset == this.w) {
            if (this.compression == 2) {
               this.advancePointer();
            }
            break;
         }

         while(!isWhite) {
            current = this.nextLesserThan8Bits(4);
            entry = initBlack[current];
            isT = entry & 1;
            bits = entry >>> 1 & 15;
            code = entry >>> 5 & 2047;
            if (code == 100) {
               current = this.nextNBits(9);
               entry = black[current];
               isT = entry & 1;
               bits = entry >>> 1 & 15;
               code = entry >>> 5 & 2047;
               if (bits == 12) {
                  this.updatePointer(5);
                  current = this.nextLesserThan8Bits(4);
                  entry = additionalMakeup[current];
                  bits = entry >>> 1 & 7;
                  code = entry >>> 4 & 4095;
                  this.setToBlack(buffer, lineOffset, bitOffset, code);
                  bitOffset += code;
                  this.updatePointer(4 - bits);
               } else {
                  if (bits == 15) {
                     throw new Error("TIFFFaxDecoder2");
                  }

                  this.setToBlack(buffer, lineOffset, bitOffset, code);
                  bitOffset += code;
                  this.updatePointer(9 - bits);
                  if (isT == 0) {
                     isWhite = true;
                     this.currChangingElems[this.changingElemSize++] = bitOffset;
                  }
               }
            } else if (code == 200) {
               current = this.nextLesserThan8Bits(2);
               entry = twoBitBlack[current];
               code = entry >>> 5 & 2047;
               bits = entry >>> 1 & 15;
               this.setToBlack(buffer, lineOffset, bitOffset, code);
               bitOffset += code;
               this.updatePointer(2 - bits);
               isWhite = true;
               this.currChangingElems[this.changingElemSize++] = bitOffset;
            } else {
               this.setToBlack(buffer, lineOffset, bitOffset, code);
               bitOffset += code;
               this.updatePointer(4 - bits);
               isWhite = true;
               this.currChangingElems[this.changingElemSize++] = bitOffset;
            }
         }

         if (bitOffset == this.w) {
            if (this.compression == 2) {
               this.advancePointer();
            }
            break;
         }
      }

      this.currChangingElems[this.changingElemSize++] = bitOffset;
   }

   public void decode2D(byte[] buffer, byte[] compData, int startX, int height, long tiffT4Options) {
      this.data = compData;
      this.compression = 3;
      this.bitPointer = 0;
      this.bytePointer = 0;
      int scanlineStride = (this.w + 7) / 8;
      int[] b = new int[2];
      int currIndex = false;
      this.oneD = (int)(tiffT4Options & 1L);
      this.uncompressedMode = (int)((tiffT4Options & 2L) >> 1);
      this.fillBits = (int)((tiffT4Options & 4L) >> 2);
      if (this.readEOL() != 1) {
         throw new Error("TIFFFaxDecoder3");
      } else {
         int lineOffset = 0;
         this.decodeNextScanline(buffer, lineOffset, startX);
         lineOffset += scanlineStride;

         for(int lines = 1; lines < height; ++lines) {
            if (this.readEOL() != 0) {
               this.decodeNextScanline(buffer, lineOffset, startX);
            } else {
               int[] temp = this.prevChangingElems;
               this.prevChangingElems = this.currChangingElems;
               this.currChangingElems = temp;
               int currIndex = 0;
               int a0 = -1;
               boolean isWhite = true;
               int bitOffset = startX;
               this.lastChangingElement = 0;

               while(bitOffset < this.w) {
                  this.getNextChangingElement(a0, isWhite, b);
                  int b1 = b[0];
                  int b2 = b[1];
                  int entry = this.nextLesserThan8Bits(7);
                  entry = twoDCodes[entry] & 255;
                  int code = (entry & 120) >>> 3;
                  int bits = entry & 7;
                  if (code == 0) {
                     if (!isWhite) {
                        this.setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
                     }

                     a0 = b2;
                     bitOffset = b2;
                     this.updatePointer(7 - bits);
                  } else if (code == 1) {
                     this.updatePointer(7 - bits);
                     int number;
                     if (isWhite) {
                        number = this.decodeWhiteCodeWord();
                        bitOffset += number;
                        this.currChangingElems[currIndex++] = bitOffset;
                        number = this.decodeBlackCodeWord();
                        this.setToBlack(buffer, lineOffset, bitOffset, number);
                        bitOffset += number;
                        this.currChangingElems[currIndex++] = bitOffset;
                     } else {
                        number = this.decodeBlackCodeWord();
                        this.setToBlack(buffer, lineOffset, bitOffset, number);
                        bitOffset += number;
                        this.currChangingElems[currIndex++] = bitOffset;
                        number = this.decodeWhiteCodeWord();
                        bitOffset += number;
                        this.currChangingElems[currIndex++] = bitOffset;
                     }

                     a0 = bitOffset;
                  } else {
                     if (code > 8) {
                        throw new Error("TIFFFaxDecoder4");
                     }

                     int a1 = b1 + (code - 5);
                     this.currChangingElems[currIndex++] = a1;
                     if (!isWhite) {
                        this.setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                     }

                     a0 = a1;
                     bitOffset = a1;
                     isWhite = !isWhite;
                     this.updatePointer(7 - bits);
                  }
               }

               this.currChangingElems[currIndex++] = bitOffset;
               this.changingElemSize = currIndex;
            }

            lineOffset += scanlineStride;
         }

      }
   }

   public synchronized void decodeT6(byte[] buffer, byte[] compData, int startX, int height, long tiffT6Options) {
      this.data = compData;
      this.compression = 4;
      this.bitPointer = 0;
      this.bytePointer = 0;
      int scanlineStride = (this.w + 7) / 8;
      int[] b = new int[2];
      this.uncompressedMode = (int)((tiffT6Options & 2L) >> 1);
      int[] cce = this.currChangingElems;
      this.changingElemSize = 0;
      cce[this.changingElemSize++] = this.w;
      cce[this.changingElemSize++] = this.w;
      int lineOffset = 0;

      for(int lines = 0; lines < height; ++lines) {
         int a0 = -1;
         boolean isWhite = true;
         int[] temp = this.prevChangingElems;
         this.prevChangingElems = this.currChangingElems;
         cce = this.currChangingElems = temp;
         int currIndex = 0;
         int bitOffset = startX;
         this.lastChangingElement = 0;

         while(true) {
            while(bitOffset < this.w) {
               this.getNextChangingElement(a0, isWhite, b);
               int b1 = b[0];
               int b2 = b[1];
               int entry = this.nextLesserThan8Bits(7);
               entry = twoDCodes[entry] & 255;
               int code = (entry & 120) >>> 3;
               int bits = entry & 7;
               if (code == 0) {
                  if (!isWhite) {
                     this.setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
                  }

                  a0 = b2;
                  bitOffset = b2;
                  this.updatePointer(7 - bits);
               } else {
                  int zeros;
                  if (code == 1) {
                     this.updatePointer(7 - bits);
                     if (isWhite) {
                        zeros = this.decodeWhiteCodeWord();
                        bitOffset += zeros;
                        cce[currIndex++] = bitOffset;
                        zeros = this.decodeBlackCodeWord();
                        this.setToBlack(buffer, lineOffset, bitOffset, zeros);
                        bitOffset += zeros;
                        cce[currIndex++] = bitOffset;
                     } else {
                        zeros = this.decodeBlackCodeWord();
                        this.setToBlack(buffer, lineOffset, bitOffset, zeros);
                        bitOffset += zeros;
                        cce[currIndex++] = bitOffset;
                        zeros = this.decodeWhiteCodeWord();
                        bitOffset += zeros;
                        cce[currIndex++] = bitOffset;
                     }

                     a0 = bitOffset;
                  } else if (code <= 8) {
                     int a1 = b1 + (code - 5);
                     cce[currIndex++] = a1;
                     if (!isWhite) {
                        this.setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                     }

                     a0 = a1;
                     bitOffset = a1;
                     isWhite = !isWhite;
                     this.updatePointer(7 - bits);
                  } else {
                     if (code != 11) {
                        throw new Error("TIFFFaxDecoder5");
                     }

                     if (this.nextLesserThan8Bits(3) != 7) {
                        throw new Error("TIFFFaxDecoder5");
                     }

                     zeros = 0;
                     boolean exit = false;

                     while(!exit) {
                        while(this.nextLesserThan8Bits(1) != 1) {
                           ++zeros;
                        }

                        if (zeros > 5) {
                           zeros -= 6;
                           if (!isWhite && zeros > 0) {
                              cce[currIndex++] = bitOffset;
                           }

                           bitOffset += zeros;
                           if (zeros > 0) {
                              isWhite = true;
                           }

                           if (this.nextLesserThan8Bits(1) == 0) {
                              if (!isWhite) {
                                 cce[currIndex++] = bitOffset;
                              }

                              isWhite = true;
                           } else {
                              if (isWhite) {
                                 cce[currIndex++] = bitOffset;
                              }

                              isWhite = false;
                           }

                           exit = true;
                        }

                        if (zeros == 5) {
                           if (!isWhite) {
                              cce[currIndex++] = bitOffset;
                           }

                           bitOffset += zeros;
                           isWhite = true;
                        } else {
                           bitOffset += zeros;
                           cce[currIndex++] = bitOffset;
                           this.setToBlack(buffer, lineOffset, bitOffset, 1);
                           ++bitOffset;
                           isWhite = false;
                        }
                     }
                  }
               }
            }

            cce[currIndex++] = bitOffset;
            this.changingElemSize = currIndex;
            lineOffset += scanlineStride;
            break;
         }
      }

   }

   private void setToBlack(byte[] buffer, int lineOffset, int bitOffset, int numBits) {
      int bitNum = 8 * lineOffset + bitOffset;
      int lastBit = bitNum + numBits;
      int byteNum = bitNum >> 3;
      int shift = bitNum & 7;
      if (shift > 0) {
         int maskVal = 1 << 7 - shift;

         byte val;
         for(val = buffer[byteNum]; maskVal > 0 && bitNum < lastBit; ++bitNum) {
            val = (byte)(val | maskVal);
            maskVal >>= 1;
         }

         buffer[byteNum] = val;
      }

      for(byteNum = bitNum >> 3; bitNum < lastBit - 7; bitNum += 8) {
         buffer[byteNum++] = -1;
      }

      while(bitNum < lastBit) {
         byteNum = bitNum >> 3;
         buffer[byteNum] = (byte)(buffer[byteNum] | 1 << 7 - (bitNum & 7));
         ++bitNum;
      }

   }

   private int decodeWhiteCodeWord() {
      int code = true;
      int runLength = 0;
      boolean isWhite = true;

      while(isWhite) {
         int current = this.nextNBits(10);
         int entry = white[current];
         int isT = entry & 1;
         int bits = entry >>> 1 & 15;
         int code;
         if (bits == 12) {
            int twoBits = this.nextLesserThan8Bits(2);
            current = current << 2 & 12 | twoBits;
            entry = additionalMakeup[current];
            bits = entry >>> 1 & 7;
            code = entry >>> 4 & 4095;
            runLength += code;
            this.updatePointer(4 - bits);
         } else {
            if (bits == 0) {
               throw new Error("TIFFFaxDecoder0");
            }

            if (bits == 15) {
               throw new Error("TIFFFaxDecoder1");
            }

            code = entry >>> 5 & 2047;
            runLength += code;
            this.updatePointer(10 - bits);
            if (isT == 0) {
               isWhite = false;
            }
         }
      }

      return runLength;
   }

   private int decodeBlackCodeWord() {
      int code = true;
      int runLength = 0;
      boolean isWhite = false;

      while(!isWhite) {
         int current = this.nextLesserThan8Bits(4);
         int entry = initBlack[current];
         int isT = entry & 1;
         int bits = entry >>> 1 & 15;
         int code = entry >>> 5 & 2047;
         if (code == 100) {
            current = this.nextNBits(9);
            entry = black[current];
            isT = entry & 1;
            bits = entry >>> 1 & 15;
            code = entry >>> 5 & 2047;
            if (bits == 12) {
               this.updatePointer(5);
               current = this.nextLesserThan8Bits(4);
               entry = additionalMakeup[current];
               bits = entry >>> 1 & 7;
               code = entry >>> 4 & 4095;
               runLength += code;
               this.updatePointer(4 - bits);
            } else {
               if (bits == 15) {
                  throw new Error("TIFFFaxDecoder2");
               }

               runLength += code;
               this.updatePointer(9 - bits);
               if (isT == 0) {
                  isWhite = true;
               }
            }
         } else if (code == 200) {
            current = this.nextLesserThan8Bits(2);
            entry = twoBitBlack[current];
            code = entry >>> 5 & 2047;
            runLength += code;
            bits = entry >>> 1 & 15;
            this.updatePointer(2 - bits);
            isWhite = true;
         } else {
            runLength += code;
            this.updatePointer(4 - bits);
            isWhite = true;
         }
      }

      return runLength;
   }

   private int readEOL() {
      if (this.fillBits == 0) {
         if (this.nextNBits(12) != 1) {
            throw new Error("TIFFFaxDecoder6");
         }
      } else if (this.fillBits == 1) {
         int bitsLeft = 8 - this.bitPointer;
         if (this.nextNBits(bitsLeft) != 0) {
            throw new Error("TIFFFaxDecoder8");
         }

         if (bitsLeft < 4 && this.nextNBits(8) != 0) {
            throw new Error("TIFFFaxDecoder8");
         }

         int n;
         while((n = this.nextNBits(8)) != 1) {
            if (n != 0) {
               throw new Error("TIFFFaxDecoder8");
            }
         }
      }

      return this.oneD == 0 ? 1 : this.nextLesserThan8Bits(1);
   }

   private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
      int[] pce = this.prevChangingElems;
      int ces = this.changingElemSize;
      int start = this.lastChangingElement > 0 ? this.lastChangingElement - 1 : 0;
      if (isWhite) {
         start &= -2;
      } else {
         start |= 1;
      }

      int i;
      for(i = start; i < ces; i += 2) {
         int temp = pce[i];
         if (temp > a0) {
            this.lastChangingElement = i;
            ret[0] = temp;
            break;
         }
      }

      if (i + 1 < ces) {
         ret[1] = pce[i + 1];
      }

   }

   private int nextNBits(int bitsToGet) {
      int l = this.data.length - 1;
      int bp = this.bytePointer;
      byte b;
      byte next;
      byte var4;
      if (this.fillOrder == 1) {
         b = this.data[bp];
         if (bp == l) {
            next = 0;
            var4 = 0;
         } else if (bp + 1 == l) {
            next = this.data[bp + 1];
            var4 = 0;
         } else {
            next = this.data[bp + 1];
            var4 = this.data[bp + 2];
         }
      } else {
         if (this.fillOrder != 2) {
            throw new Error("TIFFFaxDecoder7");
         }

         b = flipTable[this.data[bp] & 255];
         if (bp == l) {
            next = 0;
            var4 = 0;
         } else if (bp + 1 == l) {
            next = flipTable[this.data[bp + 1] & 255];
            var4 = 0;
         } else {
            next = flipTable[this.data[bp + 1] & 255];
            var4 = flipTable[this.data[bp + 2] & 255];
         }
      }

      int bitsLeft = 8 - this.bitPointer;
      int bitsFromNextByte = bitsToGet - bitsLeft;
      int bitsFromNext2NextByte = 0;
      if (bitsFromNextByte > 8) {
         bitsFromNext2NextByte = bitsFromNextByte - 8;
         bitsFromNextByte = 8;
      }

      ++this.bytePointer;
      int i1 = (b & table1[bitsLeft]) << bitsToGet - bitsLeft;
      int i2 = (next & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
      int i3 = false;
      if (bitsFromNext2NextByte != 0) {
         i2 <<= bitsFromNext2NextByte;
         int i3 = (var4 & table2[bitsFromNext2NextByte]) >>> 8 - bitsFromNext2NextByte;
         i2 |= i3;
         ++this.bytePointer;
         this.bitPointer = bitsFromNext2NextByte;
      } else if (bitsFromNextByte == 8) {
         this.bitPointer = 0;
         ++this.bytePointer;
      } else {
         this.bitPointer = bitsFromNextByte;
      }

      int i = i1 | i2;
      return i;
   }

   private int nextLesserThan8Bits(int bitsToGet) {
      int l = this.data.length - 1;
      int bp = this.bytePointer;
      byte b;
      byte var3;
      if (this.fillOrder == 1) {
         b = this.data[bp];
         if (bp == l) {
            var3 = 0;
         } else {
            var3 = this.data[bp + 1];
         }
      } else {
         if (this.fillOrder != 2) {
            throw new Error("TIFFFaxDecoder7");
         }

         b = flipTable[this.data[bp] & 255];
         if (bp == l) {
            var3 = 0;
         } else {
            var3 = flipTable[this.data[bp + 1] & 255];
         }
      }

      int bitsLeft = 8 - this.bitPointer;
      int bitsFromNextByte = bitsToGet - bitsLeft;
      int shift = bitsLeft - bitsToGet;
      int i1;
      if (shift >= 0) {
         i1 = (b & table1[bitsLeft]) >>> shift;
         this.bitPointer += bitsToGet;
         if (this.bitPointer == 8) {
            this.bitPointer = 0;
            ++this.bytePointer;
         }
      } else {
         i1 = (b & table1[bitsLeft]) << -shift;
         int i2 = (var3 & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
         i1 |= i2;
         ++this.bytePointer;
         this.bitPointer = bitsFromNextByte;
      }

      return i1;
   }

   private void updatePointer(int bitsToMoveBack) {
      int i = this.bitPointer - bitsToMoveBack;
      if (i < 0) {
         --this.bytePointer;
         this.bitPointer = 8 + i;
      } else {
         this.bitPointer = i;
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
