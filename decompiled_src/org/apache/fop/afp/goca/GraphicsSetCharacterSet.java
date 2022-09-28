package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class GraphicsSetCharacterSet extends AbstractGraphicsDrawingOrder {
   private final int fontReference;

   public GraphicsSetCharacterSet(int fontReference) {
      this.fontReference = fontReference;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[]{this.getOrderCode(), BinaryUtils.convert(this.fontReference)[0]};
      os.write(data);
   }

   public int getDataLength() {
      return 2;
   }

   public String toString() {
      return "GraphicsSetCharacterSet(" + this.fontReference + ")";
   }

   byte getOrderCode() {
      return 56;
   }
}
