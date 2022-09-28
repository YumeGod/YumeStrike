package org.apache.batik.css.engine.sac;

import org.w3c.dom.Element;

public class CSSOneOfAttributeCondition extends CSSAttributeCondition {
   public CSSOneOfAttributeCondition(String var1, String var2, boolean var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public short getConditionType() {
      return 7;
   }

   public boolean match(Element var1, String var2) {
      String var3 = var1.getAttribute(this.getLocalName());
      String var4 = this.getValue();
      int var5 = var3.indexOf(var4);
      if (var5 == -1) {
         return false;
      } else if (var5 != 0 && !Character.isSpaceChar(var3.charAt(var5 - 1))) {
         return false;
      } else {
         int var6 = var5 + var4.length();
         return var6 == var3.length() || var6 < var3.length() && Character.isSpaceChar(var3.charAt(var6));
      }
   }

   public String toString() {
      return "[" + this.getLocalName() + "~=\"" + this.getValue() + "\"]";
   }
}
