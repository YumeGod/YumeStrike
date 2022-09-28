package org.apache.fop.fonts;

import java.util.List;

public class CustomFontCollection implements FontCollection {
   private FontResolver fontResolver;
   private List embedFontInfoList;

   public CustomFontCollection(FontResolver fontResolver, List customFonts) {
      this.fontResolver = fontResolver;
      if (this.fontResolver == null) {
         this.fontResolver = FontManager.createMinimalFontResolver();
      }

      this.embedFontInfoList = customFonts;
   }

   public int setup(int num, FontInfo fontInfo) {
      if (this.embedFontInfoList == null) {
         return num;
      } else {
         String internalName = null;

         for(int i = 0; i < this.embedFontInfoList.size(); ++i) {
            EmbedFontInfo embedFontInfo = (EmbedFontInfo)this.embedFontInfoList.get(i);
            internalName = "F" + num;
            ++num;
            LazyFont font = new LazyFont(embedFontInfo, this.fontResolver);
            fontInfo.addMetrics(internalName, font);
            List triplets = embedFontInfo.getFontTriplets();

            for(int tripletIndex = 0; tripletIndex < triplets.size(); ++tripletIndex) {
               FontTriplet triplet = (FontTriplet)triplets.get(tripletIndex);
               fontInfo.addFontProperties(internalName, triplet);
            }
         }

         return num;
      }
   }
}
