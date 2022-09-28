package org.apache.batik.css.parser;

import org.w3c.css.sac.Condition;

public class DefaultAndCondition extends AbstractCombinatorCondition {
   public DefaultAndCondition(Condition var1, Condition var2) {
      super(var1, var2);
   }

   public short getConditionType() {
      return 0;
   }

   public String toString() {
      return String.valueOf(this.getFirstCondition()) + this.getSecondCondition();
   }
}
