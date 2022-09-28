package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfLineBreak extends RtfElement {
   RtfLineBreak(IRtfTextContainer parent, Writer w) throws IOException {
      super((RtfContainer)parent, w);
   }

   protected void writeRtfContent() throws IOException {
      this.writeControlWord("line");
   }

   public boolean isEmpty() {
      return false;
   }
}
