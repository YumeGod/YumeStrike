package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class ContainerDataDescriptor extends AbstractDescriptor {
   public ContainerDataDescriptor(int width, int height, int widthRes, int heightRes) {
      super(width, height, widthRes, heightRes);
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[21];
      this.copySF(data, (byte)-90, (byte)-110);
      byte[] len = BinaryUtils.convert(data.length - 1, 2);
      data[1] = len[0];
      data[2] = len[1];
      data[9] = 0;
      data[10] = 0;
      byte[] xdpi = BinaryUtils.convert(this.widthRes * 10, 2);
      data[11] = xdpi[0];
      data[12] = xdpi[1];
      byte[] ydpi = BinaryUtils.convert(this.heightRes * 10, 2);
      data[13] = ydpi[0];
      data[14] = ydpi[1];
      byte[] xsize = BinaryUtils.convert(this.width, 3);
      data[15] = xsize[0];
      data[16] = xsize[1];
      data[17] = xsize[2];
      byte[] ysize = BinaryUtils.convert(this.height, 3);
      data[18] = ysize[0];
      data[19] = ysize[1];
      data[20] = ysize[2];
      os.write(data);
   }
}
