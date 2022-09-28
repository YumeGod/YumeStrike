package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class SpacePropertyMaker extends CorrespondingPropertyMaker {
   public SpacePropertyMaker(PropertyMaker baseMaker) {
      super(baseMaker);
   }

   public Property compute(PropertyList propertyList) throws PropertyException {
      Property prop = super.compute(propertyList);
      if (prop != null && prop instanceof SpaceProperty) {
         ((SpaceProperty)prop).setConditionality(EnumProperty.getInstance(118, "RETAIN"), false);
      }

      return prop;
   }
}
