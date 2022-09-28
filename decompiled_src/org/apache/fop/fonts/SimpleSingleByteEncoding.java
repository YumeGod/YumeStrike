package org.apache.fop.fonts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleSingleByteEncoding implements SingleByteEncoding {
   private String name;
   private List mapping = new ArrayList();
   private Map charMap = new HashMap();

   public SimpleSingleByteEncoding(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public char mapChar(char c) {
      Character nc = (Character)this.charMap.get(new Character(c));
      return nc != null ? nc : '\u0000';
   }

   public String[] getCharNameMap() {
      String[] map = new String[this.getSize()];
      Arrays.fill(map, ".notdef");

      for(int i = this.getFirstChar(); i <= this.getLastChar(); ++i) {
         NamedCharacter ch = (NamedCharacter)this.mapping.get(i - 1);
         map[i] = ch.getName();
      }

      return map;
   }

   public int getFirstChar() {
      return 1;
   }

   public int getLastChar() {
      return this.mapping.size();
   }

   public int getSize() {
      return this.mapping.size() + 1;
   }

   public boolean isFull() {
      return this.getSize() == 256;
   }

   public char addCharacter(NamedCharacter ch) {
      if (!ch.hasSingleUnicodeValue()) {
         throw new IllegalArgumentException("Only NamedCharacters with a single Unicode value are currently supported!");
      } else if (this.isFull()) {
         throw new IllegalStateException("Encoding is full!");
      } else {
         char newSlot = (char)(this.getLastChar() + 1);
         this.mapping.add(ch);
         this.charMap.put(new Character(ch.getSingleUnicodeValue()), new Character(newSlot));
         return newSlot;
      }
   }

   public NamedCharacter getCharacterForIndex(int codePoint) {
      if (codePoint >= 0 && codePoint <= 255) {
         return codePoint <= this.getLastChar() ? (NamedCharacter)this.mapping.get(codePoint - 1) : null;
      } else {
         throw new IllegalArgumentException("codePoint must be between 0 and 255");
      }
   }

   public char[] getUnicodeCharMap() {
      char[] map = new char[this.getLastChar() + 1];

      int i;
      for(i = 0; i < this.getFirstChar(); ++i) {
         map[i] = '\uffff';
      }

      for(i = this.getFirstChar(); i <= this.getLastChar(); ++i) {
         map[i] = this.getCharacterForIndex(i).getSingleUnicodeValue();
      }

      return map;
   }

   public String toString() {
      return this.getName() + " (" + this.getSize() + " chars)";
   }
}
