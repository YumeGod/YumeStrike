package org.apache.batik.css.parser;

public class ScannerUtilities {
   protected static final int[] IDENTIFIER_START = new int[]{0, 0, -2013265922, 134217726};
   protected static final int[] NAME = new int[]{0, 67051520, -2013265922, 134217726};
   protected static final int[] HEXADECIMAL = new int[]{0, 67043328, 126, 126};
   protected static final int[] STRING = new int[]{512, -133, -1, Integer.MAX_VALUE};
   protected static final int[] URI = new int[]{0, -902, -1, Integer.MAX_VALUE};

   protected ScannerUtilities() {
   }

   public static boolean isCSSSpace(char var0) {
      return var0 <= ' ' && (4294981120L >> var0 & 1L) != 0L;
   }

   public static boolean isCSSIdentifierStartCharacter(char var0) {
      return var0 >= 128 || (IDENTIFIER_START[var0 >> 5] & 1 << (var0 & 31)) != 0;
   }

   public static boolean isCSSNameCharacter(char var0) {
      return var0 >= 128 || (NAME[var0 >> 5] & 1 << (var0 & 31)) != 0;
   }

   public static boolean isCSSHexadecimalCharacter(char var0) {
      return var0 < 128 && (HEXADECIMAL[var0 >> 5] & 1 << (var0 & 31)) != 0;
   }

   public static boolean isCSSStringCharacter(char var0) {
      return var0 >= 128 || (STRING[var0 >> 5] & 1 << (var0 & 31)) != 0;
   }

   public static boolean isCSSURICharacter(char var0) {
      return var0 >= 128 || (URI[var0 >> 5] & 1 << (var0 & 31)) != 0;
   }
}
