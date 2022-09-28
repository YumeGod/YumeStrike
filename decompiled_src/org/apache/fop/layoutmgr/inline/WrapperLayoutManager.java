package org.apache.fop.layoutmgr.inline;

import org.apache.fop.area.Block;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.fo.flow.Wrapper;
import org.apache.fop.layoutmgr.BlockLayoutManager;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.TraitSetter;

public class WrapperLayoutManager extends LeafNodeLayoutManager {
   public WrapperLayoutManager(Wrapper node) {
      super(node);
   }

   public InlineArea get(LayoutContext context) {
      InlineArea area = new InlineArea();
      if (this.fobj.hasId()) {
         TraitSetter.setProducerID(area, this.fobj.getId());
      }

      return area;
   }

   public void addAreas(PositionIterator posIter, LayoutContext context) {
      if (this.fobj.hasId()) {
         this.addId();
         if (this.parentLayoutManager instanceof BlockStackingLayoutManager && !(this.parentLayoutManager instanceof BlockLayoutManager)) {
            Block helperBlock = new Block();
            TraitSetter.setProducerID(helperBlock, this.fobj.getId());
            this.parentLayoutManager.addChildArea(helperBlock);
         } else {
            InlineArea area = this.getEffectiveArea();
            this.parentLayoutManager.addChildArea(area);
         }
      }

      while(posIter.hasNext()) {
         posIter.next();
      }

   }

   protected void addId() {
      this.getPSLM().addIDToPage(this.fobj.getId());
   }
}
