package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.afp.ptoca.PtocaConstants;
import org.apache.fop.afp.util.BinaryUtils;

public class PresentationTextData extends AbstractAFPObject implements PtocaConstants {
   private static final int MAX_SIZE = 8192;
   private final ByteArrayOutputStream baos;
   private static final int HEADER_LENGTH = 9;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PresentationTextData() {
      this(false);
   }

   public PresentationTextData(boolean controlInd) {
      this.baos = new ByteArrayOutputStream();
      byte[] data = new byte[]{90, 0, 0, -45, -18, -101, 0, 0, 0};
      this.baos.write(data, 0, 9);
      if (controlInd) {
         this.baos.write(new byte[]{43, -45}, 0, 2);
      }

   }

   public int getBytesAvailable() {
      return 8192 - this.baos.size() + 9;
   }

   protected OutputStream getOutputStream() {
      return this.baos;
   }

   public void writeToStream(OutputStream os) throws IOException {
      if (!$assertionsDisabled && this.getBytesAvailable() < 0) {
         throw new AssertionError();
      } else {
         byte[] data = this.baos.toByteArray();
         byte[] size = BinaryUtils.convert(data.length - 1, 2);
         data[1] = size[0];
         data[2] = size[1];
         os.write(data);
      }
   }

   static {
      $assertionsDisabled = !PresentationTextData.class.desiredAssertionStatus();
   }
}
