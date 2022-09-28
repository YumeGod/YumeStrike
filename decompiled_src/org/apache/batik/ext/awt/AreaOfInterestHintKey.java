package org.apache.batik.ext.awt;

import java.awt.RenderingHints;
import java.awt.Shape;

final class AreaOfInterestHintKey extends RenderingHints.Key {
   AreaOfInterestHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      boolean var2 = true;
      if (var1 != null && !(var1 instanceof Shape)) {
         var2 = false;
      }

      return var2;
   }
}
