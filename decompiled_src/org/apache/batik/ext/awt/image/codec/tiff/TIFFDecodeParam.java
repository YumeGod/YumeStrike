package org.apache.batik.ext.awt.image.codec.tiff;

import org.apache.batik.ext.awt.image.codec.util.ImageDecodeParam;

public class TIFFDecodeParam implements ImageDecodeParam {
   private boolean decodePaletteAsShorts = false;
   private Long ifdOffset = null;
   private boolean convertJPEGYCbCrToRGB = true;

   public void setDecodePaletteAsShorts(boolean var1) {
      this.decodePaletteAsShorts = var1;
   }

   public boolean getDecodePaletteAsShorts() {
      return this.decodePaletteAsShorts;
   }

   public byte decode16BitsTo8Bits(int var1) {
      return (byte)(var1 >> 8 & '\uffff');
   }

   public byte decodeSigned16BitsTo8Bits(short var1) {
      return (byte)(var1 + Short.MIN_VALUE >> 8);
   }

   public void setIFDOffset(long var1) {
      this.ifdOffset = new Long(var1);
   }

   public Long getIFDOffset() {
      return this.ifdOffset;
   }

   public void setJPEGDecompressYCbCrToRGB(boolean var1) {
      this.convertJPEGYCbCrToRGB = var1;
   }

   public boolean getJPEGDecompressYCbCrToRGB() {
      return this.convertJPEGYCbCrToRGB;
   }
}
