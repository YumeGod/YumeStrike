package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class PageBreakShorthandParser implements ShorthandParser {
   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      if (propId != 133 && propId != 132 && propId != 131) {
         if (propId == 59 || propId == 58) {
            switch (property.getEnum()) {
               case 7:
                  return EnumProperty.getInstance(104, "PAGE");
               case 73:
                  return EnumProperty.getInstance(44, "EVEN_PAGE");
               case 120:
                  return EnumProperty.getInstance(100, "ODD_PAGE");
               case 178:
            }
         }
      } else if (property.getEnum() == 178) {
         return maker.make((Property)null, 5632, propertyList, "always", propertyList.getFObj());
      }

      return null;
   }
}
