package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ObjectAreaSizeTriplet extends AbstractTriplet {
   private final int x;
   private final int y;
   private final byte type;

   public ObjectAreaSizeTriplet(int x, int y, byte type) {
      super((byte)76);
      this.x = x;
      this.y = y;
      this.type = type;
   }

   public ObjectAreaSizeTriplet(int x, int y) {
      this(x, y, (byte)2);
   }

   public int getDataLength() {
      return 9;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.type;
      byte[] xOASize = BinaryUtils.convert(this.x, 3);
      data[3] = xOASize[0];
      data[4] = xOASize[1];
      data[5] = xOASize[2];
      byte[] yOASize = BinaryUtils.convert(this.y, 3);
      data[6] = yOASize[0];
      data[7] = yOASize[1];
      data[8] = yOASize[2];
      os.write(data);
   }
}
