package org.apache.batik.transcoder.keys;

import org.apache.batik.transcoder.TranscodingHints;

public class LengthKey extends TranscodingHints.Key {
   public boolean isCompatibleValue(Object var1) {
      return var1 instanceof Float && (Float)var1 > 0.0F;
   }
}
