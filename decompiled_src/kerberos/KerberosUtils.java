package kerberos;

import common.CommonUtils;
import common.MudgeSanity;
import java.util.Hashtable;
import java.util.Stack;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.SleepUtils;

public class KerberosUtils {
   private static ScriptInstance converter = null;

   public static byte[] ConvertCCacheToKrbCred(String var0) {
      Class var1 = KerberosUtils.class;
      synchronized(KerberosUtils.class) {
         if (converter == null) {
            try {
               ScriptLoader var2 = new ScriptLoader();
               converter = var2.loadScript("ccache_krbcred.sl", CommonUtils.resource("resources/ccache_krbcred.sl"), new Hashtable());
               converter.runScript();
            } catch (Exception var5) {
               MudgeSanity.logException("compile converter", var5, false);
               return new byte[0];
            }
         }

         Stack var7 = new Stack();
         var7.push(SleepUtils.getScalar(var0));
         Scalar var3 = converter.callFunction("&convert", var7);
         return CommonUtils.toBytes(var3.toString());
      }
   }
}
