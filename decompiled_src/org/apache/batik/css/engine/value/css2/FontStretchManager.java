package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;

public class FontStretchManager extends IdentifierManager {
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
      return 15;
   }

   public String getPropertyName() {
      return "font-stretch";
   }

   public Value getDefaultValue() {
      return ValueConstants.NORMAL_VALUE;
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      CSSStylableElement var7;
      Value var8;
      if (var6 == ValueConstants.NARROWER_VALUE) {
         var5.putParentRelative(var4, true);
         var7 = CSSEngine.getParentCSSStylableElement(var1);
         if (var7 == null) {
            return ValueConstants.SEMI_CONDENSED_VALUE;
         } else {
            var8 = var3.getComputedStyle(var7, var2, var4);
            if (var8 == ValueConstants.NORMAL_VALUE) {
               return ValueConstants.SEMI_CONDENSED_VALUE;
            } else if (var8 == ValueConstants.CONDENSED_VALUE) {
               return ValueConstants.EXTRA_CONDENSED_VALUE;
            } else if (var8 == ValueConstants.EXPANDED_VALUE) {
               return ValueConstants.SEMI_EXPANDED_VALUE;
            } else if (var8 == ValueConstants.SEMI_EXPANDED_VALUE) {
               return ValueConstants.NORMAL_VALUE;
            } else if (var8 == ValueConstants.SEMI_CONDENSED_VALUE) {
               return ValueConstants.CONDENSED_VALUE;
            } else if (var8 == ValueConstants.EXTRA_CONDENSED_VALUE) {
               return ValueConstants.ULTRA_CONDENSED_VALUE;
            } else if (var8 == ValueConstants.EXTRA_EXPANDED_VALUE) {
               return ValueConstants.EXPANDED_VALUE;
            } else {
               return var8 == ValueConstants.ULTRA_CONDENSED_VALUE ? ValueConstants.ULTRA_CONDENSED_VALUE : ValueConstants.EXTRA_EXPANDED_VALUE;
            }
         }
      } else if (var6 == ValueConstants.WIDER_VALUE) {
         var5.putParentRelative(var4, true);
         var7 = CSSEngine.getParentCSSStylableElement(var1);
         if (var7 == null) {
            return ValueConstants.SEMI_CONDENSED_VALUE;
         } else {
            var8 = var3.getComputedStyle(var7, var2, var4);
            if (var8 == ValueConstants.NORMAL_VALUE) {
               return ValueConstants.SEMI_EXPANDED_VALUE;
            } else if (var8 == ValueConstants.CONDENSED_VALUE) {
               return ValueConstants.SEMI_CONDENSED_VALUE;
            } else if (var8 == ValueConstants.EXPANDED_VALUE) {
               return ValueConstants.EXTRA_EXPANDED_VALUE;
            } else if (var8 == ValueConstants.SEMI_EXPANDED_VALUE) {
               return ValueConstants.EXPANDED_VALUE;
            } else if (var8 == ValueConstants.SEMI_CONDENSED_VALUE) {
               return ValueConstants.NORMAL_VALUE;
            } else if (var8 == ValueConstants.EXTRA_CONDENSED_VALUE) {
               return ValueConstants.CONDENSED_VALUE;
            } else if (var8 == ValueConstants.EXTRA_EXPANDED_VALUE) {
               return ValueConstants.ULTRA_EXPANDED_VALUE;
            } else {
               return var8 == ValueConstants.ULTRA_CONDENSED_VALUE ? ValueConstants.EXTRA_CONDENSED_VALUE : ValueConstants.ULTRA_EXPANDED_VALUE;
            }
         }
      } else {
         return var6;
      }
   }

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("all", ValueConstants.ALL_VALUE);
      values.put("condensed", ValueConstants.CONDENSED_VALUE);
      values.put("expanded", ValueConstants.EXPANDED_VALUE);
      values.put("extra-condensed", ValueConstants.EXTRA_CONDENSED_VALUE);
      values.put("extra-expanded", ValueConstants.EXTRA_EXPANDED_VALUE);
      values.put("narrower", ValueConstants.NARROWER_VALUE);
      values.put("normal", ValueConstants.NORMAL_VALUE);
      values.put("semi-condensed", ValueConstants.SEMI_CONDENSED_VALUE);
      values.put("semi-expanded", ValueConstants.SEMI_EXPANDED_VALUE);
      values.put("ultra-condensed", ValueConstants.ULTRA_CONDENSED_VALUE);
      values.put("ultra-expanded", ValueConstants.ULTRA_EXPANDED_VALUE);
      values.put("wider", ValueConstants.WIDER_VALUE);
   }
}
