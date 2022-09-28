package org.apache.fop.afp.ioca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.modca.AbstractAFPObject;
import org.apache.fop.afp.util.BinaryUtils;

public class ImageOutputControl extends AbstractAFPObject {
   private int orientation = 0;
   private int xCoord = 0;
   private int yCoord = 0;
   private boolean singlePoint = true;

   public ImageOutputControl(int x, int y) {
      this.xCoord = x;
      this.yCoord = y;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[33];
      data[0] = 90;
      data[1] = 0;
      data[2] = 32;
      data[3] = -45;
      data[4] = -89;
      data[5] = 123;
      data[6] = 0;
      data[7] = 0;
      data[8] = 0;
      byte[] x1 = BinaryUtils.convert(this.xCoord, 3);
      data[9] = x1[0];
      data[10] = x1[1];
      data[11] = x1[2];
      byte[] x2 = BinaryUtils.convert(this.yCoord, 3);
      data[12] = x2[0];
      data[13] = x2[1];
      data[14] = x2[2];
      switch (this.orientation) {
         case 0:
            data[15] = 0;
            data[16] = 0;
            data[17] = 45;
            data[18] = 0;
            break;
         case 90:
            data[15] = 45;
            data[16] = 0;
            data[17] = 90;
            data[18] = 0;
            break;
         case 180:
            data[15] = 90;
            data[16] = 0;
            data[17] = -121;
            data[18] = 0;
            break;
         case 270:
            data[15] = -121;
            data[16] = 0;
            data[17] = 0;
            data[18] = 0;
            break;
         default:
            data[15] = 0;
            data[16] = 0;
            data[17] = 45;
            data[18] = 0;
      }

      data[19] = 0;
      data[20] = 0;
      data[21] = 0;
      data[22] = 0;
      data[23] = 0;
      data[24] = 0;
      data[25] = 0;
      data[26] = 0;
      if (this.singlePoint) {
         data[27] = 3;
         data[28] = -24;
         data[29] = 3;
         data[30] = -24;
      } else {
         data[27] = 7;
         data[28] = -48;
         data[29] = 7;
         data[30] = -48;
      }

      data[31] = -1;
      data[32] = -1;
      os.write(data);
   }

   public void setOrientation(int orientation) {
      if (orientation != 0 && orientation != 90 && orientation != 180 && orientation != 270) {
         throw new IllegalArgumentException("The orientation must be one of the values 0, 90, 180, 270");
      } else {
         this.orientation = orientation;
      }
   }

   public void setSinglepoint(boolean singlepoint) {
      this.singlePoint = singlepoint;
   }
}
