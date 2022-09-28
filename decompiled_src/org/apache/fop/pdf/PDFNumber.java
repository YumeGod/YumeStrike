package org.apache.fop.pdf;

import org.apache.fop.util.DecimalFormatCache;

public class PDFNumber extends PDFObject {
   private Number number;

   public Number getNumber() {
      return this.number;
   }

   public void setNumber(Number number) {
      this.number = number;
   }

   public static String doubleOut(Double doubleDown) {
      return doubleOut(doubleDown);
   }

   public static String doubleOut(double doubleDown) {
      return doubleOut(doubleDown, 6);
   }

   public static String doubleOut(double doubleDown, int dec) {
      return DecimalFormatCache.getDecimalFormat(dec).format(doubleDown);
   }

   protected String toPDFString() {
      if (this.getNumber() == null) {
         throw new IllegalArgumentException("The number of this PDFNumber must not be empty");
      } else {
         StringBuffer sb = new StringBuffer(64);
         if (this.hasObjectNumber()) {
            sb.append(this.getObjectID());
         }

         sb.append(doubleOut(this.getNumber().doubleValue(), 10));
         if (this.hasObjectNumber()) {
            sb.append("\nendobj\n");
         }

         return sb.toString();
      }
   }
}
