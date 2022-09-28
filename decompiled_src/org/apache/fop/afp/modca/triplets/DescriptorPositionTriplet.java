package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class DescriptorPositionTriplet extends AbstractTriplet {
   private final byte oapId;

   public DescriptorPositionTriplet(byte oapId) {
      super((byte)67);
      this.oapId = oapId;
   }

   public int getDataLength() {
      return 3;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.oapId;
      os.write(data);
   }
}
