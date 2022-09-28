package org.apache.batik.dom.svg;

import org.apache.batik.parser.LengthListHandler;
import org.apache.batik.parser.LengthListParser;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGLengthList;

public abstract class AbstractSVGLengthList extends AbstractSVGList implements SVGLengthList {
   protected short direction;
   public static final String SVG_LENGTH_LIST_SEPARATOR = " ";

   protected String getItemSeparator() {
      return " ";
   }

   protected abstract SVGException createSVGException(short var1, String var2, Object[] var3);

   protected abstract Element getElement();

   protected AbstractSVGLengthList(short var1) {
      this.direction = var1;
   }

   public SVGLength initialize(SVGLength var1) throws DOMException, SVGException {
      return (SVGLength)this.initializeImpl(var1);
   }

   public SVGLength getItem(int var1) throws DOMException {
      return (SVGLength)this.getItemImpl(var1);
   }

   public SVGLength insertItemBefore(SVGLength var1, int var2) throws DOMException, SVGException {
      return (SVGLength)this.insertItemBeforeImpl(var1, var2);
   }

   public SVGLength replaceItem(SVGLength var1, int var2) throws DOMException, SVGException {
      return (SVGLength)this.replaceItemImpl(var1, var2);
   }

   public SVGLength removeItem(int var1) throws DOMException {
      return (SVGLength)this.removeItemImpl(var1);
   }

   public SVGLength appendItem(SVGLength var1) throws DOMException, SVGException {
      return (SVGLength)this.appendItemImpl(var1);
   }

   protected SVGItem createSVGItem(Object var1) {
      SVGLength var2 = (SVGLength)var1;
      return new SVGLengthItem(var2.getUnitType(), var2.getValueInSpecifiedUnits(), this.direction);
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      LengthListParser var3 = new LengthListParser();
      LengthListBuilder var4 = new LengthListBuilder(var2);
      var3.setLengthListHandler(var4);
      var3.parse(var1);
   }

   protected void checkItemType(Object var1) throws SVGException {
      if (!(var1 instanceof SVGLength)) {
         this.createSVGException((short)0, "expected.length", (Object[])null);
      }

   }

   protected class LengthListBuilder implements LengthListHandler {
      protected ListHandler listHandler;
      protected float currentValue;
      protected short currentType;

      public LengthListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startLengthList() throws ParseException {
         this.listHandler.startList();
      }

      public void startLength() throws ParseException {
         this.currentType = 1;
         this.currentValue = 0.0F;
      }

      public void lengthValue(float var1) throws ParseException {
         this.currentValue = var1;
      }

      public void em() throws ParseException {
         this.currentType = 3;
      }

      public void ex() throws ParseException {
         this.currentType = 4;
      }

      public void in() throws ParseException {
         this.currentType = 8;
      }

      public void cm() throws ParseException {
         this.currentType = 6;
      }

      public void mm() throws ParseException {
         this.currentType = 7;
      }

      public void pc() throws ParseException {
         this.currentType = 10;
      }

      public void pt() throws ParseException {
         this.currentType = 3;
      }

      public void px() throws ParseException {
         this.currentType = 5;
      }

      public void percentage() throws ParseException {
         this.currentType = 2;
      }

      public void endLength() throws ParseException {
         this.listHandler.item(AbstractSVGLengthList.this.new SVGLengthItem(this.currentType, this.currentValue, AbstractSVGLengthList.this.direction));
      }

      public void endLengthList() throws ParseException {
         this.listHandler.endList();
      }
   }

   protected class SVGLengthItem extends AbstractSVGLength implements SVGItem {
      protected AbstractSVGList parentList;

      public SVGLengthItem(short var2, float var3, short var4) {
         super(var4);
         this.unitType = var2;
         this.value = var3;
      }

      protected SVGOMElement getAssociatedElement() {
         return (SVGOMElement)AbstractSVGLengthList.this.getElement();
      }

      public void setParent(AbstractSVGList var1) {
         this.parentList = var1;
      }

      public AbstractSVGList getParent() {
         return this.parentList;
      }

      protected void reset() {
         if (this.parentList != null) {
            this.parentList.itemChanged();
         }

      }
   }
}
