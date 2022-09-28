package org.apache.batik.css.parser;

import org.w3c.css.sac.ElementSelector;

public abstract class AbstractElementSelector implements ElementSelector {
   protected String namespaceURI;
   protected String localName;

   protected AbstractElementSelector(String var1, String var2) {
      this.namespaceURI = var1;
      this.localName = var2;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalName() {
      return this.localName;
   }
}
