package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.properties.ColorProperty;
import org.apache.fop.fo.properties.Property;

class RGBColorFunction extends FunctionBase {
   public int nbArgs() {
      return 3;
   }

   public PercentBase getPercentBase() {
      return new RGBPercentBase();
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      return ColorProperty.getInstance(pInfo.getUserAgent(), "rgb(" + args[0] + "," + args[1] + "," + args[2] + ")");
   }

   private static class RGBPercentBase implements PercentBase {
      private RGBPercentBase() {
      }

      public int getDimension() {
         return 0;
      }

      public double getBaseValue() {
         return 255.0;
      }

      public int getBaseLength(PercentBaseContext context) {
         return 0;
      }

      // $FF: synthetic method
      RGBPercentBase(Object x0) {
         this();
      }
   }
}
