package sleep.engine.types;

import sleep.runtime.ScalarType;

public class ObjectValue implements ScalarType {
   protected Object value;

   public ObjectValue(Object var1) {
      this.value = var1;
   }

   public ScalarType copyValue() {
      return this;
   }

   public int intValue() {
      String var1 = this.toString();
      if (var1.length() == 0) {
         return 0;
      } else if (var1.equals("true")) {
         return 1;
      } else if (var1.equals("false")) {
         return 0;
      } else {
         try {
            return Integer.decode(var1);
         } catch (Exception var3) {
            return 0;
         }
      }
   }

   public long longValue() {
      String var1 = this.toString();
      if (var1.length() == 0) {
         return 0L;
      } else if (var1.equals("true")) {
         return 1L;
      } else if (var1.equals("false")) {
         return 0L;
      } else {
         try {
            return Long.decode(var1);
         } catch (Exception var3) {
            return 0L;
         }
      }
   }

   public double doubleValue() {
      String var1 = this.toString();
      if (var1.length() == 0) {
         return 0.0;
      } else if (var1.equals("true")) {
         return 1.0;
      } else if (var1.equals("false")) {
         return 0.0;
      } else {
         try {
            return Double.parseDouble(var1);
         } catch (Exception var3) {
            return 0.0;
         }
      }
   }

   public String toString() {
      return this.value.toString();
   }

   public Object objectValue() {
      return this.value;
   }

   public Class getType() {
      return this.getClass();
   }
}
