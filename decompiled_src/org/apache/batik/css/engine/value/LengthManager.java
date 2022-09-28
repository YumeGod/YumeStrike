package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public abstract class LengthManager extends AbstractValueManager {
   static final double SQRT2 = Math.sqrt(2.0);
   protected static final int HORIZONTAL_ORIENTATION = 0;
   protected static final int VERTICAL_ORIENTATION = 1;
   protected static final int BOTH_ORIENTATION = 2;

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
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
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value createFloatValue(short var1, float var2) throws DOMException {
      switch (var1) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
            return new FloatValue(var1, var2);
         default:
            throw this.createInvalidFloatTypeDOMException(var1);
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getCssValueType() != 1) {
         return var6;
      } else {
         CSSContext var7;
         float var8;
         int var9;
         float var10;
         switch (var6.getPrimitiveType()) {
            case 1:
            case 5:
               return var6;
            case 2:
               var7 = var3.getCSSContext();
               switch (this.getOrientation()) {
                  case 0:
                     var5.putBlockWidthRelative(var4, true);
                     var10 = var6.getFloatValue() * var7.getBlockWidth(var1) / 100.0F;
                     break;
                  case 1:
                     var5.putBlockHeightRelative(var4, true);
                     var10 = var6.getFloatValue() * var7.getBlockHeight(var1) / 100.0F;
                     break;
                  default:
                     var5.putBlockWidthRelative(var4, true);
                     var5.putBlockHeightRelative(var4, true);
                     double var11 = (double)var7.getBlockWidth(var1);
                     double var13 = (double)var7.getBlockHeight(var1);
                     var10 = (float)((double)var6.getFloatValue() * (Math.sqrt(var11 * var11 + var13 * var13) / SQRT2) / 100.0);
               }

               return new FloatValue((short)1, var10);
            case 3:
               var5.putFontSizeRelative(var4, true);
               var8 = var6.getFloatValue();
               var9 = var3.getFontSizeIndex();
               var10 = var3.getComputedStyle(var1, var2, var9).getFloatValue();
               return new FloatValue((short)1, var8 * var10);
            case 4:
               var5.putFontSizeRelative(var4, true);
               var8 = var6.getFloatValue();
               var9 = var3.getFontSizeIndex();
               var10 = var3.getComputedStyle(var1, var2, var9).getFloatValue();
               return new FloatValue((short)1, var8 * var10 * 0.5F);
            case 6:
               var7 = var3.getCSSContext();
               var8 = var6.getFloatValue();
               return new FloatValue((short)1, var8 * 10.0F / var7.getPixelUnitToMillimeter());
            case 7:
               var7 = var3.getCSSContext();
               var8 = var6.getFloatValue();
               return new FloatValue((short)1, var8 / var7.getPixelUnitToMillimeter());
            case 8:
               var7 = var3.getCSSContext();
               var8 = var6.getFloatValue();
               return new FloatValue((short)1, var8 * 25.4F / var7.getPixelUnitToMillimeter());
            case 9:
               var7 = var3.getCSSContext();
               var8 = var6.getFloatValue();
               return new FloatValue((short)1, var8 * 25.4F / (72.0F * var7.getPixelUnitToMillimeter()));
            case 10:
               var7 = var3.getCSSContext();
               var8 = var6.getFloatValue();
               return new FloatValue((short)1, var8 * 25.4F / (6.0F * var7.getPixelUnitToMillimeter()));
            default:
               return var6;
         }
      }
   }

   protected abstract int getOrientation();
}
