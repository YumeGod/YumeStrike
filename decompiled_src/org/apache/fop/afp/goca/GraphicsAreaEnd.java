package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsAreaEnd extends AbstractGraphicsDrawingOrder {
   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), 0};
      os.write(data);
   }

   public int getDataLength() {
      return 2;
   }

   public String toString() {
      return "GraphicsAreaEnd";
   }

   byte getOrderCode() {
      return 96;
   }
}
