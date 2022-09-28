package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfPageArea extends RtfContainer {
   private RtfPage currentPage;
   private RtfNull nullChild;
   private RtfAttributes childAttributes;

   RtfPageArea(RtfFile f, Writer w) throws IOException {
      super(f, w);
   }

   public RtfPage newPage(RtfAttributes attr) throws IOException {
      if (this.currentPage != null) {
         this.currentPage.close();
      }

      this.currentPage = new RtfPage(this, this.writer, attr);
      return this.currentPage;
   }

   protected boolean okToWriteRtf() {
      return true;
   }
}
