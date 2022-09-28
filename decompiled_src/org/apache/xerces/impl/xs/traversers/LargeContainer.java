package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;

class LargeContainer extends Container {
   Hashtable items;

   LargeContainer(int var1) {
      this.items = new Hashtable(var1 * 2 + 1);
      super.values = new OneAttr[var1];
   }

   void put(String var1, OneAttr var2) {
      this.items.put(var1, var2);
      super.values[super.pos++] = var2;
   }

   OneAttr get(String var1) {
      OneAttr var2 = (OneAttr)this.items.get(var1);
      return var2;
   }
}
