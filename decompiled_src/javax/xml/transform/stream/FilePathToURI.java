package javax.xml.transform.stream;

import java.io.File;
import java.io.UnsupportedEncodingException;

class FilePathToURI {
   private static boolean[] gNeedEscaping = new boolean[128];
   private static char[] gAfterEscaping1 = new char[128];
   private static char[] gAfterEscaping2 = new char[128];
   private static char[] gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public static String filepath2URI(String var0) {
      if (var0 == null) {
         return null;
      } else {
         char var1 = File.separatorChar;
         var0 = var0.replace(var1, '/');
         int var2 = var0.length();
         StringBuffer var4 = new StringBuffer(var2 * 3);
         var4.append("file://");
         int var3;
         if (var2 >= 2 && var0.charAt(1) == ':') {
            var3 = Character.toUpperCase(var0.charAt(0));
            if (var3 >= 65 && var3 <= 90) {
               var4.append('/');
            }
         }

         int var5;
         for(var5 = 0; var5 < var2; ++var5) {
            var3 = var0.charAt(var5);
            if (var3 >= 128) {
               break;
            }

            if (gNeedEscaping[var3]) {
               var4.append('%');
               var4.append(gAfterEscaping1[var3]);
               var4.append(gAfterEscaping2[var3]);
            } else {
               var4.append((char)var3);
            }
         }

         if (var5 < var2) {
            Object var6 = null;

            byte[] var10;
            try {
               var10 = var0.substring(var5).getBytes("UTF-8");
            } catch (UnsupportedEncodingException var9) {
               return var0;
            }

            var2 = var10.length;

            for(var5 = 0; var5 < var2; ++var5) {
               byte var7 = var10[var5];
               if (var7 < 0) {
                  var3 = var7 + 256;
                  var4.append('%');
                  var4.append(gHexChs[var3 >> 4]);
                  var4.append(gHexChs[var3 & 15]);
               } else if (gNeedEscaping[var7]) {
                  var4.append('%');
                  var4.append(gAfterEscaping1[var7]);
                  var4.append(gAfterEscaping2[var7]);
               } else {
                  var4.append((char)var7);
               }
            }
         }

         return var4.toString();
      }
   }

   static {
      for(int var0 = 0; var0 <= 31; ++var0) {
         gNeedEscaping[var0] = true;
         gAfterEscaping1[var0] = gHexChs[var0 >> 4];
         gAfterEscaping2[var0] = gHexChs[var0 & 15];
      }

      gNeedEscaping[127] = true;
      gAfterEscaping1[127] = '7';
      gAfterEscaping2[127] = 'F';
      char[] var1 = new char[]{' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
      int var2 = var1.length;

      for(int var4 = 0; var4 < var2; ++var4) {
         char var3 = var1[var4];
         gNeedEscaping[var3] = true;
         gAfterEscaping1[var3] = gHexChs[var3 >> 4];
         gAfterEscaping2[var3] = gHexChs[var3 & 15];
      }

   }
}
