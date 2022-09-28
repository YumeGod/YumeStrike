package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.TableColLength;

public class RelativeNumericProperty extends Property implements Length {
   public static final int ADDITION = 1;
   public static final int SUBTRACTION = 2;
   public static final int MULTIPLY = 3;
   public static final int DIVIDE = 4;
   public static final int MODULO = 5;
   public static final int NEGATE = 6;
   public static final int ABS = 7;
   public static final int MAX = 8;
   public static final int MIN = 9;
   private static String operations;
   private int operation;
   private Numeric op1;
   private Numeric op2 = null;
   private int dimension;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public RelativeNumericProperty(int operation, Numeric op1, Numeric op2) {
      this.operation = operation;
      this.op1 = op1;
      this.op2 = op2;
      switch (operation) {
         case 3:
            this.dimension = op1.getDimension() + op2.getDimension();
            break;
         case 4:
            this.dimension = op1.getDimension() - op2.getDimension();
            break;
         default:
            this.dimension = op1.getDimension();
      }

   }

   public RelativeNumericProperty(int operation, Numeric op) {
      this.operation = operation;
      this.op1 = op;
      this.dimension = op.getDimension();
   }

   private Numeric getResolved(PercentBaseContext context) throws PropertyException {
      switch (this.operation) {
         case 1:
            return NumericOp.addition2(this.op1, this.op2, context);
         case 2:
            return NumericOp.subtraction2(this.op1, this.op2, context);
         case 3:
            return NumericOp.multiply2(this.op1, this.op2, context);
         case 4:
            return NumericOp.divide2(this.op1, this.op2, context);
         case 5:
            return NumericOp.modulo2(this.op1, this.op2, context);
         case 6:
            return NumericOp.negate2(this.op1, context);
         case 7:
            return NumericOp.abs2(this.op1, context);
         case 8:
            return NumericOp.max2(this.op1, this.op2, context);
         case 9:
            return NumericOp.min2(this.op1, this.op2, context);
         default:
            throw new PropertyException("Unknown expr operation " + this.operation);
      }
   }

   public double getNumericValue() throws PropertyException {
      return this.getResolved((PercentBaseContext)null).getNumericValue((PercentBaseContext)null);
   }

   public double getNumericValue(PercentBaseContext context) throws PropertyException {
      return this.getResolved(context).getNumericValue(context);
   }

   public int getDimension() {
      return this.dimension;
   }

   public boolean isAbsolute() {
      return false;
   }

   public Length getLength() {
      if (this.dimension == 1) {
         return this;
      } else {
         log.error("Can't create length with dimension " + this.dimension);
         return null;
      }
   }

   public Numeric getNumeric() {
      return this;
   }

   public int getValue() {
      try {
         return (int)this.getNumericValue();
      } catch (PropertyException var2) {
         log.error(var2);
         return 0;
      }
   }

   public int getValue(PercentBaseContext context) {
      try {
         return (int)this.getNumericValue(context);
      } catch (PropertyException var3) {
         log.error(var3);
         return 0;
      }
   }

   public double getTableUnits() {
      double tu1 = 0.0;
      double tu2 = 0.0;
      if (this.op1 instanceof RelativeNumericProperty) {
         tu1 = ((RelativeNumericProperty)this.op1).getTableUnits();
      } else if (this.op1 instanceof TableColLength) {
         tu1 = ((TableColLength)this.op1).getTableUnits();
      }

      if (this.op2 instanceof RelativeNumericProperty) {
         tu2 = ((RelativeNumericProperty)this.op2).getTableUnits();
      } else if (this.op2 instanceof TableColLength) {
         tu2 = ((TableColLength)this.op2).getTableUnits();
      }

      if (tu1 != 0.0 && tu2 != 0.0) {
         switch (this.operation) {
            case 1:
               return tu1 + tu2;
            case 2:
               return tu1 - tu2;
            case 3:
               return tu1 * tu2;
            case 4:
               return tu1 / tu2;
            case 5:
               return tu1 % tu2;
            case 6:
            case 7:
            default:
               if (!$assertionsDisabled) {
                  throw new AssertionError();
               }
               break;
            case 8:
               return Math.max(tu1, tu2);
            case 9:
               return Math.min(tu1, tu2);
         }
      } else {
         if (tu1 != 0.0) {
            switch (this.operation) {
               case 6:
                  return -tu1;
               case 7:
                  return Math.abs(tu1);
               default:
                  return tu1;
            }
         }

         if (tu2 != 0.0) {
            return tu2;
         }
      }

      return 0.0;
   }

   public String toString() {
      switch (this.operation) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            return "(" + this.op1 + " " + operations.charAt(this.operation) + this.op2 + ")";
         case 6:
            return "-" + this.op1;
         case 7:
            return "abs(" + this.op1 + ")";
         case 8:
            return "max(" + this.op1 + ", " + this.op2 + ")";
         case 9:
            return "min(" + this.op1 + ", " + this.op2 + ")";
         default:
            return "unknown operation " + this.operation;
      }
   }

   static {
      $assertionsDisabled = !RelativeNumericProperty.class.desiredAssertionStatus();
      operations = " +-*/%";
   }
}
