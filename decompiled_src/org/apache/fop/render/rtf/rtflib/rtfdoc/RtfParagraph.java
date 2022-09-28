package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class RtfParagraph extends RtfBookmarkContainerImpl implements IRtfTextContainer, IRtfPageBreakContainer, IRtfHyperLinkContainer, IRtfExternalGraphicContainer, IRtfPageNumberContainer, IRtfPageNumberCitationContainer {
   private RtfText text;
   private RtfHyperLink hyperlink;
   private RtfExternalGraphic externalGraphic;
   private RtfPageNumber pageNumber;
   private RtfPageNumberCitation pageNumberCitation;
   private boolean keepn = false;
   private boolean resetProperties = false;
   private boolean writeForBreak = false;
   private static final String[] PARA_ATTRIBUTES = new String[]{"intbl"};

   RtfParagraph(IRtfParagraphContainer parent, Writer w) throws IOException {
      super((RtfContainer)parent, w);
   }

   RtfParagraph(IRtfParagraphContainer parent, Writer w, RtfAttributes attr) throws IOException {
      super((RtfContainer)parent, w, attr);
   }

   public String getText() {
      return this.text.getText();
   }

   public void setKeepn() {
      this.keepn = true;
   }

   public void setResetProperties() {
      this.resetProperties = true;
   }

   public RtfAttributes getTextContainerAttributes() {
      return this.attrib == null ? null : (RtfAttributes)this.attrib.clone();
   }

   protected void writeRtfPrefix() throws IOException {
      if (this.resetProperties) {
         this.writeControlWord("pard");
      }

      this.writeAttributes(this.attrib, RtfText.ATTR_NAMES);
      this.writeAttributes(this.attrib, PARA_ATTRIBUTES);
      if (this.attrib.isSet("intbl") && this.mustWriteAttributes()) {
         this.writeAttributes(this.attrib, RtfText.ALIGNMENT);
      }

      if (this.keepn) {
         this.writeControlWord("keepn");
      }

      if (this.mustWriteGroupMark()) {
         this.writeGroupMark(true);
      }

      if (this.mustWriteAttributes()) {
         if (!this.attrib.isSet("intbl")) {
            this.writeAttributes(this.attrib, RtfText.ALIGNMENT);
         }

         this.writeAttributes(this.attrib, RtfText.BORDER);
         this.writeAttributes(this.attrib, RtfText.INDENT);
         this.writeAttributes(this.attrib, RtfText.TABS);
         if (this.writeForBreak) {
            this.writeControlWord("pard\\par");
         }
      }

   }

   protected void writeRtfSuffix() throws IOException {
      boolean writeMark = true;
      if (this.parent instanceof RtfTableCell) {
         writeMark = ((RtfTableCell)this.parent).paragraphNeedsPar(this);
      }

      if (writeMark) {
         this.writeControlWord("par");
      }

      if (this.mustWriteGroupMark()) {
         this.writeGroupMark(false);
      }

   }

   public RtfText newText(String str) throws IOException {
      return this.newText(str, (RtfAttributes)null);
   }

   public RtfText newText(String str, RtfAttributes attr) throws IOException {
      this.closeAll();
      this.text = new RtfText(this, this.writer, str, attr);
      return this.text;
   }

   public void newPageBreak() throws IOException {
      this.writeForBreak = true;
      new RtfPageBreak(this, this.writer);
   }

   public void newLineBreak() throws IOException {
      new RtfLineBreak(this, this.writer);
   }

   public RtfPageNumber newPageNumber() throws IOException {
      this.pageNumber = new RtfPageNumber(this, this.writer);
      return this.pageNumber;
   }

   public RtfPageNumberCitation newPageNumberCitation(String id) throws IOException {
      this.pageNumberCitation = new RtfPageNumberCitation(this, this.writer, id);
      return this.pageNumberCitation;
   }

   public RtfHyperLink newHyperLink(String str, RtfAttributes attr) throws IOException {
      this.hyperlink = new RtfHyperLink(this, this.writer, str, attr);
      return this.hyperlink;
   }

   public RtfExternalGraphic newImage() throws IOException {
      this.closeAll();
      this.externalGraphic = new RtfExternalGraphic(this, this.writer);
      return this.externalGraphic;
   }

   private void closeCurrentText() throws IOException {
      if (this.text != null) {
         this.text.close();
      }

   }

   private void closeCurrentHyperLink() throws IOException {
      if (this.hyperlink != null) {
         this.hyperlink.close();
      }

   }

   private void closeAll() throws IOException {
      this.closeCurrentText();
      this.closeCurrentHyperLink();
   }

   protected boolean okToWriteRtf() {
      boolean result = super.okToWriteRtf();
      if (this.parent.getOptions().ignoreEmptyParagraphs() && this.getChildCount() == 0) {
         result = false;
      }

      return result;
   }

   private boolean mustWriteAttributes() {
      boolean writeAttributes = false;
      int children = this.getChildCount();
      if (children > 0) {
         List childList = this.getChildren();

         for(int i = 0; i < children; ++i) {
            RtfElement el = (RtfElement)childList.get(i);
            if (!el.isEmpty()) {
               if (el.getClass() != RtfText.class) {
                  writeAttributes = true;
                  break;
               }

               boolean tmp = ((RtfText)el).isNbsp();
               if (!tmp) {
                  writeAttributes = true;
                  break;
               }
            }
         }
      }

      return writeAttributes;
   }

   private boolean mustWriteGroupMark() {
      return this.getChildCount() > 0;
   }

   public RtfAttributes getTextAttributes() {
      return this.text == null ? null : this.text.getTextAttributes();
   }
}
