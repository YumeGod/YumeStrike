package org.apache.batik.css.parser;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public abstract class AbstractDescendantSelector implements DescendantSelector {
   protected Selector ancestorSelector;
   protected SimpleSelector simpleSelector;

   protected AbstractDescendantSelector(Selector var1, SimpleSelector var2) {
      this.ancestorSelector = var1;
      this.simpleSelector = var2;
   }

   public Selector getAncestorSelector() {
      return this.ancestorSelector;
   }

   public SimpleSelector getSimpleSelector() {
      return this.simpleSelector;
   }
}
