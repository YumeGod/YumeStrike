package org.apache.fop.layoutmgr.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.GridUnit;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.apache.fop.layoutmgr.ElementListObserver;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.BreakUtil;

class RowGroupLayoutManager {
   private static Log log;
   private static final MinOptMax MAX_STRETCH;
   private EffRow[] rowGroup;
   private TableLayoutManager tableLM;
   private TableStepper tableStepper;

   RowGroupLayoutManager(TableLayoutManager tableLM, EffRow[] rowGroup, TableStepper tableStepper) {
      this.tableLM = tableLM;
      this.rowGroup = rowGroup;
      this.tableStepper = tableStepper;
   }

   public LinkedList getNextKnuthElements(LayoutContext context, int alignment, int bodyType) {
      LinkedList returnList = new LinkedList();
      this.createElementsForRowGroup(context, alignment, bodyType, returnList);
      context.updateKeepWithPreviousPending(this.rowGroup[0].getKeepWithPrevious());
      context.updateKeepWithNextPending(this.rowGroup[this.rowGroup.length - 1].getKeepWithNext());
      int breakBefore = 9;
      TableRow firstRow = this.rowGroup[0].getTableRow();
      if (firstRow != null) {
         breakBefore = firstRow.getBreakBefore();
      }

      context.setBreakBefore(BreakUtil.compareBreakClasses(breakBefore, this.rowGroup[0].getBreakBefore()));
      int breakAfter = 9;
      TableRow lastRow = this.rowGroup[this.rowGroup.length - 1].getTableRow();
      if (lastRow != null) {
         breakAfter = lastRow.getBreakAfter();
      }

      context.setBreakAfter(BreakUtil.compareBreakClasses(breakAfter, this.rowGroup[this.rowGroup.length - 1].getBreakAfter()));
      return returnList;
   }

   private void createElementsForRowGroup(LayoutContext context, int alignment, int bodyType, LinkedList returnList) {
      log.debug("Handling row group with " + this.rowGroup.length + " rows...");

      label34:
      for(int rgi = 0; rgi < this.rowGroup.length; ++rgi) {
         EffRow row = this.rowGroup[rgi];
         Iterator iter = row.getGridUnits().iterator();

         while(true) {
            GridUnit gu;
            do {
               if (!iter.hasNext()) {
                  continue label34;
               }

               gu = (GridUnit)iter.next();
            } while(!gu.isPrimary());

            PrimaryGridUnit primary = gu.getPrimary();
            primary.createCellLM();
            primary.getCellLM().setParent(this.tableLM);
            int spanWidth = 0;
            Iterator colIter = this.tableLM.getTable().getColumns().listIterator(primary.getColIndex());
            int i = 0;

            for(int c = primary.getCell().getNumberColumnsSpanned(); i < c; ++i) {
               spanWidth += ((TableColumn)colIter.next()).getColumnWidth().getValue(this.tableLM);
            }

            LayoutContext childLC = new LayoutContext(0);
            childLC.setStackLimitBP(context.getStackLimitBP());
            childLC.setRefIPD(spanWidth);
            List elems = primary.getCellLM().getNextKnuthElements(childLC, alignment);
            ElementListObserver.observe(elems, "table-cell", primary.getCell().getId());
            primary.setElements(elems);
         }
      }

      this.computeRowHeights();
      List elements = this.tableStepper.getCombinedKnuthElementsForRowGroup(context, this.rowGroup, bodyType);
      returnList.addAll(elements);
   }

   private void computeRowHeights() {
      log.debug("rowGroup:");
      MinOptMax[] rowHeights = new MinOptMax[this.rowGroup.length];

      label63:
      for(int rgi = 0; rgi < this.rowGroup.length; ++rgi) {
         EffRow row = this.rowGroup[rgi];
         TableRow tableRowFO = this.rowGroup[rgi].getTableRow();
         MinOptMax explicitRowHeight;
         if (tableRowFO == null) {
            rowHeights[rgi] = MAX_STRETCH;
            explicitRowHeight = MAX_STRETCH;
         } else {
            LengthRangeProperty rowBPD = tableRowFO.getBlockProgressionDimension();
            rowHeights[rgi] = rowBPD.toMinOptMax(this.tableLM);
            explicitRowHeight = rowBPD.toMinOptMax(this.tableLM);
         }

         Iterator iter = row.getGridUnits().iterator();

         while(true) {
            GridUnit gu;
            do {
               do {
                  do {
                     if (!iter.hasNext()) {
                        row.setHeight(rowHeights[rgi]);
                        row.setExplicitHeight(explicitRowHeight);
                        continue label63;
                     }

                     gu = (GridUnit)iter.next();
                  } while(gu.isEmpty());
               } while(gu.getColSpanIndex() != 0);
            } while(!gu.isLastGridUnitRowSpan());

            PrimaryGridUnit primary = gu.getPrimary();
            int effectiveCellBPD = 0;
            LengthRangeProperty cellBPD = primary.getCell().getBlockProgressionDimension();
            if (!cellBPD.getMinimum(this.tableLM).isAuto()) {
               effectiveCellBPD = cellBPD.getMinimum(this.tableLM).getLength().getValue(this.tableLM);
            }

            if (!cellBPD.getOptimum(this.tableLM).isAuto()) {
               effectiveCellBPD = cellBPD.getOptimum(this.tableLM).getLength().getValue(this.tableLM);
            }

            if (gu.getRowSpanIndex() == 0) {
               effectiveCellBPD = Math.max(effectiveCellBPD, explicitRowHeight.getOpt());
            }

            effectiveCellBPD = Math.max(effectiveCellBPD, primary.getContentLength());
            int borderWidths = primary.getBeforeAfterBorderWidth();
            int padding = 0;
            CommonBorderPaddingBackground cbpb = primary.getCell().getCommonBorderPaddingBackground();
            padding += cbpb.getPaddingBefore(false, primary.getCellLM());
            padding += cbpb.getPaddingAfter(false, primary.getCellLM());
            int effRowHeight = effectiveCellBPD + padding + borderWidths;

            for(int prev = rgi - 1; prev >= rgi - gu.getRowSpanIndex(); --prev) {
               effRowHeight -= rowHeights[prev].getOpt();
            }

            if (effRowHeight > rowHeights[rgi].getMin()) {
               rowHeights[rgi] = rowHeights[rgi].extendMinimum(effRowHeight);
            }
         }
      }

   }

   static {
      log = LogFactory.getLog(RowGroupLayoutManager.class);
      MAX_STRETCH = MinOptMax.getInstance(0, 0, Integer.MAX_VALUE);
   }
}
