package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class RtfList extends RtfContainer {
   private RtfListItem item;
   private RtfListTable listTable;
   private final boolean hasTableParent;
   private RtfListStyle defaultListStyle;
   private Integer listTemplateId = null;
   private Integer listId = null;
   private static Random listIdGenerator = new Random(0L);

   RtfList(RtfContainer parent, Writer w, RtfAttributes attr) throws IOException {
      super(parent, w, attr);
      this.listId = new Integer(listIdGenerator.nextInt());
      this.listTemplateId = new Integer(listIdGenerator.nextInt());
      this.listTable = this.getRtfFile().startListTable(attr);
      this.listTable.addList(this);
      this.hasTableParent = this.getParentOfClass(RtfTable.class) != null;
      this.setRtfListStyle(new RtfListStyleBullet());
   }

   public RtfListItem newListItem() throws IOException {
      if (this.item != null) {
         this.item.close();
      }

      this.item = new RtfListItem(this, this.writer);
      return this.item;
   }

   public Integer getListId() {
      return this.listId;
   }

   public Integer getListTemplateId() {
      return this.listTemplateId;
   }

   public void setRtfListStyle(RtfListStyle ls) {
      this.defaultListStyle = ls;
   }

   public RtfListStyle getRtfListStyle() {
      return this.defaultListStyle;
   }

   public boolean getHasTableParent() {
      return this.hasTableParent;
   }
}
