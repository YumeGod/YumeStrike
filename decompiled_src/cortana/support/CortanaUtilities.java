package cortana.support;

import common.CommonUtils;
import cortana.core.EventManager;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.KeyValuePair;
import sleep.bridges.SleepClosure;
import sleep.bridges.io.IOObject;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public class CortanaUtilities implements Function, Loadable {
   public void scriptLoaded(ScriptInstance var1) {
      var1.getScriptEnvironment().getEnvironment().put("&spawn", this);
      var1.getScriptEnvironment().getEnvironment().put("&fork", this);
      var1.getScriptEnvironment().getEnvironment().put("&dispatch_event", this);
      var1.getScriptEnvironment().getEnvironment().put("&apply", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void installVars(ScriptVariables var1, ScriptInstance var2) {
      ScriptVariables var3 = var2.getScriptVariables();
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      final SleepClosure var4;
      ScriptInstance var17;
      if (var1.equals("&fork")) {
         var4 = BridgeUtilities.getFunction(var3, var2);
         var17 = var2.fork();
         var17.installBlock(var4.getRunnableCode());
         ScriptVariables var19 = var17.getScriptVariables();

         while(!var3.isEmpty()) {
            KeyValuePair var21 = BridgeUtilities.getKeyValuePair(var3);
            var19.putScalar(var21.getKey().toString(), SleepUtils.getScalar(var21.getValue()));
         }

         this.installVars(var19, var2);
         IOObject var22 = new IOObject();
         IOObject var23 = new IOObject();

         try {
            PipedInputStream var9 = new PipedInputStream();
            PipedOutputStream var10 = new PipedOutputStream();
            var9.connect(var10);
            PipedInputStream var11 = new PipedInputStream();
            PipedOutputStream var12 = new PipedOutputStream();
            var11.connect(var12);
            var22.openRead(var11);
            var22.openWrite(var10);
            var23.openRead(var9);
            var23.openWrite(var12);
            var17.getScriptVariables().putScalar("$source", SleepUtils.getScalar((Object)var23));
            Thread var13 = new Thread(var17, "fork of " + var17.getRunnableBlock().getSourceLocation());
            var22.setThread(var13);
            var23.setThread(var13);
            var17.setParent(var22);
            var13.start();
         } catch (Exception var14) {
            var2.getScriptEnvironment().flagError(var14);
         }

         return SleepUtils.getScalar((Object)var22);
      } else if (var1.equals("&spawn")) {
         var4 = BridgeUtilities.getFunction(var3, var2);
         var17 = var2.fork();
         var17.installBlock(var4.getRunnableCode());
         Map var18 = Collections.synchronizedMap(new HashMap(var2.getMetadata()));
         var17.getScriptVariables().getGlobalVariables().putScalar("__meta__", SleepUtils.getScalar((Object)var18));
         var17.getMetadata().put("%scriptid%", (long)var17.hashCode() ^ System.currentTimeMillis() * 13L);
         ScriptVariables var20 = var17.getScriptVariables();

         while(!var3.isEmpty()) {
            KeyValuePair var8 = BridgeUtilities.getKeyValuePair(var3);
            var20.putScalar(var8.getKey().toString(), SleepUtils.getScalar(var8.getValue()));
         }

         this.installVars(var20, var2);
         return var17.runScript();
      } else {
         if (var1.equals("&dispatch_event")) {
            var4 = BridgeUtilities.getFunction(var3, var2);
            final Stack var5 = EventManager.shallowCopy(var3);
            CommonUtils.runSafe(new Runnable() {
               public void run() {
                  SleepUtils.runCode((SleepClosure)var4, "&dispatch_event", (ScriptInstance)null, var5);
               }
            });
         } else if (var1.equals("&apply")) {
            String var15 = BridgeUtilities.getString(var3, "");
            if (var15.length() != 0 && var15.charAt(0) == '&') {
               Function var16 = var2.getScriptEnvironment().getFunction(var15);
               if (var16 == null) {
                  throw new RuntimeException("Function '" + var15 + "' does not exist");
               }

               Stack var6 = new Stack();
               Iterator var7 = BridgeUtilities.getIterator(var3, var2);

               while(var7.hasNext()) {
                  var6.add(0, var7.next());
               }

               return SleepUtils.runCode(var16, var15, var2, var6);
            }

            throw new IllegalArgumentException(var1 + ": requested function name must begin with '&'");
         }

         return SleepUtils.getEmptyScalar();
      }
   }
}
