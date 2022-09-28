package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.anim.values.AnimatableLengthListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGLengthList;

public class SVGOMAnimatedLengthList extends AbstractSVGAnimatedValue implements SVGAnimatedLengthList {
   protected BaseSVGLengthList baseVal;
   protected AnimSVGLengthList animVal;
   protected boolean changing;
   protected String defaultValue;
   protected boolean emptyAllowed;
   protected short direction;

   public SVGOMAnimatedLengthList(AbstractElement var1, String var2, String var3, String var4, boolean var5, short var6) {
      super(var1, var2, var3);
      this.defaultValue = var4;
      this.emptyAllowed = var5;
      this.direction = var6;
   }

   public SVGLengthList getBaseVal() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGLengthList();
      }

      return this.baseVal;
   }

   public SVGLengthList getAnimVal() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGLengthList();
      }

      return this.animVal;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGLengthList();
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
      SVGLengthList var2 = this.getBaseVal();
      int var3 = var2.getNumberOfItems();
      short[] var4 = new short[var3];
      float[] var5 = new float[var3];

      for(int var6 = 0; var6 < var3; ++var6) {
         SVGLength var7 = var2.getItem(var6);
         var4[var6] = var7.getUnitType();
         var5[var6] = var7.getValueInSpecifiedUnits();
      }

      return new AnimatableLengthListValue(var1, var4, var5, var1.getPercentageInterpretation(this.getNamespaceURI(), this.getLocalName(), false));
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatableLengthListValue var2 = (AnimatableLengthListValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGLengthList();
         }

         this.animVal.setAnimatedValue(var2.getLengthTypes(), var2.getLengthValues());
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

   protected class AnimSVGLengthList extends AbstractSVGLengthList {
      public AnimSVGLengthList() {
         super(SVGOMAnimatedLengthList.this.direction);
         this.itemList = new ArrayList(1);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedLengthList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedLengthList.this.element).createSVGException(var1, var2, var3);
      }

      protected Element getElement() {
         return SVGOMAnimatedLengthList.this.element;
      }

      public int getNumberOfItems() {
         return SVGOMAnimatedLengthList.this.hasAnimVal ? super.getNumberOfItems() : SVGOMAnimatedLengthList.this.getBaseVal().getNumberOfItems();
      }

      public SVGLength getItem(int var1) throws DOMException {
         return SVGOMAnimatedLengthList.this.hasAnimVal ? super.getItem(var1) : SVGOMAnimatedLengthList.this.getBaseVal().getItem(var1);
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
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      public SVGLength initialize(SVGLength var1) throws DOMException, SVGException {
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      public SVGLength insertItemBefore(SVGLength var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      public SVGLength replaceItem(SVGLength var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      public SVGLength removeItem(int var1) throws DOMException {
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      public SVGLength appendItem(SVGLength var1) throws DOMException {
         throw SVGOMAnimatedLengthList.this.element.createDOMException((short)7, "readonly.length.list", (Object[])null);
      }

      protected void setAnimatedValue(short[] var1, float[] var2) {
         int var3 = this.itemList.size();

         int var4;
         for(var4 = 0; var4 < var3 && var4 < var1.length; ++var4) {
            AbstractSVGLengthList.SVGLengthItem var5 = (AbstractSVGLengthList.SVGLengthItem)this.itemList.get(var4);
            var5.unitType = var1[var4];
            var5.value = var2[var4];
            var5.direction = this.direction;
         }

         while(var4 < var1.length) {
            this.appendItemImpl(new AbstractSVGLengthList.SVGLengthItem(var1[var4], var2[var4], this.direction));
            ++var4;
         }

         while(var3 > var1.length) {
            --var3;
            this.removeItemImpl(var3);
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

   public class BaseSVGLengthList extends AbstractSVGLengthList {
      protected boolean missing;
      protected boolean malformed;

      public BaseSVGLengthList() {
         super(SVGOMAnimatedLengthList.this.direction);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedLengthList.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedLengthList.this.element).createSVGException(var1, var2, var3);
      }

      protected Element getElement() {
         return SVGOMAnimatedLengthList.this.element;
      }

      protected String getValueAsString() {
         Attr var1 = SVGOMAnimatedLengthList.this.element.getAttributeNodeNS(SVGOMAnimatedLengthList.this.namespaceURI, SVGOMAnimatedLengthList.this.localName);
         return var1 == null ? SVGOMAnimatedLengthList.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedLengthList.this.changing = true;
            SVGOMAnimatedLengthList.this.element.setAttributeNS(SVGOMAnimatedLengthList.this.namespaceURI, SVGOMAnimatedLengthList.this.localName, var1);
         } finally {
            SVGOMAnimatedLengthList.this.changing = false;
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
            if (var1 != null && (!var2 || SVGOMAnimatedLengthList.this.emptyAllowed)) {
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
