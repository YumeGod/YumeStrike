package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public class RtfListStyle {
   private RtfListItem rtfListItem;

   public void setRtfListItem(RtfListItem item) {
      this.rtfListItem = item;
   }

   public RtfListItem getRtfListItem() {
      return this.rtfListItem;
   }

   public RtfList getRtfList() {
      return this.rtfListItem.getParentList();
   }

   public void writeListPrefix(RtfListItem item) throws IOException {
   }

   public void writeParagraphPrefix(RtfElement element) throws IOException {
   }

   public void writeLevelGroup(RtfElement element) throws IOException {
   }
}
