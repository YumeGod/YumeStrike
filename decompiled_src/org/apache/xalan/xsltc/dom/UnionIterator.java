package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class UnionIterator extends DTMAxisIteratorBase {
   private final DOM _dom;
   private static final int InitSize = 8;
   private int _heapSize = 0;
   private int _size = 8;
   private LookAheadIterator[] _heap = new LookAheadIterator[8];
   private int _free = 0;
   private int _returnedLast;
   private int _cachedReturnedLast = -1;
   private int _cachedHeapSize;

   public UnionIterator(DOM dom) {
      this._dom = dom;
   }

   public DTMAxisIterator cloneIterator() {
      super._isRestartable = false;
      LookAheadIterator[] heapCopy = new LookAheadIterator[this._heap.length];

      try {
         UnionIterator clone = (UnionIterator)super.clone();

         for(int i = 0; i < this._free; ++i) {
            heapCopy[i] = this._heap[i].cloneIterator();
         }

         clone.setRestartable(false);
         clone._heap = heapCopy;
         return clone.reset();
      } catch (CloneNotSupportedException var4) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var4.toString());
         return null;
      }
   }

   public UnionIterator addIterator(DTMAxisIterator iterator) {
      if (this._free == this._size) {
         LookAheadIterator[] newArray = new LookAheadIterator[this._size *= 2];
         System.arraycopy(this._heap, 0, newArray, 0, this._free);
         this._heap = newArray;
      }

      ++this._heapSize;
      this._heap[this._free++] = new LookAheadIterator(iterator);
      return this;
   }

   public int next() {
      for(; this._heapSize > 0; this.heapify(0)) {
         int smallest = this._heap[0].node;
         if (smallest == -1) {
            if (this._heapSize <= 1) {
               return -1;
            }

            LookAheadIterator temp = this._heap[0];
            this._heap[0] = this._heap[--this._heapSize];
            this._heap[this._heapSize] = temp;
         } else {
            if (smallest != this._returnedLast) {
               this._heap[0].step();
               this.heapify(0);
               return this.returnNode(this._returnedLast = smallest);
            }

            this._heap[0].step();
         }
      }

      return -1;
   }

   public DTMAxisIterator setStartNode(int node) {
      if (!super._isRestartable) {
         return this;
      } else {
         super._startNode = node;

         for(int i = 0; i < this._free; ++i) {
            if (!this._heap[i].isStartSet) {
               this._heap[i].iterator.setStartNode(node);
               this._heap[i].step();
               this._heap[i].isStartSet = true;
            }
         }

         for(int i = (this._heapSize = this._free) / 2; i >= 0; --i) {
            this.heapify(i);
         }

         this._returnedLast = -1;
         return this.resetPosition();
      }
   }

   private void heapify(int i) {
      while(true) {
         int r = i + 1 << 1;
         int l = r - 1;
         int smallest = l < this._heapSize && this._dom.lessThan(this._heap[l].node, this._heap[i].node) ? l : i;
         if (r < this._heapSize && this._dom.lessThan(this._heap[r].node, this._heap[smallest].node)) {
            smallest = r;
         }

         if (smallest == i) {
            return;
         }

         LookAheadIterator temp = this._heap[smallest];
         this._heap[smallest] = this._heap[i];
         this._heap[i] = temp;
         i = smallest;
      }
   }

   public void setMark() {
      for(int i = 0; i < this._free; ++i) {
         this._heap[i].setMark();
      }

      this._cachedReturnedLast = this._returnedLast;
      this._cachedHeapSize = this._heapSize;
   }

   public void gotoMark() {
      for(int i = 0; i < this._free; ++i) {
         this._heap[i].gotoMark();
      }

      for(int i = (this._heapSize = this._cachedHeapSize) / 2; i >= 0; --i) {
         this.heapify(i);
      }

      this._returnedLast = this._cachedReturnedLast;
   }

   public DTMAxisIterator reset() {
      for(int i = 0; i < this._free; ++i) {
         this._heap[i].iterator.reset();
         this._heap[i].step();
      }

      for(int i = (this._heapSize = this._free) / 2; i >= 0; --i) {
         this.heapify(i);
      }

      this._returnedLast = -1;
      return this.resetPosition();
   }

   private static final class LookAheadIterator {
      public int node;
      public int markedNode;
      public DTMAxisIterator iterator;
      public boolean isStartSet = false;

      public LookAheadIterator(DTMAxisIterator iterator) {
         this.iterator = iterator;
      }

      public int step() {
         this.node = this.iterator.next();
         return this.node;
      }

      public LookAheadIterator cloneIterator() {
         LookAheadIterator clone = new LookAheadIterator(this.iterator.cloneIterator());
         clone.node = this.node;
         clone.markedNode = this.node;
         return clone;
      }

      public void setMark() {
         this.markedNode = this.node;
         this.iterator.setMark();
      }

      public void gotoMark() {
         this.node = this.markedNode;
         this.iterator.gotoMark();
      }
   }
}
