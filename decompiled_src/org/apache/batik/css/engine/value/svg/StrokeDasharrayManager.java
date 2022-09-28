package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class StrokeDasharrayManager extends LengthManager {
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
      return 34;
   }

   public String getPropertyName() {
      return "stroke-dasharray";
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
            }

            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         default:
            ListValue var3 = new ListValue(' ');

            do {
               Value var4 = super.createValue(var1, var2);
               var3.append(var4);
               var1 = var1.getNextLexicalUnit();
               if (var1 != null && var1.getLexicalUnitType() == 0) {
                  var1 = var1.getNextLexicalUnit();
               }
            } while(var1 != null);

            return var3;
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

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      switch (var6.getCssValueType()) {
         case 1:
            return var6;
         default:
            ListValue var7 = (ListValue)var6;
            ListValue var8 = new ListValue(' ');

            for(int var9 = 0; var9 < var7.getLength(); ++var9) {
               var8.append(super.computeValue(var1, var2, var3, var4, var5, var7.item(var9)));
            }

            return var8;
      }
   }

   protected int getOrientation() {
      return 2;
   }
}
