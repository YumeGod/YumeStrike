package org.apache.fop.area.inline;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.area.Area;

public class InlineParent extends InlineArea {
   protected List inlines = new ArrayList();
   protected transient boolean autoSize;

   public void addChildArea(Area childArea) {
      if (this.inlines.size() == 0) {
         this.autoSize = this.getIPD() == 0;
      }

      if (childArea instanceof InlineArea) {
         InlineArea inlineChildArea = (InlineArea)childArea;
         this.inlines.add(childArea);
         inlineChildArea.setParentArea(this);
         if (this.autoSize) {
            this.increaseIPD(inlineChildArea.getAllocIPD());
         }
      }

   }

   public List getChildAreas() {
      return this.inlines;
   }

   public boolean applyVariationFactor(double variationFactor, int lineStretch, int lineShrink) {
      boolean bUnresolvedAreasPresent = false;
      int i = 0;

      for(int len = this.inlines.size(); i < len; ++i) {
         bUnresolvedAreasPresent |= ((InlineArea)this.inlines.get(i)).applyVariationFactor(variationFactor, lineStretch, lineShrink);
      }

      return bUnresolvedAreasPresent;
   }
}
