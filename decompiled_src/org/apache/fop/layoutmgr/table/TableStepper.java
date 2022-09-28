package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.GridUnit;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBlockBox;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.util.BreakUtil;

public class TableStepper {
   private static Log log;
   private TableContentLayoutManager tclm;
   private EffRow[] rowGroup;
   private int columnCount;
   private int totalHeight;
   private int previousRowsLength;
   private int activeRowIndex;
   private boolean rowFinished;
   private List activeCells = new LinkedList();
   private List nextActiveCells = new LinkedList();
   private boolean delayingNextRow;
   private int rowFirstStep;
   private boolean rowHeightSmallerThanFirstStep;
   private int nextBreakClass;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public TableStepper(TableContentLayoutManager tclm) {
      this.tclm = tclm;
      this.columnCount = tclm.getTableLM().getTable().getNumberOfColumns();
   }

   private void setup(EffRow[] rows) {
      this.rowGroup = rows;
      this.previousRowsLength = 0;
      this.activeRowIndex = 0;
      this.activeCells.clear();
      this.nextActiveCells.clear();
      this.delayingNextRow = false;
      this.rowFirstStep = 0;
      this.rowHeightSmallerThanFirstStep = false;
   }

   private void calcTotalHeight() {
      this.totalHeight = 0;

      for(int i = 0; i < this.rowGroup.length; ++i) {
         this.totalHeight += this.rowGroup[i].getHeight().getOpt();
      }

      if (log.isDebugEnabled()) {
         log.debug("totalHeight=" + this.totalHeight);
      }

   }

   private int getMaxRemainingHeight() {
      int maxW = 0;

      int remain;
      for(Iterator iter = this.activeCells.iterator(); iter.hasNext(); maxW = Math.max(maxW, remain)) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         remain = activeCell.getRemainingLength();
         PrimaryGridUnit pgu = activeCell.getPrimaryGridUnit();

         for(int i = this.activeRowIndex + 1; i < pgu.getRowIndex() - this.rowGroup[0].getIndex() + pgu.getCell().getNumberRowsSpanned(); ++i) {
            remain -= this.rowGroup[i].getHeight().getOpt();
         }
      }

      for(int i = this.activeRowIndex + 1; i < this.rowGroup.length; ++i) {
         maxW += this.rowGroup[i].getHeight().getOpt();
      }

      return maxW;
   }

   private void activateCells(List activeCellList, int rowIndex) {
      EffRow row = this.rowGroup[rowIndex];

      for(int i = 0; i < this.columnCount; ++i) {
         GridUnit gu = row.getGridUnit(i);
         if (!gu.isEmpty() && gu.isPrimary()) {
            activeCellList.add(new ActiveCell((PrimaryGridUnit)gu, row, rowIndex, this.previousRowsLength, this.getTableLM()));
         }
      }

   }

   public LinkedList getCombinedKnuthElementsForRowGroup(LayoutContext context, EffRow[] rows, int bodyType) {
      this.setup(rows);
      this.activateCells(this.activeCells, 0);
      this.calcTotalHeight();
      int cumulateLength = 0;
      TableContentPosition lastTCPos = null;
      LinkedList returnList = new LinkedList();
      int laststep = 0;
      int step = this.getFirstStep();

      TableContentPosition tcpos;
      do {
         int maxRemainingHeight = this.getMaxRemainingHeight();
         int penaltyOrGlueLen = step + maxRemainingHeight - this.totalHeight;
         int boxLen = step - cumulateLength - Math.max(0, penaltyOrGlueLen);
         cumulateLength += boxLen + Math.max(0, -penaltyOrGlueLen);
         if (log.isDebugEnabled()) {
            log.debug("Next step: " + step + " (+" + (step - laststep) + ")");
            log.debug("           max remaining height: " + maxRemainingHeight);
            if (penaltyOrGlueLen >= 0) {
               log.debug("           box = " + boxLen + " penalty = " + penaltyOrGlueLen);
            } else {
               log.debug("           box = " + boxLen + " glue = " + -penaltyOrGlueLen);
            }
         }

         LinkedList footnoteList = new LinkedList();
         List cellParts = new ArrayList(this.columnCount);
         Iterator iter = this.activeCells.iterator();

         while(iter.hasNext()) {
            ActiveCell activeCell = (ActiveCell)iter.next();
            CellPart part = activeCell.createCellPart();
            cellParts.add(part);
            activeCell.addFootnotes(footnoteList);
         }

         tcpos = new TableContentPosition(this.getTableLM(), cellParts, this.rowGroup[this.activeRowIndex]);
         if (this.delayingNextRow) {
            tcpos.setNewPageRow(this.rowGroup[this.activeRowIndex + 1]);
         }

         if (returnList.size() == 0) {
            tcpos.setFlag(1, true);
         }

         if (footnoteList.isEmpty()) {
            returnList.add(new KnuthBox(boxLen, tcpos, false));
         } else {
            returnList.add(new KnuthBlockBox(boxLen, footnoteList, tcpos, false));
         }

         int effPenaltyLen = Math.max(0, penaltyOrGlueLen);
         TableHFPenaltyPosition penaltyPos = new TableHFPenaltyPosition(this.getTableLM());
         if (bodyType == 0) {
            if (!this.getTableLM().getTable().omitHeaderAtBreak()) {
               effPenaltyLen += this.tclm.getHeaderNetHeight();
               penaltyPos.headerElements = this.tclm.getHeaderElements();
            }

            if (!this.getTableLM().getTable().omitFooterAtBreak()) {
               effPenaltyLen += this.tclm.getFooterNetHeight();
               penaltyPos.footerElements = this.tclm.getFooterElements();
            }
         }

         Keep keep = Keep.KEEP_AUTO;
         int stepPenalty = 0;

         ActiveCell activeCell;
         for(Iterator iter = this.activeCells.iterator(); iter.hasNext(); stepPenalty = Math.max(stepPenalty, activeCell.getPenaltyValue())) {
            activeCell = (ActiveCell)iter.next();
            keep = keep.compare(activeCell.getKeepWithNext());
         }

         if (!this.rowFinished) {
            keep = keep.compare(this.rowGroup[this.activeRowIndex].getKeepTogether());
            keep = keep.compare(this.getTableLM().getKeepTogether());
         } else if (this.activeRowIndex < this.rowGroup.length - 1) {
            keep = keep.compare(this.rowGroup[this.activeRowIndex].getKeepWithNext());
            keep = keep.compare(this.rowGroup[this.activeRowIndex + 1].getKeepWithPrevious());
            this.nextBreakClass = BreakUtil.compareBreakClasses(this.nextBreakClass, this.rowGroup[this.activeRowIndex].getBreakAfter());
            this.nextBreakClass = BreakUtil.compareBreakClasses(this.nextBreakClass, this.rowGroup[this.activeRowIndex + 1].getBreakBefore());
         }

         int p = keep.getPenalty();
         if (this.rowHeightSmallerThanFirstStep) {
            this.rowHeightSmallerThanFirstStep = false;
            p = 1000;
         }

         p = Math.max(p, stepPenalty);
         int breakClass = keep.getContext();
         if (this.nextBreakClass != 9) {
            log.trace("Forced break encountered");
            p = -1000;
            breakClass = this.nextBreakClass;
         }

         returnList.add(new BreakElement(penaltyPos, effPenaltyLen, p, breakClass, context));
         if (penaltyOrGlueLen < 0) {
            returnList.add(new KnuthGlue(-penaltyOrGlueLen, 0, 0, new Position((LayoutManager)null), true));
         }

         laststep = step;
         step = this.getNextStep();
      } while(step >= 0);

      if (!$assertionsDisabled && returnList.isEmpty()) {
         throw new AssertionError();
      } else {
         tcpos.setFlag(2, true);
         return returnList;
      }
   }

   private int getFirstStep() {
      this.computeRowFirstStep(this.activeCells);
      this.signalRowFirstStep();
      int minStep = this.considerRowLastStep(this.rowFirstStep);
      this.signalNextStep(minStep);
      return minStep;
   }

   private int getNextStep() {
      if (this.rowFinished) {
         if (this.activeRowIndex == this.rowGroup.length - 1) {
            return -1;
         }

         this.rowFinished = false;
         this.removeCellsEndingOnCurrentRow();
         log.trace("Delaying next row");
         this.delayingNextRow = true;
      }

      int minStep;
      if (!this.delayingNextRow) {
         minStep = this.computeMinStep();
         minStep = this.considerRowLastStep(minStep);
         this.signalNextStep(minStep);
         return minStep;
      } else {
         minStep = this.computeMinStep();
         if (minStep < 0 || minStep >= this.rowFirstStep || minStep > this.rowGroup[this.activeRowIndex].getExplicitHeight().getMax()) {
            if (log.isTraceEnabled()) {
               log.trace("Step = " + minStep);
            }

            this.delayingNextRow = false;
            minStep = this.rowFirstStep;
            this.switchToNextRow();
            this.signalRowFirstStep();
            minStep = this.considerRowLastStep(minStep);
         }

         this.signalNextStep(minStep);
         return minStep;
      }
   }

   private void computeRowFirstStep(List cells) {
      ActiveCell activeCell;
      for(Iterator iter = cells.iterator(); iter.hasNext(); this.rowFirstStep = Math.max(this.rowFirstStep, activeCell.getFirstStep())) {
         activeCell = (ActiveCell)iter.next();
      }

   }

   private int computeMinStep() {
      int minStep = Integer.MAX_VALUE;
      boolean stepFound = false;
      Iterator iter = this.activeCells.iterator();

      while(iter.hasNext()) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         int nextStep = activeCell.getNextStep();
         if (nextStep >= 0) {
            stepFound = true;
            minStep = Math.min(minStep, nextStep);
         }
      }

      if (stepFound) {
         return minStep;
      } else {
         return -1;
      }
   }

   private void signalRowFirstStep() {
      Iterator iter = this.activeCells.iterator();

      while(iter.hasNext()) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         activeCell.signalRowFirstStep(this.rowFirstStep);
      }

   }

   private void signalNextStep(int step) {
      this.nextBreakClass = 9;

      ActiveCell activeCell;
      for(Iterator iter = this.activeCells.iterator(); iter.hasNext(); this.nextBreakClass = BreakUtil.compareBreakClasses(this.nextBreakClass, activeCell.signalNextStep(step))) {
         activeCell = (ActiveCell)iter.next();
      }

   }

   private int considerRowLastStep(int step) {
      this.rowFinished = true;
      Iterator iter = this.activeCells.iterator();

      while(iter.hasNext()) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         if (activeCell.endsOnRow(this.activeRowIndex)) {
            this.rowFinished &= activeCell.finishes(step);
         }
      }

      if (this.rowFinished) {
         if (log.isTraceEnabled()) {
            log.trace("Step = " + step);
            log.trace("Row finished, computing last step");
         }

         int maxStep = 0;
         Iterator iter = this.activeCells.iterator();

         ActiveCell activeCell;
         while(iter.hasNext()) {
            activeCell = (ActiveCell)iter.next();
            if (activeCell.endsOnRow(this.activeRowIndex)) {
               maxStep = Math.max(maxStep, activeCell.getLastStep());
            }
         }

         if (log.isTraceEnabled()) {
            log.trace("Max step: " + maxStep);
         }

         iter = this.activeCells.iterator();

         while(iter.hasNext()) {
            activeCell = (ActiveCell)iter.next();
            activeCell.endRow(this.activeRowIndex);
            if (!activeCell.endsOnRow(this.activeRowIndex)) {
               activeCell.signalRowLastStep(maxStep);
            }
         }

         if (maxStep < step) {
            log.trace("Row height smaller than first step, produced penalty will be infinite");
            this.rowHeightSmallerThanFirstStep = true;
         }

         step = maxStep;
         this.prepareNextRow();
      }

      return step;
   }

   private void prepareNextRow() {
      if (this.activeRowIndex < this.rowGroup.length - 1) {
         this.previousRowsLength += this.rowGroup[this.activeRowIndex].getHeight().getOpt();
         this.activateCells(this.nextActiveCells, this.activeRowIndex + 1);
         if (log.isTraceEnabled()) {
            log.trace("Computing first step for row " + (this.activeRowIndex + 2));
         }

         this.computeRowFirstStep(this.nextActiveCells);
         if (log.isTraceEnabled()) {
            log.trace("Next first step = " + this.rowFirstStep);
         }
      }

   }

   private void removeCellsEndingOnCurrentRow() {
      Iterator iter = this.activeCells.iterator();

      while(iter.hasNext()) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         if (activeCell.endsOnRow(this.activeRowIndex)) {
            iter.remove();
         }
      }

   }

   private void switchToNextRow() {
      ++this.activeRowIndex;
      if (log.isTraceEnabled()) {
         log.trace("Switching to row " + (this.activeRowIndex + 1));
      }

      Iterator iter = this.activeCells.iterator();

      while(iter.hasNext()) {
         ActiveCell activeCell = (ActiveCell)iter.next();
         activeCell.nextRowStarts();
      }

      this.activeCells.addAll(this.nextActiveCells);
      this.nextActiveCells.clear();
   }

   private TableLayoutManager getTableLM() {
      return this.tclm.getTableLM();
   }

   static {
      $assertionsDisabled = !TableStepper.class.desiredAssertionStatus();
      log = LogFactory.getLog(TableStepper.class);
   }
}
