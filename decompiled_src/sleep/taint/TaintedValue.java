package sleep.taint;

import sleep.runtime.ScalarType;

public class TaintedValue implements ScalarType {
   protected ScalarType value = null;

   public TaintedValue(ScalarType var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return new TaintedValue(this.value.copyValue());
   }

   public ScalarType untaint() {
      return this.value;
   }

   public int intValue() {
      return this.value.intValue();
   }

   public long longValue() {
      return this.value.longValue();
   }

   public double doubleValue() {
      return this.value.doubleValue();
   }

   public String toString() {
      return this.value.toString();
   }

   public Object objectValue() {
      return this.value.objectValue();
   }

   public Class getType() {
      return this.value.getType();
   }
}
