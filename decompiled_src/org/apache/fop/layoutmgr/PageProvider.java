package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.Region;
import org.apache.fop.fo.pagination.SimplePageMaster;

public class PageProvider implements Constants {
   private Log log;
   public static final int RELTO_PAGE_SEQUENCE = 0;
   public static final int RELTO_CURRENT_ELEMENT_LIST = 1;
   private int startPageOfPageSequence;
   private int startPageOfCurrentElementList;
   private int startColumnOfCurrentElementList;
   private List cachedPages;
   private int lastPageIndex;
   private int indexOfCachedLastPage;
   private int lastRequestedIndex;
   private int lastReportedBPD;
   private AreaTreeHandler areaTreeHandler;
   private PageSequence pageSeq;

   public PageProvider(AreaTreeHandler ath, PageSequence ps) {
      this.log = LogFactory.getLog(PageProvider.class);
      this.cachedPages = new ArrayList();
      this.lastPageIndex = -1;
      this.indexOfCachedLastPage = -1;
      this.lastRequestedIndex = -1;
      this.lastReportedBPD = -1;
      this.areaTreeHandler = ath;
      this.pageSeq = ps;
      this.startPageOfPageSequence = ps.getStartingPageNumber();
   }

   public void setStartOfNextElementList(int startPage, int startColumn) {
      this.log.debug("start of the next element list is: page=" + startPage + " col=" + startColumn);
      this.startPageOfCurrentElementList = startPage - this.startPageOfPageSequence + 1;
      this.startColumnOfCurrentElementList = startColumn;
      this.lastRequestedIndex = -1;
      this.lastReportedBPD = -1;
   }

   public void setLastPageIndex(int index) {
      this.lastPageIndex = index;
   }

   public int getAvailableBPD(int index) {
      if (this.lastRequestedIndex == index) {
         if (this.log.isTraceEnabled()) {
            this.log.trace("getAvailableBPD(" + index + ") -> (cached) " + this.lastReportedBPD);
         }

         return this.lastReportedBPD;
      } else {
         int c = index;
         int pageIndex = 0;
         int colIndex = this.startColumnOfCurrentElementList;

         Page page;
         for(page = this.getPage(false, pageIndex, 1); c > 0; --c) {
            ++colIndex;
            if (colIndex >= page.getPageViewport().getCurrentSpan().getColumnCount()) {
               colIndex = 0;
               ++pageIndex;
               page = this.getPage(false, pageIndex, 1);
            }
         }

         this.lastRequestedIndex = index;
         this.lastReportedBPD = page.getPageViewport().getBodyRegion().getRemainingBPD();
         if (this.log.isTraceEnabled()) {
            this.log.trace("getAvailableBPD(" + index + ") -> " + this.lastReportedBPD);
         }

         return this.lastReportedBPD;
      }
   }

   private int[] getColIndexAndColCount(int index) {
      int columnCount = 0;
      int colIndex = this.startColumnOfCurrentElementList + index;
      int pageIndex = -1;

      do {
         colIndex -= columnCount;
         ++pageIndex;
         Page page = this.getPage(false, pageIndex, 1);
         columnCount = page.getPageViewport().getCurrentSpan().getColumnCount();
      } while(colIndex >= columnCount);

      return new int[]{colIndex, columnCount};
   }

   public int compareIPDs(int index) {
      int columnCount = 0;
      int colIndex = this.startColumnOfCurrentElementList + index;
      int pageIndex = -1;

      Page page;
      do {
         colIndex -= columnCount;
         ++pageIndex;
         page = this.getPage(false, pageIndex, 1);
         columnCount = page.getPageViewport().getCurrentSpan().getColumnCount();
      } while(colIndex >= columnCount);

      if (colIndex + 1 < columnCount) {
         return 0;
      } else {
         Page nextPage = this.getPage(false, pageIndex + 1, 1);
         return page.getPageViewport().getBodyRegion().getIPD() - nextPage.getPageViewport().getBodyRegion().getIPD();
      }
   }

   boolean startPage(int index) {
      return this.getColIndexAndColCount(index)[0] == 0;
   }

   boolean endPage(int index) {
      int[] colIndexAndColCount = this.getColIndexAndColCount(index);
      return colIndexAndColCount[0] == colIndexAndColCount[1] - 1;
   }

   int getColumnCount(int index) {
      return this.getColIndexAndColCount(index)[1];
   }

   public int getStartingPartIndexForLastPage(int partCount) {
      int result = 0;
      int idx = 0;
      int pageIndex = 0;
      int colIndex = this.startColumnOfCurrentElementList;

      for(Page page = this.getPage(false, pageIndex, 1); idx < partCount; ++idx) {
         if (colIndex >= page.getPageViewport().getCurrentSpan().getColumnCount()) {
            colIndex = 0;
            ++pageIndex;
            page = this.getPage(false, pageIndex, 1);
            result = idx;
         }

         ++colIndex;
      }

      return result;
   }

   public Page getPage(boolean isBlank, int index, int relativeTo) {
      if (relativeTo == 0) {
         return this.getPage(isBlank, index);
      } else if (relativeTo == 1) {
         int effIndex = this.startPageOfCurrentElementList + index;
         effIndex += this.startPageOfPageSequence - 1;
         return this.getPage(isBlank, effIndex);
      } else {
         throw new IllegalArgumentException("Illegal value for relativeTo: " + relativeTo);
      }
   }

   protected Page getPage(boolean isBlank, int index) {
      boolean isLastPage = this.lastPageIndex >= 0 && index == this.lastPageIndex;
      if (this.log.isTraceEnabled()) {
         this.log.trace("getPage(" + index + " " + (isBlank ? "blank" : "non-blank") + (isLastPage ? " <LAST>" : "") + ")");
      }

      int intIndex = index - this.startPageOfPageSequence;
      if (this.log.isTraceEnabled()) {
         if (isBlank) {
            this.log.trace("blank page requested: " + index);
         }

         if (isLastPage) {
            this.log.trace("last page requested: " + index);
         }
      }

      for(; intIndex >= this.cachedPages.size(); this.cacheNextPage(index, isBlank, isLastPage)) {
         if (this.log.isTraceEnabled()) {
            this.log.trace("Caching " + index);
         }
      }

      Page page = (Page)this.cachedPages.get(intIndex);
      boolean replace = false;
      if (page.getPageViewport().isBlank() != isBlank) {
         this.log.debug("blank condition doesn't match. Replacing PageViewport.");
         replace = true;
      }

      if (isLastPage && this.indexOfCachedLastPage != intIndex || !isLastPage && this.indexOfCachedLastPage >= 0) {
         this.log.debug("last page condition doesn't match. Replacing PageViewport.");
         replace = true;
         this.indexOfCachedLastPage = isLastPage ? intIndex : -1;
      }

      if (replace) {
         this.discardCacheStartingWith(intIndex);
         page = this.cacheNextPage(index, isBlank, isLastPage);
      }

      return page;
   }

   private void discardCacheStartingWith(int index) {
      while(index < this.cachedPages.size()) {
         this.cachedPages.remove(this.cachedPages.size() - 1);
         if (!this.pageSeq.goToPreviousSimplePageMaster()) {
            this.log.warn("goToPreviousSimplePageMaster() on the first page called!");
         }
      }

   }

   private Page cacheNextPage(int index, boolean isBlank, boolean isLastPage) {
      String pageNumberString = this.pageSeq.makeFormattedPageNumber(index);
      boolean isFirstPage = this.startPageOfPageSequence == index;
      SimplePageMaster spm = this.pageSeq.getNextSimplePageMaster(index, isFirstPage, isLastPage, isBlank);
      Region body = spm.getRegion(58);
      if (!this.pageSeq.getMainFlow().getFlowName().equals(body.getRegionName())) {
         BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.pageSeq.getUserAgent().getEventBroadcaster());
         eventProducer.flowNotMappingToRegionBody(this, this.pageSeq.getMainFlow().getFlowName(), spm.getMasterName(), spm.getLocator());
      }

      Page page = new Page(spm, index, pageNumberString, isBlank);
      page.getPageViewport().setKey(this.areaTreeHandler.generatePageViewportKey());
      page.getPageViewport().setForeignAttributes(spm.getForeignAttributes());
      this.cachedPages.add(page);
      return page;
   }
}
