package org.apache.fop.util;

public class CharUtilities {
   public static final char CODE_EOT = '\u0000';
   public static final int UCWHITESPACE = 0;
   public static final int LINEFEED = 1;
   public static final int EOT = 2;
   public static final int NONWHITESPACE = 3;
   public static final int XMLWHITESPACE = 4;
   public static final char NULL_CHAR = '\u0000';
   public static final char LINEFEED_CHAR = '\n';
   public static final char CARRIAGE_RETURN = '\r';
   public static final char TAB = '\t';
   public static final char SPACE = ' ';
   public static final char NBSPACE = ' ';
   public static final char NEXT_LINE = '\u0085';
   public static final char ZERO_WIDTH_SPACE = '\u200b';
   public static final char WORD_JOINER = '\u2060';
   public static final char ZERO_WIDTH_JOINER = '\u200d';
   public static final char ZERO_WIDTH_NOBREAK_SPACE = '\ufeff';
   public static final char SOFT_HYPHEN = '\u00ad';
   public static final char LINE_SEPARATOR = '\u2028';
   public static final char PARAGRAPH_SEPARATOR = '\u2029';
   public static final char MISSING_IDEOGRAPH = '□';
   public static final char IDEOGRAPHIC_SPACE = '　';
   public static final char NOT_A_CHARACTER = '\uffff';

   protected CharUtilities() {
      throw new UnsupportedOperationException();
   }

   public static int classOf(char c) {
      switch (c) {
         case '\u0000':
            return 2;
         case '\t':
         case '\r':
         case ' ':
            return 4;
         case '\n':
            return 1;
         default:
            return isAnySpace(c) ? 0 : 3;
      }
   }

   public static boolean isBreakableSpace(char c) {
      return c == ' ' || isFixedWidthSpace(c);
   }

   public static boolean isZeroWidthSpace(char c) {
      return c == 8203 || c == 8288 || c == '\ufeff';
   }

   public static boolean isFixedWidthSpace(char c) {
      return c >= 8192 && c <= 8203 || c == 12288;
   }

   public static boolean isNonBreakableSpace(char c) {
      return c == 160 || c == 8239 || c == 12288 || c == 8288 || c == '\ufeff';
   }

   public static boolean isAdjustableSpace(char c) {
      return c == ' ' || c == 160;
   }

   public static boolean isAnySpace(char c) {
      return isBreakableSpace(c) || isNonBreakableSpace(c);
   }

   public static boolean isAlphabetic(char ch) {
      int generalCategory = Character.getType(ch);
      switch (generalCategory) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 10:
            return true;
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            return false;
      }
   }

   public static boolean isExplicitBreak(char ch) {
      return ch == '\n' || ch == '\r' || ch == 133 || ch == 8232 || ch == 8233;
   }
}
