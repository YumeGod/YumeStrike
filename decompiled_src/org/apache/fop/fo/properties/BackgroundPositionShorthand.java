package org.apache.fop.fo.properties;

import java.util.List;
import org.apache.fop.datatypes.PercentBase;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class BackgroundPositionShorthand extends ListProperty {
   public static class Parser extends GenericShorthandParser {
      public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
         int index = -1;
         List propList = property.getList();
         if (propId == 12) {
            index = 0;
         } else if (propId == 13) {
            index = 1;
         }

         return index >= 0 ? maker.convertProperty((Property)propList.get(index), propertyList, propertyList.getFObj()) : null;
      }
   }

   public static class Maker extends ListProperty.Maker {
      public Maker(int propId) {
         super(propId);
      }

      public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
         Property p = super.make(propertyList, value, fo);
         if (p.getList().size() == 1) {
            PropertyMaker m = FObj.getPropertyMakerFor(13);
            p.getList().add(1, m.make(propertyList, "50%", fo));
         }

         return p;
      }

      public PercentBase getPercentBase(PropertyList pl) {
         return new PercentBase() {
            public int getBaseLength(PercentBaseContext context) throws PropertyException {
               return 0;
            }

            public double getBaseValue() {
               return 0.0;
            }

            public int getDimension() {
               return 1;
            }
         };
      }
   }
}
