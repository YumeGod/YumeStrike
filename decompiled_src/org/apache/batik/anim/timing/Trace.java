package org.apache.batik.anim.timing;

public class Trace {
   private static int level;
   private static boolean enabled = false;

   public static void enter(Object var0, String var1, Object[] var2) {
      if (enabled) {
         System.err.print("LOG\t");

         int var3;
         for(var3 = 0; var3 < level; ++var3) {
            System.err.print("  ");
         }

         if (var1 == null) {
            System.err.print("new " + var0.getClass().getName() + "(");
         } else {
            System.err.print(var0 + "." + var1 + "(");
         }

         if (var2 != null) {
            System.err.print(var2[0]);

            for(var3 = 1; var3 < var2.length; ++var3) {
               System.err.print(", " + var2[var3]);
            }
         }

         System.err.println(")");
      }

      ++level;
   }

   public static void exit() {
      --level;
   }

   public static void print(String var0) {
      if (enabled) {
         System.err.print("LOG\t");

         for(int var1 = 0; var1 < level; ++var1) {
            System.err.print("  ");
         }

         System.err.println(var0);
      }

   }
}
