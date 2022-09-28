package org.apache.fop.fo.properties;

import java.util.Iterator;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class GenericShorthandParser implements ShorthandParser {
   protected Property getElement(Property list, int index) {
      return list.getList().size() > index ? (Property)list.getList().get(index) : null;
   }

   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      if (property.getList().size() == 1) {
         String sval = this.getElement(property, 0).getString();
         if (sval != null && sval.equals("inherit")) {
            return propertyList.getFromParent(propId);
         }
      }

      return this.convertValueForProperty(propId, property, maker, propertyList);
   }

   protected Property convertValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      Property prop = null;

      Property p;
      for(Iterator iprop = property.getList().iterator(); iprop.hasNext() && prop == null; prop = maker.convertShorthandProperty(propertyList, p, (FObj)null)) {
         p = (Property)iprop.next();
      }

      return prop;
   }
}
