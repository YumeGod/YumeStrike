package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.ListUtil;

public abstract class AbstractBreaker {
   protected static Log log;
   private int blockListIndex = 0;
   private List blockLists = null;
   protected int alignment;
   private int alignmentLast;
   protected MinOptMax footnoteSeparatorLength;

   public AbstractBreaker() {
      this.footnoteSeparatorLength = MinOptMax.ZERO;
   }

   static String getBreakClassName(int breakClassId) {
      switch (breakClassId) {
         case 5:
            return "ALL";
         case 8:
            return "ANY";
         case 9:
            return "AUTO";
         case 28:
            return "COLUMN";
         case 44:
            return "EVEN PAGE";
         case 75:
            return "LINE";
         case 95:
            return "NONE";
         case 100:
            return "ODD PAGE";
         case 104:
            return "PAGE";
         default:
            return "??? (" + String.valueOf(breakClassId) + ")";
      }
   }

   protected abstract int getCurrentDisplayAlign();

   protected abstract boolean hasMoreContent();

   protected abstract void addAreas(PositionIterator var1, LayoutContext var2);

   protected abstract LayoutManager getTopLevelLM();

   protected abstract LayoutManager getCurrentChildLM();

   protected boolean isPartOverflowRecoveryActivated() {
      return true;
   }

   protected boolean isSinglePartFavored() {
      return false;
   }

   protected PageProvider getPageProvider() {
      return null;
   }

   protected PageBreakingAlgorithm.PageBreakingLayoutListener createLayoutListener() {
      return null;
   }

   protected abstract List getNextKnuthElements(LayoutContext var1, int var2);

   protected List getNextKnuthElements(LayoutContext context, int alignment, Position positionAtIPDChange, LayoutManager restartAtLM) {
      throw new UnsupportedOperationException("TODO: implement acceptable fallback");
   }

   public boolean isEmpty() {
      return this.blockLists.isEmpty();
   }

   protected void startPart(BlockSequence list, int breakClass) {
   }

   protected void handleEmptyContent() {
   }

   protected abstract void finishPart(PageBreakingAlgorithm var1, PageBreakPosition var2);

   protected LayoutContext createLayoutContext() {
      return new LayoutContext(0);
   }

   protected void updateLayoutContext(LayoutContext context) {
   }

   protected void observeElementList(List elementList) {
      ElementListObserver.observe(elementList, "breaker", (String)null);
   }

   public void doLayout(int flowBPD, boolean autoHeight) {
      LayoutContext childLC = this.createLayoutContext();
      childLC.setStackLimitBP(MinOptMax.getInstance(flowBPD));
      if (this.getCurrentDisplayAlign() == 162) {
         this.alignment = 70;
      } else if (this.getCurrentDisplayAlign() == 163) {
         this.alignment = 70;
      } else {
         this.alignment = 135;
      }

      this.alignmentLast = 135;
      if (this.isSinglePartFavored() && this.alignment == 70) {
         this.alignmentLast = 70;
      }

      childLC.setBPAlignment(this.alignment);
      this.blockLists = new ArrayList();
      log.debug("PLM> flow BPD =" + flowBPD);
      int nextSequenceStartsOn = 8;

      while(this.hasMoreContent()) {
         this.blockLists.clear();
         nextSequenceStartsOn = this.getNextBlockList(childLC, nextSequenceStartsOn);
         log.debug("PLM> blockLists.size() = " + this.blockLists.size());

         for(this.blockListIndex = 0; this.blockListIndex < this.blockLists.size(); ++this.blockListIndex) {
            BlockSequence blockList = (BlockSequence)this.blockLists.get(this.blockListIndex);
            if (log.isDebugEnabled()) {
               log.debug("  blockListIndex = " + this.blockListIndex);
               log.debug("  sequence starts on " + getBreakClassName(blockList.startOn));
            }

            this.observeElementList(blockList);
            log.debug("PLM> start of algorithm (" + this.getClass().getName() + "), flow BPD =" + flowBPD);
            PageBreakingAlgorithm alg = new PageBreakingAlgorithm(this.getTopLevelLM(), this.getPageProvider(), this.createLayoutListener(), this.alignment, this.alignmentLast, this.footnoteSeparatorLength, this.isPartOverflowRecoveryActivated(), autoHeight, this.isSinglePartFavored());
            BlockSequence effectiveList;
            if (this.getCurrentDisplayAlign() == 162) {
               effectiveList = this.justifyBoxes(blockList, alg, flowBPD);
            } else {
               effectiveList = blockList;
            }

            alg.setConstantLineWidth(flowBPD);
            int optimalPageCount = alg.findBreakingPoints(effectiveList, 1.0, true, 0);
            if (alg.getIPDdifference() != 0) {
               BreakingAlgorithm.KnuthNode optimalBreak = alg.getBestNodeBeforeIPDChange();
               int positionIndex = optimalBreak.position;
               KnuthElement elementAtBreak = alg.getElement(positionIndex);
               Position positionAtBreak = elementAtBreak.getPosition();
               if (!(positionAtBreak instanceof SpaceResolver.SpaceHandlingBreakPosition)) {
                  throw new UnsupportedOperationException("Don't know how to restart at position" + positionAtBreak);
               }

               positionAtBreak = positionAtBreak.getPosition();
               LayoutManager restartAtLM = null;
               List firstElements = Collections.EMPTY_LIST;
               ListIterator iter;
               if (this.containsNonRestartableLM(positionAtBreak)) {
                  if (alg.getIPDdifference() > 0) {
                     EventBroadcaster eventBroadcaster = this.getCurrentChildLM().getFObj().getUserAgent().getEventBroadcaster();
                     BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(eventBroadcaster);
                     eventProducer.nonRestartableContentFlowingToNarrowerPage(this);
                  }

                  firstElements = new LinkedList();
                  boolean boxFound = false;
                  iter = effectiveList.listIterator(positionIndex + 1);
                  Position position = null;

                  while(iter.hasNext() && (position == null || this.containsNonRestartableLM(position))) {
                     ++positionIndex;
                     KnuthElement element = (KnuthElement)iter.next();
                     position = element.getPosition();
                     if (element.isBox()) {
                        boxFound = true;
                        ((List)firstElements).add(element);
                     } else if (boxFound) {
                        ((List)firstElements).add(element);
                     }
                  }

                  if (position instanceof SpaceResolver.SpaceHandlingBreakPosition) {
                     positionAtBreak = position.getPosition();
                  } else {
                     positionAtBreak = null;
                  }
               }

               if (positionAtBreak != null && positionAtBreak.getIndex() == -1) {
                  iter = effectiveList.listIterator(positionIndex + 1);

                  Position position;
                  do {
                     do {
                        KnuthElement nextElement = (KnuthElement)iter.next();
                        position = nextElement.getPosition();
                     } while(position == null);
                  } while(position instanceof SpaceResolver.SpaceHandlingPosition || position instanceof SpaceResolver.SpaceHandlingBreakPosition && position.getPosition().getIndex() == -1);

                  for(LayoutManager surroundingLM = positionAtBreak.getLM(); position.getLM() != surroundingLM; position = position.getPosition()) {
                  }

                  restartAtLM = position.getPosition().getLM();
               }

               log.trace("IPD changes after page " + optimalPageCount + " at index " + optimalBreak.position);
               this.addAreas(alg, optimalPageCount, blockList, effectiveList);
               this.blockLists.clear();
               this.blockListIndex = -1;
               nextSequenceStartsOn = this.getNextBlockList(childLC, 28, positionAtBreak, restartAtLM, (List)firstElements);
            } else {
               log.debug("PLM> iOptPageCount= " + optimalPageCount + " pageBreaks.size()= " + alg.getPageBreaks().size());
               this.doPhase3(alg, optimalPageCount, blockList, effectiveList);
            }
         }
      }

   }

   private boolean containsNonRestartableLM(Position position) {
      LayoutManager lm = position.getLM();
      if (lm != null && !lm.isRestartable()) {
         return true;
      } else {
         Position subPosition = position.getPosition();
         return subPosition == null ? false : this.containsNonRestartableLM(subPosition);
      }
   }

   protected abstract void doPhase3(PageBreakingAlgorithm var1, int var2, BlockSequence var3, BlockSequence var4);

   protected void addAreas(PageBreakingAlgorithm alg, int partCount, BlockSequence originalList, BlockSequence effectiveList) {
      this.addAreas(alg, 0, partCount, originalList, effectiveList);
   }

   protected void addAreas(PageBreakingAlgorithm alg, int startPart, int partCount, BlockSequence originalList, BlockSequence effectiveList) {
      ListIterator effectiveListIterator = effectiveList.listIterator();
      int startElementIndex = 0;
      int endElementIndex = 0;
      int lastBreak = -1;

      for(int p = startPart; p < startPart + partCount; ++p) {
         PageBreakPosition pbp = (PageBreakPosition)alg.getPageBreaks().get(p);
         int lastBreakClass;
         if (p == 0) {
            lastBreakClass = effectiveList.getStartOn();
         } else {
            ListElement lastBreakElement = effectiveList.getElement(endElementIndex);
            if (lastBreakElement.isPenalty()) {
               KnuthPenalty pen = (KnuthPenalty)lastBreakElement;
               lastBreakClass = pen.getBreakClass();
            } else {
               lastBreakClass = 28;
            }
         }

         endElementIndex = pbp.getLeafPos();
         startElementIndex += startElementIndex == 0 ? effectiveList.ignoreAtStart : 0;
         log.debug("PLM> part: " + (p + 1) + ", start at pos " + startElementIndex + ", break at pos " + endElementIndex + ", break class = " + getBreakClassName(lastBreakClass));
         this.startPart(effectiveList, lastBreakClass);
         int displayAlign = this.getCurrentDisplayAlign();
         int notificationEndElementIndex = endElementIndex;
         endElementIndex -= endElementIndex == originalList.size() - 1 ? effectiveList.ignoreAtEnd : 0;
         if (((KnuthElement)effectiveList.get(endElementIndex)).isGlue()) {
            --endElementIndex;
         }

         for(effectiveListIterator = effectiveList.listIterator(startElementIndex); effectiveListIterator.hasNext() && !((KnuthElement)effectiveListIterator.next()).isBox(); ++startElementIndex) {
         }

         if (startElementIndex <= endElementIndex) {
            if (log.isDebugEnabled()) {
               log.debug("     addAreas from " + startElementIndex + " to " + endElementIndex);
            }

            LayoutContext childLC = new LayoutContext(0);
            childLC.setSpaceAdjust(pbp.bpdAdjust);
            int boxCount;
            if (pbp.difference != 0 && displayAlign == 23) {
               childLC.setSpaceBefore(pbp.difference / 2);
            } else if (pbp.difference != 0 && displayAlign == 3) {
               childLC.setSpaceBefore(pbp.difference);
            } else if (pbp.difference != 0 && displayAlign == 163 && p < partCount - 1) {
               boxCount = 0;
               effectiveListIterator = effectiveList.listIterator(startElementIndex);

               while(effectiveListIterator.nextIndex() <= endElementIndex) {
                  KnuthElement tempEl = (KnuthElement)effectiveListIterator.next();
                  if (tempEl.isBox() && tempEl.getWidth() > 0) {
                     ++boxCount;
                  }
               }

               if (boxCount >= 2) {
                  childLC.setSpaceAfter(pbp.difference / (boxCount - 1));
               }
            }

            if (displayAlign == 162) {
               boxCount = this.optimizeLineLength(effectiveList, startElementIndex, endElementIndex);
               if (boxCount != 0) {
                  childLC.setStackLimitBP(MinOptMax.getInstance(boxCount));
               }
            }

            SpaceResolver.performConditionalsNotification(effectiveList, startElementIndex, notificationEndElementIndex, lastBreak);
            this.addAreas(new KnuthPossPosIter(effectiveList, startElementIndex, endElementIndex + 1), childLC);
         } else {
            this.handleEmptyContent();
         }

         this.finishPart(alg, pbp);
         lastBreak = endElementIndex;
         startElementIndex = pbp.getLeafPos() + 1;
      }

   }

   protected int handleSpanChange(LayoutContext childLC, int nextSequenceStartsOn) {
      return nextSequenceStartsOn;
   }

   protected int getNextBlockList(LayoutContext childLC, int nextSequenceStartsOn) {
      return this.getNextBlockList(childLC, nextSequenceStartsOn, (Position)null, (LayoutManager)null, (List)null);
   }

   protected int getNextBlockList(LayoutContext childLC, int nextSequenceStartsOn, Position positionAtIPDChange, LayoutManager restartAtLM, List firstElements) {
      this.updateLayoutContext(childLC);
      childLC.signalSpanChange(0);
      List returnedList;
      if (firstElements == null) {
         returnedList = this.getNextKnuthElements(childLC, this.alignment);
      } else if (positionAtIPDChange == null) {
         returnedList = firstElements;
         ListIterator iter = firstElements.listIterator(firstElements.size());

         for(int i = 0; i < 3; ++i) {
            iter.previous();
            iter.remove();
         }
      } else {
         returnedList = this.getNextKnuthElements(childLC, this.alignment, positionAtIPDChange, restartAtLM);
         returnedList.addAll(0, firstElements);
      }

      if (returnedList != null) {
         if (returnedList.isEmpty()) {
            nextSequenceStartsOn = this.handleSpanChange(childLC, nextSequenceStartsOn);
            return nextSequenceStartsOn;
         }

         BlockSequence blockList = new BlockSequence(nextSequenceStartsOn, this.getCurrentDisplayAlign());
         nextSequenceStartsOn = this.handleSpanChange(childLC, nextSequenceStartsOn);
         Position breakPosition = null;
         if (ElementListUtils.endsWithForcedBreak(returnedList)) {
            KnuthPenalty breakPenalty = (KnuthPenalty)ListUtil.removeLast(returnedList);
            breakPosition = breakPenalty.getPosition();
            log.debug("PLM> break - " + getBreakClassName(breakPenalty.getBreakClass()));
            switch (breakPenalty.getBreakClass()) {
               case 28:
                  nextSequenceStartsOn = 28;
                  break;
               case 44:
                  nextSequenceStartsOn = 44;
                  break;
               case 100:
                  nextSequenceStartsOn = 100;
                  break;
               case 104:
                  nextSequenceStartsOn = 8;
                  break;
               default:
                  throw new IllegalStateException("Invalid break class: " + breakPenalty.getBreakClass());
            }
         }

         blockList.addAll(returnedList);
         BlockSequence seq = blockList.endBlockSequence(breakPosition);
         if (seq != null) {
            this.blockLists.add(seq);
         }
      }

      return nextSequenceStartsOn;
   }

   private int optimizeLineLength(KnuthSequence effectiveList, int startElementIndex, int endElementIndex) {
      int boxCount = 0;
      int accumulatedLineLength = 0;
      int greatestMinimumLength = 0;
      ListIterator effectiveListIterator = effectiveList.listIterator(startElementIndex);

      while(effectiveListIterator.nextIndex() <= endElementIndex) {
         KnuthElement tempEl = (KnuthElement)effectiveListIterator.next();
         if (tempEl instanceof KnuthBlockBox) {
            KnuthBlockBox blockBox = (KnuthBlockBox)tempEl;
            if (blockBox.getBPD() > 0) {
               log.debug("PSLM> nominal length of line = " + blockBox.getBPD());
               log.debug("      range = " + blockBox.getIPDRange());
               ++boxCount;
               accumulatedLineLength += ((KnuthBlockBox)tempEl).getBPD();
            }

            if (blockBox.getIPDRange().getMin() > greatestMinimumLength) {
               greatestMinimumLength = blockBox.getIPDRange().getMin();
            }
         }
      }

      int averageLineLength = 0;
      if (accumulatedLineLength > 0 && boxCount > 0) {
         averageLineLength = accumulatedLineLength / boxCount;
         log.debug("Average line length = " + averageLineLength);
         if (averageLineLength < greatestMinimumLength) {
            averageLineLength = greatestMinimumLength;
            log.debug("  Correction to: " + greatestMinimumLength);
         }
      }

      return averageLineLength;
   }

   private BlockSequence justifyBoxes(BlockSequence blockList, PageBreakingAlgorithm alg, int availableBPD) {
      alg.setConstantLineWidth(availableBPD);
      int iOptPageNumber = alg.findBreakingPoints(blockList, 1.0, true, 0);
      log.debug("PLM> iOptPageNumber= " + iOptPageNumber);
      ListIterator sequenceIterator = blockList.listIterator();
      ListIterator breakIterator = alg.getPageBreaks().listIterator();
      KnuthElement thisElement = null;

      while(true) {
         label125:
         while(breakIterator.hasNext()) {
            PageBreakPosition thisBreak = (PageBreakPosition)breakIterator.next();
            if (log.isDebugEnabled()) {
               log.debug("| first page: break= " + thisBreak.getLeafPos() + " difference= " + thisBreak.difference + " ratio= " + thisBreak.bpdAdjust);
            }

            int accumulatedS = false;
            int adjustedDiff = 0;

            KnuthElement firstElement;
            while(!(firstElement = (KnuthElement)sequenceIterator.next()).isBox()) {
               log.debug("PLM> ignoring glue or penalty element at the beginning of the sequence");
               if (firstElement.isGlue()) {
                  ((BlockLevelLayoutManager)firstElement.getLayoutManager()).discardSpace((KnuthGlue)firstElement);
               }
            }

            int firstElementIndex = sequenceIterator.previousIndex();
            sequenceIterator.previous();
            MinOptMax lineNumberMaxAdjustment = MinOptMax.ZERO;
            MinOptMax spaceMaxAdjustment = MinOptMax.ZERO;
            double spaceAdjustmentRatio = 0.0;
            LinkedList blockSpacesList = new LinkedList();
            LinkedList unconfirmedList = new LinkedList();
            LinkedList adjustableLinesList = new LinkedList();
            boolean bBoxSeen = false;

            while(true) {
               while(true) {
                  while(sequenceIterator.hasNext() && sequenceIterator.nextIndex() <= thisBreak.getLeafPos()) {
                     thisElement = (KnuthElement)sequenceIterator.next();
                     if (thisElement.isGlue()) {
                        Adjustment adjustment = ((KnuthGlue)thisElement).getAdjustmentClass();
                        if (!adjustment.equals(Adjustment.SPACE_BEFORE_ADJUSTMENT) && !adjustment.equals(Adjustment.SPACE_AFTER_ADJUSTMENT)) {
                           if (adjustment.equals(Adjustment.LINE_NUMBER_ADJUSTMENT)) {
                              lineNumberMaxAdjustment = lineNumberMaxAdjustment.plusMax(thisElement.getStretch());
                              lineNumberMaxAdjustment = lineNumberMaxAdjustment.minusMin(thisElement.getShrink());
                              adjustableLinesList.add(thisElement);
                           } else if (adjustment.equals(Adjustment.LINE_HEIGHT_ADJUSTMENT)) {
                           }
                        } else {
                           unconfirmedList.add(thisElement);
                        }
                     } else if (thisElement.isBox()) {
                        if (!bBoxSeen) {
                           bBoxSeen = true;
                        } else {
                           while(!unconfirmedList.isEmpty()) {
                              KnuthGlue blockSpace = (KnuthGlue)unconfirmedList.removeFirst();
                              spaceMaxAdjustment = spaceMaxAdjustment.plusMax(blockSpace.getStretch());
                              spaceMaxAdjustment = spaceMaxAdjustment.minusMin(blockSpace.getShrink());
                              blockSpacesList.add(blockSpace);
                           }
                        }
                     }
                  }

                  log.debug("| line number adj= " + lineNumberMaxAdjustment);
                  log.debug("| space adj      = " + spaceMaxAdjustment);
                  if (thisElement.isPenalty() && thisElement.getWidth() > 0) {
                     log.debug("  mandatory variation to the number of lines!");
                     ((BlockLevelLayoutManager)thisElement.getLayoutManager()).negotiateBPDAdjustment(thisElement.getWidth(), thisElement);
                  }

                  if (thisBreak.bpdAdjust != 0.0 && thisBreak.difference > 0 && thisBreak.difference <= spaceMaxAdjustment.getMax() || thisBreak.difference < 0 && thisBreak.difference >= spaceMaxAdjustment.getMin()) {
                     spaceAdjustmentRatio = (double)thisBreak.difference / (double)(thisBreak.difference > 0 ? spaceMaxAdjustment.getMax() : spaceMaxAdjustment.getMin());
                     adjustedDiff += this.adjustBlockSpaces(blockSpacesList, thisBreak.difference, thisBreak.difference > 0 ? spaceMaxAdjustment.getMax() : -spaceMaxAdjustment.getMin());
                     log.debug("single space: " + (adjustedDiff != thisBreak.difference && thisBreak.bpdAdjust != 0.0 ? "ERROR" : "ok"));
                     continue label125;
                  }

                  if (thisBreak.bpdAdjust != 0.0) {
                     adjustedDiff += this.adjustLineNumbers(adjustableLinesList, thisBreak.difference, thisBreak.difference > 0 ? lineNumberMaxAdjustment.getMax() : -lineNumberMaxAdjustment.getMin());
                     adjustedDiff += this.adjustBlockSpaces(blockSpacesList, thisBreak.difference - adjustedDiff, thisBreak.difference - adjustedDiff > 0 ? spaceMaxAdjustment.getMax() : -spaceMaxAdjustment.getMin());
                     log.debug("lines and space: " + (adjustedDiff != thisBreak.difference && thisBreak.bpdAdjust != 0.0 ? "ERROR" : "ok"));
                  }
                  continue label125;
               }
            }
         }

         BlockSequence effectiveList = new BlockSequence(blockList.getStartOn(), blockList.getDisplayAlign());
         effectiveList.addAll(this.getCurrentChildLM().getChangedKnuthElements(blockList.subList(0, blockList.size() - blockList.ignoreAtEnd), 0));
         effectiveList.endSequence();
         ElementListObserver.observe(effectiveList, "breaker-effective", (String)null);
         alg.getPageBreaks().clear();
         return effectiveList;
      }
   }

   private int adjustBlockSpaces(LinkedList spaceList, int difference, int total) {
      if (log.isDebugEnabled()) {
         log.debug("AdjustBlockSpaces: difference " + difference + " / " + total + " on " + spaceList.size() + " spaces in block");
      }

      ListIterator spaceListIterator = spaceList.listIterator();
      int adjustedDiff = 0;

      int newAdjust;
      for(int partial = 0; spaceListIterator.hasNext(); adjustedDiff += newAdjust) {
         KnuthGlue blockSpace = (KnuthGlue)spaceListIterator.next();
         partial += difference > 0 ? blockSpace.getStretch() : blockSpace.getShrink();
         if (log.isDebugEnabled()) {
            log.debug("available = " + partial + " / " + total);
            log.debug("competenza  = " + ((int)((float)partial * (float)difference / (float)total) - adjustedDiff) + " / " + difference);
         }

         newAdjust = ((BlockLevelLayoutManager)blockSpace.getLayoutManager()).negotiateBPDAdjustment((int)((float)partial * (float)difference / (float)total) - adjustedDiff, blockSpace);
      }

      return adjustedDiff;
   }

   private int adjustLineNumbers(LinkedList lineList, int difference, int total) {
      if (log.isDebugEnabled()) {
         log.debug("AdjustLineNumbers: difference " + difference + " / " + total + " on " + lineList.size() + " elements");
      }

      ListIterator lineListIterator = lineList.listIterator();
      int adjustedDiff = 0;

      int newAdjust;
      for(int partial = 0; lineListIterator.hasNext(); adjustedDiff += newAdjust) {
         KnuthGlue line = (KnuthGlue)lineListIterator.next();
         partial += difference > 0 ? line.getStretch() : line.getShrink();
         newAdjust = ((BlockLevelLayoutManager)line.getLayoutManager()).negotiateBPDAdjustment((int)((float)partial * (float)difference / (float)total) - adjustedDiff, line);
      }

      return adjustedDiff;
   }

   static {
      log = LogFactory.getLog(AbstractBreaker.class);
   }

   public class BlockSequence extends BlockKnuthSequence {
      public int ignoreAtStart = 0;
      public int ignoreAtEnd = 0;
      private int startOn;
      private int displayAlign;

      public BlockSequence(int startOn, int displayAlign) {
         this.startOn = startOn;
         this.displayAlign = displayAlign;
      }

      public int getStartOn() {
         return this.startOn;
      }

      public int getDisplayAlign() {
         return this.displayAlign;
      }

      public KnuthSequence endSequence() {
         return this.endSequence((Position)null);
      }

      public KnuthSequence endSequence(Position breakPosition) {
         while(this.size() > this.ignoreAtStart && !((KnuthElement)ListUtil.getLast(this)).isBox()) {
            ListUtil.removeLast(this);
         }

         if (this.size() <= this.ignoreAtStart) {
            this.clear();
            return null;
         } else {
            if (this.getDisplayAlign() == 163 && AbstractBreaker.this.isSinglePartFavored()) {
               this.add(new KnuthPenalty(0, -1000, false, breakPosition, false));
               this.ignoreAtEnd = 1;
            } else {
               this.add(new KnuthPenalty(0, 1000, false, (Position)null, false));
               this.add(new KnuthGlue(0, 10000000, 0, (Position)null, false));
               this.add(new KnuthPenalty(0, -1000, false, breakPosition, false));
               this.ignoreAtEnd = 3;
            }

            return this;
         }
      }

      public BlockSequence endBlockSequence(Position breakPosition) {
         KnuthSequence temp = this.endSequence(breakPosition);
         if (temp != null) {
            BlockSequence returnSequence = AbstractBreaker.this.new BlockSequence(this.startOn, this.displayAlign);
            returnSequence.addAll(temp);
            returnSequence.ignoreAtEnd = this.ignoreAtEnd;
            return returnSequence;
         } else {
            return null;
         }
      }
   }

   public static class PageBreakPosition extends LeafPosition {
      double bpdAdjust;
      int difference;
      int footnoteFirstListIndex;
      int footnoteFirstElementIndex;
      int footnoteLastListIndex;
      int footnoteLastElementIndex;

      PageBreakPosition(LayoutManager lm, int breakIndex, int ffli, int ffei, int flli, int flei, double bpdA, int diff) {
         super(lm, breakIndex);
         this.bpdAdjust = bpdA;
         this.difference = diff;
         this.footnoteFirstListIndex = ffli;
         this.footnoteFirstElementIndex = ffei;
         this.footnoteLastListIndex = flli;
         this.footnoteLastElementIndex = flei;
      }
   }
}
