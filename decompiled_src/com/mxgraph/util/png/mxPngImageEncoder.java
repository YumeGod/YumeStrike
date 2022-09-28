package com.mxgraph.util.png;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class mxPngImageEncoder {
   private static final int PNG_COLOR_GRAY = 0;
   private static final int PNG_COLOR_RGB = 2;
   private static final int PNG_COLOR_PALETTE = 3;
   private static final int PNG_COLOR_GRAY_ALPHA = 4;
   private static final int PNG_COLOR_RGB_ALPHA = 6;
   private static final byte[] magic = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private mxPngEncodeParam param;
   private RenderedImage image;
   private int width;
   private int height;
   private int bitDepth;
   private int bitShift;
   private int numBands;
   private int colorType;
   private int bpp;
   private boolean skipAlpha = false;
   private boolean compressGray = false;
   private boolean interlace;
   private byte[] redPalette = null;
   private byte[] greenPalette = null;
   private byte[] bluePalette = null;
   private byte[] alphaPalette = null;
   private DataOutputStream dataOutput;
   protected OutputStream output;
   private byte[] prevRow = null;
   private byte[] currRow = null;
   private byte[][] filteredRows = (byte[][])null;
   private static final float[] srgbChroma = new float[]{0.3127F, 0.329F, 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F};

   public mxPngImageEncoder(OutputStream var1, mxPngEncodeParam var2) {
      this.output = var1;
      this.param = var2;
      this.dataOutput = new DataOutputStream(var1);
   }

   public mxPngEncodeParam getParam() {
      return this.param;
   }

   public void setParam(mxPngEncodeParam var1) {
      this.param = var1;
   }

   public OutputStream getOutputStream() {
      return this.output;
   }

   private void writeMagic() throws IOException {
      this.dataOutput.write(magic);
   }

   private void writeIHDR() throws IOException {
      ChunkStream var1 = new ChunkStream("IHDR");
      var1.writeInt(this.width);
      var1.writeInt(this.height);
      var1.writeByte((byte)this.bitDepth);
      var1.writeByte((byte)this.colorType);
      var1.writeByte(0);
      var1.writeByte(0);
      var1.writeByte(this.interlace ? 1 : 0);
      var1.writeToStream(this.dataOutput);
      var1.close();
   }

   private static int clamp(int var0, int var1) {
      return var0 > var1 ? var1 : var0;
   }

   private void encodePass(OutputStream var1, Raster var2, int var3, int var4, int var5, int var6) throws IOException {
      int var7 = var2.getMinX();
      int var8 = var2.getMinY();
      int var9 = var2.getWidth();
      int var10 = var2.getHeight();
      var3 *= this.numBands;
      var5 *= this.numBands;
      int var11 = 8 / this.bitDepth;
      int var12 = var9 * this.numBands;
      int[] var13 = new int[var12];
      int var14 = (var12 - var3 + var5 - 1) / var5;
      int var15 = var14 * this.numBands;
      if (this.bitDepth < 8) {
         var15 = (var15 + var11 - 1) / var11;
      } else if (this.bitDepth == 16) {
         var15 *= 2;
      }

      if (var15 != 0) {
         this.currRow = new byte[var15 + this.bpp];
         this.prevRow = new byte[var15 + this.bpp];
         this.filteredRows = new byte[5][var15 + this.bpp];
         int var16 = (1 << this.bitDepth) - 1;

         for(int var17 = var8 + var4; var17 < var8 + var10; var17 += var6) {
            var2.getPixels(var7, var17, var9, 1, var13);
            int var18;
            int var19;
            if (this.compressGray) {
               var18 = 8 - this.bitDepth;

               for(var19 = 0; var19 < var9; ++var19) {
                  var13[var19] >>= var18;
               }
            }

            int var21;
            var18 = this.bpp;
            var19 = 0;
            int var20 = 0;
            int var22;
            int var23;
            label77:
            switch (this.bitDepth) {
               case 1:
               case 2:
               case 4:
                  var21 = var11 - 1;
                  var22 = var3;

                  for(; var22 < var12; var22 += var5) {
                     var23 = clamp(var13[var22] >> this.bitShift, var16);
                     var20 = var20 << this.bitDepth | var23;
                     if (var19++ == var21) {
                        this.currRow[var18++] = (byte)var20;
                        var20 = 0;
                        var19 = 0;
                     }
                  }

                  if (var19 != 0) {
                     var20 <<= (var11 - var19) * this.bitDepth;
                     this.currRow[var18++] = (byte)var20;
                  }
                  break;
               case 8:
                  var22 = var3;

                  while(true) {
                     if (var22 >= var12) {
                        break label77;
                     }

                     for(var23 = 0; var23 < this.numBands; ++var23) {
                        this.currRow[var18++] = (byte)clamp(var13[var22 + var23] >> this.bitShift, var16);
                     }

                     var22 += var5;
                  }
               case 16:
                  for(var22 = var3; var22 < var12; var22 += var5) {
                     for(var23 = 0; var23 < this.numBands; ++var23) {
                        int var24 = clamp(var13[var22 + var23] >> this.bitShift, var16);
                        this.currRow[var18++] = (byte)(var24 >> 8);
                        this.currRow[var18++] = (byte)(var24 & 255);
                     }
                  }
            }

            var21 = this.param.filterRow(this.currRow, this.prevRow, this.filteredRows, var15, this.bpp);
            var1.write(var21);
            var1.write(this.filteredRows[var21], this.bpp, var15);
            byte[] var25 = this.currRow;
            this.currRow = this.prevRow;
            this.prevRow = var25;
         }

      }
   }

   private void writeIDAT() throws IOException {
      IDATOutputStream var1 = new IDATOutputStream(this.dataOutput, 8192);
      DeflaterOutputStream var2 = new DeflaterOutputStream(var1, new Deflater(9));
      Raster var3 = this.image.getData(new Rectangle(this.image.getMinX(), this.image.getMinY(), this.image.getWidth(), this.image.getHeight()));
      if (this.skipAlpha) {
         int var4 = var3.getNumBands() - 1;
         int[] var5 = new int[var4];

         for(int var6 = 0; var6 < var4; var5[var6] = var6++) {
         }

         var3 = var3.createChild(0, 0, var3.getWidth(), var3.getHeight(), 0, 0, var5);
      }

      if (this.interlace) {
         this.encodePass(var2, var3, 0, 0, 8, 8);
         this.encodePass(var2, var3, 4, 0, 8, 8);
         this.encodePass(var2, var3, 0, 4, 4, 8);
         this.encodePass(var2, var3, 2, 0, 4, 4);
         this.encodePass(var2, var3, 0, 2, 2, 4);
         this.encodePass(var2, var3, 1, 0, 2, 2);
         this.encodePass(var2, var3, 0, 1, 1, 2);
      } else {
         this.encodePass(var2, var3, 0, 0, 1, 1);
      }

      var2.finish();
      var1.flush();
   }

   private void writeIEND() throws IOException {
      ChunkStream var1 = new ChunkStream("IEND");
      var1.writeToStream(this.dataOutput);
      var1.close();
   }

   private void writeCHRM() throws IOException {
      if (this.param.isChromaticitySet() || this.param.isSRGBIntentSet()) {
         ChunkStream var1 = new ChunkStream("cHRM");
         float[] var2;
         if (!this.param.isSRGBIntentSet()) {
            var2 = this.param.getChromaticity();
         } else {
            var2 = srgbChroma;
         }

         for(int var3 = 0; var3 < 8; ++var3) {
            var1.writeInt((int)(var2[var3] * 100000.0F));
         }

         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeGAMA() throws IOException {
      if (this.param.isGammaSet() || this.param.isSRGBIntentSet()) {
         ChunkStream var1 = new ChunkStream("gAMA");
         float var2;
         if (!this.param.isSRGBIntentSet()) {
            var2 = this.param.getGamma();
         } else {
            var2 = 0.45454544F;
         }

         var1.writeInt((int)(var2 * 100000.0F));
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeICCP() throws IOException {
      if (this.param.isICCProfileDataSet()) {
         ChunkStream var1 = new ChunkStream("iCCP");
         byte[] var2 = this.param.getICCProfileData();
         var1.write(var2);
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeSBIT() throws IOException {
      if (this.param.isSignificantBitsSet()) {
         ChunkStream var1 = new ChunkStream("sBIT");
         int[] var2 = this.param.getSignificantBits();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.writeByte(var2[var4]);
         }

         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeSRGB() throws IOException {
      if (this.param.isSRGBIntentSet()) {
         ChunkStream var1 = new ChunkStream("sRGB");
         int var2 = this.param.getSRGBIntent();
         var1.write(var2);
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writePLTE() throws IOException {
      if (this.redPalette != null) {
         ChunkStream var1 = new ChunkStream("PLTE");

         for(int var2 = 0; var2 < this.redPalette.length; ++var2) {
            var1.writeByte(this.redPalette[var2]);
            var1.writeByte(this.greenPalette[var2]);
            var1.writeByte(this.bluePalette[var2]);
         }

         var1.writeToStream(this.dataOutput);
         var1.close();
      }
   }

   private void writeBKGD() throws IOException {
      if (this.param.isBackgroundSet()) {
         ChunkStream var1 = new ChunkStream("bKGD");
         switch (this.colorType) {
            case 0:
            case 4:
               int var2 = ((mxPngEncodeParam.Gray)this.param).getBackgroundGray();
               var1.writeShort(var2);
            case 1:
            case 5:
            default:
               break;
            case 2:
            case 6:
               int[] var4 = ((mxPngEncodeParam.RGB)this.param).getBackgroundRGB();
               var1.writeShort(var4[0]);
               var1.writeShort(var4[1]);
               var1.writeShort(var4[2]);
               break;
            case 3:
               int var3 = ((mxPngEncodeParam.Palette)this.param).getBackgroundPaletteIndex();
               var1.writeByte(var3);
         }

         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeHIST() throws IOException {
      if (this.param.isPaletteHistogramSet()) {
         ChunkStream var1 = new ChunkStream("hIST");
         int[] var2 = this.param.getPaletteHistogram();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.writeShort(var2[var3]);
         }

         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeTRNS() throws IOException {
      int var2;
      if (this.param.isTransparencySet() && this.colorType != 4 && this.colorType != 6) {
         ChunkStream var5 = new ChunkStream("tRNS");
         if (this.param instanceof mxPngEncodeParam.Palette) {
            byte[] var7 = ((mxPngEncodeParam.Palette)this.param).getPaletteTransparency();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var5.writeByte(var7[var8]);
            }
         } else if (this.param instanceof mxPngEncodeParam.Gray) {
            var2 = ((mxPngEncodeParam.Gray)this.param).getTransparentGray();
            var5.writeShort(var2);
         } else if (this.param instanceof mxPngEncodeParam.RGB) {
            int[] var6 = ((mxPngEncodeParam.RGB)this.param).getTransparentRGB();
            var5.writeShort(var6[0]);
            var5.writeShort(var6[1]);
            var5.writeShort(var6[2]);
         }

         var5.writeToStream(this.dataOutput);
         var5.close();
      } else if (this.colorType == 3) {
         int var1 = Math.min(255, this.alphaPalette.length - 1);

         for(var2 = var1; var2 >= 0 && this.alphaPalette[var2] == -1; --var2) {
         }

         if (var2 >= 0) {
            ChunkStream var3 = new ChunkStream("tRNS");

            for(int var4 = 0; var4 <= var2; ++var4) {
               var3.writeByte(this.alphaPalette[var4]);
            }

            var3.writeToStream(this.dataOutput);
            var3.close();
         }
      }

   }

   private void writePHYS() throws IOException {
      if (this.param.isPhysicalDimensionSet()) {
         ChunkStream var1 = new ChunkStream("pHYs");
         int[] var2 = this.param.getPhysicalDimension();
         var1.writeInt(var2[0]);
         var1.writeInt(var2[1]);
         var1.writeByte((byte)var2[2]);
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeSPLT() throws IOException {
      if (this.param.isSuggestedPaletteSet()) {
         ChunkStream var1 = new ChunkStream("sPLT");
         System.out.println("sPLT not supported yet.");
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeTIME() throws IOException {
      if (this.param.isModificationTimeSet()) {
         ChunkStream var1 = new ChunkStream("tIME");
         Date var2 = this.param.getModificationTime();
         TimeZone var3 = TimeZone.getTimeZone("GMT");
         GregorianCalendar var4 = new GregorianCalendar(var3);
         var4.setTime(var2);
         int var5 = var4.get(1);
         int var6 = var4.get(2);
         int var7 = var4.get(5);
         int var8 = var4.get(11);
         int var9 = var4.get(12);
         int var10 = var4.get(13);
         var1.writeShort(var5);
         var1.writeByte(var6 + 1);
         var1.writeByte(var7);
         var1.writeByte(var8);
         var1.writeByte(var9);
         var1.writeByte(var10);
         var1.writeToStream(this.dataOutput);
         var1.close();
      }

   }

   private void writeTEXT() throws IOException {
      if (this.param.isTextSet()) {
         String[] var1 = this.param.getText();

         for(int var2 = 0; var2 < var1.length / 2; ++var2) {
            byte[] var3 = var1[2 * var2].getBytes();
            byte[] var4 = var1[2 * var2 + 1].getBytes();
            ChunkStream var5 = new ChunkStream("tEXt");
            var5.write(var3, 0, Math.min(var3.length, 79));
            var5.write(0);
            var5.write(var4);
            var5.writeToStream(this.dataOutput);
            var5.close();
         }
      }

   }

   private void writeZTXT() throws IOException {
      if (this.param.isCompressedTextSet()) {
         String[] var1 = this.param.getCompressedText();

         for(int var2 = 0; var2 < var1.length / 2; ++var2) {
            byte[] var3 = var1[2 * var2].getBytes();
            byte[] var4 = var1[2 * var2 + 1].getBytes();
            ChunkStream var5 = new ChunkStream("zTXt");
            var5.write(var3, 0, Math.min(var3.length, 79));
            var5.write(0);
            var5.write(0);
            DeflaterOutputStream var6 = new DeflaterOutputStream(var5);
            var6.write(var4);
            var6.finish();
            var5.writeToStream(this.dataOutput);
            var5.close();
         }
      }

   }

   private void writePrivateChunks() throws IOException {
      int var1 = this.param.getNumPrivateChunks();

      for(int var2 = 0; var2 < var1; ++var2) {
         String var3 = this.param.getPrivateChunkType(var2);
         byte[] var4 = this.param.getPrivateChunkData(var2);
         ChunkStream var5 = new ChunkStream(var3);
         var5.write(var4);
         var5.writeToStream(this.dataOutput);
         var5.close();
      }

   }

   private mxPngEncodeParam.Gray createGrayParam(byte[] var1, byte[] var2, byte[] var3, byte[] var4) {
      mxPngEncodeParam.Gray var5 = new mxPngEncodeParam.Gray();
      int var6 = 0;
      int var7 = 255 / ((1 << this.bitDepth) - 1);
      int var8 = 1 << this.bitDepth;

      for(int var9 = 0; var9 < var8; ++var9) {
         byte var10 = var1[var9];
         if (var10 != var9 * var7 || var10 != var2[var9] || var10 != var3[var9]) {
            return null;
         }

         byte var11 = var4[var9];
         if (var11 == 0) {
            var5.setTransparentGray(var9);
            ++var6;
            if (var6 > 1) {
               return null;
            }
         } else if (var11 != -1) {
            return null;
         }
      }

      return var5;
   }

   public void encode(RenderedImage var1) throws IOException {
      this.image = var1;
      this.width = this.image.getWidth();
      this.height = this.image.getHeight();
      SampleModel var2 = this.image.getSampleModel();
      int[] var3 = var2.getSampleSize();
      this.bitDepth = -1;
      this.bitShift = 0;
      if (this.param instanceof mxPngEncodeParam.Gray) {
         mxPngEncodeParam.Gray var4 = (mxPngEncodeParam.Gray)this.param;
         if (var4.isBitDepthSet()) {
            this.bitDepth = var4.getBitDepth();
         }

         if (var4.isBitShiftSet()) {
            this.bitShift = var4.getBitShift();
         }
      }

      if (this.bitDepth == -1) {
         this.bitDepth = var3[0];

         for(int var11 = 1; var11 < var3.length; ++var11) {
            if (var3[var11] != this.bitDepth) {
               throw new RuntimeException();
            }
         }

         if (this.bitDepth > 2 && this.bitDepth < 4) {
            this.bitDepth = 4;
         } else if (this.bitDepth > 4 && this.bitDepth < 8) {
            this.bitDepth = 8;
         } else if (this.bitDepth > 8 && this.bitDepth < 16) {
            this.bitDepth = 16;
         } else if (this.bitDepth > 16) {
            throw new RuntimeException();
         }
      }

      this.numBands = var2.getNumBands();
      this.bpp = this.numBands * (this.bitDepth == 16 ? 2 : 1);
      ColorModel var12 = this.image.getColorModel();
      if (var12 instanceof IndexColorModel) {
         if (this.bitDepth < 1 || this.bitDepth > 8) {
            throw new RuntimeException();
         }

         if (var2.getNumBands() != 1) {
            throw new RuntimeException();
         }

         IndexColorModel var5 = (IndexColorModel)var12;
         int var6 = var5.getMapSize();
         this.redPalette = new byte[var6];
         this.greenPalette = new byte[var6];
         this.bluePalette = new byte[var6];
         this.alphaPalette = new byte[var6];
         var5.getReds(this.redPalette);
         var5.getGreens(this.greenPalette);
         var5.getBlues(this.bluePalette);
         var5.getAlphas(this.alphaPalette);
         this.bpp = 1;
         if (this.param == null) {
            this.param = this.createGrayParam(this.redPalette, this.greenPalette, this.bluePalette, this.alphaPalette);
         }

         if (this.param == null) {
            this.param = new mxPngEncodeParam.Palette();
         }

         if (this.param instanceof mxPngEncodeParam.Palette) {
            mxPngEncodeParam.Palette var7 = (mxPngEncodeParam.Palette)this.param;
            if (var7.isPaletteSet()) {
               int[] var8 = var7.getPalette();
               var6 = var8.length / 3;
               int var9 = 0;

               for(int var10 = 0; var10 < var6; ++var10) {
                  this.redPalette[var10] = (byte)var8[var9++];
                  this.greenPalette[var10] = (byte)var8[var9++];
                  this.bluePalette[var10] = (byte)var8[var9++];
                  this.alphaPalette[var10] = -1;
               }
            }

            this.colorType = 3;
         } else {
            if (!(this.param instanceof mxPngEncodeParam.Gray)) {
               throw new RuntimeException();
            }

            this.redPalette = this.greenPalette = this.bluePalette = this.alphaPalette = null;
            this.colorType = 0;
         }
      } else if (this.numBands == 1) {
         if (this.param == null) {
            this.param = new mxPngEncodeParam.Gray();
         }

         this.colorType = 0;
      } else if (this.numBands == 2) {
         if (this.param == null) {
            this.param = new mxPngEncodeParam.Gray();
         }

         if (this.param.isTransparencySet()) {
            this.skipAlpha = true;
            this.numBands = 1;
            if (var3[0] == 8 && this.bitDepth < 8) {
               this.compressGray = true;
            }

            this.bpp = this.bitDepth == 16 ? 2 : 1;
            this.colorType = 0;
         } else {
            if (this.bitDepth < 8) {
               this.bitDepth = 8;
            }

            this.colorType = 4;
         }
      } else if (this.numBands == 3) {
         if (this.param == null) {
            this.param = new mxPngEncodeParam.RGB();
         }

         this.colorType = 2;
      } else if (this.numBands == 4) {
         if (this.param == null) {
            this.param = new mxPngEncodeParam.RGB();
         }

         if (this.param.isTransparencySet()) {
            this.skipAlpha = true;
            this.numBands = 3;
            this.bpp = this.bitDepth == 16 ? 6 : 3;
            this.colorType = 2;
         } else {
            this.colorType = 6;
         }
      }

      this.interlace = this.param.getInterlacing();
      this.writeMagic();
      this.writeIHDR();
      this.writeCHRM();
      this.writeGAMA();
      this.writeICCP();
      this.writeSBIT();
      this.writeSRGB();
      this.writePLTE();
      this.writeHIST();
      this.writeTRNS();
      this.writeBKGD();
      this.writePHYS();
      this.writeSPLT();
      this.writeTIME();
      this.writeTEXT();
      this.writeZTXT();
      this.writePrivateChunks();
      this.writeIDAT();
      this.writeIEND();
      this.dataOutput.flush();
   }
}
