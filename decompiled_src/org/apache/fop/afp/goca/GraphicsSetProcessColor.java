package org.apache.fop.afp.goca;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

public class GraphicsSetProcessColor extends AbstractGraphicsDrawingOrder {
   private static final byte RGB = 1;
   private static final byte CMYK = 4;
   private final Color color;
   private final float[] colorComponents;

   public GraphicsSetProcessColor(Color color) {
      this.color = color;
      this.colorComponents = color.getColorComponents((float[])null);
   }

   public int getDataLength() {
      return 12 + this.colorComponents.length;
   }

   byte getOrderCode() {
      return -78;
   }

   public void writeToStream(OutputStream os) throws IOException {
      int colSpaceType = this.color.getColorSpace().getType();
      byte colspace;
      if (colSpaceType == 9) {
         colspace = 4;
      } else if (colSpaceType == 5) {
         colspace = 1;
      } else {
         log.error("unsupported colorspace " + colSpaceType);
         colspace = 1;
      }

      byte[] colsizes = new byte[]{0, 0, 0, 0};

      int len;
      for(len = 0; len < this.colorComponents.length; ++len) {
         colsizes[len] = 8;
      }

      len = this.getDataLength();
      byte[] data = new byte[len];
      data[0] = this.getOrderCode();
      data[1] = (byte)(len - 2);
      data[2] = 0;
      data[3] = colspace;
      data[4] = 0;
      data[5] = 0;
      data[6] = 0;
      data[7] = 0;
      data[8] = colsizes[0];
      data[9] = colsizes[1];
      data[10] = colsizes[2];
      data[11] = colsizes[3];

      for(int i = 0; i < this.colorComponents.length; ++i) {
         data[i + 12] = (byte)((int)(this.colorComponents[i] * 255.0F));
      }

      os.write(data);
   }

   public String toString() {
      return "GraphicsSetProcessColor(col=" + this.color + ")";
   }
}
