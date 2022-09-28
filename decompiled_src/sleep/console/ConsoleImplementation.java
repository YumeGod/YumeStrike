package sleep.console;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import sleep.bridges.DefaultVariable;
import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.error.RuntimeWarningWatcher;
import sleep.error.ScriptWarning;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Loadable;
import sleep.interfaces.Variable;
import sleep.parser.ImportManager;
import sleep.parser.Parser;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.ScriptVariables;

public class ConsoleImplementation implements RuntimeWarningWatcher, Loadable, ConsoleProxy {
   private ScriptInstance script;
   private ConsoleProxy myProxy;
   private Hashtable sharedEnvironment;
   private Variable sharedVariables;
   private ScriptLoader loader;
   private ImportManager imports;
   private boolean interact;

   public ConsoleImplementation() {
      this(new Hashtable(), new DefaultVariable(), new ScriptLoader());
   }

   public ConsoleImplementation(Hashtable var1, Variable var2, ScriptLoader var3) {
      this.interact = true;
      if (var1 == null) {
         var1 = new Hashtable();
      }

      if (var2 == null) {
         var2 = new DefaultVariable();
      }

      if (var3 == null) {
         var3 = new ScriptLoader();
      }

      this.sharedEnvironment = var1;
      this.sharedVariables = (Variable)var2;
      this.loader = var3;
      this.loader.addSpecificBridge(this);
      this.setProxy(this);
   }

   public ConsoleProxy getProxy() {
      return this.myProxy;
   }

   public void setProxy(ConsoleProxy var1) {
      this.myProxy = var1;
   }

   public void consolePrint(String var1) {
   }

   public String consoleReadln() {
      return null;
   }

   public void consolePrintln(Object var1) {
   }

   public void rppl() throws IOException {
      this.getProxy().consolePrintln(">> Welcome to the Sleep scripting language");
      this.interact = false;
      StringBuffer var2 = new StringBuffer();
      String var3 = "";

      label141:
      while(true) {
         while(true) {
            if (!this.interact) {
               this.getProxy().consolePrint("> ");
            }

            String var1 = this.getProxy().consoleReadln();
            if (!this.interact) {
               if (var1 == null) {
                  this.getProxy().consolePrintln("Good bye!");
                  this.setProxy(this);
                  break label141;
               }

               String var4;
               String var5;
               if (var1.indexOf(32) > -1) {
                  var4 = var1.substring(0, var1.indexOf(32));
                  var5 = var1.substring(var4.length() + 1, var1.length());
               } else {
                  var4 = var1;
                  var5 = null;
               }

               if (var4.equals("env")) {
                  String var6;
                  if (var5 != null && var5.indexOf(32) > -1) {
                     var6 = var5.substring(var5.indexOf(32) + 1, var5.length());
                     var5 = var5.substring(0, var5.indexOf(32));
                  } else {
                     var6 = null;
                  }

                  this.env(var5, var6);
               } else if (var4.equals("version")) {
                  this.getProxy().consolePrintln("Sleep 2.1 (20090430)");
               } else if (var4.equals("help") && var5 != null) {
                  this.help(var5);
               } else if (var4.equals("help")) {
                  this.help();
               } else if (var4.equals("interact")) {
                  this.interact();
               } else if (var4.equals("list")) {
                  this.list();
               } else if (var4.equals("debug") && var5 != null) {
                  String[] var8 = var5.split(" ");
                  if (var8.length == 2) {
                     this.debug(var8[0], Integer.parseInt(var8[1]));
                  } else if (var8.length == 1) {
                     this.debug((String)null, Integer.parseInt(var8[0]));
                  } else {
                     this.getProxy().consolePrintln("Invalid usage: debug [script] <level>");
                  }
               } else if (var4.equals("load") && var5 != null) {
                  this.load(var5);
               } else if (!var4.equals("tree") || var5 == null && this.script == null) {
                  if (var4.equals("unload") && var5 != null) {
                     this.unload(var5);
                  } else {
                     Scalar var7;
                     if (var4.equals("x") && var5 != null) {
                        var7 = this.eval("return " + var5 + ";", var5);
                        if (var7 != null) {
                           this.getProxy().consolePrintln(var7 + "");
                        }
                     } else if (var4.equals("?") && var5 != null) {
                        var7 = this.eval("return iff(" + var5 + ", 'true', 'false');", var5);
                        if (var7 != null) {
                           this.getProxy().consolePrintln(var7 + "");
                        }
                     } else {
                        if (var4.equals("quit") || var4.equals("exit") || var4.equals("done")) {
                           this.getProxy().consolePrintln("Good bye!");
                           this.setProxy(this);
                           break label141;
                        }

                        if (var4.trim().length() > 0) {
                           this.getProxy().consolePrintln("Command '" + var4 + "' not understood.  Type 'help' if you need it");
                        }
                     }
                  }
               } else {
                  this.tree(var5);
               }
            } else if (var1 != null && !var1.equals("done")) {
               if (var1.equals(".")) {
                  if (var2.length() == 0) {
                     this.eval(var3, var3);
                  } else {
                     this.eval(var2.toString(), var2.toString());
                     var3 = var2.toString();
                     var2 = new StringBuffer();
                  }
               } else {
                  var2.append(var1 + "\n");
               }
            } else {
               this.interact = false;
            }
         }
      }

      this.interact = true;
   }

   private void help() {
      this.getProxy().consolePrintln("debug [script] <level>");
      this.getProxy().consolePrintln("env [functions/other] [regex filter]");
      this.getProxy().consolePrintln("help [command]");
      this.getProxy().consolePrintln("interact");
      this.getProxy().consolePrintln("list");
      this.getProxy().consolePrintln("load <file>");
      this.getProxy().consolePrintln("unload <file>");
      this.getProxy().consolePrintln("tree [key]");
      this.getProxy().consolePrintln("quit");
      this.getProxy().consolePrintln("version");
      this.getProxy().consolePrintln("x <expression>");
      this.getProxy().consolePrintln("? <predicate expression>");
   }

   private void help(String var1) {
      if (var1.equals("debug")) {
         this.getProxy().consolePrintln("debug [script] <level>");
         this.getProxy().consolePrintln("   sets the debug level for the specified script");
         this.getProxy().consolePrintln("   1 - show critical errors");
         this.getProxy().consolePrintln("   2 - show warnings");
         this.getProxy().consolePrintln("   4 - strict mode, complain about non-declared variables");
         this.getProxy().consolePrintln("   8 - trace all function calls");
         this.getProxy().consolePrintln("   to combine options, add their numbers together");
      } else if (var1.equals("env")) {
         this.getProxy().consolePrintln("env [functions/other] [regex filter]");
         this.getProxy().consolePrintln("   dumps the shared environment, filters output with specified regex");
      } else if (var1.equals("interact")) {
         this.getProxy().consolePrintln("interact");
         this.getProxy().consolePrintln("   enters the console into interactive mode.");
      } else if (var1.equals("list")) {
         this.getProxy().consolePrintln("list");
         this.getProxy().consolePrintln("   lists all of the currently loaded scripts");
      } else if (var1.equals("load")) {
         this.getProxy().consolePrintln("load <file>");
         this.getProxy().consolePrintln("   loads a script file into the script loader");
      } else if (var1.equals("unload")) {
         this.getProxy().consolePrintln("unload <file>");
         this.getProxy().consolePrintln("   unloads a script file from the script loader");
      } else if (var1.equals("tree")) {
         this.getProxy().consolePrintln("tree [key]");
         this.getProxy().consolePrintln("   displays the Abstract Syntax Tree for the specified key");
      } else if (var1.equals("quit")) {
         this.getProxy().consolePrintln("quit");
         this.getProxy().consolePrintln("   stops the console");
      } else if (var1.equals("version")) {
         this.getProxy().consolePrintln("version");
         this.getProxy().consolePrintln("   display the current Sleep version");
      } else if (var1.equals("x")) {
         this.getProxy().consolePrintln("x <expression>");
         this.getProxy().consolePrintln("   evaluates a sleep expression and displays the value");
      } else if (var1.equals("?")) {
         this.getProxy().consolePrintln("? <predicate expression>");
         this.getProxy().consolePrintln("   evaluates a sleep predicate expression and displays the truth value");
      } else {
         this.getProxy().consolePrintln("help [command]");
         this.getProxy().consolePrintln("   displays a help message for the specified command");
      }

   }

   private void load(String var1) {
      try {
         ScriptInstance var2 = this.loader.loadScript(var1, this.sharedEnvironment);
         if (System.getProperty("sleep.debug") != null) {
            var2.setDebugFlags(Integer.parseInt(System.getProperty("sleep.debug")));
         }

         var2.runScript();
      } catch (YourCodeSucksException var3) {
         this.processScriptErrors(var3);
      } catch (Exception var4) {
         this.getProxy().consolePrintln("Could not load script " + var1 + ": " + var4.getMessage());
      }

   }

   private String getFullScript(String var1) {
      if (this.loader.getScriptsByKey().containsKey(var1)) {
         return var1;
      } else {
         Iterator var2 = this.loader.getScripts().iterator();

         File var4;
         do {
            if (!var2.hasNext()) {
               return var1;
            }

            ScriptInstance var3 = (ScriptInstance)var2.next();
            var4 = new File(var3.getName());
         } while(!var4.getName().equals(var1));

         return var4.getAbsolutePath();
      }
   }

   private void unload(String var1) {
      try {
         this.loader.unloadScript(this.getFullScript(var1));
      } catch (Exception var3) {
         this.getProxy().consolePrintln("Could not unloaded script " + var1 + ": " + var3.getMessage());
      }

   }

   private void list() {
      Iterator var1 = this.loader.getScripts().iterator();

      while(var1.hasNext()) {
         ScriptInstance var2 = (ScriptInstance)var1.next();
         this.getProxy().consolePrintln(var2.getName());
      }

   }

   private void env(String var1, String var2) {
      Iterator var3 = this.sharedEnvironment.keySet().iterator();

      while(true) {
         Object var4;
         do {
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = var3.next();
            } while(var1 != null && (!var1.equals("functions") || var4.toString().charAt(0) != '&') && (!var1.equals("other") || var4.toString().charAt(0) == '&'));
         } while(var2 != null && !Pattern.matches(".*?" + var2 + ".*", this.sharedEnvironment.get(var4).toString()));

         this.getProxy().consolePrintln(this.align(var4.toString(), 20) + " => " + this.sharedEnvironment.get(var4));
      }
   }

   private String align(String var1, int var2) {
      StringBuffer var3 = new StringBuffer(var1);

      while(var3.length() < var2) {
         var3.append(" ");
      }

      return var3.toString();
   }

   private void tree(String var1) {
      if (var1 == null) {
         this.getProxy().consolePrintln(this.script.getRunnableBlock().toString());
      } else if (var1.charAt(0) != '&' && var1.charAt(0) != '$') {
         Map var3 = this.loader.getScriptsByKey();
         if (var3.get(this.getFullScript(var1)) != null) {
            this.getProxy().consolePrintln(((ScriptInstance)var3.get(this.getFullScript(var1))).getRunnableBlock());
         } else {
            this.getProxy().consolePrintln("Could not find script " + var1 + " to print tree of");
         }
      } else if (this.sharedEnvironment != null && this.sharedEnvironment.get(var1) instanceof SleepClosure) {
         SleepClosure var2 = (SleepClosure)this.sharedEnvironment.get(var1);
         this.getProxy().consolePrintln(var2.getRunnableCode());
      } else {
         this.getProxy().consolePrintln("Could not find code block " + var1 + " to print tree of");
      }

   }

   private void debug(String var1, int var2) {
      if (var1 == null) {
         System.setProperty("sleep.debug", "" + var2);
         this.getProxy().consolePrintln("Default debug level set");
      } else {
         Map var3 = this.loader.getScriptsByKey();
         if (var3.get(this.getFullScript(var1)) != null) {
            ((ScriptInstance)var3.get(this.getFullScript(var1))).setDebugFlags(var2);
            this.getProxy().consolePrintln("Debug level set for " + var1);
         } else {
            this.getProxy().consolePrintln("Could not find script " + var1 + " to set debug level for");
         }
      }

   }

   private void interact() {
      this.interact = true;
      this.getProxy().consolePrintln(">> Welcome to interactive mode.");
      this.getProxy().consolePrintln("Type your code and then '.' on a line by itself to execute the code.");
      this.getProxy().consolePrintln("Type Ctrl+D or 'done' on a line by itself to leave interactive mode.");
   }

   private Scalar eval(String var1, String var2) {
      try {
         Parser var3 = new Parser("eval", var1.toString(), this.imports);
         this.imports = var3.getImportManager();
         var3.parse();
         Block var4 = var3.getRunnableBlock();
         this.script = this.loader.loadScript("<interact mode>", var4, this.sharedEnvironment);
         if (System.getProperty("sleep.debug") != null) {
            this.script.setDebugFlags(Integer.parseInt(System.getProperty("sleep.debug")));
         }

         return this.script.runScript();
      } catch (YourCodeSucksException var5) {
         this.processScriptErrors(var5);
      } catch (Exception var6) {
         this.getProxy().consolePrintln("error with " + var2 + ": " + var6.toString());
      }

      return null;
   }

   public void processScriptErrors(YourCodeSucksException var1) {
      this.getProxy().consolePrint(var1.formatErrors());
   }

   public void processScriptWarning(ScriptWarning var1) {
      this.getProxy().consolePrintln(var1.toString());
   }

   public void scriptLoaded(ScriptInstance var1) {
      if (!var1.getName().equals("<interact mode>") && !this.interact) {
         this.getProxy().consolePrintln(var1.getName() + " loaded successfully.");
      }

      var1.addWarningWatcher(this);
      var1.setScriptVariables(new ScriptVariables(this.sharedVariables));
   }

   public void scriptUnloaded(ScriptInstance var1) {
      this.getProxy().consolePrintln(var1.getName() + " has been unloaded");
   }
}
