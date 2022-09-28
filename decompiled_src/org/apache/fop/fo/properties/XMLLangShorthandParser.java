package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class XMLLangShorthandParser extends GenericShorthandParser {
   private static final char HYPHEN_MINUS = '-';

   public Property getValueForProperty(int propId, Property property, PropertyMaker maker, PropertyList propertyList) throws PropertyException {
      String shorthandValue = property.getString();
      int hyphenIndex = shorthandValue.indexOf(45);
      if (propId == 134) {
         return (Property)(hyphenIndex == -1 ? property : StringProperty.getInstance(shorthandValue.substring(0, hyphenIndex)));
      } else if (propId == 81 && hyphenIndex != -1) {
         int nextHyphenIndex = shorthandValue.indexOf(45, hyphenIndex + 1);
         return nextHyphenIndex != -1 ? StringProperty.getInstance(shorthandValue.substring(hyphenIndex + 1, nextHyphenIndex)) : StringProperty.getInstance(shorthandValue.substring(hyphenIndex + 1));
      } else {
         return null;
      }
   }
}
