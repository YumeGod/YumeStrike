package encoders;

import java.io.ByteArrayOutputStream;

public class NetBIOS {
   public static String encode(char var0, String var1) {
      try {
         return encode(var0, var1.getBytes("UTF-8"));
      } catch (Exception var3) {
         return encode(var0, var1.getBytes());
      }
   }

   public static String encode(char var0, byte[] var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         int var4 = (var1[var3] & 240) >> 4;
         int var5 = var1[var3] & 15;
         var4 += (byte)var0;
         var5 += (byte)var0;
         var2.append((char)var4);
         var2.append((char)var5);
      }

      return var2.toString();
   }

   public static byte[] decode(char var0, String var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      for(int var3 = 0; var3 < var1.length(); var3 += 2) {
         char var4 = var1.charAt(var3);
         char var5 = var1.charAt(var3 + 1);
         byte var6 = (byte)(var4 - (byte)var0 << 4);
         var6 += (byte)(var5 - (byte)var0);
         var2.write(var6);
      }

      return var2.toByteArray();
   }

   public static void main(String[] var0) throws Exception {
      String var1 = encode('A', "this is a test");
      System.err.println("Encode: " + var1);
      System.err.println("Decode: '" + new String(decode('A', var1), "UTF-8") + "'");
   }
}
