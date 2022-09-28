package org.apache.fop.layoutmgr;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.BlockParent;
import org.apache.fop.fo.pagination.Flow;

public class FlowLayoutManager extends BlockStackingLayoutManager implements BlockLevelLayoutManager {
   private static Log log;
   private BlockParent[] currentAreas = new BlockParent[6];
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public FlowLayoutManager(PageSequenceLayoutManager pslm, Flow node) {
      super(node);
      this.setParent(pslm);
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      List elements = new LinkedList();

      LayoutManager currentChildLM;
      do {
         if ((currentChildLM = this.getChildLM()) == null) {
            SpaceResolver.resolveElementList(elements);
            this.setFinished(true);
            if (!$assertionsDisabled && elements.isEmpty()) {
               throw new AssertionError();
            }

            return elements;
         }
      } while(this.addChildElements(elements, currentChildLM, context, alignment) == null);

      return elements;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment, Position positionAtIPDChange, LayoutManager restartAtLM) {
      List elements = new LinkedList();
      LayoutManager currentChildLM = positionAtIPDChange.getLM();
      if (currentChildLM == null) {
         throw new IllegalStateException("Cannot find layout manager from where to re-start layout after IPD change");
      } else {
         if (restartAtLM != null && restartAtLM.getParent() == this) {
            this.setCurrentChildLM(restartAtLM);
            restartAtLM.reset();
            if (this.addChildElements(elements, restartAtLM, context, alignment) != null) {
               return elements;
            }
         } else {
            Stack lmStack;
            for(lmStack = new Stack(); currentChildLM.getParent() != this; currentChildLM = currentChildLM.getParent()) {
               lmStack.push(currentChildLM);
            }

            this.setCurrentChildLM(currentChildLM);
            if (this.addChildElements(elements, currentChildLM, context, alignment, lmStack, positionAtIPDChange, restartAtLM) != null) {
               return elements;
            }
         }

         do {
            if ((currentChildLM = this.getChildLM()) == null) {
               SpaceResolver.resolveElementList(elements);
               this.setFinished(true);
               if (!$assertionsDisabled && elements.isEmpty()) {
                  throw new AssertionError();
               }

               return elements;
            }

            currentChildLM.reset();
         } while(this.addChildElements(elements, currentChildLM, context, alignment) == null);

         return elements;
      }
   }

   private List addChildElements(List elements, LayoutManager childLM, LayoutContext context, int alignment) {
      return this.addChildElements(elements, childLM, context, alignment, (Stack)null, (Position)null, (LayoutManager)null);
   }

   private List addChildElements(List elements, LayoutManager childLM, LayoutContext context, int alignment, Stack lmStack, Position position, LayoutManager restartAtLM) {
      if (this.handleSpanChange(childLM, elements, context)) {
         SpaceResolver.resolveElementList(elements);
         return elements;
      } else {
         LayoutContext childLC = new LayoutContext(0);
         List childrenElements = this.getNextChildElements(childLM, context, childLC, alignment, lmStack, position, restartAtLM);
         if (elements.isEmpty()) {
            context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
         }

         if (!elements.isEmpty() && !ElementListUtils.startsWithForcedBreak(childrenElements)) {
            this.addInBetweenBreak(elements, context, childLC);
         }

         context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
         elements.addAll(childrenElements);
         if (ElementListUtils.endsWithForcedBreak(elements)) {
            if (childLM.isFinished() && !this.hasNextChildLM()) {
               this.setFinished(true);
            }

            SpaceResolver.resolveElementList(elements);
            return elements;
         } else {
            return null;
         }
      }
   }

   private boolean handleSpanChange(LayoutManager childLM, List elements, LayoutContext context) {
      int span = 95;
      int disableColumnBalancing = 48;
      if (childLM instanceof BlockLayoutManager) {
         span = ((BlockLayoutManager)childLM).getBlockFO().getSpan();
         disableColumnBalancing = ((BlockLayoutManager)childLM).getBlockFO().getDisableColumnBalancing();
      } else if (childLM instanceof BlockContainerLayoutManager) {
         span = ((BlockContainerLayoutManager)childLM).getBlockContainerFO().getSpan();
         disableColumnBalancing = ((BlockContainerLayoutManager)childLM).getBlockContainerFO().getDisableColumnBalancing();
      }

      int currentSpan = context.getCurrentSpan();
      if (currentSpan != span) {
         if (span == 5) {
            context.setDisableColumnBalancing(disableColumnBalancing);
         }

         log.debug("span change from " + currentSpan + " to " + span);
         context.signalSpanChange(span);
         return true;
      } else {
         return false;
      }
   }

   private List getNextChildElements(LayoutManager childLM, LayoutContext context, LayoutContext childLC, int alignment, Stack lmStack, Position restartPosition, LayoutManager restartLM) {
      childLC.setStackLimitBP(context.getStackLimitBP());
      childLC.setRefIPD(context.getRefIPD());
      childLC.setWritingMode(this.getCurrentPage().getSimplePageMaster().getWritingMode());
      List childrenElements;
      if (lmStack == null) {
         childrenElements = childLM.getNextKnuthElements(childLC, alignment);
      } else {
         childrenElements = childLM.getNextKnuthElements(childLC, alignment, lmStack, restartPosition, restartLM);
      }

      if (!$assertionsDisabled && childrenElements.isEmpty()) {
         throw new AssertionError();
      } else {
         List tempList = childrenElements;
         List childrenElements = new LinkedList();
         this.wrapPositionElements(tempList, childrenElements);
         return childrenElements;
      }
   }

   public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
      log.debug(" FLM.negotiateBPDAdjustment> " + adj);
      if (lastElement.getPosition() instanceof NonLeafPosition) {
         NonLeafPosition savedPos = (NonLeafPosition)lastElement.getPosition();
         lastElement.setPosition(savedPos.getPosition());
         int returnValue = ((BlockLevelLayoutManager)lastElement.getLayoutManager()).negotiateBPDAdjustment(adj, lastElement);
         lastElement.setPosition(savedPos);
         log.debug(" FLM.negotiateBPDAdjustment> result " + returnValue);
         return returnValue;
      } else {
         return 0;
      }
   }

   public void discardSpace(KnuthGlue spaceGlue) {
      log.debug(" FLM.discardSpace> ");
      if (spaceGlue.getPosition() instanceof NonLeafPosition) {
         NonLeafPosition savedPos = (NonLeafPosition)spaceGlue.getPosition();
         spaceGlue.setPosition(savedPos.getPosition());
         ((BlockLevelLayoutManager)spaceGlue.getLayoutManager()).discardSpace(spaceGlue);
         spaceGlue.setPosition(savedPos);
      }

   }

   public Keep getKeepTogether() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      ListIterator oldListIterator = oldList.listIterator();
      List returnedList = new LinkedList();
      List returnList = new LinkedList();
      KnuthElement prevElement = null;
      KnuthElement currElement = null;
      int fromIndex = 0;

      while(oldListIterator.hasNext()) {
         KnuthElement oldElement = (KnuthElement)oldListIterator.next();
         if (oldElement.getPosition() instanceof NonLeafPosition) {
            oldElement.setPosition(oldElement.getPosition().getPosition());
         } else {
            oldListIterator.remove();
         }
      }

      BlockLevelLayoutManager prevLM;
      for(oldListIterator = oldList.listIterator(); oldListIterator.hasNext(); prevElement = currElement) {
         currElement = (KnuthElement)oldListIterator.next();
         if (prevElement != null && prevElement.getLayoutManager() != currElement.getLayoutManager()) {
            prevLM = (BlockLevelLayoutManager)prevElement.getLayoutManager();
            BlockLevelLayoutManager currLM = (BlockLevelLayoutManager)currElement.getLayoutManager();
            returnedList.addAll(prevLM.getChangedKnuthElements(oldList.subList(fromIndex, oldListIterator.previousIndex()), alignment));
            fromIndex = oldListIterator.previousIndex();
            if (!prevLM.mustKeepWithNext() && !currLM.mustKeepWithPrevious()) {
               if (!((KnuthElement)returnedList.get(returnedList.size() - 1)).isGlue()) {
                  returnedList.add(new KnuthPenalty(0, 0, false, new Position(this), false));
               }
            } else {
               returnedList.add(new KnuthPenalty(0, 1000, false, new Position(this), false));
            }
         }
      }

      if (currElement != null) {
         prevLM = (BlockLevelLayoutManager)currElement.getLayoutManager();
         returnedList.addAll(prevLM.getChangedKnuthElements(oldList.subList(fromIndex, oldList.size()), alignment));
      }

      KnuthElement returnedElement;
      for(ListIterator listIter = returnedList.listIterator(); listIter.hasNext(); returnList.add(returnedElement)) {
         returnedElement = (KnuthElement)listIter.next();
         if (returnedElement.getLayoutManager() != this) {
            returnedElement.setPosition(new NonLeafPosition(this, returnedElement.getPosition()));
         }
      }

      return returnList;
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      AreaAdditionUtil.addAreas(this, parentIter, layoutContext);
      this.flush();
   }

   public void addChildArea(Area childArea) {
      this.getParentArea(childArea);
      this.addChildToArea(childArea, this.currentAreas[childArea.getAreaClass()]);
   }

   public Area getParentArea(Area childArea) {
      BlockParent parentArea = null;
      int aclass = childArea.getAreaClass();
      if (aclass == 0) {
         parentArea = this.getCurrentPV().getCurrentFlow();
      } else if (aclass == 3) {
         parentArea = this.getCurrentPV().getBodyRegion().getBeforeFloat();
      } else {
         if (aclass != 4) {
            throw new IllegalStateException("(internal error) Invalid area class (" + aclass + ") requested.");
         }

         parentArea = this.getCurrentPV().getBodyRegion().getFootnote();
      }

      this.currentAreas[aclass] = (BlockParent)parentArea;
      this.setCurrentArea((BlockParent)parentArea);
      return (Area)parentArea;
   }

   public int getContentAreaIPD() {
      return this.getCurrentPV().getCurrentSpan().getColumnWidth();
   }

   public int getContentAreaBPD() {
      return this.getCurrentPV().getBodyRegion().getBPD();
   }

   public boolean isRestartable() {
      return true;
   }

   static {
      $assertionsDisabled = !FlowLayoutManager.class.desiredAssertionStatus();
      log = LogFactory.getLog(FlowLayoutManager.class);
   }
}
