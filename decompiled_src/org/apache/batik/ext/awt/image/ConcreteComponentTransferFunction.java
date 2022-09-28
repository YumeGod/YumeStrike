package org.apache.batik.ext.awt.image;

public class ConcreteComponentTransferFunction implements ComponentTransferFunction {
   private int type;
   private float slope;
   private float[] tableValues;
   private float intercept;
   private float amplitude;
   private float exponent;
   private float offset;

   private ConcreteComponentTransferFunction() {
   }

   public static ComponentTransferFunction getIdentityTransfer() {
      ConcreteComponentTransferFunction var0 = new ConcreteComponentTransferFunction();
      var0.type = 0;
      return var0;
   }

   public static ComponentTransferFunction getTableTransfer(float[] var0) {
      ConcreteComponentTransferFunction var1 = new ConcreteComponentTransferFunction();
      var1.type = 1;
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else if (var0.length < 2) {
         throw new IllegalArgumentException();
      } else {
         var1.tableValues = new float[var0.length];
         System.arraycopy(var0, 0, var1.tableValues, 0, var0.length);
         return var1;
      }
   }

   public static ComponentTransferFunction getDiscreteTransfer(float[] var0) {
      ConcreteComponentTransferFunction var1 = new ConcreteComponentTransferFunction();
      var1.type = 2;
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else if (var0.length < 2) {
         throw new IllegalArgumentException();
      } else {
         var1.tableValues = new float[var0.length];
         System.arraycopy(var0, 0, var1.tableValues, 0, var0.length);
         return var1;
      }
   }

   public static ComponentTransferFunction getLinearTransfer(float var0, float var1) {
      ConcreteComponentTransferFunction var2 = new ConcreteComponentTransferFunction();
      var2.type = 3;
      var2.slope = var0;
      var2.intercept = var1;
      return var2;
   }

   public static ComponentTransferFunction getGammaTransfer(float var0, float var1, float var2) {
      ConcreteComponentTransferFunction var3 = new ConcreteComponentTransferFunction();
      var3.type = 4;
      var3.amplitude = var0;
      var3.exponent = var1;
      var3.offset = var2;
      return var3;
   }

   public int getType() {
      return this.type;
   }

   public float getSlope() {
      return this.slope;
   }

   public float[] getTableValues() {
      return this.tableValues;
   }

   public float getIntercept() {
      return this.intercept;
   }

   public float getAmplitude() {
      return this.amplitude;
   }

   public float getExponent() {
      return this.exponent;
   }

   public float getOffset() {
      return this.offset;
   }
}
