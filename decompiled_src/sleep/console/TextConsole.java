package sleep.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Variable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.SleepUtils;
import sleep.taint.TaintUtils;

public class TextConsole implements ConsoleProxy {
   BufferedReader in;

   public static void main(String[] var0) {
      ScriptLoader var1 = new ScriptLoader();
      ConsoleImplementation var2 = new ConsoleImplementation((Hashtable)null, (Variable)null, var1);
      var2.setProxy(new TextConsole());
      if (var0.length <= 0) {
         try {
            var2.rppl();
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      } else {
         boolean var3 = false;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         int var9 = 0;

         while(true) {
            if (var9 >= var0.length || !var0[var9].startsWith("--") && (var0[var9].length() < 2 || var0[var9].charAt(0) != '-')) {
               Scalar var10 = SleepUtils.getArrayScalar();

               for(int var11 = var9 + 1; var11 < var0.length; ++var11) {
                  var10.getArray().push(TaintUtils.taint(SleepUtils.getScalar(var0[var11])));
               }

               try {
                  ScriptInstance var19;
                  if (var5) {
                     var19 = var1.loadScript(var0[var9 - 1], var0[var9], new Hashtable());
                  } else if (var6) {
                     var19 = var1.loadScript(var0[var9 - 1], "println(" + var0[var9] + ");", new Hashtable());
                  } else if (var0[var9].equals("-")) {
                     var19 = var1.loadScript("STDIN", System.in);
                  } else {
                     var19 = var1.loadScript(var0[var9]);
                  }

                  var19.getScriptVariables().putScalar("@ARGV", var10);
                  var19.getScriptVariables().putScalar("$__SCRIPT__", SleepUtils.getScalar(var19.getName()));
                  if (System.getProperty("sleep.debug") != null) {
                     var19.setDebugFlags(Integer.parseInt(System.getProperty("sleep.debug")));
                  }

                  if (var7) {
                     var19.setDebugFlags(var19.getDebugFlags() | 24);
                  }

                  if (var3) {
                     System.out.println(var0[var9] + " syntax OK");
                  } else if (var4) {
                     System.out.println(var19.getRunnableBlock());
                  } else {
                     long var12 = System.currentTimeMillis();
                     var19.runScript();
                     if (var7) {
                        var19.printProfileStatistics(System.out);
                     }

                     if (var8) {
                        long var14 = System.currentTimeMillis() - var12;
                        System.out.println("time: " + (double)var14 / 1000.0 + "s");
                     }
                  }
               } catch (YourCodeSucksException var17) {
                  var2.processScriptErrors(var17);
               } catch (Exception var18) {
                  var18.printStackTrace();
               }
               break;
            }

            if (!var0[var9].equals("-version") && !var0[var9].equals("--version") && !var0[var9].equals("-v")) {
               if (!var0[var9].equals("-help") && !var0[var9].equals("--help") && !var0[var9].equals("-h")) {
                  if (!var0[var9].equals("--check") && !var0[var9].equals("-c")) {
                     if (!var0[var9].equals("--ast") && !var0[var9].equals("-a")) {
                        if (!var0[var9].equals("--profile") && !var0[var9].equals("-p")) {
                           if (!var0[var9].equals("--time") && !var0[var9].equals("-t")) {
                              if (!var0[var9].equals("--eval") && !var0[var9].equals("-e")) {
                                 if (!var0[var9].equals("--expr") && !var0[var9].equals("-x")) {
                                    System.err.println("Unknown argument: " + var0[var9]);
                                    return;
                                 }

                                 var6 = true;
                              } else {
                                 var5 = true;
                              }
                           } else {
                              var8 = true;
                           }
                        } else {
                           var7 = true;
                        }
                     } else {
                        var4 = true;
                     }
                  } else {
                     var3 = true;
                  }

                  ++var9;
                  continue;
               }

               System.out.println("Sleep 2.1 (20090430)");
               System.out.println("Usage: java [properties] -jar sleep.jar [options] [-|file|expression]");
               System.out.println("       properties:");
               System.out.println("         -Dsleep.assert=<true|false>");
               System.out.println("         -Dsleep.classpath=<path to locate 3rd party jars from>");
               System.out.println("         -Dsleep.debug=<debug level>");
               System.out.println("         -Dsleep.taint=<true|false>");
               System.out.println("       options:");
               System.out.println("         -a --ast       display the abstract syntax tree of the specified script");
               System.out.println("         -c --check     check the syntax of the specified file");
               System.out.println("         -e --eval      evaluate a script as specified on command line");
               System.out.println("         -h --help      display this help message");
               System.out.println("         -p --profile   collect and display runtime profile statistics");
               System.out.println("         -t --time      display total script runtime");
               System.out.println("         -v --version   display version information");
               System.out.println("         -x --expr      evaluate an expression as specified on the command line");
               System.out.println("       file:");
               System.out.println("         specify a '-' to read script from STDIN");
               return;
            }

            System.out.println("Sleep 2.1 (20090430)");
            return;
         }
      }

   }

   public void consolePrint(String var1) {
      System.out.print(var1);
   }

   public void consolePrintln(Object var1) {
      System.out.println(var1.toString());
   }

   public String consoleReadln() {
      try {
         return this.in.readLine();
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public TextConsole() {
      this.in = new BufferedReader(new InputStreamReader(System.in));
   }
}
