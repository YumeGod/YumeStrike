package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.LengthBase;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class LineHeightPropertyMaker extends SpaceProperty.Maker {
   public LineHeightPropertyMaker(int propId) {
      super(propId);
   }

   public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
      Property p = super.make(propertyList, value, fo);
      p.getSpace().setConditionality(EnumProperty.getInstance(118, "RETAIN"), true);
      p.getSpace().setPrecedence(EnumProperty.getInstance(53, "FORCE"), true);
      return p;
   }

   protected Property compute(PropertyList propertyList) throws PropertyException {
      Property specProp = propertyList.getNearestSpecified(this.propId);
      if (specProp != null) {
         String specVal = specProp.getSpecifiedValue();
         if (specVal != null) {
            return this.make(propertyList, specVal, propertyList.getParentFObj());
         }
      }

      return null;
   }

   public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      Numeric numval = ((Property)p).getNumeric();
      if (numval != null && numval.getDimension() == 0) {
         if (this.getPercentBase(propertyList) instanceof LengthBase) {
            Length base = ((LengthBase)this.getPercentBase(propertyList)).getBaseLength();
            if (base != null && base.isAbsolute()) {
               p = FixedLength.getInstance(numval.getNumericValue() * base.getNumericValue());
            } else {
               p = new PercentLength(numval.getNumericValue(), this.getPercentBase(propertyList));
            }
         }

         Property spaceProp = super.convertProperty((Property)p, propertyList, fo);
         spaceProp.setSpecifiedValue(String.valueOf(numval.getNumericValue()));
         return spaceProp;
      } else {
         return super.convertProperty((Property)p, propertyList, fo);
      }
   }
}
