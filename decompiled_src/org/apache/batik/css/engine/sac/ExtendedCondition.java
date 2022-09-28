package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Condition;
import org.w3c.dom.Element;

public interface ExtendedCondition extends Condition {
   boolean match(Element var1, String var2);

   int getSpecificity();

   void fillAttributeSet(Set var1);
}
