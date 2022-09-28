package org.apache.fop.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormatCache {
   private static final String BASE_FORMAT = "0.################";
   private static final ThreadLocal[] DECIMAL_FORMAT_CACHE = new DecimalFormatThreadLocal[17];

   public static DecimalFormat getDecimalFormat(int dec) {
      if (dec >= 0 && dec < DECIMAL_FORMAT_CACHE.length) {
         return (DecimalFormat)DECIMAL_FORMAT_CACHE[dec].get();
      } else {
         throw new IllegalArgumentException("Parameter dec must be between 1 and " + (DECIMAL_FORMAT_CACHE.length + 1));
      }
   }

   static {
      int i = 0;

      for(int c = DECIMAL_FORMAT_CACHE.length; i < c; ++i) {
         DECIMAL_FORMAT_CACHE[i] = new DecimalFormatThreadLocal(i);
      }

   }

   private static class DecimalFormatThreadLocal extends ThreadLocal {
      private int dec;

      public DecimalFormatThreadLocal(int dec) {
         this.dec = dec;
      }

      protected synchronized Object initialValue() {
         String s = "0";
         if (this.dec > 0) {
            s = "0.################".substring(0, this.dec + 2);
         }

         DecimalFormat df = new DecimalFormat(s, new DecimalFormatSymbols(Locale.US));
         return df;
      }
   }
}
