package sleep.parser;

import java.io.File;

public class ParserConfig {
   public static void installEscapeConstant(char var0, String var1) {
      CodeGenerator.installEscapeConstant(var0, var1);
   }

   public static void addKeyword(String var0) {
      Checkers.addKeyword(var0);
   }

   public static String getSleepClasspath() {
      return System.getProperty("sleep.classpath", ".");
   }

   public static void setSleepClasspath(String var0) {
      System.setProperty("sleep.classpath", var0);
   }

   public static File findJarFile(String var0) {
      File var1 = new File(var0);
      if (var1.exists()) {
         return var1;
      } else {
         String[] var2 = System.getProperty("sleep.classpath", ".").replace(':', ';').split(";");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            File var4 = new File(var2[var3], var0);
            if (var4.exists()) {
               return var4;
            }
         }

         return var1;
      }
   }
}
