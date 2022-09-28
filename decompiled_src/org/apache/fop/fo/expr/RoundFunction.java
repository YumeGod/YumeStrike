package org.apache.fop.fo.expr;

import org.apache.fop.fo.properties.NumberProperty;
import org.apache.fop.fo.properties.Property;

class RoundFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Number dbl = args[0].getNumber();
      if (dbl == null) {
         throw new PropertyException("Non number operand to round function");
      } else {
         double n = dbl.doubleValue();
         double r = Math.floor(n + 0.5);
         if (r == 0.0 && n < 0.0) {
            r = -r;
         }

         return NumberProperty.getInstance(r);
      }
   }
}
