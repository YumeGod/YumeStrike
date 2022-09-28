package org.apache.xerces.dom;

import java.util.Hashtable;

class LCount {
   static Hashtable lCounts = new Hashtable();
   public int captures = 0;
   public int bubbles = 0;
   public int defaults;
   public int total = 0;

   static LCount lookup(String var0) {
      LCount var1 = (LCount)lCounts.get(var0);
      if (var1 == null) {
         lCounts.put(var0, var1 = new LCount());
      }

      return var1;
   }
}
