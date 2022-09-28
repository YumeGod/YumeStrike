package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfString extends RtfElement {
   private String text = "";

   RtfString(RtfContainer parent, Writer w, String s) throws IOException {
      super(parent, w);
      this.text = s;
   }

   public boolean isEmpty() {
      return this.text.trim().equals("");
   }

   protected void writeRtfContent() throws IOException {
      RtfStringConverter.getInstance().writeRtfString(this.writer, this.text);
   }

   public String getText() {
      return this.text;
   }

   public void setText(String s) {
      this.text = s;
   }
}
