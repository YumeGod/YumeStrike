package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class FilterIterator extends DTMAxisIteratorBase {
   private DTMAxisIterator _source;
   private final DTMFilter _filter;
   private final boolean _isReverse;

   public FilterIterator(DTMAxisIterator source, DTMFilter filter) {
      this._source = source;
      this._filter = filter;
      this._isReverse = source.isReverse();
   }

   public boolean isReverse() {
      return this._isReverse;
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
   }

   public DTMAxisIterator cloneIterator() {
      try {
         FilterIterator clone = (FilterIterator)super.clone();
         clone._source = this._source.cloneIterator();
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public DTMAxisIterator reset() {
      this._source.reset();
      return this.resetPosition();
   }

   public int next() {
      int node;
      do {
         if ((node = this._source.next()) == -1) {
            return -1;
         }
      } while(this._filter.acceptNode(node, -1) != 1);

      return this.returnNode(node);
   }

   public DTMAxisIterator setStartNode(int node) {
      if (super._isRestartable) {
         this._source.setStartNode(super._startNode = node);
         return this.resetPosition();
      } else {
         return this;
      }
   }

   public void setMark() {
      this._source.setMark();
   }

   public void gotoMark() {
      this._source.gotoMark();
   }
}
