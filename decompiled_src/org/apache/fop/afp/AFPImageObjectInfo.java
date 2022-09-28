package org.apache.fop.afp;

public class AFPImageObjectInfo extends AFPDataObjectInfo {
   private int bitsPerPixel;
   private boolean color;
   private int compression = -1;
   private boolean subtractive;

   public void setBitsPerPixel(int bitsPerPixel) {
      this.bitsPerPixel = bitsPerPixel;
   }

   public void setColor(boolean color) {
      this.color = color;
   }

   public int getBitsPerPixel() {
      return this.bitsPerPixel;
   }

   public boolean isColor() {
      return this.color;
   }

   public boolean hasCompression() {
      return this.compression > -1;
   }

   public int getCompression() {
      return this.compression;
   }

   public void setCompression(int compression) {
      this.compression = compression;
   }

   public void setSubtractive(boolean subtractive) {
      this.subtractive = subtractive;
   }

   public boolean isSubtractive() {
      return this.subtractive;
   }

   public String toString() {
      return "AFPImageObjectInfo{" + super.toString() + ", compression=" + this.compression + ", color=" + this.color + ", bitsPerPixel=" + this.bitsPerPixel + ", " + (this.isSubtractive() ? "subtractive" : "additive") + "}";
   }
}
