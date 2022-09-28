package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class IncludePageSegment extends AbstractNamedAFPObject {
   private int x;
   private int y;

   public IncludePageSegment(String name, int x, int y) {
      super(name);
      this.x = x;
      this.y = y;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[23];
      this.copySF(data, (byte)-81, (byte)95);
      byte[] len = BinaryUtils.convert(22, 2);
      data[1] = len[0];
      data[2] = len[1];
      byte[] xPos = BinaryUtils.convert(this.x, 3);
      data[17] = xPos[0];
      data[18] = xPos[1];
      data[19] = xPos[2];
      byte[] yPos = BinaryUtils.convert(this.y, 3);
      data[20] = yPos[0];
      data[21] = yPos[1];
      data[22] = yPos[2];
      os.write(data);
   }
}
