package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

public class RtfListTable extends RtfContainer {
   private LinkedList lists;
   private LinkedList styles = new LinkedList();
   public static final String LIST_TABLE = "listtable";
   public static final String LIST = "list";
   public static final String LIST_TEMPLATE_ID = "listtemplateid";
   public static final String LIST_LEVEL = "listlevel";
   public static final String LIST_NUMBER_TYPE = "levelnfc";
   public static final String LIST_JUSTIFICATION = "leveljc";
   public static final String LIST_FOLLOWING_CHAR = "levelfollow";
   public static final String LIST_START_AT = "levelstartat";
   public static final String LIST_SPACE = "levelspace";
   public static final String LIST_INDENT = "levelindent";
   public static final String LIST_TEXT_FORM = "leveltext";
   public static final String LIST_NUM_POSITION = "levelnumbers";
   public static final String LIST_NAME = "listname ;";
   public static final String LIST_ID = "listid";
   public static final String LIST_FONT_TYPE = "f";
   public static final String LIST_OVR_TABLE = "listoverridetable";
   public static final String LIST_OVR = "listoverride";
   public static final String LIST_OVR_COUNT = "listoverridecount";
   public static final String LIST_NUMBER = "ls";
   public static final String[] LIST_TABLE_ATTR = new String[]{"listtable", "list", "listtemplateid", "levelnfc", "leveljc", "levelfollow", "levelstartat", "levelspace", "levelindent", "leveltext", "levelnumbers", "listid", "listoverridetable", "listoverride", "listoverridecount", "ls", "listlevel"};

   public RtfListTable(RtfContainer parent, Writer w, Integer num, RtfAttributes attrs) throws IOException {
      super(parent, w, attrs);
   }

   public int addList(RtfList list) {
      if (this.lists == null) {
         this.lists = new LinkedList();
      }

      this.lists.add(list);
      return this.lists.size();
   }

   public void writeRtfContent() throws IOException {
      this.newLine();
      if (this.lists != null) {
         this.writeGroupMark(true);
         this.writeStarControlWordNS("listtable");
         this.newLine();
         Iterator it = this.lists.iterator();

         while(it.hasNext()) {
            RtfList list = (RtfList)it.next();
            this.writeListTableEntry(list);
            this.newLine();
         }

         this.writeGroupMark(false);
         this.newLine();
         this.writeGroupMark(true);
         this.writeStarControlWordNS("listoverridetable");
         int z = 1;
         this.newLine();
         Iterator it = this.styles.iterator();

         while(it.hasNext()) {
            RtfListStyle style = (RtfListStyle)it.next();
            this.writeGroupMark(true);
            this.writeStarControlWordNS("listoverride");
            this.writeGroupMark(true);
            this.writeOneAttributeNS("listid", style.getRtfList().getListId().toString());
            this.writeOneAttributeNS("listoverridecount", new Integer(0));
            this.writeOneAttributeNS("ls", new Integer(z++));
            this.writeGroupMark(false);
            this.writeGroupMark(false);
            this.newLine();
         }

         this.writeGroupMark(false);
         this.newLine();
      }

   }

   public boolean isEmpty() {
      return false;
   }

   private void writeListTableEntry(RtfList list) throws IOException {
      this.writeGroupMark(true);
      this.writeControlWordNS("list");
      this.writeOneAttributeNS("listtemplateid", list.getListTemplateId().toString());
      this.writeOneAttributeNS("list", this.attrib.getValue("list"));
      this.writeGroupMark(true);
      this.writeControlWordNS("listlevel");
      this.writeOneAttributeNS("leveljc", this.attrib.getValue("leveljc"));
      this.writeOneAttributeNS("levelfollow", this.attrib.getValue("levelfollow"));
      this.writeOneAttributeNS("levelspace", new Integer(0));
      this.writeOneAttributeNS("levelindent", this.attrib.getValue("levelindent"));
      RtfListItem item = (RtfListItem)list.getChildren().get(0);
      if (item != null) {
         item.getRtfListStyle().writeLevelGroup(this);
      }

      this.writeGroupMark(false);
      this.writeGroupMark(true);
      this.writeControlWordNS("listname ;");
      this.writeGroupMark(false);
      this.writeOneAttributeNS("listid", list.getListId().toString());
      this.writeGroupMark(false);
   }

   public int addRtfListStyle(RtfListStyle ls) {
      this.styles.add(ls);
      return this.styles.size();
   }
}
