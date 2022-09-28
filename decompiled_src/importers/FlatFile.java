package importers;

import common.CommonUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FlatFile extends Importer {
   public FlatFile(ImportHandler var1) {
      super(var1);
   }

   public boolean isHostAndPort(String var1) {
      String[] var2 = var1.split(":");
      if (var2.length == 2 && CommonUtils.isIP(var2[0])) {
         this.host(var2[0], (String)null, (String)null, 0.0);
         this.service(var2[0], var2[1], (String)null);
         return true;
      } else {
         return false;
      }
   }

   public boolean parse(File var1) throws Exception {
      BufferedReader var2 = new BufferedReader(new FileReader(var1));

      for(String var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
         var3 = var3.trim();
         if (!var3.startsWith("# ")) {
            if (CommonUtils.isIP(var3)) {
               this.host(var3, (String)null, (String)null, 0.0);
            } else if (!this.isHostAndPort(var3) && !"".equals(var3)) {
               var2.close();
               return false;
            }
         }
      }

      return true;
   }
}
