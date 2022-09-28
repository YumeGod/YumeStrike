package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.ValidationPercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.StaticPropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.LengthPairProperty;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.apache.fop.fo.properties.TableColLength;
import org.xml.sax.Locator;

public class Table extends TableFObj implements ColumnNumberManagerHolder, BreakPropertySet {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private CommonMarginBlock commonMarginBlock;
   private LengthRangeProperty blockProgressionDimension;
   private int borderCollapse;
   private LengthPairProperty borderSeparation;
   private int breakAfter;
   private int breakBefore;
   private LengthRangeProperty inlineProgressionDimension;
   private KeepProperty keepTogether;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;
   private int tableLayout;
   private int tableOmitFooterAtBreak;
   private int tableOmitHeaderAtBreak;
   private Length widowContentLimit;
   private Length orphanContentLimit;
   private List columns = new ArrayList();
   private ColumnNumberManager columnNumberManager = new ColumnNumberManager();
   private TableHeader tableHeader = null;
   private TableFooter tableFooter = null;
   private boolean tableColumnFound = false;
   private boolean tableHeaderFound = false;
   private boolean tableFooterFound = false;
   private boolean tableBodyFound = false;
   private boolean hasExplicitColumns = false;
   private boolean columnsFinalized = false;
   private RowGroupBuilder rowGroupBuilder;
   private PropertyList propList;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public Table(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.commonMarginBlock = pList.getMarginBlockProps();
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.borderCollapse = pList.get(31).getEnum();
      this.borderSeparation = pList.get(45).getLengthPair();
      this.breakAfter = pList.get(58).getEnum();
      this.breakBefore = pList.get(59).getEnum();
      this.inlineProgressionDimension = pList.get(127).getLengthRange();
      this.keepTogether = pList.get(131).getKeep();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      this.tableLayout = pList.get(239).getEnum();
      this.tableOmitFooterAtBreak = pList.get(240).getEnum();
      this.tableOmitHeaderAtBreak = pList.get(241).getEnum();
      this.widowContentLimit = pList.get(271).getLength();
      this.orphanContentLimit = pList.get(272).getLength();
      TableEventProducer eventProducer;
      if (!this.blockProgressionDimension.getOptimum((PercentBaseContext)null).isAuto()) {
         eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.nonAutoBPDOnTable(this, this.getLocator());
      }

      if (this.tableLayout == 9) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), "table-layout=\"auto\"", this.getLocator());
      }

      if (!this.isSeparateBorderModel() && this.getCommonBorderPaddingBackground().hasPadding(ValidationPercentBaseContext.getPseudoContext())) {
         eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.noTablePaddingWithCollapsingBorderModel(this, this.getLocator());
      }

      this.propList = pList;
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startTable(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if ("marker".equals(localName)) {
            if (this.tableColumnFound || this.tableHeaderFound || this.tableFooterFound || this.tableBodyFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(table-column*,table-header?,table-footer?,table-body+)");
            }
         } else if ("table-column".equals(localName)) {
            this.tableColumnFound = true;
            if (this.tableHeaderFound || this.tableFooterFound || this.tableBodyFound) {
               this.nodesOutOfOrderError(loc, "fo:table-column", "(table-header?,table-footer?,table-body+)");
            }
         } else if ("table-header".equals(localName)) {
            if (this.tableHeaderFound) {
               this.tooManyNodesError(loc, "table-header");
            } else {
               this.tableHeaderFound = true;
               if (this.tableFooterFound || this.tableBodyFound) {
                  this.nodesOutOfOrderError(loc, "fo:table-header", "(table-footer?,table-body+)");
               }
            }
         } else if ("table-footer".equals(localName)) {
            if (this.tableFooterFound) {
               this.tooManyNodesError(loc, "table-footer");
            } else {
               this.tableFooterFound = true;
               if (this.tableBodyFound) {
                  if (this.getUserAgent().validateStrictly()) {
                     this.nodesOutOfOrderError(loc, "fo:table-footer", "(table-body+)", true);
                  }

                  if (!this.isSeparateBorderModel()) {
                     TableEventProducer eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
                     eventProducer.footerOrderCannotRecover(this, this.getName(), this.getLocator());
                  }
               }
            }
         } else if ("table-body".equals(localName)) {
            this.tableBodyFound = true;
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endTable(this);
   }

   public void finalizeNode() throws FOPException {
      if (!this.tableBodyFound) {
         this.missingChildElementError("(marker*,table-column*,table-header?,table-footer?,table-body+)");
      }

      if (!this.hasChildren()) {
         this.getParent().removeChild(this);
      } else {
         if (!this.inMarker()) {
            this.rowGroupBuilder.endTable();
            int i = this.columns.size();

            while(true) {
               --i;
               if (i < 0) {
                  this.propList = null;
                  this.rowGroupBuilder = null;
                  break;
               }

               TableColumn col = (TableColumn)this.columns.get(i);
               if (col != null) {
                  col.releasePropertyList();
               }
            }
         }

      }
   }

   protected void addChildNode(FONode child) throws FOPException {
      int childId = child.getNameId();
      switch (childId) {
         case 73:
         case 77:
         case 78:
            if (!this.inMarker() && !this.columnsFinalized) {
               this.columnsFinalized = true;
               if (this.hasExplicitColumns) {
                  this.finalizeColumns();
                  this.rowGroupBuilder = new FixedColRowGroupBuilder(this);
               } else {
                  this.rowGroupBuilder = new VariableColRowGroupBuilder(this);
               }
            }

            switch (childId) {
               case 77:
                  this.tableFooter = (TableFooter)child;
                  return;
               case 78:
                  this.tableHeader = (TableHeader)child;
                  return;
               default:
                  super.addChildNode(child);
                  return;
            }
         case 74:
         case 75:
         default:
            super.addChildNode(child);
            break;
         case 76:
            this.hasExplicitColumns = true;
            if (!this.inMarker()) {
               this.addColumnNode((TableColumn)child);
            } else {
               this.columns.add(child);
            }
      }

   }

   private void finalizeColumns() throws FOPException {
      for(int i = 0; i < this.columns.size(); ++i) {
         if (this.columns.get(i) == null) {
            this.columns.set(i, this.createImplicitColumn(i + 1));
         }
      }

   }

   public Table getTable() {
      return this;
   }

   void ensureColumnNumber(int columnNumber) throws FOPException {
      if (!$assertionsDisabled && this.hasExplicitColumns) {
         throw new AssertionError();
      } else {
         for(int i = this.columns.size() + 1; i <= columnNumber; ++i) {
            this.columns.add(this.createImplicitColumn(i));
         }

      }
   }

   private TableColumn createImplicitColumn(int colNumber) throws FOPException {
      TableColumn implicitColumn = new TableColumn(this, true);
      PropertyList pList = new StaticPropertyList(implicitColumn, this.propList);
      pList.setWritingMode();
      implicitColumn.bind(pList);
      implicitColumn.setColumnWidth(new TableColLength(1.0, implicitColumn));
      implicitColumn.setColumnNumber(colNumber);
      if (!this.isSeparateBorderModel()) {
         implicitColumn.setCollapsedBorders(this.collapsingBorderModel);
      }

      return implicitColumn;
   }

   private void addColumnNode(TableColumn col) {
      int colNumber = col.getColumnNumber();
      int colRepeat = col.getNumberColumnsRepeated();

      while(this.columns.size() < colNumber + colRepeat - 1) {
         this.columns.add((Object)null);
      }

      for(int i = colNumber - 1; i < colNumber + colRepeat - 1; ++i) {
         this.columns.set(i, col);
      }

      this.columnNumberManager.signalUsedColumnNumbers(colNumber, colNumber + colRepeat - 1);
   }

   boolean hasExplicitColumns() {
      return this.hasExplicitColumns;
   }

   public boolean isAutoLayout() {
      return this.tableLayout == 9;
   }

   public List getColumns() {
      return this.columns;
   }

   public TableColumn getColumn(int index) {
      return (TableColumn)this.columns.get(index);
   }

   public int getNumberOfColumns() {
      return this.columns.size();
   }

   public TableHeader getTableHeader() {
      return this.tableHeader;
   }

   public TableFooter getTableFooter() {
      return this.tableFooter;
   }

   public boolean omitHeaderAtBreak() {
      return this.tableOmitHeaderAtBreak == 149;
   }

   public boolean omitFooterAtBreak() {
      return this.tableOmitFooterAtBreak == 149;
   }

   public LengthRangeProperty getInlineProgressionDimension() {
      return this.inlineProgressionDimension;
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public CommonMarginBlock getCommonMarginBlock() {
      return this.commonMarginBlock;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public int getBreakAfter() {
      return this.breakAfter;
   }

   public int getBreakBefore() {
      return this.breakBefore;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public boolean mustKeepTogether() {
      return !this.getKeepTogether().getWithinPage().isAuto() || !this.getKeepTogether().getWithinColumn().isAuto();
   }

   public int getBorderCollapse() {
      return this.borderCollapse;
   }

   public boolean isSeparateBorderModel() {
      return this.getBorderCollapse() == 129;
   }

   public LengthPairProperty getBorderSeparation() {
      return this.borderSeparation;
   }

   public Length getWidowContentLimit() {
      return this.widowContentLimit;
   }

   public Length getOrphanContentLimit() {
      return this.orphanContentLimit;
   }

   public String getLocalName() {
      return "table";
   }

   public int getNameId() {
      return 71;
   }

   public FONode clone(FONode parent, boolean removeChildren) throws FOPException {
      Table clone = (Table)super.clone(parent, removeChildren);
      if (removeChildren) {
         clone.columns = new ArrayList();
         clone.columnsFinalized = false;
         clone.columnNumberManager = new ColumnNumberManager();
         clone.tableHeader = null;
         clone.tableFooter = null;
         clone.rowGroupBuilder = null;
      }

      return clone;
   }

   public ColumnNumberManager getColumnNumberManager() {
      return this.columnNumberManager;
   }

   RowGroupBuilder getRowGroupBuilder() {
      return this.rowGroupBuilder;
   }

   static {
      $assertionsDisabled = !Table.class.desiredAssertionStatus();
   }
}
