package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGException;

public abstract class AbstractSVGList {
   protected boolean valid;
   protected List itemList;

   protected abstract String getItemSeparator();

   protected abstract SVGItem createSVGItem(Object var1);

   protected abstract void doParse(String var1, ListHandler var2) throws ParseException;

   protected abstract void checkItemType(Object var1) throws SVGException;

   protected abstract String getValueAsString();

   protected abstract void setAttributeValue(String var1);

   protected abstract DOMException createDOMException(short var1, String var2, Object[] var3);

   public int getNumberOfItems() {
      this.revalidate();
      return this.itemList != null ? this.itemList.size() : 0;
   }

   public void clear() throws DOMException {
      this.revalidate();
      if (this.itemList != null) {
         this.clear(this.itemList);
         this.resetAttribute();
      }

   }

   protected SVGItem initializeImpl(Object var1) throws DOMException, SVGException {
      this.checkItemType(var1);
      if (this.itemList == null) {
         this.itemList = new ArrayList(1);
      } else {
         this.clear(this.itemList);
      }

      SVGItem var2 = this.removeIfNeeded(var1);
      this.itemList.add(var2);
      var2.setParent(this);
      this.resetAttribute();
      return var2;
   }

   protected SVGItem getItemImpl(int var1) throws DOMException {
      this.revalidate();
      if (var1 >= 0 && this.itemList != null && var1 < this.itemList.size()) {
         return (SVGItem)this.itemList.get(var1);
      } else {
         throw this.createDOMException((short)1, "index.out.of.bounds", new Object[]{new Integer(var1)});
      }
   }

   protected SVGItem insertItemBeforeImpl(Object var1, int var2) throws DOMException, SVGException {
      this.checkItemType(var1);
      this.revalidate();
      if (var2 < 0) {
         throw this.createDOMException((short)1, "index.out.of.bounds", new Object[]{new Integer(var2)});
      } else {
         if (var2 > this.itemList.size()) {
            var2 = this.itemList.size();
         }

         SVGItem var3 = this.removeIfNeeded(var1);
         this.itemList.add(var2, var3);
         var3.setParent(this);
         this.resetAttribute();
         return var3;
      }
   }

   protected SVGItem replaceItemImpl(Object var1, int var2) throws DOMException, SVGException {
      this.checkItemType(var1);
      this.revalidate();
      if (var2 >= 0 && var2 < this.itemList.size()) {
         SVGItem var3 = this.removeIfNeeded(var1);
         this.itemList.set(var2, var3);
         var3.setParent(this);
         this.resetAttribute();
         return var3;
      } else {
         throw this.createDOMException((short)1, "index.out.of.bounds", new Object[]{new Integer(var2)});
      }
   }

   protected SVGItem removeItemImpl(int var1) throws DOMException {
      this.revalidate();
      if (var1 >= 0 && var1 < this.itemList.size()) {
         SVGItem var2 = (SVGItem)this.itemList.remove(var1);
         var2.setParent((AbstractSVGList)null);
         this.resetAttribute();
         return var2;
      } else {
         throw this.createDOMException((short)1, "index.out.of.bounds", new Object[]{new Integer(var1)});
      }
   }

   protected SVGItem appendItemImpl(Object var1) throws DOMException, SVGException {
      this.checkItemType(var1);
      this.revalidate();
      SVGItem var2 = this.removeIfNeeded(var1);
      this.itemList.add(var2);
      var2.setParent(this);
      if (this.itemList.size() <= 1) {
         this.resetAttribute();
      } else {
         this.resetAttribute(var2);
      }

      return var2;
   }

   protected SVGItem removeIfNeeded(Object var1) {
      SVGItem var2;
      if (var1 instanceof SVGItem) {
         var2 = (SVGItem)var1;
         if (var2.getParent() != null) {
            var2.getParent().removeItem(var2);
         }
      } else {
         var2 = this.createSVGItem(var1);
      }

      return var2;
   }

   protected void revalidate() {
      if (!this.valid) {
         try {
            ListBuilder var1 = new ListBuilder();
            this.doParse(this.getValueAsString(), var1);
            List var2 = var1.getList();
            if (var2 != null) {
               this.clear(this.itemList);
            }

            this.itemList = var2;
         } catch (ParseException var3) {
            this.itemList = null;
         }

         this.valid = true;
      }
   }

   protected void setValueAsString(List var1) throws DOMException {
      String var2 = null;
      Iterator var3 = var1.iterator();
      if (var3.hasNext()) {
         SVGItem var4 = (SVGItem)var3.next();
         StringBuffer var5 = new StringBuffer(var1.size() * 8);
         var5.append(var4.getValueAsString());

         while(var3.hasNext()) {
            var4 = (SVGItem)var3.next();
            var5.append(this.getItemSeparator());
            var5.append(var4.getValueAsString());
         }

         var2 = var5.toString();
      }

      this.setAttributeValue(var2);
      this.valid = true;
   }

   public void itemChanged() {
      this.resetAttribute();
   }

   protected void resetAttribute() {
      this.setValueAsString(this.itemList);
   }

   protected void resetAttribute(SVGItem var1) {
      String var2 = this.getValueAsString() + this.getItemSeparator() + var1.getValueAsString();
      this.setAttributeValue(var2);
      this.valid = true;
   }

   public void invalidate() {
      this.valid = false;
   }

   protected void removeItem(SVGItem var1) {
      if (this.itemList.contains(var1)) {
         this.itemList.remove(var1);
         var1.setParent((AbstractSVGList)null);
         this.resetAttribute();
      }

   }

   protected void clear(List var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            SVGItem var3 = (SVGItem)var2.next();
            var3.setParent((AbstractSVGList)null);
         }

         var1.clear();
      }
   }

   protected class ListBuilder implements ListHandler {
      protected List list;

      public List getList() {
         return this.list;
      }

      public void startList() {
         this.list = new ArrayList();
      }

      public void item(SVGItem var1) {
         var1.setParent(AbstractSVGList.this);
         this.list.add(var1);
      }

      public void endList() {
      }
   }
}
