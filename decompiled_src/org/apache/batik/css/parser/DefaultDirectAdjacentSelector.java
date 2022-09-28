package org.apache.batik.css.parser;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public class DefaultDirectAdjacentSelector extends AbstractSiblingSelector {
   public DefaultDirectAdjacentSelector(short var1, Selector var2, SimpleSelector var3) {
      super(var1, var2, var3);
   }

   public short getSelectorType() {
      return 12;
   }

   public String toString() {
      return this.getSelector() + " + " + this.getSiblingSelector();
   }
}
