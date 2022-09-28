package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class GraphicsDataDescriptor extends AbstractDescriptor {
   private final int xlwind;
   private final int xrwind;
   private final int ybwind;
   private final int ytwind;
   private static final int ABS = 64;
   private static final int IMGRES = 16;

   public GraphicsDataDescriptor(int xlwind, int xrwind, int ybwind, int ytwind, int widthRes, int heightRes) {
      this.xlwind = xlwind;
      this.xrwind = xrwind;
      this.ybwind = ybwind;
      this.ytwind = ytwind;
      super.widthRes = widthRes;
      super.heightRes = heightRes;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] headerData = new byte[9];
      this.copySF(headerData, (byte)-90, (byte)-69);
      byte[] drawingOrderSubsetData = this.getDrawingOrderSubset();
      byte[] windowSpecificationData = this.getWindowSpecification();
      byte[] len = BinaryUtils.convert(headerData.length + drawingOrderSubsetData.length + windowSpecificationData.length - 1, 2);
      headerData[1] = len[0];
      headerData[2] = len[1];
      os.write(headerData);
      os.write(drawingOrderSubsetData);
      os.write(windowSpecificationData);
   }

   private byte[] getDrawingOrderSubset() {
      byte[] data = new byte[]{-9, 7, -80, 0, 0, 2, 0, 1, 0};
      return data;
   }

   private byte[] getWindowSpecification() {
      byte[] xlcoord = BinaryUtils.convert(this.xlwind, 2);
      byte[] xrcoord = BinaryUtils.convert(this.xrwind, 2);
      byte[] xbcoord = BinaryUtils.convert(this.ybwind, 2);
      byte[] ytcoord = BinaryUtils.convert(this.ytwind, 2);
      byte[] xResol = BinaryUtils.convert(this.widthRes * 10, 2);
      byte[] yResol = BinaryUtils.convert(this.heightRes * 10, 2);
      byte[] data = new byte[]{-10, 18, 80, 0, 0, 0, xResol[0], xResol[1], yResol[0], yResol[1], xResol[0], xResol[1], xlcoord[0], xlcoord[1], xrcoord[0], xrcoord[1], xbcoord[0], xbcoord[1], ytcoord[0], ytcoord[1]};
      return data;
   }
}
