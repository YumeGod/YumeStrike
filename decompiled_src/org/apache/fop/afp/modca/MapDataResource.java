package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class MapDataResource extends AbstractTripletStructuredObject {
   public void writeToStream(OutputStream os) throws IOException {
      super.writeStart(os);
      byte[] data = new byte[11];
      this.copySF(data, (byte)-85, (byte)-61);
      int tripletDataLen = this.getTripletDataLength();
      byte[] len = BinaryUtils.convert(10 + tripletDataLen, 2);
      data[1] = len[0];
      data[2] = len[1];
      len = BinaryUtils.convert(2 + tripletDataLen, 2);
      data[9] = len[0];
      data[10] = len[1];
      os.write(data);
      this.writeTriplets(os);
   }
}
