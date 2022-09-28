package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class MappingOptionTriplet extends AbstractTriplet {
   public static final byte POSITION = 0;
   public static final byte POSITION_AND_TRIM = 16;
   public static final byte SCALE_TO_FIT = 32;
   public static final byte CENTER_AND_TRIM = 48;
   public static final byte IMAGE_POINT_TO_PEL = 65;
   public static final byte IMAGE_POINT_TO_PEL_DOUBLE_DOT = 66;
   public static final byte REPLICATE_AND_TRIM = 80;
   public static final byte SCALE_TO_FILL = 96;
   public static final byte UP3I_PRINT_DATA = 112;
   private final byte mapValue;

   public MappingOptionTriplet(byte mapValue) {
      super((byte)4);
      this.mapValue = mapValue;
   }

   public int getDataLength() {
      return 3;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      data[2] = this.mapValue;
      os.write(data);
   }
}
