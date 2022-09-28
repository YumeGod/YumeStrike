package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class StepIterator extends DTMAxisIteratorBase {
   protected DTMAxisIterator _source;
   protected DTMAxisIterator _iterator;
   private int _pos = -1;

   public StepIterator(DTMAxisIterator source, DTMAxisIterator iterator) {
      this._source = source;
      this._iterator = iterator;
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
      this._iterator.setRestartable(true);
   }

   public DTMAxisIterator cloneIterator() {
      super._isRestartable = false;

      try {
         StepIterator clone = (StepIterator)super.clone();
         clone._source = this._source.cloneIterator();
         clone._iterator = this._iterator.cloneIterator();
         clone._iterator.setRestartable(true);
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public DTMAxisIterator setStartNode(int node) {
      if (super._isRestartable) {
         this._source.setStartNode(super._startNode = node);
         this._iterator.setStartNode(super._includeSelf ? super._startNode : this._source.next());
         return this.resetPosition();
      } else {
         return this;
      }
   }

   public DTMAxisIterator reset() {
      this._source.reset();
      this._iterator.setStartNode(super._includeSelf ? super._startNode : this._source.next());
      return this.resetPosition();
   }

   public int next() {
      int node;
      while((node = this._iterator.next()) == -1) {
         if ((node = this._source.next()) == -1) {
            return -1;
         }

         this._iterator.setStartNode(node);
      }

      return this.returnNode(node);
   }

   public void setMark() {
      this._source.setMark();
      this._iterator.setMark();
   }

   public void gotoMark() {
      this._source.gotoMark();
      this._iterator.gotoMark();
   }
}
