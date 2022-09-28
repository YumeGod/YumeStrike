package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfNull extends RtfContainer {
   RtfNull(RtfPage parent, Writer w) throws IOException {
      super(parent, w);
   }
}
