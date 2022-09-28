package org.apache.fop.area.inline;

import org.apache.fop.area.Area;
import org.apache.fop.area.Block;

public class InlineBlockParent extends InlineArea {
   protected Block child = null;

   public void addChildArea(Area childArea) {
      if (this.child != null) {
         throw new IllegalStateException("InlineBlockParent may have only one child area.");
      } else if (childArea instanceof Block) {
         this.child = (Block)childArea;
         this.setIPD(childArea.getAllocIPD());
         this.setBPD(childArea.getAllocBPD());
      } else {
         throw new IllegalArgumentException("The child of an InlineBlockParent must be a Block area");
      }
   }

   public Block getChildArea() {
      return this.child;
   }
}
