package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBlockBox;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.traits.MinOptMax;

class ActiveCell {
   private static Log log;
   private PrimaryGridUnit pgu;
   private List elementList;
   private ListIterator knuthIter;
   private int endRowIndex;
   private int remainingLength;
   private int totalLength;
   private int includedLength;
   private int paddingBeforeNormal;
   private int paddingBeforeLeading;
   private int paddingAfterNormal;
   private int paddingAfterTrailing;
   private int bpBeforeNormal;
   private int bpBeforeLeading;
   private int bpAfterNormal;
   private int bpAfterTrailing;
   private boolean lastCellPart;
   private Keep keepWithNext;
   private int spanIndex = 0;
   private Step previousStep;
   private Step nextStep;
   private Step afterNextStep;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   static int getElementContentLength(KnuthElement el) {
      if (el instanceof FillerPenalty) {
         return ((FillerPenalty)el).contentLength;
      } else {
         return el instanceof FillerBox ? 0 : el.getWidth();
      }
   }

   ActiveCell(PrimaryGridUnit pgu, EffRow row, int rowIndex, int previousRowsLength, TableLayoutManager tableLM) {
      this.pgu = pgu;
      CommonBorderPaddingBackground bordersPaddings = pgu.getCell().getCommonBorderPaddingBackground();
      TableCellLayoutManager cellLM = pgu.getCellLM();
      this.paddingBeforeNormal = bordersPaddings.getPaddingBefore(false, cellLM);
      this.paddingBeforeLeading = bordersPaddings.getPaddingBefore(true, cellLM);
      this.paddingAfterNormal = bordersPaddings.getPaddingAfter(false, cellLM);
      this.paddingAfterTrailing = bordersPaddings.getPaddingAfter(true, cellLM);
      this.bpBeforeNormal = this.paddingBeforeNormal + pgu.getBeforeBorderWidth(0, 0);
      this.bpBeforeLeading = this.paddingBeforeLeading + pgu.getBeforeBorderWidth(0, 2);
      this.bpAfterNormal = this.paddingAfterNormal + pgu.getAfterBorderWidth(0);
      this.bpAfterTrailing = this.paddingAfterTrailing + pgu.getAfterBorderWidth(0, 2);
      this.elementList = pgu.getElements();
      this.handleExplicitHeight(pgu.getCell().getBlockProgressionDimension().toMinOptMax(tableLM), row.getExplicitHeight());
      this.knuthIter = this.elementList.listIterator();
      this.includedLength = -1;
      this.totalLength = previousRowsLength + ElementListUtils.calcContentLength(this.elementList);
      this.endRowIndex = rowIndex + pgu.getCell().getNumberRowsSpanned() - 1;
      this.keepWithNext = Keep.KEEP_AUTO;
      this.remainingLength = this.totalLength - previousRowsLength;
      this.afterNextStep = new Step(previousRowsLength);
      this.previousStep = new Step(this.afterNextStep);
      this.gotoNextLegalBreak();
      this.nextStep = new Step(this.afterNextStep);
      if (this.afterNextStep.end < this.elementList.size() - 1) {
         this.gotoNextLegalBreak();
      }

   }

   private void handleExplicitHeight(MinOptMax cellBPD, MinOptMax rowBPD) {
      int minBPD = Math.max(cellBPD.getMin(), rowBPD.getMin());
      if (minBPD > 0) {
         ListIterator iter = this.elementList.listIterator();
         int cumulateLength = 0;
         boolean prevIsBox = false;

         while(iter.hasNext() && cumulateLength < minBPD) {
            KnuthElement el = (KnuthElement)iter.next();
            if (el.isBox()) {
               prevIsBox = true;
               cumulateLength += el.getWidth();
            } else if (el.isGlue()) {
               if (prevIsBox) {
                  this.elementList.add(iter.nextIndex() - 1, new FillerPenalty(minBPD - cumulateLength));
               }

               prevIsBox = false;
               cumulateLength += el.getWidth();
            } else {
               prevIsBox = false;
               if (cumulateLength + el.getWidth() < minBPD) {
                  iter.set(new FillerPenalty((KnuthPenalty)el, minBPD - cumulateLength));
               }
            }
         }
      }

      int optBPD = Math.max(minBPD, Math.max(cellBPD.getOpt(), rowBPD.getOpt()));
      if (this.pgu.getContentLength() < optBPD) {
         this.elementList.add(new FillerBox(optBPD - this.pgu.getContentLength()));
      }

   }

   PrimaryGridUnit getPrimaryGridUnit() {
      return this.pgu;
   }

   boolean endsOnRow(int rowIndex) {
      return rowIndex == this.endRowIndex;
   }

   int getRemainingLength() {
      return this.includedInLastStep() && this.nextStep.end == this.elementList.size() - 1 ? 0 : this.bpBeforeLeading + this.remainingLength + this.bpAfterNormal;
   }

   private void gotoNextLegalBreak() {
      this.afterNextStep.penaltyLength = 0;
      this.afterNextStep.penaltyValue = 0;
      this.afterNextStep.condBeforeContentLength = 0;
      this.afterNextStep.breakClass = 9;
      if (this.afterNextStep.footnoteList != null) {
         this.afterNextStep.footnoteList.clear();
      }

      boolean breakFound = false;
      boolean prevIsBox = false;
      boolean boxFound = false;

      while(!breakFound && this.knuthIter.hasNext()) {
         KnuthElement el = (KnuthElement)this.knuthIter.next();
         if (el.isPenalty()) {
            prevIsBox = false;
            if (el.getPenalty() < 1000 || ((KnuthPenalty)el).getBreakClass() == 104) {
               breakFound = true;
               KnuthPenalty p = (KnuthPenalty)el;
               this.afterNextStep.penaltyLength = p.getWidth();
               this.afterNextStep.penaltyValue = p.getPenalty();
               if (p.isForcedBreak()) {
                  this.afterNextStep.breakClass = p.getBreakClass();
               }
            }
         } else if (el.isGlue()) {
            if (prevIsBox) {
               breakFound = true;
            } else {
               this.afterNextStep.contentLength = el.getWidth();
               if (!boxFound) {
                  this.afterNextStep.condBeforeContentLength = el.getWidth();
               }
            }

            prevIsBox = false;
         } else {
            if (el instanceof KnuthBlockBox && ((KnuthBlockBox)el).hasAnchors()) {
               if (this.afterNextStep.footnoteList == null) {
                  this.afterNextStep.footnoteList = new LinkedList();
               }

               this.afterNextStep.footnoteList.addAll(((KnuthBlockBox)el).getFootnoteBodyLMs());
            }

            prevIsBox = true;
            boxFound = true;
            this.afterNextStep.contentLength = el.getWidth();
         }
      }

      this.afterNextStep.end = this.knuthIter.nextIndex() - 1;
      this.afterNextStep.totalLength = this.bpBeforeNormal + this.afterNextStep.contentLength + this.afterNextStep.penaltyLength + this.bpAfterTrailing;
   }

   int getFirstStep() {
      log.debug(this + ": min first step = " + this.nextStep.totalLength);
      return this.nextStep.totalLength;
   }

   int getLastStep() {
      if (!$assertionsDisabled && this.nextStep.end != this.elementList.size() - 1) {
         throw new AssertionError();
      } else if ($assertionsDisabled || this.nextStep.contentLength == this.totalLength && this.nextStep.penaltyLength == 0) {
         int lastStep = this.bpBeforeNormal + this.totalLength + this.paddingAfterNormal + this.pgu.getAfterBorderWidth(1);
         log.debug(this + ": last step = " + lastStep);
         return lastStep;
      } else {
         throw new AssertionError();
      }
   }

   private void increaseCurrentStep(int limit) {
      if (this.nextStep.end < this.elementList.size() - 1) {
         while(this.afterNextStep.totalLength <= limit && this.nextStep.breakClass == 9) {
            int condBeforeContentLength = this.nextStep.condBeforeContentLength;
            this.nextStep.set(this.afterNextStep);
            this.nextStep.condBeforeContentLength = condBeforeContentLength;
            if (this.afterNextStep.end >= this.elementList.size() - 1) {
               break;
            }

            this.gotoNextLegalBreak();
         }
      }

   }

   void signalRowFirstStep(int firstStep) {
      this.increaseCurrentStep(firstStep);
      if (log.isTraceEnabled()) {
         log.trace(this + ": first step increased to " + this.nextStep.totalLength);
      }

   }

   void signalRowLastStep(int lastStep) {
      this.increaseCurrentStep(lastStep);
      if (log.isTraceEnabled()) {
         log.trace(this + ": next step increased to " + this.nextStep.totalLength);
      }

   }

   int getNextStep() {
      if (this.includedInLastStep()) {
         this.previousStep.set(this.nextStep);
         if (this.nextStep.end >= this.elementList.size() - 1) {
            this.nextStep.start = this.elementList.size();
            return -1;
         }

         this.nextStep.set(this.afterNextStep);
         this.nextStep.start = this.previousStep.end + 1;
         this.afterNextStep.start = this.nextStep.start;
         if (this.afterNextStep.end < this.elementList.size() - 1) {
            this.gotoNextLegalBreak();
         }
      }

      return this.nextStep.totalLength;
   }

   private boolean includedInLastStep() {
      return this.includedLength == this.nextStep.contentLength;
   }

   int signalNextStep(int minStep) {
      if (this.nextStep.totalLength <= minStep) {
         this.includedLength = this.nextStep.contentLength;
         this.remainingLength = this.totalLength - this.includedLength - this.afterNextStep.condBeforeContentLength;
         return this.nextStep.breakClass;
      } else {
         return 9;
      }
   }

   void nextRowStarts() {
      ++this.spanIndex;
      this.nextStep.totalLength = this.bpAfterTrailing;
      this.afterNextStep.totalLength = this.bpAfterTrailing;
      this.bpAfterTrailing = this.paddingAfterTrailing + this.pgu.getAfterBorderWidth(this.spanIndex, 2);
      this.nextStep.totalLength = this.bpAfterTrailing;
      this.afterNextStep.totalLength = this.bpAfterTrailing;
   }

   void endRow(int rowIndex) {
      if (this.endsOnRow(rowIndex)) {
         this.nextStep.totalLength = this.bpAfterTrailing;
         this.bpAfterTrailing = this.paddingAfterNormal + this.pgu.getAfterBorderWidth(1);
         this.nextStep.totalLength = this.bpAfterTrailing;
         this.lastCellPart = true;
      } else {
         this.bpBeforeLeading = this.paddingBeforeLeading + this.pgu.getBeforeBorderWidth(this.spanIndex + 1, 2);
      }

   }

   boolean finishes(int step) {
      return this.nextStep.totalLength <= step && this.nextStep.end == this.elementList.size() - 1;
   }

   CellPart createCellPart() {
      if (this.nextStep.end + 1 == this.elementList.size()) {
         this.keepWithNext = this.pgu.getKeepWithNext();
      }

      int bpBeforeFirst;
      if (this.nextStep.start == 0) {
         bpBeforeFirst = this.pgu.getBeforeBorderWidth(0, 1) + this.paddingBeforeNormal;
      } else {
         bpBeforeFirst = this.bpBeforeLeading;
      }

      int length = this.nextStep.contentLength - this.nextStep.condBeforeContentLength - this.previousStep.contentLength;
      return this.includedInLastStep() && this.nextStep.start != this.elementList.size() ? new CellPart(this.pgu, this.nextStep.start, this.nextStep.end, this.lastCellPart, this.nextStep.condBeforeContentLength, length, this.nextStep.penaltyLength, this.bpBeforeNormal, bpBeforeFirst, this.bpAfterNormal, this.bpAfterTrailing) : new CellPart(this.pgu, this.nextStep.start, this.previousStep.end, this.lastCellPart, 0, 0, this.previousStep.penaltyLength, this.bpBeforeNormal, bpBeforeFirst, this.bpAfterNormal, this.bpAfterTrailing);
   }

   void addFootnotes(List footnoteList) {
      if (this.includedInLastStep() && this.nextStep.footnoteList != null) {
         footnoteList.addAll(this.nextStep.footnoteList);
         this.nextStep.footnoteList.clear();
      }

   }

   Keep getKeepWithNext() {
      return this.keepWithNext;
   }

   int getPenaltyValue() {
      return this.includedInLastStep() ? this.nextStep.penaltyValue : this.previousStep.penaltyValue;
   }

   public String toString() {
      return "Cell " + (this.pgu.getRowIndex() + 1) + "." + (this.pgu.getColIndex() + 1);
   }

   static {
      $assertionsDisabled = !ActiveCell.class.desiredAssertionStatus();
      log = LogFactory.getLog(ActiveCell.class);
   }

   private static class FillerBox extends KnuthBox {
      FillerBox(int length) {
         super(length, (Position)null, true);
      }
   }

   private static class FillerPenalty extends KnuthPenalty {
      private int contentLength;

      FillerPenalty(KnuthPenalty p, int length) {
         super(length, p.getPenalty(), p.isPenaltyFlagged(), p.getBreakClass(), p.getPosition(), p.isAuxiliary());
         this.contentLength = p.getWidth();
      }

      FillerPenalty(int length) {
         super(length, 0, false, (Position)null, true);
         this.contentLength = 0;
      }
   }

   private static class Step {
      private int start;
      private int end;
      private int contentLength;
      private int totalLength;
      private int penaltyLength;
      private int penaltyValue;
      private List footnoteList;
      private int breakClass;
      private int condBeforeContentLength;

      Step(int contentLength) {
         this.contentLength = contentLength;
         this.end = -1;
      }

      Step(Step other) {
         this.set(other);
      }

      void set(Step other) {
         this.start = other.start;
         this.end = other.end;
         this.contentLength = other.contentLength;
         this.totalLength = other.totalLength;
         this.penaltyLength = other.penaltyLength;
         this.penaltyValue = other.penaltyValue;
         if (other.footnoteList != null) {
            if (this.footnoteList == null) {
               this.footnoteList = new ArrayList();
            }

            this.footnoteList.addAll(other.footnoteList);
         }

         this.condBeforeContentLength = other.condBeforeContentLength;
         this.breakClass = other.breakClass;
      }

      public String toString() {
         return "Step: start=" + this.start + " end=" + this.end + " length=" + this.totalLength;
      }
   }
}
