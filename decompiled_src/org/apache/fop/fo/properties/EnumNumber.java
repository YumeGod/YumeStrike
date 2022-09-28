package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.expr.PropertyException;

public final class EnumNumber extends Property implements Numeric {
   private static final PropertyCache cache;
   private final EnumProperty enumProperty;

   private EnumNumber(Property enumProperty) {
      this.enumProperty = (EnumProperty)enumProperty;
   }

   public static EnumNumber getInstance(Property enumProperty) {
      return (EnumNumber)cache.fetch((Property)(new EnumNumber((EnumProperty)enumProperty)));
   }

   public int getEnum() {
      return this.enumProperty.getEnum();
   }

   public String getString() {
      return this.enumProperty.toString();
   }

   public Object getObject() {
      return this.enumProperty.getObject();
   }

   public boolean equals(Object obj) {
      if (obj instanceof EnumNumber) {
         return ((EnumNumber)obj).enumProperty == this.enumProperty;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.enumProperty.hashCode();
   }

   public int getDimension() {
      return 0;
   }

   public boolean isAbsolute() {
      return true;
   }

   public double getNumericValue(PercentBaseContext context) throws PropertyException {
      log.error("getNumericValue() called on " + this.enumProperty + " number");
      return 0.0;
   }

   public int getValue(PercentBaseContext context) {
      log.error("getValue() called on " + this.enumProperty + " number");
      return 0;
   }

   public int getValue() {
      log.error("getValue() called on " + this.enumProperty + " number");
      return 0;
   }

   public double getNumericValue() {
      log.error("getNumericValue() called on " + this.enumProperty + " number");
      return 0.0;
   }

   public Numeric getNumeric() {
      return this;
   }

   static {
      cache = new PropertyCache(EnumNumber.class);
   }
}
