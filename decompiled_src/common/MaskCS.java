package common;

import dns.SleeveSecurity;
import java.io.File;

public class MaskCS {
   protected SleeveSecurity crypto;

   public MaskCS() {
      byte[] var1 = CommonUtils.toBytes("aliens");
      this.crypto = new SleeveSecurity();
      this.crypto.registerKey(var1);
      this.processFile("./cobaltstrike.jar");
   }

   public void processFile(String var1) {
      byte[] var2 = CommonUtils.readFile(var1);
      byte[] var3 = CommonUtils.MD5(var2);
      byte[] var4 = this.crypto.encrypt(var2);
      CommonUtils.writeToFile(new File(var1 + ".mask"), var4);
      CommonUtils.print_stat("Wrote '" + var1 + "' " + var4.length + " bytes");
   }

   public static void main(String[] var0) {
      new MaskCS();
   }
}
