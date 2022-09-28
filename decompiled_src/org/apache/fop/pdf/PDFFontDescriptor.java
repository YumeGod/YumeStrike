package org.apache.fop.pdf;

import org.apache.fop.fonts.FontType;

public class PDFFontDescriptor extends PDFDictionary {
   public PDFFontDescriptor(String basefont, int ascent, int descent, int capHeight, int flags, PDFRectangle fontBBox, int italicAngle, int stemV) {
      this.put("Type", new PDFName("FontDescriptor"));
      this.put("FontName", new PDFName(basefont));
      this.put("FontBBox", fontBBox);
      this.put("Flags", flags);
      this.put("CapHeight", capHeight);
      this.put("Ascent", ascent);
      this.put("Descent", descent);
      this.put("ItalicAngle", italicAngle);
      this.put("StemV", stemV);
   }

   public void setMetrics(int avgWidth, int maxWidth, int missingWidth, int leading, int stemH, int xHeight) {
      if (avgWidth != 0) {
         this.put("AvgWidth", avgWidth);
      }

      if (maxWidth != 0) {
         this.put("MaxWidth", maxWidth);
      }

      if (missingWidth != 0) {
         this.put("MissingWidth", missingWidth);
      }

      if (leading != 0) {
         this.put("Leading", leading);
      }

      if (stemH != 0) {
         this.put("StemH", stemH);
      }

      if (xHeight != 0) {
         this.put("XHeight", xHeight);
      }

   }

   public void setFontFile(FontType subtype, AbstractPDFStream fontfile) {
      if (subtype == FontType.TYPE1) {
         this.put("FontFile", fontfile);
      } else {
         this.put("FontFile2", fontfile);
      }

   }

   public AbstractPDFStream getFontFile() {
      AbstractPDFStream stream = (AbstractPDFStream)this.get("FontFile");
      if (stream == null) {
         stream = (AbstractPDFStream)this.get("FontFile2");
      }

      if (stream == null) {
         stream = (AbstractPDFStream)this.get("FontFile3");
      }

      return stream;
   }

   public void setCIDSet(AbstractPDFStream cidSet) {
      this.put("CIDSet", cidSet);
   }

   public AbstractPDFStream getCIDSet() {
      return (AbstractPDFStream)this.get("CIDSet");
   }
}
