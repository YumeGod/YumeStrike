package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;

public class NumericOp {
   public static Numeric addition(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? addition2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(1, op1, op2));
   }

   public static Numeric addition2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      if (op1.getDimension() != op2.getDimension()) {
         throw new PropertyException("Can't subtract Numerics of different dimensions");
      } else {
         return numeric(op1.getNumericValue(context) + op2.getNumericValue(context), op1.getDimension());
      }
   }

   public static Numeric subtraction(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? subtraction2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(2, op1, op2));
   }

   public static Numeric subtraction2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      if (op1.getDimension() != op2.getDimension()) {
         throw new PropertyException("Can't subtract Numerics of different dimensions");
      } else {
         return numeric(op1.getNumericValue(context) - op2.getNumericValue(context), op1.getDimension());
      }
   }

   public static Numeric multiply(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? multiply2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(3, op1, op2));
   }

   public static Numeric multiply2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      return numeric(op1.getNumericValue(context) * op2.getNumericValue(context), op1.getDimension() + op2.getDimension());
   }

   public static Numeric divide(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? divide2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(4, op1, op2));
   }

   public static Numeric divide2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      return numeric(op1.getNumericValue(context) / op2.getNumericValue(context), op1.getDimension() - op2.getDimension());
   }

   public static Numeric modulo(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? modulo2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(5, op1, op2));
   }

   public static Numeric modulo2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      return numeric(op1.getNumericValue(context) % op2.getNumericValue(context), op1.getDimension());
   }

   public static Numeric abs(Numeric op) throws PropertyException {
      return (Numeric)(op.isAbsolute() ? abs2(op, (PercentBaseContext)null) : new RelativeNumericProperty(7, op));
   }

   public static Numeric abs2(Numeric op, PercentBaseContext context) throws PropertyException {
      return numeric(Math.abs(op.getNumericValue(context)), op.getDimension());
   }

   public static Numeric negate(Numeric op) throws PropertyException {
      return (Numeric)(op.isAbsolute() ? negate2(op, (PercentBaseContext)null) : new RelativeNumericProperty(6, op));
   }

   public static Numeric negate2(Numeric op, PercentBaseContext context) throws PropertyException {
      return numeric(-op.getNumericValue(context), op.getDimension());
   }

   public static Numeric max(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? max2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(8, op1, op2));
   }

   public static Numeric max2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      if (op1.getDimension() != op2.getDimension()) {
         throw new PropertyException("Arguments to max() must have same dimensions");
      } else {
         return op1.getNumericValue(context) > op2.getNumericValue(context) ? op1 : op2;
      }
   }

   public static Numeric min(Numeric op1, Numeric op2) throws PropertyException {
      return (Numeric)(op1.isAbsolute() && op2.isAbsolute() ? min2(op1, op2, (PercentBaseContext)null) : new RelativeNumericProperty(9, op1, op2));
   }

   public static Numeric min2(Numeric op1, Numeric op2, PercentBaseContext context) throws PropertyException {
      if (op1.getDimension() != op2.getDimension()) {
         throw new PropertyException("Arguments to min() must have same dimensions");
      } else {
         return op1.getNumericValue(context) <= op2.getNumericValue(context) ? op1 : op2;
      }
   }

   private static Numeric numeric(double value, int dimension) {
      return new NumericProperty(value, dimension);
   }
}
