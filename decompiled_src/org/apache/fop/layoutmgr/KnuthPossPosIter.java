package org.apache.fop.layoutmgr;

import java.util.List;

public class KnuthPossPosIter extends PositionIterator {
   private int iterCount;

   public KnuthPossPosIter(List elementList, int startPos, int endPos) {
      super(elementList.listIterator(startPos));
      this.iterCount = endPos - startPos;
   }

   public KnuthPossPosIter(List elementList) {
      this(elementList, 0, elementList.size());
   }

   protected boolean checkNext() {
      if (this.iterCount > 0) {
         return super.checkNext();
      } else {
         this.endIter();
         return false;
      }
   }

   public Object next() {
      --this.iterCount;
      return super.next();
   }

   public ListElement getKE() {
      return (ListElement)this.peekNext();
   }

   protected LayoutManager getLM(Object nextObj) {
      return ((ListElement)nextObj).getLayoutManager();
   }

   protected Position getPos(Object nextObj) {
      return ((ListElement)nextObj).getPosition();
   }
}
