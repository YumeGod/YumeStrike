package aggressor.bridges;

import aggressor.AggressorClient;
import common.AddressList;
import common.CommonUtils;
import common.PowerShellUtils;
import common.RangeList;
import cortana.Cortana;
import dialog.DialogUtils;
import encoders.Base64;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class UtilityBridge implements Function, Loadable {
   protected AggressorClient client;

   public UtilityBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&url_open", this);
      Cortana.put(var1, "&licenseKey", this);
      Cortana.put(var1, "&format_size", this);
      Cortana.put(var1, "&script_resource", this);
      Cortana.put(var1, "&base64_encode", this);
      Cortana.put(var1, "&base64_decode", this);
      Cortana.put(var1, "&str_encode", this);
      Cortana.put(var1, "&str_decode", this);
      Cortana.put(var1, "&powershell_encode_stager", this);
      Cortana.put(var1, "&powershell_encode_oneliner", this);
      Cortana.put(var1, "&powershell_command", this);
      Cortana.put(var1, "&powershell_compress", this);
      Cortana.put(var1, "&gzip", this);
      Cortana.put(var1, "&gunzip", this);
      Cortana.put(var1, "&add_to_clipboard", this);
      Cortana.put(var1, "&range", this);
      Cortana.put(var1, "&iprange", this);
      Cortana.put(var1, "&str_xor", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&url_open")) {
         DialogUtils.gotoURL(BridgeUtilities.getString(var3, "")).actionPerformed((ActionEvent)null);
      } else {
         String var4;
         if (var1.equals("&licenseKey")) {
            var4 = CommonUtils.bString(CommonUtils.readFile((new File(System.getProperty("user.home"), ".cobaltstrike.license")).getAbsolutePath())).trim();
            return SleepUtils.getScalar(var4);
         }

         if (var1.equals("&format_size")) {
            long var12 = BridgeUtilities.getLong(var3, 0L);
            String var6 = "b";
            if (var12 > 1024L) {
               var12 /= 1024L;
               var6 = "kb";
            }

            if (var12 > 1024L) {
               var12 /= 1024L;
               var6 = "mb";
            }

            if (var12 > 1024L) {
               var12 /= 1024L;
               var6 = "gb";
            }

            return SleepUtils.getScalar(var12 + var6);
         }

         if (var1.equals("&script_resource")) {
            return SleepUtils.getScalar((new File((new File(var2.getName())).getParent(), BridgeUtilities.getString(var3, ""))).getAbsolutePath());
         }

         byte[] var7;
         if (var1.equals("&base64_encode")) {
            var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
            return SleepUtils.getScalar(Base64.encode(var7));
         }

         if (var1.equals("&base64_decode")) {
            var4 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getScalar(Base64.decode(var4));
         }

         if (var1.equals("&powershell_encode_stager")) {
            var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
            return SleepUtils.getScalar((new PowerShellUtils(this.client)).encodePowerShellCommand(var7));
         }

         if (var1.equals("&powershell_encode_oneliner")) {
            var4 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getScalar(CommonUtils.EncodePowerShellOneLiner(var4));
         }

         if (var1.equals("&powershell_command")) {
            var4 = BridgeUtilities.getString(var3, "");
            boolean var11 = SleepUtils.isTrueScalar(BridgeUtilities.getScalar(var3));
            return SleepUtils.getScalar((new PowerShellUtils(this.client)).format(var4, var11));
         }

         if (var1.equals("&powershell_compress")) {
            var4 = BridgeUtilities.getString(var3, "");
            return SleepUtils.getScalar((new PowerShellUtils(this.client)).PowerShellCompress(CommonUtils.toBytes(var4)));
         }

         if (var1.equals("&gzip")) {
            var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
            return SleepUtils.getScalar(CommonUtils.gzip(var7));
         }

         if (var1.equals("&gunzip")) {
            var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
            return SleepUtils.getScalar(CommonUtils.gunzip(var7));
         }

         if (var1.equals("&add_to_clipboard")) {
            var4 = BridgeUtilities.getString(var3, "");
            DialogUtils.addToClipboard(var4);
         } else {
            if (var1.equals("&range")) {
               var4 = BridgeUtilities.getString(var3, "");
               RangeList var10 = new RangeList(var4);
               if (var10.hasError()) {
                  throw new RuntimeException(var10.getError());
               }

               return SleepUtils.getArrayWrapper(var10.toList());
            }

            if (var1.equals("&iprange")) {
               var4 = BridgeUtilities.getString(var3, "");
               AddressList var9 = new AddressList(var4);
               if (var9.hasError()) {
                  throw new RuntimeException(var9.getError());
               }

               return SleepUtils.getArrayWrapper(var9.toList());
            }

            String var8;
            if (var1.equals("&str_encode")) {
               var4 = BridgeUtilities.getString(var3, "");
               var8 = BridgeUtilities.getString(var3, "");
               return SleepUtils.getScalar(CommonUtils.toBytes(var4, var8));
            }

            if (var1.equals("&str_decode")) {
               var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var8 = BridgeUtilities.getString(var3, "");
               return SleepUtils.getScalar(CommonUtils.bString(var7, var8));
            }

            if (var1.equals("&str_xor")) {
               var7 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               byte[] var5 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               return SleepUtils.getScalar(CommonUtils.XorString(var7, var5));
            }
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
