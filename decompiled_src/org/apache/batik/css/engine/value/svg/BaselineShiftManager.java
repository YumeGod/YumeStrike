package org.apache.batik.css.engine.value.svg;

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

public class BaselineShiftManager extends LengthManager {
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
      return 40;
   }

   public String getPropertyName() {
      return "baseline-shift";
   }

   public Value getDefaultValue() {
      return SVGValueConstants.BASELINE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            Object var3 = values.get(var1.getStringValue().toLowerCase().intern());
            if (var3 == null) {
               throw this.createInvalidIdentifierDOMException(var1.getStringValue());
            }

            return (Value)var3;
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidIdentifierDOMException(var2);
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
      if (var6.getPrimitiveType() == 2) {
         var5.putLineHeightRelative(var4, true);
         int var7 = var3.getLineHeightIndex();
         CSSStylableElement var8 = (CSSStylableElement)var1.getParentNode();
         if (var8 == null) {
            var8 = var1;
         }

         Value var9 = var3.getComputedStyle(var8, var2, var7);
         float var10 = var9.getFloatValue();
         float var11 = var6.getFloatValue();
         return new FloatValue((short)1, var10 * var11 / 100.0F);
      } else {
         return super.computeValue(var1, var2, var3, var4, var5, var6);
      }
   }

   protected int getOrientation() {
      return 2;
   }

   static {
      values.put("baseline", SVGValueConstants.BASELINE_VALUE);
      values.put("sub", SVGValueConstants.SUB_VALUE);
      values.put("super", SVGValueConstants.SUPER_VALUE);
   }
}
