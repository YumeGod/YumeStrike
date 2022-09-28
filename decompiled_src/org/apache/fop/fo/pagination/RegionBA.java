package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;

public abstract class RegionBA extends SideRegion {
   private int precedence;

   protected RegionBA(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.precedence = pList.get(194).getEnum();
   }

   public int getPrecedence() {
      return this.precedence;
   }

   protected void adjustIPD(Rectangle vpRefRect, int wm, PercentBaseContext siblingContext) {
      int offset = 0;
      RegionStart start = (RegionStart)this.getSiblingRegion(61);
      if (start != null) {
         offset = start.getExtent().getValue(siblingContext);
         vpRefRect.translate(offset, 0);
      }

      RegionEnd end = (RegionEnd)this.getSiblingRegion(59);
      if (end != null) {
         offset += end.getExtent().getValue(siblingContext);
      }

      if (offset > 0) {
         if (wm != 79 && wm != 121) {
            vpRefRect.height -= offset;
         } else {
            vpRefRect.width -= offset;
         }
      }

   }
}
