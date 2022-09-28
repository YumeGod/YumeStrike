package sleep.engine.types;

import sleep.runtime.ScalarType;

public class DoubleValue implements ScalarType {
   protected double value;

   public DoubleValue(double var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return this;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public double doubleValue() {
      return this.value;
   }

   public String toString() {
      return this.value + "";
   }

   public Object objectValue() {
      return new Double(this.value);
   }

   public Class getType() {
      return this.getClass();
   }
}
