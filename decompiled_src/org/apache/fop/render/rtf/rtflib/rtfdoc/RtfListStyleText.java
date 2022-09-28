package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public class RtfListStyleText extends RtfListStyle {
   private String text;

   public RtfListStyleText(String s) {
      this.text = s;
   }

   public void writeListPrefix(RtfListItem item) throws IOException {
      item.writeControlWord("pnlvlblt");
      item.writeControlWord("ilvl0");
      item.writeOneAttribute("ls", new Integer(item.getNumber()));
      item.writeOneAttribute("pnindent", item.getParentList().attrib.getValue("levelindent"));
      item.writeControlWord("pnf1");
      item.writeGroupMark(true);
      item.writeOneAttribute("f", "2");
      item.writeControlWord("pntxtb");
      RtfStringConverter.getInstance().writeRtfString(item.writer, this.text);
      item.writeGroupMark(false);
   }

   public void writeParagraphPrefix(RtfElement element) throws IOException {
      element.writeGroupMark(true);
      element.writeControlWord("pntext");
      element.writeGroupMark(false);
   }

   public void writeLevelGroup(RtfElement element) throws IOException {
      element.attrib.set("levelnfc", 23);
      element.writeGroupMark(true);
      String sCount;
      if (this.text.length() < 10) {
         sCount = "0" + String.valueOf(this.text.length());
      } else {
         sCount = String.valueOf(Integer.toHexString(this.text.length()));
         if (sCount.length() == 1) {
            sCount = "0" + sCount;
         }
      }

      element.writeOneAttributeNS("leveltext", "\\'" + sCount + RtfStringConverter.getInstance().escape(this.text));
      element.writeGroupMark(false);
      element.writeGroupMark(true);
      element.writeOneAttributeNS("levelnumbers", (Object)null);
      element.writeGroupMark(false);
      element.attrib.set("f", 2);
   }
}
