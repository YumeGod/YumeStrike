package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.ValidationPercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.EnumNumber;
import org.apache.fop.fo.properties.EnumProperty;
import org.apache.fop.fo.properties.NumberProperty;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.PropertyMaker;
import org.apache.fop.fo.properties.StructurePointerPropertySet;
import org.apache.fop.layoutmgr.table.CollapsingBorderModel;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class TableFObj extends FObj implements StructurePointerPropertySet {
   private Numeric borderAfterPrecedence;
   private Numeric borderBeforePrecedence;
   private Numeric borderEndPrecedence;
   private Numeric borderStartPrecedence;
   private String ptr;
   ConditionalBorder borderBefore;
   ConditionalBorder borderAfter;
   BorderSpecification borderStart;
   BorderSpecification borderEnd;
   CollapsingBorderModel collapsingBorderModel;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public TableFObj(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.borderAfterPrecedence = pList.get(20).getNumeric();
      this.borderBeforePrecedence = pList.get(24).getNumeric();
      this.borderEndPrecedence = pList.get(34).getNumeric();
      this.borderStartPrecedence = pList.get(48).getNumeric();
      this.ptr = pList.get(274).getString();
      if (this.getNameId() != 71 && this.getNameId() != 75 && this.getCommonBorderPaddingBackground().hasPadding(ValidationPercentBaseContext.getPseudoContext())) {
         TableEventProducer eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.paddingNotApplicable(this, this.getName(), this.getLocator());
      }

   }

   public Numeric getBorderPrecedence(int side) {
      switch (side) {
         case 0:
            return this.borderBeforePrecedence;
         case 1:
            return this.borderAfterPrecedence;
         case 2:
            return this.borderStartPrecedence;
         case 3:
            return this.borderEndPrecedence;
         default:
            return null;
      }
   }

   public Table getTable() {
      return ((TableFObj)this.parent).getTable();
   }

   public abstract CommonBorderPaddingBackground getCommonBorderPaddingBackground();

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      super.processNode(elementName, locator, attlist, pList);
      Table table = this.getTable();
      if (!this.inMarker() && !table.isSeparateBorderModel()) {
         this.collapsingBorderModel = CollapsingBorderModel.getBorderModelFor(table.getBorderCollapse());
         this.setCollapsedBorders();
      }

   }

   public String getPtr() {
      return this.ptr;
   }

   protected void setCollapsedBorders() {
      this.createBorder(2);
      this.createBorder(3);
      this.createBorder(0);
      this.createBorder(1);
   }

   private void createBorder(int side) {
      BorderSpecification borderSpec = new BorderSpecification(this.getCommonBorderPaddingBackground().getBorderInfo(side), this.getNameId());
      switch (side) {
         case 0:
            this.borderBefore = new ConditionalBorder(borderSpec, this.collapsingBorderModel);
            break;
         case 1:
            this.borderAfter = new ConditionalBorder(borderSpec, this.collapsingBorderModel);
            break;
         case 2:
            this.borderStart = borderSpec;
            break;
         case 3:
            this.borderEnd = borderSpec;
            break;
         default:
            if (!$assertionsDisabled) {
               throw new AssertionError();
            }
      }

   }

   static {
      $assertionsDisabled = !TableFObj.class.desiredAssertionStatus();
   }

   public static class ColumnNumberPropertyMaker extends PropertyMaker {
      public ColumnNumberPropertyMaker(int propId) {
         super(propId);
      }

      public Property make(PropertyList propertyList) throws PropertyException {
         FObj fo = propertyList.getFObj();
         return NumberProperty.getInstance(((ColumnNumberManagerHolder)fo.getParent()).getColumnNumberManager().getCurrentColumnNumber());
      }

      public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
         Property p = super.make(propertyList, value, fo);
         int columnIndex = p.getNumeric().getValue();
         int colSpan = propertyList.get(165).getNumeric().getValue();
         int foId = propertyList.getFObj().getNameId();
         if (foId == 76 || foId == 75) {
            ColumnNumberManagerHolder parent = (ColumnNumberManagerHolder)propertyList.getParentFObj();
            ColumnNumberManager columnIndexManager = parent.getColumnNumberManager();
            int lastIndex = columnIndex - 1 + colSpan;

            for(int i = columnIndex; i <= lastIndex; ++i) {
               if (columnIndexManager.isColumnNumberUsed(i)) {
                  TableEventProducer eventProducer = TableEventProducer.Provider.get(fo.getUserAgent().getEventBroadcaster());
                  eventProducer.cellOverlap(this, propertyList.getFObj().getName(), i, propertyList.getFObj().getLocator());
               }
            }
         }

         return p;
      }

      public Property convertProperty(Property p, PropertyList propertyList, FObj fo) throws PropertyException {
         if (p instanceof EnumProperty) {
            return EnumNumber.getInstance(p);
         } else {
            Number val = p.getNumber();
            if (val == null) {
               return this.convertPropertyDatatype(p, propertyList, fo);
            } else {
               int i = Math.round(val.floatValue());
               int foId = propertyList.getFObj().getNameId();
               if (i <= 0) {
                  if (foId != 75 && foId != 76) {
                     i = 1;
                  } else {
                     ColumnNumberManagerHolder parent = (ColumnNumberManagerHolder)propertyList.getParentFObj();
                     ColumnNumberManager columnIndexManager = parent.getColumnNumberManager();
                     i = columnIndexManager.getCurrentColumnNumber();
                  }

                  TableEventProducer eventProducer = TableEventProducer.Provider.get(fo.getUserAgent().getEventBroadcaster());
                  eventProducer.forceNextColumnNumber(this, propertyList.getFObj().getName(), val, i, propertyList.getFObj().getLocator());
               }

               return NumberProperty.getInstance(i);
            }
         }
      }
   }
}
