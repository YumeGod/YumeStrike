package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class XMLSchema extends AbstractXMLSchema {
   private final XMLGrammarPool fGrammarPool;

   public XMLSchema(XMLGrammarPool var1) {
      this.fGrammarPool = var1;
   }

   public XMLGrammarPool getGrammarPool() {
      return this.fGrammarPool;
   }

   public boolean isFullyComposed() {
      return true;
   }
}
