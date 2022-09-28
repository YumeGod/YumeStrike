package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.xml.sax.Locator;

public class TableCell extends TableFObj {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private LengthRangeProperty blockProgressionDimension;
   private int columnNumber;
   private int displayAlign;
   private int emptyCells;
   private int endsRow;
   private int numberColumnsSpanned;
   private int numberRowsSpanned;
   private int startsRow;
   private Length width;
   private boolean blockItemFound = false;

   public TableCell(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.displayAlign = pList.get(87).getEnum();
      this.emptyCells = pList.get(90).getEnum();
      this.startsRow = pList.get(235).getEnum();
      if (this.startsRow() && this.getParent().getNameId() != 79) {
         ((TablePart)this.getParent()).signalNewRow();
      }

      this.endsRow = pList.get(92).getEnum();
      this.columnNumber = pList.get(76).getNumeric().getValue();
      this.numberColumnsSpanned = pList.get(165).getNumeric().getValue();
      this.numberRowsSpanned = pList.get(166).getNumeric().getValue();
      this.width = pList.get(264).getLength();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startCell(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endCell(this);
   }

   public void finalizeNode() throws FOPException {
      if (!this.blockItemFound) {
         this.missingChildElementError("marker* (%block;)+", true);
      }

      if ((this.startsRow() || this.endsRow()) && this.getParent().getNameId() == 79) {
         TableEventProducer eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.startEndRowUnderTableRowWarning(this, this.getLocator());
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.blockItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(%block;)");
            }
         } else if (!this.isBlockItem(nsURI, localName)) {
            this.invalidChildError(loc, nsURI, localName);
         } else {
            this.blockItemFound = true;
         }
      }

   }

   public boolean generatesReferenceAreas() {
      return true;
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   public boolean showEmptyCells() {
      return this.emptyCells == 130;
   }

   public int getNumberColumnsSpanned() {
      return Math.max(this.numberColumnsSpanned, 1);
   }

   public int getNumberRowsSpanned() {
      return Math.max(this.numberRowsSpanned, 1);
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public int getDisplayAlign() {
      return this.displayAlign;
   }

   public Length getWidth() {
      return this.width;
   }

   public boolean startsRow() {
      return this.startsRow == 149;
   }

   public boolean endsRow() {
      return this.endsRow == 149;
   }

   public String getLocalName() {
      return "table-cell";
   }

   public final int getNameId() {
      return 75;
   }
}
