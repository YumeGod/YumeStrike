package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.layoutmgr.BlockLevelEventProducer;
import org.apache.fop.layoutmgr.BlockStackingLayoutManager;
import org.apache.fop.layoutmgr.BreakElement;
import org.apache.fop.layoutmgr.ConditionalElementListener;
import org.apache.fop.layoutmgr.KnuthElement;
import org.apache.fop.layoutmgr.KnuthGlue;
import org.apache.fop.layoutmgr.LayoutContext;
import org.apache.fop.layoutmgr.LeafPosition;
import org.apache.fop.layoutmgr.ListElement;
import org.apache.fop.layoutmgr.Position;
import org.apache.fop.layoutmgr.PositionIterator;
import org.apache.fop.layoutmgr.RelSide;
import org.apache.fop.layoutmgr.TraitSetter;
import org.apache.fop.traits.MinOptMax;
import org.apache.fop.traits.SpaceVal;
import org.apache.fop.util.BreakUtil;

public class TableLayoutManager extends BlockStackingLayoutManager implements ConditionalElementListener {
   private static Log log;
   private TableContentLayoutManager contentLM;
   private ColumnSetup columns = null;
   private Block curBlockArea;
   private double tableUnit;
   private boolean autoLayout = true;
   private boolean discardBorderBefore;
   private boolean discardBorderAfter;
   private boolean discardPaddingBefore;
   private boolean discardPaddingAfter;
   private MinOptMax effSpaceBefore;
   private MinOptMax effSpaceAfter;
   private int halfBorderSeparationBPD;
   private int halfBorderSeparationIPD;
   private List columnBackgroundAreas;
   private Position auxiliaryPosition;

   public TableLayoutManager(Table node) {
      super(node);
      this.columns = new ColumnSetup(node);
   }

   public Table getTable() {
      return (Table)this.fobj;
   }

   public ColumnSetup getColumns() {
      return this.columns;
   }

   public void initialize() {
      this.foSpaceBefore = (new SpaceVal(this.getTable().getCommonMarginBlock().spaceBefore, this)).getSpace();
      this.foSpaceAfter = (new SpaceVal(this.getTable().getCommonMarginBlock().spaceAfter, this)).getSpace();
      this.startIndent = this.getTable().getCommonMarginBlock().startIndent.getValue(this);
      this.endIndent = this.getTable().getCommonMarginBlock().endIndent.getValue(this);
      if (this.getTable().isSeparateBorderModel()) {
         this.halfBorderSeparationBPD = this.getTable().getBorderSeparation().getBPD().getLength().getValue(this) / 2;
         this.halfBorderSeparationIPD = this.getTable().getBorderSeparation().getIPD().getLength().getValue(this) / 2;
      } else {
         this.halfBorderSeparationBPD = 0;
         this.halfBorderSeparationIPD = 0;
      }

      if (!this.getTable().isAutoLayout() && this.getTable().getInlineProgressionDimension().getOptimum(this).getEnum() != 9) {
         this.autoLayout = false;
      }

   }

   private void resetSpaces() {
      this.discardBorderBefore = false;
      this.discardBorderAfter = false;
      this.discardPaddingBefore = false;
      this.discardPaddingAfter = false;
      this.effSpaceBefore = null;
      this.effSpaceAfter = null;
   }

   public int getHalfBorderSeparationBPD() {
      return this.halfBorderSeparationBPD;
   }

   public int getHalfBorderSeparationIPD() {
      return this.halfBorderSeparationIPD;
   }

   public List getNextKnuthElements(LayoutContext context, int alignment) {
      List returnList = new LinkedList();
      this.referenceIPD = context.getRefIPD();
      int sumOfColumns;
      if (this.getTable().getInlineProgressionDimension().getOptimum(this).getEnum() != 9) {
         sumOfColumns = this.getTable().getInlineProgressionDimension().getOptimum(this).getLength().getValue(this);
         this.updateContentAreaIPDwithOverconstrainedAdjust(sumOfColumns);
      } else {
         if (!this.getTable().isAutoLayout()) {
            BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getTable().getUserAgent().getEventBroadcaster());
            eventProducer.tableFixedAutoWidthNotSupported(this, this.getTable().getLocator());
         }

         this.updateContentAreaIPDwithOverconstrainedAdjust();
      }

      sumOfColumns = this.columns.getSumOfColumnWidths(this);
      if (!this.autoLayout && sumOfColumns > this.getContentAreaIPD()) {
         log.debug(FONode.decorateWithContextInfo("The sum of all column widths is larger than the specified table width.", this.getTable()));
         this.updateContentAreaIPDwithOverconstrainedAdjust(sumOfColumns);
      }

      int availableIPD = this.referenceIPD - this.getIPIndents();
      if (this.getContentAreaIPD() > availableIPD) {
         BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getTable().getUserAgent().getEventBroadcaster());
         eventProducer.objectTooWide(this, this.getTable().getName(), this.getContentAreaIPD(), context.getRefIPD(), this.getTable().getLocator());
      }

      if (this.tableUnit == 0.0) {
         this.tableUnit = this.columns.computeTableUnit(this);
      }

      if (!this.firstVisibleMarkServed) {
         this.addKnuthElementsForSpaceBefore(returnList, alignment);
      }

      if (this.getTable().isSeparateBorderModel()) {
         this.addKnuthElementsForBorderPaddingBefore(returnList, !this.firstVisibleMarkServed);
         this.firstVisibleMarkServed = true;
         this.addPendingMarks(context);
      }

      this.contentLM = new TableContentLayoutManager(this);
      LayoutContext childLC = new LayoutContext(0);
      childLC.setRefIPD(context.getRefIPD());
      childLC.copyPendingMarksFrom(context);
      LinkedList contentKnuthElements = this.contentLM.getNextKnuthElements(childLC, alignment);
      Iterator iter = contentKnuthElements.iterator();

      while(iter.hasNext()) {
         ListElement el = (ListElement)iter.next();
         this.notifyPos(el.getPosition());
      }

      log.debug(contentKnuthElements);
      this.wrapPositionElements(contentKnuthElements, returnList);
      context.updateKeepWithPreviousPending(this.getKeepWithPrevious());
      context.updateKeepWithPreviousPending(childLC.getKeepWithPreviousPending());
      context.updateKeepWithNextPending(this.getKeepWithNext());
      context.updateKeepWithNextPending(childLC.getKeepWithNextPending());
      if (this.getTable().isSeparateBorderModel()) {
         this.addKnuthElementsForBorderPaddingAfter(returnList, true);
      }

      this.addKnuthElementsForSpaceAfter(returnList, alignment);
      int breakAfter;
      if (!context.suppressBreakBefore()) {
         breakAfter = BreakUtil.compareBreakClasses(this.getTable().getBreakBefore(), childLC.getBreakBefore());
         if (breakAfter != 9) {
            returnList.add(0, new BreakElement(this.getAuxiliaryPosition(), 0, -1000, breakAfter, context));
         }
      }

      breakAfter = BreakUtil.compareBreakClasses(this.getTable().getBreakAfter(), childLC.getBreakAfter());
      if (breakAfter != 9) {
         returnList.add(new BreakElement(this.getAuxiliaryPosition(), 0, -1000, breakAfter, context));
      }

      this.setFinished(true);
      this.resetSpaces();
      return returnList;
   }

   public Position getAuxiliaryPosition() {
      if (this.auxiliaryPosition == null) {
         this.auxiliaryPosition = new LeafPosition(this, 0);
      }

      return this.auxiliaryPosition;
   }

   void registerColumnBackgroundArea(TableColumn column, Block backgroundArea, int xShift) {
      this.addBackgroundArea(backgroundArea);
      if (this.columnBackgroundAreas == null) {
         this.columnBackgroundAreas = new ArrayList();
      }

      this.columnBackgroundAreas.add(new ColumnBackgroundInfo(column, backgroundArea, xShift));
   }

   public void addAreas(PositionIterator parentIter, LayoutContext layoutContext) {
      this.getParentArea((Area)null);
      this.addId();
      if (layoutContext.getSpaceBefore() != 0) {
         this.addBlockSpacing(0.0, MinOptMax.getInstance(layoutContext.getSpaceBefore()));
      }

      int startXOffset = this.getTable().getCommonMarginBlock().startIndent.getValue(this);
      int tableHeight = 0;
      LayoutContext lc = new LayoutContext(0);
      lc.setRefIPD(this.getContentAreaIPD());
      this.contentLM.setStartXOffset(startXOffset);
      this.contentLM.addAreas(parentIter, lc);
      tableHeight += this.contentLM.getUsedBPD();
      this.curBlockArea.setBPD(tableHeight);
      if (this.columnBackgroundAreas != null) {
         Iterator iter = this.columnBackgroundAreas.iterator();

         while(iter.hasNext()) {
            ColumnBackgroundInfo b = (ColumnBackgroundInfo)iter.next();
            TraitSetter.addBackground(b.backgroundArea, b.column.getCommonBorderPaddingBackground(), this, b.xShift, -b.backgroundArea.getYOffset(), b.column.getColumnWidth().getValue(this), tableHeight);
         }

         this.columnBackgroundAreas.clear();
      }

      if (this.getTable().isSeparateBorderModel()) {
         TraitSetter.addBorders(this.curBlockArea, this.getTable().getCommonBorderPaddingBackground(), this.discardBorderBefore, this.discardBorderAfter, false, false, this);
         TraitSetter.addPadding(this.curBlockArea, this.getTable().getCommonBorderPaddingBackground(), this.discardPaddingBefore, this.discardPaddingAfter, false, false, this);
      }

      TraitSetter.addBackground(this.curBlockArea, this.getTable().getCommonBorderPaddingBackground(), this);
      TraitSetter.addMargins(this.curBlockArea, this.getTable().getCommonBorderPaddingBackground(), this.startIndent, this.endIndent, this);
      TraitSetter.addBreaks(this.curBlockArea, this.getTable().getBreakBefore(), this.getTable().getBreakAfter());
      TraitSetter.addSpaceBeforeAfter(this.curBlockArea, layoutContext.getSpaceAdjust(), this.effSpaceBefore, this.effSpaceAfter);
      this.flush();
      this.resetSpaces();
      this.curBlockArea = null;
      this.notifyEndOfLayout();
   }

   public Area getParentArea(Area childArea) {
      if (this.curBlockArea == null) {
         this.curBlockArea = new Block();
         this.parentLayoutManager.getParentArea(this.curBlockArea);
         TraitSetter.setProducerID(this.curBlockArea, this.getTable().getId());
         this.curBlockArea.setIPD(this.getContentAreaIPD());
         this.setCurrentArea(this.curBlockArea);
      }

      return this.curBlockArea;
   }

   public void addChildArea(Area childArea) {
      if (this.curBlockArea != null) {
         this.curBlockArea.addBlock((Block)childArea);
      }

   }

   void addBackgroundArea(Block background) {
      this.curBlockArea.addChildArea(background);
   }

   public int negotiateBPDAdjustment(int adj, KnuthElement lastElement) {
      return 0;
   }

   public void discardSpace(KnuthGlue spaceGlue) {
   }

   public KeepProperty getKeepTogetherProperty() {
      return this.getTable().getKeepTogether();
   }

   public KeepProperty getKeepWithPreviousProperty() {
      return this.getTable().getKeepWithPrevious();
   }

   public KeepProperty getKeepWithNextProperty() {
      return this.getTable().getKeepWithNext();
   }

   public int getBaseLength(int lengthBase, FObj fobj) {
      if (fobj instanceof TableColumn && fobj.getParent() == this.getFObj()) {
         switch (lengthBase) {
            case 5:
               return this.getContentAreaIPD();
            case 11:
               return (int)this.tableUnit;
            default:
               log.error("Unknown base type for LengthBase.");
               return 0;
         }
      } else {
         switch (lengthBase) {
            case 11:
               return (int)this.tableUnit;
            default:
               return super.getBaseLength(lengthBase, fobj);
         }
      }
   }

   public void notifySpace(RelSide side, MinOptMax effectiveLength) {
      if (RelSide.BEFORE == side) {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceBefore + "-> " + effectiveLength);
         }

         this.effSpaceBefore = effectiveLength;
      } else {
         if (log.isDebugEnabled()) {
            log.debug(this + ": Space " + side + ", " + this.effSpaceAfter + "-> " + effectiveLength);
         }

         this.effSpaceAfter = effectiveLength;
      }

   }

   public void notifyBorder(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardBorderBefore = true;
         } else {
            this.discardBorderAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Border " + side + " -> " + effectiveLength);
      }

   }

   public void notifyPadding(RelSide side, MinOptMax effectiveLength) {
      if (effectiveLength == null) {
         if (RelSide.BEFORE == side) {
            this.discardPaddingBefore = true;
         } else {
            this.discardPaddingAfter = true;
         }
      }

      if (log.isDebugEnabled()) {
         log.debug(this + ": Padding " + side + " -> " + effectiveLength);
      }

   }

   static {
      log = LogFactory.getLog(TableLayoutManager.class);
   }

   private static final class ColumnBackgroundInfo {
      private TableColumn column;
      private Block backgroundArea;
      private int xShift;

      private ColumnBackgroundInfo(TableColumn column, Block backgroundArea, int xShift) {
         this.column = column;
         this.backgroundArea = backgroundArea;
         this.xShift = xShift;
      }

      // $FF: synthetic method
      ColumnBackgroundInfo(TableColumn x0, Block x1, int x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
