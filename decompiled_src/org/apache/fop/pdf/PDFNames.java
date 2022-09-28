package org.apache.fop.pdf;

public class PDFNames extends PDFDictionary {
   public PDFDests getDests() {
      return (PDFDests)this.get("Dests");
   }

   public void setDests(PDFDests dests) {
      this.put("Dests", dests);
   }
}
