package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class PageDimensionMaker extends LengthProperty.Maker {
   public PageDimensionMaker(int propId) {
      super(propId);
   }

   public Property get(int subpropId, PropertyList propertyList, boolean tryInherit, boolean tryDefault) throws PropertyException {
      Property p = super.get(0, propertyList, tryInherit, tryDefault);
      FObj fo = propertyList.getFObj();
      String fallbackValue = this.propId == 183 ? fo.getUserAgent().getPageHeight() : fo.getUserAgent().getPageWidth();
      if (p.getEnum() == 64) {
         int otherId = this.propId == 183 ? 186 : 183;
         int writingMode = propertyList.get(267).getEnum();
         int refOrientation = propertyList.get(197).getNumeric().getValue();
         if (propertyList.getExplicit(otherId) != null && propertyList.getExplicit(otherId).getEnum() == 64) {
            if (writingMode != 140 && (refOrientation == 0 || refOrientation == 180 || refOrientation == -180) || writingMode == 140 && (refOrientation == 90 || refOrientation == 270 || refOrientation == -270)) {
               if (this.propId == 186) {
                  Property.log.warn("Both page-width and page-height set to \"indefinite\". Forcing page-width to \"auto\"");
                  return this.make(propertyList, fallbackValue, fo);
               }
            } else {
               Property.log.warn("Both page-width and page-height set to \"indefinite\". Forcing page-height to \"auto\"");
               if (this.propId == 183) {
                  return this.make(propertyList, fallbackValue, fo);
               }
            }
         }
      } else if (p.isAuto()) {
         return this.make(propertyList, fallbackValue, fo);
      }

      return p;
   }
}
