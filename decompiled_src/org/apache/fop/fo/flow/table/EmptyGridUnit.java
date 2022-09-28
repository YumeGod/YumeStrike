package org.apache.fop.fo.flow.table;

public class EmptyGridUnit extends GridUnit {
   EmptyGridUnit(Table table, TableRow row, int colIndex) {
      super((Table)table, 0, 0);
      this.setRow(row);
   }

   protected void setBordersFromCell() {
      this.borderBefore = ConditionalBorder.getDefaultBorder(this.collapsingBorderModel);
      this.borderAfter = ConditionalBorder.getDefaultBorder(this.collapsingBorderModel);
      this.borderStart = BorderSpecification.getDefaultBorder();
      this.borderEnd = BorderSpecification.getDefaultBorder();
   }

   public PrimaryGridUnit getPrimary() {
      throw new UnsupportedOperationException();
   }

   public boolean isPrimary() {
      return false;
   }

   public boolean isLastGridUnitColSpan() {
      return true;
   }

   public boolean isLastGridUnitRowSpan() {
      return true;
   }
}
