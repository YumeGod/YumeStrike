package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

public class GraphicsSetPatternSymbol extends AbstractGraphicsDrawingOrder {
   public static final byte DOTTED_DENSITY_1 = 1;
   public static final byte DOTTED_DENSITY_2 = 2;
   public static final byte DOTTED_DENSITY_3 = 3;
   public static final byte DOTTED_DENSITY_4 = 4;
   public static final byte DOTTED_DENSITY_5 = 5;
   public static final byte DOTTED_DENSITY_6 = 6;
   public static final byte DOTTED_DENSITY_7 = 7;
   public static final byte DOTTED_DENSITY_8 = 8;
   public static final byte VERTICAL_LINES = 9;
   public static final byte HORIZONTAL_LINES = 10;
   public static final byte DIAGONAL_LINES_BLTR_1 = 11;
   public static final byte DIAGONAL_LINES_BLTR_2 = 12;
   public static final byte DIAGONAL_LINES_TLBR_1 = 13;
   public static final byte DIAGONAL_LINES_TLBR_2 = 14;
   public static final byte NO_FILL = 15;
   public static final byte SOLID_FILL = 16;
   public static final byte BLANK = 64;
   private final byte pattern;

   public GraphicsSetPatternSymbol(byte pattern) {
      this.pattern = pattern;
   }

   public int getDataLength() {
      return 2;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), this.pattern};
      os.write(data);
   }

   public String toString() {
      return "GraphicsSetPatternSymbol(fill=" + (this.pattern == 16) + ")";
   }

   byte getOrderCode() {
      return 40;
   }
}
