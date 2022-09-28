package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfListItem extends RtfContainer implements IRtfTextrunContainer, IRtfListContainer, IRtfParagraphContainer {
   private RtfList parentList;
   private RtfParagraph paragraph;
   private RtfListStyle listStyle;
   private int number = 0;

   RtfListItem(RtfList parent, Writer w) throws IOException {
      super(parent, w);
      this.parentList = parent;
   }

   public RtfParagraph newParagraph(RtfAttributes attrs) throws IOException {
      if (this.paragraph != null) {
         this.paragraph.close();
      }

      this.paragraph = new RtfListItemParagraph(this, attrs);
      return this.paragraph;
   }

   public RtfParagraph newParagraph() throws IOException {
      return this.newParagraph((RtfAttributes)null);
   }

   RtfListItem(RtfList parent, Writer w, RtfAttributes attr) throws IOException {
      super(parent, w, attr);
      this.parentList = parent;
   }

   public RtfTextrun getTextrun() throws IOException {
      RtfTextrun textrun = RtfTextrun.getTextrun(this, this.writer, (RtfAttributes)null);
      textrun.setRtfListItem(this);
      return textrun;
   }

   public RtfList newList(RtfAttributes attrs) throws IOException {
      RtfList list = new RtfList(this, this.writer, attrs);
      return list;
   }

   protected void writeRtfPrefix() throws IOException {
      if (!this.parentList.getHasTableParent()) {
         this.writeControlWord("pard");
      }

      this.writeOneAttribute("fi-", "360");
      this.writeOneAttribute("li", this.attrib.getValue("li"));
      this.writeGroupMark(true);
      this.writeStarControlWord("pn");
      this.getRtfListStyle().writeListPrefix(this);
      this.writeGroupMark(false);
      this.writeOneAttribute("ls", new Integer(this.number));
   }

   protected void writeRtfSuffix() throws IOException {
      super.writeRtfSuffix();
      if (!this.parentList.getHasTableParent()) {
         this.writeControlWord("pard");
      }

   }

   public void setRtfListStyle(RtfListStyle ls) {
      this.listStyle = ls;
      this.listStyle.setRtfListItem(this);
      this.number = this.getRtfFile().getListTable().addRtfListStyle(ls);
   }

   public RtfListStyle getRtfListStyle() {
      return this.listStyle == null ? this.parentList.getRtfListStyle() : this.listStyle;
   }

   public RtfList getParentList() {
      return this.parentList;
   }

   public int getNumber() {
      return this.number;
   }

   public class RtfListItemLabel extends RtfTextrun implements IRtfTextrunContainer {
      private RtfListItem rtfListItem;

      public RtfListItemLabel(RtfListItem item) throws IOException {
         super((RtfContainer)null, item.writer, (RtfAttributes)null);
         this.rtfListItem = item;
      }

      public RtfTextrun getTextrun() throws IOException {
         return this;
      }

      public void addString(String s) throws IOException {
         String label = s.trim();
         if (label.length() > 0 && Character.isDigit(label.charAt(0))) {
            this.rtfListItem.setRtfListStyle(new RtfListStyleNumber());
         } else {
            this.rtfListItem.setRtfListStyle(new RtfListStyleText(label));
         }

      }
   }

   private class RtfListItemParagraph extends RtfParagraph {
      RtfListItemParagraph(RtfListItem rli, RtfAttributes attrs) throws IOException {
         super(rli, rli.writer, attrs);
      }

      protected void writeRtfPrefix() throws IOException {
         super.writeRtfPrefix();
         RtfListItem.this.getRtfListStyle().writeParagraphPrefix(this);
      }
   }
}
