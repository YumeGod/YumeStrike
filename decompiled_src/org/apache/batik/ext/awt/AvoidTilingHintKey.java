package org.apache.batik.ext.awt;

import java.awt.RenderingHints;

public class AvoidTilingHintKey extends RenderingHints.Key {
   AvoidTilingHintKey(int var1) {
      super(var1);
   }

   public boolean isCompatibleValue(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1 == RenderingHintsKeyExt.VALUE_AVOID_TILE_PAINTING_ON || var1 == RenderingHintsKeyExt.VALUE_AVOID_TILE_PAINTING_OFF || var1 == RenderingHintsKeyExt.VALUE_AVOID_TILE_PAINTING_DEFAULT;
      }
   }
}
