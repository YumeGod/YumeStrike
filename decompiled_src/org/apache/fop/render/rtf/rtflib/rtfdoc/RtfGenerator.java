package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfGenerator extends RtfElement {
   public RtfGenerator(RtfHeader h, Writer w) throws IOException {
      super(h, w);
   }

   protected void writeRtfContent() throws IOException {
      this.newLine();
      this.writeGroupMark(true);
      this.writeStarControlWord("generator");
      this.writer.write("Apache XML Graphics RTF Library");
      this.writer.write(";");
      this.writeGroupMark(false);
   }

   public boolean isEmpty() {
      return false;
   }
}
