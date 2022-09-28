package org.apache.regexp;

public class recompile {
   public static void main(String[] var0) {
      RECompiler var1 = new RECompiler();
      if (var0.length <= 0 || var0.length % 2 != 0) {
         System.out.println("Usage: recompile <patternname> <pattern>");
         System.exit(0);
      }

      for(int var2 = 0; var2 < var0.length; var2 += 2) {
         try {
            String var3 = var0[var2];
            String var4 = var0[var2 + 1];
            String var5 = var3 + "PatternInstructions";
            System.out.print("\n    // Pre-compiled regular expression '" + var4 + "'\n" + "    private static char[] " + var5 + " = \n    {");
            REProgram var6 = var1.compile(var4);
            byte var7 = 7;
            char[] var8 = var6.getInstructions();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               if (var9 % var7 == 0) {
                  System.out.print("\n        ");
               }

               String var10;
               for(var10 = Integer.toHexString(var8[var9]); var10.length() < 4; var10 = "0" + var10) {
               }

               System.out.print("0x" + var10 + ", ");
            }

            System.out.println("\n    };");
            System.out.println("\n    private static RE " + var3 + "Pattern = new RE(new REProgram(" + var5 + "));");
         } catch (RESyntaxException var11) {
            System.out.println("Syntax error in expression \"" + var0[var2] + "\": " + var11.toString());
         } catch (Exception var12) {
            System.out.println("Unexpected exception: " + var12.toString());
         } catch (Error var13) {
            System.out.println("Internal error: " + var13.toString());
         }
      }

   }
}
