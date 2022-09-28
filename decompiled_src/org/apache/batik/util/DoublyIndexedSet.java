package org.apache.batik.util;

public class DoublyIndexedSet {
   protected DoublyIndexedTable table = new DoublyIndexedTable();
   protected static Object value = new Object();

   public int size() {
      return this.table.size();
   }

   public void add(Object var1, Object var2) {
      this.table.put(var1, var2, value);
   }

   public void remove(Object var1, Object var2) {
      this.table.remove(var1, var2);
   }

   public boolean contains(Object var1, Object var2) {
      return this.table.get(var1, var2) != null;
   }

   public void clear() {
      this.table.clear();
   }
}
