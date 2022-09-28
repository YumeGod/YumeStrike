package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public class RectValue extends AbstractValue {
   protected Value top;
   protected Value right;
   protected Value bottom;
   protected Value left;

   public RectValue(Value var1, Value var2, Value var3, Value var4) {
      this.top = var1;
      this.right = var2;
      this.bottom = var3;
      this.left = var4;
   }

   public short getPrimitiveType() {
      return 24;
   }

   public String getCssText() {
      return "rect(" + this.top.getCssText() + ", " + this.right.getCssText() + ", " + this.bottom.getCssText() + ", " + this.left.getCssText() + ')';
   }

   public Value getTop() throws DOMException {
      return this.top;
   }

   public Value getRight() throws DOMException {
      return this.right;
   }

   public Value getBottom() throws DOMException {
      return this.bottom;
   }

   public Value getLeft() throws DOMException {
      return this.left;
   }

   public String toString() {
      return this.getCssText();
   }
}
