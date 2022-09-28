package org.apache.fop.util;

public final class BreakUtil {
   private BreakUtil() {
   }

   private static int getBreakClassPriority(int breakClass) {
      switch (breakClass) {
         case 9:
            return 0;
         case 28:
            return 1;
         case 44:
            return 3;
         case 100:
            return 3;
         case 104:
            return 2;
         default:
            throw new IllegalArgumentException("Illegal value for breakClass: " + breakClass);
      }
   }

   public static int compareBreakClasses(int break1, int break2) {
      int p1 = getBreakClassPriority(break1);
      int p2 = getBreakClassPriority(break2);
      return p1 < p2 ? break2 : break1;
   }
}
