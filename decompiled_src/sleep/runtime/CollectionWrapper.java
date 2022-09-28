package sleep.runtime;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import sleep.engine.ObjectUtilities;

public class CollectionWrapper implements ScalarArray {
   protected Collection values;
   protected Object[] array = null;

   public ScalarArray sublist(int var1, int var2) {
      LinkedList var3 = new LinkedList();
      Iterator var4 = this.values.iterator();

      for(int var5 = 0; var4.hasNext() && var5 < var2; ++var5) {
         Object var6 = var4.next();
         if (var5 >= var1) {
            var3.add(var6);
         }
      }

      return new CollectionWrapper(var3);
   }

   public CollectionWrapper(Collection var1) {
      this.values = var1;
   }

   public String toString() {
      return "(read-only array: " + this.values.toString() + ")";
   }

   public Scalar pop() {
      throw new RuntimeException("array is read-only");
   }

   public void sort(Comparator var1) {
      throw new RuntimeException("array is read-only");
   }

   public Scalar push(Scalar var1) {
      throw new RuntimeException("array is read-only");
   }

   public int size() {
      return this.values.size();
   }

   public Scalar remove(int var1) {
      throw new RuntimeException("array is read-only");
   }

   public Scalar getAt(int var1) {
      if (this.array == null) {
         this.array = this.values.toArray();
      }

      return ObjectUtilities.BuildScalar(true, this.array[var1]);
   }

   public Iterator scalarIterator() {
      return new ProxyIterator(this.values.iterator(), false);
   }

   public Scalar add(Scalar var1, int var2) {
      throw new RuntimeException("array is read-only");
   }

   public void remove(Scalar var1) {
      throw new RuntimeException("array is read-only");
   }
}
