package org.apache.xmlgraphics.java2d;

import java.awt.geom.AffineTransform;

public abstract class TransformStackElement implements Cloneable {
   private TransformType type;
   private double[] transformParameters;

   protected TransformStackElement(TransformType type, double[] transformParameters) {
      this.type = type;
      this.transformParameters = transformParameters;
   }

   public Object clone() {
      TransformStackElement newElement = null;

      try {
         newElement = (TransformStackElement)super.clone();
      } catch (CloneNotSupportedException var3) {
      }

      double[] transformParameters = new double[this.transformParameters.length];
      System.arraycopy(this.transformParameters, 0, transformParameters, 0, transformParameters.length);
      newElement.transformParameters = transformParameters;
      return newElement;
   }

   public static TransformStackElement createTranslateElement(double tx, double ty) {
      return new TransformStackElement(TransformType.TRANSLATE, new double[]{tx, ty}) {
         boolean isIdentity(double[] parameters) {
            return parameters[0] == 0.0 && parameters[1] == 0.0;
         }
      };
   }

   public static TransformStackElement createRotateElement(double theta) {
      return new TransformStackElement(TransformType.ROTATE, new double[]{theta}) {
         boolean isIdentity(double[] parameters) {
            return Math.cos(parameters[0]) == 1.0;
         }
      };
   }

   public static TransformStackElement createScaleElement(double scaleX, double scaleY) {
      return new TransformStackElement(TransformType.SCALE, new double[]{scaleX, scaleY}) {
         boolean isIdentity(double[] parameters) {
            return parameters[0] == 1.0 && parameters[1] == 1.0;
         }
      };
   }

   public static TransformStackElement createShearElement(double shearX, double shearY) {
      return new TransformStackElement(TransformType.SHEAR, new double[]{shearX, shearY}) {
         boolean isIdentity(double[] parameters) {
            return parameters[0] == 0.0 && parameters[1] == 0.0;
         }
      };
   }

   public static TransformStackElement createGeneralTransformElement(AffineTransform txf) {
      double[] matrix = new double[6];
      txf.getMatrix(matrix);
      return new TransformStackElement(TransformType.GENERAL, matrix) {
         boolean isIdentity(double[] m) {
            return m[0] == 1.0 && m[2] == 0.0 && m[4] == 0.0 && m[1] == 0.0 && m[3] == 1.0 && m[5] == 0.0;
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

   public boolean concatenate(TransformStackElement stackElement) {
      boolean canConcatenate = false;
      if (this.type.toInt() == stackElement.type.toInt()) {
         canConcatenate = true;
         double[] var10000;
         switch (this.type.toInt()) {
            case 0:
               var10000 = this.transformParameters;
               var10000[0] += stackElement.transformParameters[0];
               var10000 = this.transformParameters;
               var10000[1] += stackElement.transformParameters[1];
               break;
            case 1:
               var10000 = this.transformParameters;
               var10000[0] += stackElement.transformParameters[0];
               break;
            case 2:
               var10000 = this.transformParameters;
               var10000[0] *= stackElement.transformParameters[0];
               var10000 = this.transformParameters;
               var10000[1] *= stackElement.transformParameters[1];
               break;
            case 3:
            default:
               canConcatenate = false;
               break;
            case 4:
               this.transformParameters = this.matrixMultiply(this.transformParameters, stackElement.transformParameters);
         }
      }

      return canConcatenate;
   }

   private double[] matrixMultiply(double[] matrix1, double[] matrix2) {
      double[] product = new double[6];
      AffineTransform transform1 = new AffineTransform(matrix1);
      transform1.concatenate(new AffineTransform(matrix2));
      transform1.getMatrix(product);
      return product;
   }
}
