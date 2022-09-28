package org.apache.batik.ext.awt.image.codec.tiff;

import com.sun.image.codec.jpeg.JPEGEncodeParam;
import java.util.Iterator;
import org.apache.batik.ext.awt.image.codec.util.ImageEncodeParam;

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

   public void setCompression(int var1) {
      switch (var1) {
         case 1:
         case 7:
         case 32773:
         case 32946:
            this.compression = var1;
            return;
         default:
            throw new Error("TIFFEncodeParam0");
      }
   }

   public boolean getWriteTiled() {
      return this.writeTiled;
   }

   public void setWriteTiled(boolean var1) {
      this.writeTiled = var1;
   }

   public void setTileSize(int var1, int var2) {
      this.tileWidth = var1;
      this.tileHeight = var2;
   }

   public int getTileWidth() {
      return this.tileWidth;
   }

   public int getTileHeight() {
      return this.tileHeight;
   }

   public synchronized void setExtraImages(Iterator var1) {
      this.extraImages = var1;
   }

   public synchronized Iterator getExtraImages() {
      return this.extraImages;
   }

   public void setDeflateLevel(int var1) {
      if (var1 < 1 && var1 > 9 && var1 != -1) {
         throw new Error("TIFFEncodeParam1");
      } else {
         this.deflateLevel = var1;
      }
   }

   public int getDeflateLevel() {
      return this.deflateLevel;
   }

   public void setJPEGCompressRGBToYCbCr(boolean var1) {
      this.convertJPEGRGBToYCbCr = var1;
   }

   public boolean getJPEGCompressRGBToYCbCr() {
      return this.convertJPEGRGBToYCbCr;
   }

   public void setJPEGEncodeParam(JPEGEncodeParam var1) {
      if (var1 != null) {
         var1 = (JPEGEncodeParam)var1.clone();
         var1.setTableInfoValid(false);
         var1.setImageInfoValid(true);
      }

      this.jpegEncodeParam = var1;
   }

   public JPEGEncodeParam getJPEGEncodeParam() {
      return this.jpegEncodeParam;
   }

   public void setExtraFields(TIFFField[] var1) {
      this.extraFields = var1;
   }

   public TIFFField[] getExtraFields() {
      return this.extraFields;
   }
}
