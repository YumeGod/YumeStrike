package net.jsign.bouncycastle.util;

public class IPAddress {
   public static boolean isValid(String var0) {
      return isValidIPv4(var0) || isValidIPv6(var0);
   }

   public static boolean isValidWithNetMask(String var0) {
      return isValidIPv4WithNetmask(var0) || isValidIPv6WithNetmask(var0);
   }

   public static boolean isValidIPv4(String var0) {
      if (var0.length() == 0) {
         return false;
      } else {
         int var1 = 0;
         String var2 = var0 + ".";
         int var3 = 0;

         while(true) {
            int var4;
            if (var3 < var2.length() && (var4 = var2.indexOf(46, var3)) > var3) {
               if (var1 == 4) {
                  return false;
               }

               int var5;
               try {
                  var5 = Integer.parseInt(var2.substring(var3, var4));
               } catch (NumberFormatException var7) {
                  return false;
               }

               if (var5 >= 0 && var5 <= 255) {
                  var3 = var4 + 1;
                  ++var1;
                  continue;
               }

               return false;
            }

            return var1 == 4;
         }
      }
   }

   public static boolean isValidIPv4WithNetmask(String var0) {
      int var1 = var0.indexOf("/");
      String var2 = var0.substring(var1 + 1);
      return var1 > 0 && isValidIPv4(var0.substring(0, var1)) && (isValidIPv4(var2) || isMaskValue(var2, 32));
   }

   public static boolean isValidIPv6WithNetmask(String var0) {
      int var1 = var0.indexOf("/");
      String var2 = var0.substring(var1 + 1);
      return var1 > 0 && isValidIPv6(var0.substring(0, var1)) && (isValidIPv6(var2) || isMaskValue(var2, 128));
   }

   private static boolean isMaskValue(String var0, int var1) {
      try {
         int var2 = Integer.parseInt(var0);
         return var2 >= 0 && var2 <= var1;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public static boolean isValidIPv6(String var0) {
      if (var0.length() == 0) {
         return false;
      } else {
         int var1 = 0;
         String var2 = var0 + ":";
         boolean var3 = false;

         int var5;
         for(int var4 = 0; var4 < var2.length() && (var5 = var2.indexOf(58, var4)) >= var4; ++var1) {
            if (var1 == 8) {
               return false;
            }

            if (var4 != var5) {
               String var6 = var2.substring(var4, var5);
               if (var5 == var2.length() - 1 && var6.indexOf(46) > 0) {
                  if (!isValidIPv4(var6)) {
                     return false;
                  }

                  ++var1;
               } else {
                  int var7;
                  try {
                     var7 = Integer.parseInt(var2.substring(var4, var5), 16);
                  } catch (NumberFormatException var9) {
                     return false;
                  }

                  if (var7 < 0 || var7 > 65535) {
                     return false;
                  }
               }
            } else {
               if (var5 != 1 && var5 != var2.length() - 1 && var3) {
                  return false;
               }

               var3 = true;
            }

            var4 = var5 + 1;
         }

         return var1 == 8 || var3;
      }
   }
}
