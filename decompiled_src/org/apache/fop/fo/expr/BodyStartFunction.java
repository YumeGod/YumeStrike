package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.properties.Property;

public class BodyStartFunction extends FunctionBase {
   public int nbArgs() {
      return 0;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Numeric distance = pInfo.getPropertyList().get(195).getNumeric();

      PropertyList pList;
      for(pList = pInfo.getPropertyList(); pList != null && !(pList.getFObj() instanceof ListItem); pList = pList.getParentPropertyList()) {
      }

      if (pList == null) {
         throw new PropertyException("body-start() called from outside an fo:list-item");
      } else {
         Numeric startIndent = pList.get(233).getNumeric();
         return (Property)NumericOp.addition(distance, startIndent);
      }
   }
}
