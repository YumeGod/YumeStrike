package org.apache.html.dom;

class CollectionIndex {
   private int _index;

   int getIndex() {
      return this._index;
   }

   void decrement() {
      --this._index;
   }

   boolean isZero() {
      return this._index <= 0;
   }

   CollectionIndex(int var1) {
      this._index = var1;
   }
}
