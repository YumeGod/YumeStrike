package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfPageNumberCitation extends RtfContainer {
   public static final String RTF_FIELD = "field";
   public static final String RTF_FIELD_PAGEREF_MODEL = "fldinst { PAGEREF }";
   public static final String RTF_FIELD_RESULT = "fldrslt";
   private String id = null;

   RtfPageNumberCitation(RtfContainer parent, Writer w, String id) throws IOException {
      super(parent, w);
      this.id = id;
   }

   RtfPageNumberCitation(RtfParagraph parent, Writer w, String id) throws IOException {
      super(parent, w, parent.attrib);
      if (parent.getTextAttributes() != null) {
         this.attrib.set(parent.getTextAttributes());
      }

      this.id = id;
   }

   protected void writeRtfContent() throws IOException {
      if (this.isValid()) {
         String pageRef = "fldinst { PAGEREF }";
         int insertionIndex = pageRef.indexOf("}");
         pageRef = pageRef.substring(0, insertionIndex) + "\"" + this.id + "\"" + " " + pageRef.substring(insertionIndex, pageRef.length());
         this.id = null;
         this.writeGroupMark(true);
         this.writeControlWord("field");
         this.writeGroupMark(true);
         this.writeAttributes(this.attrib, RtfText.ATTR_NAMES);
         this.writeStarControlWord(pageRef);
         this.writeGroupMark(false);
         this.writeGroupMark(true);
         this.writeControlWord("fldrslt#");
         this.writeGroupMark(false);
         this.writeGroupMark(false);
      }

   }

   private boolean isValid() {
      return this.id != null;
   }

   public boolean isEmpty() {
      return false;
   }
}
