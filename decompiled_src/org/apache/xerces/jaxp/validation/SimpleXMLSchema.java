package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class SimpleXMLSchema extends AbstractXMLSchema implements XMLGrammarPool {
   private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
   private Grammar fGrammar;
   private Grammar[] fGrammars;
   private XMLGrammarDescription fGrammarDescription;

   public SimpleXMLSchema(Grammar var1) {
      this.fGrammar = var1;
      this.fGrammars = new Grammar[]{var1};
      this.fGrammarDescription = var1.getGrammarDescription();
   }

   public Grammar[] retrieveInitialGrammarSet(String var1) {
      return "http://www.w3.org/2001/XMLSchema".equals(var1) ? (Grammar[])this.fGrammars.clone() : ZERO_LENGTH_GRAMMAR_ARRAY;
   }

   public void cacheGrammars(String var1, Grammar[] var2) {
   }

   public Grammar retrieveGrammar(XMLGrammarDescription var1) {
      return this.fGrammarDescription.equals(var1) ? this.fGrammar : null;
   }

   public void lockPool() {
   }

   public void unlockPool() {
   }

   public void clear() {
   }

   public XMLGrammarPool getGrammarPool() {
      return this;
   }

   public boolean isFullyComposed() {
      return true;
   }
}
