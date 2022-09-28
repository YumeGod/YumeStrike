package com.mxgraph.util.png;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class mxPngEncodeParam {
   public static final int INTENT_PERCEPTUAL = 0;
   public static final int INTENT_RELATIVE = 1;
   public static final int INTENT_SATURATION = 2;
   public static final int INTENT_ABSOLUTE = 3;
   public static final int PNG_FILTER_NONE = 0;
   public static final int PNG_FILTER_SUB = 1;
   public static final int PNG_FILTER_UP = 2;
   public static final int PNG_FILTER_AVERAGE = 3;
   public static final int PNG_FILTER_PAETH = 4;
   protected int bitDepth;
   protected boolean bitDepthSet = false;
   private boolean useInterlacing = false;
   private float[] chromaticity = null;
   private boolean chromaticitySet = false;
   private float gamma;
   private boolean gammaSet = false;
   private int[] paletteHistogram = null;
   private boolean paletteHistogramSet = false;
   private byte[] ICCProfileData = null;
   private boolean ICCProfileDataSet = false;
   private int[] physicalDimension = null;
   private boolean physicalDimensionSet = false;
   private mxPngSuggestedPaletteEntry[] suggestedPalette = null;
   private boolean suggestedPaletteSet = false;
   private int[] significantBits = null;
   private boolean significantBitsSet = false;
   private int SRGBIntent;
   private boolean SRGBIntentSet = false;
   private String[] text = null;
   private boolean textSet = false;
   private Date modificationTime;
   private boolean modificationTimeSet = false;
   boolean transparencySet = false;
   private String[] zText = null;
   private boolean zTextSet = false;
   List chunkType = new ArrayList();
   List chunkData = new ArrayList();

   public static mxPngEncodeParam getDefaultEncodeParam(RenderedImage var0) {
      ColorModel var1 = var0.getColorModel();
      if (var1 instanceof IndexColorModel) {
         return new Palette();
      } else {
         SampleModel var2 = var0.getSampleModel();
         int var3 = var2.getNumBands();
         return (mxPngEncodeParam)(var3 != 1 && var3 != 2 ? new RGB() : new Gray());
      }
   }

   public abstract void setBitDepth(int var1);

   public int getBitDepth() {
      if (!this.bitDepthSet) {
         throw new IllegalStateException("PNGEncodeParam11");
      } else {
         return this.bitDepth;
      }
   }

   public void unsetBitDepth() {
      this.bitDepthSet = false;
   }

   public void setInterlacing(boolean var1) {
      this.useInterlacing = var1;
   }

   public boolean getInterlacing() {
      return this.useInterlacing;
   }

   public void unsetBackground() {
      throw new RuntimeException("PNGEncodeParam23");
   }

   public boolean isBackgroundSet() {
      throw new RuntimeException("PNGEncodeParam24");
   }

   public void setChromaticity(float[] var1) {
      if (var1.length != 8) {
         throw new IllegalArgumentException();
      } else {
         this.chromaticity = (float[])((float[])var1.clone());
         this.chromaticitySet = true;
      }
   }

   public void setChromaticity(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float[] var9 = new float[]{var1, var2, var3, var4, var5, var6, var7, var8};
      this.setChromaticity(var9);
   }

   public float[] getChromaticity() {
      if (!this.chromaticitySet) {
         throw new IllegalStateException("PNGEncodeParam12");
      } else {
         return (float[])((float[])this.chromaticity.clone());
      }
   }

   public void unsetChromaticity() {
      this.chromaticity = null;
      this.chromaticitySet = false;
   }

   public boolean isChromaticitySet() {
      return this.chromaticitySet;
   }

   public void setGamma(float var1) {
      this.gamma = var1;
      this.gammaSet = true;
   }

   public float getGamma() {
      if (!this.gammaSet) {
         throw new IllegalStateException("PNGEncodeParam13");
      } else {
         return this.gamma;
      }
   }

   public void unsetGamma() {
      this.gammaSet = false;
   }

   public boolean isGammaSet() {
      return this.gammaSet;
   }

   public void setPaletteHistogram(int[] var1) {
      this.paletteHistogram = (int[])((int[])var1.clone());
      this.paletteHistogramSet = true;
   }

   public int[] getPaletteHistogram() {
      if (!this.paletteHistogramSet) {
         throw new IllegalStateException("PNGEncodeParam14");
      } else {
         return this.paletteHistogram;
      }
   }

   public void unsetPaletteHistogram() {
      this.paletteHistogram = null;
      this.paletteHistogramSet = false;
   }

   public boolean isPaletteHistogramSet() {
      return this.paletteHistogramSet;
   }

   public void setICCProfileData(byte[] var1) {
      this.ICCProfileData = (byte[])((byte[])var1.clone());
      this.ICCProfileDataSet = true;
   }

   public byte[] getICCProfileData() {
      if (!this.ICCProfileDataSet) {
         throw new IllegalStateException("PNGEncodeParam15");
      } else {
         return (byte[])((byte[])this.ICCProfileData.clone());
      }
   }

   public void unsetICCProfileData() {
      this.ICCProfileData = null;
      this.ICCProfileDataSet = false;
   }

   public boolean isICCProfileDataSet() {
      return this.ICCProfileDataSet;
   }

   public void setPhysicalDimension(int[] var1) {
      this.physicalDimension = (int[])((int[])var1.clone());
      this.physicalDimensionSet = true;
   }

   public void setPhysicalDimension(int var1, int var2, int var3) {
      int[] var4 = new int[]{var1, var2, var3};
      this.setPhysicalDimension(var4);
   }

   public int[] getPhysicalDimension() {
      if (!this.physicalDimensionSet) {
         throw new IllegalStateException("PNGEncodeParam16");
      } else {
         return (int[])((int[])this.physicalDimension.clone());
      }
   }

   public void unsetPhysicalDimension() {
      this.physicalDimension = null;
      this.physicalDimensionSet = false;
   }

   public boolean isPhysicalDimensionSet() {
      return this.physicalDimensionSet;
   }

   public void setSuggestedPalette(mxPngSuggestedPaletteEntry[] var1) {
      this.suggestedPalette = (mxPngSuggestedPaletteEntry[])((mxPngSuggestedPaletteEntry[])var1.clone());
      this.suggestedPaletteSet = true;
   }

   mxPngSuggestedPaletteEntry[] getSuggestedPalette() {
      if (!this.suggestedPaletteSet) {
         throw new IllegalStateException("PNGEncodeParam17");
      } else {
         return (mxPngSuggestedPaletteEntry[])((mxPngSuggestedPaletteEntry[])this.suggestedPalette.clone());
      }
   }

   public void unsetSuggestedPalette() {
      this.suggestedPalette = null;
      this.suggestedPaletteSet = false;
   }

   public boolean isSuggestedPaletteSet() {
      return this.suggestedPaletteSet;
   }

   public void setSignificantBits(int[] var1) {
      this.significantBits = (int[])((int[])var1.clone());
      this.significantBitsSet = true;
   }

   public int[] getSignificantBits() {
      if (!this.significantBitsSet) {
         throw new IllegalStateException("PNGEncodeParam18");
      } else {
         return (int[])((int[])this.significantBits.clone());
      }
   }

   public void unsetSignificantBits() {
      this.significantBits = null;
      this.significantBitsSet = false;
   }

   public boolean isSignificantBitsSet() {
      return this.significantBitsSet;
   }

   public void setSRGBIntent(int var1) {
      this.SRGBIntent = var1;
      this.SRGBIntentSet = true;
   }

   public int getSRGBIntent() {
      if (!this.SRGBIntentSet) {
         throw new IllegalStateException("PNGEncodeParam19");
      } else {
         return this.SRGBIntent;
      }
   }

   public void unsetSRGBIntent() {
      this.SRGBIntentSet = false;
   }

   public boolean isSRGBIntentSet() {
      return this.SRGBIntentSet;
   }

   public void setText(String[] var1) {
      this.text = var1;
      this.textSet = true;
   }

   public String[] getText() {
      if (!this.textSet) {
         throw new IllegalStateException("PNGEncodeParam20");
      } else {
         return this.text;
      }
   }

   public void unsetText() {
      this.text = null;
      this.textSet = false;
   }

   public boolean isTextSet() {
      return this.textSet;
   }

   public void setModificationTime(Date var1) {
      this.modificationTime = var1;
      this.modificationTimeSet = true;
   }

   public Date getModificationTime() {
      if (!this.modificationTimeSet) {
         throw new IllegalStateException("PNGEncodeParam21");
      } else {
         return this.modificationTime;
      }
   }

   public void unsetModificationTime() {
      this.modificationTime = null;
      this.modificationTimeSet = false;
   }

   public boolean isModificationTimeSet() {
      return this.modificationTimeSet;
   }

   public void unsetTransparency() {
      this.transparencySet = false;
   }

   public boolean isTransparencySet() {
      return this.transparencySet;
   }

   public void setCompressedText(String[] var1) {
      this.zText = var1;
      this.zTextSet = true;
   }

   public String[] getCompressedText() {
      if (!this.zTextSet) {
         throw new IllegalStateException("PNGEncodeParam22");
      } else {
         return this.zText;
      }
   }

   public void unsetCompressedText() {
      this.zText = null;
      this.zTextSet = false;
   }

   public boolean isCompressedTextSet() {
      return this.zTextSet;
   }

   public synchronized void addPrivateChunk(String var1, byte[] var2) {
      this.chunkType.add(var1);
      this.chunkData.add(var2.clone());
   }

   public synchronized int getNumPrivateChunks() {
      return this.chunkType.size();
   }

   public synchronized String getPrivateChunkType(int var1) {
      return (String)this.chunkType.get(var1);
   }

   public synchronized byte[] getPrivateChunkData(int var1) {
      return (byte[])((byte[])this.chunkData.get(var1));
   }

   public synchronized void removeUnsafeToCopyPrivateChunks() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      int var3 = this.getNumPrivateChunks();

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = this.getPrivateChunkType(var4);
         char var6 = var5.charAt(3);
         if (var6 >= 'a' && var6 <= 'z') {
            var1.add(var5);
            var2.add(this.getPrivateChunkData(var4));
         }
      }

      this.chunkType = var1;
      this.chunkData = var2;
   }

   public synchronized void removeAllPrivateChunks() {
      this.chunkType = new ArrayList();
      this.chunkData = new ArrayList();
   }

   private static final int abs(int var0) {
      return var0 < 0 ? -var0 : var0;
   }

   public static final int paethPredictor(int var0, int var1, int var2) {
      int var3 = var0 + var1 - var2;
      int var4 = abs(var3 - var0);
      int var5 = abs(var3 - var1);
      int var6 = abs(var3 - var2);
      if (var4 <= var5 && var4 <= var6) {
         return var0;
      } else {
         return var5 <= var6 ? var1 : var2;
      }
   }

   public int filterRow(byte[] var1, byte[] var2, byte[][] var3, int var4, int var5) {
      int[] var6 = new int[]{0, 0, 0, 0, 0};

      int var15;
      for(var15 = var5; var15 < var4 + var5; ++var15) {
         int var7 = var1[var15] & 255;
         int var8 = var1[var15 - var5] & 255;
         int var9 = var2[var15] & 255;
         int var10 = var2[var15 - var5] & 255;
         var6[0] += var7;
         int var11 = var7 - var8;
         var3[1][var15] = (byte)var11;
         var6[1] += var11 > 0 ? var11 : -var11;
         var11 = var7 - var9;
         var3[2][var15] = (byte)var11;
         var6[2] += var11 >= 0 ? var11 : -var11;
         var11 = var7 - (var8 + var9 >> 1);
         var3[3][var15] = (byte)var11;
         var6[3] += var11 >= 0 ? var11 : -var11;
         int var12 = var9 - var10;
         int var13 = var8 - var10;
         int var14;
         if (var12 < 0) {
            if (var13 < 0) {
               if (var12 >= var13) {
                  var11 = var7 - var8;
               } else {
                  var11 = var7 - var9;
               }
            } else {
               var14 = var12 + var13;
               var12 = -var12;
               if (var12 <= var13) {
                  if (var12 <= var14) {
                     var11 = var7 - var8;
                  } else {
                     var11 = var7 - var10;
                  }
               } else if (var13 <= -var14) {
                  var11 = var7 - var9;
               } else {
                  var11 = var7 - var10;
               }
            }
         } else if (var13 < 0) {
            var13 = -var13;
            if (var12 <= var13) {
               var14 = var13 - var12;
               if (var12 <= var14) {
                  var11 = var7 - var8;
               } else if (var13 == var14) {
                  var11 = var7 - var9;
               } else {
                  var11 = var7 - var10;
               }
            } else {
               var14 = var12 - var13;
               if (var13 <= var14) {
                  var11 = var7 - var9;
               } else {
                  var11 = var7 - var10;
               }
            }
         } else if (var12 <= var13) {
            var11 = var7 - var8;
         } else {
            var11 = var7 - var9;
         }

         var3[4][var15] = (byte)var11;
         var6[4] += var11 >= 0 ? var11 : -var11;
      }

      var15 = 0;
      int var16 = var6[0];

      for(int var17 = 1; var17 < 5; ++var17) {
         if (var6[var17] < var16) {
            var16 = var6[var17];
            var15 = var17;
         }
      }

      if (var15 == 0) {
         System.arraycopy(var1, var5, var3[0], var5, var4);
      }

      return var15;
   }

   public static class RGB extends mxPngEncodeParam {
      private boolean backgroundSet = false;
      private int[] backgroundRGB;
      private int[] transparency;

      public void unsetBackground() {
         this.backgroundSet = false;
      }

      public boolean isBackgroundSet() {
         return this.backgroundSet;
      }

      public void setBitDepth(int var1) {
         if (var1 != 8 && var1 != 16) {
            throw new RuntimeException();
         } else {
            this.bitDepth = var1;
            this.bitDepthSet = true;
         }
      }

      public void setBackgroundRGB(int[] var1) {
         if (var1.length != 3) {
            throw new RuntimeException();
         } else {
            this.backgroundRGB = var1;
            this.backgroundSet = true;
         }
      }

      public int[] getBackgroundRGB() {
         if (!this.backgroundSet) {
            throw new IllegalStateException("PNGEncodeParam9");
         } else {
            return this.backgroundRGB;
         }
      }

      public void setTransparentRGB(int[] var1) {
         this.transparency = (int[])((int[])var1.clone());
         this.transparencySet = true;
      }

      public int[] getTransparentRGB() {
         if (!this.transparencySet) {
            throw new IllegalStateException("PNGEncodeParam10");
         } else {
            return (int[])((int[])this.transparency.clone());
         }
      }
   }

   public static class Gray extends mxPngEncodeParam {
      private boolean backgroundSet = false;
      private int backgroundPaletteGray;
      private int[] transparency;
      private int bitShift;
      private boolean bitShiftSet = false;

      public void unsetBackground() {
         this.backgroundSet = false;
      }

      public boolean isBackgroundSet() {
         return this.backgroundSet;
      }

      public void setBitDepth(int var1) {
         if (var1 != 1 && var1 != 2 && var1 != 4 && var1 != 8 && var1 != 16) {
            throw new IllegalArgumentException();
         } else {
            this.bitDepth = var1;
            this.bitDepthSet = true;
         }
      }

      public void setBackgroundGray(int var1) {
         this.backgroundPaletteGray = var1;
         this.backgroundSet = true;
      }

      public int getBackgroundGray() {
         if (!this.backgroundSet) {
            throw new IllegalStateException("PNGEncodeParam6");
         } else {
            return this.backgroundPaletteGray;
         }
      }

      public void setTransparentGray(int var1) {
         this.transparency = new int[1];
         this.transparency[0] = var1;
         this.transparencySet = true;
      }

      public int getTransparentGray() {
         if (!this.transparencySet) {
            throw new IllegalStateException("PNGEncodeParam7");
         } else {
            int var1 = this.transparency[0];
            return var1;
         }
      }

      public void setBitShift(int var1) {
         if (var1 < 0) {
            throw new RuntimeException();
         } else {
            this.bitShift = var1;
            this.bitShiftSet = true;
         }
      }

      public int getBitShift() {
         if (!this.bitShiftSet) {
            throw new IllegalStateException("PNGEncodeParam8");
         } else {
            return this.bitShift;
         }
      }

      public void unsetBitShift() {
         this.bitShiftSet = false;
      }

      public boolean isBitShiftSet() {
         return this.bitShiftSet;
      }

      public boolean isBitDepthSet() {
         return this.bitDepthSet;
      }
   }

   public static class Palette extends mxPngEncodeParam {
      private boolean backgroundSet = false;
      private int[] palette = null;
      private boolean paletteSet = false;
      private int backgroundPaletteIndex;
      private int[] transparency;

      public void unsetBackground() {
         this.backgroundSet = false;
      }

      public boolean isBackgroundSet() {
         return this.backgroundSet;
      }

      public void setBitDepth(int var1) {
         if (var1 != 1 && var1 != 2 && var1 != 4 && var1 != 8) {
            throw new IllegalArgumentException("PNGEncodeParam2");
         } else {
            this.bitDepth = var1;
            this.bitDepthSet = true;
         }
      }

      public void setPalette(int[] var1) {
         if (var1.length >= 3 && var1.length <= 768) {
            if (var1.length % 3 != 0) {
               throw new IllegalArgumentException("PNGEncodeParam1");
            } else {
               this.palette = (int[])((int[])var1.clone());
               this.paletteSet = true;
            }
         } else {
            throw new IllegalArgumentException("PNGEncodeParam0");
         }
      }

      public int[] getPalette() {
         if (!this.paletteSet) {
            throw new IllegalStateException("PNGEncodeParam3");
         } else {
            return (int[])((int[])this.palette.clone());
         }
      }

      public void unsetPalette() {
         this.palette = null;
         this.paletteSet = false;
      }

      public boolean isPaletteSet() {
         return this.paletteSet;
      }

      public void setBackgroundPaletteIndex(int var1) {
         this.backgroundPaletteIndex = var1;
         this.backgroundSet = true;
      }

      public int getBackgroundPaletteIndex() {
         if (!this.backgroundSet) {
            throw new IllegalStateException("PNGEncodeParam4");
         } else {
            return this.backgroundPaletteIndex;
         }
      }

      public void setPaletteTransparency(byte[] var1) {
         this.transparency = new int[var1.length];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.transparency[var2] = var1[var2] & 255;
         }

         this.transparencySet = true;
      }

      public byte[] getPaletteTransparency() {
         if (!this.transparencySet) {
            throw new IllegalStateException("PNGEncodeParam5");
         } else {
            byte[] var1 = new byte[this.transparency.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               var1[var2] = (byte)this.transparency[var2];
            }

            return var1;
         }
      }
   }
}
