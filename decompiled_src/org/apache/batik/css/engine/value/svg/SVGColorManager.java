package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class SVGColorManager extends ColorManager {
   protected String property;
   protected Value defaultValue;

   public SVGColorManager(String var1) {
      this(var1, ValueConstants.BLACK_RGB_VALUE);
   }

   public SVGColorManager(String var1, Value var2) {
      this.property = var1;
      this.defaultValue = var2;
   }

   public boolean isInheritedProperty() {
      return false;
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return true;
   }

   public int getPropertyType() {
      return 6;
   }

   public String getPropertyName() {
      return this.property;
   }

   public Value getDefaultValue() {
      return this.defaultValue;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      if (var1.getLexicalUnitType() == 35 && var1.getStringValue().equalsIgnoreCase("currentcolor")) {
         return SVGValueConstants.CURRENTCOLOR_VALUE;
      } else {
         Value var3 = super.createValue(var1, var2);
         var1 = var1.getNextLexicalUnit();
         if (var1 == null) {
            return var3;
         } else if (var1.getLexicalUnitType() == 41 && var1.getFunctionName().equalsIgnoreCase("icc-color")) {
            var1 = var1.getParameters();
            if (var1.getLexicalUnitType() != 35) {
               throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
            } else {
               ListValue var4 = new ListValue(' ');
               var4.append(var3);
               ICCColor var5 = new ICCColor(var1.getStringValue());
               var4.append(var5);

               for(var1 = var1.getNextLexicalUnit(); var1 != null; var1 = var1.getNextLexicalUnit()) {
                  if (var1.getLexicalUnitType() != 0) {
                     throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
                  }

                  var1 = var1.getNextLexicalUnit();
                  if (var1 == null) {
                     throw this.createInvalidLexicalUnitDOMException((short)-1);
                  }

                  var5.append(this.getColorValue(var1));
               }

               return var4;
            }
         } else {
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
         }
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6 == SVGValueConstants.CURRENTCOLOR_VALUE) {
         var5.putColorRelative(var4, true);
         int var11 = var3.getColorIndex();
         return var3.getComputedStyle(var1, var2, var11);
      } else if (var6.getCssValueType() == 2) {
         ListValue var7 = (ListValue)var6;
         Value var8 = var7.item(0);
         Value var9 = super.computeValue(var1, var2, var3, var4, var5, var8);
         if (var9 != var8) {
            ListValue var10 = new ListValue(' ');
            var10.append(var9);
            var10.append(var7.item(1));
            return var10;
         } else {
            return var6;
         }
      } else {
         return super.computeValue(var1, var2, var3, var4, var5, var6);
      }
   }

   protected float getColorValue(LexicalUnit var1) {
      switch (var1.getLexicalUnitType()) {
         case 13:
            return (float)var1.getIntegerValue();
         case 14:
            return var1.getFloatValue();
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }
}
