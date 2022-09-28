package org.apache.fop.fo.expr;

import org.apache.fop.fo.properties.NumberProperty;
import org.apache.fop.fo.properties.Property;

class CeilingFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Number dbl = args[0].getNumber();
      if (dbl == null) {
         throw new PropertyException("Non number operand to ceiling function");
      } else {
         return NumberProperty.getInstance(Math.ceil(dbl.doubleValue()));
      }
   }
}
