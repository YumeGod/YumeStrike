package org.apache.xerces.util;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class XMLGrammarPoolImpl implements XMLGrammarPool {
   protected static final int TABLE_SIZE = 11;
   protected Entry[] fGrammars = null;
   protected boolean fPoolIsLocked;
   protected int fGrammarCount = 0;
   private static final boolean DEBUG = false;

   public XMLGrammarPoolImpl() {
      this.fGrammars = new Entry[11];
      this.fPoolIsLocked = false;
   }

   public XMLGrammarPoolImpl(int var1) {
      this.fGrammars = new Entry[var1];
      this.fPoolIsLocked = false;
   }

   public Grammar[] retrieveInitialGrammarSet(String var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         int var3 = this.fGrammars.length;
         Grammar[] var4 = new Grammar[this.fGrammarCount];
         int var5 = 0;

         for(int var6 = 0; var6 < var3; ++var6) {
            for(Entry var7 = this.fGrammars[var6]; var7 != null; var7 = var7.next) {
               if (var7.desc.getGrammarType().equals(var1)) {
                  var4[var5++] = var7.grammar;
               }
            }
         }

         Grammar[] var11 = new Grammar[var5];
         System.arraycopy(var4, 0, var11, 0, var5);
         return var11;
      }
   }

   public void cacheGrammars(String var1, Grammar[] var2) {
      if (!this.fPoolIsLocked) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.putGrammar(var2[var3]);
         }
      }

   }

   public Grammar retrieveGrammar(XMLGrammarDescription var1) {
      return this.getGrammar(var1);
   }

   public void putGrammar(Grammar var1) {
      if (!this.fPoolIsLocked) {
         Entry[] var2 = this.fGrammars;
         synchronized(var2) {
            XMLGrammarDescription var3 = var1.getGrammarDescription();
            int var4 = this.hashCode(var3);
            int var5 = (var4 & Integer.MAX_VALUE) % this.fGrammars.length;

            for(Entry var6 = this.fGrammars[var5]; var6 != null; var6 = var6.next) {
               if (var6.hash == var4 && this.equals(var6.desc, var3)) {
                  var6.grammar = var1;
                  return;
               }
            }

            Entry var7 = new Entry(var4, var3, var1, this.fGrammars[var5]);
            this.fGrammars[var5] = var7;
            ++this.fGrammarCount;
         }
      }

   }

   public Grammar getGrammar(XMLGrammarDescription var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;

         Grammar var6;
         for(Entry var5 = this.fGrammars[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && this.equals(var5.desc, var1)) {
               var6 = var5.grammar;
               return var6;
            }
         }

         var6 = null;
         return var6;
      }
   }

   public Grammar removeGrammar(XMLGrammarDescription var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;
         Entry var5 = this.fGrammars[var4];

         Grammar var7;
         for(Entry var6 = null; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && this.equals(var5.desc, var1)) {
               if (var6 != null) {
                  var6.next = var5.next;
               } else {
                  this.fGrammars[var4] = var5.next;
               }

               var7 = var5.grammar;
               var5.grammar = null;
               --this.fGrammarCount;
               return var7;
            }

            var6 = var5;
         }

         var7 = null;
         return var7;
      }
   }

   public boolean containsGrammar(XMLGrammarDescription var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;

         boolean var6;
         for(Entry var5 = this.fGrammars[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && this.equals(var5.desc, var1)) {
               var6 = true;
               return var6;
            }
         }

         var6 = false;
         return var6;
      }
   }

   public void lockPool() {
      this.fPoolIsLocked = true;
   }

   public void unlockPool() {
      this.fPoolIsLocked = false;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.fGrammars.length; ++var1) {
         if (this.fGrammars[var1] != null) {
            this.fGrammars[var1].clear();
            this.fGrammars[var1] = null;
         }
      }

      this.fGrammarCount = 0;
   }

   public boolean equals(XMLGrammarDescription var1, XMLGrammarDescription var2) {
      return var1.equals(var2);
   }

   public int hashCode(XMLGrammarDescription var1) {
      return var1.hashCode();
   }

   protected static final class Entry {
      public int hash;
      public XMLGrammarDescription desc;
      public Grammar grammar;
      public Entry next;

      protected Entry(int var1, XMLGrammarDescription var2, Grammar var3, Entry var4) {
         this.hash = var1;
         this.desc = var2;
         this.grammar = var3;
         this.next = var4;
      }

      protected void clear() {
         this.desc = null;
         this.grammar = null;
         if (this.next != null) {
            this.next.clear();
            this.next = null;
         }

      }
   }
}
