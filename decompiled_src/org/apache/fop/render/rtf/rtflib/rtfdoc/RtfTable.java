package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;

public class RtfTable extends RtfContainer {
   private RtfTableRow row;
   private int highestRow = 0;
   private Boolean isNestedTable = null;
   private RtfAttributes borderAttributes = null;
   private ITableColumnsInfo tableContext;
   private int nestedTableDepth = 0;
   private RtfAttributes headerAttribs = null;

   RtfTable(IRtfTableContainer parent, Writer w, ITableColumnsInfo tc) throws IOException {
      super((RtfContainer)parent, w);
      this.tableContext = tc;
   }

   RtfTable(IRtfTableContainer parent, Writer w, RtfAttributes attrs, ITableColumnsInfo tc) throws IOException {
      super((RtfContainer)parent, w, attrs);
      this.tableContext = tc;
   }

   public RtfTableRow newTableRow() throws IOException {
      if (this.row != null) {
         this.row.close();
      }

      ++this.highestRow;
      this.row = new RtfTableRow(this, this.writer, this.attrib, this.highestRow);
      return this.row;
   }

   public RtfTableRow newTableRow(RtfAttributes attrs) throws IOException {
      RtfAttributes attr = null;
      if (this.attrib != null) {
         attr = (RtfAttributes)this.attrib.clone();
         attr.set(attrs);
      } else {
         attr = attrs;
      }

      if (this.row != null) {
         this.row.close();
      }

      ++this.highestRow;
      this.row = new RtfTableRow(this, this.writer, attr, this.highestRow);
      return this.row;
   }

   protected void writeRtfPrefix() throws IOException {
      if (this.isNestedTable()) {
         this.writeControlWordNS("pard");
      }

      this.writeGroupMark(true);
   }

   protected void writeRtfSuffix() throws IOException {
      this.writeGroupMark(false);
      if (this.isNestedTable()) {
         this.getRow().writeRowAndCellsDefintions();
      }

   }

   public boolean isHighestRow(int id) {
      return this.highestRow == id;
   }

   public ITableColumnsInfo getITableColumnsInfo() {
      return this.tableContext;
   }

   public void setHeaderAttribs(RtfAttributes attrs) {
      this.headerAttribs = attrs;
   }

   public RtfAttributes getHeaderAttribs() {
      return this.headerAttribs;
   }

   public RtfAttributes getRtfAttributes() {
      return this.headerAttribs != null ? this.headerAttribs : super.getRtfAttributes();
   }

   public boolean isNestedTable() {
      if (this.isNestedTable == null) {
         for(RtfElement e = this; ((RtfElement)e).parent != null; e = ((RtfElement)e).parent) {
            if (((RtfElement)e).parent instanceof RtfTableCell) {
               this.isNestedTable = Boolean.TRUE;
               return true;
            }
         }

         this.isNestedTable = Boolean.FALSE;
         return false;
      } else {
         return this.isNestedTable;
      }
   }

   public RtfTableRow getRow() {
      for(RtfElement e = this; ((RtfElement)e).parent != null; e = ((RtfElement)e).parent) {
         if (((RtfElement)e).parent instanceof RtfTableRow) {
            return (RtfTableRow)((RtfElement)e).parent;
         }
      }

      return null;
   }

   public void setNestedTableDepth(int nestedTableDepth) {
      this.nestedTableDepth = nestedTableDepth;
   }

   public int getNestedTableDepth() {
      return this.nestedTableDepth;
   }

   public void setBorderAttributes(RtfAttributes attributes) {
      this.borderAttributes = attributes;
   }

   public RtfAttributes getBorderAttributes() {
      return this.borderAttributes;
   }
}
