package org.apache.fop.fo.properties;

import org.apache.fop.fo.FOPropertyMapping;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class BoxPropShorthandParser extends GenericShorthandParser {
   protected Property convertValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      String name = FOPropertyMapping.getPropertyName(propId);
      Property p = null;
      int count = property.getList().size();
      if (name.indexOf("-top") >= 0) {
         p = this.getElement(property, 0);
      } else if (name.indexOf("-right") >= 0) {
         p = this.getElement(property, count > 1 ? 1 : 0);
      } else if (name.indexOf("-bottom") >= 0) {
         p = this.getElement(property, count > 2 ? 2 : 0);
      } else if (name.indexOf("-left") >= 0) {
         p = this.getElement(property, count > 3 ? 3 : (count > 1 ? 1 : 0));
      }

      return p != null ? maker.convertShorthandProperty(propertyList, p, (FObj)null) : p;
   }
}
