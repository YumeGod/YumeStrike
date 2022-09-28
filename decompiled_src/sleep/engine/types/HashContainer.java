package sleep.engine.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sleep.runtime.CollectionWrapper;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScalarType;
import sleep.runtime.SleepUtils;

public class HashContainer implements ScalarHash {
   protected Map values;

   public HashContainer(Map var1) {
      this.values = var1;
   }

   public HashContainer() {
      this(new HashMap());
   }

   public Scalar getAt(Scalar var1) {
      String var2 = var1.getValue().toString();
      Scalar var3 = (Scalar)this.values.get(var2);
      if (var3 == null) {
         var3 = SleepUtils.getEmptyScalar();
         this.values.put(var2, var3);
      }

      return var3;
   }

   public Map getData() {
      return this.values;
   }

   public ScalarArray keys() {
      ScalarType var1 = SleepUtils.getEmptyScalar().getValue();
      Iterator var2 = this.values.values().iterator();

      while(var2.hasNext()) {
         Scalar var3 = (Scalar)var2.next();
         if (var3.getArray() == null && var3.getHash() == null && var3.getActualValue() == var1) {
            var2.remove();
         }
      }

      return new CollectionWrapper(this.values.keySet());
   }

   public void remove(Scalar var1) {
      SleepUtils.removeScalar(this.values.values().iterator(), var1);
   }

   public String toString() {
      return this.values.toString();
   }
}
