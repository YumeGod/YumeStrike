package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.properties.Property;

public class AbsFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public Property eval(Property[] args, PropertyInfo propInfo) throws PropertyException {
      Numeric num = args[0].getNumeric();
      if (num == null) {
         throw new PropertyException("Non numeric operand to abs function");
      } else {
         return (Property)NumericOp.abs(num);
      }
   }
}
