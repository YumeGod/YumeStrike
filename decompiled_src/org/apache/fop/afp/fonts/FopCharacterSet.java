package org.apache.fop.afp.fonts;

import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.fonts.Typeface;

public class FopCharacterSet extends CharacterSet {
   private Typeface charSet = null;

   public FopCharacterSet(String codePage, String encoding, String name, Typeface charSet) {
      super(codePage, encoding, name, (ResourceAccessor)null);
      this.charSet = charSet;
   }

   public int getAscender() {
      return this.charSet.getAscender(1);
   }

   public int getCapHeight() {
      return this.charSet.getCapHeight(1);
   }

   public int getDescender() {
      return this.charSet.getDescender(1);
   }

   public char getFirstChar() {
      return '\u0000';
   }

   public char getLastChar() {
      return '\u0000';
   }

   public int[] getWidths() {
      return this.charSet.getWidths();
   }

   public int getXHeight() {
      return this.charSet.getXHeight(1);
   }

   public int getWidth(char character) {
      return this.charSet.getWidth(character, 1);
   }

   public char mapChar(char c) {
      return this.charSet.mapChar(c);
   }
}
