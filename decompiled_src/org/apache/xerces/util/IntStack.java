package org.apache.xerces.util;

public final class IntStack {
   private int fDepth;
   private int[] fData;

   public int size() {
      return this.fDepth;
   }

   public void push(int var1) {
      this.ensureCapacity(this.fDepth + 1);
      this.fData[this.fDepth++] = var1;
   }

   public int peek() {
      return this.fData[this.fDepth - 1];
   }

   public int elementAt(int var1) {
      return this.fData[var1];
   }

   public int pop() {
      return this.fData[--this.fDepth];
   }

   public void clear() {
      this.fDepth = 0;
   }

   public void print() {
      System.out.print('(');
      System.out.print(this.fDepth);
      System.out.print(") {");

      for(int var1 = 0; var1 < this.fDepth; ++var1) {
         if (var1 == 3) {
            System.out.print(" ...");
            break;
         }

         System.out.print(' ');
         System.out.print(this.fData[var1]);
         if (var1 < this.fDepth - 1) {
            System.out.print(',');
         }
      }

      System.out.print(" }");
      System.out.println();
   }

   private void ensureCapacity(int var1) {
      if (this.fData == null) {
         this.fData = new int[32];
      } else if (this.fData.length <= var1) {
         int[] var2 = new int[this.fData.length * 2];
         System.arraycopy(this.fData, 0, var2, 0, this.fData.length);
         this.fData = var2;
      }

   }
}
