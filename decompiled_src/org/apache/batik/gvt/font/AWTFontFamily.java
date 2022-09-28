package org.apache.batik.gvt.font;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.gvt.text.GVTAttributedCharacterIterator;

public class AWTFontFamily implements GVTFontFamily {
   public static final AttributedCharacterIterator.Attribute TEXT_COMPOUND_DELIMITER;
   protected GVTFontFace fontFace;
   protected Font font;

   public AWTFontFamily(GVTFontFace var1) {
      this.fontFace = var1;
   }

   public AWTFontFamily(String var1) {
      this(new GVTFontFace(var1));
   }

   public AWTFontFamily(GVTFontFace var1, Font var2) {
      this.fontFace = var1;
      this.font = var2;
   }

   public String getFamilyName() {
      return this.fontFace.getFamilyName();
   }

   public GVTFontFace getFontFace() {
      return this.fontFace;
   }

   public GVTFont deriveFont(float var1, AttributedCharacterIterator var2) {
      return (GVTFont)(this.font != null ? new AWTGVTFont(this.font, var1) : this.deriveFont(var1, var2.getAttributes()));
   }

   public GVTFont deriveFont(float var1, Map var2) {
      if (this.font != null) {
         return new AWTGVTFont(this.font, var1);
      } else {
         HashMap var3 = new HashMap(var2);
         var3.put(TextAttribute.SIZE, new Float(var1));
         var3.put(TextAttribute.FAMILY, this.fontFace.getFamilyName());
         var3.remove(TEXT_COMPOUND_DELIMITER);
         return new AWTGVTFont(var3);
      }
   }

   static {
      TEXT_COMPOUND_DELIMITER = GVTAttributedCharacterIterator.TextAttribute.TEXT_COMPOUND_DELIMITER;
   }
}
