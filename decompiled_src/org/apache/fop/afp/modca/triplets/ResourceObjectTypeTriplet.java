package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class ResourceObjectTypeTriplet extends AbstractTriplet {
   private static final byte RESOURCE_OBJECT = 33;
   private final byte objectType;

   public ResourceObjectTypeTriplet(byte objectType) {
      super((byte)33);
      this.objectType = objectType;
   }

   public int getDataLength() {
      return 10;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.objectType;
      os.write(data);
   }
}
