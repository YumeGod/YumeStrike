package org.apache.fop.layoutmgr;

import java.util.Iterator;
import java.util.LinkedList;

public class AreaAdditionUtil {
   public static void addAreas(BlockStackingLayoutManager bslm, PositionIterator parentIter, LayoutContext layoutContext) {
      LayoutManager childLM = null;
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
               positionList.add(((NonLeafPosition)pos).getPosition());
               lastLM = ((NonLeafPosition)pos).getPosition().getLM();
               if (firstLM == null) {
                  firstLM = lastLM;
               }
            } else if (pos instanceof SpaceResolver.SpaceHandlingBreakPosition) {
               positionList.add(pos);
            }
         }
      }

      if (firstPos != null) {
         if (bslm != null) {
            bslm.addMarkersToPage(true, bslm.isFirst(firstPos), bslm.isLast(lastPos));
         }

         StackingIter childPosIter = new StackingIter(positionList.listIterator());

         while((childLM = childPosIter.getNextChildLM()) != null) {
            lc.setFlags(32, childLM == firstLM);
            lc.setFlags(128, childLM == lastLM);
            lc.setSpaceAdjust(layoutContext.getSpaceAdjust());
            lc.setSpaceBefore(childLM == firstLM ? layoutContext.getSpaceBefore() : 0);
            lc.setSpaceAfter(layoutContext.getSpaceAfter());
            lc.setStackLimitBP(layoutContext.getStackLimitBP());
            childLM.addAreas(childPosIter, lc);
         }

         if (bslm != null) {
            bslm.addMarkersToPage(false, bslm.isFirst(firstPos), bslm.isLast(lastPos));
         }

      }
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
