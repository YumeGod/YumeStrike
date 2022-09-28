package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class KerningManager extends LengthManager {
   public boolean isInheritedProperty() {
      return true;
   }

   public String getPropertyName() {
      return "kerning";
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return true;
   }

   public int getPropertyType() {
      return 41;
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("auto")) {
               return ValueConstants.AUTO_VALUE;
            }

            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (var2.equalsIgnoreCase("auto")) {
         return ValueConstants.AUTO_VALUE;
      } else {
         throw this.createInvalidIdentifierDOMException(var2);
      }
   }

   protected int getOrientation() {
      return 0;
   }
}
