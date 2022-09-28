package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.Element;

public class CSSConditionalSelector implements ConditionalSelector, ExtendedSelector {
   protected SimpleSelector simpleSelector;
   protected Condition condition;

   public CSSConditionalSelector(SimpleSelector var1, Condition var2) {
      this.simpleSelector = var1;
      this.condition = var2;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         CSSConditionalSelector var2 = (CSSConditionalSelector)var1;
         return var2.simpleSelector.equals(this.simpleSelector) && var2.condition.equals(this.condition);
      } else {
         return false;
      }
   }

   public short getSelectorType() {
      return 0;
   }

   public boolean match(Element var1, String var2) {
      return ((ExtendedSelector)this.getSimpleSelector()).match(var1, var2) && ((ExtendedCondition)this.getCondition()).match(var1, var2);
   }

   public void fillAttributeSet(Set var1) {
      ((ExtendedSelector)this.getSimpleSelector()).fillAttributeSet(var1);
      ((ExtendedCondition)this.getCondition()).fillAttributeSet(var1);
   }

   public int getSpecificity() {
      return ((ExtendedSelector)this.getSimpleSelector()).getSpecificity() + ((ExtendedCondition)this.getCondition()).getSpecificity();
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
