package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.URIValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class SVGPaintManager extends SVGColorManager {
   public SVGPaintManager(String var1) {
      super(var1);
   }

   public SVGPaintManager(String var1, Value var2) {
      super(var1, var2);
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

   public int getPropertyType() {
      return 7;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 24:
            String var3 = var1.getStringValue();
            String var4 = resolveURI(var2.getCSSBaseURI(), var3);
            var1 = var1.getNextLexicalUnit();
            if (var1 == null) {
               return new URIValue(var3, var4);
            } else {
               ListValue var5 = new ListValue(' ');
               var5.append(new URIValue(var3, var4));
               if (var1.getLexicalUnitType() == 35 && var1.getStringValue().equalsIgnoreCase("none")) {
                  var5.append(ValueConstants.NONE_VALUE);
                  return var5;
               } else {
                  Value var6 = super.createValue(var1, var2);
                  if (var6.getCssValueType() == 3) {
                     ListValue var7 = (ListValue)var6;

                     for(int var8 = 0; var8 < var7.getLength(); ++var8) {
                        var5.append(var7.item(var8));
                     }
                  } else {
                     var5.append(var6);
                  }

                  return var5;
               }
            }
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("none")) {
               return ValueConstants.NONE_VALUE;
            }
         default:
            return super.createValue(var1, var2);
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6 == ValueConstants.NONE_VALUE) {
         return var6;
      } else {
         if (var6.getCssValueType() == 2) {
            ListValue var7 = (ListValue)var6;
            Value var8 = var7.item(0);
            if (var8.getPrimitiveType() == 20) {
               var8 = var7.item(1);
               if (var8 == ValueConstants.NONE_VALUE) {
                  return var6;
               }

               Value var9 = super.computeValue(var1, var2, var3, var4, var5, var8);
               if (var9 != var8) {
                  ListValue var10 = new ListValue(' ');
                  var10.append(var7.item(0));
                  var10.append(var9);
                  if (var7.getLength() == 3) {
                     var10.append(var7.item(1));
                  }

                  return var10;
               }

               return var6;
            }
         }

         return super.computeValue(var1, var2, var3, var4, var5, var6);
      }
   }
}
