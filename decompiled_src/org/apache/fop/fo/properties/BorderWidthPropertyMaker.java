package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class BorderWidthPropertyMaker extends LengthProperty.Maker {
   int borderStyleId = 0;

   public BorderWidthPropertyMaker(int propId) {
      super(propId);
   }

   public void setBorderStyleId(int borderStyleId) {
      this.borderStyleId = borderStyleId;
   }

   public Property get(int subpropId, PropertyList propertyList, boolean bTryInherit, boolean bTryDefault) throws PropertyException {
      Property p = super.get(subpropId, propertyList, bTryInherit, bTryDefault);
      Property style = propertyList.get(this.borderStyleId);
      return (Property)(style.getEnum() == 95 ? FixedLength.ZERO_FIXED_LENGTH : p);
   }
}
