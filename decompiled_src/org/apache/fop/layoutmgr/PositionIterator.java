package org.apache.fop.layoutmgr;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class PositionIterator implements Iterator {
   private Iterator parentIter;
   private Object nextObj;
   private LayoutManager childLM;
   private boolean bHasNext;

   protected PositionIterator(Iterator pIter) {
      this.parentIter = pIter;
      this.lookAhead();
   }

   public LayoutManager getNextChildLM() {
      if (this.childLM == null && this.nextObj != null) {
         this.childLM = this.getLM(this.nextObj);
         this.bHasNext = true;
      }

      return this.childLM;
   }

   protected abstract LayoutManager getLM(Object var1);

   protected abstract Position getPos(Object var1);

   private void lookAhead() {
      if (this.parentIter.hasNext()) {
         this.bHasNext = true;
         this.nextObj = this.parentIter.next();
      } else {
         this.endIter();
      }

   }

   protected boolean checkNext() {
      LayoutManager lm = this.getLM(this.nextObj);
      if (this.childLM == null) {
         this.childLM = lm;
      } else if (this.childLM != lm && lm != null) {
         this.bHasNext = false;
         this.childLM = null;
         return false;
      }

      return true;
   }

   protected void endIter() {
      this.bHasNext = false;
      this.nextObj = null;
      this.childLM = null;
   }

   public boolean hasNext() {
      return this.bHasNext && this.checkNext();
   }

   public Object next() throws NoSuchElementException {
      if (this.bHasNext) {
         Object retObj = this.getPos(this.nextObj);
         this.lookAhead();
         return retObj;
      } else {
         throw new NoSuchElementException("PosIter");
      }
   }

   public Object peekNext() {
      return this.nextObj;
   }

   public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("PositionIterator doesn't support remove");
   }
}
