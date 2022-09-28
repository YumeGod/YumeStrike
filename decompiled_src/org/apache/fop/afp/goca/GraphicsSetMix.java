package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsSetMix extends AbstractGraphicsDrawingOrder {
   public static final byte MODE_DEFAULT = 0;
   public static final byte MODE_OVERPAINT = 2;
   private final byte mode;

   public GraphicsSetMix(byte mode) {
      this.mode = mode;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{12, this.mode};
      os.write(data);
   }

   public String toString() {
      return "GraphicsSetMix{mode=" + this.mode + "}";
   }

   byte getOrderCode() {
      return 12;
   }

   public int getDataLength() {
      return 2;
   }
}
