package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class IncludePageOverlay extends AbstractNamedAFPObject {
   private int x = 0;
   private int y = 0;
   private int orientation = 0;

   public IncludePageOverlay(String overlayName, int x, int y, int orientation) {
      super(overlayName);
      this.x = x;
      this.y = y;
      this.setOrientation(orientation);
   }

   public void setOrientation(int orientation) {
      if (orientation != 0 && orientation != 90 && orientation != 180 && orientation != 270) {
         throw new IllegalArgumentException("The orientation must be one of the values 0, 90, 180, 270");
      } else {
         this.orientation = orientation;
      }
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[25];
      this.copySF(data, (byte)-81, (byte)-40);
      byte[] len = BinaryUtils.convert(24, 2);
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
      switch (this.orientation) {
         case 90:
            data[23] = 45;
            data[24] = 0;
            break;
         case 180:
            data[23] = 90;
            data[24] = 0;
            break;
         case 270:
            data[23] = -121;
            data[24] = 0;
            break;
         default:
            data[23] = 0;
            data[24] = 0;
      }

      os.write(data);
   }
}
