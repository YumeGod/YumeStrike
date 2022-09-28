package org.apache.batik.ext.awt;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.ref.Reference;

final class BufferedImageHintKey extends RenderingHints.Key {
   BufferedImageHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      if (var1 == null) {
         return true;
      } else if (!(var1 instanceof Reference)) {
         return false;
      } else {
         Reference var2 = (Reference)var1;
         var1 = var2.get();
         if (var1 == null) {
            return true;
         } else {
            return var1 instanceof BufferedImage;
         }
      }
   }
}
