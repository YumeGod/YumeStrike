package org.apache.batik.css.parser;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public class DefaultDescendantSelector extends AbstractDescendantSelector {
   public DefaultDescendantSelector(Selector var1, SimpleSelector var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 10;
   }

   public String toString() {
      return this.getAncestorSelector() + " " + this.getSimpleSelector();
   }
}
