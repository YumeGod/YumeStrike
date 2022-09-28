package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsAreaBegin extends AbstractGraphicsDrawingOrder {
   private static final int RES1 = 128;
   private static final int BOUNDARY = 64;
   private static final int NO_BOUNDARY = 0;
   private boolean drawBoundary = false;

   public void setDrawBoundaryLines(boolean drawBoundaryLines) {
      this.drawBoundary = drawBoundaryLines;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), (byte)(128 + (this.drawBoundary ? 64 : 0))};
      os.write(data);
   }

   public int getDataLength() {
      return 2;
   }

   public String toString() {
      return "GraphicsAreaBegin{drawBoundary=" + this.drawBoundary + "}";
   }

   byte getOrderCode() {
      return 104;
   }
}
