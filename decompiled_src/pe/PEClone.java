package pe;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class PEClone {
   public void start(String var1) {
      try {
         this._start(var1);
      } catch (Exception var3) {
         MudgeSanity.logException("Error cloning headers of " + var1, var3, false);
      }

   }

   public void out(String var1) {
      System.out.println(var1);
   }

   public void set(String var1, long var2) {
      this.set(var1, var2 + "");
   }

   public void set(String var1, byte[] var2) {
      if (var2.length != 0) {
         this.set(var1, CommonUtils.toAggressorScriptHexString(var2));
      }
   }

   public void set(String var1, String var2) {
      if (var2 != null) {
         System.out.println("\tset " + var1 + " \"" + var2 + "\";");
      }

   }

   public void _start(String var1) throws Exception {
      File var2 = new File(var1);
      PEParser var3 = PEParser.load((InputStream)(new FileInputStream(var2)));
      this.out("# ./peclone " + (new File(var1)).getName());
      this.out("stage {");
      this.set("checksum      ", (long)var3.get("Checksum"));
      this.set("compile_time  ", CommonUtils.formatDateAny("dd MMM yyyy HH:mm:ss", var3.getDate("TimeDateStamp").getTime()));
      this.set("entry_point   ", (long)var3.get("AddressOfEntryPoint"));
      if (var3.get("SizeOfImage") > 307200) {
         this.set("image_size_x86", (long)var3.get("SizeOfImage"));
         this.set("image_size_x64", (long)var3.get("SizeOfImage"));
      }

      this.set("name          ", var3.getString("Export.Name"));
      this.set("rich_header   ", var3.getRichHeader());
      this.out("}");
   }
}
