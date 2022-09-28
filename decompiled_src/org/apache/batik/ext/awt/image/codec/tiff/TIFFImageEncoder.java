package org.apache.batik.ext.awt.image.codec.tiff;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGQTable;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.Deflater;
import org.apache.batik.ext.awt.image.codec.util.ImageEncodeParam;
import org.apache.batik.ext.awt.image.codec.util.ImageEncoderImpl;
import org.apache.batik.ext.awt.image.codec.util.SeekableOutputStream;

public class TIFFImageEncoder extends ImageEncoderImpl {
   private static final int TIFF_UNSUPPORTED = -1;
   private static final int TIFF_BILEVEL_WHITE_IS_ZERO = 0;
   private static final int TIFF_BILEVEL_BLACK_IS_ZERO = 1;
   private static final int TIFF_GRAY = 2;
   private static final int TIFF_PALETTE = 3;
   private static final int TIFF_RGB = 4;
   private static final int TIFF_CMYK = 5;
   private static final int TIFF_YCBCR = 6;
   private static final int TIFF_CIELAB = 7;
   private static final int TIFF_GENERIC = 8;
   private static final int COMP_NONE = 1;
   private static final int COMP_JPEG_TTN2 = 7;
   private static final int COMP_PACKBITS = 32773;
   private static final int COMP_DEFLATE = 32946;
   private static final int TIFF_JPEG_TABLES = 347;
   private static final int TIFF_YCBCR_SUBSAMPLING = 530;
   private static final int TIFF_YCBCR_POSITIONING = 531;
   private static final int TIFF_REF_BLACK_WHITE = 532;
   private static final int EXTRA_SAMPLE_UNSPECIFIED = 0;
   private static final int EXTRA_SAMPLE_ASSOCIATED_ALPHA = 1;
   private static final int EXTRA_SAMPLE_UNASSOCIATED_ALPHA = 2;
   private static final int DEFAULT_ROWS_PER_STRIP = 8;
   private static final int[] sizeOfType = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};

   public TIFFImageEncoder(OutputStream var1, ImageEncodeParam var2) {
      super(var1, var2);
      if (this.param == null) {
         this.param = new TIFFEncodeParam();
      }

   }

   public void encode(RenderedImage var1) throws IOException {
      this.writeFileHeader();
      TIFFEncodeParam var2 = (TIFFEncodeParam)this.param;
      Iterator var3 = var2.getExtraImages();
      if (var3 != null) {
         int var4 = 8;
         RenderedImage var5 = var1;
         TIFFEncodeParam var6 = var2;

         boolean var7;
         do {
            var7 = var3.hasNext();
            var4 = this.encode(var5, var6, var4, !var7);
            if (var7) {
               Object var8 = var3.next();
               if (var8 instanceof RenderedImage) {
                  var5 = (RenderedImage)var8;
                  var6 = var2;
               } else if (var8 instanceof Object[]) {
                  Object[] var9 = (Object[])var8;
                  var5 = (RenderedImage)var9[0];
                  var6 = (TIFFEncodeParam)var9[1];
               }
            }
         } while(var7);
      } else {
         this.encode(var1, var2, 8, true);
      }

   }

   private int encode(RenderedImage var1, TIFFEncodeParam var2, int var3, boolean var4) throws IOException {
      int var5 = var2.getCompression();
      boolean var6 = var2.getWriteTiled();
      int var7 = var1.getMinX();
      int var8 = var1.getMinY();
      int var9 = var1.getWidth();
      int var10 = var1.getHeight();
      SampleModel var11 = var1.getSampleModel();
      int[] var12 = var11.getSampleSize();

      int var13;
      for(var13 = 1; var13 < var12.length; ++var13) {
         if (var12[var13] != var12[0]) {
            throw new Error("TIFFImageEncoder0");
         }
      }

      var13 = var11.getNumBands();
      if ((var12[0] == 1 || var12[0] == 4) && var13 != 1) {
         throw new Error("TIFFImageEncoder1");
      } else {
         int var14 = var11.getDataType();
         switch (var14) {
            case 0:
               if (var12[0] != 1 && var12[0] == 4 && var12[0] != 8) {
                  throw new Error("TIFFImageEncoder2");
               }
               break;
            case 1:
            case 2:
               if (var12[0] != 16) {
                  throw new Error("TIFFImageEncoder3");
               }
               break;
            case 3:
            case 4:
               if (var12[0] != 32) {
                  throw new Error("TIFFImageEncoder4");
               }
               break;
            default:
               throw new Error("TIFFImageEncoder5");
         }

         boolean var15 = var14 == 2 || var14 == 1;
         ColorModel var16 = var1.getColorModel();
         if (var16 != null && var16 instanceof IndexColorModel && var14 != 0) {
            throw new Error("TIFFImageEncoder6");
         } else {
            IndexColorModel var17 = null;
            int var18 = 0;
            char[] var19 = null;
            byte var20 = -1;
            int var21 = 0;
            int var22 = 0;
            byte[] var24;
            byte[] var25;
            byte[] var26;
            if (var16 instanceof IndexColorModel) {
               var17 = (IndexColorModel)var16;
               int var23 = var17.getMapSize();
               if (var12[0] == 1 && var13 == 1) {
                  if (var23 != 2) {
                     throw new IllegalArgumentException("TIFFImageEncoder7");
                  }

                  var24 = new byte[var23];
                  var17.getReds(var24);
                  var25 = new byte[var23];
                  var17.getGreens(var25);
                  var26 = new byte[var23];
                  var17.getBlues(var26);
                  if ((var24[0] & 255) == 0 && (var24[1] & 255) == 255 && (var25[0] & 255) == 0 && (var25[1] & 255) == 255 && (var26[0] & 255) == 0 && (var26[1] & 255) == 255) {
                     var20 = 1;
                  } else if ((var24[0] & 255) == 255 && (var24[1] & 255) == 0 && (var25[0] & 255) == 255 && (var25[1] & 255) == 0 && (var26[0] & 255) == 255 && (var26[1] & 255) == 0) {
                     var20 = 0;
                  } else {
                     var20 = 3;
                  }
               } else if (var13 == 1) {
                  var20 = 3;
               }
            } else if (var16 == null) {
               if (var12[0] == 1 && var13 == 1) {
                  var20 = 1;
               } else {
                  var20 = 8;
                  if (var13 > 1) {
                     var21 = var13 - 1;
                  }
               }
            } else {
               ColorSpace var73 = var16.getColorSpace();
               switch (var73.getType()) {
                  case 1:
                     var20 = 7;
                     break;
                  case 2:
                  case 4:
                  case 7:
                  case 8:
                  default:
                     var20 = 8;
                     break;
                  case 3:
                     var20 = 6;
                     break;
                  case 5:
                     if (var5 == 7 && var2.getJPEGCompressRGBToYCbCr()) {
                        var20 = 6;
                        break;
                     }

                     var20 = 4;
                     break;
                  case 6:
                     var20 = 2;
                     break;
                  case 9:
                     var20 = 5;
               }

               if (var20 == 8) {
                  var21 = var13 - 1;
               } else if (var13 > 1) {
                  var21 = var13 - var73.getNumComponents();
               }

               if (var21 == 1 && var16.hasAlpha()) {
                  var22 = var16.isAlphaPremultiplied() ? 1 : 2;
               }
            }

            if (var20 == -1) {
               throw new Error("TIFFImageEncoder8");
            } else {
               if (var5 == 7) {
                  if (var20 == 3) {
                     throw new Error("TIFFImageEncoder11");
                  }

                  if (var12[0] != 8 || var20 != 2 && var20 != 4 && var20 != 6) {
                     throw new Error("TIFFImageEncoder9");
                  }
               }

               boolean var74 = true;
               int var27;
               int var28;
               int var29;
               int var30;
               int var31;
               byte var75;
               switch (var20) {
                  case 0:
                     var75 = 0;
                     break;
                  case 1:
                     var75 = 1;
                     break;
                  case 2:
                  case 8:
                     var75 = 1;
                     break;
                  case 3:
                     var75 = 3;
                     var17 = (IndexColorModel)var16;
                     var18 = var17.getMapSize();
                     var24 = new byte[var18];
                     var17.getReds(var24);
                     var25 = new byte[var18];
                     var17.getGreens(var25);
                     var26 = new byte[var18];
                     var17.getBlues(var26);
                     var27 = 0;
                     var28 = var18;
                     var29 = 2 * var18;
                     var19 = new char[var18 * 3];

                     for(var30 = 0; var30 < var18; ++var30) {
                        var31 = 255 & var24[var30];
                        var19[var27++] = (char)(var31 << 8 | var31);
                        var31 = 255 & var25[var30];
                        var19[var28++] = (char)(var31 << 8 | var31);
                        var31 = 255 & var26[var30];
                        var19[var29++] = (char)(var31 << 8 | var31);
                     }

                     var18 *= 3;
                     break;
                  case 4:
                     var75 = 2;
                     break;
                  case 5:
                     var75 = 5;
                     break;
                  case 6:
                     var75 = 6;
                     break;
                  case 7:
                     var75 = 8;
                     break;
                  default:
                     throw new Error("TIFFImageEncoder8");
               }

               int var76;
               int var77;
               if (var6) {
                  var76 = var2.getTileWidth() > 0 ? var2.getTileWidth() : var1.getTileWidth();
                  var77 = var2.getTileHeight() > 0 ? var2.getTileHeight() : var1.getTileHeight();
               } else {
                  var76 = var9;
                  var77 = var2.getTileHeight() > 0 ? var2.getTileHeight() : 8;
               }

               JPEGEncodeParam var78 = null;
               if (var5 == 7) {
                  var78 = var2.getJPEGEncodeParam();
                  var27 = var78.getHorizontalSubsampling(0);
                  var28 = var78.getVerticalSubsampling(0);

                  for(var29 = 1; var29 < var13; ++var29) {
                     var30 = var78.getHorizontalSubsampling(var29);
                     if (var30 > var27) {
                        var27 = var30;
                     }

                     var31 = var78.getVerticalSubsampling(var29);
                     if (var31 > var28) {
                        var28 = var31;
                     }
                  }

                  var29 = 8 * var28;
                  var77 = (int)((float)var77 / (float)var29 + 0.5F) * var29;
                  if (var77 < var29) {
                     var77 = var29;
                  }

                  if (var6) {
                     var30 = 8 * var27;
                     var76 = (int)((float)var76 / (float)var30 + 0.5F) * var30;
                     if (var76 < var30) {
                        var76 = var30;
                     }
                  }
               }

               if (var6) {
                  var27 = (var9 + var76 - 1) / var76 * ((var10 + var77 - 1) / var77);
               } else {
                  var27 = (int)Math.ceil((double)var10 / (double)var77);
               }

               long[] var79 = new long[var27];
               long var80 = (long)Math.ceil((double)var12[0] / 8.0 * (double)var76 * (double)var13);
               long var81 = var80 * (long)var77;

               for(int var33 = 0; var33 < var27; ++var33) {
                  var79[var33] = var81;
               }

               long var82;
               if (!var6) {
                  var82 = (long)(var10 - var77 * (var27 - 1));
                  var79[var27 - 1] = var82 * var80;
               }

               var82 = var81 * (long)(var27 - 1) + var79[var27 - 1];
               long[] var35 = new long[var27];
               TreeSet var36 = new TreeSet();
               var36.add(new TIFFField(256, 4, 1, new long[]{(long)var9}));
               var36.add(new TIFFField(257, 4, 1, new long[]{(long)var10}));
               char[] var37 = new char[var13];

               for(int var38 = 0; var38 < var13; ++var38) {
                  var37[var38] = (char)var12[var38];
               }

               var36.add(new TIFFField(258, 3, var13, var37));
               var36.add(new TIFFField(259, 3, 1, new char[]{(char)var5}));
               var36.add(new TIFFField(262, 3, 1, new char[]{(char)var75}));
               if (!var6) {
                  var36.add(new TIFFField(273, 4, var27, var35));
               }

               var36.add(new TIFFField(277, 3, 1, new char[]{(char)var13}));
               if (!var6) {
                  var36.add(new TIFFField(278, 4, 1, new long[]{(long)var77}));
                  var36.add(new TIFFField(279, 4, var27, var79));
               }

               if (var19 != null) {
                  var36.add(new TIFFField(320, 3, var18, var19));
               }

               if (var6) {
                  var36.add(new TIFFField(322, 4, 1, new long[]{(long)var76}));
                  var36.add(new TIFFField(323, 4, 1, new long[]{(long)var77}));
                  var36.add(new TIFFField(324, 4, var27, var35));
                  var36.add(new TIFFField(325, 4, var27, var79));
               }

               int var39;
               char[] var83;
               if (var21 > 0) {
                  var83 = new char[var21];

                  for(var39 = 0; var39 < var21; ++var39) {
                     var83[var39] = (char)var22;
                  }

                  var36.add(new TIFFField(338, 3, var21, var83));
               }

               if (var14 != 0) {
                  var83 = new char[var13];
                  if (var14 == 4) {
                     var83[0] = 3;
                  } else if (var14 == 1) {
                     var83[0] = 1;
                  } else {
                     var83[0] = 2;
                  }

                  for(var39 = 1; var39 < var13; ++var39) {
                     var83[var39] = var83[0];
                  }

                  var36.add(new TIFFField(339, 3, var13, var83));
               }

               JPEGEncodeParam var84 = null;
               JPEGImageEncoder var85 = null;
               byte var40 = 0;
               if (var5 == 7) {
                  var40 = 0;
                  switch (var20) {
                     case 2:
                     case 3:
                        var40 = 1;
                        break;
                     case 4:
                        var40 = 2;
                     case 5:
                     default:
                        break;
                     case 6:
                        var40 = 3;
                  }

                  Raster var41 = var1.getTile(0, 0);
                  var84 = JPEGCodec.getDefaultJPEGEncodeParam(var41, var40);
                  modifyEncodeParam(var78, var84, var13);
                  var84.setImageInfoValid(false);
                  var84.setTableInfoValid(true);
                  ByteArrayOutputStream var42 = new ByteArrayOutputStream();
                  var85 = JPEGCodec.createJPEGEncoder(var42, var84);
                  var85.encode(var41);
                  byte[] var43 = var42.toByteArray();
                  var36.add(new TIFFField(347, 7, var43.length, var43));
                  var85 = null;
               }

               int var44;
               int var45;
               int var87;
               if (var20 == 6) {
                  char var86 = 1;
                  var87 = 1;
                  if (var5 == 7) {
                     var86 = (char)var78.getHorizontalSubsampling(0);
                     var87 = (char)var78.getVerticalSubsampling(0);

                     for(int var89 = 1; var89 < var13; ++var89) {
                        var44 = (char)var78.getHorizontalSubsampling(var89);
                        if (var44 > var86) {
                           var86 = (char)var44;
                        }

                        var45 = (char)var78.getVerticalSubsampling(var89);
                        if (var45 > var87) {
                           var87 = var45;
                        }
                     }
                  }

                  var36.add(new TIFFField(530, 3, 2, new char[]{var86, (char)var87}));
                  var36.add(new TIFFField(531, 3, 1, new char[]{(char)(var5 == 7 ? 1 : 2)}));
                  long[][] var91;
                  if (var5 == 7) {
                     var91 = new long[][]{{0L, 1L}, {255L, 1L}, {128L, 1L}, {255L, 1L}, {128L, 1L}, {255L, 1L}};
                  } else {
                     var91 = new long[][]{{15L, 1L}, {235L, 1L}, {128L, 1L}, {240L, 1L}, {128L, 1L}, {240L, 1L}};
                  }

                  var36.add(new TIFFField(532, 5, 6, var91));
               }

               TIFFField[] var88 = var2.getExtraFields();
               if (var88 != null) {
                  ArrayList var90 = new ArrayList(var36.size());
                  Iterator var93 = var36.iterator();

                  while(var93.hasNext()) {
                     TIFFField var92 = (TIFFField)var93.next();
                     var90.add(new Integer(var92.getTag()));
                  }

                  var44 = var88.length;

                  for(var45 = 0; var45 < var44; ++var45) {
                     TIFFField var46 = var88[var45];
                     Integer var47 = new Integer(var46.getTag());
                     if (!var90.contains(var47)) {
                        var36.add(var46);
                        var90.add(var47);
                     }
                  }
               }

               var87 = this.getDirectorySize(var36);
               var35[0] = (long)(var3 + var87);
               OutputStream var95 = null;
               byte[] var94 = null;
               File var96 = null;
               int var97 = 0;
               boolean var98 = false;
               Deflater var48 = null;
               boolean var49 = false;
               int var50;
               if (var5 == 1) {
                  var50 = 0;
                  if (var12[0] == 16 && var35[0] % 2L != 0L) {
                     var50 = 1;
                     int var10002 = var35[0]++;
                  } else if (var12[0] == 32 && var35[0] % 4L != 0L) {
                     var50 = (int)(4L - var35[0] % 4L);
                     var35[0] += (long)var50;
                  }

                  int var51;
                  for(var51 = 1; var51 < var27; ++var51) {
                     var35[var51] = var35[var51 - 1] + var79[var51 - 1];
                  }

                  if (!var4) {
                     var97 = (int)(var35[0] + var82);
                     if ((var97 & 1) != 0) {
                        ++var97;
                        var98 = true;
                     }
                  }

                  this.writeDirectory(var3, var36, var97);
                  if (var50 != 0) {
                     for(var51 = 0; var51 < var50; ++var51) {
                        this.output.write(0);
                     }
                  }
               } else {
                  if (this.output instanceof SeekableOutputStream) {
                     ((SeekableOutputStream)this.output).seek(var35[0]);
                  } else {
                     var95 = this.output;

                     try {
                        var96 = File.createTempFile("jai-SOS-", ".tmp");
                        var96.deleteOnExit();
                        RandomAccessFile var99 = new RandomAccessFile(var96, "rw");
                        this.output = new SeekableOutputStream(var99);
                     } catch (Exception var72) {
                        this.output = new ByteArrayOutputStream((int)var82);
                     }
                  }

                  boolean var100 = false;
                  switch (var5) {
                     case 7:
                        var50 = 0;
                        if (var20 == 6 && var16 != null && var16.getColorSpace().getType() == 5) {
                           var49 = true;
                        }
                        break;
                     case 32773:
                        var50 = (int)(var81 + (var80 + 127L) / 128L * (long)var77);
                        break;
                     case 32946:
                        var50 = (int)var81;
                        var48 = new Deflater(var2.getDeflateLevel());
                        break;
                     default:
                        var50 = 0;
                  }

                  if (var50 != 0) {
                     var94 = new byte[var50];
                  }
               }

               int[] var102 = null;
               float[] var101 = null;
               boolean var52 = var12[0] == 1 && var11 instanceof MultiPixelPackedSampleModel && var14 == 0 || var12[0] == 8 && var11 instanceof ComponentSampleModel;
               byte[] var53 = null;
               if (var5 != 7) {
                  if (var14 == 0) {
                     var53 = new byte[var77 * var76 * var13];
                  } else if (var15) {
                     var53 = new byte[2 * var77 * var76 * var13];
                  } else if (var14 == 3 || var14 == 4) {
                     var53 = new byte[4 * var77 * var76 * var13];
                  }
               }

               int var54 = var8 + var10;
               int var55 = var7 + var9;
               int var56 = 0;

               int var57;
               int var58;
               int var59;
               int var60;
               for(var57 = var8; var57 < var54; var57 += var77) {
                  var58 = var6 ? var77 : Math.min(var77, var54 - var57);
                  var59 = var58 * var76 * var13;

                  for(var60 = var7; var60 < var55; var60 += var76) {
                     Raster var61 = var1.getData(new Rectangle(var60, var57, var76, var58));
                     boolean var62 = false;
                     int var66;
                     int var67;
                     int var68;
                     if (var5 != 7) {
                        if (var52) {
                           if (var12[0] == 8) {
                              ComponentSampleModel var63 = (ComponentSampleModel)var61.getSampleModel();
                              int[] var64 = var63.getBankIndices();
                              int[] var65 = var63.getBandOffsets();
                              var66 = var63.getPixelStride();
                              var67 = var63.getScanlineStride();
                              if (var66 == var13 && (long)var67 == var80) {
                                 var62 = true;

                                 for(var68 = 0; var62 && var68 < var13; ++var68) {
                                    if (var64[var68] != 0 || var65[var68] != var68) {
                                       var62 = false;
                                    }
                                 }
                              } else {
                                 var62 = false;
                              }
                           } else {
                              MultiPixelPackedSampleModel var109 = (MultiPixelPackedSampleModel)var61.getSampleModel();
                              if (var109.getNumBands() == 1 && var109.getDataBitOffset() == 0 && var109.getPixelBitStride() == 1) {
                                 var62 = true;
                              }
                           }
                        }

                        if (!var62) {
                           if (var14 == 4) {
                              var101 = var61.getPixels(var60, var57, var76, var58, var101);
                           } else {
                              var102 = var61.getPixels(var60, var57, var76, var58, var102);
                           }
                        }
                     }

                     boolean var111 = false;
                     int var113 = 0;
                     int var69;
                     int var70;
                     int var71;
                     int var110;
                     int var112;
                     byte[] var114;
                     switch (var12[0]) {
                        case 1:
                           if (var62) {
                              var114 = ((DataBufferByte)var61.getDataBuffer()).getData();
                              MultiPixelPackedSampleModel var120 = (MultiPixelPackedSampleModel)var61.getSampleModel();
                              var68 = var120.getScanlineStride();
                              var69 = var120.getOffset(var60 - var61.getSampleModelTranslateX(), var57 - var61.getSampleModelTranslateY());
                              if (var68 == (int)var80) {
                                 System.arraycopy(var114, var69, var53, 0, (int)var80 * var58);
                              } else {
                                 var70 = 0;

                                 for(var71 = 0; var71 < var58; ++var71) {
                                    System.arraycopy(var114, var69, var53, var70, (int)var80);
                                    var69 += var68;
                                    var70 += (int)var80;
                                 }
                              }
                           } else {
                              var110 = 0;

                              for(var66 = 0; var66 < var58; ++var66) {
                                 for(var67 = 0; var67 < var76 / 8; ++var67) {
                                    var112 = var102[var110++] << 7 | var102[var110++] << 6 | var102[var110++] << 5 | var102[var110++] << 4 | var102[var110++] << 3 | var102[var110++] << 2 | var102[var110++] << 1 | var102[var110++];
                                    var53[var113++] = (byte)var112;
                                 }

                                 if (var76 % 8 > 0) {
                                    var112 = 0;

                                    for(var67 = 0; var67 < var76 % 8; ++var67) {
                                       var112 |= var102[var110++] << 7 - var67;
                                    }

                                    var53[var113++] = (byte)var112;
                                 }
                              }
                           }

                           if (var5 == 1) {
                              this.output.write(var53, 0, var58 * ((var76 + 7) / 8));
                           } else if (var5 == 32773) {
                              var66 = compressPackBits(var53, var58, (int)var80, var94);
                              var79[var56++] = (long)var66;
                              this.output.write(var94, 0, var66);
                           } else if (var5 == 32946) {
                              var66 = deflate(var48, var53, var94);
                              var79[var56++] = (long)var66;
                              this.output.write(var94, 0, var66);
                           }
                           break;
                        case 4:
                           var110 = 0;

                           for(var66 = 0; var66 < var58; ++var66) {
                              for(var67 = 0; var67 < var76 / 2; ++var67) {
                                 var112 = var102[var110++] << 4 | var102[var110++];
                                 var53[var113++] = (byte)var112;
                              }

                              if ((var76 & 1) == 1) {
                                 var112 = var102[var110++] << 4;
                                 var53[var113++] = (byte)var112;
                              }
                           }

                           if (var5 == 1) {
                              this.output.write(var53, 0, var58 * ((var76 + 1) / 2));
                           } else if (var5 == 32773) {
                              var66 = compressPackBits(var53, var58, (int)var80, var94);
                              var79[var56++] = (long)var66;
                              this.output.write(var94, 0, var66);
                           } else if (var5 == 32946) {
                              var66 = deflate(var48, var53, var94);
                              var79[var56++] = (long)var66;
                              this.output.write(var94, 0, var66);
                           }
                           break;
                        case 8:
                           if (var5 != 7) {
                              if (var62) {
                                 var114 = ((DataBufferByte)var61.getDataBuffer()).getData();
                                 ComponentSampleModel var118 = (ComponentSampleModel)var61.getSampleModel();
                                 var68 = var118.getOffset(var60 - var61.getSampleModelTranslateX(), var57 - var61.getSampleModelTranslateY());
                                 var69 = var118.getScanlineStride();
                                 if (var69 == (int)var80) {
                                    System.arraycopy(var114, var68, var53, 0, (int)var80 * var58);
                                 } else {
                                    var70 = 0;

                                    for(var71 = 0; var71 < var58; ++var71) {
                                       System.arraycopy(var114, var68, var53, var70, (int)var80);
                                       var68 += var69;
                                       var70 += (int)var80;
                                    }
                                 }
                              } else {
                                 for(var66 = 0; var66 < var59; ++var66) {
                                    var53[var66] = (byte)var102[var66];
                                 }
                              }
                           }

                           if (var5 == 1) {
                              this.output.write(var53, 0, var59);
                           } else if (var5 == 32773) {
                              var66 = compressPackBits(var53, var58, (int)var80, var94);
                              var79[var56++] = (long)var66;
                              this.output.write(var94, 0, var66);
                           } else {
                              if (var5 != 7) {
                                 if (var5 == 32946) {
                                    var66 = deflate(var48, var53, var94);
                                    var79[var56++] = (long)var66;
                                    this.output.write(var94, 0, var66);
                                 }
                                 break;
                              }

                              long var115 = this.getOffset(this.output);
                              if (var85 == null || var84.getWidth() != var61.getWidth() || var84.getHeight() != var61.getHeight()) {
                                 var84 = JPEGCodec.getDefaultJPEGEncodeParam(var61, var40);
                                 modifyEncodeParam(var78, var84, var13);
                                 var85 = JPEGCodec.createJPEGEncoder(this.output, var84);
                              }

                              if (var49) {
                                 WritableRaster var117 = null;
                                 if (var61 instanceof WritableRaster) {
                                    var117 = (WritableRaster)var61;
                                 } else {
                                    var117 = var61.createCompatibleWritableRaster();
                                    var117.setRect(var61);
                                 }

                                 if (var117.getMinX() != 0 || var117.getMinY() != 0) {
                                    var117 = var117.createWritableTranslatedChild(0, 0);
                                 }

                                 BufferedImage var116 = new BufferedImage(var16, var117, false, (Hashtable)null);
                                 var85.encode(var116);
                              } else {
                                 var85.encode(var61.createTranslatedChild(0, 0));
                              }

                              long var119 = this.getOffset(this.output);
                              var79[var56++] = (long)((int)(var119 - var115));
                           }
                           break;
                        case 16:
                           var66 = 0;

                           for(var67 = 0; var67 < var59; ++var67) {
                              var68 = var102[var67];
                              var53[var66++] = (byte)((var68 & '\uff00') >> 8);
                              var53[var66++] = (byte)(var68 & 255);
                           }

                           if (var5 == 1) {
                              this.output.write(var53, 0, var59 * 2);
                           } else if (var5 == 32773) {
                              var67 = compressPackBits(var53, var58, (int)var80, var94);
                              var79[var56++] = (long)var67;
                              this.output.write(var94, 0, var67);
                           } else if (var5 == 32946) {
                              var67 = deflate(var48, var53, var94);
                              var79[var56++] = (long)var67;
                              this.output.write(var94, 0, var67);
                           }
                           break;
                        case 32:
                           if (var14 == 3) {
                              var67 = 0;

                              for(var68 = 0; var68 < var59; ++var68) {
                                 var69 = var102[var68];
                                 var53[var67++] = (byte)((var69 & -16777216) >>> 24);
                                 var53[var67++] = (byte)((var69 & 16711680) >>> 16);
                                 var53[var67++] = (byte)((var69 & '\uff00') >>> 8);
                                 var53[var67++] = (byte)(var69 & 255);
                              }
                           } else {
                              var67 = 0;

                              for(var68 = 0; var68 < var59; ++var68) {
                                 var69 = Float.floatToIntBits(var101[var68]);
                                 var53[var67++] = (byte)((var69 & -16777216) >>> 24);
                                 var53[var67++] = (byte)((var69 & 16711680) >>> 16);
                                 var53[var67++] = (byte)((var69 & '\uff00') >>> 8);
                                 var53[var67++] = (byte)(var69 & 255);
                              }
                           }

                           if (var5 == 1) {
                              this.output.write(var53, 0, var59 * 4);
                           } else if (var5 == 32773) {
                              var67 = compressPackBits(var53, var58, (int)var80, var94);
                              var79[var56++] = (long)var67;
                              this.output.write(var94, 0, var67);
                           } else if (var5 == 32946) {
                              var67 = deflate(var48, var53, var94);
                              var79[var56++] = (long)var67;
                              this.output.write(var94, 0, var67);
                           }
                     }
                  }
               }

               if (var5 == 1) {
                  if (var98) {
                     this.output.write(0);
                  }
               } else {
                  var57 = 0;

                  for(var58 = 1; var58 < var27; ++var58) {
                     var59 = (int)var79[var58 - 1];
                     var57 += var59;
                     var35[var58] = var35[var58 - 1] + (long)var59;
                  }

                  var57 += (int)var79[var27 - 1];
                  var97 = var4 ? 0 : var3 + var87 + var57;
                  if ((var97 & 1) != 0) {
                     ++var97;
                     var98 = true;
                  }

                  if (var95 == null) {
                     if (var98) {
                        this.output.write(0);
                     }

                     SeekableOutputStream var103 = (SeekableOutputStream)this.output;
                     long var105 = var103.getFilePointer();
                     var103.seek((long)var3);
                     this.writeDirectory(var3, var36, var97);
                     var103.seek(var105);
                  } else if (var96 != null) {
                     FileInputStream var104 = new FileInputStream(var96);
                     this.output.close();
                     this.output = var95;
                     this.writeDirectory(var3, var36, var97);
                     byte[] var107 = new byte[8192];

                     int var108;
                     for(var60 = 0; var60 < var57; var60 += var108) {
                        var108 = var104.read(var107);
                        if (var108 == -1) {
                           break;
                        }

                        this.output.write(var107, 0, var108);
                     }

                     var104.close();
                     var96.delete();
                     if (var98) {
                        this.output.write(0);
                     }
                  } else {
                     if (!(this.output instanceof ByteArrayOutputStream)) {
                        throw new IllegalStateException();
                     }

                     ByteArrayOutputStream var106 = (ByteArrayOutputStream)this.output;
                     this.output = var95;
                     this.writeDirectory(var3, var36, var97);
                     var106.writeTo(this.output);
                     if (var98) {
                        this.output.write(0);
                     }
                  }
               }

               return var97;
            }
         }
      }
   }

   private int getDirectorySize(SortedSet var1) {
      int var2 = var1.size();
      int var3 = 2 + var2 * 12 + 4;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         TIFFField var5 = (TIFFField)var4.next();
         int var6 = var5.getCount() * sizeOfType[var5.getType()];
         if (var6 > 4) {
            var3 += var6;
         }
      }

      return var3;
   }

   private void writeFileHeader() throws IOException {
      this.output.write(77);
      this.output.write(77);
      this.output.write(0);
      this.output.write(42);
      this.writeLong(8L);
   }

   private void writeDirectory(int var1, SortedSet var2, int var3) throws IOException {
      int var4 = var2.size();
      long var5 = (long)(var1 + 12 * var4 + 4 + 2);
      ArrayList var7 = new ArrayList();
      this.writeUnsignedShort(var4);
      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         TIFFField var9 = (TIFFField)var8.next();
         int var10 = var9.getTag();
         this.writeUnsignedShort(var10);
         int var11 = var9.getType();
         this.writeUnsignedShort(var11);
         int var12 = var9.getCount();
         int var13 = getValueSize(var9);
         this.writeLong(var11 == 2 ? (long)var13 : (long)var12);
         if (var13 > 4) {
            this.writeLong(var5);
            var5 += (long)var13;
            var7.add(var9);
         } else {
            this.writeValuesAsFourBytes(var9);
         }
      }

      this.writeLong((long)var3);

      for(int var14 = 0; var14 < var7.size(); ++var14) {
         this.writeValues((TIFFField)var7.get(var14));
      }

   }

   private static int getValueSize(TIFFField var0) {
      int var1 = var0.getType();
      int var2 = var0.getCount();
      int var3 = 0;
      if (var1 == 2) {
         for(int var4 = 0; var4 < var2; ++var4) {
            byte[] var5 = var0.getAsString(var4).getBytes();
            var3 += var5.length;
            if (var5[var5.length - 1] != 0) {
               ++var3;
            }
         }
      } else {
         var3 = var2 * sizeOfType[var1];
      }

      return var3;
   }

   private void writeValuesAsFourBytes(TIFFField var1) throws IOException {
      int var2 = var1.getType();
      int var3 = var1.getCount();
      switch (var2) {
         case 1:
            byte[] var4 = var1.getAsBytes();
            if (var3 > 4) {
               var3 = 4;
            }

            int var8;
            for(var8 = 0; var8 < var3; ++var8) {
               this.output.write(var4[var8]);
            }

            for(var8 = 0; var8 < 4 - var3; ++var8) {
               this.output.write(0);
            }
         case 2:
         default:
            break;
         case 3:
            char[] var5 = var1.getAsChars();
            if (var3 > 2) {
               var3 = 2;
            }

            int var9;
            for(var9 = 0; var9 < var3; ++var9) {
               this.writeUnsignedShort(var5[var9]);
            }

            for(var9 = 0; var9 < 2 - var3; ++var9) {
               this.writeUnsignedShort(0);
            }

            return;
         case 4:
            long[] var6 = var1.getAsLongs();

            for(int var7 = 0; var7 < var3; ++var7) {
               this.writeLong(var6[var7]);
            }
      }

   }

   private void writeValues(TIFFField var1) throws IOException {
      int var2 = var1.getType();
      int var3 = var1.getCount();
      int var10;
      int var19;
      switch (var2) {
         case 1:
         case 6:
         case 7:
            byte[] var4 = var1.getAsBytes();

            for(int var13 = 0; var13 < var3; ++var13) {
               this.output.write(var4[var13]);
            }

            return;
         case 2:
            for(var19 = 0; var19 < var3; ++var19) {
               byte[] var12 = var1.getAsString(var19).getBytes();
               this.output.write(var12);
               if (var12[var12.length - 1] != 0) {
                  this.output.write(0);
               }
            }

            return;
         case 3:
            char[] var5 = var1.getAsChars();

            for(int var14 = 0; var14 < var3; ++var14) {
               this.writeUnsignedShort(var5[var14]);
            }

            return;
         case 4:
         case 9:
            long[] var15 = var1.getAsLongs();

            for(int var16 = 0; var16 < var3; ++var16) {
               this.writeLong(var15[var16]);
            }

            return;
         case 5:
         case 10:
            long[][] var18 = var1.getAsRationals();

            for(var19 = 0; var19 < var3; ++var19) {
               this.writeLong(var18[var19][0]);
               this.writeLong(var18[var19][1]);
            }

            return;
         case 8:
            short[] var6 = var1.getAsShorts();

            for(int var7 = 0; var7 < var3; ++var7) {
               this.writeUnsignedShort(var6[var7]);
            }

            return;
         case 11:
            float[] var8 = var1.getAsFloats();

            for(int var17 = 0; var17 < var3; ++var17) {
               var10 = Float.floatToIntBits(var8[var17]);
               this.writeLong((long)var10);
            }

            return;
         case 12:
            double[] var9 = var1.getAsDoubles();

            for(var10 = 0; var10 < var3; ++var10) {
               long var11 = Double.doubleToLongBits(var9[var10]);
               this.writeLong(var11 >>> 32);
               this.writeLong(var11 & 4294967295L);
            }

            return;
         default:
            throw new Error("TIFFImageEncoder10");
      }
   }

   private void writeUnsignedShort(int var1) throws IOException {
      this.output.write((var1 & '\uff00') >>> 8);
      this.output.write(var1 & 255);
   }

   private void writeLong(long var1) throws IOException {
      this.output.write((int)((var1 & -16777216L) >>> 24));
      this.output.write((int)((var1 & 16711680L) >>> 16));
      this.output.write((int)((var1 & 65280L) >>> 8));
      this.output.write((int)(var1 & 255L));
   }

   private long getOffset(OutputStream var1) throws IOException {
      if (var1 instanceof ByteArrayOutputStream) {
         return (long)((ByteArrayOutputStream)var1).size();
      } else if (var1 instanceof SeekableOutputStream) {
         return ((SeekableOutputStream)var1).getFilePointer();
      } else {
         throw new IllegalStateException();
      }
   }

   private static int compressPackBits(byte[] var0, int var1, int var2, byte[] var3) {
      int var4 = 0;
      int var5 = 0;

      for(int var6 = 0; var6 < var1; ++var6) {
         var5 = packBits(var0, var4, var2, var3, var5);
         var4 += var2;
      }

      return var5;
   }

   private static int packBits(byte[] var0, int var1, int var2, byte[] var3, int var4) {
      int var5 = var1 + var2 - 1;
      int var6 = var5 - 1;

      while(true) {
         while(true) {
            int var7;
            do {
               if (var1 > var5) {
                  return var4;
               }

               var7 = 1;

               byte var8;
               for(var8 = var0[var1]; var7 < 127 && var1 < var5 && var0[var1] == var0[var1 + 1]; ++var1) {
                  ++var7;
               }

               if (var7 > 1) {
                  ++var1;
                  var3[var4++] = (byte)(-(var7 - 1));
                  var3[var4++] = var8;
               }

               for(var7 = 0; var7 < 128 && (var1 < var5 && var0[var1] != var0[var1 + 1] || var1 < var6 && var0[var1] != var0[var1 + 2]); var3[var4] = var0[var1++]) {
                  ++var7;
                  ++var4;
               }

               if (var7 > 0) {
                  var3[var4] = (byte)(var7 - 1);
                  ++var4;
               }
            } while(var1 != var5);

            if (var7 > 0 && var7 < 128) {
               ++var3[var4];
               var3[var4++] = var0[var1++];
            } else {
               var3[var4++] = 0;
               var3[var4++] = var0[var1++];
            }
         }
      }
   }

   private static int deflate(Deflater var0, byte[] var1, byte[] var2) {
      var0.setInput(var1);
      var0.finish();
      int var3 = var0.deflate(var2);
      var0.reset();
      return var3;
   }

   private static void modifyEncodeParam(JPEGEncodeParam var0, JPEGEncodeParam var1, int var2) {
      var1.setDensityUnit(var0.getDensityUnit());
      var1.setXDensity(var0.getXDensity());
      var1.setYDensity(var0.getYDensity());
      var1.setRestartInterval(var0.getRestartInterval());

      for(int var3 = 0; var3 < 4; ++var3) {
         JPEGQTable var4 = var0.getQTable(var3);
         if (var4 != null) {
            var1.setQTable(var3, var4);
         }
      }

   }
}
