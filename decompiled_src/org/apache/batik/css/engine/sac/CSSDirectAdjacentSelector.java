package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CSSDirectAdjacentSelector extends AbstractSiblingSelector {
   public CSSDirectAdjacentSelector(short var1, Selector var2, SimpleSelector var3) {
      super(var1, var2, var3);
   }

   public short getSelectorType() {
      return 12;
   }

   public boolean match(Element var1, String var2) {
      Object var3 = var1;
      if (!((ExtendedSelector)this.getSiblingSelector()).match(var1, var2)) {
         return false;
      } else {
         while((var3 = ((Node)var3).getPreviousSibling()) != null && ((Node)var3).getNodeType() != 1) {
         }

         return var3 == null ? false : ((ExtendedSelector)this.getSelector()).match((Element)var3, (String)null);
      }
   }

   public void fillAttributeSet(Set var1) {
      ((ExtendedSelector)this.getSelector()).fillAttributeSet(var1);
      ((ExtendedSelector)this.getSiblingSelector()).fillAttributeSet(var1);
   }

   public String toString() {
      return this.getSelector() + " + " + this.getSiblingSelector();
   }
}
