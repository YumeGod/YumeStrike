package org.apache.fop.pdf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PDFEncoding extends PDFDictionary {
   public static final String STANDARD_ENCODING = "StandardEncoding";
   public static final String MAC_ROMAN_ENCODING = "MacRomanEncoding";
   public static final String MAC_EXPERT_ENCODING = "MacExpertEncoding";
   public static final String WIN_ANSI_ENCODING = "WinAnsiEncoding";
   public static final String PDF_DOC_ENCODING = "PDFDocEncoding";
   private static final Set PREDEFINED_ENCODINGS;

   public PDFEncoding(String basename) {
      this.put("Type", new PDFName("Encoding"));
      if (basename != null) {
         this.put("BaseEncoding", new PDFName(basename));
      }

   }

   public static boolean isPredefinedEncoding(String name) {
      return PREDEFINED_ENCODINGS.contains(name);
   }

   public DifferencesBuilder createDifferencesBuilder() {
      return new DifferencesBuilder();
   }

   public void setDifferences(PDFArray differences) {
      this.put("Differences", differences);
   }

   static {
      Set encodings = new HashSet();
      encodings.add("StandardEncoding");
      encodings.add("MacRomanEncoding");
      encodings.add("MacExpertEncoding");
      encodings.add("WinAnsiEncoding");
      encodings.add("PDFDocEncoding");
      PREDEFINED_ENCODINGS = Collections.unmodifiableSet(encodings);
   }

   public class DifferencesBuilder {
      private PDFArray differences = new PDFArray();
      private int currentCode = -1;

      public DifferencesBuilder addDifference(int code) {
         this.currentCode = code;
         this.differences.add(new Integer(code));
         return this;
      }

      public DifferencesBuilder addName(String name) {
         if (this.currentCode < 0) {
            throw new IllegalStateException("addDifference(int) must be called first");
         } else {
            this.differences.add(new PDFName(name));
            return this;
         }
      }

      public boolean hasDifferences() {
         return this.differences.length() > 0;
      }

      public PDFArray toPDFArray() {
         return this.differences;
      }
   }
}
