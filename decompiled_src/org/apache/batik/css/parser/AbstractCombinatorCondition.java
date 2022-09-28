package org.apache.batik.css.parser;

import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;

public abstract class AbstractCombinatorCondition implements CombinatorCondition {
   protected Condition firstCondition;
   protected Condition secondCondition;

   protected AbstractCombinatorCondition(Condition var1, Condition var2) {
      this.firstCondition = var1;
      this.secondCondition = var2;
   }

   public Condition getFirstCondition() {
      return this.firstCondition;
   }

   public Condition getSecondCondition() {
      return this.secondCondition;
   }
}
