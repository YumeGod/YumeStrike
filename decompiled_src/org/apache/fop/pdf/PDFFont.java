package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.fonts.FontType;

public class PDFFont extends PDFDictionary {
   private String fontname;

   public PDFFont(String fontname, FontType subtype, String basefont, Object encoding) {
      this.fontname = fontname;
      this.put("Type", new PDFName("Font"));
      this.put("Subtype", this.getPDFNameForFontType(subtype));
      this.put("BaseFont", new PDFName(basefont));
      if (encoding instanceof PDFEncoding) {
         this.setEncoding((PDFEncoding)encoding);
      } else if (encoding instanceof String) {
         this.setEncoding((String)encoding);
      }

   }

   public void setEncoding(String encoding) {
      if (encoding != null) {
         this.put("Encoding", new PDFName(encoding));
      }

   }

   public void setEncoding(PDFEncoding encoding) {
      if (encoding != null) {
         this.put("Encoding", encoding);
      }

   }

   public void setToUnicode(PDFCMap cmap) {
      this.put("ToUnicode", cmap);
   }

   public static PDFFont createFont(String fontname, FontType subtype, String basefont, Object encoding) {
      if (subtype == FontType.TYPE0) {
         return new PDFFontType0(fontname, basefont, encoding);
      } else if (subtype != FontType.TYPE1 && subtype != FontType.MMTYPE1) {
         if (subtype == FontType.TYPE3) {
            return null;
         } else {
            return subtype == FontType.TRUETYPE ? new PDFFontTrueType(fontname, basefont, encoding) : null;
         }
      } else {
         return new PDFFontType1(fontname, basefont, encoding);
      }
   }

   public String getName() {
      return this.fontname;
   }

   public PDFName getBaseFont() {
      return (PDFName)this.get("BaseFont");
   }

   protected PDFName getPDFNameForFontType(FontType fontType) {
      if (fontType == FontType.TYPE0) {
         return new PDFName(fontType.getName());
      } else if (fontType == FontType.TYPE1) {
         return new PDFName(fontType.getName());
      } else if (fontType == FontType.MMTYPE1) {
         return new PDFName(fontType.getName());
      } else if (fontType == FontType.TYPE3) {
         return new PDFName(fontType.getName());
      } else if (fontType == FontType.TRUETYPE) {
         return new PDFName(fontType.getName());
      } else {
         throw new IllegalArgumentException("Unsupported font type: " + fontType.getName());
      }
   }

   protected void validate() {
      if (this.getDocumentSafely().getProfile().isFontEmbeddingRequired() && this.getClass() == PDFFont.class) {
         throw new PDFConformanceException("For " + this.getDocumentSafely().getProfile() + ", all fonts, even the base 14" + " fonts, have to be embedded! Offending font: " + this.getBaseFont());
      }
   }

   protected int output(OutputStream stream) throws IOException {
      this.validate();
      return super.output(stream);
   }
}
