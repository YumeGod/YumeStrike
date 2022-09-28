package parser;

import common.BeaconEntry;
import common.CommonUtils;
import common.RegexParser;
import server.Resources;
import server.ServerUtils;

public class MimikatzDcSyncCSV extends Parser {
   public MimikatzDcSyncCSV(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return this.isOutput(var2) && var1.indexOf("will be the domain") > 0 && var1.indexOf("will be the DC server") > 0 && var1.indexOf("Exporting domain") > 0;
   }

   public void parse(String var1, String var2) throws Exception {
      BeaconEntry var3 = ServerUtils.getBeacon(this.resources, var2);
      if (var3 != null) {
         String var4 = var3.getComputer();
         var1 = CommonUtils.strrep(var1, "\r", "");
         String[] var5 = var1.split("\n");

         for(int var6 = 0; var6 < var5.length; ++var6) {
            String var7 = var5[var6];
            if (var7.endsWith("' will be the domain")) {
               RegexParser var8 = new RegexParser(var7);
               if (var8.matches(".*? '(.*?)' will be the domain")) {
                  var4 = var8.group(1);
               }
            }

            String[] var9 = var7.split("\t");
            if (var9.length == 3 && CommonUtils.isNumber(var9[0]) && !var9[1].endsWith("$")) {
               ServerUtils.addCredential(this.resources, var9[1], var9[2], var4, "mimikatz", var3.getInternal());
            }
         }

         this.resources.call("credentials.push");
      }
   }
}
