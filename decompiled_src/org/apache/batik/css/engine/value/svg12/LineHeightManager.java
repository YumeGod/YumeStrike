package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class LineHeightManager extends LengthManager {
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
      return 43;
   }

   public String getPropertyName() {
      return "line-height";
   }

   public Value getDefaultValue() {
      return SVG12ValueConstants.NORMAL_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            String var3 = var1.getStringValue().toLowerCase();
            if ("normal".equals(var3)) {
               return SVG12ValueConstants.NORMAL_VALUE;
            }

            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         default:
            return super.createValue(var1, var2);
      }
   }

   protected int getOrientation() {
      return 1;
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getCssValueType() != 1) {
         return var6;
      } else {
         switch (var6.getPrimitiveType()) {
            case 1:
               return new LineHeightValue((short)1, var6.getFloatValue(), true);
            case 2:
               float var7 = var6.getFloatValue();
               int var8 = var3.getFontSizeIndex();
               float var9 = var3.getComputedStyle(var1, var2, var8).getFloatValue();
               return new FloatValue((short)1, var7 * var9 * 0.01F);
            default:
               return super.computeValue(var1, var2, var3, var4, var5, var6);
         }
      }
   }
}
