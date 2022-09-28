package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public class RtfListStyleBullet extends RtfListStyle {
   public void writeListPrefix(RtfListItem item) throws IOException {
      item.writeControlWord("pnlvlblt");
      item.writeControlWord("ilvl0");
      item.writeOneAttribute("ls", new Integer(item.getNumber()));
      item.writeOneAttribute("pnindent", item.getParentList().attrib.getValue("levelindent"));
      item.writeControlWord("pnf1");
      item.writeGroupMark(true);
      item.writeControlWord("pndec");
      item.writeOneAttribute("f", "2");
      item.writeControlWord("pntxtb");
      item.writeControlWord("'b7");
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
      element.writeOneAttributeNS("leveltext", "\\'01\\'b7");
      element.writeGroupMark(false);
      element.writeGroupMark(true);
      element.writeOneAttributeNS("levelnumbers", (Object)null);
      element.writeGroupMark(false);
      element.attrib.set("f", 2);
   }
}
