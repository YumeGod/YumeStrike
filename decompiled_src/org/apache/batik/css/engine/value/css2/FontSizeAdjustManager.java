package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class FontSizeAdjustManager extends AbstractValueManager {
   public boolean isInheritedProperty() {
      return true;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 44;
   }

   public String getPropertyName() {
      return "font-size-adjust";
   }

   public Value getDefaultValue() {
      return ValueConstants.NONE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 13:
            return new FloatValue((short)1, (float)var1.getIntegerValue());
         case 14:
            return new FloatValue((short)1, var1.getFloatValue());
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("none")) {
               return ValueConstants.NONE_VALUE;
            }

            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (var2.equalsIgnoreCase("none")) {
         return ValueConstants.NONE_VALUE;
      } else {
         throw this.createInvalidIdentifierDOMException(var2);
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
