package org.apache.batik.css.parser;

public class DefaultElementSelector extends AbstractElementSelector {
   public DefaultElementSelector(String var1, String var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 4;
   }

   public String toString() {
      String var1 = this.getLocalName();
      return var1 == null ? "*" : var1;
   }
}
