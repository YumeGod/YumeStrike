package org.apache.batik.css.engine.value;

public class InheritValue extends AbstractValue {
   public static final InheritValue INSTANCE = new InheritValue();

   protected InheritValue() {
   }

   public String getCssText() {
      return "inherit";
   }

   public short getCssValueType() {
      return 0;
   }

   public String toString() {
      return this.getCssText();
   }
}
