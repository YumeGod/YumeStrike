package org.apache.fop.afp.modca.triplets;

import java.io.IOException;
import java.io.OutputStream;

public class CommentTriplet extends AbstractTriplet {
   private final String commentString;

   public CommentTriplet(byte id, String commentString) {
      super(id);
      this.commentString = commentString;
   }

   public int getDataLength() {
      return 2 + this.commentString.length();
   }

   public void writeToStream(OutputStream os) throws IOException {
      byte[] data = this.getData();
      byte[] strData = this.commentString.getBytes("Cp1146");
      System.arraycopy(strData, 0, data, 2, strData.length);
      os.write(data);
   }
}
