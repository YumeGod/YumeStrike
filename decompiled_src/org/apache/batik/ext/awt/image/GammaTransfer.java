package org.apache.batik.ext.awt.image;

public class GammaTransfer implements TransferFunction {
   public byte[] lutData;
   public float amplitude;
   public float exponent;
   public float offset;

   public GammaTransfer(float var1, float var2, float var3) {
      this.amplitude = var1;
      this.exponent = var2;
      this.offset = var3;
   }

   private void buildLutData() {
      this.lutData = new byte[256];

      for(int var1 = 0; var1 <= 255; ++var1) {
         int var2 = (int)Math.round(255.0 * ((double)this.amplitude * Math.pow((double)((float)var1 / 255.0F), (double)this.exponent) + (double)this.offset));
         if (var2 > 255) {
            var2 = -1;
         } else if (var2 < 0) {
            var2 = 0;
         }

         this.lutData[var1] = (byte)(var2 & 255);
      }

   }

   public byte[] getLookupTable() {
      this.buildLutData();
      return this.lutData;
   }
}
