package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class SortingIterator extends DTMAxisIteratorBase {
   private static final int INIT_DATA_SIZE = 16;
   private DTMAxisIterator _source;
   private NodeSortRecordFactory _factory;
   private NodeSortRecord[] _data;
   private int _free = 0;
   private int _current;

   public SortingIterator(DTMAxisIterator source, NodeSortRecordFactory factory) {
      this._source = source;
      this._factory = factory;
   }

   public int next() {
      return this._current < this._free ? this._data[this._current++].getNode() : -1;
   }

   public DTMAxisIterator setStartNode(int node) {
      try {
         this._source.setStartNode(super._startNode = node);
         this._data = new NodeSortRecord[16];
         this._free = 0;

         while((node = this._source.next()) != -1) {
            this.addRecord(this._factory.makeNodeSortRecord(node, this._free));
         }

         this.quicksort(0, this._free - 1);
         this._current = 0;
         return this;
      } catch (Exception var3) {
         return this;
      }
   }

   public int getPosition() {
      return this._current == 0 ? 1 : this._current;
   }

   public int getLast() {
      return this._free;
   }

   public void setMark() {
      this._source.setMark();
      super._markedNode = this._current;
   }

   public void gotoMark() {
      this._source.gotoMark();
      this._current = super._markedNode;
   }

   public DTMAxisIterator cloneIterator() {
      try {
         SortingIterator clone = (SortingIterator)super.clone();
         clone._source = this._source.cloneIterator();
         clone._factory = this._factory;
         clone._data = this._data;
         clone._free = this._free;
         clone._current = this._current;
         clone.setRestartable(false);
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   private void addRecord(NodeSortRecord record) {
      if (this._free == this._data.length) {
         NodeSortRecord[] newArray = new NodeSortRecord[this._data.length * 2];
         System.arraycopy(this._data, 0, newArray, 0, this._free);
         this._data = newArray;
      }

      this._data[this._free++] = record;
   }

   private void quicksort(int p, int r) {
      while(p < r) {
         int q = this.partition(p, r);
         this.quicksort(p, q);
         p = q + 1;
      }

   }

   private int partition(int p, int r) {
      NodeSortRecord x = this._data[p + r >>> 1];
      int i = p - 1;
      int j = r + 1;

      while(true) {
         do {
            --j;
         } while(x.compareTo(this._data[j]) < 0);

         do {
            ++i;
         } while(x.compareTo(this._data[i]) > 0);

         if (i >= j) {
            return j;
         }

         NodeSortRecord t = this._data[i];
         this._data[i] = this._data[j];
         this._data[j] = t;
      }
   }
}
