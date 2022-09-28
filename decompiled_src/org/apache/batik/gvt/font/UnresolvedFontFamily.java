package org.apache.batik.gvt.font;

import java.text.AttributedCharacterIterator;
import java.util.Map;

public class UnresolvedFontFamily implements GVTFontFamily {
   protected GVTFontFace fontFace;

   public UnresolvedFontFamily(GVTFontFace var1) {
      this.fontFace = var1;
   }

   public UnresolvedFontFamily(String var1) {
      this(new GVTFontFace(var1));
   }

   public GVTFontFace getFontFace() {
      return this.fontFace;
   }

   public String getFamilyName() {
      return this.fontFace.getFamilyName();
   }

   public GVTFont deriveFont(float var1, AttributedCharacterIterator var2) {
      return null;
   }

   public GVTFont deriveFont(float var1, Map var2) {
      return null;
   }
}
