package org.apache.fop.layoutmgr.table;

import org.apache.fop.fo.flow.table.BorderSpecification;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;

public class CollapsingBorderModelEyeCatching extends CollapsingBorderModel {
   public BorderSpecification determineWinner(BorderSpecification border1, BorderSpecification border2, boolean discard) {
      CommonBorderPaddingBackground.BorderInfo bi1 = border1.getBorderInfo();
      CommonBorderPaddingBackground.BorderInfo bi2 = border2.getBorderInfo();
      if (discard) {
         if (bi1.getWidth().isDiscard()) {
            if (bi2.getWidth().isDiscard()) {
               return new BorderSpecification(CommonBorderPaddingBackground.getDefaultBorderInfo(), 0);
            }

            return border2;
         }

         if (bi2.getWidth().isDiscard()) {
            return border1;
         }
      }

      return this.determineWinner(border1, border2);
   }

   public BorderSpecification determineWinner(BorderSpecification border1, BorderSpecification border2) {
      CommonBorderPaddingBackground.BorderInfo bi1 = border1.getBorderInfo();
      CommonBorderPaddingBackground.BorderInfo bi2 = border2.getBorderInfo();
      if (bi1.getStyle() == 57) {
         return border1;
      } else if (bi2.getStyle() == 57) {
         return border2;
      } else if (bi2.getStyle() == 95) {
         return border1;
      } else if (bi1.getStyle() == 95) {
         return border2;
      } else {
         int width1 = bi1.getRetainedWidth();
         int width2 = bi2.getRetainedWidth();
         if (width1 > width2) {
            return border1;
         } else if (width1 == width2) {
            int cmp = compareStyles(bi1.getStyle(), bi2.getStyle());
            if (cmp > 0) {
               return border1;
            } else if (cmp < 0) {
               return border2;
            } else {
               cmp = compareFOs(border1.getHolder(), border2.getHolder());
               if (cmp > 0) {
                  return border1;
               } else {
                  return cmp < 0 ? border2 : null;
               }
            }
         } else {
            return border2;
         }
      }
   }
}
