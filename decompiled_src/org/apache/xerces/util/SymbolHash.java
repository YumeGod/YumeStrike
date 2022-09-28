package org.apache.xerces.util;

public class SymbolHash {
   protected int fTableSize = 101;
   protected Entry[] fBuckets;
   protected int fNum = 0;

   public SymbolHash() {
      this.fBuckets = new Entry[this.fTableSize];
   }

   public SymbolHash(int var1) {
      this.fTableSize = var1;
      this.fBuckets = new Entry[this.fTableSize];
   }

   public void put(Object var1, Object var2) {
      int var3 = (var1.hashCode() & Integer.MAX_VALUE) % this.fTableSize;
      Entry var4 = this.search(var1, var3);
      if (var4 != null) {
         var4.value = var2;
      } else {
         var4 = new Entry(var1, var2, this.fBuckets[var3]);
         this.fBuckets[var3] = var4;
         ++this.fNum;
      }

   }

   public Object get(Object var1) {
      int var2 = (var1.hashCode() & Integer.MAX_VALUE) % this.fTableSize;
      Entry var3 = this.search(var1, var2);
      return var3 != null ? var3.value : null;
   }

   public int getLength() {
      return this.fNum;
   }

   public int getValues(Object[] var1, int var2) {
      int var3 = 0;

      for(int var4 = 0; var3 < this.fTableSize && var4 < this.fNum; ++var3) {
         for(Entry var5 = this.fBuckets[var3]; var5 != null; var5 = var5.next) {
            var1[var2 + var4] = var5.value;
            ++var4;
         }
      }

      return this.fNum;
   }

   public SymbolHash makeClone() {
      SymbolHash var1 = new SymbolHash(this.fTableSize);
      var1.fNum = this.fNum;

      for(int var2 = 0; var2 < this.fTableSize; ++var2) {
         if (this.fBuckets[var2] != null) {
            var1.fBuckets[var2] = this.fBuckets[var2].makeClone();
         }
      }

      return var1;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.fTableSize; ++var1) {
         this.fBuckets[var1] = null;
      }

      this.fNum = 0;
   }

   protected Entry search(Object var1, int var2) {
      for(Entry var3 = this.fBuckets[var2]; var3 != null; var3 = var3.next) {
         if (var1.equals(var3.key)) {
            return var3;
         }
      }

      return null;
   }

   protected static final class Entry {
      public Object key;
      public Object value;
      public Entry next;

      public Entry() {
         this.key = null;
         this.value = null;
         this.next = null;
      }

      public Entry(Object var1, Object var2, Entry var3) {
         this.key = var1;
         this.value = var2;
         this.next = var3;
      }

      public Entry makeClone() {
         Entry var1 = new Entry();
         var1.key = this.key;
         var1.value = this.value;
         if (this.next != null) {
            var1.next = this.next.makeClone();
         }

         return var1;
      }
   }
}
