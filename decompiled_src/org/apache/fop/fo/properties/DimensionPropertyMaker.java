package org.apache.fop.fo.properties;

import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class DimensionPropertyMaker extends CorrespondingPropertyMaker {
   int[][] extraCorresponding = (int[][])null;

   public DimensionPropertyMaker(PropertyMaker baseMaker) {
      super(baseMaker);
   }

   public void setExtraCorresponding(int[][] extraCorresponding) {
      this.extraCorresponding = extraCorresponding;
   }

   public boolean isCorrespondingForced(PropertyList propertyList) {
      if (super.isCorrespondingForced(propertyList)) {
         return true;
      } else {
         for(int i = 0; i < this.extraCorresponding.length; ++i) {
            int wmcorr = this.extraCorresponding[i][0];
            if (propertyList.getExplicit(wmcorr) != null) {
               return true;
            }
         }

         return false;
      }
   }

   public Property compute(PropertyList propertyList) throws PropertyException {
      Property p = super.compute(propertyList);
      if (p == null) {
         p = this.baseMaker.make(propertyList);
      }

      int wmcorr = propertyList.getWritingMode(this.extraCorresponding[0][0], this.extraCorresponding[0][1], this.extraCorresponding[0][2]);
      Property subprop = propertyList.getExplicitOrShorthand(wmcorr);
      if (subprop != null) {
         this.baseMaker.setSubprop(p, 3072, subprop);
      }

      wmcorr = propertyList.getWritingMode(this.extraCorresponding[1][0], this.extraCorresponding[1][1], this.extraCorresponding[1][2]);
      subprop = propertyList.getExplicitOrShorthand(wmcorr);
      if (subprop != null) {
         this.baseMaker.setSubprop(p, 2560, subprop);
      }

      return p;
   }
}
