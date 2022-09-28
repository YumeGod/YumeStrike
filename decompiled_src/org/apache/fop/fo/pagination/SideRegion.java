package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;

public abstract class SideRegion extends Region {
   private Length extent;

   protected SideRegion(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.extent = pList.get(93).getLength();
   }

   public Length getExtent() {
      return this.extent;
   }
}
