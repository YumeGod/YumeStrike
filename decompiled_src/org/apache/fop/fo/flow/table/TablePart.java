package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class TablePart extends TableCellContainer {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   protected boolean tableRowsFound = false;
   protected boolean tableCellsFound = false;
   private boolean firstRow = true;
   private boolean rowsStarted = false;
   private boolean lastCellEndsRow = true;
   private List rowGroups = new LinkedList();

   public TablePart(FONode parent) {
      super(parent);
   }

   protected Object clone() {
      TablePart clone = (TablePart)super.clone();
      clone.rowGroups = new LinkedList(this.rowGroups);
      return clone;
   }

   public void bind(PropertyList pList) throws FOPException {
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      super.bind(pList);
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      super.processNode(elementName, locator, attlist, pList);
      if (!this.inMarker()) {
         Table t = this.getTable();
         if (t.hasExplicitColumns()) {
            int size = t.getNumberOfColumns();
            this.pendingSpans = new ArrayList(size);

            for(int i = 0; i < size; ++i) {
               this.pendingSpans.add((Object)null);
            }
         } else {
            this.pendingSpans = new ArrayList();
         }

         this.columnNumberManager = new ColumnNumberManager();
      }

   }

   public void finalizeNode() throws FOPException {
      if (!this.inMarker()) {
         this.pendingSpans = null;
         this.columnNumberManager = null;
      }

      if (!this.tableRowsFound && !this.tableCellsFound) {
         this.missingChildElementError("marker* (table-row+|table-cell+)", true);
         this.getParent().removeChild(this);
      } else {
         this.finishLastRowGroup();
      }

   }

   TablePart getTablePart() {
      return this;
   }

   protected void finishLastRowGroup() throws ValidationException {
      if (!this.inMarker()) {
         RowGroupBuilder rowGroupBuilder = this.getTable().getRowGroupBuilder();
         if (this.tableRowsFound) {
            rowGroupBuilder.endTableRow();
         } else if (!this.lastCellEndsRow) {
            rowGroupBuilder.endRow(this);
         }

         try {
            rowGroupBuilder.endTablePart();
         } catch (ValidationException var3) {
            var3.setLocator(this.locator);
            throw var3;
         }
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.tableRowsFound || this.tableCellsFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(table-row+|table-cell+)");
            }
         } else {
            TableEventProducer eventProducer;
            if (localName.equals("table-row")) {
               this.tableRowsFound = true;
               if (this.tableCellsFound) {
                  eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                  eventProducer.noMixRowsAndCells(this, this.getName(), this.getLocator());
               }
            } else if (localName.equals("table-cell")) {
               this.tableCellsFound = true;
               if (this.tableRowsFound) {
                  eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                  eventProducer.noMixRowsAndCells(this, this.getName(), this.getLocator());
               }
            } else {
               this.invalidChildError(loc, nsURI, localName);
            }
         }
      }

   }

   protected void addChildNode(FONode child) throws FOPException {
      if (!this.inMarker()) {
         switch (child.getNameId()) {
            case 75:
               if (!this.rowsStarted) {
                  this.getTable().getRowGroupBuilder().startTablePart(this);
               }

               this.rowsStarted = true;
               TableCell cell = (TableCell)child;
               this.addTableCellChild(cell, this.firstRow);
               this.lastCellEndsRow = cell.endsRow();
               if (this.lastCellEndsRow) {
                  this.firstRow = false;
                  this.columnNumberManager.prepareForNextRow(this.pendingSpans);
                  this.getTable().getRowGroupBuilder().endRow(this);
               }
               break;
            case 79:
               if (!this.rowsStarted) {
                  this.getTable().getRowGroupBuilder().startTablePart(this);
               } else {
                  this.columnNumberManager.prepareForNextRow(this.pendingSpans);
                  this.getTable().getRowGroupBuilder().endTableRow();
               }

               this.rowsStarted = true;
               this.getTable().getRowGroupBuilder().startTableRow((TableRow)child);
         }
      }

      super.addChildNode(child);
   }

   void addRowGroup(List rowGroup) {
      this.rowGroups.add(rowGroup);
   }

   public List getRowGroups() {
      return this.rowGroups;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public boolean isFirst(TableRow obj) {
      return this.firstChild == null || this.firstChild == obj;
   }

   void signalNewRow() {
      if (this.rowsStarted) {
         this.firstRow = false;
         if (!this.lastCellEndsRow) {
            this.columnNumberManager.prepareForNextRow(this.pendingSpans);
            this.getTable().getRowGroupBuilder().endRow(this);
         }
      }

   }
}
