package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageRasterData extends AbstractAFPObject {
   private final byte[] rasterData;

   public ImageRasterData(byte[] data) {
      this.rasterData = data;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[9];
      this.copySF(data, (byte)-18, (byte)123);
      byte[] len = BinaryUtils.convert(this.rasterData.length + 8, 2);
      data[1] = len[0];
      data[2] = len[1];
      os.write(data);
      os.write(this.rasterData);
   }
}
