package org.apache.batik.transcoder.keys;

import org.apache.batik.transcoder.TranscodingHints;

public class StringKey extends TranscodingHints.Key {
   public boolean isCompatibleValue(Object var1) {
      return var1 instanceof String;
   }
}
