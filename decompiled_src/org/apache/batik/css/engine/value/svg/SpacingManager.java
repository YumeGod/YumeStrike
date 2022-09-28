package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class SpacingManager extends LengthManager {
   protected String property;

   public SpacingManager(String var1) {
      this.property = var1;
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
      return 42;
   }

   public String getPropertyName() {
      return this.property;
   }

   public Value getDefaultValue() {
      return ValueConstants.NORMAL_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("normal")) {
               return ValueConstants.NORMAL_VALUE;
            }

            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (var2.equalsIgnoreCase("normal")) {
         return ValueConstants.NORMAL_VALUE;
      } else {
         throw this.createInvalidIdentifierDOMException(var2);
      }
   }

   protected int getOrientation() {
      return 2;
   }
}
