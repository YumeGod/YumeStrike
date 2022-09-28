package org.apache.fop.pdf;

public class DCTFilter extends NullFilter {
   public String getName() {
      return "/DCTDecode";
   }

   public PDFObject getDecodeParms() {
      return null;
   }
}
