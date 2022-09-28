package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsSetLineWidth extends AbstractGraphicsDrawingOrder {
   private int multiplier = 1;

   public GraphicsSetLineWidth(int multiplier) {
      this.multiplier = multiplier;
   }

   public int getDataLength() {
      return 2;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), (byte)this.multiplier};
      os.write(data);
   }

   public String toString() {
      return "GraphicsSetLineWidth{multiplier=" + this.multiplier + "}";
   }

   byte getOrderCode() {
      return 25;
   }
}
