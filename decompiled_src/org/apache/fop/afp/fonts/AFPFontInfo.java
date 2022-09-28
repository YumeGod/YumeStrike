package org.apache.fop.afp.fonts;

import java.util.List;

public class AFPFontInfo {
   private AFPFont font;
   private List tripletList;

   public AFPFontInfo(AFPFont afpFont, List tripletList) {
      this.font = afpFont;
      this.tripletList = tripletList;
   }

   public AFPFont getAFPFont() {
      return this.font;
   }

   public List getFontTriplets() {
      return this.tripletList;
   }
}
