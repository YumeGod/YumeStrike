package sleep.engine.types;

import sleep.runtime.ScalarType;

public class StringValue implements ScalarType {
   protected String value;

   public StringValue(String var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return this;
   }

   private String numberOnlyString() {
      return this.value;
   }

   public int intValue() {
      try {
         return Integer.parseInt(this.numberOnlyString());
      } catch (Exception var2) {
         return 0;
      }
   }

   public long longValue() {
      try {
         return Long.parseLong(this.numberOnlyString());
      } catch (Exception var2) {
         return 0L;
      }
   }

   public double doubleValue() {
      try {
         return Double.parseDouble(this.numberOnlyString());
      } catch (Exception var2) {
         return 0.0;
      }
   }

   public String toString() {
      return this.value;
   }

   public Object objectValue() {
      return this.value;
   }

   public Class getType() {
      return this.getClass();
   }
}
