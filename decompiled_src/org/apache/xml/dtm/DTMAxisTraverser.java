package org.apache.xml.dtm;

public abstract class DTMAxisTraverser {
   public int first(int context) {
      return this.next(context, context);
   }

   public int first(int context, int extendedTypeID) {
      return this.next(context, context, extendedTypeID);
   }

   public abstract int next(int var1, int var2);

   public abstract int next(int var1, int var2, int var3);
}
