package org.apache.xmlgraphics.image.codec.png;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.xmlgraphics.image.GraphicsUtil;
import org.apache.xmlgraphics.image.codec.util.PropertyUtil;
import org.apache.xmlgraphics.image.rendered.AbstractRed;
import org.apache.xmlgraphics.image.rendered.CachableRed;

public class PNGRed extends AbstractRed {
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
   private int[][] bandOffsets;
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
   private int[] significantBits;
   private boolean suppressAlpha;
   private boolean expandPalette;
   private boolean output8BitGray;
   private boolean outputHasAlphaPalette;
   private boolean performGammaCorrection;
   private boolean expandGrayAlpha;
   private boolean generateEncodeParam;
   private PNGDecodeParam decodeParam;
   private PNGEncodeParam encodeParam;
   private boolean emitProperties;
   private float fileGamma;
   private float userExponent;
   private float displayExponent;
   private float[] chromaticity;
   private int sRGBRenderingIntent;
   private int postProcess;
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
   private Vector streamVec;
   private DataInputStream dataStream;
   private int bytesPerPixel;
   private int inputBands;
   private int outputBands;
   private int chunkIndex;
   private List textKeys;
   private List textStrings;
   private List ztextKeys;
   private List ztextStrings;
   private WritableRaster theTile;
   private Rectangle bounds;
   private Hashtable properties;
   private int[] gammaLut;
   private final byte[][] expandBits;
   private int[] grayLut;
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

   private void initGammaLut(int bits) {
      double exp = (double)this.userExponent / (double)(this.fileGamma * this.displayExponent);
      int numSamples = 1 << bits;
      int maxOutSample = bits == 16 ? '\uffff' : 255;
      this.gammaLut = new int[numSamples];

      for(int i = 0; i < numSamples; ++i) {
         double gbright = (double)i / (double)(numSamples - 1);
         double gamma = Math.pow(gbright, exp);
         int igamma = (int)(gamma * (double)maxOutSample + 0.5);
         if (igamma > maxOutSample) {
            igamma = maxOutSample;
         }

         this.gammaLut[i] = igamma;
      }

   }

   private void initGrayLut(int bits) {
      int len = 1 << bits;
      this.grayLut = new int[len];
      if (this.performGammaCorrection) {
         System.arraycopy(this.gammaLut, 0, this.grayLut, 0, len);
      } else {
         for(int i = 0; i < len; ++i) {
            this.grayLut[i] = this.expandBits[bits][i];
         }
      }

   }

   public PNGRed(InputStream stream) throws IOException {
      this(stream, (PNGDecodeParam)null);
   }

   public PNGRed(InputStream stream, PNGDecodeParam decodeParam) throws IOException {
      this.bandOffsets = new int[][]{null, {0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}};
      this.significantBits = null;
      this.suppressAlpha = false;
      this.expandPalette = false;
      this.output8BitGray = false;
      this.outputHasAlphaPalette = false;
      this.performGammaCorrection = false;
      this.expandGrayAlpha = false;
      this.generateEncodeParam = false;
      this.decodeParam = null;
      this.encodeParam = null;
      this.emitProperties = true;
      this.fileGamma = 0.45455F;
      this.userExponent = 1.0F;
      this.displayExponent = 2.2F;
      this.chromaticity = null;
      this.sRGBRenderingIntent = -1;
      this.postProcess = 0;
      this.streamVec = new Vector();
      this.chunkIndex = 0;
      this.textKeys = new ArrayList();
      this.textStrings = new ArrayList();
      this.ztextKeys = new ArrayList();
      this.ztextStrings = new ArrayList();
      this.properties = new Hashtable();
      this.gammaLut = null;
      this.expandBits = new byte[][]{null, {0, -1}, {0, 85, -86, -1}, null, {0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1}};
      this.grayLut = null;
      if (!((InputStream)stream).markSupported()) {
         stream = new BufferedInputStream((InputStream)stream);
      }

      DataInputStream distream = new DataInputStream((InputStream)stream);
      if (decodeParam == null) {
         decodeParam = new PNGDecodeParam();
      }

      this.decodeParam = decodeParam;
      this.suppressAlpha = decodeParam.getSuppressAlpha();
      this.expandPalette = decodeParam.getExpandPalette();
      this.output8BitGray = decodeParam.getOutput8BitGray();
      this.expandGrayAlpha = decodeParam.getExpandGrayAlpha();
      if (decodeParam.getPerformGammaCorrection()) {
         this.userExponent = decodeParam.getUserExponent();
         this.displayExponent = decodeParam.getDisplayExponent();
         this.performGammaCorrection = true;
         this.output8BitGray = true;
      }

      this.generateEncodeParam = decodeParam.getGenerateEncodeParam();
      if (this.emitProperties) {
         this.properties.put("file_type", "PNG v. 1.0");
      }

      String chunkType;
      String type;
      try {
         long magic = distream.readLong();
         if (magic != -8552249625308161526L) {
            type = PropertyUtil.getString("PNGImageDecoder0");
            throw new RuntimeException(type);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         chunkType = PropertyUtil.getString("PNGImageDecoder1");
         throw new RuntimeException(chunkType);
      }

      while(true) {
         try {
            chunkType = getChunkType(distream);
            PNGChunk chunk;
            if (chunkType.equals("IHDR")) {
               chunk = readChunk(distream);
               this.parse_IHDR_chunk(chunk);
            } else if (chunkType.equals("PLTE")) {
               chunk = readChunk(distream);
               this.parse_PLTE_chunk(chunk);
            } else if (chunkType.equals("IDAT")) {
               chunk = readChunk(distream);
               this.streamVec.add(new ByteArrayInputStream(chunk.getData()));
            } else {
               if (chunkType.equals("IEND")) {
                  chunk = readChunk(distream);
                  this.parse_IEND_chunk(chunk);
                  break;
               }

               if (chunkType.equals("bKGD")) {
                  chunk = readChunk(distream);
                  this.parse_bKGD_chunk(chunk);
               } else if (chunkType.equals("cHRM")) {
                  chunk = readChunk(distream);
                  this.parse_cHRM_chunk(chunk);
               } else if (chunkType.equals("gAMA")) {
                  chunk = readChunk(distream);
                  this.parse_gAMA_chunk(chunk);
               } else if (chunkType.equals("hIST")) {
                  chunk = readChunk(distream);
                  this.parse_hIST_chunk(chunk);
               } else if (chunkType.equals("iCCP")) {
                  chunk = readChunk(distream);
                  this.parse_iCCP_chunk(chunk);
               } else if (chunkType.equals("pHYs")) {
                  chunk = readChunk(distream);
                  this.parse_pHYs_chunk(chunk);
               } else if (chunkType.equals("sBIT")) {
                  chunk = readChunk(distream);
                  this.parse_sBIT_chunk(chunk);
               } else if (chunkType.equals("sRGB")) {
                  chunk = readChunk(distream);
                  this.parse_sRGB_chunk(chunk);
               } else if (chunkType.equals("tEXt")) {
                  chunk = readChunk(distream);
                  this.parse_tEXt_chunk(chunk);
               } else if (chunkType.equals("tIME")) {
                  chunk = readChunk(distream);
                  this.parse_tIME_chunk(chunk);
               } else if (chunkType.equals("tRNS")) {
                  chunk = readChunk(distream);
                  this.parse_tRNS_chunk(chunk);
               } else if (chunkType.equals("zTXt")) {
                  chunk = readChunk(distream);
                  this.parse_zTXt_chunk(chunk);
               } else {
                  chunk = readChunk(distream);
                  type = chunk.getTypeString();
                  byte[] data = chunk.getData();
                  if (this.encodeParam != null) {
                     this.encodeParam.addPrivateChunk(type, data);
                  }

                  if (this.emitProperties) {
                     String key = "chunk_" + this.chunkIndex++ + ':' + type;
                     this.properties.put(key.toLowerCase(), data);
                  }
               }
            }
         } catch (Exception var10) {
            var10.printStackTrace();
            chunkType = PropertyUtil.getString("PNGImageDecoder2");
            throw new RuntimeException(chunkType);
         }
      }

      if (this.significantBits == null) {
         this.significantBits = new int[this.inputBands];

         for(int i = 0; i < this.inputBands; ++i) {
            this.significantBits[i] = this.bitDepth;
         }

         if (this.emitProperties) {
            this.properties.put("significant_bits", this.significantBits);
         }
      }

   }

   private static String getChunkType(DataInputStream distream) {
      try {
         distream.mark(8);
         distream.readInt();
         int type = distream.readInt();
         distream.reset();
         String typeString = "" + (char)(type >> 24 & 255) + (char)(type >> 16 & 255) + (char)(type >> 8 & 255) + (char)(type & 255);
         return typeString;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private static PNGChunk readChunk(DataInputStream distream) {
      try {
         int length = distream.readInt();
         int type = distream.readInt();
         byte[] data = new byte[length];
         distream.readFully(data);
         int crc = distream.readInt();
         return new PNGChunk(length, type, data, crc);
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private void parse_IHDR_chunk(PNGChunk chunk) {
      int width = chunk.getInt4(0);
      int height = chunk.getInt4(4);
      this.bounds = new Rectangle(0, 0, width, height);
      this.bitDepth = chunk.getInt1(8);
      int validMask = 65814;
      String msg;
      if ((1 << this.bitDepth & validMask) == 0) {
         msg = PropertyUtil.getString("PNGImageDecoder3");
         throw new RuntimeException(msg);
      } else {
         this.maxOpacity = (1 << this.bitDepth) - 1;
         this.colorType = chunk.getInt1(9);
         if (this.colorType != 0 && this.colorType != 2 && this.colorType != 3 && this.colorType != 4 && this.colorType != 6) {
            System.out.println(PropertyUtil.getString("PNGImageDecoder4"));
         }

         if (this.colorType == 2 && this.bitDepth < 8) {
            msg = PropertyUtil.getString("PNGImageDecoder5");
            throw new RuntimeException(msg);
         } else if (this.colorType == 3 && this.bitDepth == 16) {
            msg = PropertyUtil.getString("PNGImageDecoder6");
            throw new RuntimeException(msg);
         } else if (this.colorType == 4 && this.bitDepth < 8) {
            msg = PropertyUtil.getString("PNGImageDecoder7");
            throw new RuntimeException(msg);
         } else if (this.colorType == 6 && this.bitDepth < 8) {
            msg = PropertyUtil.getString("PNGImageDecoder8");
            throw new RuntimeException(msg);
         } else {
            if (this.emitProperties) {
               this.properties.put("color_type", colorTypeNames[this.colorType]);
            }

            if (this.generateEncodeParam) {
               if (this.colorType == 3) {
                  this.encodeParam = new PNGEncodeParam.Palette();
               } else if (this.colorType != 0 && this.colorType != 4) {
                  this.encodeParam = new PNGEncodeParam.RGB();
               } else {
                  this.encodeParam = new PNGEncodeParam.Gray();
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
               float gamma = 0.45454544F * (this.displayExponent / this.userExponent);
               if (this.encodeParam != null) {
                  this.encodeParam.setGamma(gamma);
               }

               if (this.emitProperties) {
                  this.properties.put("gamma", new Float(gamma));
               }
            }

            this.compressionMethod = chunk.getInt1(10);
            if (this.compressionMethod != 0) {
               msg = PropertyUtil.getString("PNGImageDecoder9");
               throw new RuntimeException(msg);
            } else {
               this.filterMethod = chunk.getInt1(11);
               if (this.filterMethod != 0) {
                  msg = PropertyUtil.getString("PNGImageDecoder10");
                  throw new RuntimeException(msg);
               } else {
                  this.interlaceMethod = chunk.getInt1(12);
                  if (this.interlaceMethod == 0) {
                     if (this.encodeParam != null) {
                        this.encodeParam.setInterlacing(false);
                     }

                     if (this.emitProperties) {
                        this.properties.put("interlace_method", "None");
                     }
                  } else {
                     if (this.interlaceMethod != 1) {
                        msg = PropertyUtil.getString("PNGImageDecoder11");
                        throw new RuntimeException(msg);
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

   private void parse_IEND_chunk(PNGChunk chunk) throws Exception {
      int textLen = this.textKeys.size();
      String[] textArray = new String[2 * textLen];

      int ztextLen;
      String key;
      for(ztextLen = 0; ztextLen < textLen; ++ztextLen) {
         String key = (String)this.textKeys.get(ztextLen);
         String val = (String)this.textStrings.get(ztextLen);
         textArray[2 * ztextLen] = key;
         textArray[2 * ztextLen + 1] = val;
         if (this.emitProperties) {
            key = "text_" + ztextLen + ':' + key;
            this.properties.put(key.toLowerCase(), val);
         }
      }

      if (this.encodeParam != null) {
         this.encodeParam.setText(textArray);
      }

      ztextLen = this.ztextKeys.size();
      String[] ztextArray = new String[2 * ztextLen];

      for(int i = 0; i < ztextLen; ++i) {
         key = (String)this.ztextKeys.get(i);
         String val = (String)this.ztextStrings.get(i);
         ztextArray[2 * i] = key;
         ztextArray[2 * i + 1] = val;
         if (this.emitProperties) {
            String uniqueKey = "ztext_" + i + ':' + key;
            this.properties.put(uniqueKey.toLowerCase(), val);
         }
      }

      if (this.encodeParam != null) {
         this.encodeParam.setCompressedText(ztextArray);
      }

      InputStream seqStream = new SequenceInputStream(this.streamVec.elements());
      InputStream infStream = new InflaterInputStream(seqStream, new Inflater());
      this.dataStream = new DataInputStream(infStream);
      int depth = this.bitDepth;
      if (this.colorType == 0 && this.bitDepth < 8 && this.output8BitGray) {
         depth = 8;
      }

      if (this.colorType == 3 && this.expandPalette) {
         depth = 8;
      }

      int width = this.bounds.width;
      int height = this.bounds.height;
      int bytesPerRow = (this.outputBands * width * depth + 7) / 8;
      int scanlineStride = depth == 16 ? bytesPerRow / 2 : bytesPerRow;
      this.theTile = this.createRaster(width, height, this.outputBands, scanlineStride, depth);
      if (this.performGammaCorrection && this.gammaLut == null) {
         this.initGammaLut(this.bitDepth);
      }

      if (this.postProcess == 2 || this.postProcess == 3 || this.postProcess == 19) {
         this.initGrayLut(this.bitDepth);
      }

      this.decodeImage(this.interlaceMethod == 1);
      SampleModel sm = this.theTile.getSampleModel();
      Object cm;
      if (this.colorType == 3 && !this.expandPalette) {
         if (this.outputHasAlphaPalette) {
            cm = new IndexColorModel(this.bitDepth, this.paletteEntries, this.redPalette, this.greenPalette, this.bluePalette, this.alphaPalette);
         } else {
            cm = new IndexColorModel(this.bitDepth, this.paletteEntries, this.redPalette, this.greenPalette, this.bluePalette);
         }
      } else if (this.colorType == 0 && this.bitDepth < 8 && !this.output8BitGray) {
         byte[] palette = this.expandBits[this.bitDepth];
         cm = new IndexColorModel(this.bitDepth, palette.length, palette, palette, palette);
      } else {
         cm = createComponentColorModel(sm);
      }

      this.init((CachableRed)null, this.bounds, (ColorModel)cm, sm, 0, 0, this.properties);
   }

   public static ColorModel createComponentColorModel(SampleModel sm) {
      int type = sm.getDataType();
      int bands = sm.getNumBands();
      ComponentColorModel cm = null;
      if (type == 0) {
         switch (bands) {
            case 1:
               cm = colorModelGray8;
               break;
            case 2:
               cm = colorModelGrayAlpha8;
               break;
            case 3:
               cm = colorModelRGB8;
               break;
            case 4:
               cm = colorModelRGBA8;
         }
      } else if (type == 1) {
         switch (bands) {
            case 1:
               cm = colorModelGray16;
               break;
            case 2:
               cm = colorModelGrayAlpha16;
               break;
            case 3:
               cm = colorModelRGB16;
               break;
            case 4:
               cm = colorModelRGBA16;
         }
      } else if (type == 3) {
         switch (bands) {
            case 1:
               cm = colorModelGray32;
               break;
            case 2:
               cm = colorModelGrayAlpha32;
               break;
            case 3:
               cm = colorModelRGB32;
               break;
            case 4:
               cm = colorModelRGBA32;
         }
      }

      return cm;
   }

   private void parse_PLTE_chunk(PNGChunk chunk) {
      this.paletteEntries = chunk.getLength() / 3;
      this.redPalette = new byte[this.paletteEntries];
      this.greenPalette = new byte[this.paletteEntries];
      this.bluePalette = new byte[this.paletteEntries];
      int pltIndex = 0;
      int i;
      if (this.performGammaCorrection) {
         if (this.gammaLut == null) {
            this.initGammaLut(this.bitDepth == 16 ? 16 : 8);
         }

         for(i = 0; i < this.paletteEntries; ++i) {
            byte r = chunk.getByte(pltIndex++);
            byte g = chunk.getByte(pltIndex++);
            byte b = chunk.getByte(pltIndex++);
            this.redPalette[i] = (byte)this.gammaLut[r & 255];
            this.greenPalette[i] = (byte)this.gammaLut[g & 255];
            this.bluePalette[i] = (byte)this.gammaLut[b & 255];
         }
      } else {
         for(i = 0; i < this.paletteEntries; ++i) {
            this.redPalette[i] = chunk.getByte(pltIndex++);
            this.greenPalette[i] = chunk.getByte(pltIndex++);
            this.bluePalette[i] = chunk.getByte(pltIndex++);
         }
      }

   }

   private void parse_bKGD_chunk(PNGChunk chunk) {
      int r;
      int g;
      switch (this.colorType) {
         case 0:
         case 4:
            g = chunk.getInt2(0);
            this.bkgdRed = this.bkgdGreen = this.bkgdBlue = g;
            if (this.encodeParam != null) {
               ((PNGEncodeParam.Gray)this.encodeParam).setBackgroundGray(g);
            }
         case 1:
         case 5:
         default:
            break;
         case 2:
         case 6:
            this.bkgdRed = chunk.getInt2(0);
            this.bkgdGreen = chunk.getInt2(2);
            this.bkgdBlue = chunk.getInt2(4);
            int[] bkgdRGB = new int[]{this.bkgdRed, this.bkgdGreen, this.bkgdBlue};
            if (this.encodeParam != null) {
               ((PNGEncodeParam.RGB)this.encodeParam).setBackgroundRGB(bkgdRGB);
            }
            break;
         case 3:
            r = chunk.getByte(0) & 255;
            this.bkgdRed = this.redPalette[r] & 255;
            this.bkgdGreen = this.greenPalette[r] & 255;
            this.bkgdBlue = this.bluePalette[r] & 255;
            if (this.encodeParam != null) {
               ((PNGEncodeParam.Palette)this.encodeParam).setBackgroundPaletteIndex(r);
            }
      }

      if (this.emitProperties) {
         r = 0;
         g = 0;
         int b = 0;
         if (this.colorType != 3 && this.bitDepth != 8) {
            if (this.bitDepth < 8) {
               r = this.expandBits[this.bitDepth][this.bkgdRed];
               g = this.expandBits[this.bitDepth][this.bkgdGreen];
               b = this.expandBits[this.bitDepth][this.bkgdBlue];
            } else if (this.bitDepth == 16) {
               r = this.bkgdRed >> 8;
               g = this.bkgdGreen >> 8;
               b = this.bkgdBlue >> 8;
            }
         } else {
            r = this.bkgdRed;
            g = this.bkgdGreen;
            b = this.bkgdBlue;
         }

         this.properties.put("background_color", new Color(r, g, b));
      }

   }

   private void parse_cHRM_chunk(PNGChunk chunk) {
      if (this.sRGBRenderingIntent == -1) {
         this.chromaticity = new float[8];
         this.chromaticity[0] = (float)chunk.getInt4(0) / 100000.0F;
         this.chromaticity[1] = (float)chunk.getInt4(4) / 100000.0F;
         this.chromaticity[2] = (float)chunk.getInt4(8) / 100000.0F;
         this.chromaticity[3] = (float)chunk.getInt4(12) / 100000.0F;
         this.chromaticity[4] = (float)chunk.getInt4(16) / 100000.0F;
         this.chromaticity[5] = (float)chunk.getInt4(20) / 100000.0F;
         this.chromaticity[6] = (float)chunk.getInt4(24) / 100000.0F;
         this.chromaticity[7] = (float)chunk.getInt4(28) / 100000.0F;
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

   private void parse_gAMA_chunk(PNGChunk chunk) {
      if (this.sRGBRenderingIntent == -1) {
         this.fileGamma = (float)chunk.getInt4(0) / 100000.0F;
         float exp = this.performGammaCorrection ? this.displayExponent / this.userExponent : 1.0F;
         if (this.encodeParam != null) {
            this.encodeParam.setGamma(this.fileGamma * exp);
         }

         if (this.emitProperties) {
            this.properties.put("gamma", new Float(this.fileGamma * exp));
         }

      }
   }

   private void parse_hIST_chunk(PNGChunk chunk) {
      if (this.redPalette == null) {
         String msg = PropertyUtil.getString("PNGImageDecoder18");
         throw new RuntimeException(msg);
      } else {
         int length = this.redPalette.length;
         int[] hist = new int[length];

         for(int i = 0; i < length; ++i) {
            hist[i] = chunk.getInt2(2 * i);
         }

         if (this.encodeParam != null) {
            this.encodeParam.setPaletteHistogram(hist);
         }

      }
   }

   private void parse_iCCP_chunk(PNGChunk chunk) {
      String name = "";

      byte b;
      for(int textIndex = 0; (b = chunk.getByte(textIndex++)) != 0; name = name + (char)b) {
      }

   }

   private void parse_pHYs_chunk(PNGChunk chunk) {
      int xPixelsPerUnit = chunk.getInt4(0);
      int yPixelsPerUnit = chunk.getInt4(4);
      int unitSpecifier = chunk.getInt1(8);
      if (this.encodeParam != null) {
         this.encodeParam.setPhysicalDimension(xPixelsPerUnit, yPixelsPerUnit, unitSpecifier);
      }

      if (this.emitProperties) {
         this.properties.put("x_pixels_per_unit", new Integer(xPixelsPerUnit));
         this.properties.put("y_pixels_per_unit", new Integer(yPixelsPerUnit));
         this.properties.put("pixel_aspect_ratio", new Float((float)xPixelsPerUnit / (float)yPixelsPerUnit));
         if (unitSpecifier == 1) {
            this.properties.put("pixel_units", "Meters");
         } else if (unitSpecifier != 0) {
            String msg = PropertyUtil.getString("PNGImageDecoder12");
            throw new RuntimeException(msg);
         }
      }

   }

   private void parse_sBIT_chunk(PNGChunk chunk) {
      if (this.colorType == 3) {
         this.significantBits = new int[3];
      } else {
         this.significantBits = new int[this.inputBands];
      }

      for(int i = 0; i < this.significantBits.length; ++i) {
         int bits = chunk.getByte(i);
         int depth = this.colorType == 3 ? 8 : this.bitDepth;
         if (bits <= 0 || bits > depth) {
            String msg = PropertyUtil.getString("PNGImageDecoder13");
            throw new RuntimeException(msg);
         }

         this.significantBits[i] = bits;
      }

      if (this.encodeParam != null) {
         this.encodeParam.setSignificantBits(this.significantBits);
      }

      if (this.emitProperties) {
         this.properties.put("significant_bits", this.significantBits);
      }

   }

   private void parse_sRGB_chunk(PNGChunk chunk) {
      this.sRGBRenderingIntent = chunk.getByte(0);
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
         float gamma = this.fileGamma * (this.displayExponent / this.userExponent);
         if (this.encodeParam != null) {
            this.encodeParam.setGamma(gamma);
            this.encodeParam.setChromaticity(this.chromaticity);
         }

         if (this.emitProperties) {
            this.properties.put("gamma", new Float(gamma));
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

   private void parse_tEXt_chunk(PNGChunk chunk) {
      StringBuffer key = new StringBuffer();
      StringBuffer value = new StringBuffer();
      int textIndex = 0;

      byte b;
      while((b = chunk.getByte(textIndex++)) != 0) {
         key.append((char)b);
      }

      for(int i = textIndex; i < chunk.getLength(); ++i) {
         value.append((char)chunk.getByte(i));
      }

      this.textKeys.add(key.toString());
      this.textStrings.add(value.toString());
   }

   private void parse_tIME_chunk(PNGChunk chunk) {
      int year = chunk.getInt2(0);
      int month = chunk.getInt1(2) - 1;
      int day = chunk.getInt1(3);
      int hour = chunk.getInt1(4);
      int minute = chunk.getInt1(5);
      int second = chunk.getInt1(6);
      TimeZone gmt = TimeZone.getTimeZone("GMT");
      GregorianCalendar cal = new GregorianCalendar(gmt);
      cal.set(year, month, day, hour, minute, second);
      Date date = cal.getTime();
      if (this.encodeParam != null) {
         this.encodeParam.setModificationTime(date);
      }

      if (this.emitProperties) {
         this.properties.put("timestamp", date);
      }

   }

   private void parse_tRNS_chunk(PNGChunk chunk) {
      if (this.colorType == 3) {
         int entries = chunk.getLength();
         if (entries > this.paletteEntries) {
            String msg = PropertyUtil.getString("PNGImageDecoder14");
            throw new RuntimeException(msg);
         }

         this.alphaPalette = new byte[this.paletteEntries];

         int i;
         for(i = 0; i < entries; ++i) {
            this.alphaPalette[i] = chunk.getByte(i);
         }

         for(i = entries; i < this.paletteEntries; ++i) {
            this.alphaPalette[i] = -1;
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
         this.grayTransparentAlpha = chunk.getInt2(0);
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
               ((PNGEncodeParam.Gray)this.encodeParam).setTransparentGray(this.grayTransparentAlpha);
            }
         }
      } else if (this.colorType == 2) {
         this.redTransparentAlpha = chunk.getInt2(0);
         this.greenTransparentAlpha = chunk.getInt2(2);
         this.blueTransparentAlpha = chunk.getInt2(4);
         if (!this.suppressAlpha) {
            this.outputBands = 4;
            this.postProcess = 7;
            if (this.encodeParam != null) {
               int[] rgbTrans = new int[]{this.redTransparentAlpha, this.greenTransparentAlpha, this.blueTransparentAlpha};
               ((PNGEncodeParam.RGB)this.encodeParam).setTransparentRGB(rgbTrans);
            }
         }
      } else if (this.colorType == 4 || this.colorType == 6) {
         String msg = PropertyUtil.getString("PNGImageDecoder15");
         throw new RuntimeException(msg);
      }

   }

   private void parse_zTXt_chunk(PNGChunk chunk) {
      StringBuffer key = new StringBuffer();
      StringBuffer value = new StringBuffer();
      int textIndex = 0;

      byte b;
      while((b = chunk.getByte(textIndex++)) != 0) {
         key.append((char)b);
      }

      chunk.getByte(textIndex++);

      try {
         int length = chunk.getLength() - textIndex;
         byte[] data = chunk.getData();
         InputStream cis = new ByteArrayInputStream(data, textIndex, length);
         InputStream iis = new InflaterInputStream(cis);

         int c;
         while((c = iis.read()) != -1) {
            value.append((char)c);
         }

         this.ztextKeys.add(key.toString());
         this.ztextStrings.add(value.toString());
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   private WritableRaster createRaster(int width, int height, int bands, int scanlineStride, int bitDepth) {
      WritableRaster ras = null;
      Point origin = new Point(0, 0);
      DataBufferByte dataBuffer;
      if (bitDepth < 8 && bands == 1) {
         dataBuffer = new DataBufferByte(height * scanlineStride);
         ras = Raster.createPackedRaster(dataBuffer, width, height, bitDepth, origin);
      } else if (bitDepth <= 8) {
         dataBuffer = new DataBufferByte(height * scanlineStride);
         ras = Raster.createInterleavedRaster(dataBuffer, width, height, scanlineStride, bands, this.bandOffsets[bands], origin);
      } else {
         DataBuffer dataBuffer = new DataBufferUShort(height * scanlineStride);
         ras = Raster.createInterleavedRaster(dataBuffer, width, height, scanlineStride, bands, this.bandOffsets[bands], origin);
      }

      return ras;
   }

   private static void decodeSubFilter(byte[] curr, int count, int bpp) {
      for(int i = bpp; i < count; ++i) {
         int val = curr[i] & 255;
         val += curr[i - bpp] & 255;
         curr[i] = (byte)val;
      }

   }

   private static void decodeUpFilter(byte[] curr, byte[] prev, int count) {
      for(int i = 0; i < count; ++i) {
         int raw = curr[i] & 255;
         int prior = prev[i] & 255;
         curr[i] = (byte)(raw + prior);
      }

   }

   private static void decodeAverageFilter(byte[] curr, byte[] prev, int count, int bpp) {
      int i;
      int raw;
      int priorPixel;
      for(i = 0; i < bpp; ++i) {
         raw = curr[i] & 255;
         priorPixel = prev[i] & 255;
         curr[i] = (byte)(raw + priorPixel / 2);
      }

      for(i = bpp; i < count; ++i) {
         raw = curr[i] & 255;
         priorPixel = curr[i - bpp] & 255;
         int priorRow = prev[i] & 255;
         curr[i] = (byte)(raw + (priorPixel + priorRow) / 2);
      }

   }

   private static int paethPredictor(int a, int b, int c) {
      int p = a + b - c;
      int pa = Math.abs(p - a);
      int pb = Math.abs(p - b);
      int pc = Math.abs(p - c);
      if (pa <= pb && pa <= pc) {
         return a;
      } else {
         return pb <= pc ? b : c;
      }
   }

   private static void decodePaethFilter(byte[] curr, byte[] prev, int count, int bpp) {
      int i;
      int raw;
      int priorRow;
      for(i = 0; i < bpp; ++i) {
         raw = curr[i] & 255;
         priorRow = prev[i] & 255;
         curr[i] = (byte)(raw + priorRow);
      }

      for(i = bpp; i < count; ++i) {
         raw = curr[i] & 255;
         int priorPixel = curr[i - bpp] & 255;
         priorRow = prev[i] & 255;
         int priorRowPixel = prev[i - bpp] & 255;
         curr[i] = (byte)(raw + paethPredictor(priorPixel, priorRow, priorRowPixel));
      }

   }

   private void processPixels(int process, Raster src, WritableRaster dst, int xOffset, int step, int y, int width) {
      int[] ps = src.getPixel(0, 0, (int[])null);
      int[] pd = dst.getPixel(0, 0, (int[])null);
      int dstX = xOffset;
      int srcX;
      int r;
      int g;
      int b;
      int val;
      switch (process) {
         case 0:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               dst.setPixel(dstX, y, ps);
               dstX += step;
            }

            return;
         case 1:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);

               for(val = 0; val < this.inputBands; ++val) {
                  int x = ps[val];
                  ps[val] = this.gammaLut[x];
               }

               dst.setPixel(dstX, y, ps);
               dstX += step;
            }

            return;
         case 2:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               pd[0] = this.grayLut[ps[0]];
               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 3:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               val = ps[0];
               pd[0] = this.grayLut[val];
               if (val == this.grayTransparentAlpha) {
                  pd[1] = 0;
               } else {
                  pd[1] = this.maxOpacity;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 4:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               val = ps[0];
               pd[0] = this.redPalette[val];
               pd[1] = this.greenPalette[val];
               pd[2] = this.bluePalette[val];
               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 5:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               val = ps[0];
               pd[0] = this.redPalette[val];
               pd[1] = this.greenPalette[val];
               pd[2] = this.bluePalette[val];
               pd[3] = this.alphaPalette[val];
               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 6:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               val = ps[0];
               if (this.performGammaCorrection) {
                  val = this.gammaLut[val];
               }

               pd[0] = val;
               if (val == this.grayTransparentAlpha) {
                  pd[1] = 0;
               } else {
                  pd[1] = this.maxOpacity;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 7:
            boolean flagGammaCorrection = this.performGammaCorrection;
            int[] workGammaLut = this.gammaLut;

            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               g = ps[1];
               b = ps[2];
               if (flagGammaCorrection) {
                  pd[0] = workGammaLut[r];
                  pd[1] = workGammaLut[g];
                  pd[2] = workGammaLut[b];
               } else {
                  pd[0] = r;
                  pd[1] = g;
                  pd[2] = b;
               }

               if (r == this.redTransparentAlpha && g == this.greenTransparentAlpha && b == this.blueTransparentAlpha) {
                  pd[3] = 0;
               } else {
                  pd[3] = this.maxOpacity;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 8:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               if (this.performGammaCorrection) {
                  pd[0] = this.gammaLut[r];
               } else {
                  pd[0] = r;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 9:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               g = ps[1];
               b = ps[2];
               if (this.performGammaCorrection) {
                  pd[0] = this.gammaLut[r];
                  pd[1] = this.gammaLut[g];
                  pd[2] = this.gammaLut[b];
               } else {
                  pd[0] = r;
                  pd[1] = g;
                  pd[2] = b;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
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
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               g = ps[1];
               pd[0] = r;
               pd[1] = r;
               pd[2] = r;
               pd[3] = g;
               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 17:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               g = ps[1];
               b = this.gammaLut[r];
               pd[0] = b;
               pd[1] = b;
               pd[2] = b;
               pd[3] = g;
               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 19:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               g = this.grayLut[r];
               pd[0] = g;
               pd[1] = g;
               pd[2] = g;
               if (r == this.grayTransparentAlpha) {
                  pd[3] = 0;
               } else {
                  pd[3] = this.maxOpacity;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }

            return;
         case 22:
            for(srcX = 0; srcX < width; ++srcX) {
               src.getPixel(srcX, 0, ps);
               r = ps[0];
               if (this.performGammaCorrection) {
                  r = this.gammaLut[r];
               }

               pd[0] = r;
               pd[1] = r;
               pd[2] = r;
               if (r == this.grayTransparentAlpha) {
                  pd[3] = 0;
               } else {
                  pd[3] = this.maxOpacity;
               }

               dst.setPixel(dstX, y, pd);
               dstX += step;
            }
      }

   }

   private void decodePass(WritableRaster imRas, int xOffset, int yOffset, int xStep, int yStep, int passWidth, int passHeight) {
      if (passWidth != 0 && passHeight != 0) {
         int bytesPerRow = (this.inputBands * passWidth * this.bitDepth + 7) / 8;
         int eltsPerRow = this.bitDepth == 16 ? bytesPerRow / 2 : bytesPerRow;
         byte[] curr = new byte[bytesPerRow];
         byte[] prior = new byte[bytesPerRow];
         WritableRaster passRow = this.createRaster(passWidth, 1, this.inputBands, eltsPerRow, this.bitDepth);
         DataBuffer dataBuffer = passRow.getDataBuffer();
         int type = dataBuffer.getDataType();
         byte[] byteData = null;
         short[] shortData = null;
         if (type == 0) {
            byteData = ((DataBufferByte)dataBuffer).getData();
         } else {
            shortData = ((DataBufferUShort)dataBuffer).getData();
         }

         int srcY = 0;

         for(int dstY = yOffset; srcY < passHeight; dstY += yStep) {
            int filter = 0;

            try {
               filter = this.dataStream.read();
               this.dataStream.readFully(curr, 0, bytesPerRow);
            } catch (Exception var22) {
               var22.printStackTrace();
            }

            switch (filter) {
               case 0:
                  break;
               case 1:
                  decodeSubFilter(curr, bytesPerRow, this.bytesPerPixel);
                  break;
               case 2:
                  decodeUpFilter(curr, prior, bytesPerRow);
                  break;
               case 3:
                  decodeAverageFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
                  break;
               case 4:
                  decodePaethFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
                  break;
               default:
                  String msg = PropertyUtil.getString("PNGImageDecoder16");
                  throw new RuntimeException(msg);
            }

            if (this.bitDepth < 16) {
               System.arraycopy(curr, 0, byteData, 0, bytesPerRow);
            } else {
               int idx = 0;

               for(int j = 0; j < eltsPerRow; ++j) {
                  shortData[j] = (short)(curr[idx] << 8 | curr[idx + 1] & 255);
                  idx += 2;
               }
            }

            this.processPixels(this.postProcess, passRow, imRas, xOffset, xStep, dstY, passWidth);
            byte[] tmp = prior;
            prior = curr;
            curr = tmp;
            ++srcY;
         }

      }
   }

   private void decodeImage(boolean useInterlacing) {
      int width = this.bounds.width;
      int height = this.bounds.height;
      if (!useInterlacing) {
         this.decodePass(this.theTile, 0, 0, 1, 1, width, height);
      } else {
         this.decodePass(this.theTile, 0, 0, 8, 8, (width + 7) / 8, (height + 7) / 8);
         this.decodePass(this.theTile, 4, 0, 8, 8, (width + 3) / 8, (height + 7) / 8);
         this.decodePass(this.theTile, 0, 4, 4, 8, (width + 3) / 4, (height + 3) / 8);
         this.decodePass(this.theTile, 2, 0, 4, 4, (width + 1) / 4, (height + 3) / 4);
         this.decodePass(this.theTile, 0, 2, 2, 4, (width + 1) / 2, (height + 1) / 4);
         this.decodePass(this.theTile, 1, 0, 2, 2, width / 2, (height + 1) / 2);
         this.decodePass(this.theTile, 0, 1, 1, 2, width, height / 2);
      }

   }

   public WritableRaster copyData(WritableRaster wr) {
      GraphicsUtil.copyData((Raster)this.theTile, (WritableRaster)wr);
      return wr;
   }

   public Raster getTile(int tileX, int tileY) {
      if (tileX == 0 && tileY == 0) {
         return this.theTile;
      } else {
         String msg = PropertyUtil.getString("PNGImageDecoder17");
         throw new IllegalArgumentException(msg);
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

   static class PNGChunk {
      int length;
      int type;
      byte[] data;
      int crc;
      String typeString;

      public PNGChunk(int length, int type, byte[] data, int crc) {
         this.length = length;
         this.type = type;
         this.data = data;
         this.crc = crc;
         this.typeString = "";
         this.typeString = this.typeString + (char)(type >> 24);
         this.typeString = this.typeString + (char)(type >> 16 & 255);
         this.typeString = this.typeString + (char)(type >> 8 & 255);
         this.typeString = this.typeString + (char)(type & 255);
      }

      public int getLength() {
         return this.length;
      }

      public int getType() {
         return this.type;
      }

      public String getTypeString() {
         return this.typeString;
      }

      public byte[] getData() {
         return this.data;
      }

      public byte getByte(int offset) {
         return this.data[offset];
      }

      public int getInt1(int offset) {
         return this.data[offset] & 255;
      }

      public int getInt2(int offset) {
         return (this.data[offset] & 255) << 8 | this.data[offset + 1] & 255;
      }

      public int getInt4(int offset) {
         return (this.data[offset] & 255) << 24 | (this.data[offset + 1] & 255) << 16 | (this.data[offset + 2] & 255) << 8 | this.data[offset + 3] & 255;
      }

      public String getString4(int offset) {
         String s = new String();
         s = s + (char)this.data[offset];
         s = s + (char)this.data[offset + 1];
         s = s + (char)this.data[offset + 2];
         s = s + (char)this.data[offset + 3];
         return s;
      }

      public boolean isType(String typeName) {
         return this.typeString.equals(typeName);
      }
   }
}
