package sleep.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sleep.engine.ObjectUtilities;

public class MapWrapper implements ScalarHash {
   protected Map values;

   public MapWrapper(Map var1) {
      this.values = var1;
   }

   public Scalar getAt(Scalar var1) {
      Object var2 = this.values.get(var1.getValue().toString());
      return ObjectUtilities.BuildScalar(true, var2);
   }

   public ScalarArray keys() {
      return new CollectionWrapper(this.values.keySet());
   }

   public void remove(Scalar var1) {
      throw new RuntimeException("hash is read-only");
   }

   public Map getData() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.values.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         if (var3.getValue() != null && var3.getKey() != null) {
            var1.put(var3.getKey().toString(), ObjectUtilities.BuildScalar(true, var3.getValue()));
         }
      }

      return var1;
   }

   public void rehash(int var1, float var2) {
      throw new RuntimeException("hash is read-only");
   }

   public String toString() {
      return this.values.toString();
   }
}
