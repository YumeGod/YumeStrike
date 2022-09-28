package org.apache.batik.css.parser;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public class DefaultChildSelector extends AbstractDescendantSelector {
   public DefaultChildSelector(Selector var1, SimpleSelector var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 11;
   }

   public String toString() {
      SimpleSelector var1 = this.getSimpleSelector();
      return var1.getSelectorType() == 9 ? String.valueOf(this.getAncestorSelector()) + var1 : this.getAncestorSelector() + " > " + var1;
   }
}
