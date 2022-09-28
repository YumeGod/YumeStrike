package importers;

import common.CommonUtils;
import java.io.File;
import java.util.Iterator;

public class TestImporters implements ImportHandler {
   public TestImporters(File var1) {
      this.go(var1);
   }

   public void go(File var1) {
      Iterator var2 = Importer.importers(this).iterator();

      while(var2.hasNext()) {
         Importer var3 = (Importer)var2.next();
         if (var3.process(var1)) {
            break;
         }
      }

      CommonUtils.print_info("Done!");
   }

   public void host(String var1, String var2, String var3, double var4) {
      StringBuffer var6 = new StringBuffer();
      var6.append("host: " + var1);
      if (var2 != null) {
         var6.append(" / " + var2);
      }

      if (var3 != null) {
         var6.append(" (" + var3 + " " + var4 + ")");
      }

      CommonUtils.print_good(var6.toString());
   }

   public void service(String var1, String var2, String var3) {
      if (var3 != null) {
         CommonUtils.print_info(var1 + ":" + var2 + " - " + var3);
      } else {
         CommonUtils.print_info(var1 + ":" + var2);
      }

   }

   public static void main(String[] var0) {
      File var1 = new File(var0[0]);
      new TestImporters(var1);
   }
}
