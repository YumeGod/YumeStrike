package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Condition;
import org.w3c.dom.Element;

public class CSSAndCondition extends AbstractCombinatorCondition {
   public CSSAndCondition(Condition var1, Condition var2) {
      super(var1, var2);
   }

   public short getConditionType() {
      return 0;
   }

   public boolean match(Element var1, String var2) {
      return ((ExtendedCondition)this.getFirstCondition()).match(var1, var2) && ((ExtendedCondition)this.getSecondCondition()).match(var1, var2);
   }

   public void fillAttributeSet(Set var1) {
      ((ExtendedCondition)this.getFirstCondition()).fillAttributeSet(var1);
      ((ExtendedCondition)this.getSecondCondition()).fillAttributeSet(var1);
   }

   public String toString() {
      return String.valueOf(this.getFirstCondition()) + this.getSecondCondition();
   }
}
