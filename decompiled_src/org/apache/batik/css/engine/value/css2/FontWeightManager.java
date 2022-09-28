package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class FontWeightManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

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
      return 28;
   }

   public String getPropertyName() {
      return "font-weight";
   }

   public Value getDefaultValue() {
      return ValueConstants.NORMAL_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      if (var1.getLexicalUnitType() == 13) {
         int var3 = var1.getIntegerValue();
         switch (var3) {
            case 100:
               return ValueConstants.NUMBER_100;
            case 200:
               return ValueConstants.NUMBER_200;
            case 300:
               return ValueConstants.NUMBER_300;
            case 400:
               return ValueConstants.NUMBER_400;
            case 500:
               return ValueConstants.NUMBER_500;
            case 600:
               return ValueConstants.NUMBER_600;
            case 700:
               return ValueConstants.NUMBER_700;
            case 800:
               return ValueConstants.NUMBER_800;
            case 900:
               return ValueConstants.NUMBER_900;
            default:
               throw this.createInvalidFloatValueDOMException((float)var3);
         }
      } else {
         return super.createValue(var1, var2);
      }
   }

   public Value createFloatValue(short var1, float var2) throws DOMException {
      if (var1 == 1) {
         int var3 = (int)var2;
         if (var2 == (float)var3) {
            switch (var3) {
               case 100:
                  return ValueConstants.NUMBER_100;
               case 200:
                  return ValueConstants.NUMBER_200;
               case 300:
                  return ValueConstants.NUMBER_300;
               case 400:
                  return ValueConstants.NUMBER_400;
               case 500:
                  return ValueConstants.NUMBER_500;
               case 600:
                  return ValueConstants.NUMBER_600;
               case 700:
                  return ValueConstants.NUMBER_700;
               case 800:
                  return ValueConstants.NUMBER_800;
               case 900:
                  return ValueConstants.NUMBER_900;
            }
         }
      }

      throw this.createInvalidFloatValueDOMException(var2);
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      CSSContext var7;
      CSSStylableElement var8;
      float var9;
      Value var10;
      if (var6 == ValueConstants.BOLDER_VALUE) {
         var5.putParentRelative(var4, true);
         var7 = var3.getCSSContext();
         var8 = CSSEngine.getParentCSSStylableElement(var1);
         if (var8 == null) {
            var9 = 400.0F;
         } else {
            var10 = var3.getComputedStyle(var8, var2, var4);
            var9 = var10.getFloatValue();
         }

         return this.createFontWeight(var7.getBolderFontWeight(var9));
      } else if (var6 == ValueConstants.LIGHTER_VALUE) {
         var5.putParentRelative(var4, true);
         var7 = var3.getCSSContext();
         var8 = CSSEngine.getParentCSSStylableElement(var1);
         if (var8 == null) {
            var9 = 400.0F;
         } else {
            var10 = var3.getComputedStyle(var8, var2, var4);
            var9 = var10.getFloatValue();
         }

         return this.createFontWeight(var7.getLighterFontWeight(var9));
      } else if (var6 == ValueConstants.NORMAL_VALUE) {
         return ValueConstants.NUMBER_400;
      } else {
         return var6 == ValueConstants.BOLD_VALUE ? ValueConstants.NUMBER_700 : var6;
      }
   }

   protected Value createFontWeight(float var1) {
      switch ((int)var1) {
         case 100:
            return ValueConstants.NUMBER_100;
         case 200:
            return ValueConstants.NUMBER_200;
         case 300:
            return ValueConstants.NUMBER_300;
         case 400:
            return ValueConstants.NUMBER_400;
         case 500:
            return ValueConstants.NUMBER_500;
         case 600:
            return ValueConstants.NUMBER_600;
         case 700:
            return ValueConstants.NUMBER_700;
         case 800:
            return ValueConstants.NUMBER_800;
         default:
            return ValueConstants.NUMBER_900;
      }
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("all", ValueConstants.ALL_VALUE);
      values.put("bold", ValueConstants.BOLD_VALUE);
      values.put("bolder", ValueConstants.BOLDER_VALUE);
      values.put("lighter", ValueConstants.LIGHTER_VALUE);
      values.put("normal", ValueConstants.NORMAL_VALUE);
   }
}
