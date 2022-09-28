package org.apache.batik.css.engine.sac;

import org.apache.batik.css.engine.CSSStylableElement;
import org.w3c.dom.Element;

public class CSSClassCondition extends CSSAttributeCondition {
   public CSSClassCondition(String var1, String var2, String var3) {
      super(var1, var2, true, var3);
   }

   public short getConditionType() {
      return 9;
   }

   public boolean match(Element var1, String var2) {
      if (!(var1 instanceof CSSStylableElement)) {
         return false;
      } else {
         String var3 = ((CSSStylableElement)var1).getCSSClass();
         String var4 = this.getValue();
         int var5 = var3.length();
         int var6 = var4.length();

         for(int var7 = var3.indexOf(var4); var7 != -1; var7 = var3.indexOf(var4, var7 + var6)) {
            if ((var7 == 0 || Character.isSpaceChar(var3.charAt(var7 - 1))) && (var7 + var6 == var5 || Character.isSpaceChar(var3.charAt(var7 + var6)))) {
               return true;
            }
         }

         return false;
      }
   }

   public String toString() {
      return '.' + this.getValue();
   }
}
