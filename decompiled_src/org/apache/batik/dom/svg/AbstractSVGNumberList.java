package org.apache.batik.dom.svg;

import org.apache.batik.parser.NumberListHandler;
import org.apache.batik.parser.NumberListParser;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGNumberList;

public abstract class AbstractSVGNumberList extends AbstractSVGList implements SVGNumberList {
   public static final String SVG_NUMBER_LIST_SEPARATOR = " ";

   protected String getItemSeparator() {
      return " ";
   }

   protected abstract SVGException createSVGException(short var1, String var2, Object[] var3);

   protected abstract Element getElement();

   protected AbstractSVGNumberList() {
   }

   public SVGNumber initialize(SVGNumber var1) throws DOMException, SVGException {
      return (SVGNumber)this.initializeImpl(var1);
   }

   public SVGNumber getItem(int var1) throws DOMException {
      return (SVGNumber)this.getItemImpl(var1);
   }

   public SVGNumber insertItemBefore(SVGNumber var1, int var2) throws DOMException, SVGException {
      return (SVGNumber)this.insertItemBeforeImpl(var1, var2);
   }

   public SVGNumber replaceItem(SVGNumber var1, int var2) throws DOMException, SVGException {
      return (SVGNumber)this.replaceItemImpl(var1, var2);
   }

   public SVGNumber removeItem(int var1) throws DOMException {
      return (SVGNumber)this.removeItemImpl(var1);
   }

   public SVGNumber appendItem(SVGNumber var1) throws DOMException, SVGException {
      return (SVGNumber)this.appendItemImpl(var1);
   }

   protected SVGItem createSVGItem(Object var1) {
      SVGNumber var2 = (SVGNumber)var1;
      return new SVGNumberItem(var2.getValue());
   }

   protected void doParse(String var1, ListHandler var2) throws ParseException {
      NumberListParser var3 = new NumberListParser();
      NumberListBuilder var4 = new NumberListBuilder(var2);
      var3.setNumberListHandler(var4);
      var3.parse(var1);
   }

   protected void checkItemType(Object var1) throws SVGException {
      if (!(var1 instanceof SVGNumber)) {
         this.createSVGException((short)0, "expected SVGNumber", (Object[])null);
      }

   }

   protected class NumberListBuilder implements NumberListHandler {
      protected ListHandler listHandler;
      protected float currentValue;

      public NumberListBuilder(ListHandler var2) {
         this.listHandler = var2;
      }

      public void startNumberList() throws ParseException {
         this.listHandler.startList();
      }

      public void startNumber() throws ParseException {
         this.currentValue = 0.0F;
      }

      public void numberValue(float var1) throws ParseException {
         this.currentValue = var1;
      }

      public void endNumber() throws ParseException {
         this.listHandler.item(AbstractSVGNumberList.this.new SVGNumberItem(this.currentValue));
      }

      public void endNumberList() throws ParseException {
         this.listHandler.endList();
      }
   }

   protected class SVGNumberItem extends AbstractSVGNumber implements SVGItem {
      protected AbstractSVGList parentList;

      public SVGNumberItem(float var2) {
         this.value = var2;
      }

      public String getValueAsString() {
         return Float.toString(this.value);
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
