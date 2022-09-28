package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class PDFImageXObject extends PDFXObject {
   private PDFImage pdfimage;

   public PDFImageXObject(int xnumber, PDFImage img) {
      this.put("Name", new PDFName("Im" + xnumber));
      this.pdfimage = img;
   }

   protected int output(OutputStream stream) throws IOException {
      int length = super.output(stream);
      this.pdfimage = null;
      return length;
   }

   protected void populateStreamDict(Object lengthEntry) {
      super.populateStreamDict(lengthEntry);
      if (this.pdfimage.isPS()) {
         this.populateDictionaryFromPS();
      } else {
         this.populateDictionaryFromImage();
      }

   }

   private void populateDictionaryFromPS() {
      this.getDocumentSafely().getProfile().verifyPSXObjectsAllowed();
      this.put("Subtype", new PDFName("PS"));
   }

   private void populateDictionaryFromImage() {
      this.put("Subtype", new PDFName("Image"));
      this.put("Width", new Integer(this.pdfimage.getWidth()));
      this.put("Height", new Integer(this.pdfimage.getHeight()));
      this.put("BitsPerComponent", new Integer(this.pdfimage.getBitsPerComponent()));
      PDFICCStream pdfICCStream = this.pdfimage.getICCStream();
      if (pdfICCStream != null) {
         this.put("ColorSpace", new PDFArray(this, new Object[]{new PDFName("ICCBased"), pdfICCStream}));
      } else {
         PDFDeviceColorSpace cs = this.pdfimage.getColorSpace();
         this.put("ColorSpace", new PDFName(cs.getName()));
      }

      if (this.pdfimage.isInverted()) {
         Float zero = new Float(0.0F);
         Float one = new Float(1.0F);
         PDFArray decode = new PDFArray(this);
         int i = 0;

         for(int c = this.pdfimage.getColorSpace().getNumComponents(); i < c; ++i) {
            decode.add(one);
            decode.add(zero);
         }

         this.put("Decode", decode);
      }

      if (this.pdfimage.isTransparent()) {
         PDFColor transp = this.pdfimage.getTransparentColor();
         PDFArray mask = new PDFArray(this);
         if (this.pdfimage.getColorSpace().isGrayColorSpace()) {
            mask.add(new Integer(transp.red255()));
            mask.add(new Integer(transp.red255()));
         } else {
            mask.add(new Integer(transp.red255()));
            mask.add(new Integer(transp.red255()));
            mask.add(new Integer(transp.green255()));
            mask.add(new Integer(transp.green255()));
            mask.add(new Integer(transp.blue255()));
            mask.add(new Integer(transp.blue255()));
         }

         this.put("Mask", mask);
      }

      PDFReference ref = this.pdfimage.getSoftMaskReference();
      if (ref != null) {
         this.put("SMask", ref);
      }

      this.pdfimage.populateXObjectDictionary(this);
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      this.pdfimage.outputContents(out);
   }

   protected int getSizeHint() throws IOException {
      return 0;
   }

   protected void prepareImplicitFilters() {
      PDFFilter pdfFilter = this.pdfimage.getPDFFilter();
      if (pdfFilter != null) {
         this.getFilterList().ensureFilterInPlace(pdfFilter);
      }

   }

   protected String getDefaultFilterName() {
      return this.pdfimage.getFilterHint();
   }
}
