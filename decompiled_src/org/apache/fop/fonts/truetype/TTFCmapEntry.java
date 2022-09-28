package org.apache.fop.fonts.truetype;

public class TTFCmapEntry {
   private int unicodeStart;
   private int unicodeEnd;
   private int glyphStartIndex;

   TTFCmapEntry() {
      this.unicodeStart = 0;
      this.unicodeEnd = 0;
      this.glyphStartIndex = 0;
   }

   TTFCmapEntry(int unicodeStart, int unicodeEnd, int glyphStartIndex) {
      this.unicodeStart = unicodeStart;
      this.unicodeEnd = unicodeEnd;
      this.glyphStartIndex = glyphStartIndex;
   }

   public boolean equals(Object o) {
      if (o instanceof TTFCmapEntry) {
         TTFCmapEntry ce = (TTFCmapEntry)o;
         if (ce.unicodeStart == this.unicodeStart && ce.unicodeEnd == this.unicodeEnd && ce.glyphStartIndex == this.glyphStartIndex) {
            return true;
         }
      }

      return false;
   }

   public int getGlyphStartIndex() {
      return this.glyphStartIndex;
   }

   public int getUnicodeEnd() {
      return this.unicodeEnd;
   }

   public int getUnicodeStart() {
      return this.unicodeStart;
   }

   public void setGlyphStartIndex(int glyphStartIndex) {
      this.glyphStartIndex = glyphStartIndex;
   }

   public void setUnicodeEnd(int unicodeEnd) {
      this.unicodeEnd = unicodeEnd;
   }

   public void setUnicodeStart(int unicodeStart) {
      this.unicodeStart = unicodeStart;
   }
}
