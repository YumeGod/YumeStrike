package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CSSDescendantSelector extends AbstractDescendantSelector {
   public CSSDescendantSelector(Selector var1, SimpleSelector var2) {
      super(var1, var2);
   }

   public short getSelectorType() {
      return 10;
   }

   public boolean match(Element var1, String var2) {
      ExtendedSelector var3 = (ExtendedSelector)this.getAncestorSelector();
      if (!((ExtendedSelector)this.getSimpleSelector()).match(var1, var2)) {
         return false;
      } else {
         for(Node var4 = var1.getParentNode(); var4 != null; var4 = var4.getParentNode()) {
            if (var4.getNodeType() == 1 && var3.match((Element)var4, (String)null)) {
               return true;
            }
         }

         return false;
      }
   }

   public void fillAttributeSet(Set var1) {
      ((ExtendedSelector)this.getSimpleSelector()).fillAttributeSet(var1);
   }

   public String toString() {
      return this.getAncestorSelector() + " " + this.getSimpleSelector();
   }
}
