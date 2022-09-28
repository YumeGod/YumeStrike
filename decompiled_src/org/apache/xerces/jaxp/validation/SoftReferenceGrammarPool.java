package org.apache.xerces.jaxp.validation;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;

final class SoftReferenceGrammarPool implements XMLGrammarPool {
   protected static final int TABLE_SIZE = 11;
   protected static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
   protected Entry[] fGrammars = null;
   protected boolean fPoolIsLocked;
   protected int fGrammarCount = 0;
   protected final ReferenceQueue fReferenceQueue = new ReferenceQueue();

   public SoftReferenceGrammarPool() {
      this.fGrammars = new Entry[11];
      this.fPoolIsLocked = false;
   }

   public SoftReferenceGrammarPool(int var1) {
      this.fGrammars = new Entry[var1];
      this.fPoolIsLocked = false;
   }

   public Grammar[] retrieveInitialGrammarSet(String var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         this.clean();
         Grammar[] var3 = ZERO_LENGTH_GRAMMAR_ARRAY;
         return var3;
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
            this.clean();
            XMLGrammarDescription var3 = var1.getGrammarDescription();
            int var4 = this.hashCode(var3);
            int var5 = (var4 & Integer.MAX_VALUE) % this.fGrammars.length;

            for(Entry var6 = this.fGrammars[var5]; var6 != null; var6 = var6.next) {
               if (var6.hash == var4 && this.equals(var6.desc, var3)) {
                  if (var6.grammar.get() != var1) {
                     var6.grammar = new SoftGrammarReference(var6, var1, this.fReferenceQueue);
                  }

                  return;
               }
            }

            Entry var7 = new Entry(var4, var5, var3, var1, this.fGrammars[var5], this.fReferenceQueue);
            this.fGrammars[var5] = var7;
            ++this.fGrammarCount;
         }
      }

   }

   public Grammar getGrammar(XMLGrammarDescription var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         this.clean();
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;

         Grammar var6;
         for(Entry var5 = this.fGrammars[var4]; var5 != null; var5 = var5.next) {
            var6 = (Grammar)var5.grammar.get();
            if (var6 == null) {
               this.removeEntry(var5);
            } else if (var5.hash == var3 && this.equals(var5.desc, var1)) {
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
         this.clean();
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;

         Grammar var6;
         for(Entry var5 = this.fGrammars[var4]; var5 != null; var5 = var5.next) {
            if (var5.hash == var3 && this.equals(var5.desc, var1)) {
               var6 = this.removeEntry(var5);
               return var6;
            }
         }

         var6 = null;
         return var6;
      }
   }

   public boolean containsGrammar(XMLGrammarDescription var1) {
      Entry[] var2 = this.fGrammars;
      synchronized(var2) {
         this.clean();
         int var3 = this.hashCode(var1);
         int var4 = (var3 & Integer.MAX_VALUE) % this.fGrammars.length;

         for(Entry var5 = this.fGrammars[var4]; var5 != null; var5 = var5.next) {
            Grammar var6 = (Grammar)var5.grammar.get();
            if (var6 == null) {
               this.removeEntry(var5);
            } else if (var5.hash == var3 && this.equals(var5.desc, var1)) {
               boolean var7 = true;
               return var7;
            }
         }

         boolean var10 = false;
         return var10;
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
      if (var1 instanceof XMLSchemaDescription) {
         if (!(var2 instanceof XMLSchemaDescription)) {
            return false;
         } else {
            XMLSchemaDescription var3 = (XMLSchemaDescription)var1;
            XMLSchemaDescription var4 = (XMLSchemaDescription)var2;
            String var5 = var3.getTargetNamespace();
            if (var5 != null) {
               if (!var5.equals(var4.getTargetNamespace())) {
                  return false;
               }
            } else if (var4.getTargetNamespace() != null) {
               return false;
            }

            String var6 = var3.getExpandedSystemId();
            if (var6 != null) {
               if (!var6.equals(var4.getExpandedSystemId())) {
                  return false;
               }
            } else if (var4.getExpandedSystemId() != null) {
               return false;
            }

            return true;
         }
      } else {
         return var1.equals(var2);
      }
   }

   public int hashCode(XMLGrammarDescription var1) {
      if (var1 instanceof XMLSchemaDescription) {
         XMLSchemaDescription var2 = (XMLSchemaDescription)var1;
         String var3 = var2.getTargetNamespace();
         String var4 = var2.getExpandedSystemId();
         int var5 = var3 != null ? var3.hashCode() : 0;
         var5 ^= var4 != null ? var4.hashCode() : 0;
         return var5;
      } else {
         return var1.hashCode();
      }
   }

   private Grammar removeEntry(Entry var1) {
      if (var1.prev != null) {
         var1.prev.next = var1.next;
      } else {
         this.fGrammars[var1.bucket] = var1.next;
      }

      if (var1.next != null) {
         var1.next.prev = var1.prev;
      }

      --this.fGrammarCount;
      var1.grammar.entry = null;
      return (Grammar)var1.grammar.get();
   }

   private void clean() {
      for(Reference var1 = this.fReferenceQueue.poll(); var1 != null; var1 = this.fReferenceQueue.poll()) {
         Entry var2 = ((SoftGrammarReference)var1).entry;
         if (var2 != null) {
            this.removeEntry(var2);
         }
      }

   }

   static final class SoftGrammarReference extends SoftReference {
      public Entry entry;

      protected SoftGrammarReference(Entry var1, Grammar var2, ReferenceQueue var3) {
         super(var2, var3);
         this.entry = var1;
      }
   }

   static final class Entry {
      public int hash;
      public int bucket;
      public Entry prev;
      public Entry next;
      public XMLGrammarDescription desc;
      public SoftGrammarReference grammar;

      protected Entry(int var1, int var2, XMLGrammarDescription var3, Grammar var4, Entry var5, ReferenceQueue var6) {
         this.hash = var1;
         this.bucket = var2;
         this.prev = null;
         this.next = var5;
         if (var5 != null) {
            var5.prev = this;
         }

         this.desc = var3;
         this.grammar = new SoftGrammarReference(this, var4, var6);
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
