package org.apache.fop.pdf;

public class PDFFileSpec extends PDFObject {
   protected String filename;

   public PDFFileSpec(String filename) {
      this.filename = filename;
   }

   public String toPDFString() {
      return this.getObjectID() + "<<\n/Type /FileSpec\n" + "/F (" + this.filename + ")\n" + ">>\nendobj\n";
   }

   protected boolean contentEquals(PDFObject obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof PDFFileSpec) {
         PDFFileSpec spec = (PDFFileSpec)obj;
         return spec.filename.equals(this.filename);
      } else {
         return false;
      }
   }
}
