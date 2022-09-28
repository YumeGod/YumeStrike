package org.apache.fop.afp.fonts;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Set;

public class DoubleByteFont extends AbstractOutlineFont {
   private static final Set IDEOGRAPHIC = new HashSet();

   public DoubleByteFont(String name, CharacterSet charSet) {
      super(name, charSet);
   }

   public int getWidth(int character, int size) {
      int charWidth;
      try {
         charWidth = this.charSet.getWidth(toUnicodeCodepoint(character));
      } catch (IllegalArgumentException var5) {
         charWidth = -1;
      }

      if (charWidth == -1) {
         charWidth = this.inferCharWidth(character);
      }

      return charWidth * size;
   }

   private int inferCharWidth(int character) {
      boolean isIdeographic = false;
      Character.UnicodeBlock charBlock = UnicodeBlock.of((char)character);
      if (charBlock == null) {
         isIdeographic = false;
      } else if (IDEOGRAPHIC.contains(charBlock)) {
         isIdeographic = true;
      } else {
         isIdeographic = false;
      }

      return isIdeographic ? this.charSet.getEmSpaceIncrement() : this.charSet.getSpaceIncrement();
   }

   static {
      IDEOGRAPHIC.add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
      IDEOGRAPHIC.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
      IDEOGRAPHIC.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
   }
}
