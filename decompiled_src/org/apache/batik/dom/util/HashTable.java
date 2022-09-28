package org.apache.batik.dom.util;

import java.io.Serializable;

public class HashTable implements Serializable {
   protected static final int INITIAL_CAPACITY = 11;
   protected Entry[] table;
   protected int count;

   public HashTable() {
      this.table = new Entry[11];
   }

   public HashTable(int var1) {
      this.table = new Entry[var1];
   }

   public HashTable(HashTable var1) {
      this.count = var1.count;
      this.table = new Entry[var1.table.length];

      for(int var2 = 0; var2 < this.table.length; ++var2) {
         Entry var3 = var1.table[var2];
         Entry var4 = null;
         if (var3 != null) {
            var4 = new Entry(var3.hash, var3.key, var3.value, (Entry)null);
            this.table[var2] = var4;

            for(var3 = var3.next; var3 != null; var3 = var3.next) {
               var4.next = new Entry(var3.hash, var3.key, var3.value, (Entry)null);
               var4 = var4.next;
            }
         }
      }

   }

   public int size() {
      return this.count;
   }

   public Object get(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && (var4.key == null && var1 == null || var4.key != null && var4.key.equals(var1))) {
            return var4.value;
         }
      }

      return null;
   }

   public Object put(Object var1, Object var2) {
      int var3 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;

      for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         if (var5.hash == var3 && (var5.key == null && var1 == null || var5.key != null && var5.key.equals(var1))) {
            Object var6 = var5.value;
            var5.value = var2;
            return var6;
         }
      }

      int var7 = this.table.length;
      if (this.count++ >= var7 - (var7 >> 2)) {
         this.rehash();
         var4 = var3 % this.table.length;
      }

      Entry var8 = new Entry(var3, var1, var2, this.table[var4]);
      this.table[var4] = var8;
      return null;
   }

   public Object remove(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;
      Entry var4 = null;

      for(Entry var5 = this.table[var3]; var5 != null; var5 = var5.next) {
         if (var5.hash == var2 && (var5.key == null && var1 == null || var5.key != null && var5.key.equals(var1))) {
            Object var6 = var5.value;
            if (var4 == null) {
               this.table[var3] = var5.next;
            } else {
               var4.next = var5.next;
            }

            --this.count;
            return var6;
         }

         var4 = var5;
      }

      return null;
   }

   public Object key(int var1) {
      if (var1 >= 0 && var1 < this.count) {
         int var2 = 0;

         for(int var3 = 0; var3 < this.table.length; ++var3) {
            Entry var4 = this.table[var3];
            if (var4 != null) {
               do {
                  if (var2++ == var1) {
                     return var4.key;
                  }

                  var4 = var4.next;
               } while(var4 != null);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public Object item(int var1) {
      if (var1 >= 0 && var1 < this.count) {
         int var2 = 0;

         for(int var3 = 0; var3 < this.table.length; ++var3) {
            Entry var4 = this.table[var3];
            if (var4 != null) {
               do {
                  if (var2++ == var1) {
                     return var4.value;
                  }

                  var4 = var4.next;
               } while(var4 != null);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void clear() {
      for(int var1 = 0; var1 < this.table.length; ++var1) {
         this.table[var1] = null;
      }

      this.count = 0;
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

   protected static class Entry implements Serializable {
      public int hash;
      public Object key;
      public Object value;
      public Entry next;

      public Entry(int var1, Object var2, Object var3, Entry var4) {
         this.hash = var1;
         this.key = var2;
         this.value = var3;
         this.next = var4;
      }
   }
}
