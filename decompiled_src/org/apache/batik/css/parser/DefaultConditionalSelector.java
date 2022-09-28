package org.apache.batik.css.parser;

import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;

public class DefaultConditionalSelector implements ConditionalSelector {
   protected SimpleSelector simpleSelector;
   protected Condition condition;

   public DefaultConditionalSelector(SimpleSelector var1, Condition var2) {
      this.simpleSelector = var1;
      this.condition = var2;
   }

   public short getSelectorType() {
      return 0;
   }

   public SimpleSelector getSimpleSelector() {
      return this.simpleSelector;
   }

   public Condition getCondition() {
      return this.condition;
   }

   public String toString() {
      return String.valueOf(this.simpleSelector) + this.condition;
   }
}
