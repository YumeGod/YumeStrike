package org.apache.fop.pdf;

import org.apache.fop.fonts.FontType;

public class PDFFontType3 extends PDFFontNonBase14 {
   public PDFFontType3(String fontname, String basefont, Object encoding) {
      super(fontname, FontType.TYPE3, basefont, encoding);
   }

   public PDFFontType3(String fontname, String basefont, Object encoding, PDFRectangle fontBBox, PDFArray fontMatrix, PDFCharProcs charProcs) {
      super(fontname, FontType.TYPE3, basefont, encoding);
      this.setFontBBox(fontBBox);
      this.setFontMatrix(fontMatrix);
      this.setCharProcs(charProcs);
   }

   public void setFontBBox(PDFRectangle bbox) {
      this.put("FontBBox", bbox);
   }

   public void setFontMatrix(PDFArray matrix) {
      this.put("FontMatrix", matrix);
   }

   public void setCharProcs(PDFCharProcs chars) {
      this.put("CharProcs", chars);
   }
}
