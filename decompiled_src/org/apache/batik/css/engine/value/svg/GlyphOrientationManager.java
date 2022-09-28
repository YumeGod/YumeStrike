package org.apache.batik.css.engine.value.svg;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public abstract class GlyphOrientationManager extends AbstractValueManager {
   public boolean isInheritedProperty() {
      return true;
   }

   public boolean isAnimatableProperty() {
      return false;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 5;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 13:
            int var4 = var1.getIntegerValue();
            return new FloatValue((short)11, (float)var4);
         case 14:
            float var3 = var1.getFloatValue();
            return new FloatValue((short)11, var3);
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
         case 28:
            return new FloatValue((short)11, var1.getFloatValue());
         case 29:
            return new FloatValue((short)13, var1.getFloatValue());
         case 30:
            return new FloatValue((short)12, var1.getFloatValue());
      }
   }

   public Value createFloatValue(short var1, float var2) throws DOMException {
      switch (var1) {
         case 11:
         case 12:
         case 13:
            return new FloatValue(var1, var2);
         default:
            throw this.createInvalidFloatValueDOMException(var2);
      }
   }
}
