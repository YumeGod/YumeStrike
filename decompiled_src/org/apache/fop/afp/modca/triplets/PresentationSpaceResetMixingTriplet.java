package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class PresentationSpaceResetMixingTriplet extends AbstractTriplet {
   public static final byte NOT_RESET = 0;
   public static final byte RESET = 1;
   private final byte backgroundMixFlag;

   public PresentationSpaceResetMixingTriplet(byte backgroundMixFlag) {
      super((byte)112);
      this.backgroundMixFlag = backgroundMixFlag;
   }

   public int getDataLength() {
      return 3;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.backgroundMixFlag;
      os.write(data);
   }
}
