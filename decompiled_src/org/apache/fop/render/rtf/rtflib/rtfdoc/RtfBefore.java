package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfBefore extends RtfAfterBeforeBase {
   public static final String HEADER = "header";
   public static final String[] HEADER_ATTR = new String[]{"header"};

   RtfBefore(RtfSection parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w, attrs);
   }

   protected void writeMyAttributes() throws IOException {
      this.writeAttributes(this.attrib, HEADER_ATTR);
   }
}
