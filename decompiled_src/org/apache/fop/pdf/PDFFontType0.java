package org.apache.fop.pdf;

import org.apache.fop.fonts.FontType;

public class PDFFontType0 extends PDFFont {
   public PDFFontType0(String fontname, String basefont, Object encoding) {
      super(fontname, FontType.TYPE0, basefont, encoding);
   }

   public PDFFontType0(String fontname, String basefont, Object encoding, PDFCIDFont descendantFonts) {
      super(fontname, FontType.TYPE0, basefont, encoding);
      this.setDescendantFonts(descendantFonts);
   }

   public void setDescendantFonts(PDFCIDFont descendantFonts) {
      this.put("DescendantFonts", new PDFArray(this, new PDFObject[]{descendantFonts}));
   }

   public void setCMAP(PDFCMap cmap) {
      this.put("ToUnicode", cmap);
   }
}
