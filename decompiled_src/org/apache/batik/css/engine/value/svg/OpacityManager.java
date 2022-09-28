package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class OpacityManager extends AbstractValueManager {
   protected boolean inherited;
   protected String property;

   public OpacityManager(String var1, boolean var2) {
      this.property = var1;
      this.inherited = var2;
   }

   public boolean isInheritedProperty() {
      return this.inherited;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return true;
   }

   public int getPropertyType() {
      return 25;
   }

   public String getPropertyName() {
      return this.property;
   }

   public Value getDefaultValue() {
      return SVGValueConstants.NUMBER_1;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 13:
            return new FloatValue((short)1, (float)var1.getIntegerValue());
         case 14:
            return new FloatValue((short)1, var1.getFloatValue());
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createFloatValue(short var1, float var2) throws DOMException {
      if (var1 == 1) {
         return new FloatValue(var1, var2);
      } else {
         throw this.createInvalidFloatTypeDOMException(var1);
      }
   }
}
