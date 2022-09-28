package org.apache.fop.area;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class AreaTreeModel {
   private List pageSequenceList = null;
   private int currentPageSequenceIndex = -1;
   protected PageSequence currentPageSequence;
   protected static Log log;

   public AreaTreeModel() {
      this.pageSequenceList = new ArrayList();
   }

   public void startPageSequence(PageSequence pageSequence) {
      if (pageSequence == null) {
         throw new NullPointerException("pageSequence must not be null");
      } else {
         this.currentPageSequence = pageSequence;
         this.pageSequenceList.add(this.currentPageSequence);
         this.currentPageSequenceIndex = this.pageSequenceList.size() - 1;
      }
   }

   public void addPage(PageViewport page) {
      this.currentPageSequence.addPage(page);
      int pageIndex = 0;

      for(int i = 0; i < this.currentPageSequenceIndex; ++i) {
         pageIndex += ((PageSequence)this.pageSequenceList.get(i)).getPageCount();
      }

      pageIndex += this.currentPageSequence.getPageCount() - 1;
      page.setPageIndex(pageIndex);
      page.setPageSequence(this.currentPageSequence);
   }

   public void handleOffDocumentItem(OffDocumentItem ext) {
   }

   public void endDocument() throws SAXException {
   }

   public PageSequence getCurrentPageSequence() {
      return this.currentPageSequence;
   }

   public int getPageSequenceCount() {
      return this.pageSequenceList.size();
   }

   public int getPageCount(int seq) {
      PageSequence sequence = (PageSequence)this.pageSequenceList.get(seq - 1);
      return sequence.getPageCount();
   }

   public PageViewport getPage(int seq, int count) {
      PageSequence sequence = (PageSequence)this.pageSequenceList.get(seq - 1);
      return sequence.getPage(count);
   }

   static {
      log = LogFactory.getLog(AreaTreeModel.class);
   }
}
