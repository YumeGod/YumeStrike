package org.apache.fop.layoutmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.LineArea;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.PageSequenceMaster;
import org.apache.fop.fo.pagination.SideRegion;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.layoutmgr.inline.ContentLayoutManager;

public class PageSequenceLayoutManager extends AbstractPageSequenceLayoutManager {
   private static Log log;
   private PageProvider pageProvider;

   public PageSequenceLayoutManager(AreaTreeHandler ath, PageSequence pseq) {
      super(ath, pseq);
      this.pageProvider = new PageProvider(ath, pseq);
   }

   public PageProvider getPageProvider() {
      return this.pageProvider;
   }

   protected PageSequence getPageSequence() {
      return (PageSequence)this.pageSeq;
   }

   public PageSequenceLayoutManager getPSLM() {
      return this;
   }

   public void activateLayout() {
      this.initialize();
      LineArea title = null;
      if (this.getPageSequence().getTitleFO() != null) {
         try {
            ContentLayoutManager clm = this.getLayoutManagerMaker().makeContentLayoutManager(this, this.getPageSequence().getTitleFO());
            title = (LineArea)clm.getParentArea((Area)null);
         } catch (IllegalStateException var6) {
         }
      }

      AreaTreeModel areaTreeModel = this.areaTreeHandler.getAreaTreeModel();
      org.apache.fop.area.PageSequence pageSequenceAreaObject = new org.apache.fop.area.PageSequence(title);
      this.transferExtensions(pageSequenceAreaObject);
      pageSequenceAreaObject.setLanguage(this.getPageSequence().getLanguage());
      pageSequenceAreaObject.setCountry(this.getPageSequence().getCountry());
      areaTreeModel.startPageSequence(pageSequenceAreaObject);
      if (log.isDebugEnabled()) {
         log.debug("Starting layout");
      }

      this.curPage = this.makeNewPage(false, false);
      PageBreaker breaker = new PageBreaker(this);
      int flowBPD = this.getCurrentPV().getBodyRegion().getRemainingBPD();
      breaker.doLayout(flowBPD);
      this.finishPage();
   }

   public void finishPageSequence() {
      if (this.pageSeq.hasId()) {
         this.idTracker.signalIDProcessed(this.pageSeq.getId());
      }

      this.pageSeq.getRoot().notifyPageSequenceFinished(this.currentPageNum, this.currentPageNum - this.startPageNum + 1);
      this.areaTreeHandler.notifyPageSequenceFinished(this.pageSeq, this.currentPageNum - this.startPageNum + 1);
      this.getPageSequence().releasePageSequence();
      String masterReference = this.getPageSequence().getMasterReference();
      PageSequenceMaster pageSeqMaster = this.pageSeq.getRoot().getLayoutMasterSet().getPageSequenceMaster(masterReference);
      if (pageSeqMaster != null) {
         pageSeqMaster.reset();
      }

      if (log.isDebugEnabled()) {
         log.debug("Ending layout");
      }

   }

   protected Page createPage(int pageNumber, boolean isBlank) {
      return this.pageProvider.getPage(isBlank, pageNumber, 0);
   }

   private void layoutSideRegion(int regionID) {
      SideRegion reg = (SideRegion)this.curPage.getSimplePageMaster().getRegion(regionID);
      if (reg != null) {
         StaticContent sc = this.getPageSequence().getStaticContent(reg.getRegionName());
         if (sc != null) {
            StaticContentLayoutManager lm = this.getLayoutManagerMaker().makeStaticContentLayoutManager(this, sc, reg);
            lm.doLayout();
         }
      }
   }

   protected void finishPage() {
      this.layoutSideRegion(57);
      this.layoutSideRegion(56);
      this.layoutSideRegion(61);
      this.layoutSideRegion(59);
      super.finishPage();
   }

   static {
      log = LogFactory.getLog(PageSequenceLayoutManager.class);
   }
}
