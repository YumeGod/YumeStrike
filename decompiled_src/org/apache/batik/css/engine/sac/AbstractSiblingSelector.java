package org.apache.batik.css.engine.sac;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

public abstract class AbstractSiblingSelector implements SiblingSelector, ExtendedSelector {
   protected short nodeType;
   protected Selector selector;
   protected SimpleSelector simpleSelector;

   protected AbstractSiblingSelector(short var1, Selector var2, SimpleSelector var3) {
      this.nodeType = var1;
      this.selector = var2;
      this.simpleSelector = var3;
   }

   public short getNodeType() {
      return this.nodeType;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         AbstractSiblingSelector var2 = (AbstractSiblingSelector)var1;
         return var2.simpleSelector.equals(this.simpleSelector);
      } else {
         return false;
      }
   }

   public int getSpecificity() {
      return ((ExtendedSelector)this.selector).getSpecificity() + ((ExtendedSelector)this.simpleSelector).getSpecificity();
   }

   public Selector getSelector() {
      return this.selector;
   }

   public SimpleSelector getSiblingSelector() {
      return this.simpleSelector;
   }
}
