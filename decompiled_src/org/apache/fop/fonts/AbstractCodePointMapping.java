package org.apache.fop.fonts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AbstractCodePointMapping implements SingleByteEncoding {
   private String name;
   private char[] latin1Map;
   private char[] characters;
   private char[] codepoints;
   private char[] unicodeMap;
   private String[] charNameMap;
   private Map fallbackMap;

   public AbstractCodePointMapping(String name, int[] table) {
      this(name, table, (String[])null);
   }

   public AbstractCodePointMapping(String name, int[] table, String[] charNameMap) {
      this.name = name;
      this.buildFromTable(table);
      if (charNameMap != null) {
         this.charNameMap = new String[256];

         for(int i = 0; i < 256; ++i) {
            String charName = charNameMap[i];
            if (charName == null) {
               this.charNameMap[i] = ".notdef";
            } else {
               this.charNameMap[i] = charName;
            }
         }
      }

   }

   protected void buildFromTable(int[] table) {
      int nonLatin1 = 0;
      this.latin1Map = new char[256];
      this.unicodeMap = new char[256];
      Arrays.fill(this.unicodeMap, '\uffff');

      int top;
      int i;
      for(top = 0; top < table.length; top += 2) {
         i = (char)table[top + 1];
         if (i < 256) {
            if (this.latin1Map[i] == 0) {
               this.latin1Map[i] = (char)table[top];
            }
         } else {
            ++nonLatin1;
         }

         if (this.unicodeMap[table[top]] == '\uffff') {
            this.unicodeMap[table[top]] = (char)i;
         }
      }

      this.characters = new char[nonLatin1];
      this.codepoints = new char[nonLatin1];
      top = 0;

      for(i = 0; i < table.length; i += 2) {
         char c = (char)table[i + 1];
         if (c >= 256) {
            ++top;

            for(int j = top - 1; j >= 0; --j) {
               if (j <= 0 || this.characters[j - 1] < c) {
                  this.characters[j] = c;
                  this.codepoints[j] = (char)table[i];
                  break;
               }

               this.characters[j] = this.characters[j - 1];
               this.codepoints[j] = this.codepoints[j - 1];
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public final char mapChar(char c) {
      int bot;
      if (c < 256) {
         bot = this.latin1Map[c];
         if (bot > 0) {
            return (char)bot;
         }
      }

      bot = 0;
      int top = this.characters.length - 1;

      while(top >= bot) {
         int mid = (bot + top) / 2;
         char mc = this.characters[mid];
         if (c == mc) {
            return this.codepoints[mid];
         }

         if (c < mc) {
            top = mid - 1;
         } else {
            bot = mid + 1;
         }
      }

      synchronized(this) {
         if (this.fallbackMap != null) {
            Character fallback = (Character)this.fallbackMap.get(new Character(c));
            if (fallback != null) {
               return fallback;
            }
         }
      }

      String glyphName = org.apache.xmlgraphics.fonts.Glyphs.charToGlyphName(c);
      if (glyphName.length() > 0) {
         String[] alternatives = org.apache.xmlgraphics.fonts.Glyphs.getCharNameAlternativesFor(glyphName);
         if (alternatives != null) {
            int i = 0;

            for(int ic = alternatives.length; i < ic; ++i) {
               int idx = this.getCodePointForGlyph(alternatives[i]);
               if (idx >= 0) {
                  this.putFallbackCharacter(c, (char)idx);
                  return (char)idx;
               }
            }
         }
      }

      this.putFallbackCharacter(c, '\u0000');
      return '\u0000';
   }

   private void putFallbackCharacter(char c, char mapTo) {
      synchronized(this) {
         if (this.fallbackMap == null) {
            this.fallbackMap = new HashMap();
         }

         this.fallbackMap.put(new Character(c), new Character(mapTo));
      }
   }

   public final char getUnicodeForIndex(int idx) {
      return this.unicodeMap[idx];
   }

   public final char[] getUnicodeCharMap() {
      char[] copy = new char[this.unicodeMap.length];
      System.arraycopy(this.unicodeMap, 0, copy, 0, this.unicodeMap.length);
      return copy;
   }

   public short getCodePointForGlyph(String charName) {
      String[] names = this.charNameMap;
      if (names == null) {
         names = this.getCharNameMap();
      }

      short i = 0;

      for(short c = (short)names.length; i < c; ++i) {
         if (names[i].equals(charName)) {
            return i;
         }
      }

      return -1;
   }

   public String[] getCharNameMap() {
      String[] derived;
      if (this.charNameMap != null) {
         derived = new String[this.charNameMap.length];
         System.arraycopy(this.charNameMap, 0, derived, 0, this.charNameMap.length);
         return derived;
      } else {
         derived = new String[256];
         Arrays.fill(derived, ".notdef");

         for(int i = 0; i < 256; ++i) {
            char c = this.getUnicodeForIndex(i);
            if (c != '\uffff') {
               String charName = org.apache.xmlgraphics.fonts.Glyphs.charToGlyphName(c);
               if (charName.length() > 0) {
                  derived[i] = charName;
               }
            }
         }

         return derived;
      }
   }

   public String toString() {
      return this.getName();
   }
}
