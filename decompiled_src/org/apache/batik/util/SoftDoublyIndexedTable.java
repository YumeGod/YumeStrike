package org.apache.batik.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class SoftDoublyIndexedTable {
   protected static final int INITIAL_CAPACITY = 11;
   protected Entry[] table;
   protected int count;
   protected ReferenceQueue referenceQueue = new ReferenceQueue();

   public SoftDoublyIndexedTable() {
      this.table = new Entry[11];
   }

   public SoftDoublyIndexedTable(int var1) {
      this.table = new Entry[var1];
   }

   public int size() {
      return this.count;
   }

   public Object get(Object var1, Object var2) {
      int var3 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;

      for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         if (var5.hash == var3 && var5.match(var1, var2)) {
            return var5.get();
         }
      }

      return null;
   }

   public Object put(Object var1, Object var2, Object var3) {
      this.removeClearedEntries();
      int var4 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
      int var5 = var4 % this.table.length;
      Entry var6 = this.table[var5];
      if (var6 != null) {
         if (var6.hash == var4 && var6.match(var1, var2)) {
            Object var10 = var6.get();
            this.table[var5] = new Entry(var4, var1, var2, var3, var6.next);
            return var10;
         }

         Entry var7 = var6;

         for(var6 = var6.next; var6 != null; var6 = var6.next) {
            if (var6.hash == var4 && var6.match(var1, var2)) {
               Object var8 = var6.get();
               var6 = new Entry(var4, var1, var2, var3, var6.next);
               var7.next = var6;
               return var8;
            }

            var7 = var6;
         }
      }

      int var9 = this.table.length;
      if (this.count++ >= var9 - (var9 >> 2)) {
         this.rehash();
         var5 = var4 % this.table.length;
      }

      this.table[var5] = new Entry(var4, var1, var2, var3, this.table[var5]);
      return null;
   }

   public void clear() {
      this.table = new Entry[11];
      this.count = 0;
      this.referenceQueue = new ReferenceQueue();
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

   protected int hashCode(Object var1, Object var2) {
      int var3 = var1 == null ? 0 : var1.hashCode();
      return var3 ^ (var2 == null ? 0 : var2.hashCode());
   }

   protected void removeClearedEntries() {
      Entry var1;
      for(; (var1 = (Entry)this.referenceQueue.poll()) != null; --this.count) {
         int var2 = var1.hash % this.table.length;
         Entry var3 = this.table[var2];
         if (var3 == var1) {
            this.table[var2] = var1.next;
         } else {
            while(var3 != null) {
               Entry var4 = var3.next;
               if (var4 == var1) {
                  var3.next = var1.next;
                  break;
               }

               var3 = var4;
            }
         }
      }

   }

   protected class Entry extends SoftReference {
      public int hash;
      public Object key1;
      public Object key2;
      public Entry next;

      public Entry(int var2, Object var3, Object var4, Object var5, Entry var6) {
         super(var5, SoftDoublyIndexedTable.this.referenceQueue);
         this.hash = var2;
         this.key1 = var3;
         this.key2 = var4;
         this.next = var6;
      }

      public boolean match(Object var1, Object var2) {
         if (this.key1 != null) {
            if (!this.key1.equals(var1)) {
               return false;
            }
         } else if (var1 != null) {
            return false;
         }

         if (this.key2 != null) {
            return this.key2.equals(var2);
         } else {
            return var2 == null;
         }
      }
   }
}
