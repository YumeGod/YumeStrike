package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ObjectByteExtentTriplet extends AbstractTriplet {
   private final int byteExt;

   public ObjectByteExtentTriplet(int byteExt) {
      super((byte)87);
      this.byteExt = byteExt;
   }

   public int getDataLength() {
      return 6;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      byte[] extData = BinaryUtils.convert(this.byteExt, 4);
      System.arraycopy(extData, 0, data, 2, extData.length);
      os.write(data);
   }
}
