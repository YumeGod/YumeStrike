package org.apache.fop.fo.expr;

import org.apache.fop.fo.FOPropertyMapping;
import org.apache.fop.fo.properties.Property;

public class FromParentFunction extends FunctionBase {
   public int nbArgs() {
      return 1;
   }

   public boolean padArgsWithPropertyName() {
      return true;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      String propName = args[0].getString();
      if (propName == null) {
         throw new PropertyException("Incorrect parameter to from-parent function");
      } else {
         int propId = FOPropertyMapping.getPropertyId(propName);
         if (propId < 0) {
            throw new PropertyException("Unknown property name used with inherited-property-value function: " + propName);
         } else {
            return pInfo.getPropertyList().getFromParent(propId);
         }
      }
   }
}
