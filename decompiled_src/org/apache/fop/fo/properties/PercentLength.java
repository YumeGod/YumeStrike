package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.expr.PropertyException;

public class PercentLength extends LengthProperty {
   private double factor;
   private PercentBase lbase = null;
   private double resolvedValue;

   public PercentLength(double factor, PercentBase lbase) {
      this.factor = factor;
      this.lbase = lbase;
   }

   public PercentBase getBaseLength() {
      return this.lbase;
   }

   protected double getPercentage() {
      return this.factor * 100.0;
   }

   public boolean isAbsolute() {
      return false;
   }

   public double getNumericValue() {
      return this.getNumericValue((PercentBaseContext)null);
   }

   public double getNumericValue(PercentBaseContext context) {
      try {
         this.resolvedValue = this.factor * (double)this.lbase.getBaseLength(context);
         return this.resolvedValue;
      } catch (PropertyException var3) {
         log.error(var3);
         return 0.0;
      }
   }

   public String getString() {
      return this.factor * 100.0 + "%";
   }

   public int getValue() {
      return (int)this.getNumericValue();
   }

   public int getValue(PercentBaseContext context) {
      return (int)this.getNumericValue(context);
   }

   public String toString() {
      StringBuffer sb = (new StringBuffer(PercentLength.class.getName())).append("[factor=").append(this.factor).append(",lbase=").append(this.lbase).append("]");
      return sb.toString();
   }
}
