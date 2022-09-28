package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class GraphicsFullArc extends AbstractGraphicsCoord {
   private final int mh;
   private final int mhr;

   public GraphicsFullArc(int x, int y, int mh, int mhr) {
      super(x, y);
      this.mh = mh;
      this.mhr = mhr;
   }

   public int getDataLength() {
      return 8;
   }

   byte getOrderCode() {
      return -57;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[6] = BinaryUtils.convert(this.mh, 1)[0];
      data[7] = BinaryUtils.convert(this.mhr, 1)[0];
      os.write(data);
   }

   public String toString() {
      return "GraphicsFullArc{, centerx=" + this.coords[0] + ", centery=" + this.coords[1] + ", mh=" + this.mh + ", mhr=" + this.mhr + "}";
   }
}
