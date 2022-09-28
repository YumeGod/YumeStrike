package com.mxgraph.util.png;

import java.awt.Color;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

class PNGImage extends mxSimpleRenderedImage {
   public static final int PNG_COLOR_GRAY = 0;
   public static final int PNG_COLOR_RGB = 2;
   public static final int PNG_COLOR_PALETTE = 3;
   public static final int PNG_COLOR_GRAY_ALPHA = 4;
   public static final int PNG_COLOR_RGB_ALPHA = 6;
   private static final String[] colorTypeNames = new String[]{"Grayscale", "Error", "Truecolor", "Index", "Grayscale with alpha", "Error", "Truecolor with alpha"};
   public static final int PNG_FILTER_NONE = 0;
   public static final int PNG_FILTER_SUB = 1;
   public static final int PNG_FILTER_UP = 2;
   public static final int PNG_FILTER_AVERAGE = 3;
   public static final int PNG_FILTER_PAETH = 4;
   private int[][] bandOffsets = new int[][]{null, {0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}};
   private int bitDepth;
   private int colorType;
   private int compressionMethod;
   private int filterMethod;
   private int interlaceMethod;
   private int paletteEntries;
   private byte[] redPalette;
   private byte[] greenPalette;
   private byte[] bluePalette;
   private byte[] alphaPalette;
   private int bkgdRed;
   private int bkgdGreen;
   private int bkgdBlue;
   private int grayTransparentAlpha;
   private int redTransparentAlpha;
   private int greenTransparentAlpha;
   private int blueTransparentAlpha;
   private int maxOpacity;
   private int[] significantBits = null;
   private boolean suppressAlpha = false;
   private boolean expandPalette = false;
   private boolean output8BitGray = false;
   private boolean outputHasAlphaPalette = false;
   private boolean performGammaCorrection = false;
   private boolean expandGrayAlpha = false;
   private boolean generateEncodeParam = false;
   private mxPngDecodeParam decodeParam = null;
   private mxPngEncodeParam encodeParam = null;
   private boolean emitProperties = true;
   private float fileGamma = 0.45455F;
   private float userExponent = 1.0F;
   private float displayExponent = 2.2F;
   private float[] chromaticity = null;
   private int sRGBRenderingIntent = -1;
   private int postProcess = 0;
   private static final int POST_NONE = 0;
   private static final int POST_GAMMA = 1;
   private static final int POST_GRAY_LUT = 2;
   private static final int POST_GRAY_LUT_ADD_TRANS = 3;
   private static final int POST_PALETTE_TO_RGB = 4;
   private static final int POST_PALETTE_TO_RGBA = 5;
   private static final int POST_ADD_GRAY_TRANS = 6;
   private static final int POST_ADD_RGB_TRANS = 7;
   private static final int POST_REMOVE_GRAY_TRANS = 8;
   private static final int POST_REMOVE_RGB_TRANS = 9;
   private static final int POST_EXP_MASK = 16;
   private static final int POST_GRAY_ALPHA_EXP = 16;
   private static final int POST_GAMMA_EXP = 17;
   private static final int POST_GRAY_LUT_ADD_TRANS_EXP = 19;
   private static final int POST_ADD_GRAY_TRANS_EXP = 22;
   private List streamVec = new ArrayList();
   private DataInputStream dataStream;
   private int bytesPerPixel;
   private int inputBands;
   private int outputBands;
   private int chunkIndex = 0;
   private List textKeys = new ArrayList();
   private List textStrings = new ArrayList();
   private List ztextKeys = new ArrayList();
   private List ztextStrings = new ArrayList();
   private WritableRaster theTile;
   private int[] gammaLut = null;
   private final byte[][] expandBits = new byte[][]{null, {0, -1}, {0, 85, -86, -1}, null, {0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1}};
   private int[] grayLut = null;
   private static final int[] GrayBits8 = new int[]{8};
   private static final ComponentColorModel colorModelGray8;
   private static final int[] GrayAlphaBits8;
   private static final ComponentColorModel colorModelGrayAlpha8;
   private static final int[] GrayBits16;
   private static final ComponentColorModel colorModelGray16;
   private static final int[] GrayAlphaBits16;
   private static final ComponentColorModel colorModelGrayAlpha16;
   private static final int[] GrayBits32;
   private static final ComponentColorModel colorModelGray32;
   private static final int[] GrayAlphaBits32;
   private static final ComponentColorModel colorModelGrayAlpha32;
   private static final int[] RGBBits8;
   private static final ComponentColorModel colorModelRGB8;
   private static final int[] RGBABits8;
   private static final ComponentColorModel colorModelRGBA8;
   private static final int[] RGBBits16;
   private static final ComponentColorModel colorModelRGB16;
   private static final int[] RGBABits16;
   private static final ComponentColorModel colorModelRGBA16;
   private static final int[] RGBBits32;
   private static final ComponentColorModel colorModelRGB32;
   private static final int[] RGBABits32;
   private static final ComponentColorModel colorModelRGBA32;

   private void initGammaLut(int var1) {
      double var2 = (double)this.userExponent / (double)(this.fileGamma * this.displayExponent);
      int var4 = 1 << var1;
      int var5 = var1 == 16 ? '\uffff' : 255;
      this.gammaLut = new int[var4];

      for(int var6 = 0; var6 < var4; ++var6) {
         double var7 = (double)var6 / (double)(var4 - 1);
         double var9 = Math.pow(var7, var2);
         int var11 = (int)(var9 * (double)var5 + 0.5);
         if (var11 > var5) {
            var11 = var5;
         }

         this.gammaLut[var6] = var11;
      }

   }

   private void initGrayLut(int var1) {
      int var2 = 1 << var1;
      this.grayLut = new int[var2];
      if (this.performGammaCorrection) {
         System.arraycopy(this.gammaLut, 0, this.grayLut, 0, var2);
      } else {
         for(int var3 = 0; var3 < var2; ++var3) {
            this.grayLut[var3] = this.expandBits[var1][var3];
         }
      }

   }

   public PNGImage(InputStream var1, mxPngDecodeParam var2) throws IOException {
      if (!((InputStream)var1).markSupported()) {
         var1 = new BufferedInputStream((InputStream)var1);
      }

      DataInputStream var3 = new DataInputStream((InputStream)var1);
      if (var2 == null) {
         var2 = new mxPngDecodeParam();
      }

      this.decodeParam = var2;
      this.suppressAlpha = var2.getSuppressAlpha();
      this.expandPalette = var2.getExpandPalette();
      this.output8BitGray = var2.getOutput8BitGray();
      this.expandGrayAlpha = var2.getExpandGrayAlpha();
      if (var2.getPerformGammaCorrection()) {
         this.userExponent = var2.getUserExponent();
         this.displayExponent = var2.getDisplayExponent();
         this.performGammaCorrection = true;
         this.output8BitGray = true;
      }

      this.generateEncodeParam = var2.getGenerateEncodeParam();
      if (this.emitProperties) {
         this.properties.put("file_type", "PNG v. 1.0");
      }

      try {
         long var4 = var3.readLong();
         if (var4 != -8552249625308161526L) {
            throw new RuntimeException("PNGImageDecoder0");
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         throw new RuntimeException("PNGImageDecoder1");
      }

      while(true) {
         try {
            String var5 = getChunkType(var3);
            PNGChunk var11;
            if (var5.equals("IHDR")) {
               var11 = readChunk(var3);
               this.parse_IHDR_chunk(var11);
            } else if (var5.equals("PLTE")) {
               var11 = readChunk(var3);
               this.parse_PLTE_chunk(var11);
            } else if (var5.equals("IDAT")) {
               var11 = readChunk(var3);
               this.streamVec.add(new ByteArrayInputStream(var11.getData()));
            } else {
               if (var5.equals("IEND")) {
                  var11 = readChunk(var3);
                  this.parse_IEND_chunk(var11);
                  break;
               }

               if (var5.equals("bKGD")) {
                  var11 = readChunk(var3);
                  this.parse_bKGD_chunk(var11);
               } else if (var5.equals("cHRM")) {
                  var11 = readChunk(var3);
                  this.parse_cHRM_chunk(var11);
               } else if (var5.equals("gAMA")) {
                  var11 = readChunk(var3);
                  this.parse_gAMA_chunk(var11);
               } else if (var5.equals("hIST")) {
                  var11 = readChunk(var3);
                  this.parse_hIST_chunk(var11);
               } else if (var5.equals("iCCP")) {
                  var11 = readChunk(var3);
                  this.parse_iCCP_chunk(var11);
               } else if (var5.equals("pHYs")) {
                  var11 = readChunk(var3);
                  this.parse_pHYs_chunk(var11);
               } else if (var5.equals("sBIT")) {
                  var11 = readChunk(var3);
                  this.parse_sBIT_chunk(var11);
               } else if (var5.equals("sRGB")) {
                  var11 = readChunk(var3);
                  this.parse_sRGB_chunk(var11);
               } else if (var5.equals("tEXt")) {
                  var11 = readChunk(var3);
                  this.parse_tEXt_chunk(var11);
               } else if (var5.equals("tIME")) {
                  var11 = readChunk(var3);
                  this.parse_tIME_chunk(var11);
               } else if (var5.equals("tRNS")) {
                  var11 = readChunk(var3);
                  this.parse_tRNS_chunk(var11);
               } else if (var5.equals("zTXt")) {
                  var11 = readChunk(var3);
                  this.parse_zTXt_chunk(var11);
               } else {
                  var11 = readChunk(var3);
                  String var6 = var11.getTypeString();
                  byte[] var7 = var11.getData();
                  if (this.encodeParam != null) {
                     this.encodeParam.addPrivateChunk(var6, var7);
                  }

                  if (this.emitProperties) {
                     String var8 = "chunk_" + this.chunkIndex++ + ':' + var6;
                     this.properties.put(var8.toLowerCase(), var7);
                  }
               }
            }
         } catch (Exception var10) {
            var10.printStackTrace();
            throw new RuntimeException("PNGImageDecoder2");
         }
      }

      if (this.significantBits == null) {
         this.significantBits = new int[this.inputBands];

         for(int var12 = 0; var12 < this.inputBands; ++var12) {
            this.significantBits[var12] = this.bitDepth;
         }

         if (this.emitProperties) {
            this.properties.put("significant_bits", this.significantBits);
         }
      }

   }

   private static String getChunkType(DataInputStream var0) {
      try {
         var0.mark(8);
         var0.readInt();
         int var1 = var0.readInt();
         var0.reset();
         String var2 = "";
         var2 = var2 + (char)(var1 >> 24);
         var2 = var2 + (char)(var1 >> 16 & 255);
         var2 = var2 + (char)(var1 >> 8 & 255);
         var2 = var2 + (char)(var1 & 255);
         return var2;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private static PNGChunk readChunk(DataInputStream var0) {
      try {
         int var1 = var0.readInt();
         int var2 = var0.readInt();
         byte[] var3 = new byte[var1];
         var0.readFully(var3);
         int var4 = var0.readInt();
         return new PNGChunk(var1, var2, var3, var4);
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private void parse_IHDR_chunk(PNGChunk var1) {
      this.tileWidth = this.width = var1.getInt4(0);
      this.tileHeight = this.height = var1.getInt4(4);
      this.bitDepth = var1.getInt1(8);
      if (this.bitDepth != 1 && this.bitDepth != 2 && this.bitDepth != 4 && this.bitDepth != 8 && this.bitDepth != 16) {
         throw new RuntimeException("PNGImageDecoder3");
      } else {
         this.maxOpacity = (1 << this.bitDepth) - 1;
         this.colorType = var1.getInt1(9);
         if (this.colorType != 0 && this.colorType != 2 && this.colorType != 3 && this.colorType != 4 && this.colorType != 6) {
            System.out.println("PNGImageDecoder4");
         }

         if (this.colorType == 2 && this.bitDepth < 8) {
            throw new RuntimeException("PNGImageDecoder5");
         } else if (this.colorType == 3 && this.bitDepth == 16) {
            throw new RuntimeException("PNGImageDecoder6");
         } else if (this.colorType == 4 && this.bitDepth < 8) {
            throw new RuntimeException("PNGImageDecoder7");
         } else if (this.colorType == 6 && this.bitDepth < 8) {
            throw new RuntimeException("PNGImageDecoder8");
         } else {
            if (this.emitProperties) {
               this.properties.put("color_type", colorTypeNames[this.colorType]);
            }

            if (this.generateEncodeParam) {
               if (this.colorType == 3) {
                  this.encodeParam = new mxPngEncodeParam.Palette();
               } else if (this.colorType != 0 && this.colorType != 4) {
                  this.encodeParam = new mxPngEncodeParam.RGB();
               } else {
                  this.encodeParam = new mxPngEncodeParam.Gray();
               }

               this.decodeParam.setEncodeParam(this.encodeParam);
            }

            if (this.encodeParam != null) {
               this.encodeParam.setBitDepth(this.bitDepth);
            }

            if (this.emitProperties) {
               this.properties.put("bit_depth", new Integer(this.bitDepth));
            }

            if (this.performGammaCorrection) {
               float var2 = 0.45454544F * (this.displayExponent / this.userExponent);
               if (this.encodeParam != null) {
                  this.encodeParam.setGamma(var2);
               }

               if (this.emitProperties) {
                  this.properties.put("gamma", new Float(var2));
               }
            }

            this.compressionMethod = var1.getInt1(10);
            if (this.compressionMethod != 0) {
               throw new RuntimeException("PNGImageDecoder9");
            } else {
               this.filterMethod = var1.getInt1(11);
               if (this.filterMethod != 0) {
                  throw new RuntimeException("PNGImageDecoder10");
               } else {
                  this.interlaceMethod = var1.getInt1(12);
                  if (this.interlaceMethod == 0) {
                     if (this.encodeParam != null) {
                        this.encodeParam.setInterlacing(false);
                     }

                     if (this.emitProperties) {
                        this.properties.put("interlace_method", "None");
                     }
                  } else {
                     if (this.interlaceMethod != 1) {
                        throw new RuntimeException("PNGImageDecoder11");
                     }

                     if (this.encodeParam != null) {
                        this.encodeParam.setInterlacing(true);
                     }

                     if (this.emitProperties) {
                        this.properties.put("interlace_method", "Adam7");
                     }
                  }

                  this.bytesPerPixel = this.bitDepth == 16 ? 2 : 1;
                  switch (this.colorType) {
                     case 0:
                        this.inputBands = 1;
                        this.outputBands = 1;
                        if (this.output8BitGray && this.bitDepth < 8) {
                           this.postProcess = 2;
                        } else if (this.performGammaCorrection) {
                           this.postProcess = 1;
                        } else {
                           this.postProcess = 0;
                        }
                     case 1:
                     case 5:
                     default:
                        break;
                     case 2:
                        this.inputBands = 3;
                        this.bytesPerPixel *= 3;
                        this.outputBands = 3;
                        if (this.performGammaCorrection) {
                           this.postProcess = 1;
                        } else {
                           this.postProcess = 0;
                        }
                        break;
                     case 3:
                        this.inputBands = 1;
                        this.bytesPerPixel = 1;
                        this.outputBands = this.expandPalette ? 3 : 1;
                        if (this.expandPalette) {
                           this.postProcess = 4;
                        } else {
                           this.postProcess = 0;
                        }
                        break;
                     case 4:
                        this.inputBands = 2;
                        this.bytesPerPixel *= 2;
                        if (this.suppressAlpha) {
                           this.outputBands = 1;
                           this.postProcess = 8;
                        } else {
                           if (this.performGammaCorrection) {
                              this.postProcess = 1;
                           } else {
                              this.postProcess = 0;
                           }

                           if (this.expandGrayAlpha) {
                              this.postProcess |= 16;
                              this.outputBands = 4;
                           } else {
                              this.outputBands = 2;
                           }
                        }
                        break;
                     case 6:
                        this.inputBands = 4;
                        this.bytesPerPixel *= 4;
                        this.outputBands = !this.suppressAlpha ? 4 : 3;
                        if (this.suppressAlpha) {
                           this.postProcess = 9;
                        } else if (this.performGammaCorrection) {
                           this.postProcess = 1;
                        } else {
                           this.postProcess = 0;
                        }
                  }

               }
            }
         }
      }
   }

   private void parse_IEND_chunk(PNGChunk var1) throws Exception {
      int var2 = this.textKeys.size();
      String[] var3 = new String[2 * var2];

      int var4;
      String var7;
      for(var4 = 0; var4 < var2; ++var4) {
         String var5 = (String)this.textKeys.get(var4);
         String var6 = (String)this.textStrings.get(var4);
         var3[2 * var4] = var5;
         var3[2 * var4 + 1] = var6;
         if (this.emitProperties) {
            var7 = "text_" + var4 + ':' + var5;
            this.properties.put(var7.toLowerCase(), var6);
         }
      }

      if (this.encodeParam != null) {
         this.encodeParam.setText(var3);
      }

      var4 = this.ztextKeys.size();
      String[] var12 = new String[2 * var4];

      for(int var13 = 0; var13 < var4; ++var13) {
         var7 = (String)this.ztextKeys.get(var13);
         String var8 = (String)this.ztextStrings.get(var13);
         var12[2 * var13] = var7;
         var12[2 * var13 + 1] = var8;
         if (this.emitProperties) {
            String var9 = "ztext_" + var13 + ':' + var7;
            this.properties.put(var9.toLowerCase(), var8);
         }
      }

      if (this.encodeParam != null) {
         this.encodeParam.setCompressedText(var12);
      }

      SequenceInputStream var14 = new SequenceInputStream(Collections.enumeration(this.streamVec));
      InflaterInputStream var15 = new InflaterInputStream(var14, new Inflater());
      this.dataStream = new DataInputStream(var15);
      int var16 = this.bitDepth;
      if (this.colorType == 0 && this.bitDepth < 8 && this.output8BitGray) {
         var16 = 8;
      }

      if (this.colorType == 3 && this.expandPalette) {
         var16 = 8;
      }

      int var17 = (this.outputBands * this.width * var16 + 7) / 8;
      int var10 = var16 == 16 ? var17 / 2 : var17;
      this.theTile = this.createRaster(this.width, this.height, this.outputBands, var10, var16);
      if (this.performGammaCorrection && this.gammaLut == null) {
         this.initGammaLut(this.bitDepth);
      }

      if (this.postProcess == 2 || this.postProcess == 3 || this.postProcess == 19) {
         this.initGrayLut(this.bitDepth);
      }

      this.decodeImage(this.interlaceMethod == 1);
      this.sampleModel = this.theTile.getSampleModel();
      if (this.colorType == 3 && !this.expandPalette) {
         if (this.outputHasAlphaPalette) {
            this.colorModel = new IndexColorModel(this.bitDepth, this.paletteEntries, this.redPalette, this.greenPalette, this.bluePalette, this.alphaPalette);
         } else {
            this.colorModel = new IndexColorModel(this.bitDepth, this.paletteEntries, this.redPalette, this.greenPalette, this.bluePalette);
         }
      } else if (this.colorType == 0 && this.bitDepth < 8 && !this.output8BitGray) {
         byte[] var11 = this.expandBits[this.bitDepth];
         this.colorModel = new IndexColorModel(this.bitDepth, var11.length, var11, var11, var11);
      } else {
         this.colorModel = createComponentColorModel(this.sampleModel);
      }

   }

   public static ColorModel createComponentColorModel(SampleModel var0) {
      int var1 = var0.getDataType();
      int var2 = var0.getNumBands();
      ComponentColorModel var3 = null;
      if (var1 == 0) {
         switch (var2) {
            case 1:
               var3 = colorModelGray8;
               break;
            case 2:
               var3 = colorModelGrayAlpha8;
               break;
            case 3:
               var3 = colorModelRGB8;
               break;
            case 4:
               var3 = colorModelRGBA8;
         }
      } else if (var1 == 1) {
         switch (var2) {
            case 1:
               var3 = colorModelGray16;
               break;
            case 2:
               var3 = colorModelGrayAlpha16;
               break;
            case 3:
               var3 = colorModelRGB16;
               break;
            case 4:
               var3 = colorModelRGBA16;
         }
      } else if (var1 == 3) {
         switch (var2) {
            case 1:
               var3 = colorModelGray32;
               break;
            case 2:
               var3 = colorModelGrayAlpha32;
               break;
            case 3:
               var3 = colorModelRGB32;
               break;
            case 4:
               var3 = colorModelRGBA32;
         }
      }

      return var3;
   }

   private void parse_PLTE_chunk(PNGChunk var1) {
      this.paletteEntries = var1.getLength() / 3;
      this.redPalette = new byte[this.paletteEntries];
      this.greenPalette = new byte[this.paletteEntries];
      this.bluePalette = new byte[this.paletteEntries];
      int var2 = 0;
      int var3;
      if (this.performGammaCorrection) {
         if (this.gammaLut == null) {
            this.initGammaLut(this.bitDepth == 16 ? 16 : 8);
         }

         for(var3 = 0; var3 < this.paletteEntries; ++var3) {
            byte var4 = var1.getByte(var2++);
            byte var5 = var1.getByte(var2++);
            byte var6 = var1.getByte(var2++);
            this.redPalette[var3] = (byte)this.gammaLut[var4 & 255];
            this.greenPalette[var3] = (byte)this.gammaLut[var5 & 255];
            this.bluePalette[var3] = (byte)this.gammaLut[var6 & 255];
         }
      } else {
         for(var3 = 0; var3 < this.paletteEntries; ++var3) {
            this.redPalette[var3] = var1.getByte(var2++);
            this.greenPalette[var3] = var1.getByte(var2++);
            this.bluePalette[var3] = var1.getByte(var2++);
         }
      }

   }

   private void parse_bKGD_chunk(PNGChunk var1) {
      int var2;
      int var3;
      switch (this.colorType) {
         case 0:
         case 4:
            var3 = var1.getInt2(0);
            this.bkgdRed = this.bkgdGreen = this.bkgdBlue = var3;
            if (this.encodeParam != null) {
               ((mxPngEncodeParam.Gray)this.encodeParam).setBackgroundGray(var3);
            }
         case 1:
         case 5:
         default:
            break;
         case 2:
         case 6:
            this.bkgdRed = var1.getInt2(0);
            this.bkgdGreen = var1.getInt2(2);
            this.bkgdBlue = var1.getInt2(4);
            int[] var4 = new int[]{this.bkgdRed, this.bkgdGreen, this.bkgdBlue};
            if (this.encodeParam != null) {
               ((mxPngEncodeParam.RGB)this.encodeParam).setBackgroundRGB(var4);
            }
            break;
         case 3:
            var2 = var1.getByte(0) & 255;
            this.bkgdRed = this.redPalette[var2] & 255;
            this.bkgdGreen = this.greenPalette[var2] & 255;
            this.bkgdBlue = this.bluePalette[var2] & 255;
            if (this.encodeParam != null) {
               ((mxPngEncodeParam.Palette)this.encodeParam).setBackgroundPaletteIndex(var2);
            }
      }

      var2 = 0;
      var3 = 0;
      int var5 = 0;
      if (this.bitDepth < 8) {
         var2 = this.expandBits[this.bitDepth][this.bkgdRed];
         var3 = this.expandBits[this.bitDepth][this.bkgdGreen];
         var5 = this.expandBits[this.bitDepth][this.bkgdBlue];
      } else if (this.bitDepth == 8) {
         var2 = this.bkgdRed;
         var3 = this.bkgdGreen;
         var5 = this.bkgdBlue;
      } else if (this.bitDepth == 16) {
         var2 = this.bkgdRed >> 8;
         var3 = this.bkgdGreen >> 8;
         var5 = this.bkgdBlue >> 8;
      }

      if (this.emitProperties) {
         this.properties.put("background_color", new Color(var2, var3, var5));
      }

   }

   private void parse_cHRM_chunk(PNGChunk var1) {
      if (this.sRGBRenderingIntent == -1) {
         this.chromaticity = new float[8];
         this.chromaticity[0] = (float)var1.getInt4(0) / 100000.0F;
         this.chromaticity[1] = (float)var1.getInt4(4) / 100000.0F;
         this.chromaticity[2] = (float)var1.getInt4(8) / 100000.0F;
         this.chromaticity[3] = (float)var1.getInt4(12) / 100000.0F;
         this.chromaticity[4] = (float)var1.getInt4(16) / 100000.0F;
         this.chromaticity[5] = (float)var1.getInt4(20) / 100000.0F;
         this.chromaticity[6] = (float)var1.getInt4(24) / 100000.0F;
         this.chromaticity[7] = (float)var1.getInt4(28) / 100000.0F;
         if (this.encodeParam != null) {
            this.encodeParam.setChromaticity(this.chromaticity);
         }

         if (this.emitProperties) {
            this.properties.put("white_point_x", new Float(this.chromaticity[0]));
            this.properties.put("white_point_y", new Float(this.chromaticity[1]));
            this.properties.put("red_x", new Float(this.chromaticity[2]));
            this.properties.put("red_y", new Float(this.chromaticity[3]));
            this.properties.put("green_x", new Float(this.chromaticity[4]));
            this.properties.put("green_y", new Float(this.chromaticity[5]));
            this.properties.put("blue_x", new Float(this.chromaticity[6]));
            this.properties.put("blue_y", new Float(this.chromaticity[7]));
         }

      }
   }

   private void parse_gAMA_chunk(PNGChunk var1) {
      if (this.sRGBRenderingIntent == -1) {
         this.fileGamma = (float)var1.getInt4(0) / 100000.0F;
         float var2 = this.performGammaCorrection ? this.displayExponent / this.userExponent : 1.0F;
         if (this.encodeParam != null) {
            this.encodeParam.setGamma(this.fileGamma * var2);
         }

         if (this.emitProperties) {
            this.properties.put("gamma", new Float(this.fileGamma * var2));
         }

      }
   }

   private void parse_hIST_chunk(PNGChunk var1) {
      if (this.redPalette == null) {
         throw new RuntimeException("PNGImageDecoder18");
      } else {
         int var2 = this.redPalette.length;
         int[] var3 = new int[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1.getInt2(2 * var4);
         }

         if (this.encodeParam != null) {
            this.encodeParam.setPaletteHistogram(var3);
         }

      }
   }

   private void parse_iCCP_chunk(PNGChunk var1) {
      String var2 = "";

      byte var3;
      for(int var4 = 0; (var3 = var1.getByte(var4++)) != 0; var2 = var2 + (char)var3) {
      }

   }

   private void parse_pHYs_chunk(PNGChunk var1) {
      int var2 = var1.getInt4(0);
      int var3 = var1.getInt4(4);
      int var4 = var1.getInt1(8);
      if (this.encodeParam != null) {
         this.encodeParam.setPhysicalDimension(var2, var3, var4);
      }

      if (this.emitProperties) {
         this.properties.put("x_pixels_per_unit", new Integer(var2));
         this.properties.put("y_pixels_per_unit", new Integer(var3));
         this.properties.put("pixel_aspect_ratio", new Float((float)var2 / (float)var3));
         if (var4 == 1) {
            this.properties.put("pixel_units", "Meters");
         } else if (var4 != 0) {
            throw new RuntimeException("PNGImageDecoder12");
         }
      }

   }

   private void parse_sBIT_chunk(PNGChunk var1) {
      if (this.colorType == 3) {
         this.significantBits = new int[3];
      } else {
         this.significantBits = new int[this.inputBands];
      }

      for(int var2 = 0; var2 < this.significantBits.length; ++var2) {
         byte var3 = var1.getByte(var2);
         int var4 = this.colorType == 3 ? 8 : this.bitDepth;
         if (var3 <= 0 || var3 > var4) {
            throw new RuntimeException("PNGImageDecoder13");
         }

         this.significantBits[var2] = var3;
      }

      if (this.encodeParam != null) {
         this.encodeParam.setSignificantBits(this.significantBits);
      }

      if (this.emitProperties) {
         this.properties.put("significant_bits", this.significantBits);
      }

   }

   private void parse_sRGB_chunk(PNGChunk var1) {
      this.sRGBRenderingIntent = var1.getByte(0);
      this.fileGamma = 0.45455F;
      this.chromaticity = new float[8];
      this.chromaticity[0] = 3.127F;
      this.chromaticity[1] = 3.29F;
      this.chromaticity[2] = 6.4F;
      this.chromaticity[3] = 3.3F;
      this.chromaticity[4] = 3.0F;
      this.chromaticity[5] = 6.0F;
      this.chromaticity[6] = 1.5F;
      this.chromaticity[7] = 0.6F;
      if (this.performGammaCorrection) {
         float var2 = this.fileGamma * (this.displayExponent / this.userExponent);
         if (this.encodeParam != null) {
            this.encodeParam.setGamma(var2);
            this.encodeParam.setChromaticity(this.chromaticity);
         }

         if (this.emitProperties) {
            this.properties.put("gamma", new Float(var2));
            this.properties.put("white_point_x", new Float(this.chromaticity[0]));
            this.properties.put("white_point_y", new Float(this.chromaticity[1]));
            this.properties.put("red_x", new Float(this.chromaticity[2]));
            this.properties.put("red_y", new Float(this.chromaticity[3]));
            this.properties.put("green_x", new Float(this.chromaticity[4]));
            this.properties.put("green_y", new Float(this.chromaticity[5]));
            this.properties.put("blue_x", new Float(this.chromaticity[6]));
            this.properties.put("blue_y", new Float(this.chromaticity[7]));
         }
      }

   }

   private void parse_tEXt_chunk(PNGChunk var1) {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      byte var2;
      while((var2 = var1.getByte(var4++)) != 0) {
         var3.append((char)var2);
      }

      StringBuffer var5 = new StringBuffer();

      for(int var6 = var4; var6 < var1.getLength(); ++var6) {
         var5.append((char)var1.getByte(var6));
      }

      this.textKeys.add(var3.toString());
      this.textStrings.add(var5.toString());
   }

   private void parse_tIME_chunk(PNGChunk var1) {
      int var2 = var1.getInt2(0);
      int var3 = var1.getInt1(2) - 1;
      int var4 = var1.getInt1(3);
      int var5 = var1.getInt1(4);
      int var6 = var1.getInt1(5);
      int var7 = var1.getInt1(6);
      TimeZone var8 = TimeZone.getTimeZone("GMT");
      GregorianCalendar var9 = new GregorianCalendar(var8);
      var9.set(var2, var3, var4, var5, var6, var7);
      Date var10 = var9.getTime();
      if (this.encodeParam != null) {
         this.encodeParam.setModificationTime(var10);
      }

      if (this.emitProperties) {
         this.properties.put("timestamp", var10);
      }

   }

   private void parse_tRNS_chunk(PNGChunk var1) {
      if (this.colorType == 3) {
         int var4 = var1.getLength();
         if (var4 > this.paletteEntries) {
            throw new RuntimeException("PNGImageDecoder14");
         }

         this.alphaPalette = new byte[this.paletteEntries];

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            this.alphaPalette[var3] = var1.getByte(var3);
         }

         for(var3 = var4; var3 < this.paletteEntries; ++var3) {
            this.alphaPalette[var3] = -1;
         }

         if (!this.suppressAlpha) {
            if (this.expandPalette) {
               this.postProcess = 5;
               this.outputBands = 4;
            } else {
               this.outputHasAlphaPalette = true;
            }
         }
      } else if (this.colorType == 0) {
         this.grayTransparentAlpha = var1.getInt2(0);
         if (!this.suppressAlpha) {
            if (this.bitDepth < 8) {
               this.output8BitGray = true;
               this.maxOpacity = 255;
               this.postProcess = 3;
            } else {
               this.postProcess = 6;
            }

            if (this.expandGrayAlpha) {
               this.outputBands = 4;
               this.postProcess |= 16;
            } else {
               this.outputBands = 2;
            }

            if (this.encodeParam != null) {
               ((mxPngEncodeParam.Gray)this.encodeParam).setTransparentGray(this.grayTransparentAlpha);
            }
         }
      } else if (this.colorType == 2) {
         this.redTransparentAlpha = var1.getInt2(0);
         this.greenTransparentAlpha = var1.getInt2(2);
         this.blueTransparentAlpha = var1.getInt2(4);
         if (!this.suppressAlpha) {
            this.outputBands = 4;
            this.postProcess = 7;
            if (this.encodeParam != null) {
               int[] var2 = new int[]{this.redTransparentAlpha, this.greenTransparentAlpha, this.blueTransparentAlpha};
               ((mxPngEncodeParam.RGB)this.encodeParam).setTransparentRGB(var2);
            }
         }
      } else if (this.colorType == 4 || this.colorType == 6) {
         throw new RuntimeException("PNGImageDecoder15");
      }

   }

   private void parse_zTXt_chunk(PNGChunk var1) {
      int var2 = 0;
      StringBuffer var3 = new StringBuffer();

      byte var4;
      while((var4 = var1.getByte(var2++)) != 0) {
         var3.append((char)var4);
      }

      var1.getByte(var2++);
      StringBuffer var5 = new StringBuffer();

      try {
         int var6 = var1.getLength() - var2;
         byte[] var7 = var1.getData();
         ByteArrayInputStream var8 = new ByteArrayInputStream(var7, var2, var6);
         InflaterInputStream var9 = new InflaterInputStream(var8);

         int var10;
         while((var10 = var9.read()) != -1) {
            var5.append((char)var10);
         }

         this.ztextKeys.add(var3.toString());
         this.ztextStrings.add(var5.toString());
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   private WritableRaster createRaster(int var1, int var2, int var3, int var4, int var5) {
      WritableRaster var7 = null;
      Point var8 = new Point(0, 0);
      DataBufferByte var6;
      if (var5 < 8 && var3 == 1) {
         var6 = new DataBufferByte(var2 * var4);
         var7 = Raster.createPackedRaster(var6, var1, var2, var5, var8);
      } else if (var5 <= 8) {
         var6 = new DataBufferByte(var2 * var4);
         var7 = Raster.createInterleavedRaster(var6, var1, var2, var4, var3, this.bandOffsets[var3], var8);
      } else {
         DataBufferUShort var9 = new DataBufferUShort(var2 * var4);
         var7 = Raster.createInterleavedRaster(var9, var1, var2, var4, var3, this.bandOffsets[var3], var8);
      }

      return var7;
   }

   private static void decodeSubFilter(byte[] var0, int var1, int var2) {
      for(int var3 = var2; var3 < var1; ++var3) {
         int var4 = var0[var3] & 255;
         var4 += var0[var3 - var2] & 255;
         var0[var3] = (byte)var4;
      }

   }

   private static void decodeUpFilter(byte[] var0, byte[] var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var0[var3] & 255;
         int var5 = var1[var3] & 255;
         var0[var3] = (byte)(var4 + var5);
      }

   }

   private static void decodeAverageFilter(byte[] var0, byte[] var1, int var2, int var3) {
      int var4;
      int var6;
      int var7;
      for(var7 = 0; var7 < var3; ++var7) {
         var4 = var0[var7] & 255;
         var6 = var1[var7] & 255;
         var0[var7] = (byte)(var4 + var6 / 2);
      }

      for(var7 = var3; var7 < var2; ++var7) {
         var4 = var0[var7] & 255;
         int var5 = var0[var7 - var3] & 255;
         var6 = var1[var7] & 255;
         var0[var7] = (byte)(var4 + (var5 + var6) / 2);
      }

   }

   private static void decodePaethFilter(byte[] var0, byte[] var1, int var2, int var3) {
      int var4;
      int var6;
      int var8;
      for(var8 = 0; var8 < var3; ++var8) {
         var4 = var0[var8] & 255;
         var6 = var1[var8] & 255;
         var0[var8] = (byte)(var4 + var6);
      }

      for(var8 = var3; var8 < var2; ++var8) {
         var4 = var0[var8] & 255;
         int var5 = var0[var8 - var3] & 255;
         var6 = var1[var8] & 255;
         int var7 = var1[var8 - var3] & 255;
         var0[var8] = (byte)(var4 + mxPngEncodeParam.paethPredictor(var5, var6, var7));
      }

   }

   private void processPixels(int var1, Raster var2, WritableRaster var3, int var4, int var5, int var6, int var7) {
      int[] var10 = var2.getPixel(0, 0, (int[])null);
      int[] var11 = var3.getPixel(0, 0, (int[])null);
      int var9 = var4;
      int var8;
      int var12;
      int var13;
      int var14;
      switch (var1) {
         case 0:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var3.setPixel(var9, var6, var10);
               var9 += var5;
            }

            return;
         case 1:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);

               for(var12 = 0; var12 < this.inputBands; ++var12) {
                  var13 = var10[var12];
                  var10[var12] = this.gammaLut[var13];
               }

               var3.setPixel(var9, var6, var10);
               var9 += var5;
            }

            return;
         case 2:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var11[0] = this.grayLut[var10[0]];
               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 3:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var11[0] = this.grayLut[var12];
               if (var12 == this.grayTransparentAlpha) {
                  var11[1] = 0;
               } else {
                  var11[1] = this.maxOpacity;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 4:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var11[0] = this.redPalette[var12];
               var11[1] = this.greenPalette[var12];
               var11[2] = this.bluePalette[var12];
               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 5:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var11[0] = this.redPalette[var12];
               var11[1] = this.greenPalette[var12];
               var11[2] = this.bluePalette[var12];
               var11[3] = this.alphaPalette[var12];
               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 6:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               if (this.performGammaCorrection) {
                  var12 = this.gammaLut[var12];
               }

               var11[0] = var12;
               if (var12 == this.grayTransparentAlpha) {
                  var11[1] = 0;
               } else {
                  var11[1] = this.maxOpacity;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 7:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var13 = var10[1];
               var14 = var10[2];
               if (this.performGammaCorrection) {
                  var11[0] = this.gammaLut[var12];
                  var11[1] = this.gammaLut[var13];
                  var11[2] = this.gammaLut[var14];
               } else {
                  var11[0] = var12;
                  var11[1] = var13;
                  var11[2] = var14;
               }

               if (var12 == this.redTransparentAlpha && var13 == this.greenTransparentAlpha && var14 == this.blueTransparentAlpha) {
                  var11[3] = 0;
               } else {
                  var11[3] = this.maxOpacity;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 8:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               if (this.performGammaCorrection) {
                  var11[0] = this.gammaLut[var12];
               } else {
                  var11[0] = var12;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 9:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var13 = var10[1];
               var14 = var10[2];
               if (this.performGammaCorrection) {
                  var11[0] = this.gammaLut[var12];
                  var11[1] = this.gammaLut[var13];
                  var11[2] = this.gammaLut[var14];
               } else {
                  var11[0] = var12;
                  var11[1] = var13;
                  var11[2] = var14;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 18:
         case 20:
         case 21:
         default:
            break;
         case 16:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var13 = var10[1];
               var11[0] = var12;
               var11[1] = var12;
               var11[2] = var12;
               var11[3] = var13;
               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 17:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var13 = var10[1];
               var14 = this.gammaLut[var12];
               var11[0] = var14;
               var11[1] = var14;
               var11[2] = var14;
               var11[3] = var13;
               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 19:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               var13 = this.grayLut[var12];
               var11[0] = var13;
               var11[1] = var13;
               var11[2] = var13;
               if (var12 == this.grayTransparentAlpha) {
                  var11[3] = 0;
               } else {
                  var11[3] = this.maxOpacity;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }

            return;
         case 22:
            for(var8 = 0; var8 < var7; ++var8) {
               var2.getPixel(var8, 0, var10);
               var12 = var10[0];
               if (this.performGammaCorrection) {
                  var12 = this.gammaLut[var12];
               }

               var11[0] = var12;
               var11[1] = var12;
               var11[2] = var12;
               if (var12 == this.grayTransparentAlpha) {
                  var11[3] = 0;
               } else {
                  var11[3] = this.maxOpacity;
               }

               var3.setPixel(var9, var6, var11);
               var9 += var5;
            }
      }

   }

   private void decodePass(WritableRaster var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var6 != 0 && var7 != 0) {
         int var8 = (this.inputBands * var6 * this.bitDepth + 7) / 8;
         int var9 = this.bitDepth == 16 ? var8 / 2 : var8;
         byte[] var10 = new byte[var8];
         byte[] var11 = new byte[var8];
         WritableRaster var12 = this.createRaster(var6, 1, this.inputBands, var9, this.bitDepth);
         DataBuffer var13 = var12.getDataBuffer();
         int var14 = var13.getDataType();
         byte[] var15 = null;
         short[] var16 = null;
         if (var14 == 0) {
            var15 = ((DataBufferByte)var13).getData();
         } else {
            var16 = ((DataBufferUShort)var13).getData();
         }

         int var17 = 0;

         for(int var18 = var3; var17 < var7; var18 += var5) {
            int var19 = 0;

            try {
               var19 = this.dataStream.read();
               this.dataStream.readFully(var10, 0, var8);
            } catch (Exception var22) {
               var22.printStackTrace();
            }

            switch (var19) {
               case 0:
                  break;
               case 1:
                  decodeSubFilter(var10, var8, this.bytesPerPixel);
                  break;
               case 2:
                  decodeUpFilter(var10, var11, var8);
                  break;
               case 3:
                  decodeAverageFilter(var10, var11, var8, this.bytesPerPixel);
                  break;
               case 4:
                  decodePaethFilter(var10, var11, var8, this.bytesPerPixel);
                  break;
               default:
                  throw new RuntimeException("PNGImageDecoder16");
            }

            if (this.bitDepth < 16) {
               System.arraycopy(var10, 0, var15, 0, var8);
            } else {
               int var20 = 0;

               for(int var21 = 0; var21 < var9; ++var21) {
                  var16[var21] = (short)(var10[var20] << 8 | var10[var20 + 1] & 255);
                  var20 += 2;
               }
            }

            this.processPixels(this.postProcess, var12, var1, var2, var4, var18, var6);
            byte[] var23 = var11;
            var11 = var10;
            var10 = var23;
            ++var17;
         }

      }
   }

   private void decodeImage(boolean var1) {
      if (!var1) {
         this.decodePass(this.theTile, 0, 0, 1, 1, this.width, this.height);
      } else {
         this.decodePass(this.theTile, 0, 0, 8, 8, (this.width + 7) / 8, (this.height + 7) / 8);
         this.decodePass(this.theTile, 4, 0, 8, 8, (this.width + 3) / 8, (this.height + 7) / 8);
         this.decodePass(this.theTile, 0, 4, 4, 8, (this.width + 3) / 4, (this.height + 3) / 8);
         this.decodePass(this.theTile, 2, 0, 4, 4, (this.width + 1) / 4, (this.height + 3) / 4);
         this.decodePass(this.theTile, 0, 2, 2, 4, (this.width + 1) / 2, (this.height + 1) / 4);
         this.decodePass(this.theTile, 1, 0, 2, 2, this.width / 2, (this.height + 1) / 2);
         this.decodePass(this.theTile, 0, 1, 1, 2, this.width, this.height / 2);
      }

   }

   public Raster getTile(int var1, int var2) {
      if (var1 == 0 && var2 == 0) {
         return this.theTile;
      } else {
         throw new IllegalArgumentException("PNGImageDecoder17");
      }
   }

   static {
      colorModelGray8 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayBits8, false, false, 1, 0);
      GrayAlphaBits8 = new int[]{8, 8};
      colorModelGrayAlpha8 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayAlphaBits8, true, false, 3, 0);
      GrayBits16 = new int[]{16};
      colorModelGray16 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayBits16, false, false, 1, 1);
      GrayAlphaBits16 = new int[]{16, 16};
      colorModelGrayAlpha16 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayAlphaBits16, true, false, 3, 1);
      GrayBits32 = new int[]{32};
      colorModelGray32 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayBits32, false, false, 1, 3);
      GrayAlphaBits32 = new int[]{32, 32};
      colorModelGrayAlpha32 = new ComponentColorModel(ColorSpace.getInstance(1003), GrayAlphaBits32, true, false, 3, 3);
      RGBBits8 = new int[]{8, 8, 8};
      colorModelRGB8 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBBits8, false, false, 1, 0);
      RGBABits8 = new int[]{8, 8, 8, 8};
      colorModelRGBA8 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBABits8, true, false, 3, 0);
      RGBBits16 = new int[]{16, 16, 16};
      colorModelRGB16 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBBits16, false, false, 1, 1);
      RGBABits16 = new int[]{16, 16, 16, 16};
      colorModelRGBA16 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBABits16, true, false, 3, 1);
      RGBBits32 = new int[]{32, 32, 32};
      colorModelRGB32 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBBits32, false, false, 1, 3);
      RGBABits32 = new int[]{32, 32, 32, 32};
      colorModelRGBA32 = new ComponentColorModel(ColorSpace.getInstance(1000), RGBABits32, true, false, 3, 3);
   }
}
