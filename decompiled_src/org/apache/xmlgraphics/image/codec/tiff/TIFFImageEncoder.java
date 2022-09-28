package org.apache.xmlgraphics.image.codec.tiff;

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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.Deflater;
import org.apache.xmlgraphics.image.codec.util.ImageEncodeParam;
import org.apache.xmlgraphics.image.codec.util.ImageEncoderImpl;
import org.apache.xmlgraphics.image.codec.util.SeekableOutputStream;

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

   public TIFFImageEncoder(OutputStream output, ImageEncodeParam param) {
      super(output, param);
      if (this.param == null) {
         this.param = new TIFFEncodeParam();
      }

   }

   public void encode(RenderedImage im) throws IOException {
      this.writeFileHeader();
      TIFFEncodeParam encodeParam = (TIFFEncodeParam)this.param;
      Iterator iter = encodeParam.getExtraImages();
      if (iter != null) {
         int ifdOffset = 8;
         RenderedImage nextImage = im;
         TIFFEncodeParam nextParam = encodeParam;

         boolean hasNext;
         do {
            hasNext = iter.hasNext();
            ifdOffset = this.encode(nextImage, nextParam, ifdOffset, !hasNext);
            if (hasNext) {
               Object obj = iter.next();
               if (obj instanceof RenderedImage) {
                  nextImage = (RenderedImage)obj;
                  nextParam = encodeParam;
               } else if (obj instanceof Object[]) {
                  Object[] o = (Object[])obj;
                  nextImage = (RenderedImage)o[0];
                  nextParam = (TIFFEncodeParam)o[1];
               }
            }
         } while(hasNext);
      } else {
         this.encode(im, encodeParam, 8, true);
      }

   }

   public Object encodeMultiple(Object context, RenderedImage img) throws IOException {
      TIFFEncodeParam encodeParam = (TIFFEncodeParam)this.param;
      if (encodeParam.getExtraImages() != null) {
         throw new IllegalStateException("Extra images may not be used when calling encodeMultiple!");
      } else {
         Context c = (Context)context;
         if (c == null) {
            c = new Context();
            this.writeFileHeader();
         } else {
            c.ifdOffset = this.encode(c.nextImage, encodeParam, c.ifdOffset, false);
         }

         c.nextImage = img;
         return c;
      }
   }

   public void finishMultiple(Object context) throws IOException {
      if (context == null) {
         throw new NullPointerException("context must not be null");
      } else {
         Context c = (Context)context;
         TIFFEncodeParam encodeParam = (TIFFEncodeParam)this.param;
         c.ifdOffset = this.encode(c.nextImage, encodeParam, c.ifdOffset, true);
      }
   }

   private int encode(RenderedImage im, TIFFEncodeParam encodeParam, int ifdOffset, boolean isLast) throws IOException {
      int compression = encodeParam.getCompression();
      boolean isTiled = encodeParam.getWriteTiled();
      int minX = im.getMinX();
      int minY = im.getMinY();
      int width = im.getWidth();
      int height = im.getHeight();
      SampleModel sampleModel = im.getSampleModel();
      int[] sampleSize = sampleModel.getSampleSize();

      int numBands;
      for(numBands = 1; numBands < sampleSize.length; ++numBands) {
         if (sampleSize[numBands] != sampleSize[0]) {
            throw new Error("TIFFImageEncoder0");
         }
      }

      numBands = sampleModel.getNumBands();
      if ((sampleSize[0] == 1 || sampleSize[0] == 4) && numBands != 1) {
         throw new Error("TIFFImageEncoder1");
      } else {
         int dataType = sampleModel.getDataType();
         switch (dataType) {
            case 0:
               if (sampleSize[0] != 1 && sampleSize[0] == 4 && sampleSize[0] != 8) {
                  throw new Error("TIFFImageEncoder2");
               }
               break;
            case 1:
            case 2:
               if (sampleSize[0] != 16) {
                  throw new Error("TIFFImageEncoder3");
               }
               break;
            case 3:
            case 4:
               if (sampleSize[0] != 32) {
                  throw new Error("TIFFImageEncoder4");
               }
               break;
            default:
               throw new Error("TIFFImageEncoder5");
         }

         boolean dataTypeIsShort = dataType == 2 || dataType == 1;
         ColorModel colorModel = im.getColorModel();
         if (colorModel != null && colorModel instanceof IndexColorModel && dataType != 0) {
            throw new Error("TIFFImageEncoder6");
         } else {
            IndexColorModel icm = null;
            int sizeOfColormap = 0;
            char[] colormap = null;
            int imageType = -1;
            int numExtraSamples = 0;
            int extraSampleType = 0;
            byte[] r;
            byte[] g;
            byte[] b;
            if (colorModel instanceof IndexColorModel) {
               icm = (IndexColorModel)colorModel;
               int mapSize = icm.getMapSize();
               if (sampleSize[0] == 1 && numBands == 1) {
                  if (mapSize != 2) {
                     throw new IllegalArgumentException("TIFFImageEncoder7");
                  }

                  r = new byte[mapSize];
                  icm.getReds(r);
                  g = new byte[mapSize];
                  icm.getGreens(g);
                  b = new byte[mapSize];
                  icm.getBlues(b);
                  if ((r[0] & 255) == 0 && (r[1] & 255) == 255 && (g[0] & 255) == 0 && (g[1] & 255) == 255 && (b[0] & 255) == 0 && (b[1] & 255) == 255) {
                     imageType = 1;
                  } else if ((r[0] & 255) == 255 && (r[1] & 255) == 0 && (g[0] & 255) == 255 && (g[1] & 255) == 0 && (b[0] & 255) == 255 && (b[1] & 255) == 0) {
                     imageType = 0;
                  } else {
                     imageType = 3;
                  }
               } else if (numBands == 1) {
                  imageType = 3;
               }
            } else if (colorModel == null) {
               if (sampleSize[0] == 1 && numBands == 1) {
                  imageType = 1;
               } else {
                  imageType = 8;
                  if (numBands > 1) {
                     numExtraSamples = numBands - 1;
                  }
               }
            } else {
               ColorSpace colorSpace = colorModel.getColorSpace();
               switch (colorSpace.getType()) {
                  case 1:
                     imageType = 7;
                     break;
                  case 2:
                  case 4:
                  case 7:
                  case 8:
                  default:
                     imageType = 8;
                     break;
                  case 3:
                     imageType = 6;
                     break;
                  case 5:
                     if (compression == 7 && encodeParam.getJPEGCompressRGBToYCbCr()) {
                        imageType = 6;
                        break;
                     }

                     imageType = 4;
                     break;
                  case 6:
                     imageType = 2;
                     break;
                  case 9:
                     imageType = 5;
               }

               if (imageType == 8) {
                  numExtraSamples = numBands - 1;
               } else if (numBands > 1) {
                  numExtraSamples = numBands - colorSpace.getNumComponents();
               }

               if (numExtraSamples == 1 && colorModel.hasAlpha()) {
                  extraSampleType = colorModel.isAlphaPremultiplied() ? 1 : 2;
               }
            }

            if (imageType == -1) {
               throw new Error("TIFFImageEncoder8");
            } else {
               if (compression == 7) {
                  if (imageType == 3) {
                     throw new Error("TIFFImageEncoder11");
                  }

                  if (sampleSize[0] != 8 || imageType != 2 && imageType != 4 && imageType != 6) {
                     throw new Error("TIFFImageEncoder9");
                  }
               }

               int photometricInterpretation = true;
               int numTiles;
               int maxSubV;
               int factorV;
               int factorH;
               int subV;
               byte photometricInterpretation;
               switch (imageType) {
                  case 0:
                     photometricInterpretation = 0;
                     break;
                  case 1:
                     photometricInterpretation = 1;
                     break;
                  case 2:
                  case 8:
                     photometricInterpretation = 1;
                     break;
                  case 3:
                     photometricInterpretation = 3;
                     icm = (IndexColorModel)colorModel;
                     sizeOfColormap = icm.getMapSize();
                     r = new byte[sizeOfColormap];
                     icm.getReds(r);
                     g = new byte[sizeOfColormap];
                     icm.getGreens(g);
                     b = new byte[sizeOfColormap];
                     icm.getBlues(b);
                     numTiles = 0;
                     maxSubV = sizeOfColormap;
                     factorV = 2 * sizeOfColormap;
                     colormap = new char[sizeOfColormap * 3];

                     for(factorH = 0; factorH < sizeOfColormap; ++factorH) {
                        subV = 255 & r[factorH];
                        colormap[numTiles++] = (char)(subV << 8 | subV);
                        subV = 255 & g[factorH];
                        colormap[maxSubV++] = (char)(subV << 8 | subV);
                        subV = 255 & b[factorH];
                        colormap[factorV++] = (char)(subV << 8 | subV);
                     }

                     sizeOfColormap *= 3;
                     break;
                  case 4:
                     photometricInterpretation = 2;
                     break;
                  case 5:
                     photometricInterpretation = 5;
                     break;
                  case 6:
                     photometricInterpretation = 6;
                     break;
                  case 7:
                     photometricInterpretation = 8;
                     break;
                  default:
                     throw new Error("TIFFImageEncoder8");
               }

               int tileWidth;
               int tileHeight;
               if (isTiled) {
                  tileWidth = encodeParam.getTileWidth() > 0 ? encodeParam.getTileWidth() : im.getTileWidth();
                  tileHeight = encodeParam.getTileHeight() > 0 ? encodeParam.getTileHeight() : im.getTileHeight();
               } else {
                  tileWidth = width;
                  tileHeight = encodeParam.getTileHeight() > 0 ? encodeParam.getTileHeight() : 8;
               }

               JPEGEncodeParam jep = null;
               if (compression == 7) {
                  jep = encodeParam.getJPEGEncodeParam();
                  numTiles = jep.getHorizontalSubsampling(0);
                  maxSubV = jep.getVerticalSubsampling(0);

                  for(factorV = 1; factorV < numBands; ++factorV) {
                     factorH = jep.getHorizontalSubsampling(factorV);
                     if (factorH > numTiles) {
                        numTiles = factorH;
                     }

                     subV = jep.getVerticalSubsampling(factorV);
                     if (subV > maxSubV) {
                        maxSubV = subV;
                     }
                  }

                  factorV = 8 * maxSubV;
                  tileHeight = (int)((float)tileHeight / (float)factorV + 0.5F) * factorV;
                  if (tileHeight < factorV) {
                     tileHeight = factorV;
                  }

                  if (isTiled) {
                     factorH = 8 * numTiles;
                     tileWidth = (int)((float)tileWidth / (float)factorH + 0.5F) * factorH;
                     if (tileWidth < factorH) {
                        tileWidth = factorH;
                     }
                  }
               }

               if (isTiled) {
                  numTiles = (width + tileWidth - 1) / tileWidth * ((height + tileHeight - 1) / tileHeight);
               } else {
                  numTiles = (int)Math.ceil((double)height / (double)tileHeight);
               }

               long[] tileByteCounts = new long[numTiles];
               long bytesPerRow = (long)Math.ceil((double)sampleSize[0] / 8.0 * (double)tileWidth * (double)numBands);
               long bytesPerTile = bytesPerRow * (long)tileHeight;

               for(int i = 0; i < numTiles; ++i) {
                  tileByteCounts[i] = bytesPerTile;
               }

               long totalBytesOfData;
               if (!isTiled) {
                  totalBytesOfData = (long)(height - tileHeight * (numTiles - 1));
                  tileByteCounts[numTiles - 1] = totalBytesOfData * bytesPerRow;
               }

               totalBytesOfData = bytesPerTile * (long)(numTiles - 1) + tileByteCounts[numTiles - 1];
               long[] tileOffsets = new long[numTiles];
               SortedSet fields = new TreeSet();
               fields.add(new TIFFField(256, 4, 1, new long[]{(long)width}));
               fields.add(new TIFFField(257, 4, 1, new long[]{(long)height}));
               char[] shortSampleSize = new char[numBands];

               for(int i = 0; i < numBands; ++i) {
                  shortSampleSize[i] = (char)sampleSize[i];
               }

               fields.add(new TIFFField(258, 3, numBands, shortSampleSize));
               fields.add(new TIFFField(259, 3, 1, new char[]{(char)compression}));
               fields.add(new TIFFField(262, 3, 1, new char[]{(char)photometricInterpretation}));
               if (!isTiled) {
                  fields.add(new TIFFField(273, 4, numTiles, tileOffsets));
               }

               fields.add(new TIFFField(277, 3, 1, new char[]{(char)numBands}));
               if (!isTiled) {
                  fields.add(new TIFFField(278, 4, 1, new long[]{(long)tileHeight}));
                  fields.add(new TIFFField(279, 4, numTiles, tileByteCounts));
               }

               if (colormap != null) {
                  fields.add(new TIFFField(320, 3, sizeOfColormap, colormap));
               }

               if (isTiled) {
                  fields.add(new TIFFField(322, 4, 1, new long[]{(long)tileWidth}));
                  fields.add(new TIFFField(323, 4, 1, new long[]{(long)tileHeight}));
                  fields.add(new TIFFField(324, 4, numTiles, tileOffsets));
                  fields.add(new TIFFField(325, 4, numTiles, tileByteCounts));
               }

               int b;
               char[] sampleFormat;
               if (numExtraSamples > 0) {
                  sampleFormat = new char[numExtraSamples];

                  for(b = 0; b < numExtraSamples; ++b) {
                     sampleFormat[b] = (char)extraSampleType;
                  }

                  fields.add(new TIFFField(338, 3, numExtraSamples, sampleFormat));
               }

               if (dataType != 0) {
                  sampleFormat = new char[numBands];
                  if (dataType == 4) {
                     sampleFormat[0] = 3;
                  } else if (dataType == 1) {
                     sampleFormat[0] = 1;
                  } else {
                     sampleFormat[0] = 2;
                  }

                  for(b = 1; b < numBands; ++b) {
                     sampleFormat[b] = sampleFormat[0];
                  }

                  fields.add(new TIFFField(339, 3, numBands, sampleFormat));
               }

               JPEGEncodeParam jpegEncodeParam = null;
               JPEGImageEncoder jpegEncoder = null;
               int jpegColorID = 0;
               if (compression == 7) {
                  jpegColorID = 0;
                  switch (imageType) {
                     case 2:
                     case 3:
                        jpegColorID = 1;
                        break;
                     case 4:
                        if (colorModel.hasAlpha()) {
                           jpegColorID = 6;
                        } else {
                           jpegColorID = 2;
                        }
                     case 5:
                     default:
                        break;
                     case 6:
                        if (colorModel.hasAlpha()) {
                           jpegColorID = 7;
                        } else {
                           jpegColorID = 3;
                        }
                  }

                  Raster tile00 = im.getTile(0, 0);
                  jpegEncodeParam = JPEGCodec.getDefaultJPEGEncodeParam(tile00, jpegColorID);
                  modifyEncodeParam(jep, jpegEncodeParam, numBands);
                  jpegEncodeParam.setImageInfoValid(false);
                  jpegEncodeParam.setTableInfoValid(true);
                  ByteArrayOutputStream tableStream = new ByteArrayOutputStream();
                  jpegEncoder = JPEGCodec.createJPEGEncoder(tableStream, jpegEncodeParam);
                  jpegEncoder.encode(tile00);
                  byte[] tableData = tableStream.toByteArray();
                  fields.add(new TIFFField(347, 7, tableData.length, tableData));
                  jpegEncoder = null;
               }

               int subH;
               int subV;
               int dirSize;
               if (imageType == 6) {
                  char subsampleH = 1;
                  dirSize = 1;
                  if (compression == 7) {
                     subsampleH = (char)jep.getHorizontalSubsampling(0);
                     dirSize = (char)jep.getVerticalSubsampling(0);

                     for(int i = 1; i < numBands; ++i) {
                        subH = (char)jep.getHorizontalSubsampling(i);
                        if (subH > subsampleH) {
                           subsampleH = (char)subH;
                        }

                        subV = (char)jep.getVerticalSubsampling(i);
                        if (subV > dirSize) {
                           dirSize = subV;
                        }
                     }
                  }

                  fields.add(new TIFFField(530, 3, 2, new char[]{subsampleH, (char)dirSize}));
                  fields.add(new TIFFField(531, 3, 1, new char[]{(char)(compression == 7 ? 1 : 2)}));
                  long[][] refbw;
                  if (compression == 7) {
                     refbw = new long[][]{{0L, 1L}, {255L, 1L}, {128L, 1L}, {255L, 1L}, {128L, 1L}, {255L, 1L}};
                  } else {
                     refbw = new long[][]{{15L, 1L}, {235L, 1L}, {128L, 1L}, {240L, 1L}, {128L, 1L}, {240L, 1L}};
                  }

                  fields.add(new TIFFField(532, 5, 6, refbw));
               }

               TIFFField[] extraFields = encodeParam.getExtraFields();
               if (extraFields != null) {
                  List extantTags = new ArrayList(fields.size());
                  Iterator fieldIter = fields.iterator();

                  while(fieldIter.hasNext()) {
                     TIFFField fld = (TIFFField)fieldIter.next();
                     extantTags.add(new Integer(fld.getTag()));
                  }

                  subH = extraFields.length;

                  for(subV = 0; subV < subH; ++subV) {
                     TIFFField fld = extraFields[subV];
                     Integer tagValue = new Integer(fld.getTag());
                     if (!extantTags.contains(tagValue)) {
                        fields.add(fld);
                        extantTags.add(tagValue);
                     }
                  }
               }

               dirSize = this.getDirectorySize(fields);
               tileOffsets[0] = (long)(ifdOffset + dirSize);
               OutputStream outCache = null;
               byte[] compressBuf = null;
               File tempFile = null;
               int nextIFDOffset = 0;
               boolean skipByte = false;
               Deflater deflater = null;
               boolean jpegRGBToYCbCr = false;
               int numBytesPadding;
               if (compression == 1) {
                  numBytesPadding = 0;
                  if (sampleSize[0] == 16 && tileOffsets[0] % 2L != 0L) {
                     numBytesPadding = 1;
                     int var10002 = tileOffsets[0]++;
                  } else if (sampleSize[0] == 32 && tileOffsets[0] % 4L != 0L) {
                     numBytesPadding = (int)(4L - tileOffsets[0] % 4L);
                     tileOffsets[0] += (long)numBytesPadding;
                  }

                  int padding;
                  for(padding = 1; padding < numTiles; ++padding) {
                     tileOffsets[padding] = tileOffsets[padding - 1] + tileByteCounts[padding - 1];
                  }

                  if (!isLast) {
                     nextIFDOffset = (int)(tileOffsets[0] + totalBytesOfData);
                     if ((nextIFDOffset & 1) != 0) {
                        ++nextIFDOffset;
                        skipByte = true;
                     }
                  }

                  this.writeDirectory(ifdOffset, fields, nextIFDOffset);
                  if (numBytesPadding != 0) {
                     for(padding = 0; padding < numBytesPadding; ++padding) {
                        this.output.write(0);
                     }
                  }
               } else {
                  if (this.output instanceof SeekableOutputStream) {
                     ((SeekableOutputStream)this.output).seek(tileOffsets[0]);
                  } else {
                     outCache = this.output;

                     try {
                        tempFile = File.createTempFile("jai-SOS-", ".tmp");
                        tempFile.deleteOnExit();
                        RandomAccessFile raFile = new RandomAccessFile(tempFile, "rw");
                        this.output = new SeekableOutputStream(raFile);
                     } catch (Exception var72) {
                        this.output = new ByteArrayOutputStream((int)totalBytesOfData);
                     }
                  }

                  int bufSize = false;
                  switch (compression) {
                     case 7:
                        numBytesPadding = 0;
                        if (imageType == 6 && colorModel != null && colorModel.getColorSpace().getType() == 5) {
                           jpegRGBToYCbCr = true;
                        }
                        break;
                     case 32773:
                        numBytesPadding = (int)(bytesPerTile + (bytesPerRow + 127L) / 128L * (long)tileHeight);
                        break;
                     case 32946:
                        numBytesPadding = (int)bytesPerTile;
                        deflater = new Deflater(encodeParam.getDeflateLevel());
                        break;
                     default:
                        numBytesPadding = 0;
                  }

                  if (numBytesPadding != 0) {
                     compressBuf = new byte[numBytesPadding];
                  }
               }

               int[] pixels = null;
               float[] fpixels = null;
               boolean checkContiguous = sampleSize[0] == 1 && sampleModel instanceof MultiPixelPackedSampleModel && dataType == 0 || sampleSize[0] == 8 && sampleModel instanceof ComponentSampleModel;
               byte[] bpixels = null;
               if (compression != 7) {
                  if (dataType == 0) {
                     bpixels = new byte[tileHeight * tileWidth * numBands];
                  } else if (dataTypeIsShort) {
                     bpixels = new byte[2 * tileHeight * tileWidth * numBands];
                  } else if (dataType == 3 || dataType == 4) {
                     bpixels = new byte[4 * tileHeight * tileWidth * numBands];
                  }
               }

               int lastRow = minY + height;
               int lastCol = minX + width;
               int tileNum = 0;

               int totalBytes;
               int rows;
               int size;
               int bytesCopied;
               for(totalBytes = minY; totalBytes < lastRow; totalBytes += tileHeight) {
                  rows = isTiled ? tileHeight : Math.min(tileHeight, lastRow - totalBytes);
                  size = rows * tileWidth * numBands;

                  for(bytesCopied = minX; bytesCopied < lastCol; bytesCopied += tileWidth) {
                     Raster src = im.getData(new Rectangle(bytesCopied, totalBytes, tileWidth, rows));
                     boolean useDataBuffer = false;
                     int numCompressedBytes;
                     int j;
                     int inOffset;
                     if (compression != 7) {
                        if (checkContiguous) {
                           if (sampleSize[0] == 8) {
                              ComponentSampleModel csm = (ComponentSampleModel)src.getSampleModel();
                              int[] bankIndices = csm.getBankIndices();
                              int[] bandOffsets = csm.getBandOffsets();
                              numCompressedBytes = csm.getPixelStride();
                              j = csm.getScanlineStride();
                              if (numCompressedBytes == numBands && (long)j == bytesPerRow) {
                                 useDataBuffer = true;

                                 for(inOffset = 0; useDataBuffer && inOffset < numBands; ++inOffset) {
                                    if (bankIndices[inOffset] != 0 || bandOffsets[inOffset] != inOffset) {
                                       useDataBuffer = false;
                                    }
                                 }
                              } else {
                                 useDataBuffer = false;
                              }
                           } else {
                              MultiPixelPackedSampleModel mpp = (MultiPixelPackedSampleModel)src.getSampleModel();
                              if (mpp.getNumBands() == 1 && mpp.getDataBitOffset() == 0 && mpp.getPixelBitStride() == 1) {
                                 useDataBuffer = true;
                              }
                           }
                        }

                        if (!useDataBuffer) {
                           if (dataType == 4) {
                              fpixels = src.getPixels(bytesCopied, totalBytes, tileWidth, rows, fpixels);
                           } else {
                              pixels = src.getPixels(bytesCopied, totalBytes, tileWidth, rows, pixels);
                           }
                        }
                     }

                     int pixel = false;
                     int k = 0;
                     int lineStride;
                     int outOffset;
                     int j;
                     int index;
                     int pixel;
                     byte[] btmp;
                     switch (sampleSize[0]) {
                        case 1:
                           if (useDataBuffer) {
                              btmp = ((DataBufferByte)src.getDataBuffer()).getData();
                              MultiPixelPackedSampleModel mpp = (MultiPixelPackedSampleModel)src.getSampleModel();
                              inOffset = mpp.getScanlineStride();
                              lineStride = mpp.getOffset(bytesCopied - src.getSampleModelTranslateX(), totalBytes - src.getSampleModelTranslateY());
                              if (inOffset == (int)bytesPerRow) {
                                 System.arraycopy(btmp, lineStride, bpixels, 0, (int)bytesPerRow * rows);
                              } else {
                                 outOffset = 0;

                                 for(j = 0; j < rows; ++j) {
                                    System.arraycopy(btmp, lineStride, bpixels, outOffset, (int)bytesPerRow);
                                    lineStride += inOffset;
                                    outOffset += (int)bytesPerRow;
                                 }
                              }
                           } else {
                              index = 0;

                              for(numCompressedBytes = 0; numCompressedBytes < rows; ++numCompressedBytes) {
                                 for(j = 0; j < tileWidth / 8; ++j) {
                                    pixel = pixels[index++] << 7 | pixels[index++] << 6 | pixels[index++] << 5 | pixels[index++] << 4 | pixels[index++] << 3 | pixels[index++] << 2 | pixels[index++] << 1 | pixels[index++];
                                    bpixels[k++] = (byte)pixel;
                                 }

                                 if (tileWidth % 8 > 0) {
                                    pixel = 0;

                                    for(j = 0; j < tileWidth % 8; ++j) {
                                       pixel |= pixels[index++] << 7 - j;
                                    }

                                    bpixels[k++] = (byte)pixel;
                                 }
                              }
                           }

                           if (compression == 1) {
                              this.output.write(bpixels, 0, rows * ((tileWidth + 7) / 8));
                           } else if (compression == 32773) {
                              numCompressedBytes = compressPackBits(bpixels, rows, (int)bytesPerRow, compressBuf);
                              tileByteCounts[tileNum++] = (long)numCompressedBytes;
                              this.output.write(compressBuf, 0, numCompressedBytes);
                           } else if (compression == 32946) {
                              numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                              tileByteCounts[tileNum++] = (long)numCompressedBytes;
                              this.output.write(compressBuf, 0, numCompressedBytes);
                           }
                           break;
                        case 4:
                           index = 0;

                           for(numCompressedBytes = 0; numCompressedBytes < rows; ++numCompressedBytes) {
                              for(j = 0; j < tileWidth / 2; ++j) {
                                 pixel = pixels[index++] << 4 | pixels[index++];
                                 bpixels[k++] = (byte)pixel;
                              }

                              if ((tileWidth & 1) == 1) {
                                 pixel = pixels[index++] << 4;
                                 bpixels[k++] = (byte)pixel;
                              }
                           }

                           if (compression == 1) {
                              this.output.write(bpixels, 0, rows * ((tileWidth + 1) / 2));
                           } else if (compression == 32773) {
                              numCompressedBytes = compressPackBits(bpixels, rows, (int)bytesPerRow, compressBuf);
                              tileByteCounts[tileNum++] = (long)numCompressedBytes;
                              this.output.write(compressBuf, 0, numCompressedBytes);
                           } else if (compression == 32946) {
                              numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                              tileByteCounts[tileNum++] = (long)numCompressedBytes;
                              this.output.write(compressBuf, 0, numCompressedBytes);
                           }
                           break;
                        case 8:
                           if (compression != 7) {
                              if (useDataBuffer) {
                                 btmp = ((DataBufferByte)src.getDataBuffer()).getData();
                                 ComponentSampleModel csm = (ComponentSampleModel)src.getSampleModel();
                                 inOffset = csm.getOffset(bytesCopied - src.getSampleModelTranslateX(), totalBytes - src.getSampleModelTranslateY());
                                 lineStride = csm.getScanlineStride();
                                 if (lineStride == (int)bytesPerRow) {
                                    System.arraycopy(btmp, inOffset, bpixels, 0, (int)bytesPerRow * rows);
                                 } else {
                                    outOffset = 0;

                                    for(j = 0; j < rows; ++j) {
                                       System.arraycopy(btmp, inOffset, bpixels, outOffset, (int)bytesPerRow);
                                       inOffset += lineStride;
                                       outOffset += (int)bytesPerRow;
                                    }
                                 }
                              } else {
                                 for(numCompressedBytes = 0; numCompressedBytes < size; ++numCompressedBytes) {
                                    bpixels[numCompressedBytes] = (byte)pixels[numCompressedBytes];
                                 }
                              }
                           }

                           if (compression == 1) {
                              this.output.write(bpixels, 0, size);
                           } else if (compression == 32773) {
                              numCompressedBytes = compressPackBits(bpixels, rows, (int)bytesPerRow, compressBuf);
                              tileByteCounts[tileNum++] = (long)numCompressedBytes;
                              this.output.write(compressBuf, 0, numCompressedBytes);
                           } else {
                              if (compression != 7) {
                                 if (compression == 32946) {
                                    numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                                    tileByteCounts[tileNum++] = (long)numCompressedBytes;
                                    this.output.write(compressBuf, 0, numCompressedBytes);
                                 }
                                 break;
                              }

                              long startPos = this.getOffset(this.output);
                              if (jpegEncoder == null || jpegEncodeParam.getWidth() != src.getWidth() || jpegEncodeParam.getHeight() != src.getHeight()) {
                                 jpegEncodeParam = JPEGCodec.getDefaultJPEGEncodeParam(src, jpegColorID);
                                 modifyEncodeParam(jep, jpegEncodeParam, numBands);
                                 jpegEncoder = JPEGCodec.createJPEGEncoder(this.output, jpegEncodeParam);
                              }

                              if (jpegRGBToYCbCr) {
                                 WritableRaster wRas = null;
                                 if (src instanceof WritableRaster) {
                                    wRas = (WritableRaster)src;
                                 } else {
                                    wRas = src.createCompatibleWritableRaster();
                                    wRas.setRect(src);
                                 }

                                 if (wRas.getMinX() != 0 || wRas.getMinY() != 0) {
                                    wRas = wRas.createWritableTranslatedChild(0, 0);
                                 }

                                 BufferedImage bi = new BufferedImage(colorModel, wRas, false, (Hashtable)null);
                                 jpegEncoder.encode(bi);
                              } else {
                                 jpegEncoder.encode(src.createTranslatedChild(0, 0));
                              }

                              long endPos = this.getOffset(this.output);
                              tileByteCounts[tileNum++] = (long)((int)(endPos - startPos));
                           }
                           break;
                        case 16:
                           numCompressedBytes = 0;

                           for(j = 0; j < size; ++j) {
                              inOffset = pixels[j];
                              bpixels[numCompressedBytes++] = (byte)((inOffset & '\uff00') >> 8);
                              bpixels[numCompressedBytes++] = (byte)(inOffset & 255);
                           }

                           if (compression == 1) {
                              this.output.write(bpixels, 0, size * 2);
                           } else if (compression == 32773) {
                              j = compressPackBits(bpixels, rows, (int)bytesPerRow, compressBuf);
                              tileByteCounts[tileNum++] = (long)j;
                              this.output.write(compressBuf, 0, j);
                           } else if (compression == 32946) {
                              j = deflate(deflater, bpixels, compressBuf);
                              tileByteCounts[tileNum++] = (long)j;
                              this.output.write(compressBuf, 0, j);
                           }
                           break;
                        case 32:
                           if (dataType == 3) {
                              j = 0;

                              for(inOffset = 0; inOffset < size; ++inOffset) {
                                 lineStride = pixels[inOffset];
                                 bpixels[j++] = (byte)((lineStride & -16777216) >>> 24);
                                 bpixels[j++] = (byte)((lineStride & 16711680) >>> 16);
                                 bpixels[j++] = (byte)((lineStride & '\uff00') >>> 8);
                                 bpixels[j++] = (byte)(lineStride & 255);
                              }
                           } else {
                              j = 0;

                              for(inOffset = 0; inOffset < size; ++inOffset) {
                                 lineStride = Float.floatToIntBits(fpixels[inOffset]);
                                 bpixels[j++] = (byte)((lineStride & -16777216) >>> 24);
                                 bpixels[j++] = (byte)((lineStride & 16711680) >>> 16);
                                 bpixels[j++] = (byte)((lineStride & '\uff00') >>> 8);
                                 bpixels[j++] = (byte)(lineStride & 255);
                              }
                           }

                           if (compression == 1) {
                              this.output.write(bpixels, 0, size * 4);
                           } else if (compression == 32773) {
                              j = compressPackBits(bpixels, rows, (int)bytesPerRow, compressBuf);
                              tileByteCounts[tileNum++] = (long)j;
                              this.output.write(compressBuf, 0, j);
                           } else if (compression == 32946) {
                              j = deflate(deflater, bpixels, compressBuf);
                              tileByteCounts[tileNum++] = (long)j;
                              this.output.write(compressBuf, 0, j);
                           }
                     }
                  }
               }

               if (compression == 1) {
                  if (skipByte) {
                     this.output.write(0);
                  }
               } else {
                  totalBytes = 0;

                  for(rows = 1; rows < numTiles; ++rows) {
                     size = (int)tileByteCounts[rows - 1];
                     totalBytes += size;
                     tileOffsets[rows] = tileOffsets[rows - 1] + (long)size;
                  }

                  totalBytes += (int)tileByteCounts[numTiles - 1];
                  nextIFDOffset = isLast ? 0 : ifdOffset + dirSize + totalBytes;
                  if ((nextIFDOffset & 1) != 0) {
                     ++nextIFDOffset;
                     skipByte = true;
                  }

                  if (outCache == null) {
                     if (skipByte) {
                        this.output.write(0);
                     }

                     SeekableOutputStream sos = (SeekableOutputStream)this.output;
                     long savePos = sos.getFilePointer();
                     sos.seek((long)ifdOffset);
                     this.writeDirectory(ifdOffset, fields, nextIFDOffset);
                     sos.seek(savePos);
                  } else if (tempFile != null) {
                     FileInputStream fileStream = new FileInputStream(tempFile);
                     this.output.close();
                     this.output = outCache;
                     this.writeDirectory(ifdOffset, fields, nextIFDOffset);
                     byte[] copyBuffer = new byte[8192];

                     int bytesRead;
                     for(bytesCopied = 0; bytesCopied < totalBytes; bytesCopied += bytesRead) {
                        bytesRead = fileStream.read(copyBuffer);
                        if (bytesRead == -1) {
                           break;
                        }

                        this.output.write(copyBuffer, 0, bytesRead);
                     }

                     fileStream.close();
                     tempFile.delete();
                     if (skipByte) {
                        this.output.write(0);
                     }
                  } else {
                     if (!(this.output instanceof ByteArrayOutputStream)) {
                        throw new IllegalStateException();
                     }

                     ByteArrayOutputStream memoryStream = (ByteArrayOutputStream)this.output;
                     this.output = outCache;
                     this.writeDirectory(ifdOffset, fields, nextIFDOffset);
                     memoryStream.writeTo(this.output);
                     if (skipByte) {
                        this.output.write(0);
                     }
                  }
               }

               return nextIFDOffset;
            }
         }
      }
   }

   private int getDirectorySize(SortedSet fields) {
      int numEntries = fields.size();
      int dirSize = 2 + numEntries * 12 + 4;
      Iterator iter = fields.iterator();

      while(iter.hasNext()) {
         TIFFField field = (TIFFField)iter.next();
         int valueSize = field.getCount() * sizeOfType[field.getType()];
         if (valueSize > 4) {
            dirSize += valueSize;
         }
      }

      return dirSize;
   }

   private void writeFileHeader() throws IOException {
      this.output.write(77);
      this.output.write(77);
      this.output.write(0);
      this.output.write(42);
      this.writeLong(8L);
   }

   private void writeDirectory(int thisIFDOffset, SortedSet fields, int nextIFDOffset) throws IOException {
      int numEntries = fields.size();
      long offsetBeyondIFD = (long)(thisIFDOffset + 12 * numEntries + 4 + 2);
      List tooBig = new ArrayList();
      this.writeUnsignedShort(numEntries);
      Iterator iter = fields.iterator();

      while(iter.hasNext()) {
         TIFFField field = (TIFFField)iter.next();
         int tag = field.getTag();
         this.writeUnsignedShort(tag);
         int type = field.getType();
         this.writeUnsignedShort(type);
         int count = field.getCount();
         int valueSize = getValueSize(field);
         this.writeLong(type == 2 ? (long)valueSize : (long)count);
         if (valueSize > 4) {
            this.writeLong(offsetBeyondIFD);
            offsetBeyondIFD += (long)valueSize;
            tooBig.add(field);
         } else {
            this.writeValuesAsFourBytes(field);
         }
      }

      this.writeLong((long)nextIFDOffset);

      for(int i = 0; i < tooBig.size(); ++i) {
         this.writeValues((TIFFField)tooBig.get(i));
      }

   }

   private static int getValueSize(TIFFField field) {
      int type = field.getType();
      int count = field.getCount();
      int valueSize = 0;
      if (type == 2) {
         for(int i = 0; i < count; ++i) {
            byte[] stringBytes = field.getAsString(i).getBytes();
            valueSize += stringBytes.length;
            if (stringBytes[stringBytes.length - 1] != 0) {
               ++valueSize;
            }
         }
      } else {
         valueSize = count * sizeOfType[type];
      }

      return valueSize;
   }

   private void writeValuesAsFourBytes(TIFFField field) throws IOException {
      int dataType = field.getType();
      int count = field.getCount();
      switch (dataType) {
         case 1:
            byte[] bytes = field.getAsBytes();
            if (count > 4) {
               count = 4;
            }

            int i;
            for(i = 0; i < count; ++i) {
               this.output.write(bytes[i]);
            }

            for(i = 0; i < 4 - count; ++i) {
               this.output.write(0);
            }
         case 2:
         default:
            break;
         case 3:
            char[] chars = field.getAsChars();
            if (count > 2) {
               count = 2;
            }

            int i;
            for(i = 0; i < count; ++i) {
               this.writeUnsignedShort(chars[i]);
            }

            for(i = 0; i < 2 - count; ++i) {
               this.writeUnsignedShort(0);
            }

            return;
         case 4:
            long[] longs = field.getAsLongs();

            for(int i = 0; i < count; ++i) {
               this.writeLong(longs[i]);
            }
      }

   }

   private void writeValues(TIFFField field) throws IOException {
      int dataType = field.getType();
      int count = field.getCount();
      int i;
      int i;
      switch (dataType) {
         case 1:
         case 6:
         case 7:
            byte[] bytes = field.getAsBytes();

            for(int i = 0; i < count; ++i) {
               this.output.write(bytes[i]);
            }

            return;
         case 2:
            for(i = 0; i < count; ++i) {
               byte[] stringBytes = field.getAsString(i).getBytes();
               this.output.write(stringBytes);
               if (stringBytes[stringBytes.length - 1] != 0) {
                  this.output.write(0);
               }
            }

            return;
         case 3:
            char[] chars = field.getAsChars();

            for(int i = 0; i < count; ++i) {
               this.writeUnsignedShort(chars[i]);
            }

            return;
         case 4:
         case 9:
            long[] longs = field.getAsLongs();

            for(int i = 0; i < count; ++i) {
               this.writeLong(longs[i]);
            }

            return;
         case 5:
         case 10:
            long[][] rationals = field.getAsRationals();

            for(i = 0; i < count; ++i) {
               this.writeLong(rationals[i][0]);
               this.writeLong(rationals[i][1]);
            }

            return;
         case 8:
            short[] shorts = field.getAsShorts();

            for(int i = 0; i < count; ++i) {
               this.writeUnsignedShort(shorts[i]);
            }

            return;
         case 11:
            float[] floats = field.getAsFloats();

            for(int i = 0; i < count; ++i) {
               i = Float.floatToIntBits(floats[i]);
               this.writeLong((long)i);
            }

            return;
         case 12:
            double[] doubles = field.getAsDoubles();

            for(i = 0; i < count; ++i) {
               long longBits = Double.doubleToLongBits(doubles[i]);
               this.writeLong(longBits >>> 32);
               this.writeLong(longBits & 4294967295L);
            }

            return;
         default:
            throw new Error("TIFFImageEncoder10");
      }
   }

   private void writeUnsignedShort(int s) throws IOException {
      this.output.write((s & '\uff00') >>> 8);
      this.output.write(s & 255);
   }

   private void writeLong(long l) throws IOException {
      this.output.write((int)((l & -16777216L) >>> 24));
      this.output.write((int)((l & 16711680L) >>> 16));
      this.output.write((int)((l & 65280L) >>> 8));
      this.output.write((int)(l & 255L));
   }

   private long getOffset(OutputStream out) throws IOException {
      if (out instanceof ByteArrayOutputStream) {
         return (long)((ByteArrayOutputStream)out).size();
      } else if (out instanceof SeekableOutputStream) {
         return ((SeekableOutputStream)out).getFilePointer();
      } else {
         throw new IllegalStateException();
      }
   }

   private static int compressPackBits(byte[] data, int numRows, int bytesPerRow, byte[] compData) {
      int inOffset = 0;
      int outOffset = 0;

      for(int i = 0; i < numRows; ++i) {
         outOffset = packBits(data, inOffset, bytesPerRow, compData, outOffset);
         inOffset += bytesPerRow;
      }

      return outOffset;
   }

   private static int packBits(byte[] input, int inOffset, int inCount, byte[] output, int outOffset) {
      int inMax = inOffset + inCount - 1;
      int inMaxMinus1 = inMax - 1;

      while(true) {
         while(true) {
            int run;
            do {
               if (inOffset > inMax) {
                  return outOffset;
               }

               run = 1;

               byte replicate;
               for(replicate = input[inOffset]; run < 127 && inOffset < inMax && input[inOffset] == input[inOffset + 1]; ++inOffset) {
                  ++run;
               }

               if (run > 1) {
                  ++inOffset;
                  output[outOffset++] = (byte)(-(run - 1));
                  output[outOffset++] = replicate;
               }

               for(run = 0; run < 128 && (inOffset < inMax && input[inOffset] != input[inOffset + 1] || inOffset < inMaxMinus1 && input[inOffset] != input[inOffset + 2]); output[outOffset] = input[inOffset++]) {
                  ++run;
                  ++outOffset;
               }

               if (run > 0) {
                  output[outOffset] = (byte)(run - 1);
                  ++outOffset;
               }
            } while(inOffset != inMax);

            if (run > 0 && run < 128) {
               ++output[outOffset];
               output[outOffset++] = input[inOffset++];
            } else {
               output[outOffset++] = 0;
               output[outOffset++] = input[inOffset++];
            }
         }
      }
   }

   private static int deflate(Deflater deflater, byte[] inflated, byte[] deflated) {
      deflater.setInput(inflated);
      deflater.finish();
      int numCompressedBytes = deflater.deflate(deflated);
      deflater.reset();
      return numCompressedBytes;
   }

   private static void modifyEncodeParam(JPEGEncodeParam src, JPEGEncodeParam dst, int nbands) {
      dst.setDensityUnit(src.getDensityUnit());
      dst.setXDensity(src.getXDensity());
      dst.setYDensity(src.getYDensity());
      dst.setRestartInterval(src.getRestartInterval());

      for(int i = 0; i < 4; ++i) {
         JPEGQTable tbl = src.getQTable(i);
         if (tbl != null) {
            dst.setQTable(i, tbl);
         }
      }

   }

   private class Context {
      private RenderedImage nextImage;
      private int ifdOffset;

      private Context() {
         this.ifdOffset = 8;
      }

      // $FF: synthetic method
      Context(Object x1) {
         this();
      }
   }
}
