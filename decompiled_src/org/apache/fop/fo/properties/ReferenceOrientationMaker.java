package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class ReferenceOrientationMaker extends NumberProperty.Maker {
   public ReferenceOrientationMaker(int propId) {
      super(propId);
   }

   public Property get(int subpropId, PropertyList propertyList, boolean tryInherit, boolean tryDefault) throws PropertyException {
      Property p = super.get(0, propertyList, tryInherit, tryDefault);
      int ro = 0;
      if (p != null) {
         ro = p.getNumeric().getValue();
      }

      if (Math.abs(ro) % 90 == 0 && Math.abs(ro) / 90 <= 3) {
         return p;
      } else {
         throw new PropertyException("Illegal property value: reference-orientation=\"" + ro + "\" " + "on " + propertyList.getFObj().getName());
      }
   }
}
