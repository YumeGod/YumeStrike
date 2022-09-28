package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class AbsoluteIterator extends DTMAxisIteratorBase {
   private DTMAxisIterator _source;

   public AbsoluteIterator(DTMAxisIterator source) {
      this._source = source;
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
   }

   public DTMAxisIterator setStartNode(int node) {
      super._startNode = 0;
      if (super._isRestartable) {
         this._source.setStartNode(super._startNode);
         this.resetPosition();
      }

      return this;
   }

   public int next() {
      return this.returnNode(this._source.next());
   }

   public DTMAxisIterator cloneIterator() {
      try {
         AbsoluteIterator clone = (AbsoluteIterator)super.clone();
         clone._source = this._source.cloneIterator();
         clone.resetPosition();
         clone._isRestartable = false;
         return clone;
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public DTMAxisIterator reset() {
      this._source.reset();
      return this.resetPosition();
   }

   public void setMark() {
      this._source.setMark();
   }

   public void gotoMark() {
      this._source.gotoMark();
   }
}
