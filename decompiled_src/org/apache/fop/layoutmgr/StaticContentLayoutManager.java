package org.apache.fop.layoutmgr;

import java.util.LinkedList;
import java.util.List;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.RegionReference;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.SideRegion;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.layoutmgr.inline.TextLayoutManager;

public class StaticContentLayoutManager extends BlockStackingLayoutManager {
   private RegionReference targetRegion;
   private Block targetBlock;
   private SideRegion regionFO;
   private int contentAreaIPD = 0;
   private int contentAreaBPD = -1;

   public StaticContentLayoutManager(PageSequenceLayoutManager pslm, StaticContent node, SideRegion reg) {
      super(node);
      this.setParent(pslm);
      this.regionFO = reg;
      this.targetRegion = this.getCurrentPV().getRegionReference(this.regionFO.getNameId());
   }

   public StaticContentLayoutManager(PageSequenceLayoutManager pslm, StaticContent node, Block block) {
      super(node);
      this.setParent(pslm);
      this.targetBlock = block;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      throw new IllegalStateException();
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      AreaAdditionUtil.addAreas(this, parentIter, layoutContext);
      this.flush();
      this.targetRegion = null;
   }

   public void addChildArea(Area childArea) {
      if (this.getStaticContentFO().getFlowName().equals("xsl-footnote-separator")) {
         this.targetBlock.addBlock((Block)childArea);
      } else {
         this.targetRegion.addBlock((Block)childArea);
      }

   }

   public Area getParentArea(Area childArea) {
      return (Area)(this.getStaticContentFO().getFlowName().equals("xsl-footnote-separator") ? this.targetBlock : this.targetRegion);
   }

   public void doLayout() {
      int targetIPD = false;
      int targetBPD = false;
      int targetAlign = true;
      boolean autoHeight = false;
      int targetIPD;
      int targetBPD;
      int targetAlign;
      if (this.getStaticContentFO().getFlowName().equals("xsl-footnote-separator")) {
         targetIPD = this.targetBlock.getIPD();
         targetBPD = this.targetBlock.getBPD();
         if (targetBPD == 0) {
            autoHeight = true;
         }

         targetAlign = 13;
      } else {
         targetIPD = this.targetRegion.getIPD();
         targetBPD = this.targetRegion.getBPD();
         targetAlign = this.regionFO.getDisplayAlign();
      }

      this.setContentAreaIPD(targetIPD);
      this.setContentAreaBPD(targetBPD);
      StaticContentBreaker breaker = new StaticContentBreaker(this, targetIPD, targetAlign);
      breaker.doLayout(targetBPD, autoHeight);
      if (breaker.isOverflow() && !autoHeight) {
         String page = this.getPSLM().getCurrentPage().getPageViewport().getPageNumberString();
         BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getStaticContentFO().getUserAgent().getEventBroadcaster());
         boolean canRecover = this.regionFO.getOverflow() != 42;
         boolean needClip = this.regionFO.getOverflow() == 57 || this.regionFO.getOverflow() == 42;
         eventProducer.regionOverflow(this, this.regionFO.getName(), page, breaker.getOverflowAmount(), needClip, canRecover, this.getStaticContentFO().getLocator());
      }

   }

   protected StaticContent getStaticContentFO() {
      return (StaticContent)this.fobj;
   }

   public int getContentAreaIPD() {
      return this.contentAreaIPD;
   }

   protected void setContentAreaIPD(int contentAreaIPD) {
      this.contentAreaIPD = contentAreaIPD;
   }

   public int getContentAreaBPD() {
      return this.contentAreaBPD;
   }

   private void setContentAreaBPD(int contentAreaBPD) {
      this.contentAreaBPD = contentAreaBPD;
   }

   public Keep getKeepTogether() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }

   private class StaticContentBreaker extends AbstractBreaker {
      private StaticContentLayoutManager lm;
      private int displayAlign;
      private int ipd;
      private int overflow = 0;

      public StaticContentBreaker(StaticContentLayoutManager lm, int ipd, int displayAlign) {
         this.lm = lm;
         this.ipd = ipd;
         this.displayAlign = displayAlign;
      }

      protected void observeElementList(List elementList) {
         String elementListID = StaticContentLayoutManager.this.getStaticContentFO().getFlowName();
         String pageSequenceID = ((PageSequence)this.lm.getParent().getFObj()).getId();
         if (pageSequenceID != null && pageSequenceID.length() > 0) {
            elementListID = elementListID + "-" + pageSequenceID;
         }

         ElementListObserver.observe(elementList, "static-content", elementListID);
      }

      protected boolean isPartOverflowRecoveryActivated() {
         return false;
      }

      public boolean isOverflow() {
         return this.overflow != 0;
      }

      public int getOverflowAmount() {
         return this.overflow;
      }

      protected PageBreakingAlgorithm.PageBreakingLayoutListener createLayoutListener() {
         return new PageBreakingAlgorithm.PageBreakingLayoutListener() {
            public void notifyOverflow(int part, int amount, FObj obj) {
               if (StaticContentBreaker.this.overflow == 0) {
                  StaticContentBreaker.this.overflow = amount;
               }

            }
         };
      }

      protected LayoutManager getTopLevelLM() {
         return this.lm;
      }

      protected LayoutContext createLayoutContext() {
         LayoutContext lc = super.createLayoutContext();
         lc.setRefIPD(this.ipd);
         return lc;
      }

      protected List getNextKnuthElements(LayoutContext context, int alignment) {
         List returnList = new LinkedList();

         LayoutManager curLM;
         while((curLM = StaticContentLayoutManager.this.getChildLM()) != null) {
            LayoutContext childLC = new LayoutContext(0);
            childLC.setStackLimitBP(context.getStackLimitBP());
            childLC.setRefIPD(context.getRefIPD());
            childLC.setWritingMode(context.getWritingMode());
            List returnedList = null;
            boolean ignore = curLM instanceof TextLayoutManager;
            if (!curLM.isFinished()) {
               returnedList = curLM.getNextKnuthElements(childLC, alignment);
            }

            if (returnedList != null && !ignore) {
               this.lm.wrapPositionElements(returnedList, returnList);
            }
         }

         SpaceResolver.resolveElementList(returnList);
         StaticContentLayoutManager.this.setFinished(true);
         return returnList;
      }

      protected int getCurrentDisplayAlign() {
         return this.displayAlign;
      }

      protected boolean hasMoreContent() {
         return !this.lm.isFinished();
      }

      protected void addAreas(PositionIterator posIter, LayoutContext context) {
         AreaAdditionUtil.addAreas(this.lm, posIter, context);
      }

      protected void doPhase3(PageBreakingAlgorithm alg, int partCount, AbstractBreaker.BlockSequence originalList, AbstractBreaker.BlockSequence effectiveList) {
         if (partCount > 1) {
            AbstractBreaker.PageBreakPosition pos = (AbstractBreaker.PageBreakPosition)alg.getPageBreaks().getFirst();
            int firstPartLength = ElementListUtils.calcContentLength(effectiveList, effectiveList.ignoreAtStart, pos.getLeafPos());
            this.overflow += alg.totalWidth - firstPartLength;
         }

         alg.removeAllPageBreaks();
         this.addAreas(alg, 1, originalList, effectiveList);
      }

      protected void finishPart(PageBreakingAlgorithm alg, AbstractBreaker.PageBreakPosition pbp) {
      }

      protected LayoutManager getCurrentChildLM() {
         return null;
      }
   }
}
