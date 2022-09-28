package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.InheritValue;
import org.apache.batik.css.engine.value.RectManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class ClipManager extends RectManager {
   public boolean isInheritedProperty() {
      return false;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 19;
   }

   public String getPropertyName() {
      return "clip";
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return InheritValue.INSTANCE;
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("auto")) {
               return ValueConstants.AUTO_VALUE;
            }
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (!var2.equalsIgnoreCase("auto")) {
         throw this.createInvalidIdentifierDOMException(var2);
      } else {
         return ValueConstants.AUTO_VALUE;
      }
   }
}
