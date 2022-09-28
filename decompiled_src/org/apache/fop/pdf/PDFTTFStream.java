package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;

public class PDFTTFStream extends AbstractPDFFontStream {
   private int origLength;
   private byte[] ttfData;

   public PDFTTFStream(int len) {
      this.origLength = len;
   }

   protected int getSizeHint() throws IOException {
      return this.ttfData != null ? this.ttfData.length : 0;
   }

   protected int output(OutputStream stream) throws IOException {
      if (log.isDebugEnabled()) {
         log.debug("Writing " + this.origLength + " bytes of TTF font data");
      }

      int length = super.output(stream);
      log.debug("Embedded TrueType/OpenType font");
      return length;
   }

   protected void outputRawStreamData(OutputStream out) throws IOException {
      out.write(this.ttfData);
   }

   protected void populateStreamDict(Object lengthEntry) {
      this.put("Length1", this.origLength);
      super.populateStreamDict(lengthEntry);
   }

   public void setData(byte[] data, int size) throws IOException {
      this.ttfData = new byte[size];
      System.arraycopy(data, 0, this.ttfData, 0, size);
   }
}
