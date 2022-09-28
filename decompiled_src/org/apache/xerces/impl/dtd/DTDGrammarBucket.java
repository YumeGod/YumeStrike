package org.apache.xerces.impl.dtd;

import java.util.Hashtable;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;

public class DTDGrammarBucket {
   protected Hashtable fGrammars = new Hashtable();
   protected DTDGrammar fActiveGrammar;
   protected boolean fIsStandalone;

   public void putGrammar(DTDGrammar var1) {
      XMLDTDDescription var2 = (XMLDTDDescription)var1.getGrammarDescription();
      this.fGrammars.put(var2, var1);
   }

   public DTDGrammar getGrammar(XMLGrammarDescription var1) {
      return (DTDGrammar)this.fGrammars.get((XMLDTDDescription)var1);
   }

   public void clear() {
      this.fGrammars.clear();
      this.fActiveGrammar = null;
      this.fIsStandalone = false;
   }

   void setStandalone(boolean var1) {
      this.fIsStandalone = var1;
   }

   boolean getStandalone() {
      return this.fIsStandalone;
   }

   void setActiveGrammar(DTDGrammar var1) {
      this.fActiveGrammar = var1;
   }

   DTDGrammar getActiveGrammar() {
      return this.fActiveGrammar;
   }
}
