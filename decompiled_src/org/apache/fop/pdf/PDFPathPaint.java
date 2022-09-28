package org.apache.fop.pdf;

public abstract class PDFPathPaint extends PDFObject {
   protected PDFDeviceColorSpace colorSpace;

   public String getColorSpaceOut(boolean fillNotStroke) {
      return "";
   }

   public void setColorSpace(int theColorSpace) {
      this.colorSpace.setColorSpace(theColorSpace);
   }

   public int getColorSpace() {
      return this.colorSpace.getColorSpace();
   }
}
