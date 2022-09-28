package org.apache.fop.pdf;

public class CCFFilter extends NullFilter {
   private PDFObject decodeParms;

   public String getName() {
      return "/CCITTFaxDecode";
   }

   public PDFObject getDecodeParms() {
      return this.decodeParms;
   }

   public void setDecodeParms(PDFObject decodeParms) {
      this.decodeParms = decodeParms;
   }
}
