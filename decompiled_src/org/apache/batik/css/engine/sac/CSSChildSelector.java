package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CSSChildSelector extends AbstractDescendantSelector {
   public CSSChildSelector(Selector var1, SimpleSelector var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 11;
   }

   public boolean match(Element var1, String var2) {
      Node var3 = var1.getParentNode();
      if (var3 != null && var3.getNodeType() == 1) {
         return ((ExtendedSelector)this.getAncestorSelector()).match((Element)var3, (String)null) && ((ExtendedSelector)this.getSimpleSelector()).match(var1, var2);
      } else {
         return false;
      }
   }

   public void fillAttributeSet(Set var1) {
      ((ExtendedSelector)this.getAncestorSelector()).fillAttributeSet(var1);
      ((ExtendedSelector)this.getSimpleSelector()).fillAttributeSet(var1);
   }

   public String toString() {
      SimpleSelector var1 = this.getSimpleSelector();
      return var1.getSelectorType() == 9 ? String.valueOf(this.getAncestorSelector()) + var1 : this.getAncestorSelector() + " > " + var1;
   }
}
