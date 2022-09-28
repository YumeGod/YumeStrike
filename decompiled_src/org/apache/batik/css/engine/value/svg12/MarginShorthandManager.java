package org.apache.batik.css.engine.value.svg12;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.AbstractValueFactory;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class MarginShorthandManager extends AbstractValueFactory implements ShorthandManager {
   public String getPropertyName() {
      return "margin";
   }

   public boolean isAnimatableProperty() {
      return true;
   }

   public boolean isAdditiveProperty() {
      return false;
   }

   public void setValues(CSSEngine var1, ShorthandManager.PropertyHandler var2, LexicalUnit var3, boolean var4) throws DOMException {
      if (var3.getLexicalUnitType() != 12) {
         LexicalUnit[] var5 = new LexicalUnit[4];

         int var6;
         for(var6 = 0; var3 != null; var3 = var3.getNextLexicalUnit()) {
            if (var6 == 4) {
               throw this.createInvalidLexicalUnitDOMException(var3.getLexicalUnitType());
            }

            var5[var6++] = var3;
         }

         switch (var6) {
            case 1:
               var5[3] = var5[2] = var5[1] = var5[0];
               break;
            case 2:
               var5[2] = var5[0];
               var5[3] = var5[1];
               break;
            case 3:
               var5[3] = var5[1];
         }

         var2.property("margin-top", var5[0], var4);
         var2.property("margin-right", var5[1], var4);
         var2.property("margin-bottom", var5[2], var4);
         var2.property("margin-left", var5[3], var4);
      }
   }
}
