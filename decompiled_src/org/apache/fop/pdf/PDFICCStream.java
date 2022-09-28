package org.apache.fop.pdf;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.OutputStream;

public class PDFICCStream extends PDFStream {
   private ICC_Profile cp = null;
   private PDFDeviceColorSpace pdfColorSpace;

   public void setColorSpace(ICC_Profile icc, PDFDeviceColorSpace alt) {
      this.cp = icc;
      this.pdfColorSpace = alt;
   }

   public ICC_Profile getICCProfile() {
      return this.cp;
   }

   protected int output(OutputStream stream) throws IOException {
      int length = super.output(stream);
      this.cp = null;
      return length;
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      this.cp.write(out);
   }

   protected void populateStreamDict(Object lengthEntry) {
      this.put("N", this.cp.getNumComponents());
      if (this.pdfColorSpace != null) {
         this.put("Alternate", new PDFName(this.pdfColorSpace.getName()));
      }

      super.populateStreamDict(lengthEntry);
   }
}
