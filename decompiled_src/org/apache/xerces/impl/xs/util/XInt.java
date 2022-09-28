package org.apache.xerces.impl.xs.util;

public final class XInt {
   private int fValue;

   XInt(int var1) {
      this.fValue = var1;
   }

   public final int intValue() {
      return this.fValue;
   }

   public final short shortValue() {
      return (short)this.fValue;
   }

   public final boolean equals(XInt var1) {
      return this.fValue == var1.fValue;
   }

   public String toString() {
      return Integer.toString(this.fValue);
   }
}
