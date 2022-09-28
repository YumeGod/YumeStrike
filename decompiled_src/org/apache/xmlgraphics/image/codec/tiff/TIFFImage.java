package org.apache.xmlgraphics.image.codec.tiff;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.apache.xmlgraphics.image.rendered.AbstractRed;
import org.apache.xmlgraphics.image.rendered.CachableRed;

public class TIFFImage extends AbstractRed {
   public static final int COMP_NONE = 1;
   public static final int COMP_FAX_G3_1D = 2;
   public static final int COMP_FAX_G3_2D = 3;
   public static final int COMP_FAX_G4_2D = 4;
   public static final int COMP_LZW = 5;
   public static final int COMP_JPEG_OLD = 6;
   public static final int COMP_JPEG_TTN2 = 7;
   public static final int COMP_PACKBITS = 32773;
   public static final int COMP_DEFLATE = 32946;
   private static final int TYPE_UNSUPPORTED = -1;
   private static final int TYPE_BILEVEL = 0;
   private static final int TYPE_GRAY_4BIT = 1;
   private static final int TYPE_GRAY = 2;
   private static final int TYPE_GRAY_ALPHA = 3;
   private static final int TYPE_PALETTE = 4;
   private static final int TYPE_RGB = 5;
   private static final int TYPE_RGB_ALPHA = 6;
   private static final int TYPE_YCBCR_SUB = 7;
   private static final int TYPE_GENERIC = 8;
   private static final int TIFF_JPEG_TABLES = 347;
   private static final int TIFF_YCBCR_SUBSAMPLING = 530;
   SeekableStream stream;
   int tileSize;
   int tilesX;
   int tilesY;
   long[] tileOffsets;
   long[] tileByteCounts;
   char[] colormap;
   int sampleSize;
   int compression;
   byte[] palette;
   int numBands;
   int chromaSubH;
   int chromaSubV;
   long tiffT4Options;
   long tiffT6Options;
   int fillOrder;
   int predictor;
   JPEGDecodeParam decodeParam = null;
   boolean colorConvertJPEG = false;
   Inflater inflater = null;
   boolean isBigEndian;
   int imageType;
   boolean isWhiteZero = false;
   int dataType;
   boolean decodePaletteAsShorts;
   boolean tiled;
   private TIFFFaxDecoder decoder = null;
   private TIFFLZWDecoder lzwDecoder = null;

   private static final Raster decodeJPEG(byte[] data, JPEGDecodeParam decodeParam, boolean colorConvert, int minX, int minY) {
      ByteArrayInputStream jpegStream = new ByteArrayInputStream(data);
      JPEGImageDecoder decoder = decodeParam == null ? JPEGCodec.createJPEGDecoder(jpegStream) : JPEGCodec.createJPEGDecoder(jpegStream, decodeParam);

      Object jpegRaster;
      try {
         jpegRaster = colorConvert ? decoder.decodeAsBufferedImage().getWritableTile(0, 0) : decoder.decodeAsRaster();
      } catch (IOException var9) {
         throw new RuntimeException("TIFFImage13");
      }

      return ((Raster)jpegRaster).createTranslatedChild(minX, minY);
   }

   private final void inflate(byte[] deflated, byte[] inflated) {
      this.inflater.setInput(deflated);

      try {
         this.inflater.inflate(inflated);
      } catch (DataFormatException var4) {
         throw new RuntimeException("TIFFImage17: " + var4.getMessage());
      }

      this.inflater.reset();
   }

   private static SampleModel createPixelInterleavedSampleModel(int dataType, int tileWidth, int tileHeight, int bands) {
      int[] bandOffsets = new int[bands];

      for(int i = 0; i < bands; bandOffsets[i] = i++) {
      }

      return new PixelInterleavedSampleModel(dataType, tileWidth, tileHeight, bands, tileWidth * bands, bandOffsets);
   }

   private final long[] getFieldAsLongs(TIFFField field) {
      long[] value = null;
      long[] value;
      if (field.getType() == 3) {
         char[] charValue = field.getAsChars();
         value = new long[charValue.length];

         for(int i = 0; i < charValue.length; ++i) {
            value[i] = (long)(charValue[i] & '\uffff');
         }
      } else {
         if (field.getType() != 4) {
            throw new RuntimeException();
         }

         value = field.getAsLongs();
      }

      return value;
   }

   public TIFFImage(SeekableStream stream, TIFFDecodeParam param, int directory) throws IOException {
      this.stream = stream;
      if (param == null) {
         param = new TIFFDecodeParam();
      }

      this.decodePaletteAsShorts = param.getDecodePaletteAsShorts();
      TIFFDirectory dir = param.getIFDOffset() == null ? new TIFFDirectory(stream, directory) : new TIFFDirectory(stream, param.getIFDOffset(), directory);
      TIFFField sfield = dir.getField(277);
      int samplesPerPixel = sfield == null ? 1 : (int)sfield.getAsLong(0);
      TIFFField planarConfigurationField = dir.getField(284);
      char[] planarConfiguration = planarConfigurationField == null ? new char[]{'\u0001'} : planarConfigurationField.getAsChars();
      if (planarConfiguration[0] != 1 && samplesPerPixel != 1) {
         throw new RuntimeException("TIFFImage0");
      } else {
         TIFFField bitsField = dir.getField(258);
         char[] bitsPerSample = null;
         char[] bitsPerSample;
         if (bitsField != null) {
            bitsPerSample = bitsField.getAsChars();
         } else {
            bitsPerSample = new char[]{'\u0001'};

            for(int i = 1; i < bitsPerSample.length; ++i) {
               if (bitsPerSample[i] != bitsPerSample[0]) {
                  throw new RuntimeException("TIFFImage1");
               }
            }
         }

         this.sampleSize = bitsPerSample[0];
         TIFFField sampleFormatField = dir.getField(339);
         char[] sampleFormat = null;
         char[] sampleFormat;
         if (sampleFormatField != null) {
            sampleFormat = sampleFormatField.getAsChars();

            for(int l = 1; l < sampleFormat.length; ++l) {
               if (sampleFormat[l] != sampleFormat[0]) {
                  throw new RuntimeException("TIFFImage2");
               }
            }
         } else {
            sampleFormat = new char[]{'\u0001'};
         }

         boolean isValidDataFormat = false;
         switch (this.sampleSize) {
            case 1:
            case 4:
            case 8:
               if (sampleFormat[0] != 3) {
                  this.dataType = 0;
                  isValidDataFormat = true;
               }
               break;
            case 16:
               if (sampleFormat[0] != 3) {
                  this.dataType = sampleFormat[0] == 2 ? 2 : 1;
                  isValidDataFormat = true;
               }
               break;
            case 32:
               if (sampleFormat[0] == 3) {
                  isValidDataFormat = false;
               } else {
                  this.dataType = 3;
                  isValidDataFormat = true;
               }
         }

         if (!isValidDataFormat) {
            throw new RuntimeException("TIFFImage3");
         } else {
            TIFFField compField = dir.getField(259);
            this.compression = compField == null ? 1 : compField.getAsInt(0);
            TIFFField photometricTypeField = dir.getField(262);
            int photometricType;
            if (photometricTypeField == null) {
               photometricType = 0;
            } else {
               photometricType = photometricTypeField.getAsInt(0);
            }

            this.imageType = -1;
            switch (photometricType) {
               case 0:
                  this.isWhiteZero = true;
               case 1:
                  if (this.sampleSize == 1 && samplesPerPixel == 1) {
                     this.imageType = 0;
                  } else if (this.sampleSize == 4 && samplesPerPixel == 1) {
                     this.imageType = 1;
                  } else if (this.sampleSize % 8 == 0) {
                     if (samplesPerPixel == 1) {
                        this.imageType = 2;
                     } else if (samplesPerPixel == 2) {
                        this.imageType = 3;
                     } else {
                        this.imageType = 8;
                     }
                  }
                  break;
               case 2:
                  if (this.sampleSize % 8 == 0) {
                     if (samplesPerPixel == 3) {
                        this.imageType = 5;
                     } else if (samplesPerPixel == 4) {
                        this.imageType = 6;
                     } else {
                        this.imageType = 8;
                     }
                  }
                  break;
               case 3:
                  if (samplesPerPixel == 1 && (this.sampleSize == 4 || this.sampleSize == 8 || this.sampleSize == 16)) {
                     this.imageType = 4;
                  }
                  break;
               case 4:
                  if (this.sampleSize == 1 && samplesPerPixel == 1) {
                     this.imageType = 0;
                  }
                  break;
               case 5:
               default:
                  if (this.sampleSize % 8 == 0) {
                     this.imageType = 8;
                  }
                  break;
               case 6:
                  if (this.compression == 7 && this.sampleSize == 8 && samplesPerPixel == 3) {
                     this.colorConvertJPEG = param.getJPEGDecompressYCbCrToRGB();
                     this.imageType = this.colorConvertJPEG ? 5 : 8;
                  } else {
                     TIFFField chromaField = dir.getField(530);
                     if (chromaField != null) {
                        this.chromaSubH = chromaField.getAsInt(0);
                        this.chromaSubV = chromaField.getAsInt(1);
                     } else {
                        this.chromaSubH = this.chromaSubV = 2;
                     }

                     if (this.chromaSubH * this.chromaSubV == 1) {
                        this.imageType = 8;
                     } else if (this.sampleSize == 8 && samplesPerPixel == 3) {
                        this.imageType = 7;
                     }
                  }
            }

            if (this.imageType == -1) {
               throw new RuntimeException("TIFFImage4");
            } else {
               Rectangle bounds = new Rectangle(0, 0, (int)dir.getFieldAsLong(256), (int)dir.getFieldAsLong(257));
               this.numBands = samplesPerPixel;
               TIFFField efield = dir.getField(338);
               int extraSamples = efield == null ? 0 : (int)efield.getAsLong(0);
               int tileWidth;
               int tileHeight;
               TIFFField fillOrderField;
               TIFFField jpegTableField;
               TIFFField t6OptionsField;
               if (dir.getField(324) != null) {
                  this.tiled = true;
                  tileWidth = (int)dir.getFieldAsLong(322);
                  tileHeight = (int)dir.getFieldAsLong(323);
                  this.tileOffsets = dir.getField(324).getAsLongs();
                  this.tileByteCounts = this.getFieldAsLongs(dir.getField(325));
               } else {
                  this.tiled = false;
                  tileWidth = dir.getField(322) != null ? (int)dir.getFieldAsLong(322) : bounds.width;
                  fillOrderField = dir.getField(278);
                  if (fillOrderField == null) {
                     tileHeight = dir.getField(323) != null ? (int)dir.getFieldAsLong(323) : bounds.height;
                  } else {
                     long l = fillOrderField.getAsLong(0);
                     long infinity = 1L;
                     infinity = (infinity << 32) - 1L;
                     if (l == infinity) {
                        tileHeight = bounds.height;
                     } else {
                        tileHeight = (int)l;
                     }
                  }

                  t6OptionsField = dir.getField(273);
                  if (t6OptionsField == null) {
                     throw new RuntimeException("TIFFImage5");
                  }

                  this.tileOffsets = this.getFieldAsLongs(t6OptionsField);
                  jpegTableField = dir.getField(279);
                  if (jpegTableField == null) {
                     throw new RuntimeException("TIFFImage6");
                  }

                  this.tileByteCounts = this.getFieldAsLongs(jpegTableField);
               }

               this.tilesX = (bounds.width + tileWidth - 1) / tileWidth;
               this.tilesY = (bounds.height + tileHeight - 1) / tileHeight;
               this.tileSize = tileWidth * tileHeight * this.numBands;
               this.isBigEndian = dir.isBigEndian();
               fillOrderField = dir.getField(266);
               if (fillOrderField != null) {
                  this.fillOrder = fillOrderField.getAsInt(0);
               } else {
                  this.fillOrder = 1;
               }

               byte[] map;
               switch (this.compression) {
                  case 1:
                  case 32773:
                     break;
                  case 2:
                  case 3:
                  case 4:
                     if (this.sampleSize != 1) {
                        throw new RuntimeException("TIFFImage7");
                     }

                     if (this.compression == 3) {
                        t6OptionsField = dir.getField(292);
                        if (t6OptionsField != null) {
                           this.tiffT4Options = t6OptionsField.getAsLong(0);
                        } else {
                           this.tiffT4Options = 0L;
                        }
                     }

                     if (this.compression == 4) {
                        t6OptionsField = dir.getField(293);
                        if (t6OptionsField != null) {
                           this.tiffT6Options = t6OptionsField.getAsLong(0);
                        } else {
                           this.tiffT6Options = 0L;
                        }
                     }

                     this.decoder = new TIFFFaxDecoder(this.fillOrder, tileWidth, tileHeight);
                     break;
                  case 5:
                     t6OptionsField = dir.getField(317);
                     if (t6OptionsField == null) {
                        this.predictor = 1;
                     } else {
                        this.predictor = t6OptionsField.getAsInt(0);
                        if (this.predictor != 1 && this.predictor != 2) {
                           throw new RuntimeException("TIFFImage8");
                        }

                        if (this.predictor == 2 && this.sampleSize != 8) {
                           throw new RuntimeException(this.sampleSize + "TIFFImage9");
                        }
                     }

                     this.lzwDecoder = new TIFFLZWDecoder(tileWidth, this.predictor, samplesPerPixel);
                     break;
                  case 6:
                     throw new RuntimeException("TIFFImage15");
                  case 7:
                     if (this.sampleSize != 8 || (this.imageType != 2 || samplesPerPixel != 1) && (this.imageType != 4 || samplesPerPixel != 1) && (this.imageType != 5 || samplesPerPixel != 3)) {
                        throw new RuntimeException("TIFFImage16");
                     }

                     if (dir.isTagPresent(347)) {
                        jpegTableField = dir.getField(347);
                        map = jpegTableField.getAsBytes();
                        ByteArrayInputStream tableStream = new ByteArrayInputStream(map);
                        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(tableStream);
                        decoder.decodeAsRaster();
                        this.decodeParam = decoder.getJPEGDecodeParam();
                     }
                     break;
                  case 32946:
                     this.inflater = new Inflater();
                     break;
                  default:
                     throw new RuntimeException("TIFFImage10");
               }

               t6OptionsField = null;
               SampleModel sampleModel = null;
               Object colorModel;
               int i;
               switch (this.imageType) {
                  case 0:
                  case 1:
                     sampleModel = new MultiPixelPackedSampleModel(this.dataType, tileWidth, tileHeight, this.sampleSize);
                     if (this.imageType == 0) {
                        map = new byte[]{(byte)(this.isWhiteZero ? 255 : 0), (byte)(this.isWhiteZero ? 0 : 255)};
                        colorModel = new IndexColorModel(1, 2, map, map, map);
                     } else {
                        map = new byte[16];
                        if (this.isWhiteZero) {
                           for(i = 0; i < map.length; ++i) {
                              map[i] = (byte)(255 - 16 * i);
                           }
                        } else {
                           for(i = 0; i < map.length; ++i) {
                              map[i] = (byte)(16 * i);
                           }
                        }

                        colorModel = new IndexColorModel(4, 16, map, map, map);
                     }
                     break;
                  case 2:
                  case 3:
                  case 5:
                  case 6:
                     int[] reverseOffsets = new int[this.numBands];

                     for(i = 0; i < this.numBands; ++i) {
                        reverseOffsets[i] = this.numBands - 1 - i;
                     }

                     sampleModel = new PixelInterleavedSampleModel(this.dataType, tileWidth, tileHeight, this.numBands, this.numBands * tileWidth, reverseOffsets);
                     if (this.imageType == 2) {
                        colorModel = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{this.sampleSize}, false, false, 1, this.dataType);
                     } else if (this.imageType == 5) {
                        colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{this.sampleSize, this.sampleSize, this.sampleSize}, false, false, 1, this.dataType);
                     } else {
                        int transparency = 1;
                        if (extraSamples == 1) {
                           transparency = 3;
                        } else if (extraSamples == 2) {
                           transparency = 2;
                        }

                        colorModel = this.createAlphaComponentColorModel(this.dataType, this.numBands, extraSamples == 1, transparency);
                     }
                     break;
                  case 4:
                     TIFFField cfield = dir.getField(320);
                     if (cfield == null) {
                        throw new RuntimeException("TIFFImage11");
                     }

                     this.colormap = cfield.getAsChars();
                     if (this.decodePaletteAsShorts) {
                        this.numBands = 3;
                        if (this.dataType == 0) {
                           this.dataType = 1;
                        }

                        sampleModel = createPixelInterleavedSampleModel(this.dataType, tileWidth, tileHeight, this.numBands);
                        colorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{16, 16, 16}, false, false, 1, this.dataType);
                     } else {
                        this.numBands = 1;
                        if (this.sampleSize == 4) {
                           sampleModel = new MultiPixelPackedSampleModel(0, tileWidth, tileHeight, this.sampleSize);
                        } else if (this.sampleSize == 8) {
                           sampleModel = createPixelInterleavedSampleModel(0, tileWidth, tileHeight, this.numBands);
                        } else if (this.sampleSize == 16) {
                           this.dataType = 1;
                           sampleModel = createPixelInterleavedSampleModel(1, tileWidth, tileHeight, this.numBands);
                        }

                        int bandLength = this.colormap.length / 3;
                        byte[] r = new byte[bandLength];
                        byte[] g = new byte[bandLength];
                        byte[] b = new byte[bandLength];
                        int gIndex = bandLength;
                        int bIndex = bandLength * 2;
                        int i;
                        if (this.dataType == 2) {
                           for(i = 0; i < bandLength; ++i) {
                              r[i] = param.decodeSigned16BitsTo8Bits((short)this.colormap[i]);
                              g[i] = param.decodeSigned16BitsTo8Bits((short)this.colormap[gIndex + i]);
                              b[i] = param.decodeSigned16BitsTo8Bits((short)this.colormap[bIndex + i]);
                           }
                        } else {
                           for(i = 0; i < bandLength; ++i) {
                              r[i] = param.decode16BitsTo8Bits(this.colormap[i] & '\uffff');
                              g[i] = param.decode16BitsTo8Bits(this.colormap[gIndex + i] & '\uffff');
                              b[i] = param.decode16BitsTo8Bits(this.colormap[bIndex + i] & '\uffff');
                           }
                        }

                        colorModel = new IndexColorModel(this.sampleSize, bandLength, r, g, b);
                     }
                     break;
                  case 7:
                  case 8:
                     int[] bandOffsets = new int[this.numBands];

                     for(int i = 0; i < this.numBands; bandOffsets[i] = i++) {
                     }

                     sampleModel = new PixelInterleavedSampleModel(this.dataType, tileWidth, tileHeight, this.numBands, this.numBands * tileWidth, bandOffsets);
                     colorModel = null;
                     break;
                  default:
                     throw new RuntimeException("TIFFImage4");
               }

               Map properties = new HashMap();
               properties.put("tiff_directory", dir);
               this.init((CachableRed)null, bounds, (ColorModel)colorModel, (SampleModel)sampleModel, 0, 0, properties);
            }
         }
      }
   }

   public TIFFDirectory getPrivateIFD(long offset) throws IOException {
      return new TIFFDirectory(this.stream, offset, 0);
   }

   public WritableRaster copyData(WritableRaster wr) {
      this.copyToRaster(wr);
      return wr;
   }

   public synchronized Raster getTile(int tileX, int tileY) {
      if (tileX >= 0 && tileX < this.tilesX && tileY >= 0 && tileY < this.tilesY) {
         byte[] bdata = null;
         short[] sdata = null;
         int[] idata = null;
         SampleModel sampleModel = this.getSampleModel();
         WritableRaster tile = this.makeTile(tileX, tileY);
         DataBuffer buffer = tile.getDataBuffer();
         int dataType = sampleModel.getDataType();
         if (dataType == 0) {
            bdata = ((DataBufferByte)buffer).getData();
         } else if (dataType == 1) {
            sdata = ((DataBufferUShort)buffer).getData();
         } else if (dataType == 2) {
            sdata = ((DataBufferShort)buffer).getData();
         } else if (dataType == 3) {
            idata = ((DataBufferInt)buffer).getData();
         }

         long save_offset = 0L;

         try {
            save_offset = this.stream.getFilePointer();
            this.stream.seek(this.tileOffsets[tileY * this.tilesX + tileX]);
         } catch (IOException var43) {
            throw new RuntimeException("TIFFImage13");
         }

         int byteCount = (int)this.tileByteCounts[tileY * this.tilesX + tileX];
         Rectangle newRect;
         if (!this.tiled) {
            newRect = tile.getBounds();
         } else {
            newRect = new Rectangle(tile.getMinX(), tile.getMinY(), this.tileWidth, this.tileHeight);
         }

         int unitsInThisTile = newRect.width * newRect.height * this.numBands;
         byte[] data = this.compression == 1 && this.imageType != 4 ? null : new byte[byteCount];
         int l;
         if (this.imageType == 0) {
            try {
               if (this.compression == 32773) {
                  this.stream.readFully(data, 0, byteCount);
                  if (newRect.width % 8 == 0) {
                     l = newRect.width / 8 * newRect.height;
                  } else {
                     l = (newRect.width / 8 + 1) * newRect.height;
                  }

                  this.decodePackbits(data, l, bdata);
               } else if (this.compression == 5) {
                  this.stream.readFully(data, 0, byteCount);
                  this.lzwDecoder.decode(data, bdata, newRect.height);
               } else if (this.compression == 2) {
                  this.stream.readFully(data, 0, byteCount);
                  this.decoder.decode1D(bdata, data, 0, newRect.height);
               } else if (this.compression == 3) {
                  this.stream.readFully(data, 0, byteCount);
                  this.decoder.decode2D(bdata, data, 0, newRect.height, this.tiffT4Options);
               } else if (this.compression == 4) {
                  this.stream.readFully(data, 0, byteCount);
                  this.decoder.decodeT6(bdata, data, 0, newRect.height, this.tiffT6Options);
               } else if (this.compression == 32946) {
                  this.stream.readFully(data, 0, byteCount);
                  this.inflate(data, bdata);
               } else if (this.compression == 1) {
                  this.stream.readFully(bdata, 0, byteCount);
               }

               this.stream.seek(save_offset);
            } catch (IOException var42) {
               throw new RuntimeException("TIFFImage13");
            }
         } else {
            int l;
            int l;
            byte[] tempData;
            int srcCount;
            int len;
            int len2;
            int i;
            int lookup;
            int count;
            int i;
            byte[] byteArray;
            byte[] tempData;
            if (this.imageType == 4) {
               int lookup;
               int bytes;
               if (this.sampleSize == 16) {
                  if (this.decodePaletteAsShorts) {
                     short[] tempData = null;
                     l = unitsInThisTile / 3;
                     l = l * 2;

                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.decodePackbits(data, l, tempData);
                           tempData = new short[l];
                           this.interpretBytesAsShorts(tempData, tempData, l);
                        } else if (this.compression == 5) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.lzwDecoder.decode(data, tempData, newRect.height);
                           tempData = new short[l];
                           this.interpretBytesAsShorts(tempData, tempData, l);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.inflate(data, tempData);
                           tempData = new short[l];
                           this.interpretBytesAsShorts(tempData, tempData, l);
                        } else if (this.compression == 1) {
                           tempData = new short[byteCount / 2];
                           this.readShorts(byteCount / 2, tempData);
                        }

                        this.stream.seek(save_offset);
                     } catch (IOException var41) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     if (dataType == 1) {
                        srcCount = 0;
                        len = this.colormap.length / 3;
                        len2 = len * 2;

                        for(i = 0; i < l; ++i) {
                           lookup = tempData[i] & '\uffff';
                           bytes = this.colormap[lookup + len2];
                           sdata[srcCount++] = (short)(bytes & '\uffff');
                           bytes = this.colormap[lookup + len];
                           sdata[srcCount++] = (short)(bytes & '\uffff');
                           bytes = this.colormap[lookup];
                           sdata[srcCount++] = (short)(bytes & '\uffff');
                        }
                     } else if (dataType == 2) {
                        srcCount = 0;
                        len = this.colormap.length / 3;
                        len2 = len * 2;

                        for(i = 0; i < l; ++i) {
                           lookup = tempData[i] & '\uffff';
                           bytes = this.colormap[lookup + len2];
                           sdata[srcCount++] = (short)bytes;
                           bytes = this.colormap[lookup + len];
                           sdata[srcCount++] = (short)bytes;
                           bytes = this.colormap[lookup];
                           sdata[srcCount++] = (short)bytes;
                        }
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(data, 0, byteCount);
                           l = unitsInThisTile * 2;
                           byteArray = new byte[l];
                           this.decodePackbits(data, l, byteArray);
                           this.interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                        } else if (this.compression == 5) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[unitsInThisTile * 2];
                           this.lzwDecoder.decode(data, tempData, newRect.height);
                           this.interpretBytesAsShorts(tempData, sdata, unitsInThisTile);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[unitsInThisTile * 2];
                           this.inflate(data, tempData);
                           this.interpretBytesAsShorts(tempData, sdata, unitsInThisTile);
                        } else if (this.compression == 1) {
                           this.readShorts(byteCount / 2, sdata);
                        }

                        this.stream.seek(save_offset);
                     } catch (IOException var40) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               } else if (this.sampleSize == 8) {
                  if (this.decodePaletteAsShorts) {
                     tempData = null;
                     l = unitsInThisTile / 3;

                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.decodePackbits(data, l, tempData);
                        } else if (this.compression == 5) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.lzwDecoder.decode(data, tempData, newRect.height);
                        } else if (this.compression == 7) {
                           this.stream.readFully(data, 0, byteCount);
                           Raster tempTile = decodeJPEG(data, this.decodeParam, this.colorConvertJPEG, tile.getMinX(), tile.getMinY());
                           int[] tempPixels = new int[l];
                           tempTile.getPixels(tile.getMinX(), tile.getMinY(), tile.getWidth(), tile.getHeight(), tempPixels);
                           tempData = new byte[l];

                           for(srcCount = 0; srcCount < l; ++srcCount) {
                              tempData[srcCount] = (byte)tempPixels[srcCount];
                           }
                        } else if (this.compression == 32946) {
                           this.stream.readFully(data, 0, byteCount);
                           tempData = new byte[l];
                           this.inflate(data, tempData);
                        } else if (this.compression == 1) {
                           tempData = new byte[byteCount];
                           this.stream.readFully(tempData, 0, byteCount);
                        }

                        this.stream.seek(save_offset);
                     } catch (IOException var44) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     bytes = 0;
                     lookup = this.colormap.length / 3;
                     len = lookup * 2;

                     for(len2 = 0; len2 < l; ++len2) {
                        srcCount = tempData[len2] & 255;
                        int cmapValue = this.colormap[srcCount + len];
                        sdata[bytes++] = (short)(cmapValue & '\uffff');
                        cmapValue = this.colormap[srcCount + lookup];
                        sdata[bytes++] = (short)(cmapValue & '\uffff');
                        cmapValue = this.colormap[srcCount];
                        sdata[bytes++] = (short)(cmapValue & '\uffff');
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(data, 0, byteCount);
                           this.decodePackbits(data, unitsInThisTile, bdata);
                        } else if (this.compression == 5) {
                           this.stream.readFully(data, 0, byteCount);
                           this.lzwDecoder.decode(data, bdata, newRect.height);
                        } else if (this.compression == 7) {
                           this.stream.readFully(data, 0, byteCount);
                           tile.setRect(decodeJPEG(data, this.decodeParam, this.colorConvertJPEG, tile.getMinX(), tile.getMinY()));
                        } else if (this.compression == 32946) {
                           this.stream.readFully(data, 0, byteCount);
                           this.inflate(data, bdata);
                        } else if (this.compression == 1) {
                           this.stream.readFully(bdata, 0, byteCount);
                        }

                        this.stream.seek(save_offset);
                     } catch (IOException var39) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               } else if (this.sampleSize == 4) {
                  l = newRect.width % 2 == 0 ? 0 : 1;
                  l = (newRect.width / 2 + l) * newRect.height;
                  if (this.decodePaletteAsShorts) {
                     byte[] tempData = null;

                     try {
                        this.stream.readFully(data, 0, byteCount);
                        this.stream.seek(save_offset);
                     } catch (IOException var38) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     if (this.compression == 32773) {
                        tempData = new byte[l];
                        this.decodePackbits(data, l, tempData);
                     } else if (this.compression == 5) {
                        tempData = new byte[l];
                        this.lzwDecoder.decode(data, tempData, newRect.height);
                     } else if (this.compression == 32946) {
                        tempData = new byte[l];
                        this.inflate(data, tempData);
                     } else if (this.compression == 1) {
                        tempData = data;
                     }

                     bytes = unitsInThisTile / 3;
                     data = new byte[bytes];
                     srcCount = 0;
                     lookup = 0;

                     for(len = 0; len < newRect.height; ++len) {
                        for(len2 = 0; len2 < newRect.width / 2; ++len2) {
                           data[lookup++] = (byte)((tempData[srcCount] & 240) >> 4);
                           data[lookup++] = (byte)(tempData[srcCount++] & 15);
                        }

                        if (l == 1) {
                           data[lookup++] = (byte)((tempData[srcCount++] & 240) >> 4);
                        }
                     }

                     len = this.colormap.length / 3;
                     len2 = len * 2;
                     count = 0;

                     for(i = 0; i < bytes; ++i) {
                        lookup = data[i] & 255;
                        int cmapValue = this.colormap[lookup + len2];
                        sdata[count++] = (short)(cmapValue & '\uffff');
                        cmapValue = this.colormap[lookup + len];
                        sdata[count++] = (short)(cmapValue & '\uffff');
                        cmapValue = this.colormap[lookup];
                        sdata[count++] = (short)(cmapValue & '\uffff');
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(data, 0, byteCount);
                           this.decodePackbits(data, l, bdata);
                        } else if (this.compression == 5) {
                           this.stream.readFully(data, 0, byteCount);
                           this.lzwDecoder.decode(data, bdata, newRect.height);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(data, 0, byteCount);
                           this.inflate(data, bdata);
                        } else if (this.compression == 1) {
                           this.stream.readFully(bdata, 0, byteCount);
                        }

                        this.stream.seek(save_offset);
                     } catch (IOException var37) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               }
            } else if (this.imageType == 1) {
               try {
                  if (this.compression == 32773) {
                     this.stream.readFully(data, 0, byteCount);
                     if (newRect.width % 8 == 0) {
                        l = newRect.width / 2 * newRect.height;
                     } else {
                        l = (newRect.width / 2 + 1) * newRect.height;
                     }

                     this.decodePackbits(data, l, bdata);
                  } else if (this.compression == 5) {
                     this.stream.readFully(data, 0, byteCount);
                     this.lzwDecoder.decode(data, bdata, newRect.height);
                  } else if (this.compression == 32946) {
                     this.stream.readFully(data, 0, byteCount);
                     this.inflate(data, bdata);
                  } else {
                     this.stream.readFully(bdata, 0, byteCount);
                  }

                  this.stream.seek(save_offset);
               } catch (IOException var36) {
                  throw new RuntimeException("TIFFImage13");
               }
            } else {
               try {
                  if (this.sampleSize == 8) {
                     if (this.compression == 1) {
                        this.stream.readFully(bdata, 0, byteCount);
                     } else if (this.compression == 5) {
                        this.stream.readFully(data, 0, byteCount);
                        this.lzwDecoder.decode(data, bdata, newRect.height);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(data, 0, byteCount);
                        this.decodePackbits(data, unitsInThisTile, bdata);
                     } else if (this.compression == 7) {
                        this.stream.readFully(data, 0, byteCount);
                        tile.setRect(decodeJPEG(data, this.decodeParam, this.colorConvertJPEG, tile.getMinX(), tile.getMinY()));
                     } else if (this.compression == 32946) {
                        this.stream.readFully(data, 0, byteCount);
                        this.inflate(data, bdata);
                     }
                  } else if (this.sampleSize == 16) {
                     if (this.compression == 1) {
                        this.readShorts(byteCount / 2, sdata);
                     } else if (this.compression == 5) {
                        this.stream.readFully(data, 0, byteCount);
                        tempData = new byte[unitsInThisTile * 2];
                        this.lzwDecoder.decode(data, tempData, newRect.height);
                        this.interpretBytesAsShorts(tempData, sdata, unitsInThisTile);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(data, 0, byteCount);
                        l = unitsInThisTile * 2;
                        byteArray = new byte[l];
                        this.decodePackbits(data, l, byteArray);
                        this.interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                     } else if (this.compression == 32946) {
                        this.stream.readFully(data, 0, byteCount);
                        tempData = new byte[unitsInThisTile * 2];
                        this.inflate(data, tempData);
                        this.interpretBytesAsShorts(tempData, sdata, unitsInThisTile);
                     }
                  } else if (this.sampleSize == 32 && dataType == 3) {
                     if (this.compression == 1) {
                        this.readInts(byteCount / 4, idata);
                     } else if (this.compression == 5) {
                        this.stream.readFully(data, 0, byteCount);
                        tempData = new byte[unitsInThisTile * 4];
                        this.lzwDecoder.decode(data, tempData, newRect.height);
                        this.interpretBytesAsInts(tempData, idata, unitsInThisTile);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(data, 0, byteCount);
                        l = unitsInThisTile * 4;
                        byteArray = new byte[l];
                        this.decodePackbits(data, l, byteArray);
                        this.interpretBytesAsInts(byteArray, idata, unitsInThisTile);
                     } else if (this.compression == 32946) {
                        this.stream.readFully(data, 0, byteCount);
                        tempData = new byte[unitsInThisTile * 4];
                        this.inflate(data, tempData);
                        this.interpretBytesAsInts(tempData, idata, unitsInThisTile);
                     }
                  }

                  this.stream.seek(save_offset);
               } catch (IOException var35) {
                  throw new RuntimeException("TIFFImage13");
               }

               byte bswap;
               short sswap;
               int iswap;
               switch (this.imageType) {
                  case 2:
                  case 3:
                     if (this.isWhiteZero) {
                        if (dataType == 0 && !(this.getColorModel() instanceof IndexColorModel)) {
                           for(l = 0; l < bdata.length; l += this.numBands) {
                              bdata[l] = (byte)(255 - bdata[l]);
                           }
                        } else if (dataType == 1) {
                           int ushortMax = '\uffff';

                           for(l = 0; l < sdata.length; l += this.numBands) {
                              sdata[l] = (short)(ushortMax - sdata[l]);
                           }
                        } else if (dataType == 2) {
                           for(l = 0; l < sdata.length; l += this.numBands) {
                              sdata[l] = (short)(~sdata[l]);
                           }
                        } else if (dataType == 3) {
                           long uintMax = 4294967295L;

                           for(l = 0; l < idata.length; l += this.numBands) {
                              idata[l] = (int)(uintMax - (long)idata[l]);
                           }
                        }
                     }
                  case 4:
                  default:
                     break;
                  case 5:
                     if (this.sampleSize == 8 && this.compression != 7) {
                        for(l = 0; l < unitsInThisTile; l += 3) {
                           bswap = bdata[l];
                           bdata[l] = bdata[l + 2];
                           bdata[l + 2] = bswap;
                        }

                        return tile;
                     } else if (this.sampleSize == 16) {
                        for(l = 0; l < unitsInThisTile; l += 3) {
                           sswap = sdata[l];
                           sdata[l] = sdata[l + 2];
                           sdata[l + 2] = sswap;
                        }

                        return tile;
                     } else {
                        if (this.sampleSize == 32 && dataType == 3) {
                           for(l = 0; l < unitsInThisTile; l += 3) {
                              iswap = idata[l];
                              idata[l] = idata[l + 2];
                              idata[l + 2] = iswap;
                           }
                        }
                        break;
                     }
                  case 6:
                     if (this.sampleSize == 8) {
                        for(l = 0; l < unitsInThisTile; l += 4) {
                           bswap = bdata[l];
                           bdata[l] = bdata[l + 3];
                           bdata[l + 3] = bswap;
                           bswap = bdata[l + 1];
                           bdata[l + 1] = bdata[l + 2];
                           bdata[l + 2] = bswap;
                        }

                        return tile;
                     } else if (this.sampleSize == 16) {
                        for(l = 0; l < unitsInThisTile; l += 4) {
                           sswap = sdata[l];
                           sdata[l] = sdata[l + 3];
                           sdata[l + 3] = sswap;
                           sswap = sdata[l + 1];
                           sdata[l + 1] = sdata[l + 2];
                           sdata[l + 2] = sswap;
                        }

                        return tile;
                     } else {
                        if (this.sampleSize == 32 && dataType == 3) {
                           for(l = 0; l < unitsInThisTile; l += 4) {
                              iswap = idata[l];
                              idata[l] = idata[l + 3];
                              idata[l + 3] = iswap;
                              iswap = idata[l + 1];
                              idata[l + 1] = idata[l + 2];
                              idata[l + 2] = iswap;
                           }
                        }
                        break;
                     }
                  case 7:
                     l = this.chromaSubH * this.chromaSubV;
                     l = newRect.width / this.chromaSubH;
                     l = newRect.height / this.chromaSubV;
                     tempData = new byte[l * l * (l + 2)];
                     System.arraycopy(bdata, 0, tempData, 0, tempData.length);
                     srcCount = l * 3;
                     int[] pixels = new int[srcCount];
                     len = 0;
                     len2 = l;
                     i = l + 1;
                     lookup = newRect.y;

                     for(count = 0; count < l; ++count) {
                        i = newRect.x;

                        for(int i = 0; i < l; ++i) {
                           int Cb = tempData[len + len2];
                           int Cr = tempData[len + i];

                           for(int k = 0; k < srcCount; pixels[k++] = Cr) {
                              pixels[k++] = tempData[len++];
                              pixels[k++] = Cb;
                           }

                           len += 2;
                           tile.setPixels(i, lookup, this.chromaSubH, this.chromaSubV, pixels);
                           i += this.chromaSubH;
                        }

                        lookup += this.chromaSubV;
                     }
               }
            }
         }

         return tile;
      } else {
         throw new IllegalArgumentException("TIFFImage12");
      }
   }

   private void readShorts(int shortCount, short[] shortArray) {
      int byteCount = 2 * shortCount;
      byte[] byteArray = new byte[byteCount];

      try {
         this.stream.readFully(byteArray, 0, byteCount);
      } catch (IOException var6) {
         throw new RuntimeException("TIFFImage13");
      }

      this.interpretBytesAsShorts(byteArray, shortArray, shortCount);
   }

   private void readInts(int intCount, int[] intArray) {
      int byteCount = 4 * intCount;
      byte[] byteArray = new byte[byteCount];

      try {
         this.stream.readFully(byteArray, 0, byteCount);
      } catch (IOException var6) {
         throw new RuntimeException("TIFFImage13");
      }

      this.interpretBytesAsInts(byteArray, intArray, intCount);
   }

   private void interpretBytesAsShorts(byte[] byteArray, short[] shortArray, int shortCount) {
      int j = 0;
      int firstByte;
      int secondByte;
      int i;
      if (this.isBigEndian) {
         for(i = 0; i < shortCount; ++i) {
            firstByte = byteArray[j++] & 255;
            secondByte = byteArray[j++] & 255;
            shortArray[i] = (short)((firstByte << 8) + secondByte);
         }
      } else {
         for(i = 0; i < shortCount; ++i) {
            firstByte = byteArray[j++] & 255;
            secondByte = byteArray[j++] & 255;
            shortArray[i] = (short)((secondByte << 8) + firstByte);
         }
      }

   }

   private void interpretBytesAsInts(byte[] byteArray, int[] intArray, int intCount) {
      int j = 0;
      int i;
      if (this.isBigEndian) {
         for(i = 0; i < intCount; ++i) {
            intArray[i] = (byteArray[j++] & 255) << 24 | (byteArray[j++] & 255) << 16 | (byteArray[j++] & 255) << 8 | byteArray[j++] & 255;
         }
      } else {
         for(i = 0; i < intCount; ++i) {
            intArray[i] = byteArray[j++] & 255 | (byteArray[j++] & 255) << 8 | (byteArray[j++] & 255) << 16 | (byteArray[j++] & 255) << 24;
         }
      }

   }

   private byte[] decodePackbits(byte[] data, int arraySize, byte[] dst) {
      if (dst == null) {
         dst = new byte[arraySize];
      }

      int srcCount = 0;
      int dstCount = 0;

      try {
         while(true) {
            while(dstCount < arraySize) {
               byte b = data[srcCount++];
               int i;
               if (b >= 0 && b <= 127) {
                  for(i = 0; i < b + 1; ++i) {
                     dst[dstCount++] = data[srcCount++];
                  }
               } else if (b <= -1 && b >= -127) {
                  byte repeat = data[srcCount++];

                  for(i = 0; i < -b + 1; ++i) {
                     dst[dstCount++] = repeat;
                  }
               } else {
                  ++srcCount;
               }
            }

            return dst;
         }
      } catch (ArrayIndexOutOfBoundsException var9) {
         throw new RuntimeException("TIFFImage14");
      }
   }

   private ComponentColorModel createAlphaComponentColorModel(int dataType, int numBands, boolean isAlphaPremultiplied, int transparency) {
      ComponentColorModel ccm = null;
      int[] RGBBits = null;
      ColorSpace cs = null;
      switch (numBands) {
         case 2:
            cs = ColorSpace.getInstance(1003);
            break;
         case 4:
            cs = ColorSpace.getInstance(1000);
            break;
         default:
            throw new IllegalArgumentException();
      }

      int componentSize = false;
      byte componentSize;
      switch (dataType) {
         case 0:
            componentSize = 8;
            break;
         case 1:
         case 2:
            componentSize = 16;
            break;
         case 3:
            componentSize = 32;
            break;
         default:
            throw new IllegalArgumentException();
      }

      int[] RGBBits = new int[numBands];

      for(int i = 0; i < numBands; ++i) {
         RGBBits[i] = componentSize;
      }

      ccm = new ComponentColorModel(cs, RGBBits, true, isAlphaPremultiplied, transparency, dataType);
      return ccm;
   }
}
