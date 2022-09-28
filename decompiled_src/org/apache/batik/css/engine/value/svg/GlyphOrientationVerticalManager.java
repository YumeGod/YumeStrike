package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class GlyphOrientationVerticalManager extends GlyphOrientationManager {
   public String getPropertyName() {
      return "glyph-orientation-vertical";
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      if (var1.getLexicalUnitType() == 35) {
         if (var1.getStringValue().equalsIgnoreCase("auto")) {
            return ValueConstants.AUTO_VALUE;
         } else {
            throw this.createInvalidIdentifierDOMException(var1.getStringValue());
         }
      } else {
         return super.createValue(var1, var2);
      }
   }

   public Value createStringValue(short var1, String var2, CSSEngine var3) throws DOMException {
      if (var1 != 21) {
         throw this.createInvalidStringTypeDOMException(var1);
      } else if (var2.equalsIgnoreCase("auto")) {
         return ValueConstants.AUTO_VALUE;
      } else {
         throw this.createInvalidIdentifierDOMException(var2);
      }
   }
}
