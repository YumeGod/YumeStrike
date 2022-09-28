package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ExtendedResourceLocalIdentifierTriplet extends AbstractTriplet {
   public static final byte TYPE_IMAGE_RESOURCE = 16;
   public static final byte TYPE_RETIRED_VALUE = 48;
   public static final byte TYPE_MEDIA_RESOURCE = 64;
   private final byte type;
   private final int localId;

   public ExtendedResourceLocalIdentifierTriplet(byte type, int localId) {
      super((byte)34);
      this.type = type;
      this.localId = localId;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.type;
      byte[] resLID = BinaryUtils.convert(this.localId, 4);
      System.arraycopy(resLID, 0, data, 3, resLID.length);
      os.write(data);
   }

   public int getDataLength() {
      return 7;
   }
}
