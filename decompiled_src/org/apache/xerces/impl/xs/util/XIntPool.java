package org.apache.xerces.impl.xs.util;

public final class XIntPool {
   private static final short POOL_SIZE = 10;
   private static final XInt[] fXIntPool = new XInt[10];

   public final XInt getXInt(int var1) {
      return var1 >= 0 && var1 < fXIntPool.length ? fXIntPool[var1] : new XInt(var1);
   }

   static {
      for(int var0 = 0; var0 < 10; ++var0) {
         fXIntPool[var0] = new XInt(var0);
      }

   }
}
