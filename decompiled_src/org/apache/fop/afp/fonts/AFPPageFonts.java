package org.apache.fop.afp.fonts;

import java.util.HashMap;

public class AFPPageFonts extends HashMap {
   private static final long serialVersionUID = -4991896259427109041L;

   public AFPPageFonts() {
   }

   public AFPPageFonts(AFPPageFonts fonts) {
      super(fonts);
   }

   public AFPFontAttributes registerFont(String fontName, AFPFont font, int fontSize) {
      String pageFontKey = fontName + "_" + fontSize;
      AFPFontAttributes afpFontAttributes = (AFPFontAttributes)super.get(pageFontKey);
      if (afpFontAttributes == null) {
         afpFontAttributes = new AFPFontAttributes(fontName, font, fontSize);
         super.put(pageFontKey, afpFontAttributes);
         int fontRef = super.size();
         afpFontAttributes.setFontReference(fontRef);
      }

      return afpFontAttributes;
   }
}
