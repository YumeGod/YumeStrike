package sleep.taint;

import java.util.Iterator;
import java.util.Map;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScalarHash;

public class TaintHash implements ScalarHash {
   protected ScalarHash source;

   public TaintHash(ScalarHash var1) {
      this.source = var1;
   }

   public Scalar getAt(Scalar var1) {
      return TaintUtils.taintAll(this.source.getAt(var1));
   }

   public ScalarArray keys() {
      return this.source.keys();
   }

   public void remove(Scalar var1) {
      this.source.remove(var1);
   }

   public Map getData() {
      Map var1 = this.source.getData();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         if (var3.getValue() != null && var3.getKey() != null) {
            var3.setValue(TaintUtils.taintAll((Scalar)var3.getValue()));
         }
      }

      return var1;
   }

   public String toString() {
      return this.source.toString();
   }
}
