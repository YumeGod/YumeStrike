package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfAfter extends RtfAfterBeforeBase {
   public static final String FOOTER = "footer";
   public static final String[] FOOTER_ATTR = new String[]{"footer"};

   RtfAfter(RtfSection parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w, attrs);
   }

   protected void writeMyAttributes() throws IOException {
      this.writeAttributes(this.attrib, FOOTER_ATTR);
   }
}
