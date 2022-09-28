package org.apache.fop.svg;

import org.apache.fop.pdf.PDFPage;

public class PDFContext {
   private PDFPage currentPage;
   private int pagecount;

   public boolean isPagePending() {
      return this.currentPage != null;
   }

   public void clearCurrentPage() {
      this.currentPage = null;
   }

   public PDFPage getCurrentPage() {
      return this.currentPage;
   }

   public void setCurrentPage(PDFPage page) {
      this.currentPage = page;
   }

   public void increasePageCount() {
      ++this.pagecount;
   }
}
