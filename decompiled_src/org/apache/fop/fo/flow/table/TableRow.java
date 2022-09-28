package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.BreakPropertySet;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.KeepProperty;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class TableRow extends TableCellContainer implements BreakPropertySet {
   private LengthRangeProperty blockProgressionDimension;
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private int breakAfter;
   private int breakBefore;
   private Length height;
   private KeepProperty keepTogether;
   private KeepProperty keepWithNext;
   private KeepProperty keepWithPrevious;

   public TableRow(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.breakAfter = pList.get(58).getEnum();
      this.breakBefore = pList.get(59).getEnum();
      this.height = pList.get(115).getLength();
      this.keepTogether = pList.get(131).getKeep();
      this.keepWithNext = pList.get(132).getKeep();
      this.keepWithPrevious = pList.get(133).getKeep();
      super.bind(pList);
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      super.processNode(elementName, locator, attlist, pList);
      if (!this.inMarker()) {
         TablePart part = (TablePart)this.parent;
         this.pendingSpans = part.pendingSpans;
         this.columnNumberManager = part.columnNumberManager;
      }

   }

   protected void addChildNode(FONode child) throws FOPException {
      if (!this.inMarker()) {
         TableCell cell = (TableCell)child;
         TablePart part = (TablePart)this.getParent();
         this.addTableCellChild(cell, part.isFirst(this));
      }

      super.addChildNode(child);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startRow(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endRow(this);
   }

   public void finalizeNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(table-cell+)");
      }

      if (!this.inMarker()) {
         this.pendingSpans = null;
         this.columnNumberManager = null;
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if ("marker".equals(localName)) {
            if (this.firstChild != null) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(table-cell+)");
            }
         } else if (!"table-cell".equals(localName)) {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   TablePart getTablePart() {
      return (TablePart)this.parent;
   }

   boolean isTableRow() {
      return true;
   }

   public int getBreakAfter() {
      return this.breakAfter;
   }

   public int getBreakBefore() {
      return this.breakBefore;
   }

   public KeepProperty getKeepWithPrevious() {
      return this.keepWithPrevious;
   }

   public KeepProperty getKeepWithNext() {
      return this.keepWithNext;
   }

   public KeepProperty getKeepTogether() {
      return this.keepTogether;
   }

   public boolean mustKeepTogether() {
      return !this.getKeepTogether().getWithinPage().isAuto() || !this.getKeepTogether().getWithinColumn().isAuto();
   }

   public boolean mustKeepWithNext() {
      return !this.getKeepWithNext().getWithinPage().isAuto() || !this.getKeepWithNext().getWithinColumn().isAuto();
   }

   public boolean mustKeepWithPrevious() {
      return !this.getKeepWithPrevious().getWithinPage().isAuto() || !this.getKeepWithPrevious().getWithinColumn().isAuto();
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public Length getHeight() {
      return this.height;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public String getLocalName() {
      return "table-row";
   }

   public int getNameId() {
      return 79;
   }
}
