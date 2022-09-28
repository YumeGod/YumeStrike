package org.apache.batik.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyIndexedTable {
   protected int initialCapacity;
   protected Entry[] table;
   protected int count;

   public DoublyIndexedTable() {
      this(16);
   }

   public DoublyIndexedTable(int var1) {
      this.initialCapacity = var1;
      this.table = new Entry[var1];
   }

   public DoublyIndexedTable(DoublyIndexedTable var1) {
      this.initialCapacity = var1.initialCapacity;
      this.table = new Entry[var1.table.length];

      for(int var2 = 0; var2 < var1.table.length; ++var2) {
         Entry var3 = null;

         for(Entry var4 = var1.table[var2]; var4 != null; var4 = var4.next) {
            var3 = new Entry(var4.hash, var4.key1, var4.key2, var4.value, var3);
         }

         this.table[var2] = var3;
      }

      this.count = var1.count;
   }

   public int size() {
      return this.count;
   }

   public Object put(Object var1, Object var2, Object var3) {
      int var4 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
      int var5 = var4 % this.table.length;

      for(Entry var6 = this.table[var5]; var6 != null; var6 = var6.next) {
         if (var6.hash == var4 && var6.match(var1, var2)) {
            Object var7 = var6.value;
            var6.value = var3;
            return var7;
         }
      }

      int var8 = this.table.length;
      if (this.count++ >= var8 - (var8 >> 2)) {
         this.rehash();
         var5 = var4 % this.table.length;
      }

      Entry var9 = new Entry(var4, var1, var2, var3, this.table[var5]);
      this.table[var5] = var9;
      return null;
   }

   public Object get(Object var1, Object var2) {
      int var3 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;

      for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         if (var5.hash == var3 && var5.match(var1, var2)) {
            return var5.value;
         }
      }

      return null;
   }

   public Object remove(Object var1, Object var2) {
      int var3 = this.hashCode(var1, var2) & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;
      Entry var5 = this.table[var4];
      if (var5 == null) {
         return null;
      } else if (var5.hash == var3 && var5.match(var1, var2)) {
         this.table[var4] = var5.next;
         --this.count;
         return var5.value;
      } else {
         Entry var6 = var5;

         for(var5 = var5.next; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && var5.match(var1, var2)) {
               var6.next = var5.next;
               --this.count;
               return var5.value;
            }

            var6 = var5;
         }

         return null;
      }
   }

   public Object[] getValuesArray() {
      Object[] var1 = new Object[this.count];
      int var2 = 0;

      for(int var3 = 0; var3 < this.table.length; ++var3) {
         for(Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
            var1[var2++] = var4.value;
         }
      }

      return var1;
   }

   public void clear() {
      this.table = new Entry[this.initialCapacity];
      this.count = 0;
   }

   public Iterator iterator() {
      return new TableIterator(this);
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

   protected class TableIterator implements Iterator {
      private int nextIndex;
      private Entry nextEntry;
      private boolean finished;
      // $FF: synthetic field
      private final DoublyIndexedTable this$0;

      public TableIterator(DoublyIndexedTable var1) {
         for(this.this$0 = var1; this.nextIndex < var1.table.length; ++this.nextIndex) {
            this.nextEntry = var1.table[this.nextIndex];
            if (this.nextEntry != null) {
               break;
            }
         }

         this.finished = this.nextEntry == null;
      }

      public boolean hasNext() {
         return !this.finished;
      }

      public Object next() {
         if (this.finished) {
            throw new NoSuchElementException();
         } else {
            Entry var1 = this.nextEntry;
            this.findNext();
            return var1;
         }
      }

      protected void findNext() {
         this.nextEntry = this.nextEntry.next;
         if (this.nextEntry == null) {
            ++this.nextIndex;

            while(this.nextIndex < this.this$0.table.length) {
               this.nextEntry = this.this$0.table[this.nextIndex];
               if (this.nextEntry != null) {
                  break;
               }

               ++this.nextIndex;
            }
         }

         this.finished = this.nextEntry == null;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   public static class Entry {
      protected int hash;
      protected Object key1;
      protected Object key2;
      protected Object value;
      protected Entry next;

      public Entry(int var1, Object var2, Object var3, Object var4, Entry var5) {
         this.hash = var1;
         this.key1 = var2;
         this.key2 = var3;
         this.value = var4;
         this.next = var5;
      }

      public Object getKey1() {
         return this.key1;
      }

      public Object getKey2() {
         return this.key2;
      }

      public Object getValue() {
         return this.value;
      }

      protected boolean match(Object var1, Object var2) {
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
