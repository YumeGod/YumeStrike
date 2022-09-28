package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.PercentBaseContext;

public final class FixedLength extends LengthProperty {
   public static final String PICA = "pc";
   public static final String POINT = "pt";
   public static final String MM = "mm";
   public static final String CM = "cm";
   public static final String INCH = "in";
   public static final String MPT = "mpt";
   private static final PropertyCache cache;
   public static final FixedLength ZERO_FIXED_LENGTH;
   private int millipoints;

   private FixedLength(double numUnits, String units, float res) {
      this.millipoints = convert(numUnits, units, res);
   }

   public static FixedLength getInstance(double numUnits, String units, float sourceResolution) {
      return numUnits == 0.0 ? ZERO_FIXED_LENGTH : (FixedLength)cache.fetch((Property)(new FixedLength(numUnits, units, sourceResolution)));
   }

   public static FixedLength getInstance(double numUnits, String units) {
      return getInstance(numUnits, units, 1.0F);
   }

   public static FixedLength getInstance(double numUnits) {
      return getInstance(numUnits, "mpt", 1.0F);
   }

   private static int convert(double dvalue, String unit, float res) {
      if ("px".equals(unit)) {
         dvalue *= (double)(res * 1000.0F);
      } else if ("in".equals(unit)) {
         dvalue *= 72000.0;
      } else if ("cm".equals(unit)) {
         dvalue *= 28346.4567;
      } else if ("mm".equals(unit)) {
         dvalue *= 2834.64567;
      } else if ("pt".equals(unit)) {
         dvalue *= 1000.0;
      } else if ("pc".equals(unit)) {
         dvalue *= 12000.0;
      } else if (!"mpt".equals(unit)) {
         dvalue = 0.0;
         log.error("Unknown length unit '" + unit + "'");
      }

      return (int)dvalue;
   }

   public int getValue() {
      return this.millipoints;
   }

   public int getValue(PercentBaseContext context) {
      return this.millipoints;
   }

   public double getNumericValue() {
      return (double)this.millipoints;
   }

   public double getNumericValue(PercentBaseContext context) {
      return (double)this.millipoints;
   }

   public boolean isAbsolute() {
      return true;
   }

   public String toString() {
      return this.millipoints + "mpt";
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj instanceof FixedLength) {
         return ((FixedLength)obj).millipoints == this.millipoints;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.millipoints;
   }

   static {
      cache = new PropertyCache(FixedLength.class);
      ZERO_FIXED_LENGTH = new FixedLength(0.0, "mpt", 1.0F);
   }
}
