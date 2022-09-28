package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.URIValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class ClipPathManager extends AbstractValueManager {
   public boolean isInheritedProperty() {
      return false;
   }

   public String getPropertyName() {
      return "clip-path";
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
      return ValueConstants.NONE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 24:
            return new URIValue(var1.getStringValue(), resolveURI(var2.getCSSBaseURI(), var1.getStringValue()));
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("none")) {
               return ValueConstants.NONE_VALUE;
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
            if (var2.equalsIgnoreCase("none")) {
               return ValueConstants.NONE_VALUE;
            }
         default:
            throw this.createInvalidStringTypeDOMException(var1);
      }
   }
}
