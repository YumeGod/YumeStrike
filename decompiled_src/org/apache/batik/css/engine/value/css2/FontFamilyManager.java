package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class FontFamilyManager extends AbstractValueManager {
   protected static final ListValue DEFAULT_VALUE = new ListValue();
   protected static final StringMap values;

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
      return 26;
   }

   public String getPropertyName() {
      return "font-family";
   }

   public Value getDefaultValue() {
      return DEFAULT_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 35:
         case 36:
            ListValue var3 = new ListValue();

            do {
               switch (var1.getLexicalUnitType()) {
                  case 35:
                     StringBuffer var4 = new StringBuffer(var1.getStringValue());
                     var1 = var1.getNextLexicalUnit();
                     if (var1 != null && var1.getLexicalUnitType() == 35) {
                        do {
                           var4.append(' ');
                           var4.append(var1.getStringValue());
                           var1 = var1.getNextLexicalUnit();
                        } while(var1 != null && var1.getLexicalUnitType() == 35);

                        var3.append(new StringValue((short)19, var4.toString()));
                     } else {
                        String var5 = var4.toString();
                        String var6 = var5.toLowerCase().intern();
                        Value var7 = (Value)values.get(var6);
                        var3.append((Value)(var7 != null ? var7 : new StringValue((short)19, var5)));
                     }
                     break;
                  case 36:
                     var3.append(new StringValue((short)19, var1.getStringValue()));
                     var1 = var1.getNextLexicalUnit();
               }

               if (var1 == null) {
                  return var3;
               }

               if (var1.getLexicalUnitType() != 0) {
                  throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
               }

               var1 = var1.getNextLexicalUnit();
            } while(var1 != null);

            throw this.createMalformedLexicalUnitDOMException();
         default:
            throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6 == DEFAULT_VALUE) {
         CSSContext var7 = var3.getCSSContext();
         var6 = var7.getDefaultFontFamily();
      }

      return var6;
   }

   static {
      DEFAULT_VALUE.append(new StringValue((short)19, "Arial"));
      DEFAULT_VALUE.append(new StringValue((short)19, "Helvetica"));
      DEFAULT_VALUE.append(new StringValue((short)21, "sans-serif"));
      values = new StringMap();
      values.put("cursive", ValueConstants.CURSIVE_VALUE);
      values.put("fantasy", ValueConstants.FANTASY_VALUE);
      values.put("monospace", ValueConstants.MONOSPACE_VALUE);
      values.put("serif", ValueConstants.SERIF_VALUE);
      values.put("sans-serif", ValueConstants.SANS_SERIF_VALUE);
   }
}
