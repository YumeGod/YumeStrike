package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public class RtfListStyleNumber extends RtfListStyle {
   public void writeListPrefix(RtfListItem item) throws IOException {
      item.writeControlWord("pnlvlbody");
      item.writeControlWord("ilvl0");
      item.writeOneAttribute("ls", "0");
      item.writeControlWord("pndec");
      item.writeOneAttribute("pnstart", new Integer(1));
      item.writeOneAttribute("pnindent", item.attrib.getValue("levelindent"));
      item.writeControlWord("pntxta.");
   }

   public void writeParagraphPrefix(RtfElement element) throws IOException {
      element.writeGroupMark(true);
      element.writeControlWord("pntext");
      element.writeControlWord("f" + RtfFontManager.getInstance().getFontNumber("Symbol"));
      element.writeControlWord("'b7");
      element.writeControlWord("tab");
      element.writeGroupMark(false);
   }

   public void writeLevelGroup(RtfElement element) throws IOException {
      element.writeOneAttributeNS("levelstartat", new Integer(1));
      element.attrib.set("levelnfc", 0);
      element.writeGroupMark(true);
      element.writeOneAttributeNS("leveltext", "\\'03\\'00. ;");
      element.writeGroupMark(false);
      element.writeGroupMark(true);
      element.writeOneAttributeNS("levelnumbers", "\\'01;");
      element.writeGroupMark(false);
      element.writeOneAttribute("f", new Integer(0));
   }
}
