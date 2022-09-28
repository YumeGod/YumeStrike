package org.apache.batik.transcoder.keys;

import java.awt.Paint;
import org.apache.batik.transcoder.TranscodingHints;

public class PaintKey extends TranscodingHints.Key {
   public boolean isCompatibleValue(Object var1) {
      return var1 instanceof Paint;
   }
}
