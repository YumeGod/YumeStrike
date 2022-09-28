package beacon;

import common.BeaconEntry;
import common.CommonUtils;
import common.StringStack;

public class Registry {
   public static final short HKLM = 0;
   public static final short HKCR = 1;
   public static final short HKCC = 2;
   public static final short HKCU = 3;
   public static final short HKU = 4;
   private static String[] shortNames = new String[]{"HKLM\\", "HKCR\\", "HKCC\\", "HKCU\\", "HKU\\"};
   private static String[] longNames = new String[]{"HKEY_LOCAL_MACHINE\\", "HKEY_CLASSES_ROOT\\", "HKEY_CURRENT_CONFIG\\", "HKEY_CURRENT_USER\\", "HKEY_USERS\\"};
   public static final short KEY_WOW64_64KEY = 256;
   public static final short KEY_WOW64_32KEY = 512;
   protected String pathv = "";
   protected String value = "";
   protected short hive = -1;
   protected String arch = "";
   protected boolean hasvalue = false;

   protected void parseHive(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.pathv.startsWith(var1[var2])) {
            this.pathv = this.pathv.substring(var1[var2].length());
            this.hive = (short)var2;
            break;
         }
      }

   }

   public String getPath() {
      return this.pathv;
   }

   public String getValue() {
      return this.value;
   }

   public short getHive() {
      return this.hive;
   }

   public Registry(String var1, String var2, boolean var3) {
      this.arch = var1;
      this.hasvalue = var3;
      if (var3) {
         StringStack var4 = new StringStack(var2, " ");
         this.value = var4.pop();
         this.pathv = var4.toString();
      } else {
         this.pathv = var2;
      }

      this.parseHive(shortNames);
      this.parseHive(longNames);
   }

   public short getFlags(BeaconEntry var1) {
      if (var1 != null && "x86".equals(this.arch) && !var1.is64()) {
         CommonUtils.print_stat("Windows 2000 flag rule for " + var1);
         return 0;
      } else {
         return (short)("x86".equals(this.arch) ? 512 : 256);
      }
   }

   public boolean isValid() {
      return this.getError() == null;
   }

   public String getError() {
      if (!this.hasvalue || !"".equals(this.value) && !"".equals(this.pathv)) {
         return this.hive == -1 ? "path must start with HKLM, HKCR, HKCC, HKCU, or HKU" : null;
      } else {
         return "specify a value name too (e.g., HKLM\\foo\\bar Baz)";
      }
   }

   public String toString() {
      if (!this.isValid()) {
         return "[invalid]";
      } else {
         return "".equals(this.value) ? shortNames[this.getHive()] + this.pathv + " (" + this.arch + ")" : shortNames[this.getHive()] + this.pathv + " /v " + this.value + " (" + this.arch + ")";
      }
   }
}
