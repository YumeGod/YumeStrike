package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.AbstractValueManager;
import org.apache.batik.css.engine.value.ListValue;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.URIValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class CursorManager extends AbstractValueManager {
   protected static final StringMap values = new StringMap();

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
      return 21;
   }

   public String getPropertyName() {
      return "cursor";
   }

   public Value getDefaultValue() {
      return ValueConstants.AUTO_VALUE;
   }

   public Value createValue(LexicalUnit var1, CSSEngine var2) throws DOMException {
      ListValue var3 = new ListValue();
      switch (var1.getLexicalUnitType()) {
         case 12:
            return ValueConstants.INHERIT_VALUE;
         case 24:
            do {
               var3.append(new URIValue(var1.getStringValue(), resolveURI(var2.getCSSBaseURI(), var1.getStringValue())));
               var1 = var1.getNextLexicalUnit();
               if (var1 == null) {
                  throw this.createMalformedLexicalUnitDOMException();
               }

               if (var1.getLexicalUnitType() != 0) {
                  throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
               }

               var1 = var1.getNextLexicalUnit();
               if (var1 == null) {
                  throw this.createMalformedLexicalUnitDOMException();
               }
            } while(var1.getLexicalUnitType() == 24);

            if (var1.getLexicalUnitType() != 35) {
               throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
            }
         case 35:
            String var4 = var1.getStringValue().toLowerCase().intern();
            Object var5 = values.get(var4);
            if (var5 == null) {
               throw this.createInvalidIdentifierDOMException(var1.getStringValue());
            }

            var3.append((Value)var5);
            var1 = var1.getNextLexicalUnit();
      }

      if (var1 != null) {
         throw this.createInvalidLexicalUnitDOMException(var1.getLexicalUnitType());
      } else {
         return var3;
      }
   }

   public Value computeValue(CSSStylableElement var1, String var2, CSSEngine var3, int var4, StyleMap var5, Value var6) {
      if (var6.getCssValueType() == 2) {
         ListValue var7 = (ListValue)var6;
         int var8 = var7.getLength();
         ListValue var9 = new ListValue(' ');

         for(int var10 = 0; var10 < var8; ++var10) {
            Value var11 = var7.item(0);
            if (var11.getPrimitiveType() == 20) {
               var9.append(new URIValue(var11.getStringValue(), var11.getStringValue()));
            } else {
               var9.append(var11);
            }
         }

         return var9;
      } else {
         return super.computeValue(var1, var2, var3, var4, var5, var6);
      }
   }

   static {
      values.put("auto", ValueConstants.AUTO_VALUE);
      values.put("crosshair", ValueConstants.CROSSHAIR_VALUE);
      values.put("default", ValueConstants.DEFAULT_VALUE);
      values.put("e-resize", ValueConstants.E_RESIZE_VALUE);
      values.put("help", ValueConstants.HELP_VALUE);
      values.put("move", ValueConstants.MOVE_VALUE);
      values.put("n-resize", ValueConstants.N_RESIZE_VALUE);
      values.put("ne-resize", ValueConstants.NE_RESIZE_VALUE);
      values.put("nw-resize", ValueConstants.NW_RESIZE_VALUE);
      values.put("pointer", ValueConstants.POINTER_VALUE);
      values.put("s-resize", ValueConstants.S_RESIZE_VALUE);
      values.put("se-resize", ValueConstants.SE_RESIZE_VALUE);
      values.put("sw-resize", ValueConstants.SW_RESIZE_VALUE);
      values.put("text", ValueConstants.TEXT_VALUE);
      values.put("w-resize", ValueConstants.W_RESIZE_VALUE);
      values.put("wait", ValueConstants.WAIT_VALUE);
   }
}
