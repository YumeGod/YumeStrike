package org.apache.batik.ext.awt.g2d;

import java.awt.geom.AffineTransform;

public abstract class TransformStackElement implements Cloneable {
   private TransformType type;
   private double[] transformParameters;

   protected TransformStackElement(TransformType var1, double[] var2) {
      this.type = var1;
      this.transformParameters = var2;
   }

   public Object clone() {
      TransformStackElement var1 = null;

      try {
         var1 = (TransformStackElement)super.clone();
      } catch (CloneNotSupportedException var3) {
      }

      double[] var2 = new double[this.transformParameters.length];
      System.arraycopy(this.transformParameters, 0, var2, 0, var2.length);
      var1.transformParameters = var2;
      return var1;
   }

   public static TransformStackElement createTranslateElement(double var0, double var2) {
      return new TransformStackElement(TransformType.TRANSLATE, new double[]{var0, var2}) {
         boolean isIdentity(double[] var1) {
            return var1[0] == 0.0 && var1[1] == 0.0;
         }
      };
   }

   public static TransformStackElement createRotateElement(double var0) {
      return new TransformStackElement(TransformType.ROTATE, new double[]{var0}) {
         boolean isIdentity(double[] var1) {
            return Math.cos(var1[0]) == 1.0;
         }
      };
   }

   public static TransformStackElement createScaleElement(double var0, double var2) {
      return new TransformStackElement(TransformType.SCALE, new double[]{var0, var2}) {
         boolean isIdentity(double[] var1) {
            return var1[0] == 1.0 && var1[1] == 1.0;
         }
      };
   }

   public static TransformStackElement createShearElement(double var0, double var2) {
      return new TransformStackElement(TransformType.SHEAR, new double[]{var0, var2}) {
         boolean isIdentity(double[] var1) {
            return var1[0] == 0.0 && var1[1] == 0.0;
         }
      };
   }

   public static TransformStackElement createGeneralTransformElement(AffineTransform var0) {
      double[] var1 = new double[6];
      var0.getMatrix(var1);
      return new TransformStackElement(TransformType.GENERAL, var1) {
         boolean isIdentity(double[] var1) {
            return var1[0] == 1.0 && var1[2] == 0.0 && var1[4] == 0.0 && var1[1] == 0.0 && var1[3] == 1.0 && var1[5] == 0.0;
         }
      };
   }

   abstract boolean isIdentity(double[] var1);

   public boolean isIdentity() {
      return this.isIdentity(this.transformParameters);
   }

   public double[] getTransformParameters() {
      return this.transformParameters;
   }

   public TransformType getType() {
      return this.type;
   }

   public boolean concatenate(TransformStackElement var1) {
      boolean var2 = false;
      if (this.type.toInt() == var1.type.toInt()) {
         var2 = true;
         double[] var10000;
         switch (this.type.toInt()) {
            case 0:
               var10000 = this.transformParameters;
               var10000[0] += var1.transformParameters[0];
               var10000 = this.transformParameters;
               var10000[1] += var1.transformParameters[1];
               break;
            case 1:
               var10000 = this.transformParameters;
               var10000[0] += var1.transformParameters[0];
               break;
            case 2:
               var10000 = this.transformParameters;
               var10000[0] *= var1.transformParameters[0];
               var10000 = this.transformParameters;
               var10000[1] *= var1.transformParameters[1];
               break;
            case 3:
            default:
               var2 = false;
               break;
            case 4:
               this.transformParameters = this.matrixMultiply(this.transformParameters, var1.transformParameters);
         }
      }

      return var2;
   }

   private double[] matrixMultiply(double[] var1, double[] var2) {
      double[] var3 = new double[6];
      AffineTransform var4 = new AffineTransform(var1);
      var4.concatenate(new AffineTransform(var2));
      var4.getMatrix(var3);
      return var3;
   }
}
