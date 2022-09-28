package org.apache.batik.ext.awt.image;

public class TableTransfer implements TransferFunction {
   public byte[] lutData;
   public int[] tableValues;
   private int n;

   public TableTransfer(int[] var1) {
      this.tableValues = var1;
      this.n = var1.length;
   }

   private void buildLutData() {
      this.lutData = new byte[256];

      for(int var1 = 0; var1 <= 255; ++var1) {
         float var2 = (float)(var1 * (this.n - 1)) / 255.0F;
         int var4 = (int)Math.floor((double)var2);
         int var5 = var4 + 1 > this.n - 1 ? this.n - 1 : var4 + 1;
         float var3 = var2 - (float)var4;
         this.lutData[var1] = (byte)((int)((float)this.tableValues[var4] + var3 * (float)(this.tableValues[var5] - this.tableValues[var4])) & 255);
      }

   }

   public byte[] getLookupTable() {
      this.buildLutData();
      return this.lutData;
   }
}
