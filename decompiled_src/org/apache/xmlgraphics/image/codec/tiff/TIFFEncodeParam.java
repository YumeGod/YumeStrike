package org.apache.xmlgraphics.image.codec.tiff;

import com.sun.image.codec.jpeg.JPEGEncodeParam;
import java.util.Iterator;
import org.apache.xmlgraphics.image.codec.util.ImageEncodeParam;

public class TIFFEncodeParam implements ImageEncodeParam {
   public static final int COMPRESSION_NONE = 1;
   public static final int COMPRESSION_GROUP3_1D = 2;
   public static final int COMPRESSION_GROUP3_2D = 3;
   public static final int COMPRESSION_GROUP4 = 4;
   public static final int COMPRESSION_LZW = 5;
   public static final int COMPRESSION_JPEG_BROKEN = 6;
   public static final int COMPRESSION_JPEG_TTN2 = 7;
   public static final int COMPRESSION_PACKBITS = 32773;
   public static final int COMPRESSION_DEFLATE = 32946;
   private int compression = 1;
   private boolean writeTiled = false;
   private int tileWidth;
   private int tileHeight;
   private Iterator extraImages;
   private TIFFField[] extraFields;
   private boolean convertJPEGRGBToYCbCr = true;
   private JPEGEncodeParam jpegEncodeParam = null;
   private int deflateLevel = -1;

   public int getCompression() {
      return this.compression;
   }

   public void setCompression(int compression) {
      switch (compression) {
         case 1:
         case 7:
         case 32773:
         case 32946:
            this.compression = compression;
            return;
         default:
            throw new Error("TIFFEncodeParam0");
      }
   }

   public boolean getWriteTiled() {
      return this.writeTiled;
   }

   public void setWriteTiled(boolean writeTiled) {
      this.writeTiled = writeTiled;
   }

   public void setTileSize(int tileWidth, int tileHeight) {
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
   }

   public int getTileWidth() {
      return this.tileWidth;
   }

   public int getTileHeight() {
      return this.tileHeight;
   }

   public synchronized void setExtraImages(Iterator extraImages) {
      this.extraImages = extraImages;
   }

   public synchronized Iterator getExtraImages() {
      return this.extraImages;
   }

   public void setDeflateLevel(int deflateLevel) {
      if (deflateLevel < 1 && deflateLevel > 9 && deflateLevel != -1) {
         throw new Error("TIFFEncodeParam1");
      } else {
         this.deflateLevel = deflateLevel;
      }
   }

   public int getDeflateLevel() {
      return this.deflateLevel;
   }

   public void setJPEGCompressRGBToYCbCr(boolean convertJPEGRGBToYCbCr) {
      this.convertJPEGRGBToYCbCr = convertJPEGRGBToYCbCr;
   }

   public boolean getJPEGCompressRGBToYCbCr() {
      return this.convertJPEGRGBToYCbCr;
   }

   public void setJPEGEncodeParam(JPEGEncodeParam jpegEncodeParam) {
      if (jpegEncodeParam != null) {
         jpegEncodeParam = (JPEGEncodeParam)jpegEncodeParam.clone();
         jpegEncodeParam.setTableInfoValid(false);
         jpegEncodeParam.setImageInfoValid(true);
      }

      this.jpegEncodeParam = jpegEncodeParam;
   }

   public JPEGEncodeParam getJPEGEncodeParam() {
      return this.jpegEncodeParam;
   }

   public void setExtraFields(TIFFField[] extraFields) {
      this.extraFields = extraFields;
   }

   public TIFFField[] getExtraFields() {
      return this.extraFields;
   }
}
