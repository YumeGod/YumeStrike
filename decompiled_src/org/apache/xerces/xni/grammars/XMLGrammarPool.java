package org.apache.xerces.xni.grammars;

public interface XMLGrammarPool {
   Grammar[] retrieveInitialGrammarSet(String var1);

   void cacheGrammars(String var1, Grammar[] var2);

   Grammar retrieveGrammar(XMLGrammarDescription var1);

   void lockPool();

   void unlockPool();

   void clear();
}
