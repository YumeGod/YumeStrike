package parser;

import common.CommonUtils;
import java.util.HashMap;
import java.util.Map;
import server.Resources;

public class NetViewResults extends Parser {
   private static final String IPADDR = "\\d+\\.\\d+\\.\\d+\\.\\d+";

   public NetViewResults(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return var2 == 24;
   }

   public Map host(String var1, String var2, String var3) {
      var1 = CommonUtils.trim(var1);
      HashMap var4 = new HashMap();
      var4.put("address", var1);
      var4.put("name", var2);
      var4.put("os", "Windows");
      var4.put("version", var3);
      return var4;
   }

   public Map host(String var1, String var2) {
      var1 = CommonUtils.trim(var1);
      HashMap var3 = new HashMap();
      var3.put("address", var1);
      var3.put("name", var2);
      return var3;
   }

   public void parse(String var1, String var2) throws Exception {
      String[] var3 = var1.split("\n");
      boolean var4 = false;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         String[] var6 = var3[var5].trim().split("\\s+");
         Map var7;
         String var8;
         if (var6.length >= 4 && var6[1].matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
            var7 = this.host(var6[1], var6[0], var6[3]);
            var8 = CommonUtils.TargetKey(var7);
            this.resources.call("targets.update", CommonUtils.args(var8, var7));
            var4 = true;
         } else if (var6.length == 2 && var6[1].matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
            var7 = this.host(var6[1], var6[0]);
            var8 = CommonUtils.TargetKey(var7);
            this.resources.call("targets.update", CommonUtils.args(var8, var7));
            var4 = true;
         }
      }

      if (var4) {
         this.resources.call("targets.push");
      }

   }
}
