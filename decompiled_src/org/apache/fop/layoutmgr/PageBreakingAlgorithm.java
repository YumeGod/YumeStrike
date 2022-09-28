package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.FObj;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.ListUtil;

class PageBreakingAlgorithm extends BreakingAlgorithm {
   private static Log log;
   private LayoutManager topLevelLM;
   private PageProvider pageProvider;
   private PageBreakingLayoutListener layoutListener;
   private LinkedList pageBreaks = null;
   private List footnotesList = null;
   private List lengthList = null;
   private int totalFootnotesLength = 0;
   private int insertedFootnotesLength = 0;
   private boolean footnotesPending = false;
   private boolean newFootnotes = false;
   private int firstNewFootnoteIndex = 0;
   private int footnoteListIndex = 0;
   private int footnoteElementIndex = -1;
   private int splitFootnoteDemerits = 5000;
   private int deferredFootnoteDemerits = 10000;
   private MinOptMax footnoteSeparatorLength = null;
   private int storedPrevBreakIndex = -1;
   private int storedBreakIndex = -1;
   private boolean storedValue = false;
   private boolean autoHeight = false;
   private boolean favorSinglePart = false;
   private int ipdDifference;
   private BreakingAlgorithm.KnuthNode bestNodeForIPDChange;
   private int currentKeepContext = 9;
   private BreakingAlgorithm.KnuthNode lastBeforeKeepContextSwitch;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PageBreakingAlgorithm(LayoutManager topLevelLM, PageProvider pageProvider, PageBreakingLayoutListener layoutListener, int alignment, int alignmentLast, MinOptMax footnoteSeparatorLength, boolean partOverflowRecovery, boolean autoHeight, boolean favorSinglePart) {
      super(alignment, alignmentLast, true, partOverflowRecovery, 0);
      this.topLevelLM = topLevelLM;
      this.pageProvider = pageProvider;
      this.layoutListener = layoutListener;
      this.best = new BestPageRecords();
      this.footnoteSeparatorLength = footnoteSeparatorLength;
      this.autoHeight = autoHeight;
      this.favorSinglePart = favorSinglePart;
   }

   protected void initialize() {
      super.initialize();
      this.insertedFootnotesLength = 0;
      this.footnoteListIndex = 0;
      this.footnoteElementIndex = -1;
   }

   protected BreakingAlgorithm.KnuthNode recoverFromTooLong(BreakingAlgorithm.KnuthNode lastTooLong) {
      if (log.isDebugEnabled()) {
         log.debug("Recovering from too long: " + lastTooLong);
         log.debug("\tlastTooShort = " + this.getLastTooShort());
         log.debug("\tlastBeforeKeepContextSwitch = " + this.lastBeforeKeepContextSwitch);
         log.debug("\tcurrentKeepContext = " + AbstractBreaker.getBreakClassName(this.currentKeepContext));
      }

      if (this.lastBeforeKeepContextSwitch != null && this.currentKeepContext != 9) {
         BreakingAlgorithm.KnuthNode node = this.lastBeforeKeepContextSwitch;

         for(this.lastBeforeKeepContextSwitch = null; !this.pageProvider.endPage(node.line - 1); node = this.createNode(node.position, node.line + 1, 1, 0, 0, 0, 0.0, 0, 0, 0, 0.0, node)) {
            log.trace("Adding node for empty column");
         }

         return node;
      } else {
         return super.recoverFromTooLong(lastTooLong);
      }
   }

   protected BreakingAlgorithm.KnuthNode compareNodes(BreakingAlgorithm.KnuthNode node1, BreakingAlgorithm.KnuthNode node2) {
      if (node1 != null && node2 != null) {
         if (this.pageProvider != null) {
            if (this.pageProvider.endPage(node1.line - 1) && !this.pageProvider.endPage(node2.line - 1)) {
               return node1;
            }

            if (this.pageProvider.endPage(node2.line - 1) && !this.pageProvider.endPage(node1.line - 1)) {
               return node2;
            }
         }

         return super.compareNodes(node1, node2);
      } else {
         return node1 == null ? node2 : node1;
      }
   }

   protected BreakingAlgorithm.KnuthNode createNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink, double adjustRatio, int availableShrink, int availableStretch, int difference, double totalDemerits, BreakingAlgorithm.KnuthNode previous) {
      return new KnuthPageNode(position, line, fitness, totalWidth, totalStretch, totalShrink, this.insertedFootnotesLength, this.footnoteListIndex, this.footnoteElementIndex, adjustRatio, availableShrink, availableStretch, difference, totalDemerits, previous);
   }

   protected BreakingAlgorithm.KnuthNode createNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink) {
      return new KnuthPageNode(position, line, fitness, totalWidth, totalStretch, totalShrink, ((BestPageRecords)this.best).getFootnotesLength(fitness), ((BestPageRecords)this.best).getFootnoteListIndex(fitness), ((BestPageRecords)this.best).getFootnoteElementIndex(fitness), this.best.getAdjust(fitness), this.best.getAvailableShrink(fitness), this.best.getAvailableStretch(fitness), this.best.getDifference(fitness), this.best.getDemerits(fitness), this.best.getNode(fitness));
   }

   protected void handleBox(KnuthBox box) {
      super.handleBox(box);
      if (box instanceof KnuthBlockBox && ((KnuthBlockBox)box).hasAnchors()) {
         this.handleFootnotes(((KnuthBlockBox)box).getElementLists());
         if (!this.newFootnotes) {
            this.newFootnotes = true;
            this.firstNewFootnoteIndex = this.footnotesList.size() - 1;
         }
      }

   }

   protected void handlePenaltyAt(KnuthPenalty penalty, int position, int allowedBreaks) {
      super.handlePenaltyAt(penalty, position, allowedBreaks);
      if (penalty.getPenalty() == 1000) {
         int breakClass = penalty.getBreakClass();
         if (breakClass == 104 || breakClass == 28) {
            this.considerLegalBreak(penalty, position);
         }
      }

   }

   private void handleFootnotes(List elementLists) {
      if (!this.footnotesPending) {
         this.footnotesPending = true;
         this.footnotesList = new ArrayList();
         this.lengthList = new ArrayList();
         this.totalFootnotesLength = 0;
      }

      if (!this.newFootnotes) {
         this.newFootnotes = true;
         this.firstNewFootnoteIndex = this.footnotesList.size();
      }

      int noteLength;
      label45:
      for(Iterator elementListsIterator = elementLists.iterator(); elementListsIterator.hasNext(); this.totalFootnotesLength += noteLength) {
         List noteList = (List)elementListsIterator.next();
         SpaceResolver.resolveElementList(noteList);
         noteLength = 0;
         this.footnotesList.add(noteList);
         Iterator noteListIterator = noteList.iterator();

         while(true) {
            KnuthElement element;
            do {
               if (!noteListIterator.hasNext()) {
                  int prevLength = this.lengthList != null && !this.lengthList.isEmpty() ? (Integer)ListUtil.getLast(this.lengthList) : 0;
                  this.lengthList.add(new Integer(prevLength + noteLength));
                  continue label45;
               }

               element = (KnuthElement)noteListIterator.next();
            } while(!element.isBox() && !element.isGlue());

            noteLength += element.getWidth();
         }
      }

   }

   protected int restartFrom(BreakingAlgorithm.KnuthNode restartingNode, int currentIndex) {
      int returnValue = super.restartFrom(restartingNode, currentIndex);
      this.newFootnotes = false;
      if (this.footnotesPending) {
         for(int j = currentIndex; j >= restartingNode.position; --j) {
            KnuthElement resetElement = this.getElement(j);
            if (resetElement instanceof KnuthBlockBox && ((KnuthBlockBox)resetElement).hasAnchors()) {
               this.resetFootnotes(((KnuthBlockBox)resetElement).getElementLists());
            }
         }
      }

      return returnValue;
   }

   private void resetFootnotes(List elementLists) {
      for(int i = 0; i < elementLists.size(); ++i) {
         ListUtil.removeLast(this.footnotesList);
         ListUtil.removeLast(this.lengthList);
         if (!this.lengthList.isEmpty()) {
            this.totalFootnotesLength = (Integer)ListUtil.getLast(this.lengthList);
         } else {
            this.totalFootnotesLength = 0;
         }
      }

      if (this.footnotesList.size() == 0) {
         this.footnotesPending = false;
      }

   }

   protected void considerLegalBreak(KnuthElement element, int elementIdx) {
      if (element.isPenalty()) {
         int breakClass = ((KnuthPenalty)element).getBreakClass();
         switch (breakClass) {
            case 9:
               this.currentKeepContext = breakClass;
               break;
            case 28:
               if (this.currentKeepContext != breakClass) {
                  this.lastBeforeKeepContextSwitch = this.getLastTooShort();
               }

               this.currentKeepContext = breakClass;
               break;
            case 104:
               if (this.currentKeepContext != breakClass) {
                  this.lastBeforeKeepContextSwitch = this.getLastTooShort();
               }

               this.currentKeepContext = breakClass;
         }
      }

      super.considerLegalBreak(element, elementIdx);
      this.newFootnotes = false;
   }

   protected boolean elementCanEndLine(KnuthElement element, int line, int difference) {
      if (element.isPenalty() && this.pageProvider != null) {
         KnuthPenalty p = (KnuthPenalty)element;
         if (p.getPenalty() <= 0) {
            return true;
         } else {
            int context = p.getBreakClass();
            switch (context) {
               case 9:
                  log.debug("keep is not auto but context is");
                  return true;
               case 28:
               case 75:
                  return p.getPenalty() < 1000;
               case 104:
                  return p.getPenalty() < 1000 || !this.pageProvider.endPage(line - 1);
               default:
                  if (p.getPenalty() < 1000) {
                     log.debug("Non recognized keep context:" + context);
                     return true;
                  } else {
                     return false;
                  }
            }
         }
      } else {
         return true;
      }
   }

   protected int computeDifference(BreakingAlgorithm.KnuthNode activeNode, KnuthElement element, int elementIndex) {
      KnuthPageNode pageNode = (KnuthPageNode)activeNode;
      int actualWidth = this.totalWidth - pageNode.totalWidth;
      int footnoteSplit = false;
      if (element.isPenalty()) {
         actualWidth += element.getWidth();
      }

      int allFootnotes;
      if (this.footnotesPending) {
         allFootnotes = this.totalFootnotesLength - pageNode.totalFootnotes;
         if (allFootnotes > 0) {
            actualWidth += this.footnoteSeparatorLength.getOpt();
            if (actualWidth + allFootnotes <= this.getLineWidth(activeNode.line)) {
               actualWidth += allFootnotes;
               this.insertedFootnotesLength = pageNode.totalFootnotes + allFootnotes;
               this.footnoteListIndex = this.footnotesList.size() - 1;
               this.footnoteElementIndex = this.getFootnoteList(this.footnoteListIndex).size() - 1;
            } else {
               boolean canDeferOldFootnotes;
               int footnoteSplit;
               if (((canDeferOldFootnotes = this.checkCanDeferOldFootnotes(pageNode, elementIndex)) || this.newFootnotes) && (footnoteSplit = this.getFootnoteSplit(pageNode, this.getLineWidth(activeNode.line) - actualWidth, canDeferOldFootnotes)) > 0) {
                  actualWidth += footnoteSplit;
                  this.insertedFootnotesLength = pageNode.totalFootnotes + footnoteSplit;
               } else {
                  actualWidth += allFootnotes;
                  this.insertedFootnotesLength = pageNode.totalFootnotes + allFootnotes;
                  this.footnoteListIndex = this.footnotesList.size() - 1;
                  this.footnoteElementIndex = this.getFootnoteList(this.footnoteListIndex).size() - 1;
               }
            }
         }
      }

      allFootnotes = this.getLineWidth(activeNode.line) - actualWidth;
      return this.autoHeight && allFootnotes < 0 ? 0 : allFootnotes;
   }

   private boolean checkCanDeferOldFootnotes(KnuthPageNode node, int contentElementIndex) {
      return this.noBreakBetween(node.position, contentElementIndex) && this.deferredFootnotes(node.footnoteListIndex, node.footnoteElementIndex, node.totalFootnotes);
   }

   private boolean noBreakBetween(int prevBreakIndex, int breakIndex) {
      if (this.storedPrevBreakIndex == -1 || (prevBreakIndex < this.storedPrevBreakIndex || breakIndex != this.storedBreakIndex || !this.storedValue) && (prevBreakIndex > this.storedPrevBreakIndex || breakIndex < this.storedBreakIndex || this.storedValue)) {
         int index;
         for(index = prevBreakIndex + 1; !this.par.getElement(index).isBox(); ++index) {
         }

         while(index < breakIndex && (!this.par.getElement(index).isGlue() || !this.par.getElement(index - 1).isBox()) && (!this.par.getElement(index).isPenalty() || ((KnuthElement)this.par.getElement(index)).getPenalty() >= 1000)) {
            ++index;
         }

         this.storedPrevBreakIndex = prevBreakIndex;
         this.storedBreakIndex = breakIndex;
         this.storedValue = index == breakIndex;
      }

      return this.storedValue;
   }

   private boolean deferredFootnotes(int listIndex, int elementIndex, int length) {
      return this.newFootnotes && this.firstNewFootnoteIndex != 0 && (listIndex < this.firstNewFootnoteIndex - 1 || elementIndex < this.getFootnoteList(listIndex).size() - 1) || length < this.totalFootnotesLength;
   }

   private int getFootnoteSplit(KnuthPageNode activeNode, int availableLength, boolean canDeferOldFootnotes) {
      return this.getFootnoteSplit(activeNode.footnoteListIndex, activeNode.footnoteElementIndex, activeNode.totalFootnotes, availableLength, canDeferOldFootnotes);
   }

   private int getFootnoteSplit(int prevListIndex, int prevElementIndex, int prevLength, int availableLength, boolean canDeferOldFootnotes) {
      if (availableLength <= 0) {
         return 0;
      } else {
         int splitLength = 0;
         ListIterator noteListIterator = null;
         KnuthElement element = null;
         boolean somethingAdded = false;
         int listIndex = prevListIndex;
         int elementIndex;
         if (prevElementIndex == this.getFootnoteList(prevListIndex).size() - 1) {
            listIndex = prevListIndex + 1;
            elementIndex = 0;
         } else {
            elementIndex = prevElementIndex + 1;
         }

         if (this.footnotesList.size() - 1 > listIndex) {
            if (!canDeferOldFootnotes && this.newFootnotes && this.firstNewFootnoteIndex > 0) {
               splitLength = (Integer)this.lengthList.get(this.firstNewFootnoteIndex - 1) - prevLength;
               listIndex = this.firstNewFootnoteIndex;
               elementIndex = 0;
            }

            while((Integer)this.lengthList.get(listIndex) - prevLength <= availableLength) {
               splitLength = (Integer)this.lengthList.get(listIndex) - prevLength;
               somethingAdded = true;
               ++listIndex;
               elementIndex = 0;
            }
         }

         noteListIterator = this.getFootnoteList(listIndex).listIterator(elementIndex);
         int prevSplitLength = 0;
         int prevIndex = -1;
         int index = -1;

         while(true) {
            while(!somethingAdded || splitLength <= availableLength) {
               if (!somethingAdded) {
                  somethingAdded = true;
               } else {
                  prevSplitLength = splitLength;
                  prevIndex = index;
               }

               boolean boxPreceding = false;

               while(noteListIterator.hasNext()) {
                  element = (KnuthElement)noteListIterator.next();
                  if (element.isBox()) {
                     splitLength += element.getWidth();
                     boxPreceding = true;
                  } else if (element.isGlue()) {
                     if (boxPreceding) {
                        index = noteListIterator.previousIndex();
                        break;
                     }

                     boxPreceding = false;
                     splitLength += element.getWidth();
                  } else if (element.getPenalty() < 1000) {
                     index = noteListIterator.previousIndex();
                     break;
                  }
               }
            }

            if (!somethingAdded) {
               prevSplitLength = 0;
            } else if (prevSplitLength > 0) {
               this.footnoteListIndex = prevIndex != -1 ? listIndex : listIndex - 1;
               this.footnoteElementIndex = prevIndex != -1 ? prevIndex : this.getFootnoteList(this.footnoteListIndex).size() - 1;
            }

            return prevSplitLength;
         }
      }
   }

   protected double computeAdjustmentRatio(BreakingAlgorithm.KnuthNode activeNode, int difference) {
      int maxAdjustment;
      if (difference > 0) {
         maxAdjustment = this.totalStretch - activeNode.totalStretch;
         if (((KnuthPageNode)activeNode).totalFootnotes < this.totalFootnotesLength) {
            maxAdjustment += this.footnoteSeparatorLength.getStretch();
         }

         return maxAdjustment > 0 ? (double)difference / (double)maxAdjustment : 1000.0;
      } else if (difference < 0) {
         maxAdjustment = this.totalShrink - activeNode.totalShrink;
         if (((KnuthPageNode)activeNode).totalFootnotes < this.totalFootnotesLength) {
            maxAdjustment += this.footnoteSeparatorLength.getShrink();
         }

         return maxAdjustment > 0 ? (double)difference / (double)maxAdjustment : -1000.0;
      } else {
         return 0.0;
      }
   }

   protected double computeDemerits(BreakingAlgorithm.KnuthNode activeNode, KnuthElement element, int fitnessClass, double r) {
      double demerits = 0.0;
      double f = Math.abs(r);
      f = 1.0 + 100.0 * f * f * f;
      if (element.isPenalty()) {
         double penalty = (double)element.getPenalty();
         if (penalty >= 0.0) {
            f += penalty;
            demerits = f * f;
         } else if (!element.isForcedBreak()) {
            demerits = f * f - penalty * penalty;
         } else {
            demerits = f * f;
         }
      } else {
         demerits = f * f;
      }

      if (element.isPenalty() && ((KnuthPenalty)element).isPenaltyFlagged() && this.getElement(activeNode.position).isPenalty() && ((KnuthPenalty)this.getElement(activeNode.position)).isPenaltyFlagged()) {
         demerits += (double)this.repeatedFlaggedDemerit;
      }

      if (Math.abs(fitnessClass - activeNode.fitness) > 1) {
         demerits += (double)this.incompatibleFitnessDemerit;
      }

      if (this.footnotesPending) {
         if (this.footnoteListIndex < this.footnotesList.size() - 1) {
            demerits += (double)((this.footnotesList.size() - 1 - this.footnoteListIndex) * this.deferredFootnoteDemerits);
         }

         if (this.footnoteListIndex < this.footnotesList.size() && this.footnoteElementIndex < this.getFootnoteList(this.footnoteListIndex).size() - 1) {
            demerits += (double)this.splitFootnoteDemerits;
         }
      }

      demerits += activeNode.totalDemerits;
      return demerits;
   }

   protected void finish() {
      for(int i = this.startLine; i < this.endLine; ++i) {
         for(KnuthPageNode node = (KnuthPageNode)this.getNode(i); node != null; node = (KnuthPageNode)node.next) {
            if (node.totalFootnotes < this.totalFootnotesLength) {
               this.createFootnotePages(node);
            }
         }
      }

   }

   private void createFootnotePages(KnuthPageNode lastNode) {
      this.insertedFootnotesLength = lastNode.totalFootnotes;
      this.footnoteListIndex = lastNode.footnoteListIndex;
      this.footnoteElementIndex = lastNode.footnoteElementIndex;
      int availableBPD = this.getLineWidth(lastNode.line);
      int split = false;
      KnuthPageNode prevNode = lastNode;

      while(this.insertedFootnotesLength < this.totalFootnotesLength) {
         int tmpLength = (Integer)this.lengthList.get(this.footnoteListIndex);
         if (tmpLength - this.insertedFootnotesLength <= availableBPD) {
            availableBPD -= tmpLength - this.insertedFootnotesLength;
            this.insertedFootnotesLength = tmpLength;
            this.footnoteElementIndex = this.getFootnoteList(this.footnoteListIndex).size() - 1;
         } else {
            int split;
            if ((split = this.getFootnoteSplit(this.footnoteListIndex, this.footnoteElementIndex, this.insertedFootnotesLength, availableBPD, true)) > 0) {
               availableBPD -= split;
               this.insertedFootnotesLength += split;
            } else {
               KnuthPageNode node = (KnuthPageNode)this.createNode(lastNode.position, prevNode.line + 1, 1, this.insertedFootnotesLength - prevNode.totalFootnotes, 0, 0, 0.0, 0, 0, 0, 0.0, prevNode);
               this.addNode(node.line, node);
               this.removeNode(prevNode.line, prevNode);
               prevNode = node;
               availableBPD = this.getLineWidth(node.line);
            }
         }
      }

      KnuthPageNode node = (KnuthPageNode)this.createNode(lastNode.position, prevNode.line + 1, 1, this.totalFootnotesLength - prevNode.totalFootnotes, 0, 0, 0.0, 0, 0, 0, 0.0, prevNode);
      this.addNode(node.line, node);
      this.removeNode(prevNode.line, prevNode);
   }

   public LinkedList getPageBreaks() {
      return this.pageBreaks;
   }

   public void insertPageBreakAsFirst(AbstractBreaker.PageBreakPosition pageBreak) {
      if (this.pageBreaks == null) {
         this.pageBreaks = new LinkedList();
      }

      this.pageBreaks.addFirst(pageBreak);
   }

   public void removeAllPageBreaks() {
      if (this.pageBreaks != null) {
         while(this.pageBreaks.size() > 1) {
            this.pageBreaks.removeFirst();
         }

      }
   }

   public void updateData1(int total, double demerits) {
   }

   public void updateData2(BreakingAlgorithm.KnuthNode bestActiveNode, KnuthSequence sequence, int total) {
      int difference = bestActiveNode.difference;
      if (difference + bestActiveNode.availableShrink < 0 && !this.autoHeight && this.layoutListener != null) {
         this.layoutListener.notifyOverflow(bestActiveNode.line - 1, -difference, this.getFObj());
      }

      boolean isNonLastPage = bestActiveNode.line < total;
      int blockAlignment = isNonLastPage ? this.alignment : this.alignmentLast;
      double ratio = bestActiveNode.adjustRatio;
      if (ratio < 0.0) {
         difference = 0;
      } else if (ratio <= 1.0 && isNonLastPage) {
         difference = 0;
      } else if (ratio > 1.0) {
         ratio = 1.0;
         difference -= bestActiveNode.availableStretch;
      } else if (blockAlignment != 70) {
         ratio = 0.0;
      } else {
         difference = 0;
      }

      int firstListIndex = ((KnuthPageNode)bestActiveNode.previous).footnoteListIndex;
      int firstElementIndex = ((KnuthPageNode)bestActiveNode.previous).footnoteElementIndex;
      if (this.footnotesList != null && firstElementIndex == this.getFootnoteList(firstListIndex).size() - 1) {
         ++firstListIndex;
         firstElementIndex = 0;
      } else {
         ++firstElementIndex;
      }

      if (log.isDebugEnabled()) {
         log.debug("BBA> difference=" + difference + " ratio=" + ratio + " position=" + bestActiveNode.position);
      }

      this.insertPageBreakAsFirst(new AbstractBreaker.PageBreakPosition(this.topLevelLM, bestActiveNode.position, firstListIndex, firstElementIndex, ((KnuthPageNode)bestActiveNode).footnoteListIndex, ((KnuthPageNode)bestActiveNode).footnoteElementIndex, ratio, difference));
   }

   protected int filterActiveNodes() {
      BreakingAlgorithm.KnuthNode bestActiveNode = null;

      for(int i = this.startLine; i < this.endLine; ++i) {
         for(BreakingAlgorithm.KnuthNode node = this.getNode(i); node != null; node = node.next) {
            if (!this.favorSinglePart || node.line <= 1 || bestActiveNode == null || Math.abs(bestActiveNode.difference) >= bestActiveNode.availableShrink) {
               bestActiveNode = this.compareNodes(bestActiveNode, node);
            }

            if (node != bestActiveNode) {
               this.removeNode(i, node);
            }
         }
      }

      if (!$assertionsDisabled && bestActiveNode == null) {
         throw new AssertionError();
      } else {
         return bestActiveNode.line;
      }
   }

   protected final List getFootnoteList(int index) {
      return (List)this.footnotesList.get(index);
   }

   public FObj getFObj() {
      return this.topLevelLM.getFObj();
   }

   protected int getLineWidth(int line) {
      int bpd;
      if (this.pageProvider != null) {
         bpd = this.pageProvider.getAvailableBPD(line);
      } else {
         bpd = super.getLineWidth(line);
      }

      if (log.isTraceEnabled()) {
         log.trace("getLineWidth(" + line + ") -> " + bpd);
      }

      return bpd;
   }

   protected int getIPDdifference() {
      return this.ipdDifference;
   }

   protected int handleIpdChange() {
      log.trace("Best node for ipd change:" + this.bestNodeForIPDChange);
      this.calculateBreakPoints(this.bestNodeForIPDChange, this.par, this.bestNodeForIPDChange.line + 1);
      this.activeLines = null;
      return this.bestNodeForIPDChange.line;
   }

   protected void addNode(int line, BreakingAlgorithm.KnuthNode node) {
      if (node.position < this.par.size() - 1 && line > 0 && (this.ipdDifference = this.compareIPDs(line - 1)) != 0) {
         log.trace("IPD changes at page " + line);
         if (this.bestNodeForIPDChange == null || node.totalDemerits < this.bestNodeForIPDChange.totalDemerits) {
            this.bestNodeForIPDChange = node;
         }
      } else {
         if (node.position == this.par.size() - 1) {
            this.ipdDifference = 0;
         }

         super.addNode(line, node);
      }

   }

   BreakingAlgorithm.KnuthNode getBestNodeBeforeIPDChange() {
      return this.bestNodeForIPDChange;
   }

   private int compareIPDs(int line) {
      return this.pageProvider == null ? 0 : this.pageProvider.compareIPDs(line);
   }

   static {
      $assertionsDisabled = !PageBreakingAlgorithm.class.desiredAssertionStatus();
      log = LogFactory.getLog(PageBreakingAlgorithm.class);
   }

   public interface PageBreakingLayoutListener {
      void notifyOverflow(int var1, int var2, FObj var3);
   }

   protected class BestPageRecords extends BreakingAlgorithm.BestRecords {
      private int[] bestFootnotesLength = new int[4];
      private int[] bestFootnoteListIndex = new int[4];
      private int[] bestFootnoteElementIndex = new int[4];

      protected BestPageRecords() {
         super();
      }

      public void addRecord(double demerits, BreakingAlgorithm.KnuthNode node, double adjust, int availableShrink, int availableStretch, int difference, int fitness) {
         super.addRecord(demerits, node, adjust, availableShrink, availableStretch, difference, fitness);
         this.bestFootnotesLength[fitness] = PageBreakingAlgorithm.this.insertedFootnotesLength;
         this.bestFootnoteListIndex[fitness] = PageBreakingAlgorithm.this.footnoteListIndex;
         this.bestFootnoteElementIndex[fitness] = PageBreakingAlgorithm.this.footnoteElementIndex;
      }

      public int getFootnotesLength(int fitness) {
         return this.bestFootnotesLength[fitness];
      }

      public int getFootnoteListIndex(int fitness) {
         return this.bestFootnoteListIndex[fitness];
      }

      public int getFootnoteElementIndex(int fitness) {
         return this.bestFootnoteElementIndex[fitness];
      }
   }

   protected class KnuthPageNode extends BreakingAlgorithm.KnuthNode {
      public int totalFootnotes;
      public int footnoteListIndex;
      public int footnoteElementIndex;

      public KnuthPageNode(int position, int line, int fitness, int totalWidth, int totalStretch, int totalShrink, int totalFootnotes, int footnoteListIndex, int footnoteElementIndex, double adjustRatio, int availableShrink, int availableStretch, int difference, double totalDemerits, BreakingAlgorithm.KnuthNode previous) {
         super(position, line, fitness, totalWidth, totalStretch, totalShrink, adjustRatio, availableShrink, availableStretch, difference, totalDemerits, previous);
         this.totalFootnotes = totalFootnotes;
         this.footnoteListIndex = footnoteListIndex;
         this.footnoteElementIndex = footnoteElementIndex;
      }
   }
}
