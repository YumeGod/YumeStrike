package parser;

import common.BeaconEntry;
import common.CommonUtils;
import java.util.HashMap;
import server.Resources;
import server.ServerUtils;

public class MimikatzCredentials extends Parser {
   public MimikatzCredentials(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return this.isOutput(var2) && var1.indexOf("\nAuthentication Id") >= 0;
   }

   public void parse(String var1, String var2) throws Exception {
      new HashMap();
      new HashMap();
      String var5 = "";
      String var6 = "";
      long var7 = 0L;
      BeaconEntry var9 = ServerUtils.getBeacon(this.resources, var2);
      if (var9 != null) {
         var1 = CommonUtils.strrep(var1, "\r", "");
         String[] var10 = var1.split("\n");

         for(int var11 = 0; var11 < var10.length; ++var11) {
            String var10000 = var10[var11];
            int var13 = var10[var11].indexOf(":");
            if (var13 > 0 && var13 + 1 < var10[var11].length()) {
               String var14 = var10[var11].substring(0, var13);
               String var15 = var10[var11].substring(var13 + 1);
               var14 = CommonUtils.strrep(var14, " ", "");
               var14 = CommonUtils.strrep(var14, "\t", "");
               var15 = var15.trim();
               if (!"(null)".equals(var15)) {
                  if ("*Username".equals(var14)) {
                     var5 = var15;
                  } else if ("User Name".equals(var14)) {
                     var5 = var15;
                  } else if ("*Domain".equals(var14)) {
                     var6 = var15;
                  } else if ("Domain".equals(var14)) {
                     var6 = var15;
                  } else if ("*NTLM".equals(var14) && !var5.endsWith("$") && !"".equals(var5)) {
                     ServerUtils.addCredential(this.resources, var5, var15, var6, "mimikatz", var9.getInternal(), var7);
                  } else if ("*Password".equals(var14) && !var5.endsWith("$") && !"".equals(var5)) {
                     ServerUtils.addCredential(this.resources, var5, var15, var6, "mimikatz", var9.getInternal(), var7);
                  }
               }
            }
         }

         this.resources.call("credentials.push");
      }
   }
}
