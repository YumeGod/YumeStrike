package org.apache.fop.render.rtf.rtflib.tools;

public class ImageUtil {
   private ImageUtil() {
   }

   public static int getInt(String value) {
      String retString = new String();
      StringBuffer s = new StringBuffer(value);
      int len = s.length();

      for(int i = 0; i < len && Character.isDigit(s.charAt(i)); ++i) {
         retString = retString + s.charAt(i);
      }

      return retString.length() == 0 ? -1 : Integer.parseInt(retString);
   }

   public static boolean isPercent(String value) {
      return value.endsWith("%");
   }

   public static boolean compareHexValues(byte[] pattern, byte[] data, int searchAt, boolean searchForward) {
      if (searchAt >= data.length) {
         return false;
      } else {
         int pLen = pattern.length;
         int i;
         if (searchForward) {
            if (pLen >= data.length - searchAt) {
               return false;
            } else {
               for(i = 0; i < pLen; ++i) {
                  if (pattern[i] != data[searchAt + i]) {
                     return false;
                  }
               }

               return true;
            }
         } else if (pLen > searchAt + 1) {
            return false;
         } else {
            for(i = 0; i < pLen; ++i) {
               if (pattern[pLen - i - 1] != data[searchAt - i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static int getIntFromByteArray(byte[] data, int startAt, int length, boolean searchForward) {
      int bit = 8;
      int bitMoving = length * bit;
      int retVal = 0;
      if (startAt >= data.length) {
         return retVal;
      } else {
         int i;
         int iData;
         if (searchForward) {
            if (length >= data.length - startAt) {
               return retVal;
            }

            for(i = 0; i < length; ++i) {
               bitMoving -= bit;
               iData = data[startAt + i];
               if (iData < 0) {
                  iData += 256;
               }

               retVal += iData << bitMoving;
            }
         } else {
            if (length > startAt + 1) {
               return retVal;
            }

            for(i = 0; i < length; ++i) {
               bitMoving -= bit;
               iData = data[startAt - i];
               if (iData < 0) {
                  iData += 256;
               }

               retVal += iData << bitMoving;
            }
         }

         return retVal;
      }
   }
}
