package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.anim.values.AnimatableNumberListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGNumber;
import org.w3c.dom.svg.SVGNumberList;

public class SVGOMAnimatedNumberList extends AbstractSVGAnimatedValue implements SVGAnimatedNumberList {
   protected BaseSVGNumberList baseVal;
   protected AnimSVGNumberList animVal;
   protected boolean changing;
   protected String defaultValue;
   protected boolean emptyAllowed;

   public SVGOMAnimatedNumberList(AbstractElement var1, String var2, String var3, String var4, boolean var5) {
      super(var1, var2, var3);
      this.defaultValue = var4;
      this.emptyAllowed = var5;
   }

   public SVGNumberList getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGNumberList();
      }

      return this.baseVal;
   }

   public SVGNumberList getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGNumberList();
      }

      return this.animVal;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGNumberList();
         }

         this.baseVal.revalidate();
         if (this.baseVal.missing) {
            throw new LiveAttributeException(this.element, this.localName, (short)0, (String)null);
         }

         if (this.baseVal.malformed) {
            throw new LiveAttributeException(this.element, this.localName, (short)1, this.baseVal.getValueAsString());
         }
      }

   }

   public AnimatableValue getUnderlyingValue(AnimationTarget var1) {
      SVGNumberList var2 = this.getBaseVal();
      int var3 = var2.getNumberOfItems();
      float[] var4 = new float[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var2.getItem(var3).getValue();
      }

      return new AnimatableNumberListValue(var1, var4);
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatableNumberListValue var2 = (AnimatableNumberListValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGNumberList();
         }

         this.animVal.setAnimatedValue(var2.getNumbers());
      }

      this.fireAnimatedAttributeListeners();
   }

   public void attrAdded(Attr var1, String var2) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrModified(Attr var1, String var2, String var3) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   public void attrRemoved(Attr var1, String var2) {
      if (!this.changing && this.baseVal != null) {
         this.baseVal.invalidate();
      }

      this.fireBaseAttributeListeners();
      if (!this.hasAnimVal) {
         this.fireAnimatedAttributeListeners();
      }

   }

   protected class AnimSVGNumberList extends AbstractSVGNumberList {
      public AnimSVGNumberList() {
         this.itemList = new ArrayList(1);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedNumberList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedNumberList.this.element).createSVGException(var1, var2, var3);
      }

      protected Element getElement() {
         return SVGOMAnimatedNumberList.this.element;
      }

      public int getNumberOfItems() {
         return SVGOMAnimatedNumberList.this.hasAnimVal ? super.getNumberOfItems() : SVGOMAnimatedNumberList.this.getBaseVal().getNumberOfItems();
      }

      public SVGNumber getItem(int var1) throws DOMException {
         return SVGOMAnimatedNumberList.this.hasAnimVal ? super.getItem(var1) : SVGOMAnimatedNumberList.this.getBaseVal().getItem(var1);
      }

      protected String getValueAsString() {
         if (this.itemList.size() == 0) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer(this.itemList.size() * 8);
            Iterator var2 = this.itemList.iterator();
            if (var2.hasNext()) {
               var1.append(((SVGItem)var2.next()).getValueAsString());
            }

            while(var2.hasNext()) {
               var1.append(this.getItemSeparator());
               var1.append(((SVGItem)var2.next()).getValueAsString());
            }

            return var1.toString();
         }
      }

      protected void setAttributeValue(String var1) {
      }

      public void clear() throws DOMException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      public SVGNumber initialize(SVGNumber var1) throws DOMException, SVGException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      public SVGNumber insertItemBefore(SVGNumber var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      public SVGNumber replaceItem(SVGNumber var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      public SVGNumber removeItem(int var1) throws DOMException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      public SVGNumber appendItem(SVGNumber var1) throws DOMException {
         throw SVGOMAnimatedNumberList.this.element.createDOMException((short)7, "readonly.number.list", (Object[])null);
      }

      protected void setAnimatedValue(float[] var1) {
         int var2 = this.itemList.size();

         int var3;
         for(var3 = 0; var3 < var2 && var3 < var1.length; ++var3) {
            AbstractSVGNumberList.SVGNumberItem var4 = (AbstractSVGNumberList.SVGNumberItem)this.itemList.get(var3);
            var4.value = var1[var3];
         }

         while(var3 < var1.length) {
            this.appendItemImpl(new AbstractSVGNumberList.SVGNumberItem(var1[var3]));
            ++var3;
         }

         while(var2 > var1.length) {
            --var2;
            this.removeItemImpl(var2);
         }

      }

      protected void resetAttribute() {
      }

      protected void resetAttribute(SVGItem var1) {
      }

      protected void revalidate() {
         this.valid = true;
      }
   }

   public class BaseSVGNumberList extends AbstractSVGNumberList {
      protected boolean missing;
      protected boolean malformed;

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedNumberList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedNumberList.this.element).createSVGException(var1, var2, var3);
      }

      protected Element getElement() {
         return SVGOMAnimatedNumberList.this.element;
      }

      protected String getValueAsString() {
         Attr var1 = SVGOMAnimatedNumberList.this.element.getAttributeNodeNS(SVGOMAnimatedNumberList.this.namespaceURI, SVGOMAnimatedNumberList.this.localName);
         return var1 == null ? SVGOMAnimatedNumberList.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedNumberList.this.changing = true;
            SVGOMAnimatedNumberList.this.element.setAttributeNS(SVGOMAnimatedNumberList.this.namespaceURI, SVGOMAnimatedNumberList.this.localName, var1);
         } finally {
            SVGOMAnimatedNumberList.this.changing = false;
         }

      }

      protected void resetAttribute() {
         super.resetAttribute();
         this.missing = false;
         this.malformed = false;
      }

      protected void resetAttribute(SVGItem var1) {
         super.resetAttribute(var1);
         this.missing = false;
         this.malformed = false;
      }

      protected void revalidate() {
         if (!this.valid) {
            this.valid = true;
            this.missing = false;
            this.malformed = false;
            String var1 = this.getValueAsString();
            boolean var2 = var1 != null && var1.length() == 0;
            if (var1 != null && (!var2 || SVGOMAnimatedNumberList.this.emptyAllowed)) {
               if (var2) {
                  this.itemList = new ArrayList(1);
               } else {
                  try {
                     AbstractSVGList.ListBuilder var3 = new AbstractSVGList.ListBuilder();
                     this.doParse(var1, var3);
                     if (var3.getList() != null) {
                        this.clear(this.itemList);
                     }

                     this.itemList = var3.getList();
                  } catch (ParseException var4) {
                     this.itemList = new ArrayList(1);
                     this.valid = true;
                     this.malformed = true;
                  }
               }

            } else {
               this.missing = true;
            }
         }
      }
   }
}
