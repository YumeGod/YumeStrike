package org.apache.batik.ext.awt.image;

public class DiscreteTransfer implements TransferFunction {
   public byte[] lutData;
   public int[] tableValues;
   private int n;

   public DiscreteTransfer(int[] var1) {
      this.tableValues = var1;
      this.n = var1.length;
   }

   private void buildLutData() {
      this.lutData = new byte[256];

      for(int var2 = 0; var2 <= 255; ++var2) {
         int var1 = (int)Math.floor((double)((float)(var2 * this.n) / 255.0F));
         if (var1 == this.n) {
            var1 = this.n - 1;
         }

         this.lutData[var2] = (byte)(this.tableValues[var1] & 255);
      }

   }

   public byte[] getLookupTable() {
      this.buildLutData();
      return this.lutData;
   }
}
