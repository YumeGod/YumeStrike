package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageSizeParameter extends AbstractAFPObject {
   private int hSize = 0;
   private int vSize = 0;
   private int hRes = 0;
   private int vRes = 0;

   public ImageSizeParameter(int hsize, int vsize, int hresol, int vresol) {
      this.hSize = hsize;
      this.vSize = vsize;
      this.hRes = hresol;
      this.vRes = vresol;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{-108, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      byte[] x = BinaryUtils.convert(this.hRes, 2);
      data[3] = x[0];
      data[4] = x[1];
      byte[] y = BinaryUtils.convert(this.vRes, 2);
      data[5] = y[0];
      data[6] = y[1];
      byte[] w = BinaryUtils.convert(this.hSize, 2);
      data[7] = w[0];
      data[8] = w[1];
      byte[] h = BinaryUtils.convert(this.vSize, 2);
      data[9] = h[0];
      data[10] = h[1];
      os.write(data);
   }
}
