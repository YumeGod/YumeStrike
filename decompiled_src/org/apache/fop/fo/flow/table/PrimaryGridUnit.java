package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.table.TableCellLayoutManager;

public class PrimaryGridUnit extends GridUnit {
   private TableCellLayoutManager cellLM;
   private List elements;
   private int rowIndex;
   private int colIndex;
   private List rows;
   private int contentLength = -1;
   private boolean isSeparateBorderModel;
   private int halfBorderSeparationBPD;
   private Keep keepWithPrevious;
   private Keep keepWithNext;
   private int breakBefore;
   private int breakAfter;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   PrimaryGridUnit(TableCell cell, int colIndex) {
      super((TableCell)cell, 0, 0);
      this.keepWithPrevious = Keep.KEEP_AUTO;
      this.keepWithNext = Keep.KEEP_AUTO;
      this.breakBefore = 9;
      this.breakAfter = 9;
      this.colIndex = colIndex;
      this.isSeparateBorderModel = cell.getTable().isSeparateBorderModel();
      this.halfBorderSeparationBPD = cell.getTable().getBorderSeparation().getBPD().getLength().getValue() / 2;
   }

   public TablePart getTablePart() {
      FONode node = this.cell.getParent();
      if (node instanceof TableRow) {
         node = node.getParent();
      }

      return (TablePart)node;
   }

   public TableCellLayoutManager getCellLM() {
      if (!$assertionsDisabled && this.cellLM == null) {
         throw new AssertionError();
      } else {
         return this.cellLM;
      }
   }

   public PrimaryGridUnit getPrimary() {
      return this;
   }

   public boolean isPrimary() {
      return true;
   }

   public void setElements(List elements) {
      this.elements = elements;
   }

   public List getElements() {
      return this.elements;
   }

   public int getBeforeAfterBorderWidth() {
      return this.getBeforeBorderWidth(0, 0) + this.getAfterBorderWidth(0);
   }

   public int getBeforeBorderWidth(int rowIndex, int which) {
      if (this.isSeparateBorderModel) {
         if (this.getCell() == null) {
            return 0;
         } else {
            CommonBorderPaddingBackground cellBorders = this.getCell().getCommonBorderPaddingBackground();
            switch (which) {
               case 0:
               case 1:
                  return cellBorders.getBorderBeforeWidth(false) + this.halfBorderSeparationBPD;
               case 2:
                  if (cellBorders.getBorderInfo(0).getWidth().isDiscard()) {
                     return 0;
                  }

                  return cellBorders.getBorderBeforeWidth(true) + this.halfBorderSeparationBPD;
               default:
                  if (!$assertionsDisabled) {
                     throw new AssertionError();
                  } else {
                     return 0;
                  }
            }
         }
      } else {
         int width = 0;
         GridUnit[] row = (GridUnit[])this.rows.get(rowIndex);

         for(int i = 0; i < row.length; ++i) {
            width = Math.max(width, row[i].getBorderBefore(which).getRetainedWidth());
         }

         return width / 2;
      }
   }

   public int getAfterBorderWidth(int rowIndex, int which) {
      if (this.isSeparateBorderModel) {
         if (this.getCell() == null) {
            return 0;
         } else {
            CommonBorderPaddingBackground cellBorders = this.getCell().getCommonBorderPaddingBackground();
            switch (which) {
               case 0:
               case 1:
                  return cellBorders.getBorderAfterWidth(false) + this.halfBorderSeparationBPD;
               case 2:
                  if (cellBorders.getBorderInfo(1).getWidth().isDiscard()) {
                     return 0;
                  }

                  return cellBorders.getBorderAfterWidth(true) + this.halfBorderSeparationBPD;
               default:
                  if (!$assertionsDisabled) {
                     throw new AssertionError();
                  } else {
                     return 0;
                  }
            }
         }
      } else {
         int width = 0;
         GridUnit[] row = (GridUnit[])this.rows.get(rowIndex);

         for(int i = 0; i < row.length; ++i) {
            width = Math.max(width, row[i].getBorderAfter(which).getRetainedWidth());
         }

         return width / 2;
      }
   }

   public int getAfterBorderWidth(int which) {
      return this.getAfterBorderWidth(this.getCell().getNumberRowsSpanned() - 1, which);
   }

   public int getContentLength() {
      if (this.contentLength < 0) {
         this.contentLength = ElementListUtils.calcContentLength(this.elements);
      }

      return this.contentLength;
   }

   public List getRows() {
      return this.rows;
   }

   public void addRow(GridUnit[] row) {
      if (this.rows == null) {
         this.rows = new ArrayList();
      }

      this.rows.add(row);
   }

   void setRowIndex(int rowIndex) {
      this.rowIndex = rowIndex;
   }

   public int getRowIndex() {
      return this.rowIndex;
   }

   public int getColIndex() {
      return this.colIndex;
   }

   public int[] getStartEndBorderWidths() {
      int[] widths = new int[2];
      if (this.getCell() == null) {
         return widths;
      } else {
         if (this.getCell().getTable().isSeparateBorderModel()) {
            widths[0] = this.getCell().getCommonBorderPaddingBackground().getBorderStartWidth(false);
            widths[1] = this.getCell().getCommonBorderPaddingBackground().getBorderEndWidth(false);
         } else {
            for(int i = 0; i < this.rows.size(); ++i) {
               GridUnit[] gridUnits = (GridUnit[])this.rows.get(i);
               widths[0] = Math.max(widths[0], gridUnits[0].borderStart.getBorderInfo().getRetainedWidth());
               widths[1] = Math.max(widths[1], gridUnits[gridUnits.length - 1].borderEnd.getBorderInfo().getRetainedWidth());
            }
         }

         return widths;
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(super.toString());
      sb.append(" rowIndex=").append(this.rowIndex);
      sb.append(" colIndex=").append(this.colIndex);
      return sb.toString();
   }

   public boolean hasSpanning() {
      return this.getCell().getNumberColumnsSpanned() > 1 || this.getCell().getNumberRowsSpanned() > 1;
   }

   public void createCellLM() {
      this.cellLM = new TableCellLayoutManager(this.cell, this);
   }

   public Keep getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public void setKeepWithPrevious(Keep keep) {
      this.keepWithPrevious = keep;
   }

   public Keep getKeepWithNext() {
      return this.keepWithNext;
   }

   public void setKeepWithNext(Keep keep) {
      this.keepWithNext = keep;
   }

   public int getBreakBefore() {
      return this.breakBefore;
   }

   public void setBreakBefore(int breakBefore) {
      this.breakBefore = breakBefore;
   }

   public int getBreakAfter() {
      return this.breakAfter;
   }

   public void setBreakAfter(int breakAfter) {
      this.breakAfter = breakAfter;
   }

   static {
      $assertionsDisabled = !PrimaryGridUnit.class.desiredAssertionStatus();
   }
}
