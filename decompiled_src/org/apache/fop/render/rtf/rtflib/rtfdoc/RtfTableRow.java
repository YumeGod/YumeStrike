package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class RtfTableRow extends RtfContainer implements ITableAttributes {
   private RtfTableCell cell;
   private int id;
   private int highestCell = 0;

   RtfTableRow(RtfTable parent, Writer w, int idNum) throws IOException {
      super(parent, w);
      this.id = idNum;
   }

   RtfTableRow(RtfTable parent, Writer w, RtfAttributes attrs, int idNum) throws IOException {
      super(parent, w, attrs);
      this.id = idNum;
   }

   public RtfTableCell newTableCell(int cellWidth) throws IOException {
      ++this.highestCell;
      this.cell = new RtfTableCell(this, this.writer, cellWidth, this.highestCell);
      return this.cell;
   }

   public RtfTableCell newTableCell(int cellWidth, RtfAttributes attrs) throws IOException {
      ++this.highestCell;
      this.cell = new RtfTableCell(this, this.writer, cellWidth, attrs, this.highestCell);
      return this.cell;
   }

   public RtfTableCell newTableCellMergedVertically(int cellWidth, RtfAttributes attrs) throws IOException {
      ++this.highestCell;
      this.cell = new RtfTableCell(this, this.writer, cellWidth, attrs, this.highestCell);
      this.cell.setVMerge(2);
      return this.cell;
   }

   public RtfTableCell newTableCellMergedHorizontally(int cellWidth, RtfAttributes attrs) throws IOException {
      ++this.highestCell;
      RtfAttributes wAttributes = null;
      if (attrs != null) {
         wAttributes = (RtfAttributes)attrs.clone();
      }

      this.cell = new RtfTableCell(this, this.writer, cellWidth, wAttributes, this.highestCell);
      this.cell.setHMerge(2);
      return this.cell;
   }

   protected void writeRtfPrefix() throws IOException {
      this.newLine();
      this.writeGroupMark(true);
   }

   protected void writeRtfContent() throws IOException {
      if (this.getTable().isNestedTable()) {
         this.writeControlWord("intbl");
         this.writeControlWord("itap" + this.getTable().getNestedTableDepth());
      } else {
         this.writeRowAndCellsDefintions();
      }

      super.writeRtfContent();
   }

   public void writeRowAndCellsDefintions() throws IOException {
      this.writeControlWord("trowd");
      if (!this.getTable().isNestedTable()) {
         this.writeControlWord("itap0");
      }

      if (this.attrib != null && this.attrib.isSet("trkeep")) {
         this.writeControlWord("trkeep");
      }

      this.writePaddingAttributes();
      RtfTable parentTable = (RtfTable)this.parent;
      this.adjustBorderProperties(parentTable);
      this.writeAttributes(this.attrib, new String[]{"trhdr"});
      this.writeAttributes(this.attrib, ITableAttributes.ROW_BORDER);
      this.writeAttributes(this.attrib, ITableAttributes.CELL_BORDER);
      this.writeAttributes(this.attrib, IBorderAttributes.BORDERS);
      if (this.attrib.isSet("trrh")) {
         this.writeOneAttribute("trrh", this.attrib.getValue("trrh"));
      }

      int xPos = 0;
      Object leftIndent = this.attrib.getValue("trleft");
      if (leftIndent != null) {
         xPos = (Integer)leftIndent;
      }

      RtfAttributes tableBorderAttributes = this.getTable().getBorderAttributes();
      int index = 0;

      for(Iterator it = this.getChildren().iterator(); it.hasNext(); ++index) {
         RtfElement e = (RtfElement)it.next();
         if (e instanceof RtfTableCell) {
            RtfTableCell rtfcell = (RtfTableCell)e;
            if (tableBorderAttributes != null) {
               String border;
               if (index == 0) {
                  border = "clbrdrl";
                  if (!rtfcell.getRtfAttributes().isSet(border)) {
                     rtfcell.getRtfAttributes().set(border, (RtfAttributes)tableBorderAttributes.getValue(border));
                  }
               }

               if (index == this.getChildCount() - 1) {
                  border = "clbrdrr";
                  if (!rtfcell.getRtfAttributes().isSet(border)) {
                     rtfcell.getRtfAttributes().set(border, (RtfAttributes)tableBorderAttributes.getValue(border));
                  }
               }

               if (this.isFirstRow()) {
                  border = "clbrdrt";
                  if (!rtfcell.getRtfAttributes().isSet(border)) {
                     rtfcell.getRtfAttributes().set(border, (RtfAttributes)tableBorderAttributes.getValue(border));
                  }
               }

               if (parentTable != null && parentTable.isHighestRow(this.id)) {
                  border = "clbrdrb";
                  if (!rtfcell.getRtfAttributes().isSet(border)) {
                     rtfcell.getRtfAttributes().set(border, (RtfAttributes)tableBorderAttributes.getValue(border));
                  }
               }
            }

            if (index == 0 && !rtfcell.getRtfAttributes().isSet("clbrdrl")) {
               rtfcell.getRtfAttributes().set("clbrdrl", (String)this.attrib.getValue("trbrdrl"));
            }

            if (index == this.getChildCount() - 1 && !rtfcell.getRtfAttributes().isSet("clbrdrr")) {
               rtfcell.getRtfAttributes().set("clbrdrr", (String)this.attrib.getValue("trbrdrr"));
            }

            if (this.isFirstRow() && !rtfcell.getRtfAttributes().isSet("clbrdrt")) {
               rtfcell.getRtfAttributes().set("clbrdrt", (String)this.attrib.getValue("trbrdrt"));
            }

            if (parentTable != null && parentTable.isHighestRow(this.id) && !rtfcell.getRtfAttributes().isSet("clbrdrb")) {
               rtfcell.getRtfAttributes().set("clbrdrb", (String)this.attrib.getValue("trbrdrb"));
            }

            xPos = rtfcell.writeCellDef(xPos);
         }
      }

      this.newLine();
   }

   private void adjustBorderProperties(RtfTable parentTable) {
      if (this.attrib != null && parentTable != null) {
         if (this.isFirstRow() && parentTable.isHighestRow(this.id)) {
            this.attrib.unset("trbrdrh");
         } else if (this.isFirstRow()) {
            this.attrib.unset("trbrdrb");
         } else if (parentTable.isHighestRow(this.id)) {
            this.attrib.unset("trbrdrt");
         } else {
            this.attrib.unset("trbrdrb");
            this.attrib.unset("trbrdrt");
         }
      }

   }

   protected void writeRtfSuffix() throws IOException {
      if (this.getTable().isNestedTable()) {
         this.writeGroupMark(true);
         this.writeStarControlWord("nesttableprops");
         this.writeRowAndCellsDefintions();
         this.writeControlWordNS("nestrow");
         this.writeGroupMark(false);
         this.writeGroupMark(true);
         this.writeControlWord("nonesttables");
         this.writeControlWord("par");
         this.writeGroupMark(false);
      } else {
         this.writeControlWord("row");
      }

      this.writeGroupMark(false);
   }

   private void writePaddingAttributes() throws IOException {
      if (this.attrib != null && !this.attrib.isSet("trgaph")) {
         int gaph = -1;

         try {
            Integer leftPadStr = (Integer)this.attrib.getValue("trpaddl");
            if (leftPadStr != null) {
               gaph = leftPadStr;
            }

            Integer rightPadStr = (Integer)this.attrib.getValue("trpaddr");
            if (rightPadStr != null) {
               gaph = (gaph + rightPadStr) / 2;
            }
         } catch (Exception var4) {
            String var3 = "RtfTableRow.writePaddingAttributes: " + var4.toString();
         }

         if (gaph >= 0) {
            this.attrib.set("trgaph", gaph);
         }
      }

      this.writeAttributes(this.attrib, ATTRIB_ROW_PADDING);
   }

   public boolean isFirstRow() {
      return this.id == 1;
   }

   public boolean isHighestCell(int cellId) {
      return this.highestCell == cellId;
   }

   public RtfTable getTable() {
      for(RtfElement e = this; ((RtfElement)e).parent != null; e = ((RtfElement)e).parent) {
         if (((RtfElement)e).parent instanceof RtfTable) {
            return (RtfTable)((RtfElement)e).parent;
         }
      }

      return null;
   }
}
