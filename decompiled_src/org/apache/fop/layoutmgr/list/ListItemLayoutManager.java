package org.apache.fop.layoutmgr.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.ListItemBody;
import org.apache.fop.fo.flow.ListItemLabel;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.ConditionalElementListener;
import org.apache.fop.layoutmgr.ElementListObserver;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBlockBox;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.KnuthPossPosIter;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.ListElement;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.RelSide;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;

public class ListItemLayoutManager extends BlockStackingLayoutManager implements ConditionalElementListener {
   private static Log log;
   private ListItemContentLayoutManager label;
   private ListItemContentLayoutManager body;
   private Block curBlockArea = null;
   private List labelList = null;
   private List bodyList = null;
   private boolean discardBorderBefore;
   private boolean discardBorderAfter;
   private boolean discardPaddingBefore;
   private boolean discardPaddingAfter;
   private MinOptMax effSpaceBefore;
   private MinOptMax effSpaceAfter;
   private Keep keepWithNextPendingOnLabel;
   private Keep keepWithNextPendingOnBody;
   private int listItemHeight;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ListItemLayoutManager(ListItem node) {
      super(node);
      this.setLabel(node.getLabel());
      this.setBody(node.getBody());
   }

   protected ListItem getListItemFO() {
      return (ListItem)this.fobj;
   }

   public void setLabel(ListItemLabel node) {
      this.label = new ListItemContentLayoutManager(node);
      this.label.setParent(this);
   }

   public void setBody(ListItemBody node) {
      this.body = new ListItemContentLayoutManager(node);
      this.body.setParent(this);
   }

   public void initialize() {
      this.foSpaceBefore = (new SpaceVal(this.getListItemFO().getCommonMarginBlock().spaceBefore, this)).getSpace();
      this.foSpaceAfter = (new SpaceVal(this.getListItemFO().getCommonMarginBlock().spaceAfter, this)).getSpace();
      this.startIndent = this.getListItemFO().getCommonMarginBlock().startIndent.getValue(this);
      this.endIndent = this.getListItemFO().getCommonMarginBlock().endIndent.getValue(this);
   }

   private void resetSpaces() {
      this.discardBorderBefore = false;
      this.discardBorderAfter = false;
      this.discardPaddingBefore = false;
      this.discardPaddingAfter = false;
      this.effSpaceBefore = null;
      this.effSpaceAfter = null;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      this.referenceIPD = context.getRefIPD();
      List returnList = new LinkedList();
      if (!this.breakBeforeServed) {
         this.breakBeforeServed = true;
         if (!context.suppressBreakBefore() && this.addKnuthElementsForBreakBefore(returnList, context)) {
            return returnList;
         }
      }

      this.addKnuthElementsForSpaceBefore(returnList, alignment);
      this.addKnuthElementsForBorderPaddingBefore(returnList, !this.firstVisibleMarkServed);
      this.firstVisibleMarkServed = true;
      this.addPendingMarks(context);
      LayoutContext childLC = new LayoutContext(0);
      childLC.setRefIPD(context.getRefIPD());
      this.label.initialize();
      this.labelList = this.label.getNextKnuthElements(childLC, alignment);
      SpaceResolver.resolveElementList(this.labelList);
      ElementListObserver.observe(this.labelList, "list-item-label", this.label.getPartFO().getId());
      context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
      this.keepWithNextPendingOnLabel = childLC.getKeepWithNextPending();
      childLC = new LayoutContext(0);
      childLC.setRefIPD(context.getRefIPD());
      this.body.initialize();
      this.bodyList = this.body.getNextKnuthElements(childLC, alignment);
      SpaceResolver.resolveElementList(this.bodyList);
      ElementListObserver.observe(this.bodyList, "list-item-body", this.body.getPartFO().getId());
      context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
      this.keepWithNextPendingOnBody = childLC.getKeepWithNextPending();
      List returnedList = this.getCombinedKnuthElementsForListItem(this.labelList, this.bodyList, context);
      this.wrapPositionElements(returnedList, returnList, true);
      this.addKnuthElementsForBorderPaddingAfter(returnList, true);
      this.addKnuthElementsForSpaceAfter(returnList, alignment);
      this.addKnuthElementsForBreakAfter(returnList, context);
      context.updateKeepWithNextPending(this.keepWithNextPendingOnLabel);
      context.updateKeepWithNextPending(this.keepWithNextPendingOnBody);
      context.updateKeepWithNextPending(this.getKeepWithNext());
      context.updateKeepWithPreviousPending(this.getKeepWithPrevious());
      this.setFinished(true);
      this.resetSpaces();
      return returnList;
   }

   private List getCombinedKnuthElementsForListItem(List labelElements, List bodyElements, LayoutContext context) {
      List[] elementLists = new List[]{new ArrayList(labelElements), new ArrayList(bodyElements)};
      int[] fullHeights = new int[]{ElementListUtils.calcContentLength(elementLists[0]), ElementListUtils.calcContentLength(elementLists[1])};
      int[] partialHeights = new int[]{0, 0};
      int[] start = new int[]{-1, -1};
      int[] end = new int[]{-1, -1};
      int totalHeight = Math.max(fullHeights[0], fullHeights[1]);
      int addedBoxHeight = 0;
      Keep keepWithNextActive = Keep.KEEP_AUTO;
      LinkedList returnList = new LinkedList();

      int step;
      while((step = this.getNextStep(elementLists, start, end, partialHeights)) > 0) {
         if (end[0] + 1 == elementLists[0].size()) {
            keepWithNextActive = keepWithNextActive.compare(this.keepWithNextPendingOnLabel);
         }

         if (end[1] + 1 == elementLists[1].size()) {
            keepWithNextActive = keepWithNextActive.compare(this.keepWithNextPendingOnBody);
         }

         int penaltyHeight = step + this.getMaxRemainingHeight(fullHeights, partialHeights) - totalHeight;
         int additionalPenaltyHeight = 0;
         int stepPenalty = 0;
         KnuthElement endEl = (KnuthElement)elementLists[0].get(end[0]);
         if (endEl instanceof KnuthPenalty) {
            additionalPenaltyHeight = endEl.getWidth();
            stepPenalty = Math.max(stepPenalty, endEl.getPenalty());
         }

         endEl = (KnuthElement)elementLists[1].get(end[1]);
         if (endEl instanceof KnuthPenalty) {
            additionalPenaltyHeight = Math.max(additionalPenaltyHeight, endEl.getWidth());
            stepPenalty = Math.max(stepPenalty, endEl.getPenalty());
         }

         int boxHeight = step - addedBoxHeight - penaltyHeight;
         penaltyHeight += additionalPenaltyHeight;
         LinkedList footnoteList = null;

         for(int i = 0; i < elementLists.length; ++i) {
            for(int j = start[i]; j <= end[i]; ++j) {
               ListElement el = (ListElement)elementLists[i].get(j);
               if (el instanceof KnuthBlockBox && ((KnuthBlockBox)el).hasAnchors()) {
                  if (footnoteList == null) {
                     footnoteList = new LinkedList();
                  }

                  footnoteList.addAll(((KnuthBlockBox)el).getFootnoteBodyLMs());
               }
            }
         }

         addedBoxHeight += boxHeight;
         ListItemPosition stepPosition = new ListItemPosition(this, start[0], end[0], start[1], end[1]);
         if (footnoteList == null) {
            returnList.add(new KnuthBox(boxHeight, stepPosition, false));
         } else {
            returnList.add(new KnuthBlockBox(boxHeight, footnoteList, stepPosition, false));
         }

         if (addedBoxHeight < totalHeight) {
            Keep keep = keepWithNextActive.compare(this.getKeepTogether());
            int p = stepPenalty;
            if (stepPenalty > -1000) {
               p = Math.max(stepPenalty, keep.getPenalty());
            }

            returnList.add(new BreakElement(stepPosition, penaltyHeight, p, keep.getContext(), context));
         }
      }

      return returnList;
   }

   private int getNextStep(List[] elementLists, int[] start, int[] end, int[] partialHeights) {
      int[] backupHeights = new int[]{partialHeights[0], partialHeights[1]};
      start[0] = end[0] + 1;
      start[1] = end[1] + 1;
      int seqCount = 0;

      int step;
      for(step = 0; step < start.length; ++step) {
         while(end[step] + 1 < elementLists[step].size()) {
            int var10002 = end[step]++;
            KnuthElement el = (KnuthElement)elementLists[step].get(end[step]);
            if (el.isPenalty()) {
               if (el.getPenalty() < 1000) {
                  break;
               }
            } else if (el.isGlue()) {
               if (end[step] > 0) {
                  KnuthElement prev = (KnuthElement)elementLists[step].get(end[step] - 1);
                  if (prev.isBox()) {
                     break;
                  }
               }

               partialHeights[step] += el.getWidth();
            } else {
               partialHeights[step] += el.getWidth();
            }
         }

         if (end[step] < start[step]) {
            partialHeights[step] = backupHeights[step];
         } else {
            ++seqCount;
         }
      }

      if (seqCount == 0) {
         return 0;
      } else {
         if (backupHeights[0] == 0 && backupHeights[1] == 0) {
            step = Math.max(end[0] >= start[0] ? partialHeights[0] : Integer.MIN_VALUE, end[1] >= start[1] ? partialHeights[1] : Integer.MIN_VALUE);
         } else {
            step = Math.min(end[0] >= start[0] ? partialHeights[0] : Integer.MAX_VALUE, end[1] >= start[1] ? partialHeights[1] : Integer.MAX_VALUE);
         }

         for(int i = 0; i < partialHeights.length; ++i) {
            if (partialHeights[i] > step) {
               partialHeights[i] = backupHeights[i];
               end[i] = start[i] - 1;
            }
         }

         return step;
      }
   }

   private int getMaxRemainingHeight(int[] fullHeights, int[] partialHeights) {
      return Math.max(fullHeights[0] - partialHeights[0], fullHeights[1] - partialHeights[1]);
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      this.labelList = this.label.getChangedKnuthElements(this.labelList, alignment);
      ListIterator oldListIterator = oldList.listIterator();

      while(oldListIterator.hasNext()) {
         KnuthElement oldElement = (KnuthElement)oldListIterator.next();
         Position innerPosition = oldElement.getPosition().getPosition();
         if (innerPosition != null) {
            oldElement.setPosition(innerPosition);
         } else {
            oldElement.setPosition(new Position(this));
         }
      }

      List returnedList = this.body.getChangedKnuthElements(oldList, alignment);
      List tempList = returnedList;
      List returnedList = new LinkedList();
      ListIterator listIter = tempList.listIterator();

      while(listIter.hasNext()) {
         KnuthElement tempElement = (KnuthElement)listIter.next();
         tempElement.setPosition(new NonLeafPosition(this, tempElement.getPosition()));
         returnedList.add(tempElement);
      }

      return returnedList;
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      this.getParentArea((Area)null);
      this.addId();
      LayoutContext lc = new LayoutContext(0);
      Position firstPos = null;
      Position lastPos = null;
      LinkedList positionList = new LinkedList();

      while(parentIter.hasNext()) {
         Position pos = (Position)parentIter.next();
         if (pos.getIndex() >= 0) {
            if (firstPos == null) {
               firstPos = pos;
            }

            lastPos = pos;
         }

         if (pos instanceof NonLeafPosition && pos.getPosition() != null) {
            positionList.add(pos.getPosition());
         }
      }

      this.addMarkersToPage(true, this.isFirst(firstPos), this.isLast(lastPos));
      int labelFirstIndex = ((ListItemPosition)positionList.getFirst()).getLabelFirstIndex();
      int labelLastIndex = ((ListItemPosition)positionList.getLast()).getLabelLastIndex();
      int bodyFirstIndex = ((ListItemPosition)positionList.getFirst()).getBodyFirstIndex();
      int bodyLastIndex = ((ListItemPosition)positionList.getLast()).getBodyLastIndex();
      int previousBreak = ElementListUtils.determinePreviousBreak(this.labelList, labelFirstIndex);
      SpaceResolver.performConditionalsNotification(this.labelList, labelFirstIndex, labelLastIndex, previousBreak);
      previousBreak = ElementListUtils.determinePreviousBreak(this.bodyList, bodyFirstIndex);
      SpaceResolver.performConditionalsNotification(this.bodyList, bodyFirstIndex, bodyLastIndex, previousBreak);
      KnuthPossPosIter bodyIter;
      if (labelFirstIndex <= labelLastIndex) {
         bodyIter = new KnuthPossPosIter(this.labelList, labelFirstIndex, labelLastIndex + 1);
         lc.setFlags(32, layoutContext.isFirstArea());
         lc.setFlags(128, layoutContext.isLastArea());
         lc.setSpaceAdjust(layoutContext.getSpaceAdjust());
         lc.setStackLimitBP(layoutContext.getStackLimitBP());
         this.label.addAreas(bodyIter, lc);
      }

      if (bodyFirstIndex <= bodyLastIndex) {
         bodyIter = new KnuthPossPosIter(this.bodyList, bodyFirstIndex, bodyLastIndex + 1);
         lc.setFlags(32, layoutContext.isFirstArea());
         lc.setFlags(128, layoutContext.isLastArea());
         lc.setSpaceAdjust(layoutContext.getSpaceAdjust());
         lc.setStackLimitBP(layoutContext.getStackLimitBP());
         this.body.addAreas(bodyIter, lc);
      }

      int childCount = this.curBlockArea.getChildAreas().size();
      if ($assertionsDisabled || childCount >= 1 && childCount <= 2) {
         int itemBPD = ((Block)this.curBlockArea.getChildAreas().get(0)).getAllocBPD();
         if (childCount == 2) {
            itemBPD = Math.max(itemBPD, ((Block)this.curBlockArea.getChildAreas().get(1)).getAllocBPD());
         }

         this.curBlockArea.setBPD(itemBPD);
         this.addMarkersToPage(false, this.isFirst(firstPos), this.isLast(lastPos));
         TraitSetter.addBackground(this.curBlockArea, this.getListItemFO().getCommonBorderPaddingBackground(), this);
         TraitSetter.addSpaceBeforeAfter(this.curBlockArea, layoutContext.getSpaceAdjust(), this.effSpaceBefore, this.effSpaceAfter);
         this.flush();
         this.curBlockArea = null;
         this.resetSpaces();
         this.checkEndOfLayout(lastPos);
      } else {
         throw new AssertionError();
      }
   }

   public int getListItemHeight() {
      return this.listItemHeight;
   }

   public Area getParentArea(Area childArea) {
      if (this.curBlockArea == null) {
         this.curBlockArea = new Block();
         this.parentLayoutManager.getParentArea(this.curBlockArea);
         TraitSetter.setProducerID(this.curBlockArea, this.getListItemFO().getId());
         TraitSetter.addBorders(this.curBlockArea, this.getListItemFO().getCommonBorderPaddingBackground(), this.discardBorderBefore, this.discardBorderAfter, false, false, this);
         TraitSetter.addPadding(this.curBlockArea, this.getListItemFO().getCommonBorderPaddingBackground(), this.discardPaddingBefore, this.discardPaddingAfter, false, false, this);
         TraitSetter.addMargins(this.curBlockArea, this.getListItemFO().getCommonBorderPaddingBackground(), this.getListItemFO().getCommonMarginBlock(), this);
         TraitSetter.addBreaks(this.curBlockArea, this.getListItemFO().getBreakBefore(), this.getListItemFO().getBreakAfter());
         int contentIPD = this.referenceIPD - this.getIPIndents();
         this.curBlockArea.setIPD(contentIPD);
         this.setCurrentArea(this.curBlockArea);
      }

      return this.curBlockArea;
   }

   public void addChildArea(Area childArea) {
      if (this.curBlockArea != null) {
         this.curBlockArea.addBlock((Block)childArea);
      }

   }

   public KeepProperty getKeepTogetherProperty() {
      return this.getListItemFO().getKeepTogether();
   }

   public KeepProperty getKeepWithPreviousProperty() {
      return this.getListItemFO().getKeepWithPrevious();
   }

   public KeepProperty getKeepWithNextProperty() {
      return this.getListItemFO().getKeepWithNext();
   }

   public void notifySpace(RelSide side, MinOptMax effectiveLength) {
      if (RelSide.BEFORE == side) {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceBefore + "-> " + effectiveLength);
         }

         this.effSpaceBefore = effectiveLength;
      } else {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceAfter + "-> " + effectiveLength);
         }

         this.effSpaceAfter = effectiveLength;
      }

   }

   public void notifyBorder(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardBorderBefore = true;
         } else {
            this.discardBorderAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Border " + side + " -> " + effectiveLength);
      }

   }

   public void notifyPadding(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardPaddingBefore = true;
         } else {
            this.discardPaddingAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Padding " + side + " -> " + effectiveLength);
      }

   }

   public void reset() {
      super.reset();
      this.label.reset();
      this.body.reset();
   }

   static {
      $assertionsDisabled = !ListItemLayoutManager.class.desiredAssertionStatus();
      log = LogFactory.getLog(ListItemLayoutManager.class);
   }

   private class ListItemPosition extends Position {
      private int iLabelFirstIndex;
      private int iLabelLastIndex;
      private int iBodyFirstIndex;
      private int iBodyLastIndex;

      public ListItemPosition(LayoutManager lm, int labelFirst, int labelLast, int bodyFirst, int bodyLast) {
         super(lm);
         this.iLabelFirstIndex = labelFirst;
         this.iLabelLastIndex = labelLast;
         this.iBodyFirstIndex = bodyFirst;
         this.iBodyLastIndex = bodyLast;
      }

      public int getLabelFirstIndex() {
         return this.iLabelFirstIndex;
      }

      public int getLabelLastIndex() {
         return this.iLabelLastIndex;
      }

      public int getBodyFirstIndex() {
         return this.iBodyFirstIndex;
      }

      public int getBodyLastIndex() {
         return this.iBodyLastIndex;
      }

      public boolean generatesAreas() {
         return true;
      }

      public String toString() {
         StringBuffer sb = new StringBuffer("ListItemPosition:");
         sb.append(this.getIndex()).append("(");
         sb.append("label:").append(this.iLabelFirstIndex).append("-").append(this.iLabelLastIndex);
         sb.append(" body:").append(this.iBodyFirstIndex).append("-").append(this.iBodyLastIndex);
         sb.append(")");
         return sb.toString();
      }
   }
}
