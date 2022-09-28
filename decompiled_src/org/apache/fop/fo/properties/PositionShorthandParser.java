package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;

public class PositionShorthandParser implements ShorthandParser {
   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) {
      int propVal = property.getEnum();
      if (propId == 1) {
         switch (propVal) {
            case 1:
               return EnumProperty.getInstance(1, "ABSOLUTE");
            case 51:
               return EnumProperty.getInstance(51, "FIXED");
            case 110:
            case 136:
               return EnumProperty.getInstance(9, "AUTO");
         }
      }

      if (propId == 203) {
         switch (propVal) {
            case 1:
               return EnumProperty.getInstance(136, "STATIC");
            case 51:
               return EnumProperty.getInstance(136, "STATIC");
            case 110:
               return EnumProperty.getInstance(110, "RELATIVE");
            case 136:
               return EnumProperty.getInstance(136, "STATIC");
         }
      }

      return null;
   }
}
