package parser;

import common.BeaconEntry;
import common.CommonUtils;
import server.Resources;
import server.ServerUtils;

public class MimikatzSamDump extends Parser {
   public MimikatzSamDump(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return this.isOutput(var2) && var1.indexOf("RID") > 0 && var1.indexOf("User : ") > 0 && var1.indexOf("NTLM :") > 0 && var1.indexOf("LM") > 0;
   }

   public void parse(String var1, String var2) throws Exception {
      String var3 = "";
      String var4 = "";
      BeaconEntry var5 = ServerUtils.getBeacon(this.resources, var2);
      if (var5 != null) {
         var1 = CommonUtils.strrep(var1, "\r", "");
         String[] var6 = var1.split("\n");

         for(int var7 = 0; var7 < var6.length; ++var7) {
            String var10000 = var6[var7];
            int var9 = var6[var7].indexOf(":");
            if (var9 > 0 && var9 + 1 < var6[var7].length()) {
               String var10 = var6[var7].substring(0, var9);
               String var11 = var6[var7].substring(var9 + 1);
               var10 = CommonUtils.strrep(var10, " ", "");
               var10 = CommonUtils.strrep(var10, "\t", "");
               var11 = var11.trim();
               if ("User".equals(var10)) {
                  var3 = var11;
               } else if ("NTLM".equals(var10) && !"".equals(var11)) {
                  ServerUtils.addCredential(this.resources, var3, var11, var5.getComputer(), "mimikatz", var5.getInternal());
               }
            }
         }

         this.resources.call("credentials.push");
      }
   }
}
