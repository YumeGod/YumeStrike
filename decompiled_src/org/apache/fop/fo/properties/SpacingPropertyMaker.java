package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class SpacingPropertyMaker extends SpaceProperty.Maker {
   public SpacingPropertyMaker(int propId) {
      super(propId);
   }

   public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      return p.getEnum() == 97 ? p : super.convertProperty(p, propertyList, fo);
   }
}
