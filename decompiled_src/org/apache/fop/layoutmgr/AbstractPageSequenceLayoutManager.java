package org.apache.fop.layoutmgr;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.IDTracker;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Resolvable;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.fo.flow.Marker;
import org.apache.fop.fo.flow.RetrieveMarker;
import org.apache.fop.fo.pagination.AbstractPageSequence;

public abstract class AbstractPageSequenceLayoutManager extends AbstractLayoutManager implements TopLevelLayoutManager {
   private static Log log;
   protected AreaTreeHandler areaTreeHandler;
   protected IDTracker idTracker;
   protected AbstractPageSequence pageSeq;
   protected Page curPage;
   protected int currentPageNum = 0;
   protected int startPageNum = 0;

   public AbstractPageSequenceLayoutManager(AreaTreeHandler ath, AbstractPageSequence pseq) {
      super(pseq);
      this.areaTreeHandler = ath;
      this.idTracker = ath.getIDTracker();
      this.pageSeq = pseq;
   }

   public LayoutManagerMaker getLayoutManagerMaker() {
      return this.areaTreeHandler.getLayoutManagerMaker();
   }

   public Page getCurrentPage() {
      return this.curPage;
   }

   protected void setCurrentPage(Page currentPage) {
      this.curPage = currentPage;
   }

   protected int getCurrentPageNum() {
      return this.currentPageNum;
   }

   public void initialize() {
      this.startPageNum = this.pageSeq.getStartingPageNumber();
      this.currentPageNum = this.startPageNum - 1;
   }

   public PageViewport getFirstPVWithID(String idref) {
      List list = this.idTracker.getPageViewportsContainingID(idref);
      return list != null && list.size() > 0 ? (PageViewport)list.get(0) : null;
   }

   public PageViewport getLastPVWithID(String idref) {
      List list = this.idTracker.getPageViewportsContainingID(idref);
      return list != null && list.size() > 0 ? (PageViewport)list.get(list.size() - 1) : null;
   }

   public void addIDToPage(String id) {
      if (id != null && id.length() > 0) {
         this.idTracker.associateIDWithPageViewport(id, this.curPage.getPageViewport());
      }

   }

   public boolean associateLayoutManagerID(String id) {
      if (log.isDebugEnabled()) {
         log.debug("associateLayoutManagerID(" + id + ")");
      }

      if (!this.idTracker.alreadyResolvedID(id)) {
         this.idTracker.signalPendingID(id);
         return false;
      } else {
         return true;
      }
   }

   public void notifyEndOfLayout(String id) {
      this.idTracker.signalIDProcessed(id);
   }

   public void addUnresolvedArea(String id, Resolvable res) {
      this.curPage.getPageViewport().addUnresolvedIDRef(id, res);
      this.idTracker.addUnresolvedIDRef(id, this.curPage.getPageViewport());
   }

   public RetrieveMarker resolveRetrieveMarker(RetrieveMarker rm) {
      AreaTreeModel areaTreeModel = this.areaTreeHandler.getAreaTreeModel();
      String name = rm.getRetrieveClassName();
      int pos = rm.getRetrievePosition();
      int boundary = rm.getRetrieveBoundary();
      Marker mark = (Marker)this.getCurrentPV().getMarker(name, pos);
      if (mark == null && boundary != 104) {
         boolean doc = boundary == 34;
         int seq = areaTreeModel.getPageSequenceCount();

         int page;
         for(page = areaTreeModel.getPageCount(seq) - 1; page < 0 && doc && seq > 1; page = areaTreeModel.getPageCount(seq) - 1) {
            --seq;
         }

         while(page >= 0) {
            PageViewport pv = areaTreeModel.getPage(seq, page);
            mark = (Marker)pv.getMarker(name, 74);
            if (mark != null) {
               break;
            }

            --page;
            if (page < 0 && doc && seq > 1) {
               --seq;
               page = areaTreeModel.getPageCount(seq) - 1;
            }
         }
      }

      if (mark == null) {
         log.debug("found no marker with name: " + name);
         return null;
      } else {
         rm.bindMarker(mark);
         return rm;
      }
   }

   protected abstract Page createPage(int var1, boolean var2);

   protected Page makeNewPage(boolean isBlank, boolean isLast) {
      if (this.curPage != null) {
         this.finishPage();
      }

      ++this.currentPageNum;
      this.curPage = this.createPage(this.currentPageNum, isBlank);
      if (log.isDebugEnabled()) {
         log.debug("[" + this.curPage.getPageViewport().getPageNumberString() + (isBlank ? "*" : "") + "]");
      }

      this.addIDToPage(this.pageSeq.getId());
      return this.curPage;
   }

   protected void finishPage() {
      if (log.isTraceEnabled()) {
         this.curPage.getPageViewport().dumpMarkers();
      }

      this.idTracker.tryIDResolution(this.curPage.getPageViewport());
      this.areaTreeHandler.getAreaTreeModel().addPage(this.curPage.getPageViewport());
      if (log.isDebugEnabled()) {
         log.debug("page finished: " + this.curPage.getPageViewport().getPageNumberString() + ", current num: " + this.currentPageNum);
      }

      this.curPage = null;
   }

   public void doForcePageCount(Numeric nextPageSeqInitialPageNumber) {
      int forcePageCount = this.pageSeq.getForcePageCount();
      if (nextPageSeqInitialPageNumber != null && forcePageCount == 9) {
         int nextPageSeqPageNumberType;
         if (nextPageSeqInitialPageNumber.getEnum() != 0) {
            nextPageSeqPageNumberType = nextPageSeqInitialPageNumber.getEnum();
            if (nextPageSeqPageNumberType == 11) {
               forcePageCount = 40;
            } else if (nextPageSeqPageNumberType == 10) {
               forcePageCount = 41;
            } else {
               forcePageCount = 88;
            }
         } else {
            nextPageSeqPageNumberType = nextPageSeqInitialPageNumber.getValue();
            nextPageSeqPageNumberType = nextPageSeqPageNumberType > 0 ? nextPageSeqPageNumberType : 1;
            if (nextPageSeqPageNumberType % 2 == 0) {
               forcePageCount = 41;
            } else {
               forcePageCount = 40;
            }
         }
      }

      if (forcePageCount == 43) {
         if ((this.currentPageNum - this.startPageNum + 1) % 2 != 0) {
            this.curPage = this.makeNewPage(true, false);
         }
      } else if (forcePageCount == 99) {
         if ((this.currentPageNum - this.startPageNum + 1) % 2 == 0) {
            this.curPage = this.makeNewPage(true, false);
         }
      } else if (forcePageCount == 40) {
         if (this.currentPageNum % 2 != 0) {
            this.curPage = this.makeNewPage(true, false);
         }
      } else if (forcePageCount == 41) {
         if (this.currentPageNum % 2 == 0) {
            this.curPage = this.makeNewPage(true, false);
         }
      } else if (forcePageCount == 88) {
      }

      if (this.curPage != null) {
         this.finishPage();
      }

   }

   public void reset() {
      throw new IllegalStateException();
   }

   static {
      log = LogFactory.getLog(AbstractPageSequenceLayoutManager.class);
   }
}
