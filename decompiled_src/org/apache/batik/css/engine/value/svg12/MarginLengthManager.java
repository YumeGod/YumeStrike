package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class MarginLengthManager extends LengthManager {
   protected String prop;

   public MarginLengthManager(String var1) {
      this.prop = var1;
   }

   public boolean isInheritedProperty() {
      return true;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return true;
   }

   public int getPropertyType() {
      return 17;
   }

   public String getPropertyName() {
      return this.prop;
   }

   public Value getDefaultValue() {
      return ValueConstants.NUMBER_0;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      return var1.getLexicalUnitType() == 12 ? ValueConstants.INHERIT_VALUE : super.createValue(var1, var2);
   }

   protected int getOrientation() {
      return 0;
   }
}
