package org.apache.batik.ext.awt.image;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class PadMode implements Serializable {
   public static final int MODE_ZERO_PAD = 1;
   public static final int MODE_REPLICATE = 2;
   public static final int MODE_WRAP = 3;
   public static final PadMode ZERO_PAD = new PadMode(1);
   public static final PadMode REPLICATE = new PadMode(2);
   public static final PadMode WRAP = new PadMode(3);
   private int mode;

   public int getMode() {
      return this.mode;
   }

   private PadMode(int var1) {
      this.mode = var1;
   }

   private Object readResolve() throws ObjectStreamException {
      switch (this.mode) {
         case 1:
            return ZERO_PAD;
         case 2:
            return REPLICATE;
         case 3:
            return WRAP;
         default:
            throw new Error("Unknown Pad Mode type");
      }
   }
}
