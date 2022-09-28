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

public class EnableBackgroundManager extends LengthManager {
   protected int orientation;

   public boolean isInheritedProperty() {
      return false;
   }

   public boolean isAnimatableProperty() {
      return false;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 23;
   }

   public String getPropertyName() {
      return "enable-background";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.ACCUMULATE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            String var3 = var1.getStringValue().toLowerCase().intern();
            if (var3 == "accumulate") {
               return SVGValueConstants.ACCUMULATE_VALUE;
            } else if (var3 != "new") {
               throw this.createInvalidIdentifierDOMException(var3);
            } else {
               ListValue var4 = new ListValue(' ');
               var4.append(SVGValueConstants.NEW_VALUE);
               var1 = var1.getNextLexicalUnit();
               if (var1 == null) {
                  return var4;
               } else {
                  var4.append(super.createValue(var1, var2));

                  for(int var5 = 1; var5 < 4; ++var5) {
                     var1 = var1.getNextLexicalUnit();
                     if (var1 == null) {
                        throw this.createMalformedLexicalUnitDOMException();
                     }

                     var4.append(super.createValue(var1, var2));
                  }

                  return var4;
               }
            }
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (!var2.equalsIgnoreCase("accumulate")) {
         throw this.createInvalidIdentifierDOMException(var2);
      } else {
         return SVGValueConstants.ACCUMULATE_VALUE;
      }
   }

   public Value createFloatValue(short var1, float var2) throws DOMException {
      throw this.createDOMException();
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getCssValueType() == 2) {
         ListValue var7 = (ListValue)var6;
         if (var7.getLength() == 5) {
            Value var8 = var7.item(1);
            this.orientation = 0;
            Value var9 = super.computeValue(var1, var2, var3, var4, var5, var8);
            Value var10 = var7.item(2);
            this.orientation = 1;
            Value var11 = super.computeValue(var1, var2, var3, var4, var5, var10);
            Value var12 = var7.item(3);
            this.orientation = 0;
            Value var13 = super.computeValue(var1, var2, var3, var4, var5, var12);
            Value var14 = var7.item(4);
            this.orientation = 1;
            Value var15 = super.computeValue(var1, var2, var3, var4, var5, var14);
            if (var8 != var9 || var10 != var11 || var12 != var13 || var14 != var15) {
               ListValue var16 = new ListValue(' ');
               var16.append(var7.item(0));
               var16.append(var9);
               var16.append(var11);
               var16.append(var13);
               var16.append(var15);
               return var16;
            }
         }
      }

      return var6;
   }

   protected int getOrientation() {
      return this.orientation;
   }
}
