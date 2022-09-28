package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.anim.values.AnimatableTransformListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedTransformList;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGTransform;
import org.w3c.dom.svg.SVGTransformList;

public class SVGOMAnimatedTransformList extends AbstractSVGAnimatedValue implements SVGAnimatedTransformList {
   protected BaseSVGTransformList baseVal;
   protected AnimSVGTransformList animVal;
   protected boolean changing;
   protected String defaultValue;

   public SVGOMAnimatedTransformList(AbstractElement var1, String var2, String var3, String var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public SVGTransformList getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGTransformList();
      }

      return this.baseVal;
   }

   public SVGTransformList getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGTransformList();
      }

      return this.animVal;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGTransformList();
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
      SVGTransformList var2 = this.getBaseVal();
      int var3 = var2.getNumberOfItems();
      ArrayList var4 = new ArrayList(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         var4.add(var2.getItem(var5));
      }

      return new AnimatableTransformListValue(var1, var4);
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatableTransformListValue var2 = (AnimatableTransformListValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGTransformList();
         }

         this.animVal.setAnimatedValue(var2.getTransforms());
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

   protected class AnimSVGTransformList extends AbstractSVGTransformList {
      public AnimSVGTransformList() {
         this.itemList = new ArrayList(1);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedTransformList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedTransformList.this.element).createSVGException(var1, var2, var3);
      }

      public int getNumberOfItems() {
         return SVGOMAnimatedTransformList.this.hasAnimVal ? super.getNumberOfItems() : SVGOMAnimatedTransformList.this.getBaseVal().getNumberOfItems();
      }

      public SVGTransform getItem(int var1) throws DOMException {
         return SVGOMAnimatedTransformList.this.hasAnimVal ? super.getItem(var1) : SVGOMAnimatedTransformList.this.getBaseVal().getItem(var1);
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
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform initialize(SVGTransform var1) throws DOMException, SVGException {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform insertItemBefore(SVGTransform var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform replaceItem(SVGTransform var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform removeItem(int var1) throws DOMException {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform appendItem(SVGTransform var1) throws DOMException {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      public SVGTransform consolidate() {
         throw SVGOMAnimatedTransformList.this.element.createDOMException((short)7, "readonly.transform.list", (Object[])null);
      }

      protected void setAnimatedValue(Iterator var1) {
         int var2 = this.itemList.size();

         int var3;
         for(var3 = 0; var3 < var2 && var1.hasNext(); ++var3) {
            AbstractSVGTransformList.SVGTransformItem var4 = (AbstractSVGTransformList.SVGTransformItem)this.itemList.get(var3);
            var4.assign((SVGTransform)var1.next());
         }

         while(var1.hasNext()) {
            this.appendItemImpl(new AbstractSVGTransformList.SVGTransformItem((SVGTransform)var1.next()));
            ++var3;
         }

         while(var2 > var3) {
            --var2;
            this.removeItemImpl(var2);
         }

      }

      protected void setAnimatedValue(SVGTransform var1) {
         int var2 = this.itemList.size();

         while(var2 > 1) {
            --var2;
            this.removeItemImpl(var2);
         }

         if (var2 == 0) {
            this.appendItemImpl(new AbstractSVGTransformList.SVGTransformItem(var1));
         } else {
            AbstractSVGTransformList.SVGTransformItem var3 = (AbstractSVGTransformList.SVGTransformItem)this.itemList.get(0);
            var3.assign(var1);
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

   public class BaseSVGTransformList extends AbstractSVGTransformList {
      protected boolean missing;
      protected boolean malformed;

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedTransformList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedTransformList.this.element).createSVGException(var1, var2, var3);
      }

      protected String getValueAsString() {
         Attr var1 = SVGOMAnimatedTransformList.this.element.getAttributeNodeNS(SVGOMAnimatedTransformList.this.namespaceURI, SVGOMAnimatedTransformList.this.localName);
         return var1 == null ? SVGOMAnimatedTransformList.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedTransformList.this.changing = true;
            SVGOMAnimatedTransformList.this.element.setAttributeNS(SVGOMAnimatedTransformList.this.namespaceURI, SVGOMAnimatedTransformList.this.localName, var1);
         } finally {
            SVGOMAnimatedTransformList.this.changing = false;
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
            if (var1 == null) {
               this.missing = true;
            } else {
               try {
                  AbstractSVGList.ListBuilder var2 = new AbstractSVGList.ListBuilder();
                  this.doParse(var1, var2);
                  if (var2.getList() != null) {
                     this.clear(this.itemList);
                  }

                  this.itemList = var2.getList();
               } catch (ParseException var3) {
                  this.itemList = new ArrayList(1);
                  this.malformed = true;
               }

            }
         }
      }
   }
}
