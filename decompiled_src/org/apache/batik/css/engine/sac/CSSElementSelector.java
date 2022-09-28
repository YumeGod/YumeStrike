package org.apache.batik.css.engine.sac;

import org.w3c.dom.Element;

public class CSSElementSelector extends AbstractElementSelector {
   public CSSElementSelector(String var1, String var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 4;
   }

   public boolean match(Element var1, String var2) {
      String var3 = this.getLocalName();
      if (var3 == null) {
         return true;
      } else {
         String var4;
         if (var1.getPrefix() == null) {
            var4 = var1.getNodeName();
         } else {
            var4 = var1.getLocalName();
         }

         return var4.equals(var3);
      }
   }

   public int getSpecificity() {
      return this.getLocalName() == null ? 0 : 1;
   }

   public String toString() {
      String var1 = this.getLocalName();
      return var1 == null ? "*" : var1;
   }
}
