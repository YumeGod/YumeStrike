package org.apache.batik.css.engine.sac;

import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;

public abstract class AbstractCombinatorCondition implements CombinatorCondition, ExtendedCondition {
   protected Condition firstCondition;
   protected Condition secondCondition;

   protected AbstractCombinatorCondition(Condition var1, Condition var2) {
      this.firstCondition = var1;
      this.secondCondition = var2;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         AbstractCombinatorCondition var2 = (AbstractCombinatorCondition)var1;
         return var2.firstCondition.equals(this.firstCondition) && var2.secondCondition.equals(this.secondCondition);
      } else {
         return false;
      }
   }

   public int getSpecificity() {
      return ((ExtendedCondition)this.getFirstCondition()).getSpecificity() + ((ExtendedCondition)this.getSecondCondition()).getSpecificity();
   }

   public Condition getFirstCondition() {
      return this.firstCondition;
   }

   public Condition getSecondCondition() {
      return this.secondCondition;
   }
}
