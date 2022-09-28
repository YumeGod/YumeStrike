package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.PrimaryGridUnit;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.ElementListUtils;
import org.apache.fop.layoutmgr.Keep;
import org.apache.fop.layoutmgr.KnuthBox;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.KnuthPossPosIter;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.ListElement;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.SpaceResolver;
import org.apache.fop.util.BreakUtil;

public class TableContentLayoutManager implements PercentBaseContext {
   private static Log log;
   private TableLayoutManager tableLM;
   private TableRowIterator bodyIter;
   private TableRowIterator headerIter;
   private TableRowIterator footerIter;
   private LinkedList headerList;
   private LinkedList footerList;
   private int headerNetHeight = 0;
   private int footerNetHeight = 0;
   private int startXOffset;
   private int usedBPD;
   private TableStepper stepper;

   TableContentLayoutManager(TableLayoutManager parent) {
      this.tableLM = parent;
      Table table = this.getTableLM().getTable();
      this.bodyIter = new TableRowIterator(table, 0);
      if (table.getTableHeader() != null) {
         this.headerIter = new TableRowIterator(table, 1);
      }

      if (table.getTableFooter() != null) {
         this.footerIter = new TableRowIterator(table, 2);
      }

      this.stepper = new TableStepper(this);
   }

   TableLayoutManager getTableLM() {
      return this.tableLM;
   }

   boolean isSeparateBorderModel() {
      return this.getTableLM().getTable().isSeparateBorderModel();
   }

   ColumnSetup getColumns() {
      return this.getTableLM().getColumns();
   }

   protected int getHeaderNetHeight() {
      return this.headerNetHeight;
   }

   protected int getFooterNetHeight() {
      return this.footerNetHeight;
   }

   protected LinkedList getHeaderElements() {
      return this.headerList;
   }

   protected LinkedList getFooterElements() {
      return this.footerList;
   }

   public LinkedList getNextKnuthElements(LayoutContext context, int alignment) {
      if (log.isDebugEnabled()) {
         log.debug("==> Columns: " + this.getTableLM().getColumns());
      }

      KnuthBox headerAsFirst = null;
      KnuthBox headerAsSecondToLast = null;
      KnuthBox footerAsLast = null;
      TableHeaderFooterPosition pos;
      KnuthBox box;
      if (this.headerIter != null && this.headerList == null) {
         this.headerList = this.getKnuthElementsForRowIterator(this.headerIter, context, alignment, 1);
         this.headerNetHeight = ElementListUtils.calcContentLength(this.headerList);
         if (log.isDebugEnabled()) {
            log.debug("==> Header: " + this.headerNetHeight + " - " + this.headerList);
         }

         pos = new TableHeaderFooterPosition(this.getTableLM(), true, this.headerList);
         box = new KnuthBox(this.headerNetHeight, pos, false);
         if (this.getTableLM().getTable().omitHeaderAtBreak()) {
            headerAsFirst = box;
         } else {
            headerAsSecondToLast = box;
         }
      }

      if (this.footerIter != null && this.footerList == null) {
         this.footerList = this.getKnuthElementsForRowIterator(this.footerIter, context, alignment, 2);
         this.footerNetHeight = ElementListUtils.calcContentLength(this.footerList);
         if (log.isDebugEnabled()) {
            log.debug("==> Footer: " + this.footerNetHeight + " - " + this.footerList);
         }

         pos = new TableHeaderFooterPosition(this.getTableLM(), false, this.footerList);
         box = new KnuthBox(this.footerNetHeight, pos, false);
         footerAsLast = box;
      }

      LinkedList returnList = this.getKnuthElementsForRowIterator(this.bodyIter, context, alignment, 0);
      int insertionPoint;
      if (headerAsFirst != null) {
         insertionPoint = 0;
         if (returnList.size() > 0 && ((ListElement)returnList.getFirst()).isForcedBreak()) {
            ++insertionPoint;
         }

         returnList.add(insertionPoint, headerAsFirst);
      } else if (headerAsSecondToLast != null) {
         insertionPoint = returnList.size();
         if (returnList.size() > 0 && ((ListElement)returnList.getLast()).isForcedBreak()) {
            --insertionPoint;
         }

         returnList.add(insertionPoint, headerAsSecondToLast);
      }

      if (footerAsLast != null) {
         insertionPoint = returnList.size();
         if (returnList.size() > 0 && ((ListElement)returnList.getLast()).isForcedBreak()) {
            --insertionPoint;
         }

         returnList.add(insertionPoint, footerAsLast);
      }

      return returnList;
   }

   private LinkedList getKnuthElementsForRowIterator(TableRowIterator iter, LayoutContext context, int alignment, int bodyType) {
      LinkedList returnList = new LinkedList();
      EffRow[] rowGroup = iter.getNextRowGroup();
      context.clearKeepsPending();
      context.setBreakBefore(9);
      context.setBreakAfter(9);
      Keep keepWithPrevious = Keep.KEEP_AUTO;
      int breakBefore = 9;
      int breakBetween;
      if (rowGroup != null) {
         RowGroupLayoutManager rowGroupLM = new RowGroupLayoutManager(this.getTableLM(), rowGroup, this.stepper);
         List nextRowGroupElems = rowGroupLM.getNextKnuthElements(context, alignment, bodyType);
         keepWithPrevious = keepWithPrevious.compare(context.getKeepWithPreviousPending());
         breakBefore = context.getBreakBefore();
         breakBetween = context.getBreakAfter();
         returnList.addAll(nextRowGroupElems);

         while((rowGroup = iter.getNextRowGroup()) != null) {
            rowGroupLM = new RowGroupLayoutManager(this.getTableLM(), rowGroup, this.stepper);
            Keep keepWithNextPending = context.getKeepWithNextPending();
            context.clearKeepWithNextPending();
            nextRowGroupElems = rowGroupLM.getNextKnuthElements(context, alignment, bodyType);
            Keep keep = keepWithNextPending.compare(context.getKeepWithPreviousPending());
            context.clearKeepWithPreviousPending();
            keep = keep.compare(this.getTableLM().getKeepTogether());
            int penaltyValue = keep.getPenalty();
            int breakClass = keep.getContext();
            breakBetween = BreakUtil.compareBreakClasses(breakBetween, context.getBreakBefore());
            if (breakBetween != 9) {
               penaltyValue = -1000;
               breakClass = breakBetween;
            }

            ListIterator elemIter = returnList.listIterator(returnList.size());
            ListElement elem = (ListElement)elemIter.previous();
            BreakElement breakElement;
            if (elem instanceof KnuthGlue) {
               breakElement = (BreakElement)elemIter.previous();
            } else {
               breakElement = (BreakElement)elem;
            }

            breakElement.setPenaltyValue(penaltyValue);
            breakElement.setBreakClass(breakClass);
            returnList.addAll(nextRowGroupElems);
            breakBetween = context.getBreakAfter();
         }
      }

      ListIterator elemIter = returnList.listIterator(returnList.size());
      ListElement elem = (ListElement)elemIter.previous();
      if (elem instanceof KnuthGlue) {
         BreakElement breakElement = (BreakElement)elemIter.previous();
         breakElement.setPenaltyValue(1000);
      } else {
         elemIter.remove();
      }

      context.updateKeepWithPreviousPending(keepWithPrevious);
      context.setBreakBefore(breakBefore);
      breakBetween = this.getTableLM().getTable().getWidowContentLimit().getValue();
      if (breakBetween != 0 && bodyType == 0) {
         ElementListUtils.removeLegalBreaks(returnList, breakBetween);
      }

      int orphanContentLimit = this.getTableLM().getTable().getOrphanContentLimit().getValue();
      if (orphanContentLimit != 0 && bodyType == 0) {
         ElementListUtils.removeLegalBreaksFromEnd(returnList, orphanContentLimit);
      }

      return returnList;
   }

   protected int getXOffsetOfGridUnit(PrimaryGridUnit gu) {
      return this.getXOffsetOfGridUnit(gu.getColIndex());
   }

   protected int getXOffsetOfGridUnit(int colIndex) {
      return this.startXOffset + this.getTableLM().getColumns().getXOffset(colIndex + 1, this.getTableLM());
   }

   void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      this.usedBPD = 0;
      RowPainter painter = new RowPainter(this, layoutContext);
      List tablePositions = new ArrayList();
      List headerElements = null;
      List footerElements = null;
      Position firstPos = null;
      Position lastPos = null;
      Position lastCheckPos = null;

      while(parentIter.hasNext()) {
         Position pos = (Position)parentIter.next();
         if (pos instanceof SpaceResolver.SpaceHandlingBreakPosition) {
            pos = ((SpaceResolver.SpaceHandlingBreakPosition)pos).getOriginalBreakPosition();
         }

         if (pos != null) {
            if (firstPos == null) {
               firstPos = pos;
            }

            lastPos = pos;
            if (pos.getIndex() >= 0) {
               lastCheckPos = pos;
            }

            if (pos instanceof TableHeaderFooterPosition) {
               TableHeaderFooterPosition thfpos = (TableHeaderFooterPosition)pos;
               if (thfpos.header) {
                  headerElements = thfpos.nestedElements;
               } else {
                  footerElements = thfpos.nestedElements;
               }
            } else if (!(pos instanceof TableHFPenaltyPosition)) {
               if (pos instanceof TableContentPosition) {
                  tablePositions.add(pos);
               } else if (log.isDebugEnabled()) {
                  log.debug("Ignoring position: " + pos);
               }
            }
         }
      }

      if (lastPos instanceof TableHFPenaltyPosition) {
         TableHFPenaltyPosition penaltyPos = (TableHFPenaltyPosition)lastPos;
         log.debug("Break at penalty!");
         if (penaltyPos.headerElements != null) {
            headerElements = penaltyPos.headerElements;
         }

         if (penaltyPos.footerElements != null) {
            footerElements = penaltyPos.footerElements;
         }
      }

      Map markers = this.getTableLM().getTable().getMarkers();
      if (markers != null) {
         this.getTableLM().getCurrentPV().addMarkers(markers, true, this.getTableLM().isFirst(firstPos), this.getTableLM().isLast(lastCheckPos));
      }

      if (headerElements != null) {
         this.addHeaderFooterAreas(headerElements, this.tableLM.getTable().getTableHeader(), painter, false);
      }

      if (tablePositions.isEmpty()) {
         log.error("tablePositions empty. Please send your FO file to fop-users@xmlgraphics.apache.org");
      } else {
         this.addBodyAreas(tablePositions.iterator(), painter, footerElements == null);
      }

      if (footerElements != null) {
         this.addHeaderFooterAreas(footerElements, this.tableLM.getTable().getTableFooter(), painter, true);
      }

      this.usedBPD += painter.getAccumulatedBPD();
      if (markers != null) {
         this.getTableLM().getCurrentPV().addMarkers(markers, false, this.getTableLM().isFirst(firstPos), this.getTableLM().isLast(lastCheckPos));
      }

   }

   private void addHeaderFooterAreas(List elements, TablePart part, RowPainter painter, boolean lastOnPage) {
      List lst = new ArrayList(elements.size());
      Iterator iter = new KnuthPossPosIter(elements);

      while(iter.hasNext()) {
         Position pos = (Position)iter.next();
         if (pos instanceof TableContentPosition) {
            lst.add((TableContentPosition)pos);
         }
      }

      this.addTablePartAreas(lst, painter, part, true, true, true, lastOnPage);
   }

   private void addBodyAreas(Iterator iterator, RowPainter painter, boolean lastOnPage) {
      painter.startBody();
      List lst = new ArrayList();
      TableContentPosition pos = (TableContentPosition)iterator.next();
      boolean isFirstPos = pos.getFlag(1) && pos.getRow().getFlag(0);
      TablePart part = pos.getTablePart();
      lst.add(pos);

      for(; iterator.hasNext(); lst.add(pos)) {
         pos = (TableContentPosition)iterator.next();
         if (pos.getTablePart() != part) {
            this.addTablePartAreas(lst, painter, part, isFirstPos, true, false, false);
            isFirstPos = true;
            lst.clear();
            part = pos.getTablePart();
         }
      }

      boolean isLastPos = pos.getFlag(2) && pos.getRow().getFlag(1);
      this.addTablePartAreas(lst, painter, part, isFirstPos, isLastPos, true, lastOnPage);
      painter.endBody();
   }

   private void addTablePartAreas(List positions, RowPainter painter, TablePart body, boolean isFirstPos, boolean isLastPos, boolean lastInBody, boolean lastOnPage) {
      this.getTableLM().getCurrentPV().addMarkers(body.getMarkers(), true, isFirstPos, isLastPos);
      painter.startTablePart(body);
      Iterator iter = positions.iterator();

      while(iter.hasNext()) {
         painter.handleTableContentPosition((TableContentPosition)iter.next());
      }

      this.getTableLM().getCurrentPV().addMarkers(body.getMarkers(), false, isFirstPos, isLastPos);
      painter.endTablePart(lastInBody, lastOnPage);
   }

   void setStartXOffset(int startXOffset) {
      this.startXOffset = startXOffset;
   }

   int getUsedBPD() {
      return this.usedBPD;
   }

   public int getBaseLength(int lengthBase, FObj fobj) {
      return this.tableLM.getBaseLength(lengthBase, fobj);
   }

   static {
      log = LogFactory.getLog(TableContentLayoutManager.class);
   }
}
