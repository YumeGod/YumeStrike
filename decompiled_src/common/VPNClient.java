package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VPNClient {
   private static String _filter(Set var0) {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("(not host " + var3 + ")");
         if (var2.hasNext()) {
            var1.append(" && ");
         }
      }

      return var1.toString();
   }

   public static byte[] exportClient(String var0, String var1, Map var2, Set var3) {
      String var4 = (String)var2.get("channel");
      int var5 = (Integer)var2.get("port");
      byte[] var6 = (byte[])((byte[])var2.get("secret"));
      String var7 = (String)var2.get("hook");
      String var8 = (String)var2.get("useragent");
      String var9 = _filter(var3);
      if (var4.equals("TCP (Bind)")) {
         var4 = "b";
      }

      return exportClient(var0, var1, var4.charAt(0) + "", var5, var6, var7, var8, var9);
   }

   public static byte[] exportClient(String var0, String var1, String var2, int var3, byte[] var4, String var5, String var6, String var7) {
      try {
         InputStream var8 = CommonUtils.resource("resources/covertvpn.dll");
         byte[] var9 = CommonUtils.readAll(var8);
         var8.close();
         Packer var10 = new Packer();
         var10.little();
         var10.addString((String)var0, 16);
         var10.addString((String)var1, 16);
         var10.addString((String)var2.toLowerCase(), 8);
         var10.addString((String)(var3 + ""), 8);
         var10.addString((byte[])var4, 32);
         var10.addString((String)var5, 32);
         var10.addString((String)var7, 1024);
         byte[] var11 = var10.getBytes();
         String var12 = CommonUtils.bString(var9);
         int var13 = var12.indexOf("AAAABBBBCCCCDDDDEEEEFFFF");
         var12 = CommonUtils.replaceAt(var12, CommonUtils.bString(var11), var13);
         var13 = var12.indexOf("AAABBBCCCDDDEEEFFFGGGHHHIIIJJJKKKLLLMMMNNNOOO");
         var12 = CommonUtils.replaceAt(var12, var6 + '\u0000', var13);
         return CommonUtils.toBytes(var12);
      } catch (IOException var14) {
         MudgeSanity.logException("export VPN client", var14, false);
         return new byte[0];
      }
   }
}
