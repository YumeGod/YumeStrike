package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public final class GraphicsChainedSegment extends AbstractGraphicsDrawingOrderContainer {
   protected static final int MAX_DATA_LEN = 8192;
   private byte[] predecessorNameBytes;
   private static final byte APPEND_NEW_SEGMENT = 0;
   private static final int NAME_LENGTH = 4;

   public GraphicsChainedSegment(String name) {
      super(name);
   }

   public GraphicsChainedSegment(String name, byte[] predecessorNameBytes) {
      super(name);
      this.predecessorNameBytes = predecessorNameBytes;
   }

   public int getDataLength() {
      return 14 + super.getDataLength();
   }

   protected int getNameLength() {
      return 4;
   }

   byte getOrderCode() {
      return 112;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = new byte[14];
      data[0] = this.getOrderCode();
      data[1] = 12;
      byte[] nameBytes = this.getNameBytes();
      System.arraycopy(nameBytes, 0, data, 2, 4);
      data[6] = 0;
      data[7] = 0;
      int dataLength = super.getDataLength();
      byte[] len = BinaryUtils.convert(dataLength, 2);
      data[8] = len[0];
      data[9] = len[1];
      if (this.predecessorNameBytes != null) {
         System.arraycopy(this.predecessorNameBytes, 0, data, 10, 4);
      }

      os.write(data);
      this.writeObjects(this.objects, os);
   }

   public String toString() {
      return "GraphicsChainedSegment(name=" + super.getName() + ")";
   }
}
