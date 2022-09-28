package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class InvokeMediumMap extends AbstractNamedAFPObject {
   public InvokeMediumMap(String name) {
      super(name);
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-85, (byte)-52);
      byte[] len = BinaryUtils.convert(16, 2);
      data[1] = len[0];
      data[2] = len[1];
      os.write(data);
   }
}
