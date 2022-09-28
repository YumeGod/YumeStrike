package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class MeasurementUnitsTriplet extends AbstractTriplet {
   private static final byte TEN_INCHES = 0;
   private static final byte TEN_CM = 1;
   private final int xRes;
   private final int yRes;

   public MeasurementUnitsTriplet(int xRes, int yRes) {
      super((byte)75);
      this.xRes = xRes;
      this.yRes = yRes;
   }

   public int getDataLength() {
      return 8;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = 0;
      data[3] = 0;
      byte[] xUnits = BinaryUtils.convert(this.xRes * 10, 2);
      data[4] = xUnits[0];
      data[5] = xUnits[1];
      byte[] yUnits = BinaryUtils.convert(this.yRes * 10, 2);
      data[6] = yUnits[0];
      data[7] = yUnits[1];
      os.write(data);
   }
}
