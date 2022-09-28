package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class EmptyXMLSchema extends AbstractXMLSchema implements XMLGrammarPool {
   private static EmptyXMLSchema EMPTY_XML_SCHEMA_INSTANCE = new EmptyXMLSchema();
   private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];

   public static EmptyXMLSchema getInstance() {
      return EMPTY_XML_SCHEMA_INSTANCE;
   }

   private EmptyXMLSchema() {
   }

   public Grammar[] retrieveInitialGrammarSet(String var1) {
      return ZERO_LENGTH_GRAMMAR_ARRAY;
   }

   public void cacheGrammars(String var1, Grammar[] var2) {
   }

   public Grammar retrieveGrammar(XMLGrammarDescription var1) {
      return null;
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
