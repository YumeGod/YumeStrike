package org.apache.batik.transcoder.keys;

import org.apache.batik.transcoder.TranscodingHints;

public class BooleanKey extends TranscodingHints.Key {
   public boolean isCompatibleValue(Object var1) {
      return var1 != null && var1 instanceof Boolean;
   }
}
