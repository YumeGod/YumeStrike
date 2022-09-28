package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class FontSizeManager extends LengthManager {
   protected static final StringMap values = new StringMap();

   public StringMap getIdentifiers() {
      return values;
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

   public String getPropertyName() {
      return "font-size";
   }

   public int getPropertyType() {
      return 39;
   }

   public Value getDefaultValue() {
      return ValueConstants.MEDIUM_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            String var3 = var1.getStringValue().toLowerCase().intern();
            Object var4 = values.get(var3);
            if (var4 == null) {
               throw this.createInvalidIdentifierDOMException(var3);
            }

            return (Value)var4;
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else {
         Object var4 = values.get(var2.toLowerCase().intern());
         if (var4 == null) {
            throw this.createInvalidIdentifierDOMException(var2);
         } else {
            return (Value)var4;
         }
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      float var7 = 1.0F;
      boolean var8 = false;
      CSSContext var9;
      float var10;
      switch (var6.getPrimitiveType()) {
         case 1:
         case 5:
            return var6;
         case 2:
            var8 = true;
            var7 = var6.getFloatValue() * 0.01F;
            break;
         case 3:
            var8 = true;
            var7 = var6.getFloatValue();
            break;
         case 4:
            var8 = true;
            var7 = var6.getFloatValue() * 0.5F;
            break;
         case 6:
            var9 = var3.getCSSContext();
            var10 = var6.getFloatValue();
            return new FloatValue((short)1, var10 * 10.0F / var9.getPixelUnitToMillimeter());
         case 7:
            var9 = var3.getCSSContext();
            var10 = var6.getFloatValue();
            return new FloatValue((short)1, var10 / var9.getPixelUnitToMillimeter());
         case 8:
            var9 = var3.getCSSContext();
            var10 = var6.getFloatValue();
            return new FloatValue((short)1, var10 * 25.4F / var9.getPixelUnitToMillimeter());
         case 9:
            var9 = var3.getCSSContext();
            var10 = var6.getFloatValue();
            return new FloatValue((short)1, var10 * 25.4F / (72.0F * var9.getPixelUnitToMillimeter()));
         case 10:
            var9 = var3.getCSSContext();
            var10 = var6.getFloatValue();
            return new FloatValue((short)1, var10 * 25.4F / (6.0F * var9.getPixelUnitToMillimeter()));
      }

      if (var6 == ValueConstants.LARGER_VALUE) {
         var8 = true;
         var7 = 1.2F;
      } else if (var6 == ValueConstants.SMALLER_VALUE) {
         var8 = true;
         var7 = 0.8333333F;
      }

      if (var8) {
         var5.putParentRelative(var4, true);
         CSSStylableElement var12 = CSSEngine.getParentCSSStylableElement(var1);
         if (var12 == null) {
            CSSContext var13 = var3.getCSSContext();
            var10 = var13.getMediumFontSize();
         } else {
            var10 = var3.getComputedStyle(var12, (String)null, var4).getFloatValue();
         }

         return new FloatValue((short)1, var10 * var7);
      } else {
         var9 = var3.getCSSContext();
         var10 = var9.getMediumFontSize();
         String var11 = var6.getStringValue();
         switch (var11.charAt(0)) {
            case 'l':
               var10 = (float)((double)var10 * 1.2);
            case 'm':
               break;
            case 's':
               var10 = (float)((double)var10 / 1.2);
               break;
            default:
               switch (var11.charAt(1)) {
                  case 'x':
                     switch (var11.charAt(3)) {
                        case 's':
                           var10 = (float)((double)var10 / 1.2 / 1.2 / 1.2);
                           return new FloatValue((short)1, var10);
                        default:
                           var10 = (float)((double)var10 * 1.2 * 1.2 * 1.2);
                           return new FloatValue((short)1, var10);
                     }
                  default:
                     switch (var11.charAt(2)) {
                        case 's':
                           var10 = (float)((double)var10 / 1.2 / 1.2);
                           break;
                        default:
                           var10 = (float)((double)var10 * 1.2 * 1.2);
                     }
               }
         }

         return new FloatValue((short)1, var10);
      }
   }

   protected int getOrientation() {
      return 1;
   }

   static {
      values.put("all", ValueConstants.ALL_VALUE);
      values.put("large", ValueConstants.LARGE_VALUE);
      values.put("larger", ValueConstants.LARGER_VALUE);
      values.put("medium", ValueConstants.MEDIUM_VALUE);
      values.put("small", ValueConstants.SMALL_VALUE);
      values.put("smaller", ValueConstants.SMALLER_VALUE);
      values.put("x-large", ValueConstants.X_LARGE_VALUE);
      values.put("x-small", ValueConstants.X_SMALL_VALUE);
      values.put("xx-large", ValueConstants.XX_LARGE_VALUE);
      values.put("xx-small", ValueConstants.XX_SMALL_VALUE);
   }
}
