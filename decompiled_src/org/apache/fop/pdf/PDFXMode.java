package org.apache.fop.pdf;

public final class PDFXMode {
   public static final PDFXMode DISABLED = new PDFXMode("PDF/X disabled");
   public static final PDFXMode PDFX_3_2003 = new PDFXMode("PDF/X-3:2003");
   private String name;

   private PDFXMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public static PDFXMode valueOf(String s) {
      return PDFX_3_2003.getName().equalsIgnoreCase(s) ? PDFX_3_2003 : DISABLED;
   }

   public String toString() {
      return this.name;
   }
}
