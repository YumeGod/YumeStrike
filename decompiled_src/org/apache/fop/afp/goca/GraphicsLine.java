package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsLine extends AbstractGraphicsCoord {
   public GraphicsLine(int[] coords, boolean relative) {
      super(coords, relative);
   }

   byte getOrderCode() {
      return (byte)(this.isRelative() ? -127 : -63);
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      os.write(data);
   }
}
