package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class RtfTableCell extends RtfContainer implements IRtfParagraphContainer, IRtfListContainer, IRtfTableContainer, IRtfExternalGraphicContainer, IRtfTextrunContainer {
   private RtfParagraph paragraph;
   private RtfList list;
   private RtfTable table;
   private RtfExternalGraphic externalGraphic;
   private final RtfTableRow parentRow;
   private boolean setCenter;
   private boolean setRight;
   private int id;
   public static final int DEFAULT_CELL_WIDTH = 2000;
   private int cellWidth;
   private int widthOffset;
   private int vMerge = 0;
   private int hMerge = 0;
   public static final int NO_MERGE = 0;
   public static final int MERGE_START = 1;
   public static final int MERGE_WITH_PREVIOUS = 2;

   RtfTableCell(RtfTableRow parent, Writer w, int cellWidth, int idNum) throws IOException {
      super(parent, w);
      this.id = idNum;
      this.parentRow = parent;
      this.cellWidth = cellWidth;
      this.setCenter = false;
      this.setRight = false;
   }

   RtfTableCell(RtfTableRow parent, Writer w, int cellWidth, RtfAttributes attrs, int idNum) throws IOException {
      super(parent, w, attrs);
      this.id = idNum;
      this.parentRow = parent;
      this.cellWidth = cellWidth;
   }

   public RtfParagraph newParagraph(RtfAttributes attrs) throws IOException {
      this.closeAll();
      if (attrs == null) {
         attrs = new RtfAttributes();
      }

      attrs.set("intbl");
      this.paragraph = new RtfParagraph(this, this.writer, attrs);
      if (this.paragraph.attrib.isSet("qc")) {
         this.setCenter = true;
         attrs.set("qc");
      } else if (this.paragraph.attrib.isSet("qr")) {
         this.setRight = true;
         attrs.set("qr");
      } else {
         attrs.set("ql");
      }

      attrs.set("intbl");
      return this.paragraph;
   }

   public RtfExternalGraphic newImage() throws IOException {
      this.closeAll();
      this.externalGraphic = new RtfExternalGraphic(this, this.writer);
      return this.externalGraphic;
   }

   public RtfParagraph newParagraph() throws IOException {
      return this.newParagraph((RtfAttributes)null);
   }

   public RtfList newList(RtfAttributes attrib) throws IOException {
      this.closeAll();
      this.list = new RtfList(this, this.writer, attrib);
      return this.list;
   }

   public RtfTable newTable(ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, tc);
      return this.table;
   }

   public RtfTable newTable(RtfAttributes attrs, ITableColumnsInfo tc) throws IOException {
      this.closeAll();
      this.table = new RtfTable(this, this.writer, attrs, tc);
      return this.table;
   }

   int writeCellDef(int offset) throws IOException {
      if (this.hMerge == 2) {
         return offset;
      } else {
         this.newLine();
         this.widthOffset = offset;
         if (this.vMerge == 1) {
            this.writeControlWord("clvmgf");
         } else if (this.vMerge == 2) {
            this.writeControlWord("clvmrg");
         }

         this.writeAttributes(this.attrib, ITableAttributes.CELL_COLOR);
         this.writeAttributes(this.attrib, ITableAttributes.ATTRIB_CELL_PADDING);
         this.writeAttributes(this.attrib, ITableAttributes.CELL_BORDER);
         this.writeAttributes(this.attrib, IBorderAttributes.BORDERS);
         int iCurrentWidth = this.cellWidth;
         int xPos;
         if (this.attrib.getValue("number-columns-spanned") != null) {
            xPos = (Integer)this.attrib.getValue("number-columns-spanned");
            RtfTable tab = this.getRow().getTable();
            ITableColumnsInfo tableColumnsInfo = tab.getITableColumnsInfo();
            tableColumnsInfo.selectFirstColumn();

            while(this.id - 1 != tableColumnsInfo.getColumnIndex()) {
               tableColumnsInfo.selectNextColumn();
            }

            for(int i = xPos - 1; i > 0; --i) {
               tableColumnsInfo.selectNextColumn();
               iCurrentWidth += (int)tableColumnsInfo.getColumnWidth();
            }
         }

         xPos = offset + iCurrentWidth;
         if (this.setCenter) {
            this.writeControlWord("trqc");
         } else if (this.setRight) {
            this.writeControlWord("trqr");
         } else {
            this.writeControlWord("trql");
         }

         this.writeAttributes(this.attrib, ITableAttributes.CELL_VERT_ALIGN);
         this.writeControlWord("cellx" + xPos);
         return xPos;
      }
   }

   protected void writeRtfContent() throws IOException {
      if (this.hMerge != 2) {
         super.writeRtfContent();
      }
   }

   protected void writeRtfPrefix() throws IOException {
      if (this.hMerge != 2) {
         super.writeRtfPrefix();
      }
   }

   protected void writeRtfSuffix() throws IOException {
      if (this.hMerge != 2) {
         if (this.getRow().getTable().isNestedTable()) {
            this.writeControlWordNS("nestcell");
            this.writeGroupMark(true);
            this.writeControlWord("nonesttables");
            this.writeControlWord("par");
            this.writeGroupMark(false);
         } else {
            if (this.setCenter) {
               this.writeControlWord("qc");
            } else if (this.setRight) {
               this.writeControlWord("qr");
            } else {
               RtfElement lastChild = null;
               if (this.getChildren().size() > 0) {
                  lastChild = (RtfElement)this.getChildren().get(this.getChildren().size() - 1);
               }

               if (lastChild == null || !(lastChild instanceof RtfTextrun)) {
                  this.writeControlWord("ql");
               }
            }

            if (!this.containsText()) {
               this.writeControlWord("intbl");
            }

            this.writeControlWord("cell");
         }

      }
   }

   private void closeCurrentParagraph() throws IOException {
      if (this.paragraph != null) {
         this.paragraph.close();
      }

   }

   private void closeCurrentList() throws IOException {
      if (this.list != null) {
         this.list.close();
      }

   }

   private void closeCurrentTable() throws IOException {
      if (this.table != null) {
         this.table.close();
      }

   }

   private void closeCurrentExternalGraphic() throws IOException {
      if (this.externalGraphic != null) {
         this.externalGraphic.close();
      }

   }

   private void closeAll() throws IOException {
      this.closeCurrentTable();
      this.closeCurrentParagraph();
      this.closeCurrentList();
      this.closeCurrentExternalGraphic();
   }

   public void setVMerge(int mergeStatus) {
      this.vMerge = mergeStatus;
   }

   public int getVMerge() {
      return this.vMerge;
   }

   public void setHMerge(int mergeStatus) {
      this.hMerge = mergeStatus;
   }

   public int getHMerge() {
      return this.hMerge;
   }

   int getCellWidth() {
      return this.cellWidth;
   }

   public boolean isEmpty() {
      return false;
   }

   boolean paragraphNeedsPar(RtfParagraph p) {
      boolean pFound = false;
      boolean result = false;
      Iterator it = this.getChildren().iterator();

      while(it.hasNext()) {
         Object o = it.next();
         if (!pFound) {
            pFound = o == p;
         } else if (o instanceof RtfParagraph) {
            RtfParagraph p2 = (RtfParagraph)o;
            if (!p2.isEmpty()) {
               result = true;
               break;
            }
         } else if (o instanceof RtfTable) {
            break;
         }
      }

      return result;
   }

   public RtfTextrun getTextrun() throws IOException {
      RtfAttributes attrs = new RtfAttributes();
      if (!this.getRow().getTable().isNestedTable()) {
         attrs.set("intbl");
      }

      RtfTextrun textrun = RtfTextrun.getTextrun(this, this.writer, attrs);
      textrun.setSuppressLastPar(true);
      return textrun;
   }

   public RtfTableRow getRow() {
      for(RtfElement e = this; ((RtfElement)e).parent != null; e = ((RtfElement)e).parent) {
         if (((RtfElement)e).parent instanceof RtfTableRow) {
            return (RtfTableRow)((RtfElement)e).parent;
         }
      }

      return null;
   }
}
