package encoders;

import java.io.ByteArrayOutputStream;

public class Base64 {
   private static final char[] intToBase64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
   private static final byte[] base64ToInt = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

   public static String encode(String var0) {
      try {
         return encode(var0.getBytes("UTF-8"));
      } catch (Exception var2) {
         return encode(var0.getBytes());
      }
   }

   public static String encode(byte[] var0) {
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;
      StringBuilder var4 = new StringBuilder();

      for(int var5 = 0; var5 < var0.length; ++var5) {
         int var6 = var0[var1++];
         if (var6 < 0) {
            var6 += 256;
         }

         var2 = (var2 << 8) + var6;
         ++var3;
         if (var3 == 3) {
            var4.append(intToBase64[var2 >> 18]);
            var4.append(intToBase64[var2 >> 12 & 63]);
            var4.append(intToBase64[var2 >> 6 & 63]);
            var4.append(intToBase64[var2 & 63]);
            var2 = 0;
            var3 = 0;
         }
      }

      if (var3 > 0) {
         if (var3 == 1) {
            var4.append(intToBase64[var2 >> 2]);
            var4.append(intToBase64[var2 << 4 & 63]);
            var4.append("==");
         } else {
            var4.append(intToBase64[var2 >> 10]);
            var4.append(intToBase64[var2 >> 4 & 63]);
            var4.append(intToBase64[var2 << 2 & 63]);
            var4.append('=');
         }
      }

      return var4.toString();
   }

   public static byte[] decode(String var0) {
      int var1 = 0;
      int var2 = 0;
      int var3 = 0;
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();

      for(int var5 = 0; var5 < var0.length(); ++var5) {
         char var6 = var0.charAt(var5);
         if (!Character.isWhitespace(var6)) {
            if (var6 == '=') {
               ++var3;
               var1 <<= 6;
               ++var2;
               switch (var2) {
                  case 1:
                  case 2:
                     throw new RuntimeException("Unexpected end of stream character (=)");
                  case 3:
                     break;
                  case 4:
                     var4.write((byte)(var1 >> 16));
                     if (var3 == 1) {
                        var4.write((byte)(var1 >> 8));
                     }
                     break;
                  case 5:
                     throw new RuntimeException("Trailing garbage detected");
                  default:
                     throw new IllegalStateException("Invalid value for numBytes");
               }
            } else {
               if (var3 > 0) {
                  throw new RuntimeException("Base64 characters after end of stream character (=) detected.");
               }

               if (var6 >= 0 && var6 < base64ToInt.length) {
                  byte var7 = base64ToInt[var6];
                  if (var7 >= 0) {
                     var1 = (var1 << 6) + var7;
                     ++var2;
                     if (var2 == 4) {
                        var4.write((byte)(var1 >> 16));
                        var4.write((byte)(var1 >> 8 & 255));
                        var4.write((byte)(var1 & 255));
                        var1 = 0;
                        var2 = 0;
                     }
                     continue;
                  }
               }

               if (!Character.isWhitespace(var6)) {
                  throw new RuntimeException("Invalid Base64 character: " + var6);
               }
            }
         }
      }

      return var4.toByteArray();
   }
}
