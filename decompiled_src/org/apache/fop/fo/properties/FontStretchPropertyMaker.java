package org.apache.fop.fo.properties;

import org.apache.fop.fo.Constants;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class FontStretchPropertyMaker extends EnumProperty.Maker implements Constants {
   private Property[] orderedFontStretchValues = null;

   public FontStretchPropertyMaker(int propId) {
      super(propId);
   }

   public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      if (p.getEnum() == 85) {
         return this.computeNextAbsoluteFontStretch(propertyList.getFromParent(this.getPropId()), -1);
      } else {
         return p.getEnum() == 160 ? this.computeNextAbsoluteFontStretch(propertyList.getFromParent(this.getPropId()), 1) : super.convertProperty(p, propertyList, fo);
      }
   }

   private Property computeNextAbsoluteFontStretch(Property baseProperty, int direction) {
      if (this.orderedFontStretchValues == null) {
         this.orderedFontStretchValues = new Property[]{this.checkEnumValues("ultra-condensed"), this.checkEnumValues("extra-condensed"), this.checkEnumValues("condensed"), this.checkEnumValues("semi-condensed"), this.checkEnumValues("normal"), this.checkEnumValues("semi-expanded"), this.checkEnumValues("expanded"), this.checkEnumValues("extra-expanded"), this.checkEnumValues("ultra-expanded")};
      }

      int baseValue = baseProperty.getEnum();

      for(int i = 0; i < this.orderedFontStretchValues.length; ++i) {
         if (baseValue == this.orderedFontStretchValues[i].getEnum()) {
            i = Math.min(Math.max(0, i + direction), this.orderedFontStretchValues.length - 1);
            return this.orderedFontStretchValues[i];
         }
      }

      return this.orderedFontStretchValues[4];
   }
}
