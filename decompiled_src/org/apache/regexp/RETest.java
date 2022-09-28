package org.apache.regexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RETest {
   RE r = new RE();
   REDebugCompiler compiler = new REDebugCompiler();
   static final boolean showSuccesses = false;
   char[] re1Instructions = new char[]{'|', '\u0000', '\u001a', '|', '\u0000', '\r', 'A', '\u0001', '\u0004', 'a', '|', '\u0000', '\u0003', 'G', '\u0000', '\ufff6', '|', '\u0000', '\u0003', 'N', '\u0000', '\u0003', 'A', '\u0001', '\u0004', 'b', 'E', '\u0000', '\u0000'};
   REProgram re1;
   String expr;
   int n;
   int failures;

   public RETest() {
      this.re1 = new REProgram(this.re1Instructions);
      this.n = 0;
      this.failures = 0;
   }

   public RETest(String[] var1) {
      this.re1 = new REProgram(this.re1Instructions);
      this.n = 0;
      this.failures = 0;

      try {
         if (var1.length == 2) {
            this.runInteractiveTests(var1[1]);
         } else if (var1.length == 1) {
            this.runAutomatedTests(var1[0]);
         } else {
            System.out.println("Usage: RETest ([-i] [regex]) ([/path/to/testfile.txt])");
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   void die(String var1) {
      this.say("FATAL ERROR: " + var1);
      System.exit(0);
   }

   void fail(String var1) {
      ++this.failures;
      this.say("\n");
      this.say("*******************************************************");
      this.say("*********************  FAILURE!  **********************");
      this.say("*******************************************************");
      this.say("\n");
      this.say(var1);
      this.say("");
      this.compiler.dumpProgram(new PrintWriter(System.out));
      this.say("\n");
   }

   public static void main(String[] var0) {
      try {
         test();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   void runAutomatedTests(String var1) throws Exception {
      long var2 = System.currentTimeMillis();
      RE var4 = new RE(this.re1);
      this.say("a*b");
      this.say("aaaab = " + var4.match("aaab"));
      this.showParens(var4);
      this.say("b = " + var4.match("b"));
      this.showParens(var4);
      this.say("c = " + var4.match("c"));
      this.showParens(var4);
      this.say("ccccaaaaab = " + var4.match("ccccaaaaab"));
      this.showParens(var4);
      var4 = new RE("a*b");
      String[] var5 = var4.split("xxxxaabxxxxbyyyyaaabzzz");
      var4 = new RE("x+");
      var5 = var4.grep(var5);

      for(int var6 = 0; var6 < var5.length; ++var6) {
         System.out.println("s[" + var6 + "] = " + var5[var6]);
      }

      var4 = new RE("a*b");
      String var7 = var4.subst("aaaabfooaaabgarplyaaabwackyb", "-");
      System.out.println("s = " + var7);
      File var8 = new File(var1);
      if (!var8.exists()) {
         throw new Exception("Could not find: " + var1);
      } else {
         BufferedReader var9 = new BufferedReader(new FileReader(var8));

         try {
            while(var9.ready()) {
               String var12 = "";

               while(var9.ready()) {
                  var12 = var9.readLine();
                  if (var12 == null) {
                     break;
                  }

                  var12 = var12.trim();
                  if (var12.startsWith("#")) {
                     break;
                  }

                  if (!var12.equals("")) {
                     System.out.println("Script error.  Line = " + var12);
                     System.exit(0);
                  }
               }

               if (!var9.ready()) {
                  break;
               }

               this.expr = var9.readLine();
               ++this.n;
               this.say("");
               this.say(this.n + ". " + this.expr);
               this.say("");

               String var13;
               try {
                  var4.setProgram(this.compiler.compile(this.expr));
               } catch (Exception var25) {
                  var13 = var9.readLine().trim();
                  if (var13.equals("ERR")) {
                     this.say("   Match: ERR");
                     this.success("Produces an error (" + var25.toString() + "), as expected.");
                     continue;
                  }

                  this.fail("Produces the unexpected error \"" + var25.getMessage() + "\"");
               } catch (Error var26) {
                  this.fail("Compiler threw fatal error \"" + var26.getMessage() + "\"");
                  var26.printStackTrace();
               }

               String var14 = var9.readLine().trim();
               this.say("   Match against: '" + var14 + "'");
               if (var14.equals("ERR")) {
                  this.fail("Was expected to be an error, but wasn't.");
               } else {
                  try {
                     boolean var15 = var4.match(var14);
                     var13 = var9.readLine().trim();
                     if (var15) {
                        this.say("   Match: YES");
                        if (var13.equals("NO")) {
                           this.fail("Matched \"" + var14 + "\", when not expected to.");
                        } else if (var13.equals("YES")) {
                           this.success("Matched \"" + var14 + "\", as expected:");
                           this.say("   Paren count: " + var4.getParenCount());

                           for(int var16 = 0; var16 < var4.getParenCount(); ++var16) {
                              String var17 = var9.readLine().trim();
                              this.say("   Paren " + var16 + " : " + var4.getParen(var16));
                              if (!var17.equals(var4.getParen(var16))) {
                                 this.fail("Register " + var16 + " should be = \"" + var17 + "\", but is \"" + var4.getParen(var16) + "\" instead.");
                              }
                           }
                        } else {
                           this.die("Test script error!");
                        }
                     } else {
                        this.say("   Match: NO");
                        if (var13.equals("YES")) {
                           this.fail("Did not match \"" + var14 + "\", when expected to.");
                        } else if (var13.equals("NO")) {
                           this.success("Did not match \"" + var14 + "\", as expected.");
                        } else {
                           this.die("Test script error!");
                        }
                     }
                  } catch (Exception var23) {
                     this.fail("Matcher threw exception: " + var23.toString());
                     var23.printStackTrace();
                  } catch (Error var24) {
                     this.fail("Matcher threw fatal error \"" + var24.getMessage() + "\"");
                     var24.printStackTrace();
                  }
               }
            }
         } finally {
            var9.close();
         }

         System.out.println("\n\nMatch time = " + (System.currentTimeMillis() - var2) + " ms.");
         System.out.println("\nTests complete.  " + this.n + " tests, " + this.failures + " failure(s).");
      }
   }

   void runInteractiveTests(String var1) {
      try {
         this.r.setProgram(this.compiler.compile(var1));
         this.say("\n" + var1 + "\n");
         this.compiler.dumpProgram(new PrintWriter(System.out));

         while(true) {
            BufferedReader var2 = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> ");
            System.out.flush();
            String var3 = var2.readLine();
            if (this.r.match(var3)) {
               this.say("Match successful.");
            } else {
               this.say("Match failed.");
            }

            this.showParens(this.r);
         }
      } catch (Exception var4) {
         this.say("Error: " + var4.toString());
         var4.printStackTrace();
      }
   }

   void say(String var1) {
      System.out.println(var1);
   }

   void show() {
      this.say("\n-----------------------\n");
      this.say("Expression #" + this.n + " \"" + this.expr + "\" ");
   }

   void showParens(RE var1) {
      for(int var2 = 0; var2 < var1.getParenCount(); ++var2) {
         this.say("$" + var2 + " = " + var1.getParen(var2));
      }

   }

   void success(String var1) {
   }

   public static boolean test() throws Exception {
      RETest var0 = new RETest();
      var0.runAutomatedTests("docs/RETest.txt");
      return var0.failures == 0;
   }
}
