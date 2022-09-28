package org.apache.batik.ext.awt.image.codec.tiff;

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
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.batik.ext.awt.image.codec.util.SeekableStream;
import org.apache.batik.ext.awt.image.rendered.AbstractRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;

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

   private static final Raster decodeJPEG(byte[] var0, JPEGDecodeParam var1, boolean var2, int var3, int var4) {
      ByteArrayInputStream var5 = new ByteArrayInputStream(var0);
      JPEGImageDecoder var6 = var1 == null ? JPEGCodec.createJPEGDecoder(var5) : JPEGCodec.createJPEGDecoder(var5, var1);

      Object var7;
      try {
         var7 = var2 ? var6.decodeAsBufferedImage().getWritableTile(0, 0) : var6.decodeAsRaster();
      } catch (IOException var9) {
         throw new RuntimeException("TIFFImage13");
      }

      return ((Raster)var7).createTranslatedChild(var3, var4);
   }

   private final void inflate(byte[] var1, byte[] var2) {
      this.inflater.setInput(var1);

      try {
         this.inflater.inflate(var2);
      } catch (DataFormatException var4) {
         throw new RuntimeException("TIFFImage17: " + var4.getMessage());
      }

      this.inflater.reset();
   }

   private static SampleModel createPixelInterleavedSampleModel(int var0, int var1, int var2, int var3) {
      int[] var4 = new int[var3];

      for(int var5 = 0; var5 < var3; var4[var5] = var5++) {
      }

      return new PixelInterleavedSampleModel(var0, var1, var2, var3, var1 * var3, var4);
   }

   private long[] getFieldAsLongs(TIFFField var1) {
      Object var2 = null;
      long[] var5;
      if (var1.getType() == 3) {
         char[] var3 = var1.getAsChars();
         var5 = new long[var3.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var5[var4] = (long)(var3[var4] & '\uffff');
         }
      } else {
         if (var1.getType() != 4) {
            throw new RuntimeException();
         }

         var5 = var1.getAsLongs();
      }

      return var5;
   }

   public TIFFImage(SeekableStream var1, TIFFDecodeParam var2, int var3) throws IOException {
      this.stream = var1;
      if (var2 == null) {
         var2 = new TIFFDecodeParam();
      }

      this.decodePaletteAsShorts = var2.getDecodePaletteAsShorts();
      TIFFDirectory var4 = var2.getIFDOffset() == null ? new TIFFDirectory(var1, var3) : new TIFFDirectory(var1, var2.getIFDOffset(), var3);
      TIFFField var5 = var4.getField(277);
      int var6 = var5 == null ? 1 : (int)var5.getAsLong(0);
      TIFFField var7 = var4.getField(284);
      char[] var8 = var7 == null ? new char[]{'\u0001'} : var7.getAsChars();
      if (var8[0] != 1 && var6 != 1) {
         throw new RuntimeException("TIFFImage0");
      } else {
         TIFFField var9 = var4.getField(258);
         Object var10 = null;
         char[] var34;
         if (var9 != null) {
            var34 = var9.getAsChars();
         } else {
            var34 = new char[]{'\u0001'};

            for(int var11 = 1; var11 < var34.length; ++var11) {
               if (var34[var11] != var34[0]) {
                  throw new RuntimeException("TIFFImage1");
               }
            }
         }

         this.sampleSize = var34[0];
         TIFFField var35 = var4.getField(339);
         Object var12 = null;
         char[] var36;
         if (var35 != null) {
            var36 = var35.getAsChars();

            for(int var13 = 1; var13 < var36.length; ++var13) {
               if (var36[var13] != var36[0]) {
                  throw new RuntimeException("TIFFImage2");
               }
            }
         } else {
            var36 = new char[]{'\u0001'};
         }

         boolean var37 = false;
         switch (this.sampleSize) {
            case 1:
            case 4:
            case 8:
               if (var36[0] != 3) {
                  this.dataType = 0;
                  var37 = true;
               }
               break;
            case 16:
               if (var36[0] != 3) {
                  this.dataType = var36[0] == 2 ? 2 : 1;
                  var37 = true;
               }
               break;
            case 32:
               if (var36[0] == 3) {
                  var37 = false;
               } else {
                  this.dataType = 3;
                  var37 = true;
               }
         }

         if (!var37) {
            throw new RuntimeException("TIFFImage3");
         } else {
            TIFFField var14 = var4.getField(259);
            this.compression = var14 == null ? 1 : var14.getAsInt(0);
            int var15 = (int)var4.getFieldAsLong(262);
            this.imageType = -1;
            switch (var15) {
               case 0:
                  this.isWhiteZero = true;
               case 1:
                  if (this.sampleSize == 1 && var6 == 1) {
                     this.imageType = 0;
                  } else if (this.sampleSize == 4 && var6 == 1) {
                     this.imageType = 1;
                  } else if (this.sampleSize % 8 == 0) {
                     if (var6 == 1) {
                        this.imageType = 2;
                     } else if (var6 == 2) {
                        this.imageType = 3;
                     } else {
                        this.imageType = 8;
                     }
                  }
                  break;
               case 2:
                  if (this.sampleSize % 8 == 0) {
                     if (var6 == 3) {
                        this.imageType = 5;
                     } else if (var6 == 4) {
                        this.imageType = 6;
                     } else {
                        this.imageType = 8;
                     }
                  }
                  break;
               case 3:
                  if (var6 == 1 && (this.sampleSize == 4 || this.sampleSize == 8 || this.sampleSize == 16)) {
                     this.imageType = 4;
                  }
                  break;
               case 4:
                  if (this.sampleSize == 1 && var6 == 1) {
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
                  if (this.compression == 7 && this.sampleSize == 8 && var6 == 3) {
                     this.colorConvertJPEG = var2.getJPEGDecompressYCbCrToRGB();
                     this.imageType = this.colorConvertJPEG ? 5 : 8;
                  } else {
                     TIFFField var16 = var4.getField(530);
                     if (var16 != null) {
                        this.chromaSubH = var16.getAsInt(0);
                        this.chromaSubV = var16.getAsInt(1);
                     } else {
                        this.chromaSubH = this.chromaSubV = 2;
                     }

                     if (this.chromaSubH * this.chromaSubV == 1) {
                        this.imageType = 8;
                     } else if (this.sampleSize == 8 && var6 == 3) {
                        this.imageType = 7;
                     }
                  }
            }

            if (this.imageType == -1) {
               throw new RuntimeException("TIFFImage4");
            } else {
               Rectangle var38 = new Rectangle(0, 0, (int)var4.getFieldAsLong(256), (int)var4.getFieldAsLong(257));
               this.numBands = var6;
               TIFFField var17 = var4.getField(338);
               int var18 = var17 == null ? 0 : (int)var17.getAsLong(0);
               int var19;
               int var20;
               TIFFField var21;
               TIFFField var23;
               TIFFField var39;
               if (var4.getField(324) != null) {
                  this.tiled = true;
                  var19 = (int)var4.getFieldAsLong(322);
                  var20 = (int)var4.getFieldAsLong(323);
                  this.tileOffsets = var4.getField(324).getAsLongs();
                  this.tileByteCounts = this.getFieldAsLongs(var4.getField(325));
               } else {
                  this.tiled = false;
                  var19 = var4.getField(322) != null ? (int)var4.getFieldAsLong(322) : var38.width;
                  var21 = var4.getField(278);
                  if (var21 == null) {
                     var20 = var4.getField(323) != null ? (int)var4.getFieldAsLong(323) : var38.height;
                  } else {
                     long var22 = var21.getAsLong(0);
                     long var24 = 1L;
                     var24 = (var24 << 32) - 1L;
                     if (var22 == var24) {
                        var20 = var38.height;
                     } else {
                        var20 = (int)var22;
                     }
                  }

                  var39 = var4.getField(273);
                  if (var39 == null) {
                     throw new RuntimeException("TIFFImage5");
                  }

                  this.tileOffsets = this.getFieldAsLongs(var39);
                  var23 = var4.getField(279);
                  if (var23 == null) {
                     throw new RuntimeException("TIFFImage6");
                  }

                  this.tileByteCounts = this.getFieldAsLongs(var23);
               }

               this.tilesX = (var38.width + var19 - 1) / var19;
               this.tilesY = (var38.height + var20 - 1) / var20;
               this.tileSize = var19 * var20 * this.numBands;
               this.isBigEndian = var4.isBigEndian();
               var21 = var4.getField(266);
               if (var21 != null) {
                  this.fillOrder = var21.getAsInt(0);
               } else {
                  this.fillOrder = 1;
               }

               byte[] var42;
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
                        var39 = var4.getField(292);
                        if (var39 != null) {
                           this.tiffT4Options = var39.getAsLong(0);
                        } else {
                           this.tiffT4Options = 0L;
                        }
                     }

                     if (this.compression == 4) {
                        var39 = var4.getField(293);
                        if (var39 != null) {
                           this.tiffT6Options = var39.getAsLong(0);
                        } else {
                           this.tiffT6Options = 0L;
                        }
                     }

                     this.decoder = new TIFFFaxDecoder(this.fillOrder, var19, var20);
                     break;
                  case 5:
                     var39 = var4.getField(317);
                     if (var39 == null) {
                        this.predictor = 1;
                     } else {
                        this.predictor = var39.getAsInt(0);
                        if (this.predictor != 1 && this.predictor != 2) {
                           throw new RuntimeException("TIFFImage8");
                        }

                        if (this.predictor == 2 && this.sampleSize != 8) {
                           throw new RuntimeException(this.sampleSize + "TIFFImage9");
                        }
                     }

                     this.lzwDecoder = new TIFFLZWDecoder(var19, this.predictor, var6);
                     break;
                  case 6:
                     throw new RuntimeException("TIFFImage15");
                  case 7:
                     if (this.sampleSize != 8 || (this.imageType != 2 || var6 != 1) && (this.imageType != 4 || var6 != 1) && (this.imageType != 5 || var6 != 3)) {
                        throw new RuntimeException("TIFFImage16");
                     }

                     if (var4.isTagPresent(347)) {
                        var23 = var4.getField(347);
                        var42 = var23.getAsBytes();
                        ByteArrayInputStream var25 = new ByteArrayInputStream(var42);
                        JPEGImageDecoder var26 = JPEGCodec.createJPEGDecoder(var25);
                        var26.decodeAsRaster();
                        this.decodeParam = var26.getJPEGDecodeParam();
                     }
                     break;
                  case 32946:
                     this.inflater = new Inflater();
                     break;
                  default:
                     throw new RuntimeException("TIFFImage10");
               }

               var39 = null;
               Object var40 = null;
               Object var41;
               int var45;
               switch (this.imageType) {
                  case 0:
                  case 1:
                     var40 = new MultiPixelPackedSampleModel(this.dataType, var19, var20, this.sampleSize);
                     if (this.imageType == 0) {
                        var42 = new byte[]{(byte)(this.isWhiteZero ? 255 : 0), (byte)(this.isWhiteZero ? 0 : 255)};
                        var41 = new IndexColorModel(1, 2, var42, var42, var42);
                     } else {
                        var42 = new byte[16];
                        if (this.isWhiteZero) {
                           for(var45 = 0; var45 < var42.length; ++var45) {
                              var42[var45] = (byte)(255 - 16 * var45);
                           }
                        } else {
                           for(var45 = 0; var45 < var42.length; ++var45) {
                              var42[var45] = (byte)(16 * var45);
                           }
                        }

                        var41 = new IndexColorModel(4, 16, var42, var42, var42);
                     }
                     break;
                  case 2:
                  case 3:
                  case 5:
                  case 6:
                     int[] var43 = new int[this.numBands];

                     for(var45 = 0; var45 < this.numBands; ++var45) {
                        var43[var45] = this.numBands - 1 - var45;
                     }

                     var40 = new PixelInterleavedSampleModel(this.dataType, var19, var20, this.numBands, this.numBands * var19, var43);
                     if (this.imageType == 2) {
                        var41 = new ComponentColorModel(ColorSpace.getInstance(1003), new int[]{this.sampleSize}, false, false, 1, this.dataType);
                     } else if (this.imageType == 5) {
                        var41 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{this.sampleSize, this.sampleSize, this.sampleSize}, false, false, 1, this.dataType);
                     } else {
                        byte var48 = 1;
                        if (var18 == 1) {
                           var48 = 3;
                        } else if (var18 == 2) {
                           var48 = 2;
                        }

                        var41 = this.createAlphaComponentColorModel(this.dataType, this.numBands, var18 == 1, var48);
                     }
                     break;
                  case 4:
                     TIFFField var49 = var4.getField(320);
                     if (var49 == null) {
                        throw new RuntimeException("TIFFImage11");
                     }

                     this.colormap = var49.getAsChars();
                     if (this.decodePaletteAsShorts) {
                        this.numBands = 3;
                        if (this.dataType == 0) {
                           this.dataType = 1;
                        }

                        var40 = createPixelInterleavedSampleModel(this.dataType, var19, var20, this.numBands);
                        var41 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{16, 16, 16}, false, false, 1, this.dataType);
                     } else {
                        this.numBands = 1;
                        if (this.sampleSize == 4) {
                           var40 = new MultiPixelPackedSampleModel(0, var19, var20, this.sampleSize);
                        } else if (this.sampleSize == 8) {
                           var40 = createPixelInterleavedSampleModel(0, var19, var20, this.numBands);
                        } else if (this.sampleSize == 16) {
                           this.dataType = 1;
                           var40 = createPixelInterleavedSampleModel(1, var19, var20, this.numBands);
                        }

                        int var27 = this.colormap.length / 3;
                        byte[] var28 = new byte[var27];
                        byte[] var29 = new byte[var27];
                        byte[] var30 = new byte[var27];
                        int var31 = var27;
                        int var32 = var27 * 2;
                        int var33;
                        if (this.dataType == 2) {
                           for(var33 = 0; var33 < var27; ++var33) {
                              var28[var33] = var2.decodeSigned16BitsTo8Bits((short)this.colormap[var33]);
                              var29[var33] = var2.decodeSigned16BitsTo8Bits((short)this.colormap[var31 + var33]);
                              var30[var33] = var2.decodeSigned16BitsTo8Bits((short)this.colormap[var32 + var33]);
                           }
                        } else {
                           for(var33 = 0; var33 < var27; ++var33) {
                              var28[var33] = var2.decode16BitsTo8Bits(this.colormap[var33] & '\uffff');
                              var29[var33] = var2.decode16BitsTo8Bits(this.colormap[var31 + var33] & '\uffff');
                              var30[var33] = var2.decode16BitsTo8Bits(this.colormap[var32 + var33] & '\uffff');
                           }
                        }

                        var41 = new IndexColorModel(this.sampleSize, var27, var28, var29, var30);
                     }
                     break;
                  case 7:
                  case 8:
                     int[] var44 = new int[this.numBands];

                     for(int var47 = 0; var47 < this.numBands; var44[var47] = var47++) {
                     }

                     var40 = new PixelInterleavedSampleModel(this.dataType, var19, var20, this.numBands, this.numBands * var19, var44);
                     var41 = null;
                     break;
                  default:
                     throw new RuntimeException("TIFFImage4");
               }

               HashMap var46 = new HashMap();
               var46.put("tiff_directory", var4);
               this.init((CachableRed)null, var38, (ColorModel)var41, (SampleModel)var40, 0, 0, var46);
            }
         }
      }
   }

   public TIFFDirectory getPrivateIFD(long var1) throws IOException {
      return new TIFFDirectory(this.stream, var1, 0);
   }

   public WritableRaster copyData(WritableRaster var1) {
      this.copyToRaster(var1);
      return var1;
   }

   public synchronized Raster getTile(int var1, int var2) {
      if (var1 >= 0 && var1 < this.tilesX && var2 >= 0 && var2 < this.tilesY) {
         byte[] var3 = null;
         short[] var4 = null;
         int[] var5 = null;
         SampleModel var6 = this.getSampleModel();
         WritableRaster var7 = this.makeTile(var1, var2);
         DataBuffer var8 = var7.getDataBuffer();
         int var9 = var6.getDataType();
         if (var9 == 0) {
            var3 = ((DataBufferByte)var8).getData();
         } else if (var9 == 1) {
            var4 = ((DataBufferUShort)var8).getData();
         } else if (var9 == 2) {
            var4 = ((DataBufferShort)var8).getData();
         } else if (var9 == 3) {
            var5 = ((DataBufferInt)var8).getData();
         }

         long var13 = 0L;

         try {
            var13 = this.stream.getFilePointer();
            this.stream.seek(this.tileOffsets[var2 * this.tilesX + var1]);
         } catch (IOException var43) {
            throw new RuntimeException("TIFFImage13");
         }

         int var15 = (int)this.tileByteCounts[var2 * this.tilesX + var1];
         Rectangle var16;
         if (!this.tiled) {
            var16 = var7.getBounds();
         } else {
            var16 = new Rectangle(var7.getMinX(), var7.getMinY(), this.tileWidth, this.tileHeight);
         }

         int var17 = var16.width * var16.height * this.numBands;
         byte[] var18 = this.compression == 1 && this.imageType != 4 ? null : new byte[var15];
         int var19;
         if (this.imageType == 0) {
            try {
               if (this.compression == 32773) {
                  this.stream.readFully(var18, 0, var15);
                  if (var16.width % 8 == 0) {
                     var19 = var16.width / 8 * var16.height;
                  } else {
                     var19 = (var16.width / 8 + 1) * var16.height;
                  }

                  this.decodePackbits(var18, var19, var3);
               } else if (this.compression == 5) {
                  this.stream.readFully(var18, 0, var15);
                  this.lzwDecoder.decode(var18, var3, var16.height);
               } else if (this.compression == 2) {
                  this.stream.readFully(var18, 0, var15);
                  this.decoder.decode1D(var3, var18, 0, var16.height);
               } else if (this.compression == 3) {
                  this.stream.readFully(var18, 0, var15);
                  this.decoder.decode2D(var3, var18, 0, var16.height, this.tiffT4Options);
               } else if (this.compression == 4) {
                  this.stream.readFully(var18, 0, var15);
                  this.decoder.decodeT6(var3, var18, 0, var16.height, this.tiffT6Options);
               } else if (this.compression == 32946) {
                  this.stream.readFully(var18, 0, var15);
                  this.inflate(var18, var3);
               } else if (this.compression == 1) {
                  this.stream.readFully(var3, 0, var15);
               }

               this.stream.seek(var13);
            } catch (IOException var42) {
               throw new RuntimeException("TIFFImage13");
            }
         } else {
            int var20;
            int var21;
            byte[] var22;
            int var23;
            int var25;
            int var26;
            int var27;
            int var28;
            int var29;
            int var30;
            byte[] var46;
            byte[] var50;
            if (this.imageType == 4) {
               int var24;
               int var51;
               if (this.sampleSize == 16) {
                  if (this.decodePaletteAsShorts) {
                     short[] var45 = null;
                     var20 = var17 / 3;
                     var21 = var20 * 2;

                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(var18, 0, var15);
                           var22 = new byte[var21];
                           this.decodePackbits(var18, var21, var22);
                           var45 = new short[var20];
                           this.interpretBytesAsShorts(var22, var45, var20);
                        } else if (this.compression == 5) {
                           this.stream.readFully(var18, 0, var15);
                           var22 = new byte[var21];
                           this.lzwDecoder.decode(var18, var22, var16.height);
                           var45 = new short[var20];
                           this.interpretBytesAsShorts(var22, var45, var20);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(var18, 0, var15);
                           var22 = new byte[var21];
                           this.inflate(var18, var22);
                           var45 = new short[var20];
                           this.interpretBytesAsShorts(var22, var45, var20);
                        } else if (this.compression == 1) {
                           var45 = new short[var15 / 2];
                           this.readShorts(var15 / 2, var45);
                        }

                        this.stream.seek(var13);
                     } catch (IOException var41) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     if (var9 == 1) {
                        var23 = 0;
                        var25 = this.colormap.length / 3;
                        var26 = var25 * 2;

                        for(var27 = 0; var27 < var20; ++var27) {
                           var24 = var45[var27] & '\uffff';
                           var51 = this.colormap[var24 + var26];
                           var4[var23++] = (short)(var51 & '\uffff');
                           var51 = this.colormap[var24 + var25];
                           var4[var23++] = (short)(var51 & '\uffff');
                           var51 = this.colormap[var24];
                           var4[var23++] = (short)(var51 & '\uffff');
                        }
                     } else if (var9 == 2) {
                        var23 = 0;
                        var25 = this.colormap.length / 3;
                        var26 = var25 * 2;

                        for(var27 = 0; var27 < var20; ++var27) {
                           var24 = var45[var27] & '\uffff';
                           var51 = this.colormap[var24 + var26];
                           var4[var23++] = (short)var51;
                           var51 = this.colormap[var24 + var25];
                           var4[var23++] = (short)var51;
                           var51 = this.colormap[var24];
                           var4[var23++] = (short)var51;
                        }
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(var18, 0, var15);
                           var19 = var17 * 2;
                           var46 = new byte[var19];
                           this.decodePackbits(var18, var19, var46);
                           this.interpretBytesAsShorts(var46, var4, var17);
                        } else if (this.compression == 5) {
                           this.stream.readFully(var18, 0, var15);
                           var50 = new byte[var17 * 2];
                           this.lzwDecoder.decode(var18, var50, var16.height);
                           this.interpretBytesAsShorts(var50, var4, var17);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(var18, 0, var15);
                           var50 = new byte[var17 * 2];
                           this.inflate(var18, var50);
                           this.interpretBytesAsShorts(var50, var4, var17);
                        } else if (this.compression == 1) {
                           this.readShorts(var15 / 2, var4);
                        }

                        this.stream.seek(var13);
                     } catch (IOException var40) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               } else if (this.sampleSize == 8) {
                  if (this.decodePaletteAsShorts) {
                     var50 = null;
                     var20 = var17 / 3;

                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(var18, 0, var15);
                           var50 = new byte[var20];
                           this.decodePackbits(var18, var20, var50);
                        } else if (this.compression == 5) {
                           this.stream.readFully(var18, 0, var15);
                           var50 = new byte[var20];
                           this.lzwDecoder.decode(var18, var50, var16.height);
                        } else if (this.compression == 7) {
                           this.stream.readFully(var18, 0, var15);
                           Raster var47 = decodeJPEG(var18, this.decodeParam, this.colorConvertJPEG, var7.getMinX(), var7.getMinY());
                           int[] var52 = new int[var20];
                           var47.getPixels(var7.getMinX(), var7.getMinY(), var7.getWidth(), var7.getHeight(), var52);
                           var50 = new byte[var20];

                           for(var23 = 0; var23 < var20; ++var23) {
                              var50[var23] = (byte)var52[var23];
                           }
                        } else if (this.compression == 32946) {
                           this.stream.readFully(var18, 0, var15);
                           var50 = new byte[var20];
                           this.inflate(var18, var50);
                        } else if (this.compression == 1) {
                           var50 = new byte[var15];
                           this.stream.readFully(var50, 0, var15);
                        }

                        this.stream.seek(var13);
                     } catch (IOException var44) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     var51 = 0;
                     var24 = this.colormap.length / 3;
                     var25 = var24 * 2;

                     for(var26 = 0; var26 < var20; ++var26) {
                        var23 = var50[var26] & 255;
                        char var48 = this.colormap[var23 + var25];
                        var4[var51++] = (short)(var48 & '\uffff');
                        var48 = this.colormap[var23 + var24];
                        var4[var51++] = (short)(var48 & '\uffff');
                        var48 = this.colormap[var23];
                        var4[var51++] = (short)(var48 & '\uffff');
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(var18, 0, var15);
                           this.decodePackbits(var18, var17, var3);
                        } else if (this.compression == 5) {
                           this.stream.readFully(var18, 0, var15);
                           this.lzwDecoder.decode(var18, var3, var16.height);
                        } else if (this.compression == 7) {
                           this.stream.readFully(var18, 0, var15);
                           var7.setRect(decodeJPEG(var18, this.decodeParam, this.colorConvertJPEG, var7.getMinX(), var7.getMinY()));
                        } else if (this.compression == 32946) {
                           this.stream.readFully(var18, 0, var15);
                           this.inflate(var18, var3);
                        } else if (this.compression == 1) {
                           this.stream.readFully(var3, 0, var15);
                        }

                        this.stream.seek(var13);
                     } catch (IOException var39) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               } else if (this.sampleSize == 4) {
                  var19 = var16.width % 2 == 0 ? 0 : 1;
                  var20 = (var16.width / 2 + var19) * var16.height;
                  if (this.decodePaletteAsShorts) {
                     byte[] var49 = null;

                     try {
                        this.stream.readFully(var18, 0, var15);
                        this.stream.seek(var13);
                     } catch (IOException var38) {
                        throw new RuntimeException("TIFFImage13");
                     }

                     if (this.compression == 32773) {
                        var49 = new byte[var20];
                        this.decodePackbits(var18, var20, var49);
                     } else if (this.compression == 5) {
                        var49 = new byte[var20];
                        this.lzwDecoder.decode(var18, var49, var16.height);
                     } else if (this.compression == 32946) {
                        var49 = new byte[var20];
                        this.inflate(var18, var49);
                     } else if (this.compression == 1) {
                        var49 = var18;
                     }

                     var51 = var17 / 3;
                     var18 = new byte[var51];
                     var23 = 0;
                     var24 = 0;

                     for(var25 = 0; var25 < var16.height; ++var25) {
                        for(var26 = 0; var26 < var16.width / 2; ++var26) {
                           var18[var24++] = (byte)((var49[var23] & 240) >> 4);
                           var18[var24++] = (byte)(var49[var23++] & 15);
                        }

                        if (var19 == 1) {
                           var18[var24++] = (byte)((var49[var23++] & 240) >> 4);
                        }
                     }

                     var25 = this.colormap.length / 3;
                     var26 = var25 * 2;
                     var29 = 0;

                     for(var30 = 0; var30 < var51; ++var30) {
                        var28 = var18[var30] & 255;
                        char var54 = this.colormap[var28 + var26];
                        var4[var29++] = (short)(var54 & '\uffff');
                        var54 = this.colormap[var28 + var25];
                        var4[var29++] = (short)(var54 & '\uffff');
                        var54 = this.colormap[var28];
                        var4[var29++] = (short)(var54 & '\uffff');
                     }
                  } else {
                     try {
                        if (this.compression == 32773) {
                           this.stream.readFully(var18, 0, var15);
                           this.decodePackbits(var18, var20, var3);
                        } else if (this.compression == 5) {
                           this.stream.readFully(var18, 0, var15);
                           this.lzwDecoder.decode(var18, var3, var16.height);
                        } else if (this.compression == 32946) {
                           this.stream.readFully(var18, 0, var15);
                           this.inflate(var18, var3);
                        } else if (this.compression == 1) {
                           this.stream.readFully(var3, 0, var15);
                        }

                        this.stream.seek(var13);
                     } catch (IOException var37) {
                        throw new RuntimeException("TIFFImage13");
                     }
                  }
               }
            } else if (this.imageType == 1) {
               try {
                  if (this.compression == 32773) {
                     this.stream.readFully(var18, 0, var15);
                     if (var16.width % 8 == 0) {
                        var19 = var16.width / 2 * var16.height;
                     } else {
                        var19 = (var16.width / 2 + 1) * var16.height;
                     }

                     this.decodePackbits(var18, var19, var3);
                  } else if (this.compression == 5) {
                     this.stream.readFully(var18, 0, var15);
                     this.lzwDecoder.decode(var18, var3, var16.height);
                  } else if (this.compression == 32946) {
                     this.stream.readFully(var18, 0, var15);
                     this.inflate(var18, var3);
                  } else {
                     this.stream.readFully(var3, 0, var15);
                  }

                  this.stream.seek(var13);
               } catch (IOException var36) {
                  throw new RuntimeException("TIFFImage13");
               }
            } else {
               try {
                  if (this.sampleSize == 8) {
                     if (this.compression == 1) {
                        this.stream.readFully(var3, 0, var15);
                     } else if (this.compression == 5) {
                        this.stream.readFully(var18, 0, var15);
                        this.lzwDecoder.decode(var18, var3, var16.height);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(var18, 0, var15);
                        this.decodePackbits(var18, var17, var3);
                     } else if (this.compression == 7) {
                        this.stream.readFully(var18, 0, var15);
                        var7.setRect(decodeJPEG(var18, this.decodeParam, this.colorConvertJPEG, var7.getMinX(), var7.getMinY()));
                     } else if (this.compression == 32946) {
                        this.stream.readFully(var18, 0, var15);
                        this.inflate(var18, var3);
                     }
                  } else if (this.sampleSize == 16) {
                     if (this.compression == 1) {
                        this.readShorts(var15 / 2, var4);
                     } else if (this.compression == 5) {
                        this.stream.readFully(var18, 0, var15);
                        var50 = new byte[var17 * 2];
                        this.lzwDecoder.decode(var18, var50, var16.height);
                        this.interpretBytesAsShorts(var50, var4, var17);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(var18, 0, var15);
                        var19 = var17 * 2;
                        var46 = new byte[var19];
                        this.decodePackbits(var18, var19, var46);
                        this.interpretBytesAsShorts(var46, var4, var17);
                     } else if (this.compression == 32946) {
                        this.stream.readFully(var18, 0, var15);
                        var50 = new byte[var17 * 2];
                        this.inflate(var18, var50);
                        this.interpretBytesAsShorts(var50, var4, var17);
                     }
                  } else if (this.sampleSize == 32 && var9 == 3) {
                     if (this.compression == 1) {
                        this.readInts(var15 / 4, var5);
                     } else if (this.compression == 5) {
                        this.stream.readFully(var18, 0, var15);
                        var50 = new byte[var17 * 4];
                        this.lzwDecoder.decode(var18, var50, var16.height);
                        this.interpretBytesAsInts(var50, var5, var17);
                     } else if (this.compression == 32773) {
                        this.stream.readFully(var18, 0, var15);
                        var19 = var17 * 4;
                        var46 = new byte[var19];
                        this.decodePackbits(var18, var19, var46);
                        this.interpretBytesAsInts(var46, var5, var17);
                     } else if (this.compression == 32946) {
                        this.stream.readFully(var18, 0, var15);
                        var50 = new byte[var17 * 4];
                        this.inflate(var18, var50);
                        this.interpretBytesAsInts(var50, var5, var17);
                     }
                  }

                  this.stream.seek(var13);
               } catch (IOException var35) {
                  throw new RuntimeException("TIFFImage13");
               }

               byte var10;
               short var11;
               int var12;
               switch (this.imageType) {
                  case 2:
                  case 3:
                     if (this.isWhiteZero) {
                        if (var9 == 0 && !(this.getColorModel() instanceof IndexColorModel)) {
                           for(var19 = 0; var19 < var3.length; var19 += this.numBands) {
                              var3[var19] = (byte)(255 - var3[var19]);
                           }
                        } else if (var9 == 1) {
                           char var55 = '\uffff';

                           for(var20 = 0; var20 < var4.length; var20 += this.numBands) {
                              var4[var20] = (short)(var55 - var4[var20]);
                           }
                        } else if (var9 == 2) {
                           for(var19 = 0; var19 < var4.length; var19 += this.numBands) {
                              var4[var19] = (short)(~var4[var19]);
                           }
                        } else if (var9 == 3) {
                           long var56 = 4294967295L;

                           for(var21 = 0; var21 < var5.length; var21 += this.numBands) {
                              var5[var21] = (int)(var56 - (long)var5[var21]);
                           }
                        }
                     }
                  case 4:
                  default:
                     break;
                  case 5:
                     if (this.sampleSize == 8 && this.compression != 7) {
                        for(var19 = 0; var19 < var17; var19 += 3) {
                           var10 = var3[var19];
                           var3[var19] = var3[var19 + 2];
                           var3[var19 + 2] = var10;
                        }

                        return var7;
                     } else if (this.sampleSize == 16) {
                        for(var19 = 0; var19 < var17; var19 += 3) {
                           var11 = var4[var19];
                           var4[var19] = var4[var19 + 2];
                           var4[var19 + 2] = var11;
                        }

                        return var7;
                     } else {
                        if (this.sampleSize == 32 && var9 == 3) {
                           for(var19 = 0; var19 < var17; var19 += 3) {
                              var12 = var5[var19];
                              var5[var19] = var5[var19 + 2];
                              var5[var19 + 2] = var12;
                           }
                        }
                        break;
                     }
                  case 6:
                     if (this.sampleSize == 8) {
                        for(var19 = 0; var19 < var17; var19 += 4) {
                           var10 = var3[var19];
                           var3[var19] = var3[var19 + 3];
                           var3[var19 + 3] = var10;
                           var10 = var3[var19 + 1];
                           var3[var19 + 1] = var3[var19 + 2];
                           var3[var19 + 2] = var10;
                        }

                        return var7;
                     } else if (this.sampleSize == 16) {
                        for(var19 = 0; var19 < var17; var19 += 4) {
                           var11 = var4[var19];
                           var4[var19] = var4[var19 + 3];
                           var4[var19 + 3] = var11;
                           var11 = var4[var19 + 1];
                           var4[var19 + 1] = var4[var19 + 2];
                           var4[var19 + 2] = var11;
                        }

                        return var7;
                     } else {
                        if (this.sampleSize == 32 && var9 == 3) {
                           for(var19 = 0; var19 < var17; var19 += 4) {
                              var12 = var5[var19];
                              var5[var19] = var5[var19 + 3];
                              var5[var19 + 3] = var12;
                              var12 = var5[var19 + 1];
                              var5[var19 + 1] = var5[var19 + 2];
                              var5[var19 + 2] = var12;
                           }
                        }
                        break;
                     }
                  case 7:
                     var19 = this.chromaSubH * this.chromaSubV;
                     var20 = var16.width / this.chromaSubH;
                     var21 = var16.height / this.chromaSubV;
                     var22 = new byte[var20 * var21 * (var19 + 2)];
                     System.arraycopy(var3, 0, var22, 0, var22.length);
                     var23 = var19 * 3;
                     int[] var53 = new int[var23];
                     var25 = 0;
                     var26 = var19;
                     var27 = var19 + 1;
                     var28 = var16.y;

                     for(var29 = 0; var29 < var21; ++var29) {
                        var30 = var16.x;

                        for(int var31 = 0; var31 < var20; ++var31) {
                           byte var32 = var22[var25 + var26];
                           byte var33 = var22[var25 + var27];

                           for(int var34 = 0; var34 < var23; var53[var34++] = var33) {
                              var53[var34++] = var22[var25++];
                              var53[var34++] = var32;
                           }

                           var25 += 2;
                           var7.setPixels(var30, var28, this.chromaSubH, this.chromaSubV, var53);
                           var30 += this.chromaSubH;
                        }

                        var28 += this.chromaSubV;
                     }
               }
            }
         }

         return var7;
      } else {
         throw new IllegalArgumentException("TIFFImage12");
      }
   }

   private void readShorts(int var1, short[] var2) {
      int var3 = 2 * var1;
      byte[] var4 = new byte[var3];

      try {
         this.stream.readFully(var4, 0, var3);
      } catch (IOException var6) {
         throw new RuntimeException("TIFFImage13");
      }

      this.interpretBytesAsShorts(var4, var2, var1);
   }

   private void readInts(int var1, int[] var2) {
      int var3 = 4 * var1;
      byte[] var4 = new byte[var3];

      try {
         this.stream.readFully(var4, 0, var3);
      } catch (IOException var6) {
         throw new RuntimeException("TIFFImage13");
      }

      this.interpretBytesAsInts(var4, var2, var1);
   }

   private void interpretBytesAsShorts(byte[] var1, short[] var2, int var3) {
      int var4 = 0;
      int var5;
      int var6;
      int var7;
      if (this.isBigEndian) {
         for(var7 = 0; var7 < var3; ++var7) {
            var5 = var1[var4++] & 255;
            var6 = var1[var4++] & 255;
            var2[var7] = (short)((var5 << 8) + var6);
         }
      } else {
         for(var7 = 0; var7 < var3; ++var7) {
            var5 = var1[var4++] & 255;
            var6 = var1[var4++] & 255;
            var2[var7] = (short)((var6 << 8) + var5);
         }
      }

   }

   private void interpretBytesAsInts(byte[] var1, int[] var2, int var3) {
      int var4 = 0;
      int var5;
      if (this.isBigEndian) {
         for(var5 = 0; var5 < var3; ++var5) {
            var2[var5] = (var1[var4++] & 255) << 24 | (var1[var4++] & 255) << 16 | (var1[var4++] & 255) << 8 | var1[var4++] & 255;
         }
      } else {
         for(var5 = 0; var5 < var3; ++var5) {
            var2[var5] = var1[var4++] & 255 | (var1[var4++] & 255) << 8 | (var1[var4++] & 255) << 16 | (var1[var4++] & 255) << 24;
         }
      }

   }

   private byte[] decodePackbits(byte[] var1, int var2, byte[] var3) {
      if (var3 == null) {
         var3 = new byte[var2];
      }

      int var4 = 0;
      int var5 = 0;

      try {
         while(true) {
            while(var5 < var2) {
               byte var7 = var1[var4++];
               int var8;
               if (var7 >= 0 && var7 <= 127) {
                  for(var8 = 0; var8 < var7 + 1; ++var8) {
                     var3[var5++] = var1[var4++];
                  }
               } else if (var7 <= -1 && var7 >= -127) {
                  byte var6 = var1[var4++];

                  for(var8 = 0; var8 < -var7 + 1; ++var8) {
                     var3[var5++] = var6;
                  }
               } else {
                  ++var4;
               }
            }

            return var3;
         }
      } catch (ArrayIndexOutOfBoundsException var9) {
         throw new RuntimeException("TIFFImage14");
      }
   }

   private ComponentColorModel createAlphaComponentColorModel(int var1, int var2, boolean var3, int var4) {
      ComponentColorModel var5 = null;
      Object var6 = null;
      ColorSpace var7 = null;
      switch (var2) {
         case 2:
            var7 = ColorSpace.getInstance(1003);
            break;
         case 4:
            var7 = ColorSpace.getInstance(1000);
            break;
         default:
            throw new IllegalArgumentException();
      }

      boolean var8 = false;
      byte var11;
      switch (var1) {
         case 0:
            var11 = 8;
            break;
         case 1:
         case 2:
            var11 = 16;
            break;
         case 3:
            var11 = 32;
            break;
         default:
            throw new IllegalArgumentException();
      }

      int[] var10 = new int[var2];

      for(int var9 = 0; var9 < var2; ++var9) {
         var10[var9] = var11;
      }

      var5 = new ComponentColorModel(var7, var10, true, var3, var4, var1);
      return var5;
   }
}
