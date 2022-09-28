package org.apache.fop.layoutmgr;

import java.util.List;

public class BlockKnuthSequence extends KnuthSequence {
   private boolean isClosed = false;

   public BlockKnuthSequence() {
   }

   public BlockKnuthSequence(List list) {
      super(list);
   }

   public boolean isInlineSequence() {
      return false;
   }

   public boolean canAppendSequence(KnuthSequence sequence) {
      return !sequence.isInlineSequence() && !this.isClosed;
   }

   public boolean appendSequence(KnuthSequence sequence) {
      return false;
   }

   public boolean appendSequence(KnuthSequence sequence, boolean keepTogether, BreakElement breakElement) {
      if (!this.canAppendSequence(sequence)) {
         return false;
      } else {
         if (keepTogether) {
            breakElement.setPenaltyValue(1000);
            this.add(breakElement);
         } else if (!this.getLast().isGlue()) {
            breakElement.setPenaltyValue(0);
            this.add(breakElement);
         }

         this.addAll(sequence);
         return true;
      }
   }

   public KnuthSequence endSequence() {
      this.isClosed = true;
      return this;
   }
}
