package org.dashnine.sleep;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import sleep.engine.ObjectUtilities;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.ScriptWarning;
import sleep.error.YourCodeSucksException;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public class SleepScriptEngine extends AbstractScriptEngine {
   private ScriptEngineFactory factory;
   private ScriptLoader loader = new ScriptLoader();
   private Hashtable sharedEnvironment = new Hashtable();
   private ScriptVariables variables;

   public Object eval(String var1, ScriptContext var2) throws ScriptException {
      ScriptInstance var3 = this.compile(var1, var2);
      return this.evalScript(var3, var2);
   }

   public Object eval(Reader var1, ScriptContext var2) throws ScriptException {
      ScriptInstance var3 = this.compile(this.readFully(var1), var2);
      return this.evalScript(var3, var2);
   }

   private Object evalScript(ScriptInstance var1, ScriptContext var2) {
      Bindings var3 = var2.getBindings(200);
      if (var3 != null) {
         Iterator var4 = var3.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            var1.getScriptVariables().putScalar("$" + var5.getKey().toString(), ObjectUtilities.BuildScalar(true, var5.getValue()));
         }
      }

      Bindings var8 = var2.getBindings(100);
      HashMap var9 = new HashMap();
      if (var8 != null) {
         Iterator var6 = var8.entrySet().iterator();

         while(var6.hasNext()) {
            Map.Entry var7 = (Map.Entry)var6.next();
            var9.put("$" + var7.getKey().toString(), ObjectUtilities.BuildScalar(true, var7.getValue()));
         }
      }

      if (var9.get("$javax.script.filename") != null) {
         var1.getScriptVariables().putScalar("$__SCRIPT__", (Scalar)var9.get("$javax.script.filename"));
      }

      if (var9.get("$javax.script.argv") != null) {
         var1.getScriptVariables().putScalar("@ARGV", (Scalar)var9.get("$javax.script.argv"));
      }

      return SleepUtils.runCode(var1.getRunnableScript(), "eval", var1, SleepUtils.getArgumentStack(var9)).objectValue();
   }

   private ScriptInstance compile(String var1, ScriptContext var2) throws ScriptException {
      try {
         ScriptInstance var3 = this.loader.loadScript("eval", var1, this.sharedEnvironment);
         var3.addWarningWatcher(new WarningWatcher(var2));
         return var3;
      } catch (YourCodeSucksException var4) {
         throw new ScriptException(var4.formatErrors());
      }
   }

   public ScriptEngineFactory getFactory() {
      synchronized(this) {
         if (this.factory == null) {
            this.factory = new SleepScriptEngineFactory();
         }
      }

      return this.factory;
   }

   public Bindings createBindings() {
      return new SimpleBindings();
   }

   void setFactory(ScriptEngineFactory var1) {
      this.factory = var1;
   }

   private String readFully(Reader var1) throws ScriptException {
      StringBuffer var2 = new StringBuffer(8192);

      try {
         BufferedReader var3 = new BufferedReader(var1);

         for(String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
            var2.append("\n");
            var2.append(var4);
         }

         var3.close();
      } catch (Exception var5) {
      }

      return var2.toString();
   }

   private static class WarningWatcher implements RuntimeWarningWatcher {
      protected ScriptContext context;

      public WarningWatcher(ScriptContext var1) {
         this.context = var1;
      }

      public void processScriptWarning(ScriptWarning var1) {
         System.out.println(var1.toString());
      }
   }
}
