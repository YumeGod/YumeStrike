package org.apache.xerces.jaxp.validation;

import java.lang.ref.WeakReference;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class WeakReferenceXMLSchema extends AbstractXMLSchema {
   private WeakReference fGrammarPool = new WeakReference((Object)null);

   public WeakReferenceXMLSchema() {
   }

   public synchronized XMLGrammarPool getGrammarPool() {
      Object var1 = (XMLGrammarPool)this.fGrammarPool.get();
      if (var1 == null) {
         var1 = new SoftReferenceGrammarPool();
         this.fGrammarPool = new WeakReference(var1);
      }

      return (XMLGrammarPool)var1;
   }

   public boolean isFullyComposed() {
      return false;
   }
}
