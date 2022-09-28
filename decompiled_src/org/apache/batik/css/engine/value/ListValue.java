package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public class ListValue extends AbstractValue {
   protected int length;
   protected Value[] items = new Value[5];
   protected char separator = ',';

   public ListValue() {
   }

   public ListValue(char var1) {
      this.separator = var1;
   }

   public char getSeparatorChar() {
      return this.separator;
   }

   public short getCssValueType() {
      return 2;
   }

   public String getCssText() {
      StringBuffer var1 = new StringBuffer(this.length * 8);
      if (this.length > 0) {
         var1.append(this.items[0].getCssText());
      }

      for(int var2 = 1; var2 < this.length; ++var2) {
         var1.append(this.separator);
         var1.append(this.items[var2].getCssText());
      }

      return var1.toString();
   }

   public int getLength() throws DOMException {
      return this.length;
   }

   public Value item(int var1) throws DOMException {
      return this.items[var1];
   }

   public String toString() {
      return this.getCssText();
   }

   public void append(Value var1) {
      if (this.length == this.items.length) {
         Value[] var2 = new Value[this.length * 2];
         System.arraycopy(this.items, 0, var2, 0, this.length);
         this.items = var2;
      }

      this.items[this.length++] = var1;
   }
}
