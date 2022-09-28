package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public abstract class IdentifierManager extends AbstractValueManager {
   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
            String var3 = var1.getStringValue().toLowerCase().intern();
            Object var4 = this.getIdentifiers().get(var3);
            if (var4 == null) {
               throw this.createInvalidIdentifierDOMException(var1.getStringValue());
            }

            return (Value)var4;
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else {
         Object var4 = this.getIdentifiers().get(var2.toLowerCase().intern());
         if (var4 == null) {
            throw this.createInvalidIdentifierDOMException(var2);
         } else {
            return (Value)var4;
         }
      }
   }

   public abstract StringMap getIdentifiers();
}
