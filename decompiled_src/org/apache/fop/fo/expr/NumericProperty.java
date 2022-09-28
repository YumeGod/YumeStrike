package org.apache.fop.fo.expr;

import java.awt.Color;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.Property;

public class NumericProperty extends Property implements Numeric, Length {
   private double value;
   private int dim;

   protected NumericProperty(double value, int dim) {
      this.value = value;
      this.dim = dim;
   }

   public int getDimension() {
      return this.dim;
   }

   public double getNumericValue() {
      return this.value;
   }

   public double getNumericValue(PercentBaseContext context) {
      return this.value;
   }

   public boolean isAbsolute() {
      return true;
   }

   public Numeric getNumeric() {
      return this;
   }

   public Number getNumber() {
      return new Double(this.value);
   }

   public int getValue() {
      return (int)this.value;
   }

   public int getValue(PercentBaseContext context) {
      return (int)this.value;
   }

   public Length getLength() {
      if (this.dim == 1) {
         return this;
      } else {
         log.error("Can't create length with dimension " + this.dim);
         return null;
      }
   }

   public Color getColor(FOUserAgent foUserAgent) {
      return null;
   }

   public Object getObject() {
      return this;
   }

   public String toString() {
      return this.dim == 1 ? (int)this.value + "mpt" : this.value + "^" + this.dim;
   }
}
