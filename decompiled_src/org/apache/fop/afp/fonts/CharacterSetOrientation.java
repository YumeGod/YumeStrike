package org.apache.fop.afp.fonts;

import java.util.Arrays;

public class CharacterSetOrientation {
   private int ascender;
   private int descender;
   private int capHeight;
   private int[] charsWidths = null;
   private int xHeight;
   private char firstChar;
   private char lastChar;
   private int orientation = 0;
   private int spaceIncrement;
   private int emSpaceIncrement = -1;

   public CharacterSetOrientation(int orientation) {
      this.orientation = orientation;
      this.charsWidths = new int[256];
      Arrays.fill(this.charsWidths, -1);
   }

   public int getAscender() {
      return this.ascender;
   }

   public int getCapHeight() {
      return this.capHeight;
   }

   public int getDescender() {
      return this.descender;
   }

   public char getFirstChar() {
      return this.firstChar;
   }

   public char getLastChar() {
      return this.lastChar;
   }

   public int getOrientation() {
      return this.orientation;
   }

   public int[] getWidths() {
      int[] arr = new int[this.getLastChar() - this.getFirstChar() + 1];
      System.arraycopy(this.charsWidths, this.getFirstChar(), arr, 0, this.getLastChar() - this.getFirstChar() + 1);
      return arr;
   }

   public int getXHeight() {
      return this.xHeight;
   }

   public int getWidth(char character) {
      if (character >= this.charsWidths.length) {
         throw new IllegalArgumentException("Invalid character: " + character + " (" + Integer.toString(character) + "), maximum is " + (this.charsWidths.length - 1));
      } else {
         return this.charsWidths[character];
      }
   }

   public void setAscender(int ascender) {
      this.ascender = ascender;
   }

   public void setCapHeight(int capHeight) {
      this.capHeight = capHeight;
   }

   public void setDescender(int descender) {
      this.descender = descender;
   }

   public void setFirstChar(char firstChar) {
      this.firstChar = firstChar;
   }

   public void setLastChar(char lastChar) {
      this.lastChar = lastChar;
   }

   public void setWidth(char character, int width) {
      if (character >= this.charsWidths.length) {
         int[] arr = new int[character - this.firstChar + 1];
         System.arraycopy(this.charsWidths, 0, arr, 0, this.charsWidths.length);
         Arrays.fill(arr, this.charsWidths.length, character - this.firstChar, -1);
         this.charsWidths = arr;
      }

      this.charsWidths[character] = width;
   }

   public void setXHeight(int xHeight) {
      this.xHeight = xHeight;
   }

   public int getSpaceIncrement() {
      return this.spaceIncrement;
   }

   public void setSpaceIncrement(int value) {
      this.spaceIncrement = value;
   }

   public int getEmSpaceIncrement() {
      return this.emSpaceIncrement;
   }

   public void setEmSpaceIncrement(int value) {
      this.emSpaceIncrement = value;
   }
}
