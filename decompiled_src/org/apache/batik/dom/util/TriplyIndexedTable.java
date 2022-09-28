package org.apache.batik.dom.util;

public class TriplyIndexedTable {
   protected static final int INITIAL_CAPACITY = 11;
   protected Entry[] table;
   protected int count;

   public TriplyIndexedTable() {
      this.table = new Entry[11];
   }

   public TriplyIndexedTable(int var1) {
      this.table = new Entry[var1];
   }

   public int size() {
      return this.count;
   }

   public Object put(Object var1, Object var2, Object var3, Object var4) {
      int var5 = this.hashCode(var1, var2, var3) & Integer.MAX_VALUE;
      int var6 = var5 % this.table.length;

      for(Entry var7 = this.table[var6]; var7 != null; var7 = var7.next) {
         if (var7.hash == var5 && var7.match(var1, var2, var3)) {
            Object var8 = var7.value;
            var7.value = var4;
            return var8;
         }
      }

      int var9 = this.table.length;
      if (this.count++ >= var9 - (var9 >> 2)) {
         this.rehash();
         var6 = var5 % this.table.length;
      }

      Entry var10 = new Entry(var5, var1, var2, var3, var4, this.table[var6]);
      this.table[var6] = var10;
      return null;
   }

   public Object get(Object var1, Object var2, Object var3) {
      int var4 = this.hashCode(var1, var2, var3) & Integer.MAX_VALUE;
      int var5 = var4 % this.table.length;

      for(Entry var6 = this.table[var5]; var6 != null; var6 = var6.next) {
         if (var6.hash == var4 && var6.match(var1, var2, var3)) {
            return var6.value;
         }
      }

      return null;
   }

   protected void rehash() {
      Entry[] var1 = this.table;
      this.table = new Entry[var1.length * 2 + 1];

      Entry var4;
      int var5;
      for(int var2 = var1.length - 1; var2 >= 0; --var2) {
         for(Entry var3 = var1[var2]; var3 != null; this.table[var5] = var4) {
            var4 = var3;
            var3 = var3.next;
            var5 = var4.hash % this.table.length;
            var4.next = this.table[var5];
         }
      }

   }

   protected int hashCode(Object var1, Object var2, Object var3) {
      return (var1 == null ? 0 : var1.hashCode()) ^ (var2 == null ? 0 : var2.hashCode()) ^ (var3 == null ? 0 : var3.hashCode());
   }

   protected static class Entry {
      public int hash;
      public Object key1;
      public Object key2;
      public Object key3;
      public Object value;
      public Entry next;

      public Entry(int var1, Object var2, Object var3, Object var4, Object var5, Entry var6) {
         this.hash = var1;
         this.key1 = var2;
         this.key2 = var3;
         this.key3 = var4;
         this.value = var5;
         this.next = var6;
      }

      public boolean match(Object var1, Object var2, Object var3) {
         if (this.key1 != null) {
            if (!this.key1.equals(var1)) {
               return false;
            }
         } else if (var1 != null) {
            return false;
         }

         if (this.key2 != null) {
            if (!this.key2.equals(var2)) {
               return false;
            }
         } else if (var2 != null) {
            return false;
         }

         if (this.key3 != null) {
            return this.key3.equals(var3);
         } else {
            return var3 == null;
         }
      }
   }
}
