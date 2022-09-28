package net.jsign.commons.cli;

final class OptionValidator {
   static void validateOption(String opt) throws IllegalArgumentException {
      if (opt != null) {
         if (opt.length() == 1) {
            char ch = opt.charAt(0);
            if (!isValidOpt(ch)) {
               throw new IllegalArgumentException("Illegal option name '" + ch + "'");
            }
         } else {
            char[] var5 = opt.toCharArray();
            int var2 = var5.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               char ch = var5[var3];
               if (!isValidChar(ch)) {
                  throw new IllegalArgumentException("The option '" + opt + "' contains an illegal " + "character : '" + ch + "'");
               }
            }
         }

      }
   }

   private static boolean isValidOpt(char c) {
      return isValidChar(c) || c == '?' || c == '@';
   }

   private static boolean isValidChar(char c) {
      return Character.isJavaIdentifierPart(c);
   }
}
