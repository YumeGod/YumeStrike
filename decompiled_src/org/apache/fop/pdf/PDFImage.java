package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public interface PDFImage {
   String getKey();

   void setup(PDFDocument var1);

   int getWidth();

   int getHeight();

   PDFDeviceColorSpace getColorSpace();

   int getBitsPerComponent();

   boolean isPS();

   boolean isTransparent();

   PDFColor getTransparentColor();

   String getMask();

   PDFReference getSoftMaskReference();

   boolean isInverted();

   PDFFilter getPDFFilter();

   void outputContents(OutputStream var1) throws IOException;

   void populateXObjectDictionary(PDFDictionary var1);

   PDFICCStream getICCStream();

   String getFilterHint();
}
