package sleep.engine.types;

import sleep.runtime.ScalarType;

public class IntValue implements ScalarType {
   protected int value;

   public IntValue(int var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return this;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public String toString() {
      return this.value + "";
   }

   public Object objectValue() {
      return new Integer(this.value);
   }

   public Class getType() {
      return this.getClass();
   }
}
