package cortana;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import sleep.engine.Block;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;

public class Loader implements Loadable {
   protected ScriptLoader loader = new ScriptLoader();
   protected Hashtable shared = new Hashtable();
   protected ScriptVariables vars = new ScriptVariables();
   protected Object[] passMe = new Object[4];
   protected List scripts = new LinkedList();
   protected RuntimeWarningWatcher watcher;

   public void unsetDebugLevel(int var1) {
      Iterator var2 = this.scripts.iterator();

      while(var2.hasNext()) {
         ScriptInstance var3 = (ScriptInstance)var2.next();
         int var4 = var3.getDebugFlags() & ~var1;
         var3.setDebugFlags(var4);
      }

   }

   public void printProfile(OutputStream var1) {
      Iterator var2 = this.scripts.iterator();
      if (var2.hasNext()) {
         ScriptInstance var3 = (ScriptInstance)var2.next();
         var3.printProfileStatistics(var1);
      }
   }

   public void setDebugLevel(int var1) {
      Iterator var2 = this.scripts.iterator();

      while(var2.hasNext()) {
         ScriptInstance var3 = (ScriptInstance)var2.next();
         int var4 = var3.getDebugFlags() | var1;
         var3.setDebugFlags(var4);
      }

   }

   public boolean isReady() {
      synchronized(this) {
         return this.passMe != null;
      }
   }

   public void passObjects(Object var1, Object var2, Object var3, Object var4) {
      synchronized(this) {
         this.passMe[0] = var1;
         this.passMe[1] = var2;
         this.passMe[2] = var3;
         this.passMe[3] = var4;
      }
   }

   public Object[] getPassedObjects() {
      synchronized(this) {
         return this.passMe;
      }
   }

   public void setGlobal(String var1, Scalar var2) {
      this.vars.getGlobalVariables().putScalar(var1, var2);
   }

   public ScriptLoader getScriptLoader() {
      return this.loader;
   }

   public Loader(RuntimeWarningWatcher var1) {
      this.loader.addSpecificBridge(this);
      this.watcher = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      var1.setScriptVariables(this.vars);
      var1.addWarningWatcher(this.watcher);
      this.scripts.add(var1);
      var1.getMetadata().put("%scriptid%", var1.hashCode());
   }

   public void unload() {
      Iterator var1 = this.scripts.iterator();

      while(var1.hasNext()) {
         ScriptInstance var2 = (ScriptInstance)var1.next();
         var2.setUnloaded();
      }

      this.scripts = null;
      this.vars = null;
      this.shared = null;
      this.passMe = null;
      this.loader = null;
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Object loadInternalScript(String var1, Object var2) {
      try {
         if (var2 == null) {
            InputStream var3 = this.getClass().getClassLoader().getResourceAsStream(var1);
            if (var3 == null) {
               throw new RuntimeException("resource " + var1 + " does not exist");
            }

            var2 = this.loader.compileScript(var1, var3);
         }

         ScriptInstance var6 = this.loader.loadScript(var1, (Block)var2, this.shared);
         var6.runScript();
      } catch (IOException var4) {
         System.err.println("*** Could not load: " + var1 + " - " + var4.getMessage());
      } catch (YourCodeSucksException var5) {
         var5.printErrors(System.out);
      }

      return var2;
   }

   public ScriptInstance loadScript(String var1) throws IOException {
      this.setGlobal("$__script__", SleepUtils.getScalar(var1));
      ScriptInstance var2 = this.loader.loadScript(var1, this.shared);
      var2.runScript();
      return var2;
   }

   public ScriptInstance loadScript(String var1, InputStream var2) throws IOException {
      this.setGlobal("$__script__", SleepUtils.getScalar(var1));
      ScriptInstance var3 = this.loader.loadScript(var1, var2, this.shared);
      var3.runScript();
      return var3;
   }
}
