package org.apache.fop.apps;

public class PageSequenceResults {
   private String id;
   private int pageCount;

   public PageSequenceResults(String id, int pageCount) {
      this.id = id;
      this.pageCount = pageCount;
   }

   public String getID() {
      return this.id;
   }

   public int getPageCount() {
      return this.pageCount;
   }
}
