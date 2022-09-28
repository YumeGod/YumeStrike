package org.apache.fop.layoutmgr.inline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.fop.area.Area;
import org.apache.fop.area.inline.Space;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.properties.SpaceProperty;
import org.apache.fop.layoutmgr.AbstractLayoutManager;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.traits.MinOptMax;

public abstract class InlineStackingLayoutManager extends AbstractLayoutManager implements InlineLevelLayoutManager {
   protected MinOptMax extraBPD;
   private Area currentArea;
   protected LayoutContext childLC;

   protected InlineStackingLayoutManager(FObj node) {
      super(node);
      this.extraBPD = MinOptMax.ZERO;
   }

   public void setLMiter(ListIterator iter) {
      this.childLMiter = iter;
   }

   protected MinOptMax getExtraIPD(boolean bNotFirst, boolean bNotLast) {
      return MinOptMax.ZERO;
   }

   protected boolean hasLeadingFence(boolean bNotFirst) {
      return false;
   }

   protected boolean hasTrailingFence(boolean bNotLast) {
      return false;
   }

   protected SpaceProperty getSpaceStart() {
      return null;
   }

   protected SpaceProperty getSpaceEnd() {
      return null;
   }

   protected Area getCurrentArea() {
      return this.currentArea;
   }

   protected void setCurrentArea(Area area) {
      this.currentArea = area;
   }

   protected void setTraits(boolean bNotFirst, boolean bNotLast) {
   }

   protected void setChildContext(LayoutContext lc) {
      this.childLC = lc;
   }

   protected LayoutContext getContext() {
      return this.childLC;
   }

   protected void addSpace(Area parentArea, MinOptMax spaceRange, double spaceAdjust) {
      if (spaceRange != null) {
         int iAdjust = spaceRange.getOpt();
         if (spaceAdjust > 0.0) {
            iAdjust += (int)((double)spaceRange.getStretch() * spaceAdjust);
         } else if (spaceAdjust < 0.0) {
            iAdjust += (int)((double)spaceRange.getShrink() * spaceAdjust);
         }

         if (iAdjust != 0) {
            Space ls = new Space();
            ls.setIPD(iAdjust);
            parentArea.addChildArea(ls);
         }
      }

   }

   public List addALetterSpaceTo(List oldList) {
      ListIterator oldListIterator = oldList.listIterator();
      KnuthElement element = null;

      while(oldListIterator.hasNext()) {
         element = (KnuthElement)oldListIterator.next();
         element.setPosition(element.getPosition().getPosition());
      }

      InlineLevelLayoutManager LM = (InlineLevelLayoutManager)element.getLayoutManager();
      if (LM != null) {
         oldList = LM.addALetterSpaceTo(oldList);
      }

      oldListIterator = oldList.listIterator();

      while(oldListIterator.hasNext()) {
         element = (KnuthElement)oldListIterator.next();
         element.setPosition(this.notifyPos(new NonLeafPosition(this, element.getPosition())));
      }

      return oldList;
   }

   public void removeWordSpace(List oldList) {
      ListIterator oldListIterator = oldList.listIterator();
      KnuthElement element = null;

      while(oldListIterator.hasNext()) {
         element = (KnuthElement)oldListIterator.next();
         element.setPosition(element.getPosition().getPosition());
      }

      ((InlineLevelLayoutManager)element.getLayoutManager()).removeWordSpace(oldList);
   }

   public String getWordChars(Position pos) {
      Position newPos = pos.getPosition();
      return ((InlineLevelLayoutManager)newPos.getLM()).getWordChars(newPos);
   }

   public void hyphenate(Position pos, HyphContext hc) {
      Position newPos = pos.getPosition();
      ((InlineLevelLayoutManager)newPos.getLM()).hyphenate(newPos, hc);
   }

   public boolean applyChanges(List oldList) {
      ListIterator oldListIterator = oldList.listIterator();

      KnuthElement oldElement;
      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         oldElement.setPosition(oldElement.getPosition().getPosition());
      }

      oldListIterator = oldList.listIterator();
      InlineLevelLayoutManager prevLM = null;
      int fromIndex = 0;
      boolean bSomethingChanged = false;

      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         InlineLevelLayoutManager currLM = (InlineLevelLayoutManager)oldElement.getLayoutManager();
         if (prevLM == null) {
            prevLM = currLM;
         }

         if (currLM != prevLM || !oldListIterator.hasNext()) {
            if (prevLM != this && currLM != this) {
               if (oldListIterator.hasNext()) {
                  bSomethingChanged = prevLM.applyChanges(oldList.subList(fromIndex, oldListIterator.previousIndex())) || bSomethingChanged;
                  prevLM = currLM;
                  fromIndex = oldListIterator.previousIndex();
               } else if (currLM == prevLM) {
                  bSomethingChanged = prevLM != null && prevLM.applyChanges(oldList.subList(fromIndex, oldList.size())) || bSomethingChanged;
               } else {
                  bSomethingChanged = prevLM.applyChanges(oldList.subList(fromIndex, oldListIterator.previousIndex())) || bSomethingChanged;
                  if (currLM != null) {
                     bSomethingChanged = currLM.applyChanges(oldList.subList(oldListIterator.previousIndex(), oldList.size())) || bSomethingChanged;
                  }
               }
            } else {
               prevLM = currLM;
            }
         }
      }

      oldListIterator = oldList.listIterator();

      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         oldElement.setPosition(this.notifyPos(new NonLeafPosition(this, oldElement.getPosition())));
      }

      return bSomethingChanged;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      ListIterator oldListIterator = oldList.listIterator();

      KnuthElement oldElement;
      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         oldElement.setPosition(oldElement.getPosition().getPosition());
      }

      oldListIterator = oldList.listIterator();
      LinkedList returnedList = new LinkedList();
      LinkedList returnList = new LinkedList();
      InlineLevelLayoutManager prevLM = null;
      int fromIndex = 0;

      while(oldListIterator.hasNext()) {
         oldElement = (KnuthElement)oldListIterator.next();
         InlineLevelLayoutManager currLM = (InlineLevelLayoutManager)oldElement.getLayoutManager();
         if (prevLM == null) {
            prevLM = currLM;
         }

         if (currLM != prevLM || !oldListIterator.hasNext()) {
            if (oldListIterator.hasNext()) {
               returnedList.addAll(prevLM.getChangedKnuthElements(oldList.subList(fromIndex, oldListIterator.previousIndex()), alignment));
               prevLM = currLM;
               fromIndex = oldListIterator.previousIndex();
            } else if (currLM == prevLM) {
               returnedList.addAll(prevLM.getChangedKnuthElements(oldList.subList(fromIndex, oldList.size()), alignment));
            } else {
               returnedList.addAll(prevLM.getChangedKnuthElements(oldList.subList(fromIndex, oldListIterator.previousIndex()), alignment));
               if (currLM != null) {
                  returnedList.addAll(currLM.getChangedKnuthElements(oldList.subList(oldListIterator.previousIndex(), oldList.size()), alignment));
               }
            }
         }
      }

      ListIterator listIter = returnedList.listIterator();

      while(listIter.hasNext()) {
         KnuthElement returnedElement = (KnuthElement)listIter.next();
         returnedElement.setPosition(this.notifyPos(new NonLeafPosition(this, returnedElement.getPosition())));
         returnList.add(returnedElement);
      }

      return returnList;
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
