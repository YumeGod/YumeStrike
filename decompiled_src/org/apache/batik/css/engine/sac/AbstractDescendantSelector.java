package org.apache.batik.css.engine.sac;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public abstract class AbstractDescendantSelector implements DescendantSelector, ExtendedSelector {
   protected Selector ancestorSelector;
   protected SimpleSelector simpleSelector;

   protected AbstractDescendantSelector(Selector var1, SimpleSelector var2) {
      this.ancestorSelector = var1;
      this.simpleSelector = var2;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         AbstractDescendantSelector var2 = (AbstractDescendantSelector)var1;
         return var2.simpleSelector.equals(this.simpleSelector);
      } else {
         return false;
      }
   }

   public int getSpecificity() {
      return ((ExtendedSelector)this.ancestorSelector).getSpecificity() + ((ExtendedSelector)this.simpleSelector).getSpecificity();
   }

   public Selector getAncestorSelector() {
      return this.ancestorSelector;
   }

   public SimpleSelector getSimpleSelector() {
      return this.simpleSelector;
   }
}
