package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class DupFilterIterator extends DTMAxisIteratorBase {
   private DTMAxisIterator _source;
   private IntegerArray _nodes = new IntegerArray();
   private int _current = 0;
   private int _nodesSize = 0;
   private int _lastNext = -1;
   private int _markedLastNext = -1;

   public DupFilterIterator(DTMAxisIterator source) {
      this._source = source;
      if (source instanceof KeyIndex) {
         this.setStartNode(0);
      }

   }

   public DTMAxisIterator setStartNode(int node) {
      if (super._isRestartable) {
         if (this._source instanceof KeyIndex && super._startNode == 0) {
            return this;
         }

         if (node != super._startNode) {
            this._source.setStartNode(super._startNode = node);
            this._nodes.clear();

            while((node = this._source.next()) != -1) {
               this._nodes.add(node);
            }

            this._nodes.sort();
            this._nodesSize = this._nodes.cardinality();
            this._current = 0;
            this._lastNext = -1;
            this.resetPosition();
         }
      }

      return this;
   }

   public int next() {
      int next;
      do {
         if (this._current >= this._nodesSize) {
            return -1;
         }

         next = this._nodes.at(this._current++);
      } while(next == this._lastNext);

      return this.returnNode(this._lastNext = next);
   }

   public DTMAxisIterator cloneIterator() {
      try {
         DupFilterIterator clone = (DupFilterIterator)super.clone();
         clone._nodes = (IntegerArray)this._nodes.clone();
         clone._source = this._source.cloneIterator();
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
   }

   public void setMark() {
      super._markedNode = this._current;
      this._markedLastNext = this._lastNext;
   }

   public void gotoMark() {
      this._current = super._markedNode;
      this._lastNext = this._markedLastNext;
   }

   public DTMAxisIterator reset() {
      this._current = 0;
      this._lastNext = -1;
      return this.resetPosition();
   }
}
