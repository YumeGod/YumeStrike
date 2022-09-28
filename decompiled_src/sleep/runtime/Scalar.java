package sleep.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import sleep.engine.types.ObjectValue;

public class Scalar implements Serializable {
   protected ScalarType value = null;
   protected ScalarArray array = null;
   protected ScalarHash hash = null;

   public ScalarType getActualValue() {
      return this.value;
   }

   public ScalarType getValue() {
      if (this.value != null) {
         return this.value;
      } else if (this.array != null) {
         return SleepUtils.getScalar(SleepUtils.describe(this)).getValue();
      } else {
         return this.hash != null ? SleepUtils.getScalar(SleepUtils.describe(this)).getValue() : null;
      }
   }

   public String stringValue() {
      return this.getValue().toString();
   }

   public int intValue() {
      return this.getValue().intValue();
   }

   public double doubleValue() {
      return this.getValue().doubleValue();
   }

   public long longValue() {
      return this.getValue().longValue();
   }

   public Object objectValue() {
      if (this.array != null) {
         return this.array;
      } else {
         return this.hash != null ? this.hash : this.value.objectValue();
      }
   }

   public ScalarArray getArray() {
      return this.array;
   }

   public ScalarHash getHash() {
      return this.hash;
   }

   public void setValue(ScalarType var1) {
      this.value = var1.copyValue();
      this.array = null;
      this.hash = null;
   }

   public void setValue(ScalarArray var1) {
      this.value = null;
      this.array = var1;
      this.hash = null;
   }

   public void setValue(ScalarHash var1) {
      this.value = null;
      this.array = null;
      this.hash = var1;
   }

   public Object identity() {
      if (this.getArray() != null) {
         return this.array;
      } else if (this.getHash() != null) {
         return this.hash;
      } else {
         return this.value.getType() == ObjectValue.class ? this.objectValue() : this.toString();
      }
   }

   public boolean sameAs(Scalar var1) {
      if (this.getArray() != null && var1.getArray() != null && this.getArray() == var1.getArray()) {
         return true;
      } else if (this.getHash() != null && var1.getHash() != null && this.getHash() == var1.getHash()) {
         return true;
      } else if (this.getActualValue() != null && var1.getActualValue() != null) {
         if (this.getActualValue().getType() != ObjectValue.class && var1.getActualValue().getType() != ObjectValue.class) {
            return this.identity().equals(var1.identity());
         } else {
            return this.objectValue() == var1.objectValue();
         }
      } else {
         return false;
      }
   }

   public String toString() {
      return this.stringValue();
   }

   public void setValue(Scalar var1) {
      if (var1 != null) {
         if (var1.getArray() != null) {
            this.setValue(var1.getArray());
         } else if (var1.getHash() != null) {
            this.setValue(var1.getHash());
         } else if (var1.getValue() != null) {
            this.setValue(var1.getValue());
         }
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (SleepUtils.isEmptyScalar(this)) {
         var1.writeObject((Object)null);
      } else {
         var1.writeObject(this.value);
      }

      var1.writeObject(this.array);
      var1.writeObject(this.hash);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.value = (ScalarType)var1.readObject();
      this.array = (ScalarArray)var1.readObject();
      this.hash = (ScalarHash)var1.readObject();
      if (this.value == null && this.array == null && this.hash == null) {
         this.setValue(SleepUtils.getEmptyScalar());
      }

   }
}
