package org.apache.fop.fo.pagination;

public class PageNumberGenerator {
   private String format;
   private char groupingSeparator;
   private int groupingSize;
   private int letterValue;
   private static final int DECIMAL = 1;
   private static final int LOWERALPHA = 2;
   private static final int UPPERALPHA = 3;
   private static final int LOWERROMAN = 4;
   private static final int UPPERROMAN = 5;
   private int formatType = 1;
   private int minPadding = 0;
   private String[] zeros = new String[]{"", "0", "00", "000", "0000", "00000"};

   public PageNumberGenerator(String format, char groupingSeparator, int groupingSize, int letterValue) {
      this.format = format;
      this.groupingSeparator = groupingSeparator;
      this.groupingSize = groupingSize;
      this.letterValue = letterValue;
      int fmtLen = format.length();
      if (fmtLen == 1) {
         if (format.equals("1")) {
            this.formatType = 1;
            this.minPadding = 0;
         } else if (format.equals("a")) {
            this.formatType = 2;
         } else if (format.equals("A")) {
            this.formatType = 3;
         } else if (format.equals("i")) {
            this.formatType = 4;
         } else if (format.equals("I")) {
            this.formatType = 5;
         } else {
            this.formatType = 1;
            this.minPadding = 0;
         }
      } else {
         for(int i = 0; i < fmtLen - 1; ++i) {
            if (format.charAt(i) != '0') {
               this.formatType = 1;
               this.minPadding = 0;
            } else {
               this.minPadding = fmtLen - 1;
            }
         }
      }

   }

   public String makeFormattedPageNumber(int number) {
      String pn = null;
      if (this.formatType == 1) {
         pn = Integer.toString(number);
         if (this.minPadding >= pn.length()) {
            int nz = this.minPadding - pn.length() + 1;
            pn = this.zeros[nz] + pn;
         }
      } else if (this.formatType != 4 && this.formatType != 5) {
         pn = this.makeAlpha(number);
         if (this.formatType == 3) {
            pn = pn.toUpperCase();
         }
      } else {
         pn = this.makeRoman(number);
         if (this.formatType == 5) {
            pn = pn.toUpperCase();
         }
      }

      return pn;
   }

   private String makeRoman(int num) {
      int[] arabic = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
      String[] roman = new String[]{"m", "cm", "d", "cd", "c", "xc", "l", "xl", "x", "ix", "v", "iv", "i"};
      int i = 0;

      StringBuffer romanNumber;
      for(romanNumber = new StringBuffer(); num > 0; ++i) {
         while(num >= arabic[i]) {
            num -= arabic[i];
            romanNumber.append(roman[i]);
         }
      }

      return romanNumber.toString();
   }

   private String makeAlpha(int num) {
      String letters = "abcdefghijklmnopqrstuvwxyz";
      StringBuffer alphaNumber = new StringBuffer();
      int base = 26;
      int rem = false;
      --num;
      if (num < base) {
         alphaNumber.append(letters.charAt(num));
      } else {
         while(true) {
            if (num < base) {
               alphaNumber.append(letters.charAt(num - 1));
               break;
            }

            int rem = num % base;
            alphaNumber.append(letters.charAt(rem));
            num /= base;
         }
      }

      return alphaNumber.reverse().toString();
   }
}
