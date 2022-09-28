package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageCellPosition extends AbstractAFPObject {
   private int xOffset = 0;
   private int yOffset = 0;
   private final byte[] xSize = new byte[]{-1, -1};
   private final byte[] ySize = new byte[]{-1, -1};
   private final byte[] xFillSize = new byte[]{-1, -1};
   private final byte[] yFillSize = new byte[]{-1, -1};

   public ImageCellPosition(int x, int y) {
      this.xOffset = x;
      this.yOffset = y;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[21];
      this.copySF(data, (byte)-84, (byte)123);
      data[1] = 0;
      data[2] = 20;
      byte[] x1 = BinaryUtils.convert(this.xOffset, 2);
      data[9] = x1[0];
      data[10] = x1[1];
      byte[] x2 = BinaryUtils.convert(this.yOffset, 2);
      data[11] = x2[0];
      data[12] = x2[1];
      data[13] = this.xSize[0];
      data[14] = this.xSize[1];
      data[15] = this.ySize[0];
      data[16] = this.ySize[1];
      data[17] = this.xFillSize[0];
      data[18] = this.xFillSize[1];
      data[19] = this.yFillSize[0];
      data[20] = this.yFillSize[1];
      os.write(data);
   }

   public void setXSize(int xcSize) {
      byte[] x = BinaryUtils.convert(xcSize, 2);
      this.xSize[0] = x[0];
      this.xSize[1] = x[1];
   }

   public void setXFillSize(int size) {
      byte[] x = BinaryUtils.convert(size, 2);
      this.xFillSize[0] = x[0];
      this.xFillSize[1] = x[1];
   }

   public void setYSize(int size) {
      byte[] x = BinaryUtils.convert(size, 2);
      this.ySize[0] = x[0];
      this.ySize[1] = x[1];
   }

   public void setYFillSize(int size) {
      byte[] x = BinaryUtils.convert(size, 2);
      this.yFillSize[0] = x[0];
      this.yFillSize[1] = x[1];
   }
}
