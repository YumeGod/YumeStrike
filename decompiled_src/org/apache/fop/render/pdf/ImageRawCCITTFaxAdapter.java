package org.apache.fop.render.pdf;

import java.awt.color.ColorSpace;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.pdf.CCFFilter;
import org.apache.fop.pdf.PDFDeviceColorSpace;
import org.apache.fop.pdf.PDFDictionary;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFFilter;
import org.apache.xmlgraphics.image.loader.impl.ImageRawCCITTFax;

public class ImageRawCCITTFaxAdapter extends AbstractImageAdapter {
   private PDFFilter pdfFilter = null;

   public ImageRawCCITTFaxAdapter(ImageRawCCITTFax image, String key) {
      super(image, key);
   }

   public ImageRawCCITTFax getImage() {
      return (ImageRawCCITTFax)this.image;
   }

   public void setup(PDFDocument doc) {
      this.pdfFilter = new CCFFilter();
      this.pdfFilter.setApplied(true);
      PDFDictionary dict = new PDFDictionary();
      dict.put("Columns", this.image.getSize().getWidthPx());
      int compression = this.getImage().getCompression();
      switch (compression) {
         case 2:
            dict.put("K", 0);
            break;
         case 3:
            dict.put("K", 1);
            break;
         case 4:
            dict.put("K", -1);
            break;
         default:
            throw new IllegalStateException("Invalid compression scheme: " + compression);
      }

      ((CCFFilter)this.pdfFilter).setDecodeParms(dict);
      super.setup(doc);
   }

   public PDFDeviceColorSpace getColorSpace() {
      return toPDFColorSpace(ColorSpace.getInstance(1003));
   }

   public int getBitsPerComponent() {
      return 1;
   }

   public PDFFilter getPDFFilter() {
      return this.pdfFilter;
   }

   public void outputContents(OutputStream out) throws IOException {
      this.getImage().writeTo(out);
   }

   public String getFilterHint() {
      return "tiff";
   }
}
