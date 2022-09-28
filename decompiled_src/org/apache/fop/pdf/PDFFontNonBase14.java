package org.apache.fop.pdf;

import org.apache.fop.fonts.FontType;

public abstract class PDFFontNonBase14 extends PDFFont {
   public PDFFontNonBase14(String fontname, FontType subtype, String basefont, Object encoding) {
      super(fontname, subtype, basefont, encoding);
   }

   public void setWidthMetrics(int firstChar, int lastChar, PDFArray widths) {
      this.put("FirstChar", new Integer(firstChar));
      this.put("LastChar", new Integer(lastChar));
      this.put("Widths", widths);
   }

   public void setDescriptor(PDFFontDescriptor descriptor) {
      this.put("FontDescriptor", descriptor);
   }

   public PDFFontDescriptor getDescriptor() {
      return (PDFFontDescriptor)this.get("FontDescriptor");
   }

   protected void validate() {
      if (this.getDocumentSafely().getProfile().isFontEmbeddingRequired() && this.getDescriptor().getFontFile() == null) {
         throw new PDFConformanceException("For " + this.getDocumentSafely().getProfile() + ", all fonts have to be embedded! Offending font: " + this.getBaseFont());
      }
   }
}
