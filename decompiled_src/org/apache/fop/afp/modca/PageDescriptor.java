package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class PageDescriptor extends AbstractDescriptor {
   public PageDescriptor(int width, int height, int widthRes, int heightRes) {
      super(width, height, widthRes, heightRes);
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[24];
      this.copySF(data, (byte)-90, (byte)-81);
      data[2] = 23;
      data[9] = 0;
      data[10] = 0;
      byte[] xdpi = BinaryUtils.convert(this.widthRes * 10, 2);
      data[11] = xdpi[0];
      data[12] = xdpi[1];
      byte[] ydpi = BinaryUtils.convert(this.heightRes * 10, 2);
      data[13] = ydpi[0];
      data[14] = ydpi[1];
      byte[] x = BinaryUtils.convert(this.width, 3);
      data[15] = x[0];
      data[16] = x[1];
      data[17] = x[2];
      byte[] y = BinaryUtils.convert(this.height, 3);
      data[18] = y[0];
      data[19] = y[1];
      data[20] = y[2];
      data[21] = 0;
      data[22] = 0;
      data[23] = 0;
      os.write(data);
   }
}
