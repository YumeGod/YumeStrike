package org.apache.bcel.generic;

import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class LDC_W extends LDC {
   LDC_W() {
   }

   public LDC_W(int index) {
      super(index);
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      this.setIndex(bytes.readUnsignedShort());
      super.length = 3;
   }
}
