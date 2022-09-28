package net.jsign.commons.cli;

final class Util {
   static String stripLeadingHyphens(String str) {
      if (str == null) {
         return null;
      } else if (str.startsWith("--")) {
         return str.substring(2, str.length());
      } else {
         return str.startsWith("-") ? str.substring(1, str.length()) : str;
      }
   }

   static String stripLeadingAndTrailingQuotes(String str) {
      int length = str.length();
      if (length > 1 && str.startsWith("\"") && str.endsWith("\"") && str.substring(1, length - 1).indexOf(34) == -1) {
         str = str.substring(1, length - 1);
      }

      return str;
   }
}
