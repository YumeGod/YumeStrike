package org.apache.fop.afp.util;

public class StringUtils {
   public static String lpad(String input, char padding, int length) {
      if (input == null) {
         input = new String();
      }

      if (input.length() >= length) {
         return input;
      } else {
         StringBuffer result = new StringBuffer();
         int numChars = length - input.length();

         for(int i = 0; i < numChars; ++i) {
            result.append(padding);
         }

         result.append(input);
         return result.toString();
      }
   }

   public static String rpad(String input, char padding, int length) {
      if (input == null) {
         input = new String();
      }

      if (input.length() >= length) {
         return input;
      } else {
         StringBuffer result = new StringBuffer(input);
         int numChars = length - input.length();

         for(int i = 0; i < numChars; ++i) {
            result.append(padding);
         }

         return result.toString();
      }
   }
}
