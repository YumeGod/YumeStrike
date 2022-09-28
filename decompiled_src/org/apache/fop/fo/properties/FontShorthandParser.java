package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class FontShorthandParser extends GenericShorthandParser {
   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      int index = -1;
      switch (propId) {
         case 101:
            index = 1;
            break;
         case 103:
            index = 0;
            break;
         case 106:
            index = 3;
            break;
         case 107:
            index = 4;
            break;
         case 108:
            index = 5;
            break;
         case 144:
            index = 2;
      }

      Property newProp = (Property)property.getList().get(index);
      return newProp;
   }
}
