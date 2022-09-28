package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.layoutmgr.table.CollapsingBorderModel;

class CollapsingBorderResolver implements BorderResolver {
   private Table table;
   private CollapsingBorderModel collapsingBorderModel;
   private List previousRow;
   private boolean firstInTable;
   private List footerFirstRow;
   private List footerLastRow;
   private Resolver delegate;
   private Resolver resolverInBody = new ResolverInBody();
   private Resolver resolverInFooter;
   private List leadingBorders;
   private List trailingBorders;
   private List headerLastRow = null;

   CollapsingBorderResolver(Table table) {
      this.table = table;
      this.collapsingBorderModel = CollapsingBorderModel.getBorderModelFor(table.getBorderCollapse());
      this.firstInTable = true;
      int index = 0;

      do {
         TableColumn col = table.getColumn(index);
         col.borderBefore.integrateSegment(table.borderBefore, true, false, true);
         col.borderBefore.leadingTrailing = col.borderBefore.rest;
         col.borderAfter.integrateSegment(table.borderAfter, true, false, true);
         col.borderAfter.leadingTrailing = col.borderAfter.rest;
         index += col.getNumberColumnsRepeated();
      } while(index < table.getNumberOfColumns());

   }

   public void endRow(List row, TableCellContainer container) {
      this.delegate.endRow(row, container);
   }

   public void startPart(TablePart part) {
      if (part instanceof TableHeader) {
         this.delegate = new ResolverInHeader();
      } else {
         Iterator colIter;
         ConditionalBorder border;
         if (this.leadingBorders == null || this.table.omitHeaderAtBreak()) {
            this.leadingBorders = new ArrayList(this.table.getNumberOfColumns());
            colIter = this.table.getColumns().iterator();

            while(colIter.hasNext()) {
               border = ((TableColumn)colIter.next()).borderBefore;
               this.leadingBorders.add(border);
            }
         }

         if (part instanceof TableFooter) {
            this.resolverInFooter = new ResolverInFooter();
            this.delegate = this.resolverInFooter;
         } else {
            if (this.trailingBorders == null || this.table.omitFooterAtBreak()) {
               this.trailingBorders = new ArrayList(this.table.getNumberOfColumns());
               colIter = this.table.getColumns().iterator();

               while(colIter.hasNext()) {
                  border = ((TableColumn)colIter.next()).borderAfter;
                  this.trailingBorders.add(border);
               }
            }

            this.delegate = this.resolverInBody;
         }
      }

      this.delegate.startPart(part);
   }

   public void endPart() {
      this.delegate.endPart();
   }

   public void endTable() {
      this.delegate.endTable();
      this.delegate = null;
      Iterator iter;
      GridUnit gu;
      if (this.headerLastRow != null) {
         for(iter = this.headerLastRow.iterator(); iter.hasNext(); gu.borderAfter.leadingTrailing = gu.borderAfter.normal) {
            gu = (GridUnit)iter.next();
         }
      }

      if (this.footerLastRow != null) {
         for(iter = this.footerLastRow.iterator(); iter.hasNext(); gu.borderAfter.leadingTrailing = gu.borderAfter.normal) {
            gu = (GridUnit)iter.next();
         }
      }

   }

   private class ResolverInBody extends Resolver {
      private boolean firstInBody;

      private ResolverInBody() {
         super(null);
         this.firstInBody = true;
      }

      void endRow(List row, TableCellContainer container) {
         super.endRow(row, container);
         if (CollapsingBorderResolver.this.firstInTable) {
            this.resolveBordersFirstRowInTable(row, true, true, true);
         } else {
            this.resolveBordersBetweenRows(CollapsingBorderResolver.this.previousRow, row);
            this.integrateLeadingBorders(row);
         }

         this.integrateTrailingBorders(row);
         CollapsingBorderResolver.this.previousRow = row;
         if (this.firstInBody) {
            this.firstInBody = false;

            GridUnit gu;
            for(Iterator iter = row.iterator(); iter.hasNext(); gu.borderBefore.leadingTrailing = gu.borderBefore.normal) {
               gu = (GridUnit)iter.next();
            }
         }

      }

      void endTable() {
         if (CollapsingBorderResolver.this.resolverInFooter != null) {
            CollapsingBorderResolver.this.resolverInFooter.endTable();
         } else {
            this.resolveBordersLastRowInTable(CollapsingBorderResolver.this.previousRow, true, false, false);
         }

         GridUnit gu;
         for(Iterator iter = CollapsingBorderResolver.this.previousRow.iterator(); iter.hasNext(); gu.borderAfter.leadingTrailing = gu.borderAfter.normal) {
            gu = (GridUnit)iter.next();
         }

      }

      // $FF: synthetic method
      ResolverInBody(Object x1) {
         this();
      }
   }

   private class ResolverInFooter extends Resolver {
      private ResolverInFooter() {
         super(null);
      }

      void endRow(List row, TableCellContainer container) {
         super.endRow(row, container);
         if (CollapsingBorderResolver.this.footerFirstRow == null) {
            CollapsingBorderResolver.this.footerFirstRow = row;
         } else {
            this.resolveBordersBetweenRows(CollapsingBorderResolver.this.footerLastRow, row);
         }

         CollapsingBorderResolver.this.footerLastRow = row;
      }

      void endPart() {
         this.resolveBordersLastRowInPart(CollapsingBorderResolver.this.footerLastRow, true, true, true);
         CollapsingBorderResolver.this.trailingBorders = new ArrayList(CollapsingBorderResolver.this.table.getNumberOfColumns());
         Iterator guIter = CollapsingBorderResolver.this.footerFirstRow.iterator();

         while(guIter.hasNext()) {
            ConditionalBorder borderBefore = ((GridUnit)guIter.next()).borderBefore;
            borderBefore.leadingTrailing = borderBefore.normal;
            borderBefore.rest = borderBefore.normal;
            CollapsingBorderResolver.this.trailingBorders.add(borderBefore);
         }

      }

      void endTable() {
         this.resolveBordersBetweenRows(CollapsingBorderResolver.this.previousRow, CollapsingBorderResolver.this.footerFirstRow);

         ConditionalBorder borderAfter;
         for(Iterator guIter = CollapsingBorderResolver.this.footerLastRow.iterator(); guIter.hasNext(); borderAfter.rest = borderAfter.normal) {
            borderAfter = ((GridUnit)guIter.next()).borderAfter;
            borderAfter.leadingTrailing = borderAfter.normal;
         }

         this.resolveBordersLastRowInTable(CollapsingBorderResolver.this.footerLastRow, true, false, true);
      }

      // $FF: synthetic method
      ResolverInFooter(Object x1) {
         this();
      }
   }

   private class ResolverInHeader extends Resolver {
      private ResolverInHeader() {
         super(null);
      }

      void endRow(List row, TableCellContainer container) {
         super.endRow(row, container);
         if (CollapsingBorderResolver.this.previousRow != null) {
            this.resolveBordersBetweenRows(CollapsingBorderResolver.this.previousRow, row);
         } else {
            ConditionalBorder borderBefore;
            for(Iterator guIter = row.iterator(); guIter.hasNext(); borderBefore.rest = borderBefore.normal) {
               borderBefore = ((GridUnit)guIter.next()).borderBefore;
               borderBefore.leadingTrailing = borderBefore.normal;
            }

            this.resolveBordersFirstRowInTable(row, true, false, true);
         }

         CollapsingBorderResolver.this.previousRow = row;
      }

      void endPart() {
         super.endPart();
         CollapsingBorderResolver.this.leadingBorders = new ArrayList(CollapsingBorderResolver.this.table.getNumberOfColumns());
         Iterator guIter = CollapsingBorderResolver.this.previousRow.iterator();

         while(guIter.hasNext()) {
            ConditionalBorder borderAfter = ((GridUnit)guIter.next()).borderAfter;
            borderAfter.leadingTrailing = borderAfter.normal;
            borderAfter.rest = borderAfter.normal;
            CollapsingBorderResolver.this.leadingBorders.add(borderAfter);
         }

         CollapsingBorderResolver.this.headerLastRow = CollapsingBorderResolver.this.previousRow;
      }

      void endTable() {
         throw new IllegalStateException();
      }

      // $FF: synthetic method
      ResolverInHeader(Object x1) {
         this();
      }
   }

   private abstract class Resolver {
      protected TablePart tablePart;
      protected boolean firstInPart;
      private BorderSpecification borderStartTableAndBody;
      private BorderSpecification borderEndTableAndBody;

      private Resolver() {
      }

      void resolveBordersFirstRowInTable(List row, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
         assert CollapsingBorderResolver.this.firstInTable;

         for(int i = 0; i < row.size(); ++i) {
            TableColumn column = CollapsingBorderResolver.this.table.getColumn(i);
            ((GridUnit)row.get(i)).integrateBorderSegment(0, column, withNormal, withLeadingTrailing, withRest);
         }

         CollapsingBorderResolver.this.firstInTable = false;
      }

      void resolveBordersBetweenRows(List rowBefore, List rowAfter) {
         assert rowBefore != null && rowAfter != null;

         for(int i = 0; i < rowAfter.size(); ++i) {
            GridUnit gu = (GridUnit)rowAfter.get(i);
            if (gu.getRowSpanIndex() == 0) {
               GridUnit beforeGU = (GridUnit)rowBefore.get(i);
               gu.resolveBorder(beforeGU, 0);
            }
         }

      }

      void resolveBordersLastRowInPart(List row, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
         for(int i = 0; i < row.size(); ++i) {
            ((GridUnit)row.get(i)).integrateBorderSegment(1, this.tablePart, withNormal, withLeadingTrailing, withRest);
         }

      }

      void resolveBordersLastRowInTable(List row, boolean withNormal, boolean withLeadingTrailing, boolean withRest) {
         for(int i = 0; i < row.size(); ++i) {
            TableColumn column = CollapsingBorderResolver.this.table.getColumn(i);
            ((GridUnit)row.get(i)).integrateBorderSegment(1, column, withNormal, withLeadingTrailing, withRest);
         }

      }

      void integrateLeadingBorders(List row) {
         for(int i = 0; i < CollapsingBorderResolver.this.table.getNumberOfColumns(); ++i) {
            GridUnit gu = (GridUnit)row.get(i);
            ConditionalBorder border = (ConditionalBorder)CollapsingBorderResolver.this.leadingBorders.get(i);
            gu.integrateCompetingBorder(0, border, false, true, true);
         }

      }

      void integrateTrailingBorders(List row) {
         for(int i = 0; i < CollapsingBorderResolver.this.table.getNumberOfColumns(); ++i) {
            GridUnit gu = (GridUnit)row.get(i);
            ConditionalBorder border = (ConditionalBorder)CollapsingBorderResolver.this.trailingBorders.get(i);
            gu.integrateCompetingBorder(1, border, false, true, true);
         }

      }

      void startPart(TablePart part) {
         this.tablePart = part;
         this.firstInPart = true;
         this.borderStartTableAndBody = CollapsingBorderResolver.this.collapsingBorderModel.determineWinner(CollapsingBorderResolver.this.table.borderStart, this.tablePart.borderStart);
         this.borderEndTableAndBody = CollapsingBorderResolver.this.collapsingBorderModel.determineWinner(CollapsingBorderResolver.this.table.borderEnd, this.tablePart.borderEnd);
      }

      void endRow(List row, TableCellContainer container) {
         BorderSpecification borderStart = this.borderStartTableAndBody;
         BorderSpecification borderEnd = this.borderEndTableAndBody;
         if (container instanceof TableRow) {
            TableRow tableRow = (TableRow)container;
            Iterator iter = row.iterator();

            while(iter.hasNext()) {
               GridUnit gux = (GridUnit)iter.next();
               boolean first = gux.getRowSpanIndex() == 0;
               boolean last = gux.isLastGridUnitRowSpan();
               gux.integrateBorderSegment(0, tableRow, first, first, true);
               gux.integrateBorderSegment(1, tableRow, last, last, true);
            }

            borderStart = CollapsingBorderResolver.this.collapsingBorderModel.determineWinner(borderStart, tableRow.borderStart);
            borderEnd = CollapsingBorderResolver.this.collapsingBorderModel.determineWinner(borderEnd, tableRow.borderEnd);
         }

         if (this.firstInPart) {
            for(int i = 0; i < row.size(); ++i) {
               ((GridUnit)row.get(i)).integrateBorderSegment(0, this.tablePart, true, true, true);
            }

            this.firstInPart = false;
         }

         Iterator guIter = row.iterator();
         GridUnit gu = (GridUnit)guIter.next();
         Iterator colIter = CollapsingBorderResolver.this.table.getColumns().iterator();
         TableColumn col = (TableColumn)colIter.next();
         gu.integrateBorderSegment(2, (TableFObj)col);
         gu.integrateBorderSegment(2, (BorderSpecification)borderStart);

         while(guIter.hasNext()) {
            GridUnit nextGU = (GridUnit)guIter.next();
            TableColumn nextCol = (TableColumn)colIter.next();
            if (gu.isLastGridUnitColSpan()) {
               gu.integrateBorderSegment(3, (TableFObj)col);
               nextGU.integrateBorderSegment(2, (TableFObj)nextCol);
               gu.resolveBorder(nextGU, 3);
            }

            gu = nextGU;
            col = nextCol;
         }

         gu.integrateBorderSegment(3, (TableFObj)col);
         gu.integrateBorderSegment(3, (BorderSpecification)borderEnd);
      }

      void endPart() {
         this.resolveBordersLastRowInPart(CollapsingBorderResolver.this.previousRow, true, true, true);
      }

      abstract void endTable();

      // $FF: synthetic method
      Resolver(Object x1) {
         this();
      }
   }
}
