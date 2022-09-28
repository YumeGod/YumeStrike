package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.PercentBaseContext;

public class EnumLength extends LengthProperty {
   private Property enumProperty;

   public EnumLength(Property enumProperty) {
      this.enumProperty = enumProperty;
   }

   public int getEnum() {
      return this.enumProperty.getEnum();
   }

   public boolean isAbsolute() {
      return false;
   }

   public int getValue() {
      log.error("getValue() called on " + this.enumProperty + " length");
      return 0;
   }

   public int getValue(PercentBaseContext context) {
      log.error("getValue() called on " + this.enumProperty + " length");
      return 0;
   }

   public double getNumericValue() {
      log.error("getNumericValue() called on " + this.enumProperty + " number");
      return 0.0;
   }

   public double getNumericValue(PercentBaseContext context) {
      log.error("getNumericValue() called on " + this.enumProperty + " number");
      return 0.0;
   }

   public String getString() {
      return this.enumProperty.toString();
   }

   public Object getObject() {
      return this.enumProperty.getObject();
   }
}
