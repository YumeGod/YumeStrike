package org.apache.fop.fonts;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Typeface implements FontMetrics {
   public static final char NOT_FOUND = '#';
   private static Log log;
   private long charMapOps = 0L;
   protected FontEventListener eventListener;
   private Set warnedChars;

   public abstract String getEncodingName();

   public abstract char mapChar(char var1);

   protected void notifyMapOperation() {
      ++this.charMapOps;
   }

   public boolean hadMappingOperations() {
      return this.charMapOps > 0L;
   }

   public abstract boolean hasChar(char var1);

   public boolean isMultiByte() {
      return false;
   }

   public int getMaxAscent(int size) {
      return this.getAscender(size);
   }

   public void setEventListener(FontEventListener listener) {
      this.eventListener = listener;
   }

   protected void warnMissingGlyph(char c) {
      Character ch = new Character(c);
      if (this.warnedChars == null) {
         this.warnedChars = new HashSet();
      }

      if (this.warnedChars.size() < 8 && !this.warnedChars.contains(ch)) {
         this.warnedChars.add(ch);
         if (this.eventListener != null) {
            this.eventListener.glyphNotAvailable(this, c, this.getFontName());
         } else if (this.warnedChars.size() == 8) {
            log.warn("Many requested glyphs are not available in font " + this.getFontName());
         } else {
            log.warn("Glyph " + c + " (0x" + Integer.toHexString(c) + ", " + org.apache.xmlgraphics.fonts.Glyphs.charToGlyphName(c) + ") not available in font " + this.getFontName());
         }
      }

   }

   public String toString() {
      return this.getFullName();
   }

   static {
      log = LogFactory.getLog(Typeface.class);
   }
}
