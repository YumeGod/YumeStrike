package org.apache.batik.gvt.font;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class AWTGlyphGeometryCache {
   protected static final int INITIAL_CAPACITY = 71;
   protected Entry[] table;
   protected int count;
   protected ReferenceQueue referenceQueue = new ReferenceQueue();

   public AWTGlyphGeometryCache() {
      this.table = new Entry[71];
   }

   public AWTGlyphGeometryCache(int var1) {
      this.table = new Entry[var1];
   }

   public int size() {
      return this.count;
   }

   public Value get(char var1) {
      int var2 = this.hashCode(var1) & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && var4.match(var1)) {
            return (Value)var4.get();
         }
      }

      return null;
   }

   public Value put(char var1, Value var2) {
      this.removeClearedEntries();
      int var3 = this.hashCode(var1) & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;
      Entry var5 = this.table[var4];
      if (var5 != null) {
         if (var5.hash == var3 && var5.match(var1)) {
            Object var9 = var5.get();
            this.table[var4] = new Entry(var3, var1, var2, var5.next);
            return (Value)var9;
         }

         Entry var6 = var5;

         for(var5 = var5.next; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && var5.match(var1)) {
               Object var7 = var5.get();
               var5 = new Entry(var3, var1, var2, var5.next);
               var6.next = var5;
               return (Value)var7;
            }

            var6 = var5;
         }
      }

      int var8 = this.table.length;
      if (this.count++ >= var8 - (var8 >> 2)) {
         this.rehash();
         var4 = var3 % this.table.length;
      }

      this.table[var4] = new Entry(var3, var1, var2, this.table[var4]);
      return null;
   }

   public void clear() {
      this.table = new Entry[71];
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

   protected int hashCode(char var1) {
      return var1;
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
      public char c;
      public Entry next;

      public Entry(int var2, char var3, Value var4, Entry var5) {
         super(var4, AWTGlyphGeometryCache.this.referenceQueue);
         this.hash = var2;
         this.c = var3;
         this.next = var5;
      }

      public boolean match(char var1) {
         return this.c == var1;
      }
   }

   public static class Value {
      protected Shape outline;
      protected Rectangle2D gmB;
      protected Rectangle2D outlineBounds;

      public Value(Shape var1, Rectangle2D var2) {
         this.outline = var1;
         this.outlineBounds = var1.getBounds2D();
         this.gmB = var2;
      }

      public Shape getOutline() {
         return this.outline;
      }

      public Rectangle2D getBounds2D() {
         return this.gmB;
      }

      public Rectangle2D getOutlineBounds2D() {
         return this.outlineBounds;
      }
   }
}
