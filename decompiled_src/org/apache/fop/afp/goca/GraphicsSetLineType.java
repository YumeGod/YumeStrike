package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsSetLineType extends AbstractGraphicsDrawingOrder {
   public static final byte DEFAULT = 0;
   public static final byte DOTTED = 1;
   public static final byte SHORT_DASHED = 2;
   public static final byte DASH_DOT = 3;
   public static final byte DOUBLE_DOTTED = 4;
   public static final byte LONG_DASHED = 5;
   public static final byte DASH_DOUBLE_DOTTED = 6;
   public static final byte SOLID = 7;
   public static final byte INVISIBLE = 8;
   private byte type = 0;
   private static final String[] TYPES = new String[]{"default (solid)", "dotted", "short dashed", "dash dotted", "double dotted", "long dashed", "dash double dotted", "solid", "invisible"};

   public GraphicsSetLineType(byte type) {
      this.type = type;
   }

   public int getDataLength() {
      return 2;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), this.type};
      os.write(data);
   }

   public String toString() {
      return "GraphicsSetLineType{type=" + TYPES[this.type] + "}";
   }

   byte getOrderCode() {
      return 24;
   }
}
