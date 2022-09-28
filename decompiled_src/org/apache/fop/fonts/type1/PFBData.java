package org.apache.fop.fonts.type1;

import java.io.IOException;
import java.io.OutputStream;

public class PFBData {
   public static final int PFB_RAW = 0;
   public static final int PFB_PC = 1;
   public static final int PFB_MAC = 2;
   private int pfbFormat;
   private byte[] headerSegment;
   private byte[] encryptedSegment;
   private byte[] trailerSegment;

   public void setPFBFormat(int format) {
      switch (format) {
         case 0:
         case 1:
            this.pfbFormat = format;
            return;
         case 2:
            throw new UnsupportedOperationException("Mac format is not yet implemented");
         default:
            throw new IllegalArgumentException("Invalid value for PFB format: " + format);
      }
   }

   public int getPFBFormat() {
      return this.pfbFormat;
   }

   public void setHeaderSegment(byte[] headerSeg) {
      this.headerSegment = headerSeg;
   }

   public void setEncryptedSegment(byte[] encryptedSeg) {
      this.encryptedSegment = encryptedSeg;
   }

   public void setTrailerSegment(byte[] trailerSeg) {
      this.trailerSegment = trailerSeg;
   }

   public int getLength() {
      return this.getLength1() + this.getLength2() + this.getLength3();
   }

   public int getLength1() {
      return this.headerSegment.length;
   }

   public int getLength2() {
      return this.encryptedSegment.length;
   }

   public int getLength3() {
      return this.trailerSegment.length;
   }

   public void outputAllParts(OutputStream out) throws IOException {
      out.write(this.headerSegment);
      out.write(this.encryptedSegment);
      out.write(this.trailerSegment);
   }

   public String toString() {
      return "PFB: format=" + this.getPFBFormat() + " len1=" + this.getLength1() + " len2=" + this.getLength2() + " len3=" + this.getLength3();
   }
}
