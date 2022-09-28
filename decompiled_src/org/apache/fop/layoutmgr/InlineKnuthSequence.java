package org.apache.fop.layoutmgr;

import java.util.LinkedList;
import java.util.List;
import org.apache.fop.layoutmgr.inline.InlineLevelLayoutManager;
import org.apache.fop.layoutmgr.inline.KnuthInlineBox;

public class InlineKnuthSequence extends KnuthSequence {
   private boolean isClosed = false;

   public InlineKnuthSequence() {
   }

   public InlineKnuthSequence(List list) {
      super(list);
   }

   public boolean isInlineSequence() {
      return true;
   }

   public boolean canAppendSequence(KnuthSequence sequence) {
      return sequence.isInlineSequence() && !this.isClosed;
   }

   public boolean appendSequence(KnuthSequence sequence) {
      if (!this.canAppendSequence(sequence)) {
         return false;
      } else {
         ListElement lastOldElement = this.getLast();
         ListElement firstNewElement = sequence.getElement(0);
         if (firstNewElement.isBox() && !((KnuthElement)firstNewElement).isAuxiliary() && lastOldElement.isBox() && ((KnuthElement)lastOldElement).getWidth() != 0) {
            this.addALetterSpace();
         }

         this.addAll(sequence);
         return true;
      }
   }

   public boolean appendSequence(KnuthSequence sequence, boolean keepTogether, BreakElement breakElement) {
      return this.appendSequence(sequence);
   }

   public KnuthSequence endSequence() {
      if (!this.isClosed) {
         this.add(new KnuthPenalty(0, -1000, false, (Position)null, false));
         this.isClosed = true;
      }

      return this;
   }

   public void addALetterSpace() {
      KnuthBox prevBox = (KnuthBox)this.getLast();
      if (!prevBox.isAuxiliary() || this.size() >= 4 && this.getElement(this.size() - 2).isGlue() && this.getElement(this.size() - 3).isPenalty() && this.getElement(this.size() - 4).isBox()) {
         this.removeLast();
         LinkedList oldList = new LinkedList();
         if (!prevBox.isAuxiliary()) {
            oldList.add(prevBox);
         } else {
            oldList.add(prevBox);
            oldList.addFirst((KnuthGlue)this.removeLast());
            oldList.addFirst((KnuthPenalty)this.removeLast());
            oldList.addFirst((KnuthBox)this.removeLast());
         }

         this.addAll(((InlineLevelLayoutManager)prevBox.getLayoutManager()).addALetterSpaceTo(oldList));
         if (prevBox instanceof KnuthInlineBox && ((KnuthInlineBox)prevBox).isAnchor()) {
            KnuthInlineBox newBox = (KnuthInlineBox)this.getLast();
            newBox.setFootnoteBodyLM(((KnuthInlineBox)prevBox).getFootnoteBodyLM());
         }

      }
   }
}
