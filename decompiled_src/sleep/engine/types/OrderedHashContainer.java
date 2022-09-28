package sleep.engine.types;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.SleepClosure;
import sleep.runtime.CollectionWrapper;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class OrderedHashContainer extends HashContainer {
   protected boolean shouldClean = false;
   protected transient SleepClosure missPolicy;
   protected transient SleepClosure removalPolicy;

   public OrderedHashContainer(int var1, float var2, boolean var3) {
      this.values = new OrderedHash(var1, var2, var3);
   }

   public void setRemovalPolicy(SleepClosure var1) {
      this.removalPolicy = var1;
   }

   public void setMissPolicy(SleepClosure var1) {
      this.missPolicy = var1;
   }

   protected boolean removeEldestEntryCheck(Map.Entry var1) {
      if (this.removalPolicy != null && var1 != null) {
         Stack var2 = new Stack();
         var2.push(var1.getValue());
         var2.push(SleepUtils.getScalar(var1.getKey().toString()));
         var2.push(SleepUtils.getHashScalar(this));
         Scalar var3 = this.removalPolicy.callClosure("remove", (ScriptInstance)null, var2);
         return SleepUtils.isTrueScalar(var3);
      } else {
         return false;
      }
   }

   public ScalarArray keys() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.values.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         if (!SleepUtils.isEmptyScalar((Scalar)var3.getValue())) {
            var1.add(var3.getKey());
         }
      }

      this.shouldClean = this.values.size() > var1.size() + 1;
      return new CollectionWrapper(var1);
   }

   private void cleanup() {
      if (this.shouldClean) {
         Iterator var1 = this.values.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            if (SleepUtils.isEmptyScalar((Scalar)var2.getValue())) {
               var1.remove();
            }
         }

         this.shouldClean = false;
      }

   }

   public Scalar getAt(Scalar var1) {
      String var2 = var1.getValue().toString();
      Scalar var3 = (Scalar)this.values.get(var2);
      if (this.missPolicy != null && SleepUtils.isEmptyScalar(var3)) {
         this.cleanup();
         Stack var4 = new Stack();
         var4.push(var1);
         var4.push(SleepUtils.getHashScalar(this));
         var3 = SleepUtils.getScalar(this.missPolicy.callClosure("miss", (ScriptInstance)null, var4));
         this.values.put(var2, var3);
      } else if (var3 == null) {
         this.cleanup();
         var3 = SleepUtils.getEmptyScalar();
         this.values.put(var2, var3);
      }

      return var3;
   }

   private class OrderedHash extends LinkedHashMap {
      public OrderedHash(int var2, float var3, boolean var4) {
         super(var2, var3, var4);
      }

      protected boolean removeEldestEntry(Map.Entry var1) {
         return OrderedHashContainer.this.removeEldestEntryCheck(var1);
      }
   }
}
