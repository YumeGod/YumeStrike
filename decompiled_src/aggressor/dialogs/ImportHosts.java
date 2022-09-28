package aggressor.dialogs;

import aggressor.AggressorClient;
import common.CommonUtils;
import dialog.DialogUtils;
import importers.ImportHandler;
import importers.Importer;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class ImportHosts implements ImportHandler, Runnable {
   protected int hosts = 0;
   protected int services = 0;
   protected AggressorClient client;
   protected String[] files;

   public ImportHosts(AggressorClient var1, String[] var2) {
      this.client = var1;
      this.files = var2;
      (new Thread(this, "import " + var2.length + " file(s)")).start();
   }

   public boolean runForFile(String var1) {
      File var2 = new File(var1);
      Iterator var3 = Importer.importers(this).iterator();

      Importer var4;
      do {
         if (!var3.hasNext()) {
            DialogUtils.showError("Import canceled: " + var2.getName() + " is not a recognized format");
            return false;
         }

         var4 = (Importer)var3.next();
      } while(!var4.process(var2));

      return true;
   }

   public void run() {
      for(int var1 = 0; var1 < this.files.length; ++var1) {
         if (!this.runForFile(this.files[var1])) {
            return;
         }
      }

      this.finish();
   }

   public void finish() {
      if (this.hosts > 0) {
         this.client.getConnection().call("targets.push");
      }

      if (this.services > 0) {
         this.client.getConnection().call("services.push");
      }

      if (this.hosts == 1) {
         DialogUtils.showInfo("Imported " + this.hosts + " host");
      } else {
         DialogUtils.showInfo("Imported " + this.hosts + " hosts");
      }

   }

   public void host(String var1, String var2, String var3, double var4) {
      ++this.hosts;
      HashMap var6 = new HashMap();
      var6.put("address", var1);
      if (var2 != null) {
         var6.put("name", var2);
      }

      if (var3 != null) {
         var6.put("os", var3);
         if (var4 != 0.0) {
            var6.put("version", var4 + "");
         }
      }

      this.client.getConnection().call("targets.update", CommonUtils.args(CommonUtils.TargetKey(var6), var6));
   }

   public void service(String var1, String var2, String var3) {
      ++this.services;
      HashMap var4 = new HashMap();
      var4.put("address", var1);
      var4.put("port", var2);
      if (var3 != null) {
         var4.put("banner", var3);
      }

      this.client.getConnection().call("services.update", CommonUtils.args(CommonUtils.ServiceKey(var4), var4));
   }
}
