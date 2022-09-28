package org.apache.fop.fo.flow.table;

import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.table.CollapsingBorderModel;

public class GridUnit {
   public static final int FIRST_IN_PART = 0;
   public static final int LAST_IN_PART = 1;
   public static final int KEEP_WITH_NEXT_PENDING = 2;
   public static final int KEEP_WITH_PREVIOUS_PENDING = 3;
   private PrimaryGridUnit primary;
   protected TableCell cell;
   private TableRow row;
   private int colSpanIndex;
   private int rowSpanIndex;
   private byte flags;
   ConditionalBorder borderBefore;
   ConditionalBorder borderAfter;
   BorderSpecification borderStart;
   BorderSpecification borderEnd;
   protected CollapsingBorderModel collapsingBorderModel;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   protected GridUnit(Table table, int colSpanIndex, int rowSpanIndex) {
      this(colSpanIndex, rowSpanIndex);
      this.setBorders(table);
   }

   protected GridUnit(TableCell cell, int colSpanIndex, int rowSpanIndex) {
      this(colSpanIndex, rowSpanIndex);
      this.cell = cell;
      this.setBorders(cell.getTable());
   }

   GridUnit(PrimaryGridUnit primary, int colSpanIndex, int rowSpanIndex) {
      this(primary.getCell(), colSpanIndex, rowSpanIndex);
      this.primary = primary;
   }

   private GridUnit(int colSpanIndex, int rowSpanIndex) {
      this.flags = 0;
      this.colSpanIndex = colSpanIndex;
      this.rowSpanIndex = rowSpanIndex;
   }

   private void setBorders(Table table) {
      if (!table.isSeparateBorderModel()) {
         this.collapsingBorderModel = CollapsingBorderModel.getBorderModelFor(table.getBorderCollapse());
         this.setBordersFromCell();
      }

   }

   protected void setBordersFromCell() {
      this.borderBefore = this.cell.borderBefore.copy();
      if (this.rowSpanIndex > 0) {
         this.borderBefore.normal = BorderSpecification.getDefaultBorder();
      }

      this.borderAfter = this.cell.borderAfter.copy();
      if (!this.isLastGridUnitRowSpan()) {
         this.borderAfter.normal = BorderSpecification.getDefaultBorder();
      }

      if (this.colSpanIndex == 0) {
         this.borderStart = this.cell.borderStart;
      } else {
         this.borderStart = BorderSpecification.getDefaultBorder();
      }

      if (this.isLastGridUnitColSpan()) {
         this.borderEnd = this.cell.borderEnd;
      } else {
         this.borderEnd = BorderSpecification.getDefaultBorder();
      }

   }

   public TableCell getCell() {
      return this.cell;
   }

   public TableRow getRow() {
      return this.row;
   }

   void setRow(TableRow row) {
      this.row = row;
   }

   public PrimaryGridUnit getPrimary() {
      return this.primary;
   }

   public boolean isPrimary() {
      return false;
   }

   public boolean isEmpty() {
      return this.cell == null;
   }

   public boolean isLastGridUnitColSpan() {
      return this.colSpanIndex == this.cell.getNumberColumnsSpanned() - 1;
   }

   public boolean isLastGridUnitRowSpan() {
      return this.rowSpanIndex == this.cell.getNumberRowsSpanned() - 1;
   }

   public int getRowSpanIndex() {
      return this.rowSpanIndex;
   }

   public int getColSpanIndex() {
      return this.colSpanIndex;
   }

   public CommonBorderPaddingBackground.BorderInfo getBorderBefore(int which) {
      switch (which) {
         case 0:
            return this.borderBefore.normal.getBorderInfo();
         case 1:
            return this.borderBefore.leadingTrailing.getBorderInfo();
         case 2:
            return this.borderBefore.rest.getBorderInfo();
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            } else {
               return null;
            }
      }
   }

   public CommonBorderPaddingBackground.BorderInfo getBorderAfter(int which) {
      switch (which) {
         case 0:
            return this.borderAfter.normal.getBorderInfo();
         case 1:
            return this.borderAfter.leadingTrailing.getBorderInfo();
         case 2:
            return this.borderAfter.rest.getBorderInfo();
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            } else {
               return null;
            }
      }
   }

   public CommonBorderPaddingBackground.BorderInfo getBorderStart() {
      return this.borderStart.getBorderInfo();
   }

   public CommonBorderPaddingBackground.BorderInfo getBorderEnd() {
      return this.borderEnd.getBorderInfo();
   }

   void resolveBorder(GridUnit other, int side) {
      BorderSpecification resolvedBorder;
      switch (side) {
         case 0:
            this.borderBefore.resolve(other.borderAfter, true, false, false);
            break;
         case 1:
            this.borderAfter.resolve(other.borderBefore, true, false, false);
            break;
         case 2:
            resolvedBorder = this.collapsingBorderModel.determineWinner(this.borderStart, other.borderEnd);
            if (resolvedBorder != null) {
               this.borderStart = resolvedBorder;
               other.borderEnd = resolvedBorder;
            }
            break;
         case 3:
            resolvedBorder = this.collapsingBorderModel.determineWinner(this.borderEnd, other.borderStart);
            if (resolvedBorder != null) {
               this.borderEnd = resolvedBorder;
               other.borderStart = resolvedBorder;
            }
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   void integrateBorderSegment(int side, TableFObj parent, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
      switch (side) {
         case 0:
            this.borderBefore.integrateSegment(parent.borderBefore, withNormal, withLeadingTrailing, withRest);
            break;
         case 1:
            this.borderAfter.integrateSegment(parent.borderAfter, withNormal, withLeadingTrailing, withRest);
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   void integrateBorderSegment(int side, TableFObj parent) {
      switch (side) {
         case 0:
         case 1:
            this.integrateBorderSegment(side, parent, true, true, true);
            break;
         case 2:
            this.borderStart = this.collapsingBorderModel.determineWinner(this.borderStart, parent.borderStart);
            break;
         case 3:
            this.borderEnd = this.collapsingBorderModel.determineWinner(this.borderEnd, parent.borderEnd);
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   void integrateBorderSegment(int side, BorderSpecification segment) {
      switch (side) {
         case 2:
            this.borderStart = this.collapsingBorderModel.determineWinner(this.borderStart, segment);
            break;
         case 3:
            this.borderEnd = this.collapsingBorderModel.determineWinner(this.borderEnd, segment);
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   void integrateCompetingBorder(int side, ConditionalBorder competitor, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
      switch (side) {
         case 0:
            this.borderBefore.integrateCompetingSegment(competitor, withNormal, withLeadingTrailing, withRest);
            break;
         case 1:
            this.borderAfter.integrateCompetingSegment(competitor, withNormal, withLeadingTrailing, withRest);
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   public boolean getFlag(int which) {
      return (this.flags & 1 << which) != 0;
   }

   public void setFlag(int which, boolean value) {
      if (value) {
         this.flags = (byte)(this.flags | 1 << which);
      } else {
         this.flags = (byte)(this.flags & ~(1 << which));
      }

   }

   public void setFlag(int which) {
      this.setFlag(which, true);
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      if (this.isEmpty()) {
         buffer.append("EMPTY");
      } else if (this.isPrimary()) {
         buffer.append("Primary");
      }

      buffer.append("GridUnit:");
      if (this.colSpanIndex > 0) {
         buffer.append(" colSpan=").append(this.colSpanIndex);
         if (this.isLastGridUnitColSpan()) {
            buffer.append("(last)");
         }
      }

      if (this.rowSpanIndex > 0) {
         buffer.append(" rowSpan=").append(this.rowSpanIndex);
         if (this.isLastGridUnitRowSpan()) {
            buffer.append("(last)");
         }
      }

      if (!this.isPrimary() && this.getPrimary() != null) {
         buffer.append(" primary=").append(this.getPrimary().getRowIndex());
         buffer.append("/").append(this.getPrimary().getColIndex());
         if (this.getPrimary().getCell() != null) {
            buffer.append(" id=" + this.getPrimary().getCell().getId());
         }
      }

      buffer.append(" flags=").append(Integer.toBinaryString(this.flags));
      return buffer.toString();
   }

   static {
      $assertionsDisabled = !GridUnit.class.desiredAssertionStatus();
   }
}
