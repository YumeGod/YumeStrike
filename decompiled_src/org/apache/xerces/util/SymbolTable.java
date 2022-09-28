package org.apache.xerces.util;

public class SymbolTable {
   protected static final int TABLE_SIZE = 101;
   protected Entry[] fBuckets;
   protected int fTableSize;
   protected transient int fCount;
   protected int fThreshold;
   protected float fLoadFactor;

   public SymbolTable(int var1, float var2) {
      this.fBuckets = null;
      if (var1 < 0) {
         throw new IllegalArgumentException("Illegal Capacity: " + var1);
      } else if (!(var2 <= 0.0F) && !Float.isNaN(var2)) {
         if (var1 == 0) {
            var1 = 1;
         }

         this.fLoadFactor = var2;
         this.fTableSize = var1;
         this.fBuckets = new Entry[this.fTableSize];
         this.fThreshold = (int)((float)this.fTableSize * var2);
         this.fCount = 0;
      } else {
         throw new IllegalArgumentException("Illegal Load: " + var2);
      }
   }

   public SymbolTable(int var1) {
      this(var1, 0.75F);
   }

   public SymbolTable() {
      this(101, 0.75F);
   }

   public String addSymbol(String var1) {
      int var2 = this.hash(var1) % this.fTableSize;

      for(Entry var3 = this.fBuckets[var2]; var3 != null; var3 = var3.next) {
         if (var3.symbol.equals(var1)) {
            return var3.symbol;
         }
      }

      if (this.fCount >= this.fThreshold) {
         this.rehash();
         var2 = this.hash(var1) % this.fTableSize;
      }

      Entry var4 = new Entry(var1, this.fBuckets[var2]);
      this.fBuckets[var2] = var4;
      ++this.fCount;
      return var4.symbol;
   }

   public String addSymbol(char[] var1, int var2, int var3) {
      int var4 = this.hash(var1, var2, var3) % this.fTableSize;

      label31:
      for(Entry var5 = this.fBuckets[var4]; var5 != null; var5 = var5.next) {
         if (var3 == var5.characters.length) {
            for(int var6 = 0; var6 < var3; ++var6) {
               if (var1[var2 + var6] != var5.characters[var6]) {
                  continue label31;
               }
            }

            return var5.symbol;
         }
      }

      if (this.fCount >= this.fThreshold) {
         this.rehash();
         var4 = this.hash(var1, var2, var3) % this.fTableSize;
      }

      Entry var7 = new Entry(var1, var2, var3, this.fBuckets[var4]);
      this.fBuckets[var4] = var7;
      ++this.fCount;
      return var7.symbol;
   }

   public int hash(String var1) {
      return var1.hashCode() & 134217727;
   }

   public int hash(char[] var1, int var2, int var3) {
      int var4 = 0;

      for(int var5 = 0; var5 < var3; ++var5) {
         var4 = var4 * 31 + var1[var2 + var5];
      }

      return var4 & 134217727;
   }

   protected void rehash() {
      int var1 = this.fBuckets.length;
      Entry[] var2 = this.fBuckets;
      int var3 = var1 * 2 + 1;
      Entry[] var4 = new Entry[var3];
      this.fThreshold = (int)((float)var3 * this.fLoadFactor);
      this.fBuckets = var4;
      this.fTableSize = this.fBuckets.length;
      int var5 = var1;

      Entry var7;
      int var8;
      while(var5-- > 0) {
         for(Entry var6 = var2[var5]; var6 != null; var4[var8] = var7) {
            var7 = var6;
            var6 = var6.next;
            var8 = this.hash(var7.characters, 0, var7.characters.length) % var3;
            var7.next = var4[var8];
         }
      }

   }

   public boolean containsSymbol(String var1) {
      int var2 = this.hash(var1) % this.fTableSize;
      int var3 = var1.length();

      label27:
      for(Entry var4 = this.fBuckets[var2]; var4 != null; var4 = var4.next) {
         if (var3 == var4.characters.length) {
            for(int var5 = 0; var5 < var3; ++var5) {
               if (var1.charAt(var5) != var4.characters[var5]) {
                  continue label27;
               }
            }

            return true;
         }
      }

      return false;
   }

   public boolean containsSymbol(char[] var1, int var2, int var3) {
      int var4 = this.hash(var1, var2, var3) % this.fTableSize;

      label27:
      for(Entry var5 = this.fBuckets[var4]; var5 != null; var5 = var5.next) {
         if (var3 == var5.characters.length) {
            for(int var6 = 0; var6 < var3; ++var6) {
               if (var1[var2 + var6] != var5.characters[var6]) {
                  continue label27;
               }
            }

            return true;
         }
      }

      return false;
   }

   protected static final class Entry {
      public String symbol;
      public char[] characters;
      public Entry next;

      public Entry(String var1, Entry var2) {
         this.symbol = var1.intern();
         this.characters = new char[var1.length()];
         var1.getChars(0, this.characters.length, this.characters, 0);
         this.next = var2;
      }

      public Entry(char[] var1, int var2, int var3, Entry var4) {
         this.characters = new char[var3];
         System.arraycopy(var1, var2, this.characters, 0, var3);
         this.symbol = (new String(this.characters)).intern();
         this.next = var4;
      }
   }
}
