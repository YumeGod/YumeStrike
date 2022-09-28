package org.apache.fop.fo.flow.table;

import java.util.BitSet;
import java.util.List;

public class ColumnNumberManager {
   private int columnNumber = 1;
   private BitSet usedColumnIndices = new BitSet();

   int getCurrentColumnNumber() {
      return this.columnNumber;
   }

   void signalUsedColumnNumbers(int start, int end) {
      for(int i = start - 1; i < end; ++i) {
         this.usedColumnIndices.set(i);
      }

      for(this.columnNumber = end + 1; this.usedColumnIndices.get(this.columnNumber - 1); ++this.columnNumber) {
      }

   }

   void prepareForNextRow(List pendingSpans) {
      this.usedColumnIndices.clear();

      for(int i = 0; i < pendingSpans.size(); ++i) {
         PendingSpan pSpan = (PendingSpan)pendingSpans.get(i);
         if (pSpan != null) {
            --pSpan.rowsLeft;
            if (pSpan.rowsLeft == 0) {
               pendingSpans.set(i, (Object)null);
            } else {
               this.usedColumnIndices.set(i);
            }
         }
      }

      for(this.columnNumber = 1; this.usedColumnIndices.get(this.columnNumber - 1); ++this.columnNumber) {
      }

   }

   public boolean isColumnNumberUsed(int colNr) {
      return this.usedColumnIndices.get(colNr - 1);
   }
}
