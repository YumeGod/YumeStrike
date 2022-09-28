package org.apache.fop.afp.fonts;

public abstract class AbstractOutlineFont extends AFPFont {
   protected CharacterSet charSet = null;

   public AbstractOutlineFont(String name, CharacterSet charSet) {
      super(name);
      this.charSet = charSet;
   }

   public CharacterSet getCharacterSet() {
      return this.charSet;
   }

   public CharacterSet getCharacterSet(int size) {
      return this.charSet;
   }

   public int getFirstChar() {
      return this.charSet.getFirstChar();
   }

   public int getLastChar() {
      return this.charSet.getLastChar();
   }

   public int getAscender(int size) {
      return this.charSet.getAscender() * size;
   }

   public int getCapHeight(int size) {
      return this.charSet.getCapHeight() * size;
   }

   public int getDescender(int size) {
      return this.charSet.getDescender() * size;
   }

   public int getXHeight(int size) {
      return this.charSet.getXHeight() * size;
   }

   public int getWidth(int character, int size) {
      return this.charSet.getWidth(toUnicodeCodepoint(character)) * size;
   }

   public int[] getWidths(int size) {
      int[] widths = this.charSet.getWidths();

      for(int i = 0; i < widths.length; ++i) {
         widths[i] *= size;
      }

      return widths;
   }

   public int[] getWidths() {
      return this.getWidths(1000);
   }

   public boolean hasChar(char c) {
      return this.charSet.hasChar(c);
   }

   public char mapChar(char c) {
      return this.charSet.mapChar(c);
   }

   public String getEncodingName() {
      return this.charSet.getEncoding();
   }
}
