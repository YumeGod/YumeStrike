package sleep.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sleep.taint.TaintedValue;

public class WatchScalar extends Scalar {
   protected ScriptEnvironment owner;
   protected String name;

   public WatchScalar(String var1, ScriptEnvironment var2) {
      this.name = var1;
      this.owner = var2;
   }

   public void flagChange(Scalar var1) {
      if (this.owner != null && (this.value != null || this.array != null || this.hash != null)) {
         this.owner.showDebugMessage("watch(): " + this.name + " = " + SleepUtils.describe(var1));
      }

   }

   public void setValue(ScalarType var1) {
      if (var1.getClass() != TaintedValue.class || ((TaintedValue)var1).untaint() != this.value) {
         Scalar var2 = new Scalar();
         var2.setValue(var1);
         this.flagChange(var2);
      }

      super.setValue(var1);
   }

   public void setValue(ScalarArray var1) {
      Scalar var2 = new Scalar();
      var2.setValue(var1);
      this.flagChange(var2);
      super.setValue(var1);
   }

   public void setValue(ScalarHash var1) {
      Scalar var2 = new Scalar();
      var2.setValue(var1);
      this.flagChange(var2);
      super.setValue(var1);
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
         this.setValue((Scalar)SleepUtils.getEmptyScalar());
      }

   }
}
