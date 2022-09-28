package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;

public abstract class RegionSE extends SideRegion {
   protected RegionSE(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
   }

   protected void adjustIPD(Rectangle vpRefRect, int wm, PercentBaseContext siblingContext) {
      int offset = 0;
      RegionBefore before = (RegionBefore)this.getSiblingRegion(57);
      if (before != null && before.getPrecedence() == 149) {
         offset = before.getExtent().getValue(siblingContext);
         vpRefRect.translate(0, offset);
      }

      RegionAfter after = (RegionAfter)this.getSiblingRegion(56);
      if (after != null && after.getPrecedence() == 149) {
         offset += after.getExtent().getValue(siblingContext);
      }

      if (offset > 0) {
         if (wm != 79 && wm != 121) {
            vpRefRect.width -= offset;
         } else {
            vpRefRect.height -= offset;
         }
      }

   }
}
