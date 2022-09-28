package org.apache.batik.css.engine.sac;

import java.util.Set;
import org.w3c.css.sac.ElementSelector;

public abstract class AbstractElementSelector implements ElementSelector, ExtendedSelector {
   protected String namespaceURI;
   protected String localName;

   protected AbstractElementSelector(String var1, String var2) {
      this.namespaceURI = var1;
      this.localName = var2;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1.getClass() == this.getClass()) {
         AbstractElementSelector var2 = (AbstractElementSelector)var1;
         return var2.namespaceURI.equals(this.namespaceURI) && var2.localName.equals(this.localName);
      } else {
         return false;
      }
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }

   public void fillAttributeSet(Set var1) {
   }
}
