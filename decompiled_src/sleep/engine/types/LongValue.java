package sleep.engine.types;

import sleep.runtime.ScalarType;

public class LongValue implements ScalarType {
   protected long value;

   public LongValue(long var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return this;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public String toString() {
      return this.value + "";
   }

   public Object objectValue() {
      return new Long(this.value);
   }

   public Class getType() {
      return this.getClass();
   }
}
