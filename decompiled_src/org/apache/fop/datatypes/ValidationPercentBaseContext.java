package org.apache.fop.datatypes;

import org.apache.fop.fo.FObj;

public final class ValidationPercentBaseContext implements PercentBaseContext {
   private static PercentBaseContext pseudoContextForValidation = null;

   private ValidationPercentBaseContext() {
   }

   public int getBaseLength(int lengthBase, FObj fobj) {
      return 100000;
   }

   public static PercentBaseContext getPseudoContext() {
      if (pseudoContextForValidation == null) {
         pseudoContextForValidation = new ValidationPercentBaseContext();
      }

      return pseudoContextForValidation;
   }
}
