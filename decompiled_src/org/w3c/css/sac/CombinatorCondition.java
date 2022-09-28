package org.w3c.css.sac;

public interface CombinatorCondition extends Condition {
   Condition getFirstCondition();

   Condition getSecondCondition();
}
