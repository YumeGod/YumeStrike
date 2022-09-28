package org.apache.fop.area.inline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FilledArea extends InlineParent {
   private int unitWidth;

   private void setChildOffset(ListIterator childrenIterator, int v) {
      while(childrenIterator.hasNext()) {
         InlineArea child = (InlineArea)childrenIterator.next();
         if (child instanceof InlineParent) {
            this.setChildOffset(((InlineParent)child).getChildAreas().listIterator(), v);
         } else if (!(child instanceof Viewport)) {
            child.setOffset(v);
         }
      }

   }

   public void setUnitWidth(int width) {
      this.unitWidth = width;
   }

   public int getUnitWidth() {
      return this.unitWidth;
   }

   public int getBPD() {
      int bpd = 0;
      Iterator childAreaIt = this.getChildAreas().iterator();

      while(childAreaIt.hasNext()) {
         InlineArea area = (InlineArea)childAreaIt.next();
         if (bpd < area.getBPD()) {
            bpd = area.getBPD();
         }
      }

      return bpd;
   }

   public List getChildAreas() {
      int units = this.getIPD() / this.unitWidth;
      List newList = new ArrayList();

      for(int count = 0; count < units; ++count) {
         newList.addAll(this.inlines);
      }

      return newList;
   }

   public boolean applyVariationFactor(double variationFactor, int lineStretch, int lineShrink) {
      this.setIPD(this.getIPD() + this.adjustingInfo.applyVariationFactor(variationFactor));
      return false;
   }
}
