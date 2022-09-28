package org.apache.batik.css.parser;

public class DefaultPseudoElementSelector extends AbstractElementSelector {
   public DefaultPseudoElementSelector(String var1, String var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 9;
   }

   public String toString() {
      return ":" + this.getLocalName();
   }
}
