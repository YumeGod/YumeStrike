package org.apache.batik.dom.svg;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.batik.anim.values.AnimatablePointListValue;
import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;
import org.apache.batik.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGAnimatedPoints;
import org.w3c.dom.svg.SVGException;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;

public class SVGOMAnimatedPoints extends AbstractSVGAnimatedValue implements SVGAnimatedPoints {
   protected BaseSVGPointList baseVal;
   protected AnimSVGPointList animVal;
   protected boolean changing;
   protected String defaultValue;

   public SVGOMAnimatedPoints(AbstractElement var1, String var2, String var3, String var4) {
      super(var1, var2, var3);
      this.defaultValue = var4;
   }

   public SVGPointList getPoints() {
      if (this.baseVal == null) {
         this.baseVal = new BaseSVGPointList();
      }

      return this.baseVal;
   }

   public SVGPointList getAnimatedPoints() {
      if (this.animVal == null) {
         this.animVal = new AnimSVGPointList();
      }

      return this.animVal;
   }

   public void check() {
      if (!this.hasAnimVal) {
         if (this.baseVal == null) {
            this.baseVal = new BaseSVGPointList();
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
      SVGPointList var2 = this.getPoints();
      int var3 = var2.getNumberOfItems();
      float[] var4 = new float[var3 * 2];

      for(int var5 = 0; var5 < var3; ++var5) {
         SVGPoint var6 = var2.getItem(var5);
         var4[var5 * 2] = var6.getX();
         var4[var5 * 2 + 1] = var6.getY();
      }

      return new AnimatablePointListValue(var1, var4);
   }

   protected void updateAnimatedValue(AnimatableValue var1) {
      if (var1 == null) {
         this.hasAnimVal = false;
      } else {
         this.hasAnimVal = true;
         AnimatablePointListValue var2 = (AnimatablePointListValue)var1;
         if (this.animVal == null) {
            this.animVal = new AnimSVGPointList();
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

   protected class AnimSVGPointList extends AbstractSVGPointList {
      public AnimSVGPointList() {
         this.itemList = new ArrayList(1);
      }

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPoints.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedPoints.this.element).createSVGException(var1, var2, var3);
      }

      public int getNumberOfItems() {
         return SVGOMAnimatedPoints.this.hasAnimVal ? super.getNumberOfItems() : SVGOMAnimatedPoints.this.getPoints().getNumberOfItems();
      }

      public SVGPoint getItem(int var1) throws DOMException {
         return SVGOMAnimatedPoints.this.hasAnimVal ? super.getItem(var1) : SVGOMAnimatedPoints.this.getPoints().getItem(var1);
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
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      public SVGPoint initialize(SVGPoint var1) throws DOMException, SVGException {
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      public SVGPoint insertItemBefore(SVGPoint var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      public SVGPoint replaceItem(SVGPoint var1, int var2) throws DOMException, SVGException {
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      public SVGPoint removeItem(int var1) throws DOMException {
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      public SVGPoint appendItem(SVGPoint var1) throws DOMException {
         throw SVGOMAnimatedPoints.this.element.createDOMException((short)7, "readonly.point.list", (Object[])null);
      }

      protected void setAnimatedValue(float[] var1) {
         int var2 = this.itemList.size();

         int var3;
         for(var3 = 0; var3 < var2 && var3 < var1.length / 2; ++var3) {
            AbstractSVGPointList.SVGPointItem var4 = (AbstractSVGPointList.SVGPointItem)this.itemList.get(var3);
            var4.x = var1[var3 * 2];
            var4.y = var1[var3 * 2 + 1];
         }

         while(var3 < var1.length / 2) {
            this.appendItemImpl(new AbstractSVGPointList.SVGPointItem(var1[var3 * 2], var1[var3 * 2 + 1]));
            ++var3;
         }

         while(var2 > var1.length / 2) {
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

   protected class BaseSVGPointList extends AbstractSVGPointList {
      protected boolean missing;
      protected boolean malformed;

      protected DOMException createDOMException(short var1, String var2, Object[] var3) {
         return SVGOMAnimatedPoints.this.element.createDOMException(var1, var2, var3);
      }

      protected SVGException createSVGException(short var1, String var2, Object[] var3) {
         return ((SVGOMElement)SVGOMAnimatedPoints.this.element).createSVGException(var1, var2, var3);
      }

      protected String getValueAsString() {
         Attr var1 = SVGOMAnimatedPoints.this.element.getAttributeNodeNS(SVGOMAnimatedPoints.this.namespaceURI, SVGOMAnimatedPoints.this.localName);
         return var1 == null ? SVGOMAnimatedPoints.this.defaultValue : var1.getValue();
      }

      protected void setAttributeValue(String var1) {
         try {
            SVGOMAnimatedPoints.this.changing = true;
            SVGOMAnimatedPoints.this.element.setAttributeNS(SVGOMAnimatedPoints.this.namespaceURI, SVGOMAnimatedPoints.this.localName, var1);
         } finally {
            SVGOMAnimatedPoints.this.changing = false;
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
