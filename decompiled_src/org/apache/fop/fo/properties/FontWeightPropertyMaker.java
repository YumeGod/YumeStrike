package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.expr.PropertyInfo;
import org.apache.fop.fo.expr.PropertyParser;

public class FontWeightPropertyMaker extends EnumProperty.Maker {
   public FontWeightPropertyMaker(int propId) {
      super(propId);
   }

   public Property make(PropertyList pList, String value, FObj fo) throws PropertyException {
      if ("inherit".equals(value)) {
         return super.make(pList, value, fo);
      } else {
         String pValue = this.checkValueKeywords(value);
         Property newProp = this.checkEnumValues(pValue);
         int enumValue = -1;
         if (newProp != null && ((enumValue = ((Property)newProp).getEnum()) == 167 || enumValue == 168)) {
            Property parentProp = pList.getInherited(108);
            if (enumValue == 167) {
               enumValue = parentProp.getEnum();
               switch (enumValue) {
                  case 169:
                     newProp = EnumProperty.getInstance(170, "200");
                     break;
                  case 170:
                     newProp = EnumProperty.getInstance(171, "300");
                     break;
                  case 171:
                     newProp = EnumProperty.getInstance(172, "400");
                     break;
                  case 172:
                     newProp = EnumProperty.getInstance(173, "500");
                     break;
                  case 173:
                     newProp = EnumProperty.getInstance(174, "600");
                     break;
                  case 174:
                     newProp = EnumProperty.getInstance(175, "700");
                     break;
                  case 175:
                     newProp = EnumProperty.getInstance(176, "800");
                     break;
                  case 176:
                  case 177:
                     newProp = EnumProperty.getInstance(177, "900");
               }
            } else {
               enumValue = parentProp.getEnum();
               switch (enumValue) {
                  case 169:
                  case 170:
                     newProp = EnumProperty.getInstance(169, "100");
                     break;
                  case 171:
                     newProp = EnumProperty.getInstance(170, "200");
                     break;
                  case 172:
                     newProp = EnumProperty.getInstance(171, "300");
                     break;
                  case 173:
                     newProp = EnumProperty.getInstance(172, "400");
                     break;
                  case 174:
                     newProp = EnumProperty.getInstance(173, "500");
                     break;
                  case 175:
                     newProp = EnumProperty.getInstance(174, "600");
                     break;
                  case 176:
                     newProp = EnumProperty.getInstance(175, "700");
                     break;
                  case 177:
                     newProp = EnumProperty.getInstance(176, "800");
               }
            }
         } else if (enumValue == -1) {
            newProp = PropertyParser.parse(value, new PropertyInfo(this, pList));
         }

         if (newProp != null) {
            newProp = this.convertProperty((Property)newProp, pList, fo);
         }

         return (Property)newProp;
      }
   }
}
