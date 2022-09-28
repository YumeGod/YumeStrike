package org.apache.batik.dom.util;

import org.apache.batik.util.CleanerThread;
import org.w3c.dom.Element;

public class DocumentDescriptor {
   protected static final int INITIAL_CAPACITY = 101;
   protected Entry[] table = new Entry[101];
   protected int count;

   public int getNumberOfElements() {
      synchronized(this) {
         return this.count;
      }
   }

   public int getLocationLine(Element var1) {
      synchronized(this) {
         int var3 = var1.hashCode() & Integer.MAX_VALUE;
         int var4 = var3 % this.table.length;

         for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3) {
               Object var6 = var5.get();
               if (var6 == var1) {
                  return var5.locationLine;
               }
            }
         }

         return 0;
      }
   }

   public int getLocationColumn(Element var1) {
      synchronized(this) {
         int var3 = var1.hashCode() & Integer.MAX_VALUE;
         int var4 = var3 % this.table.length;

         for(Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3) {
               Object var6 = var5.get();
               if (var6 == var1) {
                  return var5.locationColumn;
               }
            }
         }

         return 0;
      }
   }

   public void setLocation(Element var1, int var2, int var3) {
      synchronized(this) {
         int var5 = var1.hashCode() & Integer.MAX_VALUE;
         int var6 = var5 % this.table.length;

         for(Entry var7 = this.table[var6]; var7 != null; var7 = var7.next) {
            if (var7.hash == var5) {
               Object var8 = var7.get();
               if (var8 == var1) {
                  var7.locationLine = var2;
               }
            }
         }

         int var11 = this.table.length;
         if (this.count++ >= var11 - (var11 >> 2)) {
            this.rehash();
            var6 = var5 % this.table.length;
         }

         Entry var12 = new Entry(var5, var1, var2, var3, this.table[var6]);
         this.table[var6] = var12;
      }
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

   protected void removeEntry(Entry var1) {
      synchronized(this) {
         int var3 = var1.hash;
         int var4 = var3 % this.table.length;
         Entry var5 = this.table[var4];

         Entry var6;
         for(var6 = null; var5 != var1; var5 = var5.next) {
            var6 = var5;
         }

         if (var5 != null) {
            if (var6 == null) {
               this.table[var4] = var5.next;
            } else {
               var6.next = var5.next;
            }

            --this.count;
         }
      }
   }

   protected class Entry extends CleanerThread.WeakReferenceCleared {
      public int hash;
      public int locationLine;
      public int locationColumn;
      public Entry next;

      public Entry(int var2, Element var3, int var4, int var5, Entry var6) {
         super(var3);
         this.hash = var2;
         this.locationLine = var4;
         this.locationColumn = var5;
         this.next = var6;
      }

      public void cleared() {
         DocumentDescriptor.this.removeEntry(this);
      }
   }
}
