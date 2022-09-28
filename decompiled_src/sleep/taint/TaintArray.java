package sleep.taint;

import java.util.Comparator;
import java.util.Iterator;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;

public class TaintArray implements ScalarArray {
   protected ScalarArray source;

   public ScalarArray sublist(int var1, int var2) {
      return new TaintArray(this.source.sublist(var1, var2));
   }

   public TaintArray(ScalarArray var1) {
      this.source = var1;
   }

   public String toString() {
      return this.source.toString();
   }

   public Scalar pop() {
      return TaintUtils.taintAll(this.source.pop());
   }

   public void sort(Comparator var1) {
      this.source.sort(var1);
   }

   public Scalar push(Scalar var1) {
      return TaintUtils.taintAll(this.source.push(var1));
   }

   public int size() {
      return this.source.size();
   }

   public Scalar remove(int var1) {
      return TaintUtils.taintAll(this.source.remove(var1));
   }

   public Scalar getAt(int var1) {
      return TaintUtils.taintAll(this.source.getAt(var1));
   }

   public Iterator scalarIterator() {
      return new TaintIterator(this.source.scalarIterator());
   }

   public Scalar add(Scalar var1, int var2) {
      return TaintUtils.taintAll(this.source.add(var1, var2));
   }

   public void remove(Scalar var1) {
      this.source.remove(var1);
   }

   protected class TaintIterator implements Iterator {
      protected Iterator realIterator;

      public TaintIterator(Iterator var2) {
         this.realIterator = var2;
      }

      public boolean hasNext() {
         return this.realIterator.hasNext();
      }

      public Object next() {
         return TaintUtils.taintAll((Scalar)this.realIterator.next());
      }

      public void remove() {
         this.realIterator.remove();
      }
   }
}
