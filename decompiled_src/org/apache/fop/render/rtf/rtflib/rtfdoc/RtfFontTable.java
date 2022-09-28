package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

class RtfFontTable extends RtfElement {
   RtfFontTable(RtfHeader h, Writer w) throws IOException {
      super(h, w);
   }

   protected void writeRtfContent() throws IOException {
      RtfFontManager.getInstance().writeFonts((RtfHeader)this.parent);
   }

   public boolean isEmpty() {
      return false;
   }
}
