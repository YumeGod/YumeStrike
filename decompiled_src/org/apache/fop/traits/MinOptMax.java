package org.apache.fop.traits;

import java.io.Serializable;

public final class MinOptMax implements Serializable {
   private static final long serialVersionUID = -4791524475122206142L;
   public static final MinOptMax ZERO;
   private final int min;
   private final int opt;
   private final int max;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public static MinOptMax getInstance(int min, int opt, int max) {
      if (min > opt) {
         throw new IllegalArgumentException("min (" + min + ") > opt (" + opt + ")");
      } else if (max < opt) {
         throw new IllegalArgumentException("max (" + max + ") < opt (" + opt + ")");
      } else {
         return new MinOptMax(min, opt, max);
      }
   }

   public static MinOptMax getInstance(int value) {
      return new MinOptMax(value, value, value);
   }

   private MinOptMax(int min, int opt, int max) {
      if ($assertionsDisabled || min <= opt && opt <= max) {
         this.min = min;
         this.opt = opt;
         this.max = max;
      } else {
         throw new AssertionError();
      }
   }

   public int getMin() {
      return this.min;
   }

   public int getOpt() {
      return this.opt;
   }

   public int getMax() {
      return this.max;
   }

   public int getShrink() {
      return this.opt - this.min;
   }

   public int getStretch() {
      return this.max - this.opt;
   }

   public MinOptMax plus(MinOptMax operand) {
      return new MinOptMax(this.min + operand.min, this.opt + operand.opt, this.max + operand.max);
   }

   public MinOptMax plus(int value) {
      return new MinOptMax(this.min + value, this.opt + value, this.max + value);
   }

   public MinOptMax minus(MinOptMax operand) {
      this.checkCompatibility(this.getShrink(), operand.getShrink(), "shrink");
      this.checkCompatibility(this.getStretch(), operand.getStretch(), "stretch");
      return new MinOptMax(this.min - operand.min, this.opt - operand.opt, this.max - operand.max);
   }

   private void checkCompatibility(int thisElasticity, int operandElasticity, String msge) {
      if (thisElasticity < operandElasticity) {
         throw new ArithmeticException("Cannot subtract a MinOptMax from another MinOptMax that has less " + msge + " (" + thisElasticity + " < " + operandElasticity + ")");
      }
   }

   public MinOptMax minus(int value) {
      return new MinOptMax(this.min - value, this.opt - value, this.max - value);
   }

   /** @deprecated */
   public MinOptMax plusMin(int minOperand) {
      return getInstance(this.min + minOperand, this.opt, this.max);
   }

   /** @deprecated */
   public MinOptMax minusMin(int minOperand) {
      return getInstance(this.min - minOperand, this.opt, this.max);
   }

   /** @deprecated */
   public MinOptMax plusMax(int maxOperand) {
      return getInstance(this.min, this.opt, this.max + maxOperand);
   }

   /** @deprecated */
   public MinOptMax minusMax(int maxOperand) {
      return getInstance(this.min, this.opt, this.max - maxOperand);
   }

   public MinOptMax mult(int factor) {
      if (factor < 0) {
         throw new IllegalArgumentException("factor < 0; was: " + factor);
      } else {
         return factor == 1 ? this : getInstance(this.min * factor, this.opt * factor, this.max * factor);
      }
   }

   public boolean isNonZero() {
      return this.min != 0 || this.max != 0;
   }

   public boolean isStiff() {
      return this.min == this.max;
   }

   public boolean isElastic() {
      return this.min != this.opt || this.opt != this.max;
   }

   public MinOptMax extendMinimum(int newMin) {
      if (this.min < newMin) {
         int newOpt = Math.max(newMin, this.opt);
         int newMax = Math.max(newOpt, this.max);
         return getInstance(newMin, newOpt, newMax);
      } else {
         return this;
      }
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         MinOptMax minOptMax = (MinOptMax)obj;
         return this.opt == minOptMax.opt && this.max == minOptMax.max && this.min == minOptMax.min;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.min;
      result = 31 * result + this.opt;
      result = 31 * result + this.max;
      return result;
   }

   public String toString() {
      return "MinOptMax[min = " + this.min + ", opt = " + this.opt + ", max = " + this.max + "]";
   }

   static {
      $assertionsDisabled = !MinOptMax.class.desiredAssertionStatus();
      ZERO = getInstance(0);
   }
}
