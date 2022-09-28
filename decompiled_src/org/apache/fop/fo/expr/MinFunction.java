package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.properties.Property;

public class MinFunction extends FunctionBase {
   public int nbArgs() {
      return 2;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Numeric n1 = args[0].getNumeric();
      Numeric n2 = args[1].getNumeric();
      if (n1 != null && n2 != null) {
         return (Property)NumericOp.min(n1, n2);
      } else {
         throw new PropertyException("Non numeric operands to min function");
      }
   }
}
