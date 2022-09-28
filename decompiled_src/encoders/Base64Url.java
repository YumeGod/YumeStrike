package encoders;

public class Base64Url {
   public static String fix(String var0) {
      if (var0.endsWith("=")) {
         return fix(var0.substring(0, var0.length() - 1));
      } else {
         char[] var1 = var0.toCharArray();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] == '/') {
               var1[var2] = '_';
            } else if (var1[var2] == '+') {
               var1[var2] = '-';
            }
         }

         return new String(var1);
      }
   }

   public static String fix_reverse(String var0) {
      char[] var1 = var0.toCharArray();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] == '_') {
            var1[var2] = '/';
         } else if (var1[var2] == '-') {
            var1[var2] = '+';
         }
      }

      StringBuffer var3 = new StringBuffer(new String(var1));

      while(var3.length() % 4 != 0) {
         var3.append("=");
      }

      return var3.toString();
   }

   public static String encode(byte[] var0) {
      return fix(Base64.encode(var0));
   }

   public static byte[] decode(String var0) {
      return Base64.decode(fix_reverse(var0));
   }
}
