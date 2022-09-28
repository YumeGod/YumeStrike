package org.apache.fop.fo.properties;

import org.apache.fop.fo.Constants;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class FontSizePropertyMaker extends LengthProperty.Maker implements Constants {
   private static final int FONT_SIZE_NORMAL = 12000;
   private static final double FONT_SIZE_GROWTH_FACTOR = 1.2;

   public FontSizePropertyMaker(int propId) {
      super(propId);
   }

   public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
      Property p = super.make(propertyList, value, fo);
      if (p instanceof PercentLength) {
         Property pp = propertyList.getFromParent(this.propId);
         p = FixedLength.getInstance((double)pp.getLength().getValue() * ((PercentLength)p).getPercentage() / 100.0);
      }

      return (Property)p;
   }

   public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
      if (p.getEnum() != 71 && p.getEnum() != 132) {
         return super.convertProperty(p, propertyList, fo);
      } else {
         Property pp = propertyList.getFromParent(this.propId);
         int baseFontSize = this.computeClosestAbsoluteFontSize(pp.getLength().getValue());
         return p.getEnum() == 71 ? FixedLength.getInstance((double)Math.round((double)baseFontSize * 1.2)) : FixedLength.getInstance((double)Math.round((double)baseFontSize / 1.2));
      }
   }

   private int computeClosestAbsoluteFontSize(int baseFontSize) {
      double scale = 1.2;
      int lastStepFontSize = 12000;
      if (baseFontSize < 12000) {
         scale = 0.8333333333333334;
      }

      int nextStepFontSize;
      for(nextStepFontSize = (int)Math.round((double)lastStepFontSize * scale); scale < 1.0 && nextStepFontSize > baseFontSize || scale > 1.0 && nextStepFontSize < baseFontSize; nextStepFontSize = (int)Math.round((double)nextStepFontSize * scale)) {
         lastStepFontSize = nextStepFontSize;
      }

      return Math.abs(lastStepFontSize - baseFontSize) <= Math.abs(baseFontSize - nextStepFontSize) ? lastStepFontSize : nextStepFontSize;
   }
}
