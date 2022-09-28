package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfHyperLink extends RtfContainer implements IRtfTextContainer, IRtfTextrunContainer {
   protected String url = null;
   protected RtfText mText = null;

   public RtfHyperLink(IRtfTextContainer parent, Writer writer, String str, RtfAttributes attr) throws IOException {
      super((RtfContainer)parent, writer, attr);
      new RtfText(this, writer, str, attr);
   }

   public RtfHyperLink(RtfTextrun parent, Writer writer, RtfAttributes attr) throws IOException {
      super(parent, writer, attr);
   }

   public void writeRtfPrefix() throws IOException {
      super.writeGroupMark(true);
      super.writeControlWord("field");
      super.writeGroupMark(true);
      super.writeStarControlWord("fldinst");
      this.writer.write("HYPERLINK \"" + this.url + "\" ");
      super.writeGroupMark(false);
      super.writeGroupMark(true);
      super.writeControlWord("fldrslt");
      if (this.attrib != null && this.attrib.isSet("cs")) {
         this.writeGroupMark(true);
         this.writeAttributes(this.attrib, new String[]{"cs"});
      }

   }

   public void writeRtfSuffix() throws IOException {
      if (this.attrib != null && this.attrib.isSet("cs")) {
         this.writeGroupMark(false);
      }

      super.writeGroupMark(false);
      super.writeGroupMark(false);
   }

   public RtfText newText(String str) throws IOException {
      return this.newText(str, (RtfAttributes)null);
   }

   public RtfText newText(String str, RtfAttributes attr) throws IOException {
      this.closeAll();
      this.mText = new RtfText(this, this.writer, str, attr);
      return this.mText;
   }

   public RtfAttributes getTextContainerAttributes() {
      return this.attrib == null ? null : (RtfAttributes)this.attrib.clone();
   }

   public void newLineBreak() throws IOException {
      new RtfLineBreak(this, this.writer);
   }

   private void closeCurrentText() throws IOException {
      if (this.mText != null) {
         this.mText.close();
      }

   }

   private void closeAll() throws IOException {
      this.closeCurrentText();
   }

   public void setExternalURL(String url) {
      this.url = url;
   }

   public void setInternalURL(String jumpTo) {
      int now = jumpTo.length();
      int max = 40;
      this.url = "#" + jumpTo.substring(0, now > max ? max : now);
      this.url = this.url.replace('.', '_');
      this.url = this.url.replace(' ', '_');
   }

   public boolean isEmpty() {
      return false;
   }

   public RtfTextrun getTextrun() throws IOException {
      RtfTextrun textrun = RtfTextrun.getTextrun(this, this.writer, (RtfAttributes)null);
      return textrun;
   }
}
