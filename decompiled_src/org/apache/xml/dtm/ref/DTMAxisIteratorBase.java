package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.utils.WrappedRuntimeException;

public abstract class DTMAxisIteratorBase implements DTMAxisIterator {
   protected int _last = -1;
   protected int _position = 0;
   protected int _markedNode;
   protected int _startNode = -1;
   protected boolean _includeSelf = false;
   protected boolean _isRestartable = true;

   public int getStartNode() {
      return this._startNode;
   }

   public DTMAxisIterator reset() {
      boolean temp = this._isRestartable;
      this._isRestartable = true;
      this.setStartNode(this._startNode);
      this._isRestartable = temp;
      return this;
   }

   public DTMAxisIterator includeSelf() {
      this._includeSelf = true;
      return this;
   }

   public int getLast() {
      if (this._last == -1) {
         int temp = this._position;
         this.setMark();
         this.reset();

         do {
            ++this._last;
         } while(this.next() != -1);

         this.gotoMark();
         this._position = temp;
      }

      return this._last;
   }

   public int getPosition() {
      return this._position == 0 ? 1 : this._position;
   }

   public boolean isReverse() {
      return false;
   }

   public DTMAxisIterator cloneIterator() {
      try {
         DTMAxisIteratorBase clone = (DTMAxisIteratorBase)super.clone();
         clone._isRestartable = false;
         return clone;
      } catch (CloneNotSupportedException var2) {
         throw new WrappedRuntimeException(var2);
      }
   }

   protected final int returnNode(int node) {
      ++this._position;
      return node;
   }

   protected final DTMAxisIterator resetPosition() {
      this._position = 0;
      return this;
   }

   public boolean isDocOrdered() {
      return true;
   }

   public int getAxis() {
      return -1;
   }

   public void setRestartable(boolean isRestartable) {
      this._isRestartable = isRestartable;
   }

   public int getNodeByPosition(int position) {
      if (position > 0) {
         int pos = this.isReverse() ? this.getLast() - position + 1 : position;

         int node;
         while((node = this.next()) != -1) {
            if (pos == this.getPosition()) {
               return node;
            }
         }
      }

      return -1;
   }

   public abstract DTMAxisIterator setStartNode(int var1);

   public abstract void gotoMark();

   public abstract void setMark();

   public abstract int next();
}
