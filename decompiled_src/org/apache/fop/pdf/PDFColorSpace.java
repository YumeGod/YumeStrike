package org.apache.fop.pdf;

public interface PDFColorSpace {
   int getNumComponents();

   String getName();

   boolean isDeviceColorSpace();

   boolean isRGBColorSpace();

   boolean isCMYKColorSpace();

   boolean isGrayColorSpace();
}
