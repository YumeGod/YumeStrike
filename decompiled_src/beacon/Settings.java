package beacon;

import common.AssertUtils;
import common.CommonUtils;
import common.Packer;

public class Settings {
   public static final int PATCH_SIZE = 4096;
   public static final int MAX_SETTINGS = 64;
   public static final int TYPE_NONE = 0;
   public static final int TYPE_SHORT = 1;
   public static final int TYPE_INT = 2;
   public static final int TYPE_PTR = 3;
   protected Packer patch = new Packer();

   public void addShort(int var1, int var2) {
      AssertUtils.TestRange(var1, 0, 64);
      this.patch.addShort(var1);
      this.patch.addShort(1);
      this.patch.addShort(2);
      this.patch.addShort(var2);
   }

   public void addInt(int var1, int var2) {
      AssertUtils.TestRange(var1, 0, 64);
      this.patch.addShort(var1);
      this.patch.addShort(2);
      this.patch.addShort(4);
      this.patch.addInt(var2);
   }

   public void addData(int var1, byte[] var2, int var3) {
      AssertUtils.TestRange(var1, 0, 64);
      this.patch.addShort(var1);
      this.patch.addShort(3);
      this.patch.addShort(var3);
      this.patch.addString(var2, var3);
   }

   public void addString(int var1, String var2, int var3) {
      this.addData(var1, CommonUtils.toBytes(var2), var3);
   }

   public byte[] toPatch() {
      return this.toPatch(4096);
   }

   public byte[] toPatch(int var1) {
      this.patch.addShort(0);
      byte[] var2 = this.patch.getBytes();
      AssertUtils.Test(var2.length < var1, "Patch " + var2.length + " bytes is too large! Beacon will crash");
      byte[] var3 = CommonUtils.randomData(var1 - var2.length);
      this.patch.addString(var3, var3.length);
      return this.patch.getBytes();
   }
}
