package parser;

import common.BeaconEntry;
import common.CommonUtils;
import common.RegexParser;
import server.Resources;
import server.ServerUtils;

public class DcSyncCredentials extends Parser {
   public DcSyncCredentials(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return this.isOutput(var2) && var1.indexOf("\n** SAM ACCOUNT **") >= 0 && var1.indexOf("will be the domain") >= 0;
   }

   public void parse(String var1, String var2) throws Exception {
      String var3 = "";
      String var4 = "";
      String var5 = "";
      BeaconEntry var6 = ServerUtils.getBeacon(this.resources, var2);
      if (var6 != null) {
         var1 = CommonUtils.strrep(var1, "\r", "");
         String[] var7 = var1.split("\n");

         for(int var8 = 0; var8 < var7.length; ++var8) {
            RegexParser var9 = new RegexParser(var7[var8]);
            if (var9.matches(".*?'(.*)' will be the domain.*")) {
               var4 = var9.group(1);
            } else {
               var7[var8] = CommonUtils.strrep(var7[var8], " ", "");
               var7[var8] = CommonUtils.strrep(var7[var8], "\t", "");
               int var10 = var7[var8].indexOf(":");
               if (var10 > 0 && var10 + 1 < var7[var8].length()) {
                  String var11 = var7[var8].substring(0, var10);
                  String var12 = var7[var8].substring(var10 + 1);
                  if ("SAMUsername".equals(var11)) {
                     var3 = var12;
                  } else if ("HashNTLM".equals(var11)) {
                     var5 = var12;
                  }
               }
            }
         }

         if (!"".equals(var3) && !"".equals(var4) && !"".equals(var5)) {
            ServerUtils.addCredential(this.resources, var3, var5, var4, "dcsync", var6.getInternal());
            this.resources.call("credentials.push");
         }

      }
   }
}
