package org.apache.fop.fo.properties;

import org.apache.fop.fo.Constants;
import org.apache.fop.fo.PropertyList;

public class VerticalAlignShorthandParser implements ShorthandParser, Constants {
   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) {
      int propVal = property.getEnum();
      switch (propVal) {
         case 12:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(12, "BASELINE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 145:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(14, "BEFORE_EDGE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 144:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(142, "TEXT_BEFORE_EDGE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 84:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(84, "MIDDLE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 20:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(4, "AFTER_EDGE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 143:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(141, "TEXT_AFTER_EDGE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 137:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(12, "BASELINE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(137, "SUB"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         case 138:
            switch (propId) {
               case 3:
                  return new EnumLength(EnumProperty.getInstance(9, "AUTO"));
               case 4:
                  return EnumProperty.getInstance(12, "BASELINE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(138, "SUPER"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
            }
         default:
            switch (propId) {
               case 3:
                  return property;
               case 4:
                  return EnumProperty.getInstance(12, "BASELINE");
               case 15:
                  return new EnumLength(EnumProperty.getInstance(12, "BASELINE"));
               case 88:
                  return EnumProperty.getInstance(9, "AUTO");
               default:
                  return null;
            }
      }
   }
}
