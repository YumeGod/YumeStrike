package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public final class GraphicsBox extends AbstractGraphicsCoord {
   public GraphicsBox(int[] coords) {
      super(coords);
   }

   public int getDataLength() {
      return 12;
   }

   int getCoordinateDataStartIndex() {
      return 4;
   }

   byte getOrderCode() {
      return -64;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = 32;
      data[3] = 0;
      os.write(data);
   }
}
