package org.apache.xerces.impl.xs.traversers;

class OneAttr {
   public String name;
   public int dvIndex;
   public int valueIndex;
   public Object dfltValue;

   public OneAttr(String var1, int var2, int var3, Object var4) {
      this.name = var1;
      this.dvIndex = var2;
      this.valueIndex = var3;
      this.dfltValue = var4;
   }
}
