package org.apache.batik.css.engine.sac;

import org.w3c.dom.Element;

public class CSSPseudoElementSelector extends AbstractElementSelector {
   public CSSPseudoElementSelector(String var1, String var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 9;
   }

   public boolean match(Element var1, String var2) {
      return this.getLocalName().equalsIgnoreCase(var2);
   }

   public int getSpecificity() {
      return 0;
   }

   public String toString() {
      return ":" + this.getLocalName();
   }
}
