package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class GraphicsImage extends AbstractGraphicsDrawingOrder {
   public static final short MAX_DATA_LEN = 255;
   private final int x;
   private final int y;
   private final int width;
   private final int height;
   private final byte[] imageData;

   public GraphicsImage(int x, int y, int width, int height, byte[] imageData) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.imageData = imageData;
   }

   public int getDataLength() {
      return 0;
   }

   byte getOrderCode() {
      return -47;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] xcoord = BinaryUtils.convert(this.x, 2);
      byte[] ycoord = BinaryUtils.convert(this.y, 2);
      byte[] w = BinaryUtils.convert(this.width, 2);
      byte[] h = BinaryUtils.convert(this.height, 2);
      byte[] startData = new byte[]{this.getOrderCode(), 10, xcoord[0], xcoord[1], ycoord[0], ycoord[1], 0, 0, w[0], w[1], h[0], h[1]};
      os.write(startData);
      byte[] dataHeader = new byte[]{-110};
      int lengthOffset = true;
      writeChunksToStream(this.imageData, dataHeader, 1, 255, os);
      byte[] endData = new byte[]{-109, 0};
      os.write(endData);
   }

   public String toString() {
      return "GraphicsImage{x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + "}";
   }
}
