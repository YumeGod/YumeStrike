package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Block;
import org.apache.fop.area.Trait;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.EmptyGridUnit;
import org.apache.fop.fo.flow.table.GridUnit;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthPossPosIter;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.layoutmgr.TraitSetter;

class RowPainter {
   private static Log log;
   private int colCount;
   private int currentRowOffset = 0;
   private EffRow currentRow = null;
   private LayoutContext layoutContext;
   private int firstRowIndex;
   private int firstRowOnPageIndex;
   private List rowOffsets = new ArrayList();
   private int[] cellHeights;
   private boolean[] firstCellOnPage;
   private CellPart[] firstCellParts;
   private CellPart[] lastCellParts;
   private int tablePartOffset = 0;
   private CommonBorderPaddingBackground tablePartBackground;
   private List tablePartBackgroundAreas;
   private TableContentLayoutManager tclm;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   RowPainter(TableContentLayoutManager tclm, LayoutContext layoutContext) {
      this.tclm = tclm;
      this.layoutContext = layoutContext;
      this.colCount = tclm.getColumns().getColumnCount();
      this.cellHeights = new int[this.colCount];
      this.firstCellOnPage = new boolean[this.colCount];
      this.firstCellParts = new CellPart[this.colCount];
      this.lastCellParts = new CellPart[this.colCount];
      this.firstRowIndex = -1;
      this.firstRowOnPageIndex = -1;
   }

   void startTablePart(TablePart tablePart) {
      CommonBorderPaddingBackground background = tablePart.getCommonBorderPaddingBackground();
      if (background.hasBackground()) {
         this.tablePartBackground = background;
         if (this.tablePartBackgroundAreas == null) {
            this.tablePartBackgroundAreas = new ArrayList();
         }
      }

      this.tablePartOffset = this.currentRowOffset;
   }

   void endTablePart(boolean lastInBody, boolean lastOnPage) {
      this.addAreasAndFlushRow(lastInBody, lastOnPage);
      if (this.tablePartBackground != null) {
         TableLayoutManager tableLM = this.tclm.getTableLM();
         Iterator iter = this.tablePartBackgroundAreas.iterator();

         while(iter.hasNext()) {
            Block backgroundArea = (Block)iter.next();
            TraitSetter.addBackground(backgroundArea, this.tablePartBackground, tableLM, -backgroundArea.getXOffset(), this.tablePartOffset - backgroundArea.getYOffset(), tableLM.getContentAreaIPD(), this.currentRowOffset - this.tablePartOffset);
         }

         this.tablePartBackground = null;
         this.tablePartBackgroundAreas.clear();
      }

   }

   int getAccumulatedBPD() {
      return this.currentRowOffset;
   }

   void handleTableContentPosition(TableContentPosition tcpos) {
      if (log.isDebugEnabled()) {
         log.debug("===handleTableContentPosition(" + tcpos);
      }

      if (this.currentRow == null) {
         this.currentRow = tcpos.getNewPageRow();
      } else {
         EffRow row = tcpos.getRow();
         if (row.getIndex() > this.currentRow.getIndex()) {
            this.addAreasAndFlushRow(false, false);
            this.currentRow = row;
         }
      }

      if (this.firstRowIndex < 0) {
         this.firstRowIndex = this.currentRow.getIndex();
         if (this.firstRowOnPageIndex < 0) {
            this.firstRowOnPageIndex = this.firstRowIndex;
         }
      }

      CellPart cellPart;
      int colIndex;
      for(Iterator partIter = tcpos.cellParts.iterator(); partIter.hasNext(); this.lastCellParts[colIndex] = cellPart) {
         cellPart = (CellPart)partIter.next();
         if (log.isDebugEnabled()) {
            log.debug(">" + cellPart);
         }

         colIndex = cellPart.pgu.getColIndex();
         int[] var10000;
         if (this.firstCellParts[colIndex] == null) {
            this.firstCellParts[colIndex] = cellPart;
            this.cellHeights[colIndex] = cellPart.getBorderPaddingBefore(this.firstCellOnPage[colIndex]);
         } else {
            if (!$assertionsDisabled && this.firstCellParts[colIndex].pgu != cellPart.pgu) {
               throw new AssertionError();
            }

            var10000 = this.cellHeights;
            var10000[colIndex] += cellPart.getConditionalBeforeContentLength();
         }

         var10000 = this.cellHeights;
         var10000[colIndex] += cellPart.getLength();
      }

   }

   private void addAreasAndFlushRow(boolean lastInPart, boolean lastOnPage) {
      if (log.isDebugEnabled()) {
         log.debug("Remembering yoffset for row " + this.currentRow.getIndex() + ": " + this.currentRowOffset);
      }

      this.recordRowOffset(this.currentRow.getIndex(), this.currentRowOffset);
      boolean firstCellPart = true;
      boolean lastCellPart = true;
      int actualRowHeight = 0;

      int i;
      GridUnit currentGU;
      for(i = 0; i < this.colCount; ++i) {
         currentGU = this.currentRow.getGridUnit(i);
         if (!currentGU.isEmpty()) {
            if (currentGU.getColSpanIndex() == 0 && (lastInPart || currentGU.isLastGridUnitRowSpan()) && this.firstCellParts[i] != null) {
               int cellHeight = this.cellHeights[i];
               cellHeight += this.lastCellParts[i].getConditionalAfterContentLength();
               cellHeight += this.lastCellParts[i].getBorderPaddingAfter(lastInPart);
               int cellOffset = this.getRowOffset(Math.max(this.firstCellParts[i].pgu.getRowIndex(), this.firstRowIndex));
               actualRowHeight = Math.max(actualRowHeight, cellOffset + cellHeight - this.currentRowOffset);
            }

            if (this.firstCellParts[i] != null && !this.firstCellParts[i].isFirstPart()) {
               firstCellPart = false;
            }

            if (this.lastCellParts[i] != null && !this.lastCellParts[i].isLastPart()) {
               lastCellPart = false;
            }
         }
      }

      for(i = 0; i < this.colCount; ++i) {
         currentGU = this.currentRow.getGridUnit(i);
         byte borderBeforeWhich;
         byte borderAfterWhich;
         if (currentGU.isEmpty() && !this.tclm.isSeparateBorderModel()) {
            if (firstCellPart) {
               if (this.firstCellOnPage[i]) {
                  borderBeforeWhich = 1;
               } else {
                  borderBeforeWhich = 0;
               }
            } else {
               borderBeforeWhich = 2;
            }

            if (lastCellPart) {
               if (lastInPart) {
                  borderAfterWhich = 1;
               } else {
                  borderAfterWhich = 0;
               }
            } else {
               borderAfterWhich = 2;
            }

            this.addAreaForEmptyGridUnit((EmptyGridUnit)currentGU, this.currentRow.getIndex(), i, actualRowHeight, borderBeforeWhich, borderAfterWhich, lastOnPage);
            this.firstCellOnPage[i] = false;
         } else if (currentGU.getColSpanIndex() == 0 && (lastInPart || currentGU.isLastGridUnitRowSpan()) && this.firstCellParts[i] != null) {
            if (!$assertionsDisabled && this.firstCellParts[i].pgu != currentGU.getPrimary()) {
               throw new AssertionError();
            }

            if (this.firstCellParts[i].isFirstPart()) {
               if (this.firstCellOnPage[i]) {
                  borderBeforeWhich = 1;
               } else {
                  borderBeforeWhich = 0;
               }
            } else {
               if (!$assertionsDisabled && !this.firstCellOnPage[i]) {
                  throw new AssertionError();
               }

               borderBeforeWhich = 2;
            }

            if (this.lastCellParts[i].isLastPart()) {
               if (lastInPart) {
                  borderAfterWhich = 1;
               } else {
                  borderAfterWhich = 0;
               }
            } else {
               borderAfterWhich = 2;
            }

            this.addAreasForCell(this.firstCellParts[i].pgu, this.firstCellParts[i].start, this.lastCellParts[i].end, actualRowHeight, borderBeforeWhich, borderAfterWhich, lastOnPage);
            this.firstCellParts[i] = null;
            Arrays.fill(this.firstCellOnPage, i, i + currentGU.getCell().getNumberColumnsSpanned(), false);
         }
      }

      this.currentRowOffset += actualRowHeight;
      if (lastInPart) {
         this.currentRow = null;
         this.firstRowIndex = -1;
         this.rowOffsets.clear();
         this.firstRowOnPageIndex = Integer.MAX_VALUE;
      }

   }

   private int computeContentLength(PrimaryGridUnit pgu, int startIndex, int endIndex) {
      if (startIndex > endIndex) {
         return 0;
      } else {
         ListIterator iter = pgu.getElements().listIterator(startIndex);

         for(boolean nextIsBox = false; iter.nextIndex() <= endIndex && !nextIsBox; nextIsBox = ((KnuthElement)iter.next()).isBox()) {
         }

         int len = 0;
         if (((KnuthElement)iter.previous()).isBox()) {
            while(true) {
               KnuthElement el;
               do {
                  if (iter.nextIndex() >= endIndex) {
                     len += ActiveCell.getElementContentLength((KnuthElement)iter.next());
                     return len;
                  }

                  el = (KnuthElement)iter.next();
               } while(!el.isBox() && !el.isGlue());

               len += el.getWidth();
            }
         } else {
            return len;
         }
      }
   }

   private void addAreasForCell(PrimaryGridUnit pgu, int startPos, int endPos, int rowHeight, int borderBeforeWhich, int borderAfterWhich, boolean lastOnPage) {
      int currentRowIndex = this.currentRow.getIndex();
      int startRowIndex;
      int firstRowHeight;
      if (pgu.getRowIndex() >= this.firstRowIndex) {
         startRowIndex = pgu.getRowIndex();
         if (startRowIndex < currentRowIndex) {
            firstRowHeight = this.getRowOffset(startRowIndex + 1) - this.getRowOffset(startRowIndex);
         } else {
            firstRowHeight = rowHeight;
         }
      } else {
         startRowIndex = this.firstRowIndex;
         firstRowHeight = 0;
      }

      int[] spannedGridRowHeights = null;
      int prevOffset;
      int i;
      if (!this.tclm.getTableLM().getTable().isSeparateBorderModel() && pgu.hasSpanning()) {
         spannedGridRowHeights = new int[currentRowIndex - startRowIndex + 1];
         prevOffset = this.getRowOffset(startRowIndex);

         for(i = 0; i < currentRowIndex - startRowIndex; ++i) {
            int newOffset = this.getRowOffset(startRowIndex + i + 1);
            spannedGridRowHeights[i] = newOffset - prevOffset;
            prevOffset = newOffset;
         }

         spannedGridRowHeights[currentRowIndex - startRowIndex] = rowHeight;
      }

      prevOffset = this.getRowOffset(startRowIndex);
      i = rowHeight + this.currentRowOffset - prevOffset;
      if (log.isDebugEnabled()) {
         log.debug("Creating area for cell:");
         log.debug("  start row: " + pgu.getRowIndex() + " " + this.currentRowOffset + " " + prevOffset);
         log.debug(" rowHeight=" + rowHeight + " cellTotalHeight=" + i);
      }

      TableCellLayoutManager cellLM = pgu.getCellLM();
      cellLM.setXOffset(this.tclm.getXOffsetOfGridUnit(pgu));
      cellLM.setYOffset(prevOffset);
      cellLM.setContentHeight(this.computeContentLength(pgu, startPos, endPos));
      cellLM.setTotalHeight(i);
      int prevBreak = ElementListUtils.determinePreviousBreak(pgu.getElements(), startPos);
      if (endPos >= 0) {
         SpaceResolver.performConditionalsNotification(pgu.getElements(), startPos, endPos, prevBreak);
      }

      cellLM.addAreas(new KnuthPossPosIter(pgu.getElements(), startPos, endPos + 1), this.layoutContext, spannedGridRowHeights, startRowIndex - pgu.getRowIndex(), currentRowIndex - pgu.getRowIndex(), borderBeforeWhich, borderAfterWhich, startRowIndex == this.firstRowOnPageIndex, lastOnPage, this, firstRowHeight);
   }

   private void addAreaForEmptyGridUnit(EmptyGridUnit gu, int rowIndex, int colIndex, int actualRowHeight, int borderBeforeWhich, int borderAfterWhich, boolean lastOnPage) {
      CommonBorderPaddingBackground.BorderInfo borderBefore = gu.getBorderBefore(borderBeforeWhich);
      CommonBorderPaddingBackground.BorderInfo borderAfter = gu.getBorderAfter(borderAfterWhich);
      CommonBorderPaddingBackground.BorderInfo borderStart = gu.getBorderStart();
      CommonBorderPaddingBackground.BorderInfo borderEnd = gu.getBorderEnd();
      if (borderBefore.getRetainedWidth() != 0 || borderAfter.getRetainedWidth() != 0 || borderStart.getRetainedWidth() != 0 || borderEnd.getRetainedWidth() != 0) {
         TableLayoutManager tableLM = this.tclm.getTableLM();
         Table table = tableLM.getTable();
         TableColumn col = this.tclm.getColumns().getColumn(colIndex + 1);
         boolean firstOnPage = rowIndex == this.firstRowOnPageIndex;
         boolean inFirstColumn = colIndex == 0;
         boolean inLastColumn = colIndex == table.getNumberOfColumns() - 1;
         int ipd = col.getColumnWidth().getValue(tableLM);
         ipd -= (borderStart.getRetainedWidth() + borderEnd.getRetainedWidth()) / 2;
         int bpd = actualRowHeight - (borderBefore.getRetainedWidth() + borderAfter.getRetainedWidth()) / 2;
         Block block = new Block();
         block.setPositioning(2);
         block.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
         block.setIPD(ipd);
         block.setBPD(bpd);
         block.setXOffset(this.tclm.getXOffsetOfGridUnit(colIndex) + borderStart.getRetainedWidth() / 2);
         block.setYOffset(this.getRowOffset(rowIndex) - borderBefore.getRetainedWidth() / 2);
         boolean[] outer = new boolean[]{firstOnPage, lastOnPage, inFirstColumn, inLastColumn};
         TraitSetter.addCollapsingBorders(block, borderBefore, borderAfter, borderStart, borderEnd, outer);
         tableLM.addChildArea(block);
      }
   }

   void registerPartBackgroundArea(Block backgroundArea) {
      this.tclm.getTableLM().addBackgroundArea(backgroundArea);
      this.tablePartBackgroundAreas.add(backgroundArea);
   }

   private void recordRowOffset(int rowIndex, int offset) {
      for(int i = this.rowOffsets.size(); i <= rowIndex - this.firstRowIndex; ++i) {
         this.rowOffsets.add(new Integer(offset));
      }

   }

   private int getRowOffset(int rowIndex) {
      return (Integer)this.rowOffsets.get(rowIndex - this.firstRowIndex);
   }

   void startBody() {
      Arrays.fill(this.firstCellOnPage, true);
   }

   void endBody() {
      Arrays.fill(this.firstCellOnPage, false);
   }

   static {
      $assertionsDisabled = !RowPainter.class.desiredAssertionStatus();
      log = LogFactory.getLog(RowPainter.class);
   }
}
