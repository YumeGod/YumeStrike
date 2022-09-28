package org.apache.fop.svg;

import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.Typeface;

public abstract class PDFTextUtil extends org.apache.fop.pdf.PDFTextUtil {
   private FontInfo fontInfo;
   private Font[] fonts;
   private Font font;

   public PDFTextUtil(FontInfo fontInfo) {
      this.fontInfo = fontInfo;
   }

   protected void initValues() {
      super.initValues();
      this.font = null;
   }

   public void setFonts(Font[] fonts) {
      this.fonts = fonts;
   }

   public void setFont(Font font) {
      this.setFonts(new Font[]{font});
   }

   public Font getCurrentFont() {
      return this.font;
   }

   public void setCurrentFont(Font f) {
      this.font = f;
   }

   protected boolean isMultiByteFont(String name) {
      Typeface f = (Typeface)this.fontInfo.getFonts().get(name);
      return f.isMultiByte();
   }

   public void writeTf(Font f) {
      String fontName = f.getFontName();
      float fontSize = (float)f.getFontSize() / 1000.0F;
      this.updateTf(fontName, (double)fontSize, this.isMultiByteFont(fontName));
   }

   public Font selectFontForChar(char ch) {
      int i = 0;

      for(int c = this.fonts.length; i < c; ++i) {
         if (this.fonts[i].hasChar(ch)) {
            return this.fonts[i];
         }
      }

      return this.fonts[0];
   }

   public void writeTJChar(char ch) {
      char mappedChar = this.font.mapChar(ch);
      this.writeTJMappedChar(mappedChar);
   }
}
