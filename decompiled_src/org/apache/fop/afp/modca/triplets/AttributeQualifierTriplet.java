package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public class AttributeQualifierTriplet extends AbstractTriplet {
   private int seqNumber;
   private int levNumber;

   public AttributeQualifierTriplet(int seqNumber, int levNumber) {
      super((byte)-128);
      this.seqNumber = seqNumber;
      this.levNumber = levNumber;
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      byte[] id = BinaryUtils.convert(this.seqNumber, 4);
      System.arraycopy(id, 0, data, 2, id.length);
      byte[] level = BinaryUtils.convert(this.levNumber, 4);
      System.arraycopy(level, 0, data, 6, level.length);
      os.write(data);
   }

   public int getDataLength() {
      return 10;
   }

   public String toString() {
      return "seqNumber=" + this.seqNumber + ", levNumber=" + this.levNumber;
   }
}
