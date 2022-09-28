package org.apache.fop.datatypes;

import org.apache.fop.fo.FObj;

public class SimplePercentBaseContext implements PercentBaseContext {
   private PercentBaseContext parentContext;
   private int lengthBase;
   private int lengthBaseValue;

   public SimplePercentBaseContext(PercentBaseContext parentContext, int lengthBase, int lengthBaseValue) {
      this.parentContext = parentContext;
      this.lengthBase = lengthBase;
      this.lengthBaseValue = lengthBaseValue;
   }

   public int getBaseLength(int lengthBase, FObj fobj) {
      if (lengthBase == this.lengthBase) {
         return this.lengthBaseValue;
      } else {
         return this.parentContext != null ? this.parentContext.getBaseLength(lengthBase, fobj) : -1;
      }
   }
}
