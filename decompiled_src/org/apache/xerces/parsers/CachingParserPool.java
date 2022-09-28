package org.apache.xerces.parsers;

import org.apache.xerces.util.ShadowedSymbolTable;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class CachingParserPool {
   public static final boolean DEFAULT_SHADOW_SYMBOL_TABLE = false;
   public static final boolean DEFAULT_SHADOW_GRAMMAR_POOL = false;
   protected SymbolTable fSynchronizedSymbolTable;
   protected XMLGrammarPool fSynchronizedGrammarPool;
   protected boolean fShadowSymbolTable;
   protected boolean fShadowGrammarPool;

   public CachingParserPool() {
      this(new SymbolTable(), new XMLGrammarPoolImpl());
   }

   public CachingParserPool(SymbolTable var1, XMLGrammarPool var2) {
      this.fShadowSymbolTable = false;
      this.fShadowGrammarPool = false;
      this.fSynchronizedSymbolTable = new SynchronizedSymbolTable(var1);
      this.fSynchronizedGrammarPool = new SynchronizedGrammarPool(var2);
   }

   public SymbolTable getSymbolTable() {
      return this.fSynchronizedSymbolTable;
   }

   public XMLGrammarPool getXMLGrammarPool() {
      return this.fSynchronizedGrammarPool;
   }

   public void setShadowSymbolTable(boolean var1) {
      this.fShadowSymbolTable = var1;
   }

   public DOMParser createDOMParser() {
      Object var1 = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
      Object var2 = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
      return new DOMParser((SymbolTable)var1, (XMLGrammarPool)var2);
   }

   public SAXParser createSAXParser() {
      Object var1 = this.fShadowSymbolTable ? new ShadowedSymbolTable(this.fSynchronizedSymbolTable) : this.fSynchronizedSymbolTable;
      Object var2 = this.fShadowGrammarPool ? new ShadowedGrammarPool(this.fSynchronizedGrammarPool) : this.fSynchronizedGrammarPool;
      return new SAXParser((SymbolTable)var1, (XMLGrammarPool)var2);
   }

   public static final class ShadowedGrammarPool extends XMLGrammarPoolImpl {
      private XMLGrammarPool fGrammarPool;

      public ShadowedGrammarPool(XMLGrammarPool var1) {
         this.fGrammarPool = var1;
      }

      public Grammar[] retrieveInitialGrammarSet(String var1) {
         Grammar[] var2 = super.retrieveInitialGrammarSet(var1);
         return var2 != null ? var2 : this.fGrammarPool.retrieveInitialGrammarSet(var1);
      }

      public Grammar retrieveGrammar(XMLGrammarDescription var1) {
         Grammar var2 = super.retrieveGrammar(var1);
         return var2 != null ? var2 : this.fGrammarPool.retrieveGrammar(var1);
      }

      public void cacheGrammars(String var1, Grammar[] var2) {
         super.cacheGrammars(var1, var2);
         this.fGrammarPool.cacheGrammars(var1, var2);
      }

      public Grammar getGrammar(XMLGrammarDescription var1) {
         return super.containsGrammar(var1) ? super.getGrammar(var1) : null;
      }

      public boolean containsGrammar(XMLGrammarDescription var1) {
         return super.containsGrammar(var1);
      }
   }

   public static final class SynchronizedGrammarPool implements XMLGrammarPool {
      private XMLGrammarPool fGrammarPool;

      public SynchronizedGrammarPool(XMLGrammarPool var1) {
         this.fGrammarPool = var1;
      }

      public Grammar[] retrieveInitialGrammarSet(String var1) {
         XMLGrammarPool var2 = this.fGrammarPool;
         synchronized(var2) {
            Grammar[] var3 = this.fGrammarPool.retrieveInitialGrammarSet(var1);
            return var3;
         }
      }

      public Grammar retrieveGrammar(XMLGrammarDescription var1) {
         XMLGrammarPool var2 = this.fGrammarPool;
         synchronized(var2) {
            Grammar var3 = this.fGrammarPool.retrieveGrammar(var1);
            return var3;
         }
      }

      public void cacheGrammars(String var1, Grammar[] var2) {
         XMLGrammarPool var3 = this.fGrammarPool;
         synchronized(var3) {
            this.fGrammarPool.cacheGrammars(var1, var2);
         }
      }

      public void lockPool() {
         XMLGrammarPool var1 = this.fGrammarPool;
         synchronized(var1) {
            this.fGrammarPool.lockPool();
         }
      }

      public void clear() {
         XMLGrammarPool var1 = this.fGrammarPool;
         synchronized(var1) {
            this.fGrammarPool.clear();
         }
      }

      public void unlockPool() {
         XMLGrammarPool var1 = this.fGrammarPool;
         synchronized(var1) {
            this.fGrammarPool.unlockPool();
         }
      }
   }
}
