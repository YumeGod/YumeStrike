package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;

public class ComputedValue implements Value {
   protected Value cascadedValue;
   protected Value computedValue;

   public ComputedValue(Value var1) {
      this.cascadedValue = var1;
   }

   public Value getComputedValue() {
      return this.computedValue;
   }

   public Value getCascadedValue() {
      return this.cascadedValue;
   }

   public void setComputedValue(Value var1) {
      this.computedValue = var1;
   }

   public String getCssText() {
      return this.computedValue.getCssText();
   }

   public short getCssValueType() {
      return this.computedValue.getCssValueType();
   }

   public short getPrimitiveType() {
      return this.computedValue.getPrimitiveType();
   }

   public float getFloatValue() throws DOMException {
      return this.computedValue.getFloatValue();
   }

   public String getStringValue() throws DOMException {
      return this.computedValue.getStringValue();
   }

   public Value getRed() throws DOMException {
      return this.computedValue.getRed();
   }

   public Value getGreen() throws DOMException {
      return this.computedValue.getGreen();
   }

   public Value getBlue() throws DOMException {
      return this.computedValue.getBlue();
   }

   public int getLength() throws DOMException {
      return this.computedValue.getLength();
   }

   public Value item(int var1) throws DOMException {
      return this.computedValue.item(var1);
   }

   public Value getTop() throws DOMException {
      return this.computedValue.getTop();
   }

   public Value getRight() throws DOMException {
      return this.computedValue.getRight();
   }

   public Value getBottom() throws DOMException {
      return this.computedValue.getBottom();
   }

   public Value getLeft() throws DOMException {
      return this.computedValue.getLeft();
   }

   public String getIdentifier() throws DOMException {
      return this.computedValue.getIdentifier();
   }

   public String getListStyle() throws DOMException {
      return this.computedValue.getListStyle();
   }

   public String getSeparator() throws DOMException {
      return this.computedValue.getSeparator();
   }
}
