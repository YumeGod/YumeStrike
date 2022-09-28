package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.URIValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class ColorProfileManager extends AbstractValueManager {
   public boolean isInheritedProperty() {
      return true;
   }

   public String getPropertyName() {
      return "color-profile";
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 20;
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 24:
            return new URIValue(var1.getStringValue(), resolveURI(var2.getCSSBaseURI(), var1.getStringValue()));
         case 35:
            String var3 = var1.getStringValue().toLowerCase();
            if (var3.equals("auto")) {
               return ValueConstants.AUTO_VALUE;
            } else {
               if (var3.equals("srgb")) {
                  return SVGValueConstants.SRGB_VALUE;
               }

               return new StringValue((short)21, var3);
            }
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      switch (var1) {
         case 20:
            return new URIValue(var2, resolveURI(var3.getCSSBaseURI(), var2));
         case 21:
            String var4 = var2.toLowerCase();
            if (var4.equals("auto")) {
               return ValueConstants.AUTO_VALUE;
            } else {
               if (var4.equals("srgb")) {
                  return SVGValueConstants.SRGB_VALUE;
               }

               return new StringValue((short)21, var4);
            }
         default:
            throw this.createInvalidStringTypeDOMException(var1);
      }
   }
}
