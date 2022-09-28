package org.apache.fop.fonts;

public class BFEntry {
   private int unicodeStart;
   private int unicodeEnd;
   private int glyphStartIndex;

   public BFEntry(int unicodeStart, int unicodeEnd, int glyphStartIndex) {
      this.unicodeStart = unicodeStart;
      this.unicodeEnd = unicodeEnd;
      this.glyphStartIndex = glyphStartIndex;
   }

   public int getUnicodeStart() {
      return this.unicodeStart;
   }

   public int getUnicodeEnd() {
      return this.unicodeEnd;
   }

   public int getGlyphStartIndex() {
      return this.glyphStartIndex;
   }
}
