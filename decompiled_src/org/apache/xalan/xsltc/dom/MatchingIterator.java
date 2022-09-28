package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public final class MatchingIterator extends DTMAxisIteratorBase {
   private DTMAxisIterator _source;
   private final int _match;

   public MatchingIterator(int match, DTMAxisIterator source) {
      this._source = source;
      this._match = match;
   }

   public void setRestartable(boolean isRestartable) {
      super._isRestartable = isRestartable;
      this._source.setRestartable(isRestartable);
   }

   public DTMAxisIterator cloneIterator() {
      try {
         MatchingIterator clone = (MatchingIterator)super.clone();
         clone._source = this._source.cloneIterator();
         clone._isRestartable = false;
         return clone.reset();
      } catch (CloneNotSupportedException var2) {
         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", (Object)var2.toString());
         return null;
      }
   }

   public DTMAxisIterator setStartNode(int node) {
      if (super._isRestartable) {
         this._source.setStartNode(node);

         for(super._position = 1; (node = this._source.next()) != -1 && node != this._match; ++super._position) {
         }
      }

      return this;
   }

   public DTMAxisIterator reset() {
      this._source.reset();
      return this.resetPosition();
   }

   public int next() {
      return this._source.next();
   }

   public int getLast() {
      if (super._last == -1) {
         super._last = this._source.getLast();
      }

      return super._last;
   }

   public int getPosition() {
      return super._position;
   }

   public void setMark() {
      this._source.setMark();
   }

   public void gotoMark() {
      this._source.gotoMark();
   }
}
