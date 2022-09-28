package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public abstract class NodeIteratorBase implements NodeIterator {
   protected int _last = -1;
   protected int _position = 0;
   protected int _markedNode;
   protected int _startNode = -1;
   protected boolean _includeSelf = false;
   protected boolean _isRestartable = true;

   public void setRestartable(boolean isRestartable) {
      this._isRestartable = isRestartable;
   }

   public abstract NodeIterator setStartNode(int var1);

   public NodeIterator reset() {
      boolean temp = this._isRestartable;
      this._isRestartable = true;
      this.setStartNode(this._includeSelf ? this._startNode + 1 : this._startNode);
      this._isRestartable = temp;
      return this;
   }

   public NodeIterator includeSelf() {
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

   public NodeIterator cloneIterator() {
      try {
         NodeIteratorBase clone = (NodeIteratorBase)super.clone();
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   protected final int returnNode(int node) {
      ++this._position;
      return node;
   }

   protected final NodeIterator resetPosition() {
      this._position = 0;
      return this;
   }

   public abstract void gotoMark();

   public abstract void setMark();

   public abstract int next();
}
