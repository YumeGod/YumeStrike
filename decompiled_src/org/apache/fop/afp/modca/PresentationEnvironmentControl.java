package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class PresentationEnvironmentControl extends AbstractTripletStructuredObject {
   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[11];
      this.copySF(data, (byte)-89, (byte)-88);
      int tripletDataLen = this.getTripletDataLength();
      byte[] len = BinaryUtils.convert(10 + tripletDataLen);
      data[1] = len[0];
      data[2] = len[1];
      data[9] = 0;
      data[10] = 0;
      os.write(data);
      this.writeTriplets(os);
   }
}
