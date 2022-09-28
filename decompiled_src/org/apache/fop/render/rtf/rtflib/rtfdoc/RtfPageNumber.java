package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfPageNumber extends RtfContainer {
   public static final String RTF_FIELD = "field";
   public static final String RTF_FIELD_PAGE = "fldinst { PAGE }";
   public static final String RTF_FIELD_RESULT = "fldrslt";

   RtfPageNumber(IRtfPageNumberContainer parent, Writer w) throws IOException {
      super((RtfContainer)parent, w);
   }

   RtfPageNumber(RtfContainer parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w, attrs);
   }

   RtfPageNumber(RtfParagraph parent, Writer w) throws IOException {
      super(parent, w, parent.attrib);
      if (parent.getTextAttributes() != null) {
         this.attrib.set(parent.getTextAttributes());
      }

   }

   protected void writeRtfContent() throws IOException {
      this.writeGroupMark(true);
      this.writeAttributes(this.attrib, RtfText.ATTR_NAMES);
      this.writeControlWord("chpgn");
      this.writeGroupMark(false);
   }

   public boolean isEmpty() {
      return false;
   }
}
