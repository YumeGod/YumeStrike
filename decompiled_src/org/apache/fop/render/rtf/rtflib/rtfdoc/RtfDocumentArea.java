package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfDocumentArea extends RtfContainer {
   private RtfSection currentSection;

   RtfDocumentArea(RtfFile f, Writer w) throws IOException {
      super(f, w);
   }

   public RtfSection newSection() throws IOException {
      if (this.currentSection != null) {
         this.currentSection.close();
      }

      this.currentSection = new RtfSection(this, this.writer);
      return this.currentSection;
   }
}
