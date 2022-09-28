package org.apache.batik.ext.awt.image;

public class LinearTransfer implements TransferFunction {
   public byte[] lutData;
   public float slope;
   public float intercept;

   public LinearTransfer(float var1, float var2) {
      this.slope = var1;
      this.intercept = var2;
   }

   private void buildLutData() {
      this.lutData = new byte[256];
      float var3 = this.intercept * 255.0F + 0.5F;

      for(int var1 = 0; var1 <= 255; ++var1) {
         int var2 = (int)(this.slope * (float)var1 + var3);
         if (var2 < 0) {
            var2 = 0;
         } else if (var2 > 255) {
            var2 = 255;
         }

         this.lutData[var1] = (byte)(255 & var2);
      }

   }

   public byte[] getLookupTable() {
      this.buildLutData();
      return this.lutData;
   }
}
