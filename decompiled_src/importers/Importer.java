package importers;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class Importer {
   protected ImportHandler handler;
   protected HashSet hosts = new HashSet();

   public static List importers(ImportHandler var0) {
      LinkedList var1 = new LinkedList();
      var1.add(new FlatFile(var0));
      var1.add(new NmapXML(var0));
      return var1;
   }

   public Importer(ImportHandler var1) {
      this.handler = var1;
   }

   public void host(String var1, String var2, String var3, double var4) {
      var1 = CommonUtils.trim(var1);
      if (!this.hosts.contains(var1)) {
         this.handler.host(var1, var2, var3, var4);
         this.hosts.add(var1);
      }

   }

   public void service(String var1, String var2, String var3) {
      var1 = CommonUtils.trim(var1);
      this.handler.service(var1, var2, var3);
   }

   public abstract boolean parse(File var1) throws Exception;

   public boolean process(File var1) {
      try {
         if (this.parse(var1)) {
            return true;
         }
      } catch (Exception var3) {
         MudgeSanity.logException("import " + var1, var3, false);
      }

      return false;
   }
}
