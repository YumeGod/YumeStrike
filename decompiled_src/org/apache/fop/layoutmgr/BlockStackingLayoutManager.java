package org.apache.fop.layoutmgr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.BlockParent;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.layoutmgr.inline.InlineLayoutManager;
import org.apache.fop.layoutmgr.inline.LineLayoutManager;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.BreakUtil;
import org.apache.fop.util.ListUtil;

public abstract class BlockStackingLayoutManager extends AbstractLayoutManager implements BlockLevelLayoutManager {
   private static Log log;
   protected BlockParent parentArea;
   protected int bpUnit;
   protected int adjustedSpaceBefore;
   protected int adjustedSpaceAfter;
   protected List storedList;
   protected boolean breakBeforeServed;
   protected boolean firstVisibleMarkServed;
   protected int referenceIPD;
   protected int startIndent;
   protected int endIndent;
   protected MinOptMax foSpaceBefore;
   protected MinOptMax foSpaceAfter;
   private Position auxiliaryPosition;
   private int contentAreaIPD;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public BlockStackingLayoutManager(FObj node) {
      super(node);
      this.setGeneratesBlockArea(true);
   }

   protected BlockParent getCurrentArea() {
      return this.parentArea;
   }

   protected void setCurrentArea(BlockParent parentArea) {
      this.parentArea = parentArea;
   }

   public void addBlockSpacing(double adjust, MinOptMax minoptmax) {
      int sp = TraitSetter.getEffectiveSpace(adjust, minoptmax);
      if (sp != 0) {
         Block spacer = new Block();
         spacer.setBPD(sp);
         this.parentLayoutManager.addChildArea(spacer);
      }

   }

   protected void addChildToArea(Area childArea, BlockParent parentArea) {
      if (!(childArea instanceof Block)) {
      }

      parentArea.addBlock((Block)childArea);
      this.flush();
   }

   public void addChildArea(Area childArea) {
      this.addChildToArea(childArea, this.getCurrentArea());
   }

   protected void notifyEndOfLayout() {
      super.notifyEndOfLayout();
   }

   protected void flush() {
      if (this.getCurrentArea() != null) {
         this.parentLayoutManager.addChildArea(this.getCurrentArea());
      }

   }

   protected Position getAuxiliaryPosition() {
      if (this.auxiliaryPosition == null) {
         this.auxiliaryPosition = new NonLeafPosition(this, (Position)null);
      }

      return this.auxiliaryPosition;
   }

   protected int neededUnits(int len) {
      return (int)Math.ceil((double)((float)len / (float)this.bpUnit));
   }

   protected int updateContentAreaIPDwithOverconstrainedAdjust() {
      int ipd = this.referenceIPD - (this.startIndent + this.endIndent);
      if (ipd < 0) {
         log.debug("Adjusting end-indent based on overconstrained geometry rules for " + this.fobj);
         BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getFObj().getUserAgent().getEventBroadcaster());
         eventProducer.overconstrainedAdjustEndIndent(this, this.getFObj().getName(), ipd, this.getFObj().getLocator());
         this.endIndent += ipd;
         ipd = 0;
      }

      this.setContentAreaIPD(ipd);
      return ipd;
   }

   protected int updateContentAreaIPDwithOverconstrainedAdjust(int contentIPD) {
      int ipd = this.referenceIPD - (contentIPD + this.startIndent + this.endIndent);
      if (ipd < 0) {
         log.debug("Adjusting end-indent based on overconstrained geometry rules for " + this.fobj);
         BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getFObj().getUserAgent().getEventBroadcaster());
         eventProducer.overconstrainedAdjustEndIndent(this, this.getFObj().getName(), ipd, this.getFObj().getLocator());
         this.endIndent += ipd;
      }

      this.setContentAreaIPD(contentIPD);
      return contentIPD;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      this.referenceIPD = context.getRefIPD();
      this.updateContentAreaIPDwithOverconstrainedAdjust();
      List contentList = new LinkedList();
      List elements = new LinkedList();
      if (!this.breakBeforeServed) {
         this.breakBeforeServed = true;
         if (!context.suppressBreakBefore() && this.addKnuthElementsForBreakBefore(elements, context)) {
            return elements;
         }
      }

      if (!this.firstVisibleMarkServed) {
         this.addKnuthElementsForSpaceBefore(elements, alignment);
         context.updateKeepWithPreviousPending(this.getKeepWithPrevious());
      }

      this.addKnuthElementsForBorderPaddingBefore(elements, !this.firstVisibleMarkServed);
      this.firstVisibleMarkServed = true;
      this.addPendingMarks(context);
      BreakElement forcedBreakAfterLast = null;

      LayoutManager currentChildLM;
      while((currentChildLM = this.getChildLM()) != null) {
         LayoutContext childLC = new LayoutContext(0);
         List childrenElements = this.getNextChildElements(currentChildLM, context, childLC, alignment);
         if (contentList.isEmpty()) {
            context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
         }

         if (childrenElements != null && !childrenElements.isEmpty()) {
            if (!contentList.isEmpty() && !ElementListUtils.startsWithForcedBreak(childrenElements)) {
               this.addInBetweenBreak(contentList, context, childLC);
            }

            if (childrenElements.size() == 1 && ElementListUtils.startsWithForcedBreak(childrenElements)) {
               if (currentChildLM.isFinished() && !this.hasNextChildLM()) {
                  forcedBreakAfterLast = (BreakElement)childrenElements.get(0);
                  context.clearPendingMarks();
                  break;
               }

               if (contentList.isEmpty()) {
                  elements.add(new KnuthBox(0, this.notifyPos(new Position(this)), false));
               }

               contentList.addAll(childrenElements);
               this.wrapPositionElements(contentList, elements);
               return elements;
            }

            contentList.addAll(childrenElements);
            if (ElementListUtils.endsWithForcedBreak(childrenElements)) {
               if (currentChildLM.isFinished() && !this.hasNextChildLM()) {
                  forcedBreakAfterLast = (BreakElement)ListUtil.removeLast(contentList);
                  context.clearPendingMarks();
                  break;
               }

               this.wrapPositionElements(contentList, elements);
               return elements;
            }

            context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
         }
      }

      if (!contentList.isEmpty()) {
         this.wrapPositionElements(contentList, elements);
      } else if (forcedBreakAfterLast == null) {
         elements.add(new KnuthBox(0, this.notifyPos(new Position(this)), true));
      }

      this.addKnuthElementsForBorderPaddingAfter(elements, true);
      this.addKnuthElementsForSpaceAfter(elements, alignment);
      context.clearPendingMarks();
      if (forcedBreakAfterLast == null) {
         this.addKnuthElementsForBreakAfter(elements, context);
      } else {
         forcedBreakAfterLast.clearPendingMarks();
         elements.add(forcedBreakAfterLast);
      }

      context.updateKeepWithNextPending(this.getKeepWithNext());
      this.setFinished(true);
      return elements;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment, Stack lmStack, Position restartPosition, LayoutManager restartAtLM) {
      this.referenceIPD = context.getRefIPD();
      this.updateContentAreaIPDwithOverconstrainedAdjust();
      List contentList = new LinkedList();
      List elements = new LinkedList();
      if (!this.breakBeforeServed) {
         this.breakBeforeServed = true;
         if (!context.suppressBreakBefore() && this.addKnuthElementsForBreakBefore(elements, context)) {
            return elements;
         }
      }

      if (!this.firstVisibleMarkServed) {
         this.addKnuthElementsForSpaceBefore(elements, alignment);
         context.updateKeepWithPreviousPending(this.getKeepWithPrevious());
      }

      this.addKnuthElementsForBorderPaddingBefore(elements, !this.firstVisibleMarkServed);
      this.firstVisibleMarkServed = true;
      this.addPendingMarks(context);
      BreakElement forcedBreakAfterLast = null;
      LayoutContext childLC = new LayoutContext(0);
      List childrenElements;
      Object currentChildLM;
      if (!lmStack.isEmpty()) {
         currentChildLM = (BlockLevelLayoutManager)lmStack.pop();
         this.setCurrentChildLM((LayoutManager)currentChildLM);
         childrenElements = this.getNextChildElements((LayoutManager)currentChildLM, context, childLC, alignment, lmStack, restartPosition, restartAtLM);
      } else {
         if (!$assertionsDisabled && (restartAtLM == null || restartAtLM.getParent() != this)) {
            throw new AssertionError();
         }

         currentChildLM = restartAtLM;
         restartAtLM.reset();
         this.setCurrentChildLM(restartAtLM);
         childrenElements = this.getNextChildElements(restartAtLM, context, childLC, alignment);
      }

      if (contentList.isEmpty()) {
         context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
      }

      if (childrenElements != null && !childrenElements.isEmpty()) {
         if (!contentList.isEmpty() && !ElementListUtils.startsWithForcedBreak(childrenElements)) {
            this.addInBetweenBreak(contentList, context, childLC);
         }

         if (childrenElements.size() == 1 && ElementListUtils.startsWithForcedBreak(childrenElements)) {
            if (((LayoutManager)currentChildLM).isFinished() && !this.hasNextChildLM()) {
               forcedBreakAfterLast = (BreakElement)childrenElements.get(0);
               context.clearPendingMarks();
            }

            if (contentList.isEmpty()) {
               elements.add(new KnuthBox(0, this.notifyPos(new Position(this)), false));
            }

            contentList.addAll(childrenElements);
            this.wrapPositionElements(contentList, elements);
            return elements;
         }

         contentList.addAll(childrenElements);
         if (ElementListUtils.endsWithForcedBreak(childrenElements)) {
            if (((LayoutManager)currentChildLM).isFinished() && !this.hasNextChildLM()) {
               forcedBreakAfterLast = (BreakElement)ListUtil.removeLast(contentList);
               context.clearPendingMarks();
            }

            this.wrapPositionElements(contentList, elements);
            return elements;
         }

         context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
      }

      LayoutManager currentChildLM;
      while((currentChildLM = this.getChildLM()) != null) {
         currentChildLM.reset();
         childLC = new LayoutContext(0);
         childrenElements = this.getNextChildElements(currentChildLM, context, childLC, alignment);
         if (contentList.isEmpty()) {
            context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
         }

         if (childrenElements != null && !childrenElements.isEmpty()) {
            if (!contentList.isEmpty() && !ElementListUtils.startsWithForcedBreak(childrenElements)) {
               this.addInBetweenBreak(contentList, context, childLC);
            }

            if (childrenElements.size() == 1 && ElementListUtils.startsWithForcedBreak(childrenElements)) {
               if (!currentChildLM.isFinished() || this.hasNextChildLM()) {
                  if (contentList.isEmpty()) {
                     elements.add(new KnuthBox(0, this.notifyPos(new Position(this)), false));
                  }

                  contentList.addAll(childrenElements);
                  this.wrapPositionElements(contentList, elements);
                  return elements;
               }

               forcedBreakAfterLast = (BreakElement)childrenElements.get(0);
               context.clearPendingMarks();
               break;
            }

            contentList.addAll(childrenElements);
            if (ElementListUtils.endsWithForcedBreak(childrenElements)) {
               if (currentChildLM.isFinished() && !this.hasNextChildLM()) {
                  forcedBreakAfterLast = (BreakElement)ListUtil.removeLast(contentList);
                  context.clearPendingMarks();
                  break;
               }

               this.wrapPositionElements(contentList, elements);
               return elements;
            }

            context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
         }
      }

      if (!contentList.isEmpty()) {
         this.wrapPositionElements(contentList, elements);
      } else if (forcedBreakAfterLast == null) {
         elements.add(new KnuthBox(0, this.notifyPos(new Position(this)), true));
      }

      this.addKnuthElementsForBorderPaddingAfter(elements, true);
      this.addKnuthElementsForSpaceAfter(elements, alignment);
      context.clearPendingMarks();
      if (forcedBreakAfterLast == null) {
         this.addKnuthElementsForBreakAfter(elements, context);
      } else {
         forcedBreakAfterLast.clearPendingMarks();
         elements.add(forcedBreakAfterLast);
      }

      context.updateKeepWithNextPending(this.getKeepWithNext());
      this.setFinished(true);
      return elements;
   }

   private List getNextChildElements(LayoutManager childLM, LayoutContext context, LayoutContext childLC, int alignment) {
      return this.getNextChildElements(childLM, context, childLC, alignment, (Stack)null, (Position)null, (LayoutManager)null);
   }

   private List getNextChildElements(LayoutManager childLM, LayoutContext context, LayoutContext childLC, int alignment, Stack lmStack, Position restartPosition, LayoutManager restartAtLM) {
      childLC.copyPendingMarksFrom(context);
      childLC.setStackLimitBP(context.getStackLimitBP());
      if (childLM instanceof LineLayoutManager) {
         childLC.setRefIPD(this.getContentAreaIPD());
      } else {
         childLC.setRefIPD(this.referenceIPD);
      }

      if (childLM == this.childLMs.get(0)) {
         childLC.setFlags(16);
      }

      if (lmStack == null) {
         return childLM.getNextKnuthElements(childLC, alignment);
      } else {
         return childLM instanceof LineLayoutManager ? ((LineLayoutManager)childLM).getNextKnuthElements(childLC, alignment, (LeafPosition)restartPosition) : childLM.getNextKnuthElements(childLC, alignment, lmStack, restartPosition, restartAtLM);
      }
   }

   protected void addInBetweenBreak(List contentList, LayoutContext parentLC, LayoutContext childLC) {
      if (!this.mustKeepTogether() && !parentLC.isKeepWithNextPending() && !childLC.isKeepWithPreviousPending()) {
         ListElement last = (ListElement)ListUtil.getLast(contentList);
         if (last.isGlue()) {
            log.warn("glue-type break possibility not handled properly, yet");
         } else if (!ElementListUtils.endsWithNonInfinitePenalty(contentList)) {
            contentList.add(new BreakElement(new Position(this), 0, 9, parentLC));
         }

      } else {
         Keep keep = this.getKeepTogether();
         keep = keep.compare(parentLC.getKeepWithNextPending());
         parentLC.clearKeepWithNextPending();
         keep = keep.compare(childLC.getKeepWithPreviousPending());
         childLC.clearKeepWithPreviousPending();
         contentList.add(new BreakElement(new Position(this), keep.getPenalty(), keep.getContext(), parentLC));
      }
   }

   public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
      Position innerPosition = lastElement.getPosition().getPosition();
      if (innerPosition == null && lastElement.isGlue()) {
         if (((KnuthGlue)lastElement).getAdjustmentClass() == Adjustment.SPACE_BEFORE_ADJUSTMENT) {
            this.adjustedSpaceBefore += adj;
         } else {
            this.adjustedSpaceAfter += adj;
         }

         return adj;
      } else if (innerPosition instanceof MappingPosition) {
         MappingPosition mappingPos = (MappingPosition)innerPosition;
         if (lastElement.isGlue()) {
            ListIterator storedListIterator = this.storedList.listIterator(mappingPos.getFirstIndex());
            int newAdjustment = 0;

            while(storedListIterator.nextIndex() <= mappingPos.getLastIndex()) {
               KnuthElement storedElement = (KnuthElement)storedListIterator.next();
               if (storedElement.isGlue()) {
                  newAdjustment += ((BlockLevelLayoutManager)storedElement.getLayoutManager()).negotiateBPDAdjustment(adj - newAdjustment, storedElement);
               }
            }

            newAdjustment = newAdjustment > 0 ? this.bpUnit * this.neededUnits(newAdjustment) : -this.bpUnit * this.neededUnits(-newAdjustment);
            return newAdjustment;
         } else {
            KnuthPenalty storedPenalty = (KnuthPenalty)this.storedList.get(mappingPos.getLastIndex());
            return storedPenalty.getWidth() > 0 ? ((BlockLevelLayoutManager)storedPenalty.getLayoutManager()).negotiateBPDAdjustment(storedPenalty.getWidth(), storedPenalty) : adj;
         }
      } else if (innerPosition.getLM() != this) {
         NonLeafPosition savedPos = (NonLeafPosition)lastElement.getPosition();
         lastElement.setPosition(innerPosition);
         int returnValue = ((BlockLevelLayoutManager)lastElement.getLayoutManager()).negotiateBPDAdjustment(adj, lastElement);
         lastElement.setPosition(savedPos);
         return returnValue;
      } else {
         log.error("BlockLayoutManager.negotiateBPDAdjustment(): unexpected Position");
         return 0;
      }
   }

   public void discardSpace(KnuthGlue spaceGlue) {
      Position innerPosition = ((NonLeafPosition)spaceGlue.getPosition()).getPosition();
      if (innerPosition != null && innerPosition.getLM() != this) {
         NonLeafPosition savedPos = (NonLeafPosition)spaceGlue.getPosition();
         spaceGlue.setPosition(innerPosition);
         ((BlockLevelLayoutManager)spaceGlue.getLayoutManager()).discardSpace(spaceGlue);
         spaceGlue.setPosition(savedPos);
      } else if (spaceGlue.getAdjustmentClass() == Adjustment.SPACE_BEFORE_ADJUSTMENT) {
         this.adjustedSpaceBefore = 0;
         this.foSpaceBefore = MinOptMax.ZERO;
      } else {
         this.adjustedSpaceAfter = 0;
         this.foSpaceAfter = MinOptMax.ZERO;
      }

   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      ListIterator oldListIterator = oldList.listIterator();
      KnuthElement currElement = null;
      KnuthElement prevElement = null;
      List returnedList = new LinkedList();
      List returnList = new LinkedList();
      int fromIndex = 0;
      KnuthElement oldElement = null;

      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         Position innerPosition = ((NonLeafPosition)oldElement.getPosition()).getPosition();
         if (innerPosition != null) {
            oldElement.setPosition(innerPosition);
         } else {
            oldElement.setPosition(new Position(this));
         }
      }

      List workList;
      if (this.bpUnit == 0) {
         workList = oldList;
      } else {
         oldListIterator = oldList.listIterator();

         KnuthElement el;
         for(el = (KnuthElement)oldListIterator.next(); !(el.getPosition() instanceof MappingPosition); el = (KnuthElement)oldListIterator.next()) {
         }

         int iFirst = ((MappingPosition)el.getPosition()).getFirstIndex();
         oldListIterator = oldList.listIterator(oldList.size());

         for(el = (KnuthElement)oldListIterator.previous(); !(el.getPosition() instanceof MappingPosition); el = (KnuthElement)oldListIterator.previous()) {
         }

         int iLast = ((MappingPosition)el.getPosition()).getLastIndex();
         workList = this.storedList.subList(iFirst, iLast + 1);
      }

      boolean spaceAfterIsConditional;
      BlockLevelLayoutManager prevLM;
      for(ListIterator workListIterator = workList.listIterator(); workListIterator.hasNext(); prevElement = currElement) {
         currElement = (KnuthElement)workListIterator.next();
         if (prevElement != null && prevElement.getLayoutManager() != currElement.getLayoutManager()) {
            prevLM = (BlockLevelLayoutManager)prevElement.getLayoutManager();
            BlockLevelLayoutManager currLM = (BlockLevelLayoutManager)currElement.getLayoutManager();
            spaceAfterIsConditional = false;
            if (prevLM != this) {
               ((List)returnedList).addAll(prevLM.getChangedKnuthElements(workList.subList(fromIndex, workListIterator.previousIndex()), alignment));
               spaceAfterIsConditional = true;
            }

            fromIndex = workListIterator.previousIndex();
            if (spaceAfterIsConditional && (this.mustKeepTogether() || prevLM.mustKeepWithNext() || currLM.mustKeepWithPrevious())) {
               ((List)returnedList).add(new KnuthPenalty(0, 1000, false, new Position(this), false));
            } else if (spaceAfterIsConditional && !((KnuthElement)ListUtil.getLast((List)returnedList)).isGlue()) {
               ((List)returnedList).add(new KnuthPenalty(0, 0, false, new Position(this), false));
            }
         }
      }

      if (currElement != null) {
         prevLM = (BlockLevelLayoutManager)currElement.getLayoutManager();
         if (prevLM != this) {
            ((List)returnedList).addAll(prevLM.getChangedKnuthElements(workList.subList(fromIndex, workList.size()), alignment));
         } else if (!((List)returnedList).isEmpty()) {
            ListUtil.removeLast((List)returnedList);
         }
      }

      boolean spaceBeforeIsConditional = true;
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         spaceBeforeIsConditional = ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceBefore.getSpace().isDiscard();
      }

      if (this.bpUnit > 0 || this.adjustedSpaceBefore != 0) {
         if (!spaceBeforeIsConditional) {
            returnList.add(new KnuthBox(0, new NonLeafPosition(this, (Position)null), false));
            returnList.add(new KnuthPenalty(0, 1000, false, new NonLeafPosition(this, (Position)null), false));
         }

         if (this.bpUnit > 0) {
            returnList.add(new KnuthGlue(0, 0, 0, Adjustment.SPACE_BEFORE_ADJUSTMENT, new NonLeafPosition(this, (Position)null), true));
         } else {
            returnList.add(new KnuthGlue(this.adjustedSpaceBefore, 0, 0, Adjustment.SPACE_BEFORE_ADJUSTMENT, new NonLeafPosition(this, (Position)null), true));
         }
      }

      if (this.bpUnit > 0) {
         this.storedList = (List)returnedList;
         returnedList = this.createUnitElements((List)returnedList);
      }

      ListIterator listIter = ((List)returnedList).listIterator();

      while(listIter.hasNext()) {
         KnuthElement returnedElement = (KnuthElement)listIter.next();
         returnedElement.setPosition(new NonLeafPosition(this, returnedElement.getPosition()));
         returnList.add(returnedElement);
      }

      spaceAfterIsConditional = true;
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         spaceAfterIsConditional = ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceAfter.getSpace().isDiscard();
      }

      if (this.bpUnit > 0 || this.adjustedSpaceAfter != 0) {
         if (!spaceAfterIsConditional) {
            returnList.add(new KnuthPenalty(0, 1000, false, new NonLeafPosition(this, (Position)null), false));
         }

         if (this.bpUnit > 0) {
            returnList.add(new KnuthGlue(0, 0, 0, Adjustment.SPACE_AFTER_ADJUSTMENT, new NonLeafPosition(this, (Position)null), spaceAfterIsConditional));
         } else {
            returnList.add(new KnuthGlue(this.adjustedSpaceAfter, 0, 0, Adjustment.SPACE_AFTER_ADJUSTMENT, new NonLeafPosition(this, (Position)null), spaceAfterIsConditional));
         }

         if (!spaceAfterIsConditional) {
            returnList.add(new KnuthBox(0, new NonLeafPosition(this, (Position)null), true));
         }
      }

      return returnList;
   }

   protected Keep getParentKeepTogether() {
      Keep keep = Keep.KEEP_AUTO;
      if (this.getParent() instanceof BlockLevelLayoutManager) {
         keep = ((BlockLevelLayoutManager)this.getParent()).getKeepTogether();
      } else if (this.getParent() instanceof InlineLayoutManager && ((InlineLayoutManager)this.getParent()).mustKeepTogether()) {
         keep = Keep.KEEP_ALWAYS;
      }

      return keep;
   }

   public boolean mustKeepTogether() {
      return !this.getKeepTogether().isAuto();
   }

   public boolean mustKeepWithPrevious() {
      return !this.getKeepWithPrevious().isAuto();
   }

   public boolean mustKeepWithNext() {
      return !this.getKeepWithNext().isAuto();
   }

   public Keep getKeepTogether() {
      Keep keep = Keep.getKeep(this.getKeepTogetherProperty());
      keep = keep.compare(this.getParentKeepTogether());
      return keep;
   }

   public Keep getKeepWithPrevious() {
      return Keep.getKeep(this.getKeepWithPreviousProperty());
   }

   public Keep getKeepWithNext() {
      return Keep.getKeep(this.getKeepWithNextProperty());
   }

   public KeepProperty getKeepTogetherProperty() {
      throw new IllegalStateException();
   }

   public KeepProperty getKeepWithPreviousProperty() {
      throw new IllegalStateException();
   }

   public KeepProperty getKeepWithNextProperty() {
      throw new IllegalStateException();
   }

   protected void addPendingMarks(LayoutContext context) {
      CommonBorderPaddingBackground borderAndPadding = this.getBorderPaddingBackground();
      if (borderAndPadding != null) {
         if (borderAndPadding.getBorderBeforeWidth(false) > 0) {
            context.addPendingBeforeMark(new BorderElement(this.getAuxiliaryPosition(), borderAndPadding.getBorderInfo(0).getWidth(), RelSide.BEFORE, false, false, this));
         }

         if (borderAndPadding.getPaddingBefore(false, this) > 0) {
            context.addPendingBeforeMark(new PaddingElement(this.getAuxiliaryPosition(), borderAndPadding.getPaddingLengthProperty(0), RelSide.BEFORE, false, false, this));
         }

         if (borderAndPadding.getBorderAfterWidth(false) > 0) {
            context.addPendingAfterMark(new BorderElement(this.getAuxiliaryPosition(), borderAndPadding.getBorderInfo(1).getWidth(), RelSide.AFTER, false, false, this));
         }

         if (borderAndPadding.getPaddingAfter(false, this) > 0) {
            context.addPendingAfterMark(new PaddingElement(this.getAuxiliaryPosition(), borderAndPadding.getPaddingLengthProperty(1), RelSide.AFTER, false, false, this));
         }
      }

   }

   private CommonBorderPaddingBackground getBorderPaddingBackground() {
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         return ((org.apache.fop.fo.flow.Block)this.fobj).getCommonBorderPaddingBackground();
      } else if (this.fobj instanceof BlockContainer) {
         return ((BlockContainer)this.fobj).getCommonBorderPaddingBackground();
      } else if (this.fobj instanceof ListBlock) {
         return ((ListBlock)this.fobj).getCommonBorderPaddingBackground();
      } else if (this.fobj instanceof ListItem) {
         return ((ListItem)this.fobj).getCommonBorderPaddingBackground();
      } else {
         return this.fobj instanceof Table ? ((Table)this.fobj).getCommonBorderPaddingBackground() : null;
      }
   }

   private SpaceProperty getSpaceBeforeProperty() {
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         return ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceBefore;
      } else if (this.fobj instanceof BlockContainer) {
         return ((BlockContainer)this.fobj).getCommonMarginBlock().spaceBefore;
      } else if (this.fobj instanceof ListBlock) {
         return ((ListBlock)this.fobj).getCommonMarginBlock().spaceBefore;
      } else if (this.fobj instanceof ListItem) {
         return ((ListItem)this.fobj).getCommonMarginBlock().spaceBefore;
      } else {
         return this.fobj instanceof Table ? ((Table)this.fobj).getCommonMarginBlock().spaceBefore : null;
      }
   }

   private SpaceProperty getSpaceAfterProperty() {
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         return ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceAfter;
      } else if (this.fobj instanceof BlockContainer) {
         return ((BlockContainer)this.fobj).getCommonMarginBlock().spaceAfter;
      } else if (this.fobj instanceof ListBlock) {
         return ((ListBlock)this.fobj).getCommonMarginBlock().spaceAfter;
      } else if (this.fobj instanceof ListItem) {
         return ((ListItem)this.fobj).getCommonMarginBlock().spaceAfter;
      } else {
         return this.fobj instanceof Table ? ((Table)this.fobj).getCommonMarginBlock().spaceAfter : null;
      }
   }

   protected void addKnuthElementsForBorderPaddingBefore(List returnList, boolean isFirst) {
      CommonBorderPaddingBackground borderAndPadding = this.getBorderPaddingBackground();
      if (borderAndPadding != null) {
         if (borderAndPadding.getBorderBeforeWidth(false) > 0) {
            returnList.add(new BorderElement(this.getAuxiliaryPosition(), borderAndPadding.getBorderInfo(0).getWidth(), RelSide.BEFORE, isFirst, false, this));
         }

         if (borderAndPadding.getPaddingBefore(false, this) > 0) {
            returnList.add(new PaddingElement(this.getAuxiliaryPosition(), borderAndPadding.getPaddingLengthProperty(0), RelSide.BEFORE, isFirst, false, this));
         }
      }

   }

   protected void addKnuthElementsForBorderPaddingAfter(List returnList, boolean isLast) {
      CommonBorderPaddingBackground borderAndPadding = this.getBorderPaddingBackground();
      if (borderAndPadding != null) {
         if (borderAndPadding.getPaddingAfter(false, this) > 0) {
            returnList.add(new PaddingElement(this.getAuxiliaryPosition(), borderAndPadding.getPaddingLengthProperty(1), RelSide.AFTER, false, isLast, this));
         }

         if (borderAndPadding.getBorderAfterWidth(false) > 0) {
            returnList.add(new BorderElement(this.getAuxiliaryPosition(), borderAndPadding.getBorderInfo(1).getWidth(), RelSide.AFTER, false, isLast, this));
         }
      }

   }

   protected boolean addKnuthElementsForBreakBefore(List returnList, LayoutContext context) {
      int breakBefore = this.getBreakBefore();
      if (breakBefore != 104 && breakBefore != 28 && breakBefore != 44 && breakBefore != 100) {
         return false;
      } else {
         returnList.add(new BreakElement(this.getAuxiliaryPosition(), 0, -1000, breakBefore, context));
         return true;
      }
   }

   private int getBreakBefore() {
      int breakBefore = 9;
      if (this.fobj instanceof BreakPropertySet) {
         breakBefore = ((BreakPropertySet)this.fobj).getBreakBefore();
      }

      LayoutManager lm = this.getChildLM();
      if (lm instanceof BlockStackingLayoutManager) {
         BlockStackingLayoutManager bslm = (BlockStackingLayoutManager)lm;
         breakBefore = BreakUtil.compareBreakClasses(breakBefore, bslm.getBreakBefore());
      }

      return breakBefore;
   }

   protected boolean addKnuthElementsForBreakAfter(List returnList, LayoutContext context) {
      int breakAfter = -1;
      if (this.fobj instanceof BreakPropertySet) {
         breakAfter = ((BreakPropertySet)this.fobj).getBreakAfter();
      }

      if (breakAfter != 104 && breakAfter != 28 && breakAfter != 44 && breakAfter != 100) {
         return false;
      } else {
         returnList.add(new BreakElement(this.getAuxiliaryPosition(), 0, -1000, breakAfter, context));
         return true;
      }
   }

   protected void addKnuthElementsForSpaceBefore(List returnList, int alignment) {
      SpaceProperty spaceBefore = this.getSpaceBeforeProperty();
      if (spaceBefore != null && (spaceBefore.getMinimum(this).getLength().getValue(this) != 0 || spaceBefore.getMaximum(this).getLength().getValue(this) != 0)) {
         returnList.add(new SpaceElement(this.getAuxiliaryPosition(), spaceBefore, RelSide.BEFORE, true, false, this));
      }

   }

   protected void addKnuthElementsForSpaceAfter(List returnList, int alignment) {
      SpaceProperty spaceAfter = this.getSpaceAfterProperty();
      if (spaceAfter != null && (spaceAfter.getMinimum(this).getLength().getValue(this) != 0 || spaceAfter.getMaximum(this).getLength().getValue(this) != 0)) {
         returnList.add(new SpaceElement(this.getAuxiliaryPosition(), spaceAfter, RelSide.AFTER, false, true, this));
      }

   }

   protected List createUnitElements(List oldList) {
      LayoutManager lm = ((KnuthElement)oldList.get(0)).getLayoutManager();
      boolean bAddedBoxBefore = false;
      boolean bAddedBoxAfter = false;
      if (this.adjustedSpaceBefore > 0) {
         oldList.add(0, new KnuthBox(this.adjustedSpaceBefore, new Position(lm), true));
         bAddedBoxBefore = true;
      }

      if (this.adjustedSpaceAfter > 0) {
         oldList.add(new KnuthBox(this.adjustedSpaceAfter, new Position(lm), true));
         bAddedBoxAfter = true;
      }

      MinOptMax totalLength = MinOptMax.ZERO;
      LinkedList newList = new LinkedList();
      ListIterator oldListIterator = oldList.listIterator();

      while(oldListIterator.hasNext()) {
         KnuthElement element = (KnuthElement)oldListIterator.next();
         if (element.isBox()) {
            totalLength = totalLength.plus(element.getWidth());
         } else if (element.isGlue()) {
            totalLength = totalLength.minusMin(element.getShrink());
            totalLength = totalLength.plusMax(element.getStretch());
         }
      }

      MinOptMax totalUnits = MinOptMax.getInstance(this.neededUnits(totalLength.getMin()), this.neededUnits(totalLength.getOpt()), this.neededUnits(totalLength.getMax()));
      oldListIterator = oldList.listIterator();
      MinOptMax lengthBeforeBreak = MinOptMax.ZERO;
      MinOptMax lengthAfterBreak = totalLength;
      MinOptMax unsuppressibleUnits = MinOptMax.ZERO;
      int firstIndex = 0;
      int lastIndex = -1;

      int uShrinkChange;
      int uLengthChange;
      while(oldListIterator.hasNext()) {
         KnuthElement element = (KnuthElement)oldListIterator.next();
         ++lastIndex;
         boolean prevIsBox;
         if (element.isBox()) {
            lengthBeforeBreak = lengthBeforeBreak.plus(element.getWidth());
            lengthAfterBreak = lengthAfterBreak.minus(element.getWidth());
            prevIsBox = true;
         } else if (element.isGlue()) {
            lengthBeforeBreak = lengthBeforeBreak.minusMin(element.getShrink());
            lengthAfterBreak = lengthAfterBreak.plusMin(element.getShrink());
            lengthBeforeBreak = lengthBeforeBreak.plusMax(element.getStretch());
            lengthAfterBreak = lengthAfterBreak.minusMax(element.getStretch());
            prevIsBox = false;
         } else {
            lengthBeforeBreak = lengthBeforeBreak.plus(element.getWidth());
            prevIsBox = false;
         }

         if (element.isPenalty() && element.getPenalty() < 1000 || element.isGlue() && prevIsBox || !oldListIterator.hasNext()) {
            int iStepsForward = 0;

            while(oldListIterator.hasNext()) {
               KnuthElement el = (KnuthElement)oldListIterator.next();
               ++iStepsForward;
               if (el.isGlue()) {
                  lengthAfterBreak = lengthAfterBreak.plusMin(el.getShrink());
                  lengthAfterBreak = lengthAfterBreak.minusMax(el.getStretch());
               } else {
                  if (el.isPenalty()) {
                     continue;
                  }
                  break;
               }
            }

            MinOptMax unitsBeforeBreak = MinOptMax.getInstance(this.neededUnits(lengthBeforeBreak.getMin()), this.neededUnits(lengthBeforeBreak.getOpt()), this.neededUnits(lengthBeforeBreak.getMax()));
            MinOptMax unitsAfterBreak = MinOptMax.getInstance(this.neededUnits(lengthAfterBreak.getMin()), this.neededUnits(lengthAfterBreak.getOpt()), this.neededUnits(lengthAfterBreak.getMax()));

            for(uLengthChange = 0; uLengthChange < iStepsForward; ++uLengthChange) {
               KnuthElement el = (KnuthElement)oldListIterator.previous();
               if (el.isGlue()) {
                  lengthAfterBreak = lengthAfterBreak.minusMin(el.getShrink());
                  lengthAfterBreak = lengthAfterBreak.plusMax(el.getStretch());
               }
            }

            uLengthChange = unitsBeforeBreak.getOpt() + unitsAfterBreak.getOpt() - totalUnits.getOpt();
            int uStretchChange = unitsBeforeBreak.getStretch() + unitsAfterBreak.getStretch() - totalUnits.getStretch();
            uShrinkChange = unitsBeforeBreak.getShrink() + unitsAfterBreak.getShrink() - totalUnits.getShrink();
            int uNewNormal = unitsBeforeBreak.getOpt() - unsuppressibleUnits.getOpt();
            int uNewStretch = unitsBeforeBreak.getStretch() - unsuppressibleUnits.getStretch();
            int uNewShrink = unitsBeforeBreak.getShrink() - unsuppressibleUnits.getShrink();
            int firstIndexCorrection = 0;
            int lastIndexCorrection = 0;
            if (bAddedBoxBefore) {
               if (firstIndex != 0) {
                  ++firstIndexCorrection;
               }

               ++lastIndexCorrection;
            }

            if (bAddedBoxAfter && lastIndex == oldList.size() - 1) {
               ++lastIndexCorrection;
            }

            MappingPosition mappingPos = new MappingPosition(this, firstIndex - firstIndexCorrection, lastIndex - lastIndexCorrection);
            newList.add(new KnuthBox((uNewNormal - uLengthChange) * this.bpUnit, mappingPos, false));
            unsuppressibleUnits = unsuppressibleUnits.plus(uNewNormal - uLengthChange);
            if (uNewStretch - uStretchChange > 0 || uNewShrink - uShrinkChange > 0) {
               int iStretchUnits = uNewStretch - uStretchChange > 0 ? uNewStretch - uStretchChange : 0;
               int iShrinkUnits = uNewShrink - uShrinkChange > 0 ? uNewShrink - uShrinkChange : 0;
               newList.add(new KnuthPenalty(0, 1000, false, mappingPos, false));
               newList.add(new KnuthGlue(0, iStretchUnits * this.bpUnit, iShrinkUnits * this.bpUnit, Adjustment.LINE_NUMBER_ADJUSTMENT, mappingPos, false));
               unsuppressibleUnits = unsuppressibleUnits.plusMax(iStretchUnits);
               unsuppressibleUnits = unsuppressibleUnits.minusMin(iShrinkUnits);
               if (!oldListIterator.hasNext()) {
                  newList.add(new KnuthBox(0, mappingPos, false));
               }
            }

            if (uStretchChange == 0 && uShrinkChange == 0) {
               if (oldListIterator.hasNext()) {
                  newList.add(new KnuthPenalty(uLengthChange * this.bpUnit, 0, false, mappingPos, false));
               }
            } else {
               newList.add(new KnuthPenalty(0, 1000, false, mappingPos, false));
               newList.add(new KnuthGlue(0, uStretchChange * this.bpUnit, uShrinkChange * this.bpUnit, Adjustment.LINE_NUMBER_ADJUSTMENT, mappingPos, false));
               newList.add(new KnuthPenalty(uLengthChange * this.bpUnit, 0, false, element.getPosition(), false));
               newList.add(new KnuthGlue(0, -uStretchChange * this.bpUnit, -uShrinkChange * this.bpUnit, Adjustment.LINE_NUMBER_ADJUSTMENT, mappingPos, false));
            }

            firstIndex = lastIndex + 1;
         }

         if (element.isPenalty()) {
            lengthBeforeBreak = lengthBeforeBreak.minus(element.getWidth());
         }
      }

      if (this.adjustedSpaceBefore > 0) {
         oldList.remove(0);
      }

      if (this.adjustedSpaceAfter > 0) {
         ListUtil.removeLast(oldList);
      }

      boolean correctFirstElement = false;
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         correctFirstElement = ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceBefore.getSpace().isDiscard();
      }

      if (correctFirstElement) {
         KnuthBox wrongBox = (KnuthBox)newList.removeFirst();
         uLengthChange = (this.neededUnits(totalLength.getOpt()) - this.neededUnits(totalLength.getOpt() - this.adjustedSpaceBefore)) * this.bpUnit;
         newList.addFirst(new KnuthBox(wrongBox.getWidth() - uLengthChange, wrongBox.getPosition(), false));
         newList.addFirst(new KnuthGlue(uLengthChange, 0, 0, Adjustment.SPACE_BEFORE_ADJUSTMENT, wrongBox.getPosition(), false));
      }

      boolean correctLastElement = false;
      if (this.fobj instanceof org.apache.fop.fo.flow.Block) {
         correctLastElement = ((org.apache.fop.fo.flow.Block)this.fobj).getCommonMarginBlock().spaceAfter.getSpace().isDiscard();
      }

      if (correctLastElement) {
         KnuthBox wrongBox = (KnuthBox)newList.removeLast();
         LinkedList preserveList = new LinkedList();
         if (wrongBox.getWidth() == 0) {
            preserveList.add(wrongBox);
            preserveList.addFirst((KnuthGlue)newList.removeLast());
            preserveList.addFirst((KnuthPenalty)newList.removeLast());
            wrongBox = (KnuthBox)newList.removeLast();
         }

         uShrinkChange = (this.neededUnits(totalLength.getOpt()) - this.neededUnits(totalLength.getOpt() - this.adjustedSpaceAfter)) * this.bpUnit;
         newList.addLast(new KnuthBox(wrongBox.getWidth() - uShrinkChange, wrongBox.getPosition(), false));
         if (!preserveList.isEmpty()) {
            newList.addAll(preserveList);
         }

         newList.addLast(new KnuthGlue(uShrinkChange, 0, 0, Adjustment.SPACE_AFTER_ADJUSTMENT, wrongBox.getPosition(), false));
      }

      return newList;
   }

   protected void wrapPositionElements(List sourceList, List targetList) {
      this.wrapPositionElements(sourceList, targetList, false);
   }

   protected void wrapPositionElements(List sourceList, List targetList, boolean force) {
      ListIterator listIter = sourceList.listIterator();

      while(listIter.hasNext()) {
         Object tempElement = listIter.next();
         if (tempElement instanceof ListElement) {
            this.wrapPositionElement((ListElement)tempElement, targetList, force);
         } else if (tempElement instanceof List) {
            this.wrapPositionElements((List)tempElement, targetList, force);
         }
      }

   }

   protected void wrapPositionElement(ListElement el, List targetList, boolean force) {
      if (force || el.getLayoutManager() != this) {
         el.setPosition(this.notifyPos(new NonLeafPosition(this, el.getPosition())));
      }

      targetList.add(el);
   }

   protected int getIPIndents() {
      return this.startIndent + this.endIndent;
   }

   public int getContentAreaIPD() {
      return this.contentAreaIPD;
   }

   protected void setContentAreaIPD(int contentAreaIPD) {
      this.contentAreaIPD = contentAreaIPD;
   }

   public int getContentAreaBPD() {
      return -1;
   }

   public void reset() {
      super.reset();
      this.breakBeforeServed = false;
      this.firstVisibleMarkServed = false;
   }

   static {
      $assertionsDisabled = !BlockStackingLayoutManager.class.desiredAssertionStatus();
      log = LogFactory.getLog(BlockStackingLayoutManager.class);
   }

   protected static class MappingPosition extends Position {
      private int iFirstIndex;
      private int iLastIndex;

      public MappingPosition(LayoutManager lm, int first, int last) {
         super(lm);
         this.iFirstIndex = first;
         this.iLastIndex = last;
      }

      public int getFirstIndex() {
         return this.iFirstIndex;
      }

      public int getLastIndex() {
         return this.iLastIndex;
      }
   }

   protected static class StackingIter extends PositionIterator {
      StackingIter(Iterator parentIter) {
         super(parentIter);
      }

      protected LayoutManager getLM(Object nextObj) {
         return ((Position)nextObj).getLM();
      }

      protected Position getPos(Object nextObj) {
         return (Position)nextObj;
      }
   }
}
