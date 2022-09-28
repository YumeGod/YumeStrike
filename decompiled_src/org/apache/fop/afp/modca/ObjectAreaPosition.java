package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ObjectAreaPosition extends AbstractAFPObject {
   public static final byte REFCSYS_PAGE_SEGMENT_RELATIVE = 0;
   public static final byte REFCSYS_PAGE_RELATIVE = 1;
   private final int x;
   private final int y;
   private final int rotation;
   private int xOffset;
   private int yOffset;
   private byte refCSys;

   public ObjectAreaPosition(int x, int y, int rotation) {
      this(x, y, rotation, (byte)1);
   }

   public ObjectAreaPosition(int x, int y, int rotation, byte refCSys) {
      this.refCSys = 1;
      this.x = x;
      this.y = y;
      this.rotation = rotation;
      this.setReferenceCoordinateSystem(refCSys);
   }

   public void setReferenceCoordinateSystem(byte refCSys) {
      this.refCSys = refCSys;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[33];
      this.copySF(data, (byte)-84, (byte)107);
      byte[] len = BinaryUtils.convert(32, 2);
      data[1] = len[0];
      data[2] = len[1];
      data[9] = 1;
      data[10] = 23;
      byte[] xcoord = BinaryUtils.convert(this.x, 3);
      data[11] = xcoord[0];
      data[12] = xcoord[1];
      data[13] = xcoord[2];
      byte[] ycoord = BinaryUtils.convert(this.y, 3);
      data[14] = ycoord[0];
      data[15] = ycoord[1];
      data[16] = ycoord[2];
      byte xorient = (byte)(this.rotation / 2);
      data[17] = xorient;
      byte yorient = (byte)(this.rotation / 2 + 45);
      data[19] = yorient;
      byte[] xoffset = BinaryUtils.convert(this.xOffset, 3);
      data[22] = xoffset[0];
      data[23] = xoffset[1];
      data[24] = xoffset[2];
      byte[] yoffset = BinaryUtils.convert(this.yOffset, 3);
      data[25] = yoffset[0];
      data[26] = yoffset[1];
      data[27] = yoffset[2];
      data[28] = 0;
      data[29] = 0;
      data[30] = 45;
      data[31] = 0;
      data[32] = this.refCSys;
      os.write(data);
   }

   public String toString() {
      return "ObjectAreaPosition{x=" + this.x + ", y=" + this.y + ", rotation=" + this.rotation + ", rotation=" + this.rotation + ", xOffset=" + this.xOffset + ", yOffset=" + this.yOffset;
   }
}
