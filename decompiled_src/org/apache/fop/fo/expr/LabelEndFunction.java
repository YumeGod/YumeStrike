package org.apache.fop.fo.expr;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.LengthBase;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.properties.PercentLength;
import org.apache.fop.fo.properties.Property;

public class LabelEndFunction extends FunctionBase {
   public int nbArgs() {
      return 0;
   }

   public Property eval(Property[] args, PropertyInfo pInfo) throws PropertyException {
      Length distance = pInfo.getPropertyList().get(195).getLength();
      Length separation = pInfo.getPropertyList().getNearestSpecified(196).getLength();

      PropertyList pList;
      for(pList = pInfo.getPropertyList(); pList != null && !(pList.getFObj() instanceof ListItem); pList = pList.getParentPropertyList()) {
      }

      if (pList == null) {
         throw new PropertyException("label-end() called from outside an fo:list-item");
      } else {
         Length startIndent = pList.get(233).getLength();
         LengthBase base = new LengthBase(pInfo.getPropertyList(), 4);
         PercentLength refWidth = new PercentLength(1.0, base);
         Numeric labelEnd = NumericOp.addition(distance, startIndent);
         labelEnd = NumericOp.subtraction(labelEnd, separation);
         labelEnd = NumericOp.subtraction(refWidth, labelEnd);
         return (Property)labelEnd;
      }
   }
}
