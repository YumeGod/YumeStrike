package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.TableColLength;
import org.apache.fop.layoutmgr.table.CollapsingBorderModel;
import org.xml.sax.Locator;

public class TableColumn extends TableFObj {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private int columnNumber;
   private Length columnWidth;
   private int numberColumnsRepeated;
   private int numberColumnsSpanned;
   private boolean implicitColumn;
   private PropertyList pList;

   public TableColumn(FONode parent) {
      this(parent, false);
   }

   public TableColumn(FONode parent, boolean implicit) {
      super(parent);
      this.pList = null;
      this.implicitColumn = implicit;
   }

   public void bind(PropertyList pList) throws FOPException {
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.columnNumber = pList.get(76).getNumeric().getValue();
      this.columnWidth = pList.get(77).getLength();
      this.numberColumnsRepeated = pList.get(164).getNumeric().getValue();
      this.numberColumnsSpanned = pList.get(165).getNumeric().getValue();
      super.bind(pList);
      TableEventProducer eventProducer;
      if (this.numberColumnsRepeated <= 0) {
         eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.valueMustBeBiggerGtEqOne(this, "number-columns-repeated", this.numberColumnsRepeated, this.getLocator());
      }

      if (this.numberColumnsSpanned <= 0) {
         eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.valueMustBeBiggerGtEqOne(this, "number-columns-spanned", this.numberColumnsSpanned, this.getLocator());
      }

      if (this.columnWidth.getEnum() == 9) {
         if (!this.implicitColumn && !this.getTable().isAutoLayout()) {
            eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.warnImplicitColumns(this, this.getLocator());
         }

         this.columnWidth = new TableColLength(1.0, this);
      }

      if (!this.implicitColumn) {
         this.pList = pList;
      }

   }

   public void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startColumn(this);
   }

   void setCollapsedBorders(CollapsingBorderModel collapsingBorderModel) {
      this.collapsingBorderModel = collapsingBorderModel;
      this.setCollapsedBorders();
   }

   public void endOfNode() throws FOPException {
      this.getFOEventHandler().endColumn(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public Length getColumnWidth() {
      return this.columnWidth;
   }

   public void setColumnWidth(Length columnWidth) {
      this.columnWidth = columnWidth;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   protected void setColumnNumber(int columnNumber) {
      this.columnNumber = columnNumber;
   }

   public int getNumberColumnsRepeated() {
      return this.numberColumnsRepeated;
   }

   public int getNumberColumnsSpanned() {
      return this.numberColumnsSpanned;
   }

   public String getLocalName() {
      return "table-column";
   }

   public int getNameId() {
      return 76;
   }

   public boolean isImplicitColumn() {
      return this.implicitColumn;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("fo:table-column");
      sb.append(" column-number=").append(this.getColumnNumber());
      if (this.getNumberColumnsRepeated() > 1) {
         sb.append(" number-columns-repeated=").append(this.getNumberColumnsRepeated());
      }

      if (this.getNumberColumnsSpanned() > 1) {
         sb.append(" number-columns-spanned=").append(this.getNumberColumnsSpanned());
      }

      sb.append(" column-width=").append(((Property)this.getColumnWidth()).getString());
      return sb.toString();
   }

   public Property getProperty(int propId) throws PropertyException {
      return this.pList.get(propId);
   }

   protected void releasePropertyList() {
      this.pList = null;
   }
}
