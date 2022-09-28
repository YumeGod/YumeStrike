package org.apache.xerces.impl.xs.traversers;

abstract class Container {
   static final int THRESHOLD = 5;
   OneAttr[] values;
   int pos = 0;

   static Container getContainer(int var0) {
      return (Container)(var0 > 5 ? new LargeContainer(var0) : new SmallContainer(var0));
   }

   abstract void put(String var1, OneAttr var2);

   abstract OneAttr get(String var1);
}
