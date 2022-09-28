package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class CurrentNodeListIterator extends DTMAxisIteratorBase {
   private boolean _docOrder;
   private DTMAxisIterator _source;
   private final CurrentNodeListFilter _filter;
   private IntegerArray _nodes;
   private int _currentIndex;
   private final int _currentNode;
   private AbstractTranslet _translet;

   public CurrentNodeListIterator(DTMAxisIterator source, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet) {
      this(source, !source.isReverse(), filter, currentNode, translet);
   }

   public CurrentNodeListIterator(DTMAxisIterator source, boolean docOrder, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet) {
      this._nodes = new IntegerArray();
      this._source = source;
      this._filter = filter;
      this._translet = translet;
      this._docOrder = docOrder;
      this._currentNode = currentNode;
   }

   public DTMAxisIterator forceNaturalOrder() {
      this._docOrder = true;
      return this;
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
   }

   public boolean isReverse() {
      return !this._docOrder;
   }

   public DTMAxisIterator cloneIterator() {
      try {
         CurrentNodeListIterator clone = (CurrentNodeListIterator)super.clone();
         clone._nodes = (IntegerArray)this._nodes.clone();
         clone._source = this._source.cloneIterator();
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public DTMAxisIterator reset() {
      this._currentIndex = 0;
      return this.resetPosition();
   }

   public int next() {
      int last = this._nodes.cardinality();
      int currentNode = this._currentNode;
      AbstractTranslet translet = this._translet;
      int index = this._currentIndex;

      int position;
      int node;
      do {
         if (index >= last) {
            return -1;
         }

         position = this._docOrder ? index + 1 : last - index;
         node = this._nodes.at(index++);
      } while(!this._filter.test(node, position, last, currentNode, translet, this));

      this._currentIndex = index;
      return this.returnNode(node);
   }

   public DTMAxisIterator setStartNode(int node) {
      if (super._isRestartable) {
         this._source.setStartNode(super._startNode = node);
         this._nodes.clear();

         while((node = this._source.next()) != -1) {
            this._nodes.add(node);
         }

         this._currentIndex = 0;
         this.resetPosition();
      }

      return this;
   }

   public int getLast() {
      if (super._last == -1) {
         super._last = this.computePositionOfLast();
      }

      return super._last;
   }

   public void setMark() {
      super._markedNode = this._currentIndex;
   }

   public void gotoMark() {
      this._currentIndex = super._markedNode;
   }

   private int computePositionOfLast() {
      int last = this._nodes.cardinality();
      int currNode = this._currentNode;
      AbstractTranslet translet = this._translet;
      int lastPosition = super._position;
      int index = this._currentIndex;

      while(index < last) {
         int position = this._docOrder ? index + 1 : last - index;
         int nodeIndex = this._nodes.at(index++);
         if (this._filter.test(nodeIndex, position, last, currNode, translet, this)) {
            ++lastPosition;
         }
      }

      return lastPosition;
   }
}
