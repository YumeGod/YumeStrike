package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfSection extends RtfContainer implements IRtfParagraphContainer, IRtfTableContainer, IRtfListContainer, IRtfExternalGraphicContainer, IRtfBeforeContainer, IRtfParagraphKeepTogetherContainer, IRtfAfterContainer, IRtfJforCmdContainer, IRtfTextrunContainer {
   private RtfParagraph paragraph;
   private RtfTable table;
   private RtfList list;
   private RtfExternalGraphic externalGraphic;
   private RtfBefore before;
   private RtfAfter after;
   private RtfJforCmd jforCmd;

   RtfSection(RtfDocumentArea parent, Writer w) throws IOException {
      super(parent, w);
   }

   public RtfExternalGraphic newImage() throws IOException {
      this.closeAll();
      this.externalGraphic = new RtfExternalGraphic(this, this.writer);
      return this.externalGraphic;
   }

   public RtfParagraph newParagraph(RtfAttributes attrs) throws IOException {
      this.closeAll();
      this.paragraph = new RtfParagraph(this, this.writer, attrs);
      return this.paragraph;
   }

   public RtfParagraph newParagraph() throws IOException {
      return this.newParagraph((RtfAttributes)null);
   }

   public RtfParagraphKeepTogether newParagraphKeepTogether() throws IOException {
      return new RtfParagraphKeepTogether(this, this.writer);
   }

   public RtfTable newTable(ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, tc);
      return this.table;
   }

   public RtfTable newTable(RtfAttributes attrs, ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, attrs, tc);
      return this.table;
   }

   public RtfList newList(RtfAttributes attrs) throws IOException {
      this.closeAll();
      this.list = new RtfList(this, this.writer, attrs);
      return this.list;
   }

   public RtfBefore newBefore(RtfAttributes attrs) throws IOException {
      this.closeAll();
      this.before = new RtfBefore(this, this.writer, attrs);
      return this.before;
   }

   public RtfAfter newAfter(RtfAttributes attrs) throws IOException {
      this.closeAll();
      this.after = new RtfAfter(this, this.writer, attrs);
      return this.after;
   }

   public RtfJforCmd newJforCmd(RtfAttributes attrs) throws IOException {
      this.jforCmd = new RtfJforCmd(this, this.writer, attrs);
      return this.jforCmd;
   }

   protected void writeRtfPrefix() throws IOException {
      this.writeAttributes(this.attrib, RtfPage.PAGE_ATTR);
      this.newLine();
      this.writeControlWord("sectd");
   }

   protected void writeRtfSuffix() throws IOException {
      this.writeControlWord("sect");
   }

   private void closeCurrentTable() throws IOException {
      if (this.table != null) {
         this.table.close();
      }

   }

   private void closeCurrentParagraph() throws IOException {
      if (this.paragraph != null) {
         this.paragraph.close();
      }

   }

   private void closeCurrentList() throws IOException {
      if (this.list != null) {
         this.list.close();
      }

   }

   private void closeCurrentExternalGraphic() throws IOException {
      if (this.externalGraphic != null) {
         this.externalGraphic.close();
      }

   }

   private void closeCurrentBefore() throws IOException {
      if (this.before != null) {
         this.before.close();
      }

   }

   private void closeAll() throws IOException {
      this.closeCurrentTable();
      this.closeCurrentParagraph();
      this.closeCurrentList();
      this.closeCurrentExternalGraphic();
      this.closeCurrentBefore();
   }

   public RtfTextrun getTextrun() throws IOException {
      return RtfTextrun.getTextrun(this, this.writer, (RtfAttributes)null);
   }
}
