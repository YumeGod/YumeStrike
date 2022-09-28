package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfParagraphKeepTogether extends RtfContainer {
   public static final int STATUS_NULL = 0;
   public static final int STATUS_OPEN_PARAGRAPH = 1;
   public static final int STATUS_CLOSE_PARAGRAPH = 2;
   private int status = 0;

   RtfParagraphKeepTogether(IRtfParagraphContainer parent, Writer w) throws IOException {
      super((RtfContainer)parent, w);
   }

   protected void writeRtfContent() throws IOException {
      if (this.status == 1) {
         this.writeControlWord("pard");
         this.writeControlWord("par");
         this.writeControlWord("keepn");
         this.writeGroupMark(true);
         this.status = 0;
      }

      if (this.status == 2) {
         this.writeGroupMark(false);
         this.status = 0;
      }

   }

   public void setStatus(int status) {
      this.status = status;
   }

   public boolean isEmpty() {
      return false;
   }
}
