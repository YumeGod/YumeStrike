package org.apache.fop.layoutmgr.inline;

public class HyphContext {
   private int[] hyphPoints;
   private int currentOffset = 0;
   private int currentIndex = 0;

   public HyphContext(int[] hyphPoints) {
      this.hyphPoints = hyphPoints;
   }

   public int getNextHyphPoint() {
      while(this.currentIndex < this.hyphPoints.length) {
         if (this.hyphPoints[this.currentIndex] > this.currentOffset) {
            return this.hyphPoints[this.currentIndex] - this.currentOffset;
         }

         ++this.currentIndex;
      }

      return -1;
   }

   public boolean hasMoreHyphPoints() {
      while(this.currentIndex < this.hyphPoints.length) {
         if (this.hyphPoints[this.currentIndex] > this.currentOffset) {
            return true;
         }

         ++this.currentIndex;
      }

      return false;
   }

   public void updateOffset(int iCharsProcessed) {
      this.currentOffset += iCharsProcessed;
   }
}
