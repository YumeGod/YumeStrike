package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageInputDescriptor extends AbstractAFPObject {
   private int resolution = 240;

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[45];
      this.copySF(data, (byte)-90, (byte)123);
      data[1] = 0;
      data[2] = 44;
      data[9] = 0;
      data[10] = 0;
      data[11] = 9;
      data[12] = 96;
      data[13] = 9;
      data[14] = 96;
      data[15] = 0;
      data[16] = 0;
      data[17] = 0;
      data[18] = 0;
      data[19] = 0;
      data[20] = 0;
      data[21] = 0;
      data[22] = 0;
      byte[] imagepoints = BinaryUtils.convert(this.resolution * 10, 2);
      data[23] = imagepoints[0];
      data[24] = imagepoints[1];
      data[25] = imagepoints[0];
      data[26] = imagepoints[1];
      data[27] = 0;
      data[28] = 1;
      data[29] = 0;
      data[30] = 1;
      data[31] = 0;
      data[32] = 0;
      data[33] = 0;
      data[34] = 0;
      data[35] = 45;
      data[36] = 0;
      data[37] = 0;
      data[38] = 1;
      data[39] = 0;
      data[40] = 1;
      data[41] = 0;
      data[42] = 1;
      data[43] = -1;
      data[44] = -1;
      os.write(data);
   }

   public void setResolution(int resolution) {
      this.resolution = resolution;
   }
}
