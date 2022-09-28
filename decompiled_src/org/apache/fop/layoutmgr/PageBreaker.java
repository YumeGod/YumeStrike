package org.apache.fop.layoutmgr;

import java.util.List;
import java.util.ListIterator;
import org.apache.fop.area.Block;
import org.apache.fop.area.BodyRegion;
import org.apache.fop.area.Footnote;
import org.apache.fop.area.PageViewport;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.pagination.RegionBody;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.traits.MinOptMax;

public class PageBreaker extends AbstractBreaker {
   private PageSequenceLayoutManager pslm;
   private boolean firstPart = true;
   private boolean pageBreakHandled;
   private boolean needColumnBalancing;
   private PageProvider pageProvider;
   private Block separatorArea;
   private FlowLayoutManager childFLM = null;
   private StaticContentLayoutManager footnoteSeparatorLM = null;

   public PageBreaker(PageSequenceLayoutManager pslm) {
      this.pslm = pslm;
      this.pageProvider = pslm.getPageProvider();
      this.childFLM = pslm.getLayoutManagerMaker().makeFlowLayoutManager(pslm, pslm.getPageSequence().getMainFlow());
   }

   protected void updateLayoutContext(LayoutContext context) {
      int flowIPD = this.pslm.getCurrentPV().getCurrentSpan().getColumnWidth();
      context.setRefIPD(flowIPD);
   }

   protected LayoutManager getTopLevelLM() {
      return this.pslm;
   }

   protected PageProvider getPageProvider() {
      return this.pslm.getPageProvider();
   }

   void doLayout(int flowBPD) {
      this.doLayout(flowBPD, false);
   }

   protected PageBreakingAlgorithm.PageBreakingLayoutListener createLayoutListener() {
      return new PageBreakingAlgorithm.PageBreakingLayoutListener() {
         public void notifyOverflow(int part, int amount, FObj obj) {
            Page p = PageBreaker.this.pageProvider.getPage(false, part, 1);
            RegionBody body = (RegionBody)p.getSimplePageMaster().getRegion(58);
            BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(body.getUserAgent().getEventBroadcaster());
            boolean canRecover = body.getOverflow() != 42;
            boolean needClip = body.getOverflow() == 57 || body.getOverflow() == 42;
            eventProducer.regionOverflow(this, body.getName(), p.getPageViewport().getPageNumberString(), amount, needClip, canRecover, body.getLocator());
         }
      };
   }

   protected int handleSpanChange(LayoutContext childLC, int nextSequenceStartsOn) {
      this.needColumnBalancing = false;
      if (childLC.getNextSpan() != 0) {
         nextSequenceStartsOn = childLC.getNextSpan();
         this.needColumnBalancing = childLC.getNextSpan() == 5 && childLC.getDisableColumnBalancing() == 48;
      }

      if (this.needColumnBalancing) {
         AbstractBreaker.log.debug("Column balancing necessary for the next element list!!!");
      }

      return nextSequenceStartsOn;
   }

   protected int getNextBlockList(LayoutContext childLC, int nextSequenceStartsOn) {
      return this.getNextBlockList(childLC, nextSequenceStartsOn, (Position)null, (LayoutManager)null, (List)null);
   }

   protected int getNextBlockList(LayoutContext childLC, int nextSequenceStartsOn, Position positionAtIPDChange, LayoutManager restartLM, List firstElements) {
      if (!this.firstPart) {
         this.handleBreakTrait(nextSequenceStartsOn);
      }

      this.firstPart = false;
      this.pageBreakHandled = true;
      this.pageProvider.setStartOfNextElementList(this.pslm.getCurrentPageNum(), this.pslm.getCurrentPV().getCurrentSpan().getCurrentFlowIndex());
      return super.getNextBlockList(childLC, nextSequenceStartsOn, positionAtIPDChange, restartLM, firstElements);
   }

   private boolean containsFootnotes(List contentList, LayoutContext context) {
      boolean containsFootnotes = false;
      if (contentList != null) {
         ListIterator contentListIterator = contentList.listIterator();

         while(true) {
            ListElement element;
            do {
               do {
                  if (!contentListIterator.hasNext()) {
                     return containsFootnotes;
                  }

                  element = (ListElement)contentListIterator.next();
               } while(!(element instanceof KnuthBlockBox));
            } while(!((KnuthBlockBox)element).hasAnchors());

            containsFootnotes = true;
            LayoutContext footnoteContext = new LayoutContext(context);
            footnoteContext.setStackLimitBP(context.getStackLimitBP());
            footnoteContext.setRefIPD(this.pslm.getCurrentPV().getRegionReference(58).getIPD());
            List footnoteBodyLMs = ((KnuthBlockBox)element).getFootnoteBodyLMs();
            ListIterator footnoteBodyIterator = footnoteBodyLMs.listIterator();

            while(footnoteBodyIterator.hasNext()) {
               FootnoteBodyLayoutManager fblm = (FootnoteBodyLayoutManager)footnoteBodyIterator.next();
               fblm.setParent(this.childFLM);
               fblm.initialize();
               ((KnuthBlockBox)element).addElementList(fblm.getNextKnuthElements(footnoteContext, this.alignment));
            }
         }
      } else {
         return containsFootnotes;
      }
   }

   private void handleFootnoteSeparator() {
      StaticContent footnoteSeparator = this.pslm.getPageSequence().getStaticContent("xsl-footnote-separator");
      if (footnoteSeparator != null) {
         this.separatorArea = new Block();
         this.separatorArea.setIPD(this.pslm.getCurrentPV().getRegionReference(58).getIPD());
         this.footnoteSeparatorLM = this.pslm.getLayoutManagerMaker().makeStaticContentLayoutManager(this.pslm, footnoteSeparator, this.separatorArea);
         this.footnoteSeparatorLM.doLayout();
         this.footnoteSeparatorLength = MinOptMax.getInstance(this.separatorArea.getBPD());
      }

   }

   protected List getNextKnuthElements(LayoutContext context, int alignment) {
      List contentList;
      for(contentList = null; !this.childFLM.isFinished() && contentList == null; contentList = this.childFLM.getNextKnuthElements(context, alignment)) {
      }

      if (this.containsFootnotes(contentList, context)) {
         this.handleFootnoteSeparator();
      }

      return contentList;
   }

   protected List getNextKnuthElements(LayoutContext context, int alignment, Position positionAtIPDChange, LayoutManager restartAtLM) {
      List contentList = null;

      do {
         contentList = this.childFLM.getNextKnuthElements(context, alignment, positionAtIPDChange, restartAtLM);
      } while(!this.childFLM.isFinished() && contentList == null);

      if (this.containsFootnotes(contentList, context)) {
         this.handleFootnoteSeparator();
      }

      return contentList;
   }

   protected int getCurrentDisplayAlign() {
      return this.pslm.getCurrentPage().getSimplePageMaster().getRegion(58).getDisplayAlign();
   }

   protected boolean hasMoreContent() {
      return !this.childFLM.isFinished();
   }

   protected void addAreas(PositionIterator posIter, LayoutContext context) {
      if (this.footnoteSeparatorLM != null) {
         StaticContent footnoteSeparator = this.pslm.getPageSequence().getStaticContent("xsl-footnote-separator");
         this.separatorArea = new Block();
         this.separatorArea.setIPD(this.pslm.getCurrentPV().getRegionReference(58).getIPD());
         this.footnoteSeparatorLM = this.pslm.getLayoutManagerMaker().makeStaticContentLayoutManager(this.pslm, footnoteSeparator, this.separatorArea);
         this.footnoteSeparatorLM.doLayout();
      }

      this.childFLM.addAreas(posIter, context);
   }

   protected void doPhase3(PageBreakingAlgorithm alg, int partCount, AbstractBreaker.BlockSequence originalList, AbstractBreaker.BlockSequence effectiveList) {
      if (this.needColumnBalancing) {
         this.redoLayout(alg, partCount, originalList, effectiveList);
      } else {
         boolean lastPageMasterDefined = this.pslm.getPageSequence().hasPagePositionLast();
         if (!this.hasMoreContent() && lastPageMasterDefined) {
            this.redoLayout(alg, partCount, originalList, effectiveList);
         } else {
            this.addAreas(alg, partCount, originalList, effectiveList);
         }
      }
   }

   private void redoLayout(PageBreakingAlgorithm alg, int partCount, AbstractBreaker.BlockSequence originalList, AbstractBreaker.BlockSequence effectiveList) {
      int newStartPos = 0;
      int restartPoint = this.pageProvider.getStartingPartIndexForLastPage(partCount);
      if (restartPoint > 0) {
         this.addAreas(alg, restartPoint, originalList, effectiveList);
         AbstractBreaker.PageBreakPosition pbp = (AbstractBreaker.PageBreakPosition)alg.getPageBreaks().get(restartPoint - 1);
         newStartPos = pbp.getLeafPos() + 1;
         if (newStartPos > 0) {
            this.handleBreakTrait(104);
         }
      }

      AbstractBreaker.log.debug("Restarting at " + restartPoint + ", new start position: " + newStartPos);
      this.pageBreakHandled = true;
      int currentPageNum = this.pslm.getCurrentPageNum();
      this.pageProvider.setStartOfNextElementList(currentPageNum, this.pslm.getCurrentPV().getCurrentSpan().getCurrentFlowIndex());
      effectiveList.ignoreAtStart = newStartPos;
      Object algRestart;
      if (this.needColumnBalancing) {
         AbstractBreaker.log.debug("Column balancing now!!!");
         AbstractBreaker.log.debug("===================================================");
         algRestart = new BalancingColumnBreakingAlgorithm(this.getTopLevelLM(), this.getPageProvider(), this.createLayoutListener(), this.alignment, 135, this.footnoteSeparatorLength, this.isPartOverflowRecoveryActivated(), this.pslm.getCurrentPV().getBodyRegion().getColumnCount());
         AbstractBreaker.log.debug("===================================================");
      } else {
         BodyRegion currentBody = this.pageProvider.getPage(false, currentPageNum).getPageViewport().getBodyRegion();
         this.pageProvider.setLastPageIndex(currentPageNum);
         BodyRegion lastBody = this.pageProvider.getPage(false, currentPageNum).getPageViewport().getBodyRegion();
         lastBody.getMainReference().setSpans(currentBody.getMainReference().getSpans());
         AbstractBreaker.log.debug("Last page handling now!!!");
         AbstractBreaker.log.debug("===================================================");
         algRestart = new PageBreakingAlgorithm(this.getTopLevelLM(), this.getPageProvider(), this.createLayoutListener(), alg.getAlignment(), alg.getAlignmentLast(), this.footnoteSeparatorLength, this.isPartOverflowRecoveryActivated(), false, false);
         AbstractBreaker.log.debug("===================================================");
      }

      int optimalPageCount = ((PageBreakingAlgorithm)algRestart).findBreakingPoints(effectiveList, newStartPos, 1.0, true, 0);
      AbstractBreaker.log.debug("restart: optimalPageCount= " + optimalPageCount + " pageBreaks.size()= " + ((PageBreakingAlgorithm)algRestart).getPageBreaks().size());
      boolean fitsOnePage = optimalPageCount <= this.pslm.getCurrentPV().getBodyRegion().getMainReference().getCurrentSpan().getColumnCount();
      if (this.needColumnBalancing) {
         if (!fitsOnePage) {
            AbstractBreaker.log.warn("Breaking algorithm produced more columns than are available.");
         }
      } else {
         if (!fitsOnePage) {
            this.addAreas(alg, restartPoint, partCount - restartPoint, originalList, effectiveList);
            this.pageProvider.setLastPageIndex(currentPageNum + 1);
            this.pslm.setCurrentPage(this.pslm.makeNewPage(true, true));
            return;
         }

         this.pslm.setCurrentPage(this.pageProvider.getPage(false, currentPageNum));
      }

      this.addAreas((PageBreakingAlgorithm)algRestart, optimalPageCount, originalList, effectiveList);
   }

   protected void startPart(AbstractBreaker.BlockSequence list, int breakClass) {
      AbstractBreaker.log.debug("startPart() breakClass=" + getBreakClassName(breakClass));
      if (this.pslm.getCurrentPage() == null) {
         throw new IllegalStateException("curPage must not be null");
      } else {
         if (!this.pageBreakHandled) {
            if (!this.firstPart) {
               this.handleBreakTrait(breakClass);
            }

            this.pageProvider.setStartOfNextElementList(this.pslm.getCurrentPageNum(), this.pslm.getCurrentPV().getCurrentSpan().getCurrentFlowIndex());
         }

         this.pageBreakHandled = false;
         this.firstPart = false;
      }
   }

   protected void handleEmptyContent() {
      this.pslm.getCurrentPV().getPage().fakeNonEmpty();
   }

   protected void finishPart(PageBreakingAlgorithm alg, AbstractBreaker.PageBreakPosition pbp) {
      if (pbp.footnoteFirstListIndex < pbp.footnoteLastListIndex || pbp.footnoteFirstElementIndex <= pbp.footnoteLastElementIndex) {
         for(int i = pbp.footnoteFirstListIndex; i <= pbp.footnoteLastListIndex; ++i) {
            List elementList = alg.getFootnoteList(i);
            int firstIndex = i == pbp.footnoteFirstListIndex ? pbp.footnoteFirstElementIndex : 0;
            int lastIndex = i == pbp.footnoteLastListIndex ? pbp.footnoteLastElementIndex : elementList.size() - 1;
            SpaceResolver.performConditionalsNotification(elementList, firstIndex, lastIndex, -1);
            LayoutContext childLC = new LayoutContext(0);
            AreaAdditionUtil.addAreas((BlockStackingLayoutManager)null, new KnuthPossPosIter(elementList, firstIndex, lastIndex + 1), childLC);
         }

         Footnote parentArea = this.pslm.getCurrentPV().getBodyRegion().getFootnote();
         int topOffset = this.pslm.getCurrentPV().getBodyRegion().getBPD() - parentArea.getBPD();
         if (this.separatorArea != null) {
            topOffset -= this.separatorArea.getBPD();
         }

         parentArea.setTop(topOffset);
         parentArea.setSeparator(this.separatorArea);
      }

      this.pslm.getCurrentPV().getCurrentSpan().notifyFlowsFinished();
   }

   protected LayoutManager getCurrentChildLM() {
      return this.childFLM;
   }

   protected void observeElementList(List elementList) {
      ElementListObserver.observe(elementList, "breaker", this.pslm.getFObj().getId());
   }

   private void handleBreakTrait(int breakVal) {
      Page curPage = this.pslm.getCurrentPage();
      switch (breakVal) {
         case -1:
         case 9:
         case 28:
         case 104:
            PageViewport pv = curPage.getPageViewport();
            boolean forceNewPageWithSpan = false;
            RegionBody rb = (RegionBody)curPage.getSimplePageMaster().getRegion(58);
            forceNewPageWithSpan = rb.getColumnCount() > 1 && pv.getCurrentSpan().getColumnCount() == 1;
            if (forceNewPageWithSpan) {
               log.trace("Forcing new page with span");
               curPage = this.pslm.makeNewPage(false, false);
               curPage.getPageViewport().createSpan(true);
            } else if (pv.getCurrentSpan().hasMoreFlows()) {
               log.trace("Moving to next flow");
               pv.getCurrentSpan().moveToNextFlow();
            } else {
               log.trace("Making new page");
               this.pslm.makeNewPage(false, false);
            }

            return;
         case 5:
            curPage.getPageViewport().createSpan(true);
            return;
         case 95:
            curPage.getPageViewport().createSpan(false);
            return;
         default:
            log.debug("handling break-before after page " + this.pslm.getCurrentPageNum() + " breakVal=" + getBreakClassName(breakVal));
            if (this.needBlankPageBeforeNew(breakVal)) {
               log.trace("Inserting blank page");
               this.pslm.makeNewPage(true, false);
            }

            if (this.needNewPage(breakVal)) {
               log.trace("Making new page");
               this.pslm.makeNewPage(false, false);
            }

      }
   }

   private boolean needBlankPageBeforeNew(int breakVal) {
      if (breakVal != 104 && !this.pslm.getCurrentPage().getPageViewport().getPage().isEmpty()) {
         if (this.pslm.getCurrentPageNum() % 2 == 0) {
            return breakVal == 44;
         } else {
            return breakVal == 100;
         }
      } else {
         return false;
      }
   }

   private boolean needNewPage(int breakVal) {
      if (this.pslm.getCurrentPage().getPageViewport().getPage().isEmpty()) {
         if (breakVal == 104) {
            return false;
         } else if (this.pslm.getCurrentPageNum() % 2 == 0) {
            return breakVal == 100;
         } else {
            return breakVal == 44;
         }
      } else {
         return true;
      }
   }
}
