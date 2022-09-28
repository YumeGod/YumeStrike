package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class WhiteSpaceShorthandParser implements ShorthandParser {
   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      switch (property.getEnum()) {
         case 179:
            switch (propId) {
               case 143:
               case 262:
                  return EnumProperty.getInstance(108, "PRESERVE");
               case 261:
                  return EnumProperty.getInstance(48, "FALSE");
               case 266:
                  return EnumProperty.getInstance(93, "NO_WRAP");
            }
         case 93:
            if (propId == 266) {
               return EnumProperty.getInstance(93, "NO_WRAP");
            }
         case 97:
         default:
            return null;
      }
   }
}
