package sleep.runtime;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.bridges.io.IOObject;
import sleep.engine.Block;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.ScriptWarning;
import sleep.interfaces.Function;
import sleep.interfaces.Variable;

public class ScriptInstance implements Serializable, Runnable {
   protected String name;
   protected boolean loaded;
   protected LinkedList watchers;
   protected ScriptEnvironment environment;
   protected ScriptVariables variables;
   protected SleepClosure script;
   public static final int DEBUG_NONE = 0;
   public static final int DEBUG_SHOW_ERRORS = 1;
   public static final int DEBUG_SHOW_WARNINGS = 2;
   public static final int DEBUG_REQUIRE_STRICT = 4;
   public static final int DEBUG_TRACE_CALLS = 8;
   public static final int DEBUG_TRACE_PROFILE_ONLY = 24;
   protected static final int DEBUG_TRACE_SUPPRESS = 16;
   public static final int DEBUG_THROW_WARNINGS = 34;
   public static final int DEBUG_TRACE_LOGIC = 64;
   public static final int DEBUG_TRACE_TAINT = 128;
   protected int debug;
   protected long loadTime;
   protected List sourceFiles;
   protected IOObject parent;

   public void associateFile(File var1) {
      if (var1.exists()) {
         this.sourceFiles.add(var1);
      }

   }

   public boolean hasChanged() {
      Iterator var1 = this.sourceFiles.iterator();

      File var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (File)var1.next();
      } while(var2.lastModified() <= this.loadTime);

      return true;
   }

   public void setDebugFlags(int var1) {
      this.debug = var1;
   }

   public int getDebugFlags() {
      return this.debug;
   }

   public ScriptInstance(Hashtable var1) {
      this((Variable)null, var1);
   }

   public ScriptInstance(Variable var1, Hashtable var2) {
      this.name = "Script";
      this.watchers = new LinkedList();
      this.debug = 1;
      this.loadTime = System.currentTimeMillis();
      this.sourceFiles = new LinkedList();
      this.parent = null;
      if (var2 == null) {
         var2 = new Hashtable();
      }

      if (var1 == null) {
         this.variables = new ScriptVariables();
      } else {
         this.variables = new ScriptVariables(var1);
      }

      this.environment = new ScriptEnvironment(var2, this);
      this.loaded = true;
   }

   public void installBlock(Block var1) {
      this.script = new SleepClosure(this, var1);
   }

   public ScriptInstance() {
      this((Variable)null, (Hashtable)null);
   }

   public ScriptEnvironment getScriptEnvironment() {
      return this.environment;
   }

   public void setScriptVariables(ScriptVariables var1) {
      this.variables = var1;
   }

   public ScriptVariables getScriptVariables() {
      return this.variables;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Scalar runScript() {
      return SleepUtils.runCode((SleepClosure)this.script, (String)null, this, (Stack)null);
   }

   public void recordStackFrame(String var1, String var2, int var3) {
      Object var4 = (List)this.getScriptEnvironment().getEnvironment().get("%strace%");
      if (var4 == null) {
         var4 = new LinkedList();
         this.getScriptEnvironment().getEnvironment().put("%strace%", var4);
      }

      SleepStackElement var5 = new SleepStackElement();
      var5.sourcefile = var2;
      var5.description = var1;
      var5.lineNumber = var3;
      ((List)var4).add(0, var5);
   }

   public File cwd() {
      if (!this.getMetadata().containsKey("__CWD__")) {
         this.chdir((File)null);
      }

      return (File)this.getMetadata().get("__CWD__");
   }

   public void chdir(File var1) {
      if (var1 == null) {
         var1 = new File("");
      }

      this.getMetadata().put("__CWD__", var1.getAbsoluteFile());
   }

   public void recordStackFrame(String var1, int var2) {
      this.recordStackFrame(var1, this.getScriptEnvironment().getCurrentSource(), var2);
   }

   public void clearStackTrace() {
      LinkedList var1 = new LinkedList();
      this.getScriptEnvironment().getEnvironment().put("%strace%", var1);
   }

   public List getStackTrace() {
      Object var1 = (List)this.getScriptEnvironment().getEnvironment().get("%strace%");
      this.clearStackTrace();
      if (var1 == null) {
         var1 = new LinkedList();
      }

      return (List)var1;
   }

   public long total() {
      Long var1 = (Long)this.getMetadata().get("%total%");
      return var1 == null ? 0L : var1;
   }

   public void collect(String var1, int var2, long var3) {
      Object var5 = (Map)this.getMetadata().get("%statistics%");
      Long var6 = (Long)this.getMetadata().get("%total%");
      if (var5 == null) {
         var5 = new HashMap();
         var6 = new Long(0L);
         this.getMetadata().put("%statistics%", var5);
         this.getMetadata().put("%total%", var6);
      }

      ProfilerStatistic var7 = (ProfilerStatistic)((Map)var5).get(var1);
      if (var7 == null) {
         var7 = new ProfilerStatistic();
         var7.functionName = var1;
         ((Map)var5).put(var1, var7);
      }

      var7.ticks += var3;
      ++var7.calls;
      this.getMetadata().put("%total%", new Long(var6 + var3));
   }

   public boolean isProfileOnly() {
      return (this.getDebugFlags() & 24) == 24;
   }

   public List getProfilerStatistics() {
      Map var1 = (Map)this.getMetadata().get("%statistics%");
      if (var1 != null) {
         LinkedList var2 = new LinkedList(var1.values());
         Collections.sort(var2);
         return var2;
      } else {
         return new LinkedList();
      }
   }

   public Map getMetadata() {
      Scalar var1 = this.getScriptVariables().getGlobalVariables().getScalar("__meta__");
      Map var2 = null;
      if (var1 == null) {
         var2 = Collections.synchronizedMap(new HashMap());
         this.getScriptVariables().getGlobalVariables().putScalar("__meta__", SleepUtils.getScalar((Object)var2));
      } else {
         var2 = (Map)var1.objectValue();
      }

      return var2;
   }

   public void printProfileStatistics(OutputStream var1) {
      PrintWriter var2 = new PrintWriter(var1, true);
      Iterator var3 = this.getProfilerStatistics().iterator();

      while(var3.hasNext()) {
         String var4 = var3.next().toString();
         var2.println(var4);
      }

   }

   public void makeSafe() {
      Hashtable var1 = this.environment.getEnvironment();
      Hashtable var2 = new Hashtable(var1.size() * 2 - 1);
      Iterator var3 = var1.entrySet().iterator();

      while(true) {
         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (var4.getKey().toString().charAt(0) == '&' && var4.getValue() instanceof SleepClosure) {
               SleepClosure var5 = new SleepClosure(this, ((SleepClosure)var4.getValue()).getRunnableCode());
               var2.put(var4.getKey(), var5);
            } else {
               var2.put(var4.getKey(), var4.getValue());
            }
         }

         this.environment.setEnvironment(var2);
         return;
      }
   }

   public ScriptInstance fork() {
      ScriptInstance var1 = new ScriptInstance(this.variables.getGlobalVariables().createInternalVariableContainer(), this.environment.getEnvironment());
      var1.makeSafe();
      var1.setName(this.getName());
      var1.setDebugFlags(this.getDebugFlags());
      var1.watchers = this.watchers;
      var1.getScriptVariables().getGlobalVariables().putScalar("__meta__", SleepUtils.getScalar((Object)this.getMetadata()));
      return var1;
   }

   public void run() {
      Scalar var1 = this.runScript();
      if (this.parent != null) {
         this.parent.setToken(var1);
      }

   }

   public void setParent(IOObject var1) {
      this.parent = var1;
   }

   public Block getRunnableBlock() {
      return this.script.getRunnableCode();
   }

   public SleepClosure getRunnableScript() {
      return this.script;
   }

   public Scalar callFunction(String var1, Stack var2) {
      Function var3 = this.getScriptEnvironment().getFunction(var1);
      if (var3 == null) {
         return null;
      } else {
         Scalar var4 = var3.evaluate(var1, this, var2);
         this.getScriptEnvironment().resetEnvironment();
         return var4;
      }
   }

   public void setUnloaded() {
      this.loaded = false;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public void addWarningWatcher(RuntimeWarningWatcher var1) {
      this.watchers.add(var1);
   }

   public void removeWarningWatcher(RuntimeWarningWatcher var1) {
      this.watchers.remove(var1);
   }

   public void fireWarning(String var1, int var2) {
      this.fireWarning(var1, var2, false);
   }

   public void fireWarning(String var1, int var2, boolean var3) {
      if (this.debug != 0 && (!var3 || (this.getDebugFlags() & 16) != 16)) {
         ScriptWarning var4 = new ScriptWarning(this, var1, var2, var3);
         Iterator var5 = this.watchers.iterator();

         while(var5.hasNext()) {
            ((RuntimeWarningWatcher)var5.next()).processScriptWarning(var4);
         }
      }

   }

   public static class ProfilerStatistic implements Comparable, Serializable {
      public String functionName;
      public long ticks = 0L;
      public long calls = 0L;

      public int compareTo(Object var1) {
         return (int)(((ProfilerStatistic)var1).ticks - this.ticks);
      }

      public String toString() {
         return (double)this.ticks / 1000.0 + "s " + this.calls + " " + this.functionName;
      }
   }

   public static class SleepStackElement implements Serializable {
      public String sourcefile;
      public String description;
      public int lineNumber;

      public String toString() {
         return "   " + (new File(this.sourcefile)).getName() + ":" + this.lineNumber + " " + this.description;
      }
   }
}
