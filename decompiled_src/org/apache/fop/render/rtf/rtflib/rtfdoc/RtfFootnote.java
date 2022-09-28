package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfFootnote extends RtfContainer implements IRtfTextrunContainer, IRtfListContainer {
   RtfTextrun textrunInline = null;
   RtfContainer body = null;
   RtfList list = null;
   boolean bBody = false;

   RtfFootnote(RtfContainer parent, Writer w) throws IOException {
      super(parent, w);
      this.textrunInline = new RtfTextrun(this, this.writer, (RtfAttributes)null);
      this.body = new RtfContainer(this, this.writer);
   }

   public RtfTextrun getTextrun() throws IOException {
      if (this.bBody) {
         RtfTextrun textrun = RtfTextrun.getTextrun(this.body, this.writer, (RtfAttributes)null);
         textrun.setSuppressLastPar(true);
         return textrun;
      } else {
         return this.textrunInline;
      }
   }

   protected void writeRtfContent() throws IOException {
      this.textrunInline.writeRtfContent();
      this.writeGroupMark(true);
      this.writeControlWord("footnote");
      this.writeControlWord("ftnalt");
      this.body.writeRtfContent();
      this.writeGroupMark(false);
   }

   public RtfList newList(RtfAttributes attrs) throws IOException {
      if (this.list != null) {
         this.list.close();
      }

      this.list = new RtfList(this.body, this.writer, attrs);
      return this.list;
   }

   public void startBody() {
      this.bBody = true;
   }

   public void endBody() {
      this.bBody = false;
   }
}
