package org.apache.batik.ext.awt;

import java.awt.RenderingHints;

final class TranscodingHintKey extends RenderingHints.Key {
   TranscodingHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      boolean var2 = true;
      if (var1 != null && !(var1 instanceof String)) {
         var2 = false;
      }

      return var2;
   }
}
