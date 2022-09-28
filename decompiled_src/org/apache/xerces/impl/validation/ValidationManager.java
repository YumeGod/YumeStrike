package org.apache.xerces.impl.validation;

import java.util.Vector;

public class ValidationManager {
   protected final Vector fVSs = new Vector();
   protected boolean fGrammarFound = false;
   protected boolean fCachedDTD = false;

   public final void addValidationState(ValidationState var1) {
      this.fVSs.addElement(var1);
   }

   public final void setEntityState(EntityState var1) {
      for(int var2 = this.fVSs.size() - 1; var2 >= 0; --var2) {
         ((ValidationState)this.fVSs.elementAt(var2)).setEntityState(var1);
      }

   }

   public final void setGrammarFound(boolean var1) {
      this.fGrammarFound = var1;
   }

   public final boolean isGrammarFound() {
      return this.fGrammarFound;
   }

   public final void setCachedDTD(boolean var1) {
      this.fCachedDTD = var1;
   }

   public final boolean isCachedDTD() {
      return this.fCachedDTD;
   }

   public final void reset() {
      this.fVSs.removeAllElements();
      this.fGrammarFound = false;
      this.fCachedDTD = false;
   }
}
