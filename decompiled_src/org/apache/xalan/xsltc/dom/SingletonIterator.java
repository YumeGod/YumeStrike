package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

public class SingletonIterator extends DTMAxisIteratorBase {
   private int _node;
   private final boolean _isConstant;

   public SingletonIterator() {
      this(Integer.MIN_VALUE, false);
   }

   public SingletonIterator(int node) {
      this(node, false);
   }

   public SingletonIterator(int node, boolean constant) {
      this._node = super._startNode = node;
      this._isConstant = constant;
   }

   public DTMAxisIterator setStartNode(int node) {
      if (this._isConstant) {
         this._node = super._startNode;
         return this.resetPosition();
      } else if (super._isRestartable) {
         if (this._node <= 0) {
            this._node = super._startNode = node;
         }

         return this.resetPosition();
      } else {
         return this;
      }
   }

   public DTMAxisIterator reset() {
      if (this._isConstant) {
         this._node = super._startNode;
         return this.resetPosition();
      } else {
         boolean temp = super._isRestartable;
         super._isRestartable = true;
         this.setStartNode(super._startNode);
         super._isRestartable = temp;
         return this;
      }
   }

   public int next() {
      int result = this._node;
      this._node = -1;
      return this.returnNode(result);
   }

   public void setMark() {
      super._markedNode = this._node;
   }

   public void gotoMark() {
      this._node = super._markedNode;
   }
}
