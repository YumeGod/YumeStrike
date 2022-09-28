package org.apache.xmlgraphics.image.codec.tiff;

import org.apache.xmlgraphics.image.codec.util.ImageDecodeParam;

public class TIFFDecodeParam implements ImageDecodeParam {
   private boolean decodePaletteAsShorts = false;
   private Long ifdOffset = null;
   private boolean convertJPEGYCbCrToRGB = true;

   public void setDecodePaletteAsShorts(boolean decodePaletteAsShorts) {
      this.decodePaletteAsShorts = decodePaletteAsShorts;
   }

   public boolean getDecodePaletteAsShorts() {
      return this.decodePaletteAsShorts;
   }

   public byte decode16BitsTo8Bits(int s) {
      return (byte)(s >> 8 & '\uffff');
   }

   public byte decodeSigned16BitsTo8Bits(short s) {
      return (byte)(s + Short.MIN_VALUE >> 8);
   }

   public void setIFDOffset(long offset) {
      this.ifdOffset = new Long(offset);
   }

   public Long getIFDOffset() {
      return this.ifdOffset;
   }

   public void setJPEGDecompressYCbCrToRGB(boolean convertJPEGYCbCrToRGB) {
      this.convertJPEGYCbCrToRGB = convertJPEGYCbCrToRGB;
   }

   public boolean getJPEGDecompressYCbCrToRGB() {
      return this.convertJPEGYCbCrToRGB;
   }
}
