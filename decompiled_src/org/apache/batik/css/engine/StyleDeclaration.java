package org.apache.batik.css.engine;

import org.apache.batik.css.engine.value.Value;

public class StyleDeclaration {
   protected static final int INITIAL_LENGTH = 8;
   protected Value[] values = new Value[8];
   protected int[] indexes = new int[8];
   protected boolean[] priorities = new boolean[8];
   protected int count;

   public int size() {
      return this.count;
   }

   public Value getValue(int var1) {
      return this.values[var1];
   }

   public int getIndex(int var1) {
      return this.indexes[var1];
   }

   public boolean getPriority(int var1) {
      return this.priorities[var1];
   }

   public void remove(int var1) {
      --this.count;
      int var2 = var1 + 1;
      int var4 = this.count - var1;
      System.arraycopy(this.values, var2, this.values, var1, var4);
      System.arraycopy(this.indexes, var2, this.indexes, var1, var4);
      System.arraycopy(this.priorities, var2, this.priorities, var1, var4);
      this.values[this.count] = null;
      this.indexes[this.count] = 0;
      this.priorities[this.count] = false;
   }

   public void put(int var1, Value var2, int var3, boolean var4) {
      this.values[var1] = var2;
      this.indexes[var1] = var3;
      this.priorities[var1] = var4;
   }

   public void append(Value var1, int var2, boolean var3) {
      if (this.values.length == this.count) {
         Value[] var4 = new Value[this.count * 2];
         int[] var5 = new int[this.count * 2];
         boolean[] var6 = new boolean[this.count * 2];
         System.arraycopy(this.values, 0, var4, 0, this.count);
         System.arraycopy(this.indexes, 0, var5, 0, this.count);
         System.arraycopy(this.priorities, 0, var6, 0, this.count);
         this.values = var4;
         this.indexes = var5;
         this.priorities = var6;
      }

      for(int var7 = 0; var7 < this.count; ++var7) {
         if (this.indexes[var7] == var2) {
            if (var3 || this.priorities[var7] == var3) {
               this.values[var7] = var1;
               this.priorities[var7] = var3;
            }

            return;
         }
      }

      this.values[this.count] = var1;
      this.indexes[this.count] = var2;
      this.priorities[this.count] = var3;
      ++this.count;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer(this.count * 8);

      for(int var3 = 0; var3 < this.count; ++var3) {
         var2.append(var1.getPropertyName(this.indexes[var3]));
         var2.append(": ");
         var2.append(this.values[var3]);
         var2.append(";\n");
      }

      return var2.toString();
   }
}
