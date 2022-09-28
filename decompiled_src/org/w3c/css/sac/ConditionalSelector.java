package org.w3c.css.sac;

public interface ConditionalSelector extends SimpleSelector {
   SimpleSelector getSimpleSelector();

   Condition getCondition();
}
