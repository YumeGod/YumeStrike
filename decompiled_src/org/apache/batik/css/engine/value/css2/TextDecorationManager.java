package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class TextDecorationManager extends AbstractValueManager {
   protected static final StringMap values = new StringMap();

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
      return 18;
   }

   public String getPropertyName() {
      return "text-decoration";
   }

   public Value getDefaultValue() {
      return ValueConstants.NONE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("none")) {
               return ValueConstants.NONE_VALUE;
            } else {
               ListValue var3 = new ListValue(' ');

               do {
                  if (var1.getLexicalUnitType() != 35) {
                     throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
                  }

                  String var4 = var1.getStringValue().toLowerCase().intern();
                  Object var5 = values.get(var4);
                  if (var5 == null) {
                     throw this.createInvalidIdentifierDOMException(var1.getStringValue());
                  }

                  var3.append((Value)var5);
                  var1 = var1.getNextLexicalUnit();
               } while(var1 != null);

               return var3;
            }
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (!var2.equalsIgnoreCase("none")) {
         throw this.createInvalidIdentifierDOMException(var2);
      } else {
         return ValueConstants.NONE_VALUE;
      }
   }

   static {
      values.put("blink", ValueConstants.BLINK_VALUE);
      values.put("line-through", ValueConstants.LINE_THROUGH_VALUE);
      values.put("overline", ValueConstants.OVERLINE_VALUE);
      values.put("underline", ValueConstants.UNDERLINE_VALUE);
   }
}
