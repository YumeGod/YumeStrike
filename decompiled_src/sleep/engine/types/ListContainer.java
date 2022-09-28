package sleep.engine.types;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.SleepUtils;

public class ListContainer implements ScalarArray {
   protected List values;

   public ListContainer() {
      this.values = new MyLinkedList();
   }

   public ListContainer(List var1) {
      this.values = var1;
   }

   public ScalarArray sublist(int var1, int var2) {
      return new ListContainer(this.values.subList(var1, var2));
   }

   public ListContainer(Collection var1) {
      this();
      this.values.addAll(var1);
   }

   public Scalar pop() {
      return (Scalar)this.values.remove(this.values.size() - 1);
   }

   public Scalar push(Scalar var1) {
      this.values.add(var1);
      return var1;
   }

   public int size() {
      return this.values.size();
   }

   public void sort(Comparator var1) {
      Collections.sort(this.values, var1);
   }

   public Scalar getAt(int var1) {
      if (var1 >= this.size()) {
         Scalar var2 = SleepUtils.getEmptyScalar();
         this.values.add(var2);
         return var2;
      } else {
         return (Scalar)this.values.get(var1);
      }
   }

   public void remove(Scalar var1) {
      SleepUtils.removeScalar(this.values.iterator(), var1);
   }

   public Scalar remove(int var1) {
      return (Scalar)this.values.remove(var1);
   }

   public Iterator scalarIterator() {
      return this.values.iterator();
   }

   public Scalar add(Scalar var1, int var2) {
      this.values.add(var2, var1);
      return var1;
   }

   public String toString() {
      return this.values.toString();
   }
}
