package org.apache.fop.pdf;

public class PDFCIDFontDescriptor extends PDFFontDescriptor {
   public PDFCIDFontDescriptor(String basefont, int[] fontBBox, int capHeight, int flags, int italicAngle, int stemV, String lang) {
      super(basefont, fontBBox[3], fontBBox[1], capHeight, flags, new PDFRectangle(fontBBox), italicAngle, stemV);
      this.put("MissingWidth", new Integer(500));
      if (lang != null) {
         this.put("Lang", lang);
      }

   }

   public void setCIDSet(PDFStream cidSet) {
      if (cidSet != null) {
         this.put("CIDSet", cidSet);
      }

   }
}
