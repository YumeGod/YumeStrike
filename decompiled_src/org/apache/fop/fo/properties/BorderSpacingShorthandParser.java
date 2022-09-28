package org.apache.fop.fo.properties;

import java.util.List;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class BorderSpacingShorthandParser extends GenericShorthandParser {
   protected Property convertValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      List lst = property.getList();
      if (lst != null) {
         Property ipd;
         if (lst.size() == 1) {
            ipd = (Property)lst.get(0);
            return new LengthPairProperty(ipd);
         }

         if (lst.size() == 2) {
            ipd = (Property)lst.get(0);
            Property bpd = (Property)lst.get(1);
            return new LengthPairProperty(ipd, bpd);
         }
      }

      throw new PropertyException("list with 1 or 2 length values expected");
   }
}
