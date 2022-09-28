package org.apache.fop.apps;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.fo.pagination.AbstractPageSequence;

public class FormattingResults {
   private int pageCount = 0;
   private List pageSequences = null;

   public int getPageCount() {
      return this.pageCount;
   }

   public List getPageSequences() {
      return this.pageSequences;
   }

   public void reset() {
      this.pageCount = 0;
      if (this.pageSequences != null) {
         this.pageSequences.clear();
      }

   }

   public void haveFormattedPageSequence(AbstractPageSequence pageSequence, int pageCount) {
      this.pageCount += pageCount;
      if (this.pageSequences == null) {
         this.pageSequences = new ArrayList();
      }

      this.pageSequences.add(new PageSequenceResults(pageSequence.getId(), pageCount));
   }
}
