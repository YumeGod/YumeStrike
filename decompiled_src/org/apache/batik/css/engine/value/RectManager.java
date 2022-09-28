package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public abstract class RectManager extends LengthManager {
   protected int orientation;

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 38:
            break;
         case 41:
            if (var1.getFunctionName().equalsIgnoreCase("rect")) {
               break;
            }
         default:
            throw this.createMalformedRectDOMException();
      }

      var1 = var1.getParameters();
      Value var3 = this.createRectComponent(var1);
      var1 = var1.getNextLexicalUnit();
      if (var1 != null && var1.getLexicalUnitType() == 0) {
         var1 = var1.getNextLexicalUnit();
         Value var4 = this.createRectComponent(var1);
         var1 = var1.getNextLexicalUnit();
         if (var1 != null && var1.getLexicalUnitType() == 0) {
            var1 = var1.getNextLexicalUnit();
            Value var5 = this.createRectComponent(var1);
            var1 = var1.getNextLexicalUnit();
            if (var1 != null && var1.getLexicalUnitType() == 0) {
               var1 = var1.getNextLexicalUnit();
               Value var6 = this.createRectComponent(var1);
               return new RectValue(var3, var4, var5, var6);
            } else {
               throw this.createMalformedRectDOMException();
            }
         } else {
            throw this.createMalformedRectDOMException();
         }
      } else {
         throw this.createMalformedRectDOMException();
      }
   }

   private Value createRectComponent(LexicalUnit var1) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 13:
            return new FloatValue((short)1, (float)var1.getIntegerValue());
         case 14:
            return new FloatValue((short)1, var1.getFloatValue());
         case 15:
            return new FloatValue((short)3, var1.getFloatValue());
         case 16:
            return new FloatValue((short)4, var1.getFloatValue());
         case 17:
            return new FloatValue((short)5, var1.getFloatValue());
         case 18:
            return new FloatValue((short)8, var1.getFloatValue());
         case 19:
            return new FloatValue((short)6, var1.getFloatValue());
         case 20:
            return new FloatValue((short)7, var1.getFloatValue());
         case 21:
            return new FloatValue((short)9, var1.getFloatValue());
         case 22:
            return new FloatValue((short)10, var1.getFloatValue());
         case 23:
            return new FloatValue((short)2, var1.getFloatValue());
         case 35:
            if (var1.getStringValue().equalsIgnoreCase("auto")) {
               return ValueConstants.AUTO_VALUE;
            }
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         default:
            throw this.createMalformedRectDOMException();
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getCssValueType() != 1) {
         return var6;
      } else if (var6.getPrimitiveType() != 24) {
         return var6;
      } else {
         RectValue var7 = (RectValue)var6;
         this.orientation = 1;
         Value var8 = super.computeValue(var1, var2, var3, var4, var5, var7.getTop());
         Value var9 = super.computeValue(var1, var2, var3, var4, var5, var7.getBottom());
         this.orientation = 0;
         Value var10 = super.computeValue(var1, var2, var3, var4, var5, var7.getLeft());
         Value var11 = super.computeValue(var1, var2, var3, var4, var5, var7.getRight());
         return (Value)(var8 == var7.getTop() && var11 == var7.getRight() && var9 == var7.getBottom() && var10 == var7.getLeft() ? var6 : new RectValue(var8, var11, var9, var10));
      }
   }

   protected int getOrientation() {
      return this.orientation;
   }

   private DOMException createMalformedRectDOMException() {
      Object[] var1 = new Object[]{this.getPropertyName()};
      String var2 = Messages.formatMessage("malformed.rect", var1);
      return new DOMException((short)12, var2);
   }
}
