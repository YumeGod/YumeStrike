package org.apache.batik.css.engine;

public class StringIntMap {
   protected Entry[] table;
   protected int count;

   public StringIntMap(int var1) {
      this.table = new Entry[var1 - (var1 >> 2) + 1];
   }

   public int get(String var1) {
      int var2 = var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && var4.key.equals(var1)) {
            return var4.value;
         }
      }

      return -1;
   }

   public void put(String var1, int var2) {
      int var3 = var1.hashCode() & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;

      for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         if (var5.hash == var3 && var5.key.equals(var1)) {
            var5.value = var2;
            return;
         }
      }

      int var7 = this.table.length;
      if (this.count++ >= var7 - (var7 >> 2)) {
         this.rehash();
         var4 = var3 % this.table.length;
      }

      Entry var6 = new Entry(var3, var1, var2, this.table[var4]);
      this.table[var4] = var6;
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

   protected static class Entry {
      public final int hash;
      public String key;
      public int value;
      public Entry next;

      public Entry(int var1, String var2, int var3, Entry var4) {
         this.hash = var1;
         this.key = var2;
         this.value = var3;
         this.next = var4;
      }
   }
}
