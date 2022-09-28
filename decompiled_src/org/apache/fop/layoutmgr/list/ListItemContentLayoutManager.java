package org.apache.fop.layoutmgr.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.fo.flow.AbstractListItemPart;
import org.apache.fop.fo.flow.ListItemBody;
import org.apache.fop.fo.flow.ListItemLabel;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.NonLeafPosition;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.layoutmgr.TraitSetter;

public class ListItemContentLayoutManager extends BlockStackingLayoutManager {
   private Block curBlockArea;
   private int xoffset;
   private int itemIPD;

   public ListItemContentLayoutManager(ListItemLabel node) {
      super(node);
   }

   public ListItemContentLayoutManager(ListItemBody node) {
      super(node);
   }

   protected AbstractListItemPart getPartFO() {
      return (AbstractListItemPart)this.fobj;
   }

   public void setXOffset(int off) {
      this.xoffset = off;
   }

   public List getChangedKnuthElements(List oldList, int alignment) {
      return super.getChangedKnuthElements(oldList, alignment);
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      this.getParentArea((Area)null);
      this.addId();
      LayoutContext lc = new LayoutContext(0);
      LayoutManager firstLM = null;
      LayoutManager lastLM = null;
      Position firstPos = null;
      Position lastPos = null;
      LinkedList positionList = new LinkedList();

      while(parentIter.hasNext()) {
         Position pos = (Position)parentIter.next();
         if (pos != null) {
            if (pos.getIndex() >= 0) {
               if (firstPos == null) {
                  firstPos = pos;
               }

               lastPos = pos;
            }

            if (pos instanceof NonLeafPosition) {
               positionList.add(pos.getPosition());
               lastLM = pos.getPosition().getLM();
               if (firstLM == null) {
                  firstLM = lastLM;
               }
            } else if (pos instanceof SpaceResolver.SpaceHandlingBreakPosition) {
               positionList.add(pos);
            }
         }
      }

      this.addMarkersToPage(true, this.isFirst(firstPos), this.isLast(lastPos));
      StackingIter childPosIter = new StackingIter(positionList.listIterator());

      LayoutManager childLM;
      while((childLM = childPosIter.getNextChildLM()) != null) {
         lc.setFlags(32, childLM == firstLM);
         lc.setFlags(128, childLM == lastLM);
         lc.setSpaceAdjust(layoutContext.getSpaceAdjust());
         lc.setStackLimitBP(layoutContext.getStackLimitBP());
         childLM.addAreas(childPosIter, lc);
      }

      this.addMarkersToPage(false, this.isFirst(firstPos), this.isLast(lastPos));
      this.flush();
      this.curBlockArea = null;
      this.checkEndOfLayout(lastPos);
   }

   public Area getParentArea(Area childArea) {
      if (this.curBlockArea == null) {
         this.curBlockArea = new Block();
         this.curBlockArea.setPositioning(2);
         this.curBlockArea.setXOffset(this.xoffset);
         this.curBlockArea.setIPD(this.itemIPD);
         TraitSetter.setProducerID(this.curBlockArea, this.getPartFO().getId());
         Area parentArea = this.parentLayoutManager.getParentArea(this.curBlockArea);
         int referenceIPD = parentArea.getIPD();
         this.curBlockArea.setIPD(referenceIPD);
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
      return this.getPartFO().getKeepTogether();
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }

   private static class StackingIter extends PositionIterator {
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
