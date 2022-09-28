package org.apache.batik.css.engine.sac;

import org.w3c.dom.Element;

public class CSSBeginHyphenAttributeCondition extends CSSAttributeCondition {
   public CSSBeginHyphenAttributeCondition(String var1, String var2, boolean var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public short getConditionType() {
      return 8;
   }

   public boolean match(Element var1, String var2) {
      return var1.getAttribute(this.getLocalName()).startsWith(this.getValue());
   }

   public String toString() {
      return '[' + this.getLocalName() + "|=\"" + this.getValue() + "\"]";
   }
}
