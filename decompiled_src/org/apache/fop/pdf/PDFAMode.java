package org.apache.fop.pdf;

public final class PDFAMode {
   public static final PDFAMode DISABLED = new PDFAMode("PDF/A disabled");
   public static final PDFAMode PDFA_1A = new PDFAMode("PDF/A-1a");
   public static final PDFAMode PDFA_1B = new PDFAMode("PDF/A-1b");
   private String name;

   private PDFAMode(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public boolean isPDFA1LevelA() {
      return this == PDFA_1A;
   }

   public boolean isPDFA1LevelB() {
      return this != DISABLED;
   }

   public static PDFAMode valueOf(String s) {
      if (PDFA_1A.getName().equalsIgnoreCase(s)) {
         return PDFA_1A;
      } else {
         return PDFA_1B.getName().equalsIgnoreCase(s) ? PDFA_1B : DISABLED;
      }
   }

   public String toString() {
      return this.name;
   }
}
