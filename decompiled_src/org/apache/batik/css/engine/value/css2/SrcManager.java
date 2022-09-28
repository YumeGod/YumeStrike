package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.URIValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class SrcManager extends IdentifierManager {
   protected static final StringMap values = new StringMap();

   public boolean isInheritedProperty() {
      return false;
   }

   public boolean isAnimatableProperty() {
      return false;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public int getPropertyType() {
      return 38;
   }

   public String getPropertyName() {
      return "src";
   }

   public Value getDefaultValue() {
      return ValueConstants.NONE_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 24:
         case 35:
         case 36:
            ListValue var3 = new ListValue();

            do {
               switch (var1.getLexicalUnitType()) {
                  case 24:
                     String var4 = resolveURI(var2.getCSSBaseURI(), var1.getStringValue());
                     var3.append(new URIValue(var1.getStringValue(), var4));
                     var1 = var1.getNextLexicalUnit();
                     if (var1 != null && var1.getLexicalUnitType() == 41 && var1.getFunctionName().equalsIgnoreCase("format")) {
                        var1 = var1.getNextLexicalUnit();
                     }
                     break;
                  case 35:
                     StringBuffer var5 = new StringBuffer(var1.getStringValue());
                     var1 = var1.getNextLexicalUnit();
                     if (var1 != null && var1.getLexicalUnitType() == 35) {
                        do {
                           var5.append(' ');
                           var5.append(var1.getStringValue());
                           var1 = var1.getNextLexicalUnit();
                        } while(var1 != null && var1.getLexicalUnitType() == 35);

                        var3.append(new StringValue((short)19, var5.toString()));
                     } else {
                        String var6 = var5.toString();
                        String var7 = var6.toLowerCase().intern();
                        Value var8 = (Value)values.get(var7);
                        var3.append((Value)(var8 != null ? var8 : new StringValue((short)19, var6)));
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

   public StringMap getIdentifiers() {
      return values;
   }

   static {
      values.put("none", ValueConstants.NONE_VALUE);
   }
}
