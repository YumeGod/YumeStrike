package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

abstract class RtfAfterBeforeBase extends RtfContainer implements IRtfParagraphContainer, IRtfExternalGraphicContainer, IRtfTableContainer, IRtfTextrunContainer {
   protected RtfAttributes attrib;
   private RtfParagraph para;
   private RtfExternalGraphic externalGraphic;
   private RtfTable table;

   RtfAfterBeforeBase(RtfSection parent, Writer w, RtfAttributes attrs) throws IOException {
      super(parent, w, attrs);
      this.attrib = attrs;
   }

   public RtfParagraph newParagraph() throws IOException {
      this.closeAll();
      this.para = new RtfParagraph(this, this.writer);
      return this.para;
   }

   public RtfParagraph newParagraph(RtfAttributes attrs) throws IOException {
      this.closeAll();
      this.para = new RtfParagraph(this, this.writer, attrs);
      return this.para;
   }

   public RtfExternalGraphic newImage() throws IOException {
      this.closeAll();
      this.externalGraphic = new RtfExternalGraphic(this, this.writer);
      return this.externalGraphic;
   }

   private void closeCurrentParagraph() throws IOException {
      if (this.para != null) {
         this.para.close();
      }

   }

   private void closeCurrentExternalGraphic() throws IOException {
      if (this.externalGraphic != null) {
         this.externalGraphic.close();
      }

   }

   private void closeCurrentTable() throws IOException {
      if (this.table != null) {
         this.table.close();
      }

   }

   protected void writeRtfPrefix() throws IOException {
      this.writeGroupMark(true);
      this.writeMyAttributes();
   }

   protected abstract void writeMyAttributes() throws IOException;

   protected void writeRtfSuffix() throws IOException {
      this.writeGroupMark(false);
   }

   public RtfAttributes getAttributes() {
      return this.attrib;
   }

   public void closeAll() throws IOException {
      this.closeCurrentParagraph();
      this.closeCurrentExternalGraphic();
      this.closeCurrentTable();
   }

   public RtfTable newTable(RtfAttributes attrs, ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, attrs, tc);
      return this.table;
   }

   public RtfTable newTable(ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, tc);
      return this.table;
   }

   public RtfTextrun getTextrun() throws IOException {
      return RtfTextrun.getTextrun(this, this.writer, (RtfAttributes)null);
   }
}
