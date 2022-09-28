package org.apache.fop.layoutmgr.table;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.Trait;
import org.apache.fop.fo.flow.table.GridUnit;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.layoutmgr.AreaAdditionUtil;
import org.apache.fop.layoutmgr.BlockLevelLayoutManager;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPenalty;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.util.ListUtil;

public class TableCellLayoutManager extends BlockStackingLayoutManager implements BlockLevelLayoutManager {
   private static Log log;
   private PrimaryGridUnit primaryGridUnit;
   private Block curBlockArea;
   private int xoffset;
   private int yoffset;
   private int cellIPD;
   private int totalHeight;
   private int usedBPD;
   private boolean emptyCell = true;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public TableCellLayoutManager(TableCell node, PrimaryGridUnit pgu) {
      super(node);
      this.primaryGridUnit = pgu;
   }

   public TableCell getTableCell() {
      return (TableCell)this.fobj;
   }

   private boolean isSeparateBorderModel() {
      return this.getTable().isSeparateBorderModel();
   }

   public Table getTable() {
      return this.getTableCell().getTable();
   }

   protected int getIPIndents() {
      int[] startEndBorderWidths = this.primaryGridUnit.getStartEndBorderWidths();
      this.startIndent = startEndBorderWidths[0];
      this.endIndent = startEndBorderWidths[1];
      if (this.isSeparateBorderModel()) {
         int borderSep = this.getTable().getBorderSeparation().getLengthPair().getIPD().getLength().getValue(this);
         this.startIndent += borderSep / 2;
         this.endIndent += borderSep / 2;
      } else {
         this.startIndent /= 2;
         this.endIndent /= 2;
      }

      this.startIndent += this.getTableCell().getCommonBorderPaddingBackground().getPaddingStart(false, this);
      this.endIndent += this.getTableCell().getCommonBorderPaddingBackground().getPaddingEnd(false, this);
      return this.startIndent + this.endIndent;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      MinOptMax stackLimit = context.getStackLimitBP();
      this.referenceIPD = context.getRefIPD();
      this.cellIPD = this.referenceIPD;
      this.cellIPD -= this.getIPIndents();
      List contentList = new LinkedList();
      List returnList = new LinkedList();
      LayoutManager prevLM = null;

      LayoutManager curLM;
      while((curLM = this.getChildLM()) != null) {
         LayoutContext childLC = new LayoutContext(0);
         childLC.setStackLimitBP(context.getStackLimitBP().minus(stackLimit));
         childLC.setRefIPD(this.cellIPD);
         List returnedList = curLM.getNextKnuthElements(childLC, alignment);
         if (childLC.isKeepWithNextPending()) {
            log.debug("child LM signals pending keep with next");
         }

         if (contentList.isEmpty() && childLC.isKeepWithPreviousPending()) {
            this.primaryGridUnit.setKeepWithPrevious(childLC.getKeepWithPreviousPending());
            childLC.clearKeepWithPreviousPending();
         }

         if (prevLM != null && !ElementListUtils.endsWithForcedBreak(contentList)) {
            this.addInBetweenBreak(contentList, context, childLC);
         }

         contentList.addAll(returnedList);
         if (!returnedList.isEmpty()) {
            if (childLC.isKeepWithNextPending()) {
               context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
               childLC.clearKeepWithNextPending();
            }

            prevLM = curLM;
         }
      }

      this.primaryGridUnit.setKeepWithNext(context.getKeepWithNextPending());
      new LinkedList();
      if (!contentList.isEmpty()) {
         this.wrapPositionElements(contentList, returnList);
      } else {
         returnList.add(new KnuthBox(0, this.notifyPos(new Position(this)), true));
      }

      SpaceResolver.resolveElementList(returnList);
      if (((KnuthElement)returnList.get(0)).isForcedBreak()) {
         this.primaryGridUnit.setBreakBefore(((KnuthPenalty)returnList.get(0)).getBreakClass());
         returnList.remove(0);
         if (!$assertionsDisabled && returnList.isEmpty()) {
            throw new AssertionError();
         }
      }

      KnuthElement lastItem = (KnuthElement)ListUtil.getLast(returnList);
      if (lastItem.isForcedBreak()) {
         KnuthPenalty p = (KnuthPenalty)lastItem;
         this.primaryGridUnit.setBreakAfter(p.getBreakClass());
         p.setPenalty(0);
      }

      this.setFinished(true);
      return returnList;
   }

   public void setYOffset(int off) {
      this.yoffset = off;
   }

   public void setXOffset(int off) {
      this.xoffset = off;
   }

   public void setContentHeight(int h) {
      this.usedBPD = h;
   }

   public void setTotalHeight(int h) {
      this.totalHeight = h;
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext, int[] spannedGridRowHeights, int startRow, int endRow, int borderBeforeWhich, int borderAfterWhich, boolean firstOnPage, boolean lastOnPage, RowPainter painter, int firstRowHeight) {
      this.getParentArea((Area)null);
      this.addId();
      int borderBeforeWidth = this.primaryGridUnit.getBeforeBorderWidth(startRow, borderBeforeWhich);
      int borderAfterWidth = this.primaryGridUnit.getAfterBorderWidth(endRow, borderAfterWhich);
      CommonBorderPaddingBackground padding = this.primaryGridUnit.getCell().getCommonBorderPaddingBackground();
      int paddingRectBPD = this.totalHeight - borderBeforeWidth - borderAfterWidth;
      int cellBPD = paddingRectBPD - padding.getPaddingBefore(borderBeforeWhich == 2, this);
      cellBPD -= padding.getPaddingAfter(borderAfterWhich == 2, this);
      this.addBackgroundAreas(painter, firstRowHeight, borderBeforeWidth, paddingRectBPD);
      if (this.isSeparateBorderModel()) {
         if (!this.emptyCell || this.getTableCell().showEmptyCells()) {
            if (borderBeforeWidth > 0) {
               int halfBorderSepBPD = this.getTableCell().getTable().getBorderSeparation().getBPD().getLength().getValue() / 2;
               adjustYOffset(this.curBlockArea, halfBorderSepBPD);
            }

            TraitSetter.addBorders(this.curBlockArea, this.getTableCell().getCommonBorderPaddingBackground(), borderBeforeWidth == 0, borderAfterWidth == 0, false, false, this);
         }
      } else {
         boolean inFirstColumn = this.primaryGridUnit.getColIndex() == 0;
         boolean inLastColumn = this.primaryGridUnit.getColIndex() + this.getTableCell().getNumberColumnsSpanned() == this.getTable().getNumberOfColumns();
         if (!this.primaryGridUnit.hasSpanning()) {
            adjustYOffset(this.curBlockArea, -borderBeforeWidth);
            boolean[] outer = new boolean[]{firstOnPage, lastOnPage, inFirstColumn, inLastColumn};
            TraitSetter.addCollapsingBorders(this.curBlockArea, this.primaryGridUnit.getBorderBefore(borderBeforeWhich), this.primaryGridUnit.getBorderAfter(borderAfterWhich), this.primaryGridUnit.getBorderStart(), this.primaryGridUnit.getBorderEnd(), outer);
         } else {
            adjustYOffset(this.curBlockArea, borderBeforeWidth);
            Block[][] blocks = new Block[this.getTableCell().getNumberRowsSpanned()][this.getTableCell().getNumberColumnsSpanned()];
            GridUnit[] gridUnits = (GridUnit[])this.primaryGridUnit.getRows().get(startRow);

            int dy;
            GridUnit gu;
            CommonBorderPaddingBackground.BorderInfo border;
            int dx;
            for(dy = 0; dy < this.getTableCell().getNumberColumnsSpanned(); ++dy) {
               gu = gridUnits[dy];
               border = gu.getBorderBefore(borderBeforeWhich);
               dx = border.getRetainedWidth() / 2;
               if (dx > 0) {
                  this.addBorder(blocks, startRow, dy, Trait.BORDER_BEFORE, border, firstOnPage);
                  adjustYOffset(blocks[startRow][dy], -dx);
                  adjustBPD(blocks[startRow][dy], -dx);
               }
            }

            gridUnits = (GridUnit[])this.primaryGridUnit.getRows().get(endRow);

            for(dy = 0; dy < this.getTableCell().getNumberColumnsSpanned(); ++dy) {
               gu = gridUnits[dy];
               border = gu.getBorderAfter(borderAfterWhich);
               dx = border.getRetainedWidth() / 2;
               if (dx > 0) {
                  this.addBorder(blocks, endRow, dy, Trait.BORDER_AFTER, border, lastOnPage);
                  adjustBPD(blocks[endRow][dy], -dx);
               }
            }

            int bpd;
            for(dy = startRow; dy <= endRow; ++dy) {
               gridUnits = (GridUnit[])this.primaryGridUnit.getRows().get(dy);
               CommonBorderPaddingBackground.BorderInfo border = gridUnits[0].getBorderStart();
               bpd = border.getRetainedWidth() / 2;
               if (bpd > 0) {
                  this.addBorder(blocks, dy, 0, Trait.BORDER_START, border, inFirstColumn);
                  adjustXOffset(blocks[dy][0], bpd);
                  adjustIPD(blocks[dy][0], -bpd);
               }

               border = gridUnits[gridUnits.length - 1].getBorderEnd();
               bpd = border.getRetainedWidth() / 2;
               if (bpd > 0) {
                  this.addBorder(blocks, dy, gridUnits.length - 1, Trait.BORDER_END, border, inLastColumn);
                  adjustIPD(blocks[dy][gridUnits.length - 1], -bpd);
               }
            }

            dy = this.yoffset;

            for(int y = startRow; y <= endRow; ++y) {
               bpd = spannedGridRowHeights[y - startRow];
               dx = this.xoffset;

               for(int x = 0; x < gridUnits.length; ++x) {
                  int ipd = this.getTable().getColumn(this.primaryGridUnit.getColIndex() + x).getColumnWidth().getValue(this.getParent());
                  if (blocks[y][x] != null) {
                     Block block = blocks[y][x];
                     adjustYOffset(block, dy);
                     adjustXOffset(block, dx);
                     adjustIPD(block, ipd);
                     adjustBPD(block, bpd);
                     this.parentLayoutManager.addChildArea(block);
                  }

                  dx += ipd;
               }

               dy += bpd;
            }
         }
      }

      TraitSetter.addPadding(this.curBlockArea, padding, borderBeforeWhich == 2, borderAfterWhich == 2, false, false, this);
      if (this.usedBPD < cellBPD) {
         Block space;
         if (this.getTableCell().getDisplayAlign() == 23) {
            space = new Block();
            space.setBPD((cellBPD - this.usedBPD) / 2);
            this.curBlockArea.addBlock(space);
         } else if (this.getTableCell().getDisplayAlign() == 3) {
            space = new Block();
            space.setBPD(cellBPD - this.usedBPD);
            this.curBlockArea.addBlock(space);
         }
      }

      AreaAdditionUtil.addAreas(this, parentIter, layoutContext);
      this.curBlockArea.setBPD(cellBPD);
      if (!this.isSeparateBorderModel() || !this.emptyCell || this.getTableCell().showEmptyCells()) {
         TraitSetter.addBackground(this.curBlockArea, this.getTableCell().getCommonBorderPaddingBackground(), this);
      }

      this.flush();
      this.curBlockArea = null;
      this.notifyEndOfLayout();
   }

   private void addBackgroundAreas(RowPainter painter, int firstRowHeight, int borderBeforeWidth, int paddingRectBPD) {
      TableColumn column = this.getTable().getColumn(this.primaryGridUnit.getColIndex());
      if (column.getCommonBorderPaddingBackground().hasBackground()) {
         Block colBackgroundArea = this.getBackgroundArea(paddingRectBPD, borderBeforeWidth);
         ((TableLayoutManager)this.parentLayoutManager).registerColumnBackgroundArea(column, colBackgroundArea, -this.startIndent);
      }

      TablePart body = this.primaryGridUnit.getTablePart();
      if (body.getCommonBorderPaddingBackground().hasBackground()) {
         painter.registerPartBackgroundArea(this.getBackgroundArea(paddingRectBPD, borderBeforeWidth));
      }

      TableRow row = this.primaryGridUnit.getRow();
      if (row != null && row.getCommonBorderPaddingBackground().hasBackground()) {
         Block rowBackgroundArea = this.getBackgroundArea(paddingRectBPD, borderBeforeWidth);
         ((TableLayoutManager)this.parentLayoutManager).addBackgroundArea(rowBackgroundArea);
         TraitSetter.addBackground(rowBackgroundArea, row.getCommonBorderPaddingBackground(), this.parentLayoutManager, -this.xoffset - this.startIndent, -borderBeforeWidth, this.parentLayoutManager.getContentAreaIPD(), firstRowHeight);
      }

   }

   private void addBorder(Block[][] blocks, int i, int j, Integer side, CommonBorderPaddingBackground.BorderInfo border, boolean outer) {
      if (blocks[i][j] == null) {
         blocks[i][j] = new Block();
         blocks[i][j].addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
         blocks[i][j].setPositioning(2);
      }

      blocks[i][j].addTrait(side, new BorderProps(border.getStyle(), border.getRetainedWidth(), border.getColor(), outer ? 2 : 1));
   }

   private static void adjustXOffset(Block block, int amount) {
      block.setXOffset(block.getXOffset() + amount);
   }

   private static void adjustYOffset(Block block, int amount) {
      block.setYOffset(block.getYOffset() + amount);
   }

   private static void adjustIPD(Block block, int amount) {
      block.setIPD(block.getIPD() + amount);
   }

   private static void adjustBPD(Block block, int amount) {
      block.setBPD(block.getBPD() + amount);
   }

   private Block getBackgroundArea(int bpd, int borderBeforeWidth) {
      CommonBorderPaddingBackground padding = this.getTableCell().getCommonBorderPaddingBackground();
      int paddingStart = padding.getPaddingStart(false, this);
      int paddingEnd = padding.getPaddingEnd(false, this);
      Block block = new Block();
      TraitSetter.setProducerID(block, this.getTable().getId());
      block.setPositioning(2);
      block.setIPD(this.cellIPD + paddingStart + paddingEnd);
      block.setBPD(bpd);
      block.setXOffset(this.xoffset + this.startIndent - paddingStart);
      block.setYOffset(this.yoffset + borderBeforeWidth);
      return block;
   }

   public Area getParentArea(Area childArea) {
      if (this.curBlockArea == null) {
         this.curBlockArea = new Block();
         this.curBlockArea.addTrait(Trait.IS_REFERENCE_AREA, Boolean.TRUE);
         TraitSetter.setProducerID(this.curBlockArea, this.getTableCell().getId());
         this.curBlockArea.setPositioning(2);
         this.curBlockArea.setXOffset(this.xoffset + this.startIndent);
         this.curBlockArea.setYOffset(this.yoffset);
         this.curBlockArea.setIPD(this.cellIPD);
         this.parentLayoutManager.getParentArea(this.curBlockArea);
         this.setCurrentArea(this.curBlockArea);
      }

      return this.curBlockArea;
   }

   public void addChildArea(Area childArea) {
      if (this.curBlockArea != null) {
         this.curBlockArea.addBlock((Block)childArea);
      }

   }

   public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
      return 0;
   }

   public void discardSpace(KnuthGlue spaceGlue) {
   }

   public Keep getKeepTogether() {
      Keep keep = Keep.KEEP_AUTO;
      if (this.primaryGridUnit.getRow() != null) {
         keep = Keep.getKeep(this.primaryGridUnit.getRow().getKeepTogether());
      }

      keep = keep.compare(this.getParentKeepTogether());
      return keep;
   }

   public Keep getKeepWithNext() {
      return Keep.KEEP_AUTO;
   }

   public Keep getKeepWithPrevious() {
      return Keep.KEEP_AUTO;
   }

   public int getContentAreaIPD() {
      return this.cellIPD;
   }

   public int getContentAreaBPD() {
      if (this.curBlockArea != null) {
         return this.curBlockArea.getBPD();
      } else {
         log.error("getContentAreaBPD called on unknown BPD");
         return -1;
      }
   }

   public boolean getGeneratesReferenceArea() {
      return true;
   }

   public boolean getGeneratesBlockArea() {
      return true;
   }

   static {
      $assertionsDisabled = !TableCellLayoutManager.class.desiredAssertionStatus();
      log = LogFactory.getLog(TableCellLayoutManager.class);
   }
}
