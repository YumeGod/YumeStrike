package org.apache.fop.pdf;

public class PDFDeviceColorSpace implements PDFColorSpace {
   private int numComponents;
   public static final int DEVICE_UNKNOWN = -1;
   public static final int DEVICE_GRAY = 1;
   public static final int DEVICE_RGB = 2;
   public static final int DEVICE_CMYK = 3;
   protected int currentColorSpace = -1;

   public PDFDeviceColorSpace(int theColorSpace) {
      this.currentColorSpace = theColorSpace;
      this.numComponents = this.calculateNumComponents();
   }

   private int calculateNumComponents() {
      if (this.currentColorSpace == 1) {
         return 1;
      } else if (this.currentColorSpace == 2) {
         return 3;
      } else {
         return this.currentColorSpace == 3 ? 4 : 0;
      }
   }

   public void setColorSpace(int theColorSpace) {
      this.currentColorSpace = theColorSpace;
      this.numComponents = this.calculateNumComponents();
   }

   public int getColorSpace() {
      return this.currentColorSpace;
   }

   public int getNumComponents() {
      return this.numComponents;
   }

   public String getName() {
      switch (this.currentColorSpace) {
         case 1:
            return "DeviceGray";
         case 2:
            return "DeviceRGB";
         case 3:
            return "DeviceCMYK";
         default:
            throw new IllegalStateException("Unsupported color space in use.");
      }
   }

   public boolean isDeviceColorSpace() {
      return true;
   }

   public boolean isRGBColorSpace() {
      return this.getColorSpace() == 2;
   }

   public boolean isCMYKColorSpace() {
      return this.getColorSpace() == 3;
   }

   public boolean isGrayColorSpace() {
      return this.getColorSpace() == 1;
   }
}
