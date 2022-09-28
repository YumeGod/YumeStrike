package org.apache.fop.layoutmgr;

import java.util.LinkedList;
import org.apache.fop.area.Area;
import org.apache.fop.fo.flow.FootnoteBody;

public class FootnoteBodyLayoutManager extends BlockStackingLayoutManager {
   public FootnoteBodyLayoutManager(FootnoteBody body) {
      super(body);
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      LayoutManager childLM = null;
      LayoutManager lastLM = null;
      LayoutContext lc = new LayoutContext(0);
      LinkedList positionList = new LinkedList();

      while(parentIter.hasNext()) {
         Position pos = (Position)parentIter.next();
         if (pos instanceof NonLeafPosition) {
            Position innerPosition = ((NonLeafPosition)pos).getPosition();
            if (innerPosition.getLM() != this) {
               positionList.add(innerPosition);
               lastLM = innerPosition.getLM();
            }
         }
      }

      BlockStackingLayoutManager.StackingIter childPosIter = new BlockStackingLayoutManager.StackingIter(positionList.listIterator());

      while((childLM = childPosIter.getNextChildLM()) != null) {
         lc.setFlags(128, layoutContext.isLastArea() && childLM == lastLM);
         childLM.addAreas(childPosIter, lc);
      }

   }

   public void addChildArea(Area childArea) {
      childArea.setAreaClass(4);
      this.parentLayoutManager.addChildArea(childArea);
   }

   protected FootnoteBody getFootnodeBodyFO() {
      return (FootnoteBody)this.fobj;
   }

   public Keep getKeepTogether() {
      return this.getParentKeepTogether();
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }
}
