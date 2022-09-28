package org.apache.fop.fonts;

import java.util.Collections;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Font {
   public static final int WEIGHT_EXTRA_BOLD = 800;
   public static final int WEIGHT_BOLD = 700;
   public static final int WEIGHT_NORMAL = 400;
   public static final int WEIGHT_LIGHT = 200;
   public static final String STYLE_NORMAL = "normal";
   public static final String STYLE_ITALIC = "italic";
   public static final String STYLE_OBLIQUE = "oblique";
   public static final String STYLE_INCLINED = "inclined";
   public static final int PRIORITY_DEFAULT = 0;
   public static final FontTriplet DEFAULT_FONT = new FontTriplet("any", "normal", 400, 0);
   private static Log log;
   private final String fontName;
   private final FontTriplet triplet;
   private final int fontSize;
   private final FontMetrics metric;

   public Font(String key, FontTriplet triplet, FontMetrics met, int fontSize) {
      this.fontName = key;
      this.triplet = triplet;
      this.metric = met;
      this.fontSize = fontSize;
   }

   public FontMetrics getFontMetrics() {
      return this.metric;
   }

   public int getAscender() {
      return this.metric.getAscender(this.fontSize) / 1000;
   }

   public int getCapHeight() {
      return this.metric.getCapHeight(this.fontSize) / 1000;
   }

   public int getDescender() {
      return this.metric.getDescender(this.fontSize) / 1000;
   }

   public String getFontName() {
      return this.fontName;
   }

   public FontTriplet getFontTriplet() {
      return this.triplet;
   }

   public int getFontSize() {
      return this.fontSize;
   }

   public int getXHeight() {
      return this.metric.getXHeight(this.fontSize) / 1000;
   }

   public boolean hasKerning() {
      return this.metric.hasKerningInfo();
   }

   public Map getKerning() {
      return this.metric.hasKerningInfo() ? this.metric.getKerningInfo() : Collections.EMPTY_MAP;
   }

   public int getKernValue(char ch1, char ch2) {
      Map kernPair = (Map)this.getKerning().get(new Integer(ch1));
      if (kernPair != null) {
         Integer width = (Integer)kernPair.get(new Integer(ch2));
         if (width != null) {
            return width * this.getFontSize() / 1000;
         }
      }

      return 0;
   }

   public int getWidth(int charnum) {
      return this.metric.getWidth(charnum, this.fontSize) / 1000;
   }

   public char mapChar(char c) {
      if (this.metric instanceof Typeface) {
         return ((Typeface)this.metric).mapChar(c);
      } else {
         char d = CodePointMapping.getMapping("WinAnsiEncoding").mapChar(c);
         if (d != 0) {
            c = d;
         } else {
            log.warn("Glyph " + c + " not available in font " + this.fontName);
            c = '#';
         }

         return c;
      }
   }

   public boolean hasChar(char c) {
      if (this.metric instanceof Typeface) {
         return ((Typeface)this.metric).hasChar(c);
      } else {
         return CodePointMapping.getMapping("WinAnsiEncoding").mapChar(c) > 0;
      }
   }

   public String toString() {
      StringBuffer sbuf = new StringBuffer();
      sbuf.append('(');
      sbuf.append(this.fontName);
      sbuf.append(',');
      sbuf.append(this.fontSize);
      sbuf.append(')');
      return sbuf.toString();
   }

   public int getCharWidth(char c) {
      int width;
      if (c != '\n' && c != '\r' && c != '\t' && c != 160) {
         int em;
         if (this.hasChar(c)) {
            em = this.mapChar(c);
            width = this.getWidth(em);
         } else {
            width = -1;
         }

         if (width <= 0) {
            em = this.getFontSize();
            int en = em / 2;
            if (c == ' ') {
               width = em;
            } else if (c == 8192) {
               width = en;
            } else if (c == 8193) {
               width = em;
            } else if (c == 8194) {
               width = em / 2;
            } else if (c == 8195) {
               width = this.getFontSize();
            } else if (c == 8196) {
               width = em / 3;
            } else if (c == 8197) {
               width = em / 4;
            } else if (c == 8198) {
               width = em / 6;
            } else if (c == 8199) {
               width = this.getCharWidth('0');
            } else if (c == 8200) {
               width = this.getCharWidth('.');
            } else if (c == 8201) {
               width = em / 5;
            } else if (c == 8202) {
               width = em / 10;
            } else if (c == 8203) {
               width = 0;
            } else if (c == 8239) {
               width = this.getCharWidth(' ') / 2;
            } else if (c == 8288) {
               width = 0;
            } else if (c == 12288) {
               width = this.getCharWidth(' ') * 2;
            } else if (c == '\ufeff') {
               width = 0;
            } else {
               width = this.getWidth(this.mapChar(c));
            }
         }
      } else {
         width = this.getCharWidth(' ');
      }

      return width;
   }

   public int getWordWidth(String word) {
      if (word == null) {
         return 0;
      } else {
         int wordLength = word.length();
         int width = 0;
         char[] characters = new char[wordLength];
         word.getChars(0, wordLength, characters, 0);

         for(int i = 0; i < wordLength; ++i) {
            width += this.getCharWidth(characters[i]);
         }

         return width;
      }
   }

   static {
      log = LogFactory.getLog(Font.class);
   }
}
